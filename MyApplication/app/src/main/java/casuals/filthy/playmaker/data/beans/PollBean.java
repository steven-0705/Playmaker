package casuals.filthy.playmaker.data.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PollBean extends DataBean {

    protected long id;
    protected long groupId;
    protected List<String> options;
    protected Map<String, Integer> votes;

    public PollBean() {};

    public long getId() {
        return id;
    }

    public long getGroupId() {
        return groupId;
    }

    public int[] compile() {
        int[] results = new int[options.size()];
        for (Integer vote: votes.values()) {
            results[vote]++;
        }

        return results;
    }
}
