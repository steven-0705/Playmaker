package data;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.Map;

/**
 * Created by Chris on 4/11/2015.
 */
@Entity
public class GroupUserDetailed extends DataObject {

    @Id public String id;
    public String type;
    public boolean admin;
    public long groupId;

    public Map<String, PlayerStats> stats;

    public Map<String, PlayerStats> getStats() {
        return stats;
    }

    /**
     * Adds a two value scoring system. if there is no down then leave second one 0
     * @param up
     * @param down
     */
    public void addEventStats(String type, int up, int down) {
        PlayerStats stat = stats.get(type);
        if (stat == null)
            stat = new PlayerStats(id);

        stat.numPlayed++;
        stat.totalUp += up;
        stat.totalDown += down;
        stats.put(type, stat);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public static class PlayerStats implements Comparable<PlayerStats> {

        public int numPlayed = 0;
        public int totalUp = 0;
        public int totalDown = 0;
        public String player;

        public PlayerStats() {};

        public PlayerStats(String player) {
            this.player = player;
        }

        public double computeScore() {
            return totalUp * 100 * (0.1 * numPlayed);
        }

        @Override
        public int compareTo(PlayerStats o) {
            return (((double) totalUp/totalDown / ((0.85)*numPlayed)) < ((double) o.totalUp/o.totalDown / ((0.85)*o.numPlayed))) ? -1 : 1;
        }
    }


}
