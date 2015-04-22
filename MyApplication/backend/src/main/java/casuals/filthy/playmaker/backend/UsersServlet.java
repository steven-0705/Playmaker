/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package casuals.filthy.playmaker.backend;

import com.google.gson.Gson;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.*;

import data.DataObject;
import data.GroupData;
import data.UserData;

import static casuals.filthy.playmaker.backend.OfyService.ofy;

public class UsersServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(UsersServlet.class.getName());
    private static Gson gson = new Gson();

    public void login(HashMap<String, String> params, HttpServletResponse resp) throws IOException {
        String userName = params.get("user_name");//req.getParameter("user_name");
        String userEmail = params.get("user_email");//req.getParameter("user_email");
        String userId = params.get("user_id");//req.getParameter("user_id");

        // lookup the user if the id was not specified
        if (userId == null && userEmail == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "missing user_id or user_email parameter");
            return;
        }

        // get the User data
        UserData user = null;
        if (userId != null)
            user = ofy().load().type(UserData.class).id(userId).now();
        else {
            List<UserData> users = ofy().load().type(UserData.class).list();
            for (UserData u: users) {
                if (userEmail.equals(u.getEmail())) {
                    user = u;
                    break;
                }
            }
        }

        // missing, create
        if (user == null) {
            if (userName == null || userEmail == null || userId == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters to create user");
                return;
            }

            // check if user exists
            UserData test = ofy().load().type(UserData.class).id(userId).now();
            if (test != null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "user already exists");
                return;
            }

            // create user
            user = new UserData(userEmail, userName, userId);
            ofy().save().entity(user).now();
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

     @Override
     public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HashMap<String, String> params = ServletUtils.getParams(req.getInputStream());

        String action = params.get("action");
        String userId = params.get("user_id");//req.getParameter("user_id");
        if (userId == null || action == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "missing user_id or action field");
            return;
        }

        if (action.equals("login")) {
            login(params, resp);
            return;
        } else if (action.equals("join")) {


            UserData user = ofy().load().type(UserData.class).id(userId).now();
            if (user == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "user not found");
                return;
            }

            ArrayList<DataObject> saves = new ArrayList<DataObject>();
            saves.add(user);

            String groupIdString = params.get("group_id");//req.getParameter("group_id");
            if (groupIdString != null) {
                long groupId = Long.parseLong(groupIdString);
                GroupData group = ofy().load().type(GroupData.class).id(groupId).now();
                if (group == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "group not found");
                    return;
                }

                group.addUser(user);
                user.addGroup(group);
                saves.add(group);
            }


            // done with all requests, save and return
            ofy().save().entities(saves).now();
            // respond
            String userJson = gson.toJson(user);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().write(userJson);
            resp.getWriter().flush();
            resp.getWriter().close();
        }
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        // TODO this should probably be implemented at some point
        resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}
