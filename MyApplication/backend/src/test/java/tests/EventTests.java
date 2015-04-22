package tests;

import org.junit.*;

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
        user3 = Utils.addUser("events3" + rand.nextInt(1000000), "wil", "tom@bills.com");
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
        EventData event = Utils.addEvent(user1.getId(), group1.getId(), "FUN TIME", "flamingos", 100000);
        assertNotNull(event);
        assertEquals(event.getName(), "FUN TIME");
        assertEquals(event.getType(), "flamingos");

        group1 = Utils.getGroup(group1.getId());
        assertTrue(group1.getEventTypes().contains("flamingos"));
    }

    @Test
    public void eventsGetEvent() {
        EventData event = Utils.addEvent(user1.getId(), group1.getId(), "YAY", "flamingos", 1010000);
    }




}
