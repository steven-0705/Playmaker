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
import java.util.logging.Logger;

import javax.servlet.http.*;

import data.DataObject;
import data.GroupData;
import data.UserData;

import static casuals.filthy.playmaker.backend.OfyService.ofy;

public class UsersServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(UsersServlet.class.getName());
    private static Gson gson = new Gson();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String userName = req.getParameter("user_name");
        String userEmail = req.getParameter("user_email");
        String userId = req.getParameter("user_id");

        // lookup the user if the id was not specified
        if (userId == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "missing user_id parameter");
            return;
        }

        // get the User data
        UserData user = ofy().load().type(UserData.class).id(userId).now();
        if (user == null) {
            doPut(req, resp);
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

    @Override
     public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String userId = req.getParameter("user_id");
        if (userId == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "missing user_id field");
            return;
        }
        UserData user = ofy().load().type(UserData.class).id(userId).now();
        if (user == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "user not found");
            return;
        }

        ArrayList<DataObject> saves = new ArrayList<DataObject>();
        saves.add(user);

        String groupIdString = req.getParameter("group_id");
        if (groupIdString != null) {
            long groupId = Long.parseLong(groupIdString);
            GroupData group = ofy().load().type(GroupData.class).id(groupId).now();
            if (group == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "group not found");
                return;
            }

            //TODO add to the group side too
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

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Create a new user by force
        String name = req.getParameter("user_name");
        String email = req.getParameter("user_email");
        String userId = req.getParameter("user_id");

        if (name == null || email == null || userId == null) {
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
        UserData user = new UserData(email, name, userId);
        ofy().save().entity(user).now();

        // respond
        String userJson = gson.toJson(user);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.getWriter().write(userJson);
        resp.getWriter().flush();
        resp.getWriter().close();
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        // TODO this should probably be implemented at some point
        resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}
