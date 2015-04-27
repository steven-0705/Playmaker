package casuals.filthy.playmaker.data.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBean extends DataBean {

    protected long id;
    protected long date;
    protected PollBean datePoll = new PollBean();
    protected List<Long> pollMeaning = new ArrayList<Long>();
    protected String type;
    protected String description = "No description available";
    protected Map<String, String> attending = new HashMap<String, String>();
    protected long groupId;
    protected int numTeams = 2;
    protected List<EventTeam> teams = new ArrayList<EventTeam>();
    protected boolean autoTeams = true;
    protected boolean closed;
    protected String address;
    protected long closeDate;
    protected List<String> items = new ArrayList<String>();
    protected boolean reported;

    public boolean isClosed() {
        return closed;
    }

    public EventBean() {};

    public long getId() {
        return id;
    }

    public long getDate() {
        return date;
    }

    public PollBean getDatePoll() {
        return datePoll;
    }

    public List<Long> getPollMeaning() {
        return pollMeaning;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, String> getAttending() {
        return attending;
    }

    public long getGroupId() {
        return groupId;
    }

    public int getNumTeams() {
        return numTeams;
    }

    public List<EventTeam> getTeams() {
        return teams;
    }

    public boolean isAutoTeams() {
        return autoTeams;
    }

    public String getAddress() {
        return address;
    }

    public long getCloseDate() {
        return closeDate;
    }

    public List<String> getItems() {
        return items;
    }

    public boolean isReported() {
        return reported;
    }

    public static class EventTeam {
        List<String> members;

        public EventTeam() {
            members = new ArrayList<String>();
        }

        public int size() {
            return members.size();
        }

        public List<String> getMembers() {
            return members;
        }
    }
}
