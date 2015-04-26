package casuals.filthy.playmaker.backend;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.EventData;
import data.GroupData;
import data.UserData;

import static casuals.filthy.playmaker.backend.OfyService.ofy;

/**
 * Created by Chris on 3/27/2015.
 */
public class EventsServlet extends HttpServlet {


    public static Gson gson = new Gson();

    /**
     * Create a new event
     * @param resp
     * @throws IOException
     */
    private void createEvent(HashMap<String, Object> params, HttpServletResponse resp) throws IOException {
        String userId = (String) params.get("user_id");
        String groupIdString = (String) params.get("group_id");
        String name = (String) params.get("event_name");
        String type = (String) params.get("event_type");
        List<Double> eventDates = (List<Double>) params.get("event_dates");
        String address = (String) params.get("event_address");
        String eventTeamsString = (String) params.get("event_teams");
        String autoGenString = (String) params.get("gen_teams");
        String closeString = (String) params.get("close");
        List<String> items = (List<String>) params.get("items");

        if (userId == null || groupIdString == null || closeString == null || eventTeamsString == null
                || name == null || autoGenString == null || type == null || eventDates == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "missing required field: user_id, group_id, event_name, event_type, event_date");
            return;
        }

        type = type.toLowerCase();

        long groupId = Long.parseLong(groupIdString);
        boolean autoGen = autoGenString.equals("true");
        long close = Long.parseLong(closeString);

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
        EventData event = new EventData(id, eventDates, type, group.getId(), name, address, autoGen, close);

        group.addEvent(event);

        if (eventTeamsString != null) {
            int numTeams = Integer.parseInt(eventTeamsString);
            event.setNumTeams(numTeams);
        }

        if (items != null) {
            event.addItems(items);
        }

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

    /**
     * Get an existing event
     * @param req
     * @param resp
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String groupIdString = req.getParameter("group_id");
        String eventIdString = req.getParameter("event_id");

        if (groupIdString == null || eventIdString == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "missing required field: group_id, event_id");
            return;
        }

        long groupId = Long.parseLong(groupIdString);
        long eventId = Long.parseLong(eventIdString);

        EventData event = ofy().load().type(EventData.class).id(eventId).now();
        if (event == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "event not found");
            return;
        }

        if (event.getGroupId() != groupId) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "incorrect group id");
            return;
        }

        event.checkPoll();

        // respond
        String groupJson = gson.toJson(event);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.getWriter().write(groupJson);
        resp.getWriter().flush();
        resp.getWriter().close();
    }

    /**
     * Make changes to an existing event
     * @param req
     * @param resp
     * @throws IOException
     */
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HashMap<String, Object> params = ServletUtils.getParams(req.getInputStream());
        String groupIdString = (String) params.get("group_id");//req.getParameter("group_id");
        String eventIdString = (String) params.get("event_id");//req.getParameter("event_id");

        // optional stuff
        String userId = (String) params.get("user_id");//req.getParameter("user_id");
        String name = (String) params.get("event_name");//req.getParameter("event_name");
        String type = (String) params.get("event_type");//req.getParameter("event_type");
        String dateString = (String) params.get("event_date");//req.getParameter("event_date");
        String address = (String) params.get("event_address");
        String action = (String) params.get("action");
        Double vote = (Double) params.get("vote");
        List<List<String>> teams = (List<List<String>>) params.get("teams");

        if (groupIdString == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "missing required field: group_id");
            return;
        }

        if (eventIdString == null) {
            // create a new event
            createEvent(params, resp);
            return;
        }

        long groupId = Long.parseLong(groupIdString);
        long eventId = Long.parseLong(eventIdString);

        EventData event = ofy().load().type(EventData.class).id(eventId).now();
        if (event == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "event not found");
            return;
        }
        GroupData group = ofy().load().type(GroupData.class).id(groupId).now();

        if (event.getGroupId() != groupId) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "incorrect group id");
            return;
        }


        //vote on date
        if (action != null && action.equals("vote")) {
            int voteInt = (int) Math.round(vote);
            event.dateVote(userId, voteInt);
        }

        // add user
        if (userId != null) {
            UserData user = ofy().load().type(UserData.class).id(userId).now();
            if (user == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "user not found");
                return;
            }
            event.addAttendee(user.getId(), user.getName());
        }

        // admin abilities
        if (name != null || type != null || dateString != null || (action != null && action.equals("teams"))) {
            // check to make sure admin
            if (!group.isUserAdmin(userId)) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "user is not admin");
                return;
            }

            if (name != null) {
                group.getEvent(eventId).setName(name);
                event.setName(name);
            }

            if (teams != null) {
                event.setTeams(teams);
            }

            if (type != null) {
                event.setType(type.toLowerCase());
            }

            if (address != null) {
                event.setAddress(address);
            }

            if (dateString != null) {
                long date = Long.parseLong(dateString);
                event.setDate(date);
                group.getEvent(eventId).date = date;
            }

            ofy().save().entity(group).now();
        }

        // put the data back
        event.checkPoll();
        ofy().save().entity(event).now();

        // respond
        String groupJson = gson.toJson(event);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.getWriter().write(groupJson);
        resp.getWriter().flush();
        resp.getWriter().close();
    }
}
