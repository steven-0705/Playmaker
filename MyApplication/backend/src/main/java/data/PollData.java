package data;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chris on 4/14/2015.
 */
@Entity
public class PollData extends DataObject {


    @Id
    protected long id;
    protected long groupId;
    protected List<String> options;
    protected Map<String, Integer> votes;
    protected List<String> voterIds;

    /**
     * Adds a user's vote to the poll
     * @param userId
     * @param choice
     * @return true if successful, false if the user has already voted or vote is bad
     */
    public boolean addVote(String userId, int choice) {
        if (voterIds == null)
            voterIds = new ArrayList<String>();
        else {
            if (voterIds.indexOf(userId) != -1)
                return false;
        }

        if (choice >= options.size())
            return false;

        votes.put(options.get(choice), votes.get(options.get(choice)) + 1);
        voterIds.add(userId);

        return true;
    }

    public PollData() {};

    public PollData(List<String> options, long id, long groupId) {
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
