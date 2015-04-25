package data;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.security.acl.Group;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chris on 4/11/2015.
 */
@Entity
public class GroupUserDetailed extends DataObject {

    @Id protected String id;
    protected boolean admin = false;

    protected Map<String, PlayerStats> stats;

    protected GroupUserDetailed() {};

    public GroupUserDetailed(UserData user) {
        id = user.getId();
        name = user.getName();
    }

    public GroupUserDetailed(UserData user, boolean admin) {
        this(user);
        this.admin = admin;
    }

    /**
     * Adds a two value scoring system. if there is no down then leave second one 0
     * @param up
     * @param down
     */
    public void addEventStats(String type, int up, int down) {
        if (stats == null)
            stats = new HashMap<String, PlayerStats>();

        PlayerStats stat = stats.get(type);
        if (stat == null)
            stat = new PlayerStats(id);

        stat.numPlayed++;
        stat.totalUp += up;
        stat.totalDown += down;
        stats.put(type, stat);
    }

    public PlayerStats getStat(String type) {
        if (stats == null)
            stats = new HashMap<String, PlayerStats>();

        if (stats.get(type) == null)
           stats.put(type, new PlayerStats(id));

        return stats.get(type);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public static class PlayerStats implements Comparable<PlayerStats> {

        protected int numPlayed = 0;
        protected int totalUp = 0;
        protected int totalDown = 0;
        protected String player;

        public PlayerStats() {};

        public PlayerStats(String player) {
            this.player = player;
        }

        public long computeScore() {
            return Math.round(totalUp * 100 + (totalUp * numPlayed) +
                    (totalDown != 0 ? ((double) totalUp / totalDown) : 0));
        }

        @Override
        public int compareTo(PlayerStats o) {
            return (int) (this.computeScore() - o.computeScore());
        }

        public String getPlayer() {
            return player;
        }
    }


}
