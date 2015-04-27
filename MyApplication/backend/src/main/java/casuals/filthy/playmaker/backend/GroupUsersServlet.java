package casuals.filthy.playmaker.backend;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.EventData;
import data.GroupData;

import static casuals.filthy.playmaker.backend.OfyService.ofy;

/**
 * Created by Chris on 3/27/2015.
 */
public class GroupUsersServlet extends HttpServlet {

    private static Gson gson = new Gson();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HashMap<String, Object> params = ServletUtils.getParams(req.getInputStream());

        String userId = (String) params.get("user_id");
        String eventIdString = (String) params.get("event_id");
        String groupIdString = (String) params.get("group_id");
        List<Double> up = (List<Double>) params.get("results_up");
        List<Double> down = (List<Double>) params.get("results_down");

        if (userId == null || eventIdString == null || groupIdString == null || up == null || down == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "missing user_id, event_id, or group_id fields");
            return;
        }

        long groupId = Long.parseLong(groupIdString);
        long eventId = Long.parseLong(eventIdString);

        GroupData group = ofy().load().type(GroupData.class).id(groupId).now();
        if (group == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "group not found");
            return;
        }

        if (!group.getUserById(userId).isAdmin()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "must be admin");
            return;
        }

        EventData event = ofy().load().type(EventData.class).id(eventId).now();
        if (event == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "event not found");
            return;
        }

        for (int i = 0; i < up.size(); i++) {
            for (String user: event.getTeams().get(i).getMembers()) {
                group.getUserById(user).addEventStats(event.getType(), (int)Math.round(up.get(i)), (int)Math.round(down.get(i)));
            }
        }

        event.setReported(true);
        group.getEvent(eventId).reported();

        ofy().save().entities(group, event, group).now();

        // respond
        String userJson = gson.toJson(group);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.getWriter().write(userJson);
        resp.getWriter().flush();
        resp.getWriter().close();
    }


}
