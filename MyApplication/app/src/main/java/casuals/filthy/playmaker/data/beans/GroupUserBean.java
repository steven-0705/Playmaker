package casuals.filthy.playmaker.data.beans;

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

        public int getNumPlayed() {
            return numPlayed;
        }

        public int getTotalUp() {
            return totalUp;
        }

        public int getTotalDown() {
            return totalDown;
        }
    }
}
