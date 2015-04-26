package tests;

import com.google.gson.Gson;

import org.junit.Test;

import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import data.UserData;

import static org.junit.Assert.assertEquals;

/**
 * Created by Chris on 4/17/2015.
 */
public class UserTests {

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

    }

}
