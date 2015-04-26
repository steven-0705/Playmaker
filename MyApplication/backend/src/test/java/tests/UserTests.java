package tests;

import com.google.gson.Gson;

import org.junit.Test;

import java.util.HashMap;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import data.GroupData;
import data.UserData;

import static org.junit.Assert.assertEquals;

/**
 * Created by Chris on 4/17/2015.
 */
public class UserTests {

    Random rand = new Random();

    @Test
     public void usersCreateUser() {
        UserData user = Utils.addUser("test1", "christopher", "chrisf671@gmail.com");
        assertEquals("test1", user.getId());
        assertEquals("christopher", user.getName());
        assertEquals("chrisf671@gmail.com", user.getEmail());
    }

    @Test
    public void usersGetUser() {
        UserData user = Utils.addUser("test2", "james", "test2@gmail.com");
        long date = user.getDateCreated();

        user = Utils.addUser("test2", "james", "test2@gmail.com");
        assertEquals("test2", user.getId());
        assertEquals("james", user.getName());
        assertEquals("test2@gmail.com", user.getEmail());
        assertEquals(date, user.getDateCreated());
    }

    @Test
    public void usersInviteUser() {
        UserData u1 = Utils.addUser("testestests" + rand.nextInt(10000), "dsjsadf;lj", "emailsssssss" + rand.nextInt(10000));
        UserData u2 = Utils.addUser("testestests" + rand.nextInt(10000), "dsjsadf;lj", "email2inv@gmail.com" + rand.nextInt(10000));
        GroupData group = Utils.addGroup(u1.getId(), "GROUPZZZ"+ rand.nextInt(10000));

        Utils.inviteUser(u2.getEmail(), group.getId(), u1.getName());

        u2 = Utils.getUser(u2.getId());

        assertEquals(1, u2.getInvites().size());

    }

    @Test
    public void usersInviteUserAccept() {
        UserData u1 = Utils.addUser("testestes123ts" + rand.nextInt(10000), "dsj123sadf;lj", "email123sssssss" + rand.nextInt(10000));
        UserData u2 = Utils.addUser("teswerwrtestests" + rand.nextInt(10000), "dsjs123adf;lj", "email2123inv@gmail.com" + rand.nextInt(10000));
        GroupData group = Utils.addGroup(u1.getId(), "GROUPZZZ"+ rand.nextInt(10000));

        Utils.inviteUser(u2.getEmail(), group.getId(), u1.getName());

        u2 = Utils.getUser(u2.getId());

        assertEquals(1, u2.getInvites().size());
        u2 = Utils.joinGroup(u2.getId(), group.getId());

        assertEquals(0, u2.getInvites().size());
    }

}
