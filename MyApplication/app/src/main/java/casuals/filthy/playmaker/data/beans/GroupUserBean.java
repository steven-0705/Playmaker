package casuals.filthy.playmaker.data.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Chris on 4/26/2015.
 */
public class GroupUserBean extends DataBean{

    protected String id;
    protected boolean admin = false;

    protected Map<String, PlayerStats> stats;

    public GroupUserBean() {};

    public String getId() {
        return id;
    }

    public boolean isAdmin() {
        return admin;
    }

    public Map<String, PlayerStats> getStats() {
        return stats;
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
            double score = 1200;
            for(Double ratio: ups) {
                if(ratio > 0.5) {
                    score += (ratio * 20);
                }
                else {
                    score -= ((1 - ratio) * 20);
                }
            }
            if(score < 600) { score = 600; }
            return (long) score;
        }

        @Override
        public int compareTo(PlayerStats o) {
            return (int) (this.computeScore() - o.computeScore());
        }

        public String getPlayer() {
            return player;
        }

        public int getNumPlayed() {
            return numPlayed;
        }

        public List<Double> getUps() {
            return ups;
        }

        public List<Double> getDowns() {
            return downs;
        }
    }
}
