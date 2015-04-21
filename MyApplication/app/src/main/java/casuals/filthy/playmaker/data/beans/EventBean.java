package casuals.filthy.playmaker.data.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventBean extends DataBean {

    public long id;
    public long date;
    public String type;
    public String description = "No description available";
    public Map<String, String> attending;
    public long groupId;
    public int numTeams = 2;
    public List<ArrayList<String>> teams;

    public EventBean() {};

    public EventBean(long id, long date, String type, long groupId) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.groupId = groupId;
    }

    public long getId() {
        return id;
    }

    public long getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public long getGroupId() {
        return groupId;
    }

    public int getNumTeams() {
        return numTeams;
    }

}
