package casuals.filthy.playmaker;

/**
 * Created by Shane on 3/23/2015.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaderboardEntries {

    /**
     * An array of sample (dummy) items.
     */
    public static List<HashMap<String,String>> ITEMS = new ArrayList<HashMap<String,String>>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public final static String KEY_ICON = "icon";
    public final static String KEY_NAME = "name";
    public final static String KEY_PLACE = "place";
    public final static String KEY_POINTS = "points";

//    public static Map<String, LeaderboardEntry> ITEM_MAP = new HashMap<String, LeaderboardEntry>();
    public static ArrayList<HashMap<String, String>> mLeaderboardEntries =
            new ArrayList<HashMap<String, String>>();

    static {
        // Add 3 sample items.
        addItem("1", "Andrew Luck", "1st Place", R.drawable.first_place,"15900", "user1");
        addItem("2", "Peyton Manning", "2nd Place",R.drawable.second_place, "15400", "user2");
        addItem("3", "Tom Brady", "3rd Place",R.drawable.third_place, "14200","user3");
        addItem("4", "Russell Wilson", "4th Place",R.drawable.std_ribbon,"14200", "user1");
        addItem("5", "Drew Brees", "5th Place",R.drawable.std_ribbon,"14000","user2");
        addItem("6", "Aaron Rodgers", "6th Place",R.drawable.std_ribbon,"13050","user3");
        addItem("7", "Eli Manning", "7th Place",R.drawable.std_ribbon,"13000", "user1");
        addItem("8", "Cam Newton", "8th Place",R.drawable.std_ribbon,"12900","user2");
        addItem("9", "Tony Romo", "9th Place",R.drawable.std_ribbon,"12800","user3");
        addItem("10", "Ben Roethlisberger", "10th Place",R.drawable.std_ribbon,"11100", "user1");
        addItem("11", "Phillip Rivers", "11th Place",R.drawable.std_ribbon,"10000","user2");
        addItem("12", "Colin Kaepernick", "12th Place",R.drawable.std_ribbon,"9800","user3");
        addItem("13", "Matthew Stafford", "13th Place",R.drawable.std_ribbon,"9200", "user1");
        addItem("14", "Joe Flacco", "14th Place",R.drawable.std_ribbon,"8200","user2");
        addItem("15", "Michael Vick", "15th Place",R.drawable.std_ribbon,"7100","user3");

    }

    private static void addItem(String id, String name, String place, int iconId, String points, String content) {


        HashMap<String, String> leaderboardEntry = new HashMap<String, String>();
        leaderboardEntry.put(KEY_NAME, name);
        leaderboardEntry.put(KEY_PLACE, place);
        leaderboardEntry.put(KEY_POINTS, points);
        leaderboardEntry.put(KEY_ICON, String.valueOf(iconId));

        ITEMS.add(leaderboardEntry);
    }


}