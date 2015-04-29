package data;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sun.rmi.runtime.Log;

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
    public void addEventStats(String type, double up, double down) {
        if (stats == null)
            stats = new HashMap<String, PlayerStats>();

        PlayerStats stat = stats.get(type);
        if (stat == null)
            stat = new PlayerStats(id);

        stat.numPlayed++;
        stat.ups.add(up);
        stat.downs.add(down);
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
        protected List<Double> ups = new ArrayList<Double>();
        protected List<Double> downs = new ArrayList<Double>();
        protected String player;

        public PlayerStats() {};

        public PlayerStats(String player) {
            this.player = player;
        }

        public long computeScore() {
            double score = 700;
            for(Double ratio: ups) {
                if(ratio > 0.5) {
                    score += (ratio * 20);
                }
                else {
                   score -= ((1 - ratio) * 20);
                }
            }
            if(score < 100) { score = 100; }
            return (long) score;
        }

        @Override
        public int compareTo(PlayerStats o) {
            return (int) (o.computeScore() - this.computeScore());
        }

        public String getPlayer() {
            return player;
        }
    }


}
