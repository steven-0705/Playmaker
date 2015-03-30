/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package casuals.filthy.playmaker.backend;

import com.google.gson.Gson;
import com.googlecode.objectify.ObjectifyService;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.*;

import data.UserData;
import data.metadata.MetaDataObject;
import data.metadata.UserLookup;

import static com.googlecode.objectify.ObjectifyService.*;

public class UsersServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(UsersServlet.class.getName());
    private static Gson gson = new Gson();


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String userEmail = req.getParameter("user_email");
        String userIdString = req.getParameter("user_id");
        long userId;

        // lookup the user if the id was not specified
        if (userIdString == null) {
            UserLookup lookup = ofy().load().type(UserLookup.class).id(MetaDataObject.META_ID).now();
            if (lookup == null) {
                resp.setIntHeader("No lookup table found", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            userId = lookup.getUserId(userEmail);
        } else {
            userId = Long.parseLong(userIdString);
        }

        // get the User data
        UserData user = ofy().load().type(UserData.class).id(userId).now();
        if (user == null) {
            resp.setIntHeader("User not found", HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // convert to json
        String userJson = gson.toJson(user);

        // respond with information
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.getWriter().write(userJson);
        resp.getWriter().flush();
        resp.getWriter().close();
    }
}
