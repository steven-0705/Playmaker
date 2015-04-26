package tests;

import org.junit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import data.EventData;
import data.GroupData;
import data.UserData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Chris on 4/17/2015.
 */
public class EventTests {

    GroupData group1;
    GroupData group2;
    UserData user1;
    UserData user2;
    UserData user3;
    UserData user4;

    @Before
    public void setup() {
        Random rand = new Random();
        user1 = Utils.addUser("events1" + rand.nextInt(1000000), "billy", "billy@bills.com");
        user2 = Utils.addUser("events2" + rand.nextInt(1000000), "bobby", "bobby@bills.com");
        user3 = Utils.addUser("events3" + rand.nextInt(1000000), "will", "tom@bills.com");
        user4 = Utils.addUser("events4" + rand.nextInt(1000000), "tim", "tam@bills.com");

        group1 = Utils.addGroup(user1.getId(), "group1");
        group2 = Utils.addGroup(user2.getId(), "group2");

        Utils.joinGroup(user2.getId(), group1.getId());
        Utils.joinGroup(user3.getId(), group1.getId());
        Utils.joinGroup(user4.getId(), group1.getId());
        Utils.joinGroup(user1.getId(), group2.getId());
        Utils.joinGroup(user3.getId(), group2.getId());
        Utils.joinGroup(user4.getId(), group2.getId());

        group1 = Utils.getGroup(group1.getId());
        group2 = Utils.getGroup(group2.getId());

        assertEquals(group1.getUsers().size(), 4);
        assertEquals(group2.getUsers().size(), 4);
    }

    @Test
    public void eventsCreateEvent() {
        EventData event = Utils.addEvent(user1.getId(), group1.getId(), "FUN TIME", "flamingos", 100000, 2, true, 0);
        assertNotNull(event);
        assertEquals(event.getName(), "FUN TIME");
        assertEquals(event.getType(), "flamingos");

        group1 = Utils.getGroup(group1.getId());
        assertTrue(group1.getEventTypes().contains("flamingos"));
    }

    @Test
    public void eventsGetEvent() {
        EventData event = Utils.addEvent(user1.getId(), group1.getId(), "YAY", "flamingos", 1010000, 2, true, 0);
        assertNotNull(Utils.getGroup(group1.getId()).getEvent(event.getId()));
    }

    @Test
    public void eventsJoinEvent() {
        EventData event = Utils.addEvent(user2.getId(), group2.getId(), "EVENTS YAY", "soccer", 10100100, 2, true, 0);
        event = Utils.joinEvent(user1.getId(), event.getId(), group2.getId());
        assertEquals(event.getAttending().size(), 1);
        event = Utils.joinEvent(user3.getId(), event.getId(), group2.getId());
        assertEquals(event.getAttending().size(), 2);
        event = Utils.joinEvent(user4.getId(), event.getId(), group2.getId());
        assertEquals(event.getAttending().size(), 3);
        event = Utils.joinEvent(user2.getId(), event.getId(), group2.getId());
        assertEquals(event.getAttending().size(), 4);

        assertEquals(2, event.getNumTeams());
        assertEquals(2, event.getTeams().size());
        assertEquals(2, event.getTeams().get(0).size());
        assertEquals(2, event.getTeams().get(1).size());
    }


    @Test
    public void eventsTeamsForceEvent() {
        EventData event = Utils.addEvent(user2.getId(), group2.getId(), "EVENTS YAY", "soccer", 10100100, 2, false, 0);
        event = Utils.joinEvent(user1.getId(), event.getId(), group2.getId());
        assertEquals(event.getAttending().size(), 1);
        event = Utils.joinEvent(user3.getId(), event.getId(), group2.getId());
        assertEquals(event.getAttending().size(), 2);
        event = Utils.joinEvent(user4.getId(), event.getId(), group2.getId());
        assertEquals(event.getAttending().size(), 3);
        event = Utils.joinEvent(user2.getId(), event.getId(), group2.getId());
        assertEquals(event.getAttending().size(), 4);

        ArrayList<List<String>> teams = new ArrayList<List<String>>();
        teams.add(new ArrayList<String>());
        teams.get(0).add(user1.getId());
        teams.get(0).add(user1.getId());
        teams.get(0).add(user1.getId());
        teams.get(0).add(user1.getId());
        teams.add(new ArrayList<String>());

        event = Utils.setTeams(user2.getId(), group2.getId(), event.getId(), teams);
        assertEquals(2, event.getNumTeams());
        assertEquals(2, event.getTeams().size());
        assertEquals(4, event.getTeams().get(0).size());
        assertEquals(0, event.getTeams().get(1).size());
    }

    @Test
    public void eventsDateVoting() {
        List<Long> dates = new ArrayList<Long>();
        dates.add((long) 100000000);
        dates.add((long) 110000000);
        EventData event = Utils.addEvent(user1.getId(), group1.getId(), "things", "soccer", dates, 2, true, System.currentTimeMillis() + 100000);

        event = Utils.voteDate(user1.getId(), group1.getId(), event.getId(), 0);


    }


}
