package tests;

import com.google.gson.JsonParseException;

import org.junit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import data.EventData;
import data.GroupData;
import data.UserData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
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
        group1 = Utils.addEvent(user1.getId(), group1.getId(), "FUN TIME", "flamingos", 100000, 2, true, 0);
        EventData event = Utils.getEvent(user1.getId(), group1.getId(), group1.getEvents().get(group1.getEvents().size() - 1).getEventId());
        assertNotNull(event);
        assertEquals(event.getName(), "FUN TIME");
        assertEquals(event.getType(), "flamingos");

        group1 = Utils.getGroup(group1.getId());
        assertTrue(group1.getEventTypes().contains("flamingos"));
    }

    @Test
    public void eventsGetEvent() {
        group1 = Utils.addEvent(user1.getId(), group1.getId(), "YAY", "flamingos", 1010000, 2, true, 0);
        EventData event = Utils.getEvent(user1.getId(), group1.getId(), group1.getEvents().get(group1.getEvents().size() - 1).getEventId());
        assertNotNull(Utils.getGroup(group1.getId()).getEvent(event.getId()));

        long id = event.getId();
        event = Utils.getEvent(user1.getId(), group1.getId(), event.getId());

        assertNotNull(event);
        assertEquals(id, event.getId());
        assertEquals(group1.getId(), event.getGroupId());
    }

    @Test
    public void eventsJoinEvent() {
        group2 = Utils.addEvent(user2.getId(), group2.getId(), "EVENTS YAY", "soccer", 10100100, 2, true, 0);
        EventData event = Utils.getEvent(user1.getId(), group2.getId(), group2.getEvents().get(group2.getEvents().size() - 1).getEventId());
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
        group2 = Utils.addEvent(user2.getId(), group2.getId(), "EVENTS YAY", "soccer", 10100100, 2, false, 0);
        EventData event = Utils.getEvent(user1.getId(), group2.getId(), group2.getEvents().get(group2.getEvents().size() - 1).getEventId());
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
    public void eventsDateVoting1() {
        List<Long> dates = new ArrayList<Long>();
        dates.add((long) 100000000);
        dates.add((long) 110000000);
        group1 = Utils.addEvent(user1.getId(), group1.getId(), "things", "soccer", dates, 2, true, System.currentTimeMillis() + 100000);
        EventData event = Utils.getEvent(user1.getId(), group1.getId(), group1.getEvents().get(group1.getEvents().size() - 1).getEventId());

        event = Utils.voteDate(user1.getId(), group1.getId(), event.getId(), 0);
    }

    @Test
    public void eventsDateVoting2() throws InterruptedException {
        List<Long> dates = new ArrayList<Long>();
        dates.add((long) 100000000);
        dates.add((long) 110000000);
        group1 = Utils.addEvent(user1.getId(), group1.getId(), "things", "soccer", dates, 2, true, System.currentTimeMillis() + 2000);
        EventData event = Utils.getEvent(user1.getId(), group1.getId(), group1.getEvents().get(group1.getEvents().size() - 1).getEventId());

        event = Utils.voteDate(user1.getId(), group1.getId(), event.getId(), 1);
        assertFalse(event.isClosed());

        synchronized (this) {
            this.wait(2500);
        }

        event = Utils.getEvent(user1.getId(), group1.getId(), event.getId());
        assertTrue(event.isClosed());
        assertEquals((long) 110000000, event.getDate());
    }

    @Test
    public void eventsDeleteEvent() {
        group2 = Utils.addEvent(user2.getId(), group2.getId(), "delete meee", "soccer", 100001000, 2, true, 0);

        int events = group2.getEvents().size();
        group2 = Utils.deleteEvent(user2.getId(), group2.getEvents().get(group2.getEvents().size() - 1).getEventId());

        assertEquals(events - 1, group2.getEvents().size());
    }

    @Test
    public void eventsLeaveEvent() {
        group2 = Utils.addEvent(user2.getId(), group2.getId(), "delete meee", "soccer", 100001000, 2, true, 0);
        EventData event = Utils.joinEvent(user1.getId(),  group2.getEvents().get(group2.getEvents().size() - 1).getEventId(), group2.getId());

        int members = event.getAttending().size();

        event = Utils.leaveEvent(user1.getId(), group2.getEvents().get(group2.getEvents().size() - 1).getEventId());

        assertEquals(members - 1, event.getAttending().size());
        assertFalse(event.getAttending().containsKey(user1.getId()));
    }

    @Test (expected = JsonParseException.class)
    public void eventsDeleteEventNonAdmin() {
        group2 = Utils.addEvent(user2.getId(), group2.getId(), "delete meee", "soccer", 100001000, 2, true, 0);

        int events = group2.getEvents().size();
        group2 = Utils.deleteEvent(user1.getId(), group2.getEvents().get(group2.getEvents().size() - 1).getEventId());
    }

}
