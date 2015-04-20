package casuals.filthy.playmaker.data.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PollBean extends DataBean {

    public long id;
    public long groupId;
    public List<String> options;
    public Map<String, Integer> votes;
    public List<String> voterIds;

    public PollBean() {};

    public PollBean(List<String> options, long id, long groupId) {
        this.options = new ArrayList<String>();
        this.options.addAll(options);
        this.groupId = groupId;
        this.id = id;

        votes = new HashMap<String, Integer>();
        for (String opt : this.options) {
            votes.put(opt, 0);
        }
    }

    public long getId() {
        return id;
    }

    public long getGroupId() {
        return groupId;
    }
}
