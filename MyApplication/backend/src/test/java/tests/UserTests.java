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

    private Gson gson = new Gson();

    @Test
     public void usersCreateUser() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id", "test1");
        params.put("user_name", "christopher");
        params.put("user_email", "chrisf671@gmail.com");

        String resp = Utils.getReq(Utils.USERS_URL, params);
        UserData user = gson.fromJson(resp, UserData.class);
        assertEquals("test1", user.getId());
        assertEquals("christopher", user.getName());
        assertEquals("chrisf671@gmail.com", user.getEmail());
    }

    @Test
    public void usersGetUser() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id", "test2");
        params.put("user_name", "james");
        params.put("user_email", "test2@gmail.com");

        // create the user
        String resp = Utils.getReq(Utils.USERS_URL, params);
        UserData user = gson.fromJson(resp, UserData.class);
        long date = user.getDateCreated();
        // retrieve the user
        resp = Utils.getReq(Utils.USERS_URL, params);

        user = gson.fromJson(resp, UserData.class);
        assertEquals("test2", user.getId());
        assertEquals("james", user.getName());
        assertEquals("test2@gmail.com", user.getEmail());
        assertEquals(date, user.getDateCreated());
    }

    @Test
    public void usersGetUserByEmail() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id", "test3");
        params.put("user_name", "leedle");
        params.put("user_email", "test3@gmail.com");

        // create the user
        String resp = Utils.getReq(Utils.USERS_URL, params);
        UserData user = gson.fromJson(resp, UserData.class);
        long date = user.getDateCreated();
        // retrieve the user
        params.remove("user_id");
        resp = Utils.getReq(Utils.USERS_URL, params);
        //System.out.println(resp);

        user = gson.fromJson(resp, UserData.class);
        assertEquals("test3", user.getId());
        assertEquals("leedle", user.getName());
        assertEquals("test3@gmail.com", user.getEmail());
        assertEquals(date, user.getDateCreated());
    }
}
