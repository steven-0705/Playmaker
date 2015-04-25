package data;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.security.acl.Group;
import java.util.Map;

/**
 * Created by Chris on 4/11/2015.
 */
@Entity
public class GroupUserDetailed extends DataObject {

    @Id protected String id;
    protected boolean admin = false;

    protected Map<String, PlayerStats> stats;

    public GroupUserDetailed(UserData user) {
        id = user.getId();
        name = user.getName();
    }

    public GroupUserDetailed(UserData user, boolean admin) {
        this(user);
        this.admin = admin;
    }

    protected Map<String, PlayerStats> getStats() {
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
            stat = new PlayerStats();

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

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public static class PlayerStats {

        public int numPlayed = 0;
        public int totalUp = 0;
        public int totalDown = 0;

        public PlayerStats() {};

    }


}
