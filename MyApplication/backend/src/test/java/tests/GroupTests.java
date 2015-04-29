package tests;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.security.acl.Group;
import java.util.Random;

import static org.junit.Assert.*;

import data.GroupData;
import data.UserData;

/**
 * Created by Chris on 4/17/2015.
 */
public class GroupTests {

    UserData user1;
    UserData user2;
    UserData user3;
    UserData user4;
    UserData user5;
    UserData user6;
    UserData user7;
    UserData user8;

    @Before
    public void makeUsers() {
        Random rand = new Random();
        user1 = Utils.addUser("groups1" + rand.nextInt(100000), "leedle", "loodle");
        user2 = Utils.addUser("groups2" + rand.nextInt(100000), "leedle", "loodle");
        user3 = Utils.addUser("groups3" + rand.nextInt(100000), "leedle", "loodle");
        user4 = Utils.addUser("groups4" + rand.nextInt(100000), "leedle", "loodle");
        user5 = Utils.addUser("groups5" + rand.nextInt(100000), "leedle", "loodle");
        user6 = Utils.addUser("groups6" + rand.nextInt(100000), "leedle", "loodle");
        user7 = Utils.addUser("groups7" + rand.nextInt(100000), "leedle", "loodle");
        user8 = Utils.addUser("groups8" + rand.nextInt(100000), "leedle", "loodle");
    }

    @Test
    public void groupsCreateGroup() {
        GroupData group = Utils.addGroup(user1.getId(), "group1");
        assertNotNull(group);
        assertEquals("group1", group.getName());
        assertTrue(group.getUsers().size() == 1);
        assertEquals(group.getUsers().get(0).getName(), user1.getName());
        assertTrue(group.getUsers().get(0).isAdmin());
    }

    @Test
    public void groupsGetGroup() {
        GroupData group1 = Utils.addGroup(user1.getId(), "group2");
        GroupData group2 = Utils.getGroup(group1.getId());

        assertEquals(group1.getName(), group2.getName());
        assertEquals(group1.getId(), group2.getId());
        assertEquals(group1.getUsers().size(), group2.getUsers().size());
    }

    @Test
    public void groupsJoinGroup() {
        GroupData group = Utils.addGroup(user5.getId(), "joiningGroup");
        UserData userJoined = Utils.joinGroup(user6.getId(), group.getId());
        assertNotNull(userJoined);
        System.out.println(userJoined.getGroups().size());
        assertEquals(userJoined.getGroups().size(), 1);
        assertEquals(user6.getId(), userJoined.getId());
        // need to update the group data
        group = Utils.getGroup(group.getId());
        assertEquals(group.getUsers().size(), 2);
        assertEquals(group.getUsers().get(1).getId(), userJoined.getId());

        // test more
        Utils.joinGroup(user1.getId(), group.getId());
        group = Utils.getGroup(group.getId());
        assertEquals(group.getUsers().size(), 3);
        Utils.joinGroup(user2.getId(), group.getId());
        group = Utils.getGroup(group.getId());
        assertEquals(group.getUsers().size(), 4);
        Utils.joinGroup(user3.getId(), group.getId());
        group = Utils.getGroup(group.getId());
        assertEquals(group.getUsers().size(), 5);
        // test a duplicate
        Utils.joinGroup(user5.getId(), group.getId());
        group = Utils.getGroup(group.getId());
        assertEquals(group.getUsers().size(), 5);

    }

    @Test
    public void groupsNotificationCreation() {
        GroupData group = Utils.addGroup(user1.getId(), "notifyGroup");
        int notes = group.getNotifications().size();

        group = Utils.sendNotification("MESSAGE", group.getId(), user1.getName(), user1.getId());

        assertEquals(notes + 1, group.getNotifications().size());
    }

    @Test
    public void groupsDeleteGroup() throws InterruptedException {
        GroupData group = Utils.addGroup(user5.getId(), "delete me!!!");

        user5 = Utils.getUser(user5.getId());
        int groups = user5.getGroups().size();

        synchronized (this) {
            this.wait(500);
        }

        user5 = Utils.deleteGroup(user5.getId(), group.getId());

        assertEquals(groups - 1, user5.getGroups().size());
    }

    @Test
    public void groupsLeaveGroup() {
        GroupData group = Utils.addGroup(user1.getId(), "delete me too!");
        user1 = Utils.getUser(user1.getId());
        user2 = Utils.joinGroup(user2.getId(), group.getId());
        int groups = user1.getGroups().size();

        user2 = Utils.deleteGroup(user2.getId(), group.getId());
        user1 = Utils.getUser(user1.getId());

        assertEquals(groups, user1.getGroups().size());
    }


    @Test
    public void groupsDeleteGroupInvites() throws InterruptedException {
        GroupData group = Utils.addGroup(user1.getId(), "delete me three!");
        user1 = Utils.getUser(user1.getId());

        Utils.inviteUser(user2.getEmail(), group.getId(), user1.getName());

        synchronized (this) {
            this.wait(1000);
        }

        user2 = Utils.getUser(user2.getId());

        user1 = Utils.deleteGroup(user1.getId(), group.getId());

        user2 = Utils.getUser(user2.getId());

        if (user2.getInvites() == null)
            assertTrue(true);
        else
            assertEquals(0, user2.getInvites().size());
    }

}
