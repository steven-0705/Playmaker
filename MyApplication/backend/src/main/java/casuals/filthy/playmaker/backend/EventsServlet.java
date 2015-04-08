package casuals.filthy.playmaker.backend;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.gson.Gson;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.EventData;
import data.GroupData;

import static casuals.filthy.playmaker.backend.OfyService.ofy;

/**
 * Created by Chris on 3/27/2015.
 */
public class EventsServlet extends HttpServlet {


    public static Gson gson = new Gson();

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String userId = req.getParameter("user_id");
        String groupIdString = req.getParameter("group_id");
        String name = req.getParameter("event_name");
        String type = req.getParameter("event_type");
        String dateString = req.getParameter("event_date");

        if (userId == null || groupIdString == null || name == null || type == null || dateString == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "missing required field: user_id, group_id, event_name, event_type, event_date");
            return;
        }
        long groupId = Long.parseLong(groupIdString);
        long date = Long.parseLong(dateString);

        // get the group
        GroupData group = ofy().load().type(GroupData.class).id(groupId).now();
        if (group == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "group not found");
            return;
        }

        // check for admin rights
        if (!group.isUserAdmin(userId)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "user is not group admin");
            return;
        }

        // create the event
        long id = DatastoreServiceFactory.getDatastoreService().allocateIds("event", 1).getStart().getId();
        EventData event = new EventData(id, date, type);

        group.addEvent(event);

        // put the data back
        ofy().save().entities(event, group).now();

        // respond
        String groupJson = gson.toJson(event);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.getWriter().write(groupJson);
        resp.getWriter().flush();
        resp.getWriter().close();
    }
}
