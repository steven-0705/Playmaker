package casuals.filthy.playmaker.backend;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Chris on 3/27/2015.
 */
public class GroupUsersServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HashMap<String, String> params = ServletUtils.getParams(req.getInputStream());

        String userId = params.get("user_id");
        String eventIdString = params.get("event_id");
        String groupIdString = params.get("group_id");
        String upString = params.get("score_up");
        String downString = params.get("score_down");

        if (userId == null || eventIdString == null || groupIdString == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "missing user_id, event_id, or group_id fields");
            return;
        }

        if (upString == null || downString == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "missing user_id, event_id, or group_id fields");
            return;
        }

        long eventId = Long.parseLong(eventIdString);
        long groupId = Long.parseLong(groupIdString);
        int up = Integer.parseInt(upString);
        int down = Integer.parseInt(downString);





    }


}
