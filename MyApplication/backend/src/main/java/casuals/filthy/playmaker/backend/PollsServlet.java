package casuals.filthy.playmaker.backend;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.GroupData;
import data.PollData;

import static casuals.filthy.playmaker.backend.OfyService.ofy;

/**
 * Created by Chris on 3/27/2015.
 */
public class PollsServlet extends HttpServlet {

    public static Gson gson = new Gson();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HashMap<String, Object> params = ServletUtils.getParams(req.getInputStream());

        String groupIdString = (String) params.get("group_id");req.getParameter("group_id");
        String userId = (String) params.get("user_id");//req.getParameter("user_id");
        String action = (String) params.get("actions");//req.getParameter("action");


        if (groupIdString == null || userId == null || action == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "missing group_id, action, or user_id");
            return;
        }

        long groupId = Long.parseLong(groupIdString);
        GroupData group = ofy().load().type(GroupData.class).id(groupId).now();

        if (group == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "group not found");
            return;
        }

        PollData poll = null;

        if (action.equals("vote")) {
            String pollIdString = (String) params.get("poll_id");//req.getParameter("poll_id");
            String voteString = (String) params.get("vote");//req.getParameter("vote");
            if (pollIdString == null || voteString == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "missing poll_id, or vote");
                return;
            }

            int vote = Integer.parseInt(voteString);
            long pollId = Long.parseLong(pollIdString);

            // lets get the group and the poll
            //poll = ofy().load().type(PollData.class).id(pollId).now();
            poll = group.getPoll(pollId);
            if (poll == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "poll not found");
                return;
            }

            poll.addVote(userId, vote);
        }
        else if (action.equals("create")) {
            String name = (String) params.get("poll_name");//req.getParameter("poll_name");
            if (name == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "missing required poll_name parameter");
                return;
            }

            // get the options
            List<String> options = new ArrayList<String>();
            for (int i = 0; i <= 20; i++) {
                String opt = (String) params.get("option"+i);//req.getParameter("option"+i);
                if (opt == null)
                    break;
                else
                    options.add(opt);
            }

            if (options.size() < 2) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "requires at least two options");
                return;
            }

            long id = DatastoreServiceFactory.getDatastoreService().allocateIds("poll", 1).getStart().getId();
            poll = new PollData(options, id, groupId);

            group.addPoll(poll);
        }


        // store the poll
        //ofy().save().entities(poll).now();

        // respond
        String groupJson = gson.toJson(poll);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.getWriter().write(groupJson);
        resp.getWriter().flush();
        resp.getWriter().close();
    }
}
