package data;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import casuals.filthy.playmaker.backend.OfyService;

import static casuals.filthy.playmaker.backend.OfyService.ofy;

/**
 * Created by Chris on 4/6/2015.
 */
@Entity
public class EventData extends DataObject {

    @Id
    protected long id;
    protected long date;
    protected PollData datePoll;
    protected List<Long> pollMeaning;
    protected String type;
    protected String description = "No description available";
    protected Map<String, String> attending;
    protected long groupId;
    protected int numTeams = 2;
    protected List<EventTeam> teams;
    protected boolean autoTeams = true;
    protected boolean closed;
    protected String address;
    protected long closeDate;
    protected boolean reported = false;
    protected List<String> items;

    public EventData() {};

    public EventData(long id, List<Double> dates, String type, long groupId, String name, String address, boolean autogen, long closeDate) {
        this.id = id;
        if (dates.size() == 1) {
            date = Math.round(dates.get(0));
            closed = true;
        }
        else {
            closed = false;
            List<String> options = new ArrayList<String>();
            pollMeaning = new ArrayList<Long>();
            for (Double date: dates) {
                options.add(new Date(Math.round(date)).toString());
                pollMeaning.add(Math.round(date));
            }
            datePoll = new PollData(options, 0, groupId);
        }

        this.type = type;
        this.groupId = groupId;
        this.address = address;
        this.name = name;
        this.closeDate = closeDate;
        autoTeams = autogen;
    }

    public void dateVote(String user, int choice) {
        if (!closed)
            datePoll.addVote(user, choice);
    }

    public void checkPoll() {
        if (closeDate > System.currentTimeMillis()) {
            int[] results = datePoll.compile();

            int max = 0;
            for (int i = 1; i < results.length; i++) {
                if (results[i] > results[max])
                    max = i;
            }

            date = pollMeaning.get(max);
            pollMeaning = null;
            datePoll = null;
            closed = true;
        }
    }

    public void addAttendee(String id, String name) {
        if (attending == null)
            attending = new HashMap<String, String>();
        attending.put(id, name);

        // reform teams
        if (autoTeams && numTeams > 0)
            updateTeams();
    }

    public void setTeams(List<List<String>> teamsList) {
        numTeams = teamsList.size();
        teams = new ArrayList<EventTeam>();
        for (int i = 0; i < numTeams; i++) {
            EventTeam team = new EventTeam();
            for (String player: teamsList.get(i)) {
                team.add(player);
            }
            teams.add(team);
        }
    }

    private void updateTeams() {

        teams = new ArrayList<EventTeam>();
        for (int i = 0; i < numTeams; i++) {
            teams.add(new EventTeam());
        }

        // get the data
        List<GroupUserDetailed.PlayerStats> ranking = new ArrayList<GroupUserDetailed.PlayerStats>();
        for (GroupUserDetailed player: ofy().load().type(GroupData.class).id(groupId).now().getUsers()) {
            if (attending.containsKey(player.getId()))
                ranking.add(player.getStat(type));
        }

        Collections.sort(ranking);

        for (int i = 0; i < ranking.size(); i++) {
            teams.get(i % numTeams).add(ranking.get(i).getPlayer());
        }

    }

    public List<String> getItems() {
        if (items == null)
            items = new ArrayList<String>();
        return items ;
    }

    public void addItems(List<String> items) {
        if (this.items == null)
            this.items = new ArrayList<String>();
        this.items.addAll(items);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isClosed() {
        return closed;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public int getNumTeams() {
        return numTeams;
    }

    public void setNumTeams(int numTeams) {
        this.numTeams = numTeams;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Map<String, String> getAttending() {
        return attending;
    }

    public List<EventTeam> getTeams() {
        return teams;
    }

    public void setReported(boolean b) {
        reported = true;
    }

    public static class EventTeam {
        List<String> members;

        public EventTeam() {
            members = new ArrayList<String>();
        }

        public void add(String member) {
            if (members == null)
                members = new ArrayList<String>();
            members.add(member);
        }

        public int size() {
            return members.size();
        }

        public List<String> getMembers() {
            return members;
        }
    }

}
