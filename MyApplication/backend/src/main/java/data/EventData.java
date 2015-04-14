package data;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static casuals.filthy.playmaker.backend.OfyService.ofy;

/**
 * Created by Chris on 4/6/2015.
 */
@Entity
public class EventData extends DataObject {

    @Id
    public long id;
    public long date;
    public String type;
    public String description = "No description available";
    public Map<String, String> attending;
    public long groupId;
    public int numTeams = 2;
    public List<ArrayList<String>> teams;

    public EventData() {};

    public EventData(long id, long date, String type, long groupId) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.groupId = groupId;
    }

    public void addAttendee(String id, String name) {
        if (attending == null)
            attending = new HashMap<String, String>();
        attending.put(id, name);

        // reform teams
        updateTeams();
    }

    private void updateTeams() {

        teams = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < numTeams; i++) {
            teams.add(new ArrayList<String>());
        }

        // get the data
        Map<String, GroupUserDetailed> stats = ofy().load().type(GroupUserDetailed.class).ids(attending.keySet());
        List<GroupUserDetailed.PlayerStats> ranking = new ArrayList<GroupUserDetailed.PlayerStats>();
        for (GroupUserDetailed player: stats.values()) {
            ranking.add(player.getStats().get(type));
        }

        Collections.sort(ranking);

        for (int i = 0; i < ranking.size(); i++) {
            teams.get(i%numTeams).add(ranking.get(i).player);
        }

    }

}
