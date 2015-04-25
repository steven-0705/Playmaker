package tests;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.EventData;
import data.GroupData;
import data.UserData;

import static org.junit.Assert.*;

/**
 * Created by Chris on 4/17/2015.
 */
public class Utils {

    // DO NOT RUN THIS AGAINST THE SERVER, IT WILL CLUTTER IT!!!
    public static final String URL = "http://localhost:8080";
    public static final String USERS_URL = URL + "/users";
    public static final String GROUPS_URL = URL + "/groups";
    public static final String EVENTS_URL = URL + "/groups/events";
    public static final String POLLS_URL = URL + "/groups/polls";
    public static final String GROUP_USERS_URL = URL + "/groups/users";
    private static Gson gson = new Gson();

    private static String makeParams(Map<String, String> pars) {
        StringBuilder sb = new StringBuilder();
        sb.append("?");
        boolean first = true;
        for (String par: pars.keySet()) {
            if (first)
                first = false;
            else
                sb.append("&");

            sb.append(par);
            sb.append("=");
            sb.append(pars.get(par));
        }

        return sb.toString();
    }

    public static String getReq(String urlString, Map<String, String> params) {
        try {
            URL url = new URL(urlString + makeParams(params));
            //System.out.println(url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connection.setDoOutput(true);
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                return sb.toString();
            } else {
                return "" + connection.getResponseCode();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            assertFalse("Malformed url", false);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            assertFalse("IO exception", false);
            return null;
        }
    }

    public static String postReq(String urlString, Map<String, Object> params) {
        try {
            URL url = new URL(urlString);// + makeParams(params));
            //System.out.println(url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");


            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            writer.write(gson.toJson(params));
            writer.flush();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                return sb.toString();
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                System.out.println(sb.toString());
                return "" + connection.getResponseCode() + ": " + connection.getResponseMessage();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            assertTrue("Malformed url", false);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue("IO exceoption", false);
            return null;
        }
    }

    public static UserData addUser(String id, String name, String email) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user_id", id);
        params.put("user_name", name);
        params.put("user_email", email);
        params.put("action", "login");

        String resp = Utils.postReq(Utils.USERS_URL, params);
        UserData user = gson.fromJson(resp, UserData.class);
        return user;
    }

    public static GroupData addGroup(String userId, String groupName) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user_id", userId);
        params.put("group_name", groupName);

        String resp = Utils.postReq(Utils.GROUPS_URL, params);
        UserData user = gson.fromJson(resp, UserData.class);

        GroupData group = getGroup(user.getGroups().get(user.getGroups().size() - 1).getId());
        return group;
    }

    public static GroupData getGroup(long groupId) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("group_id", groupId+"");

        String resp = Utils.getReq(Utils.GROUPS_URL, params);
        GroupData group = gson.fromJson(resp, GroupData.class);

        return group;
    }

    public static UserData joinGroup(String userId, long groupId) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user_id", userId);
        params.put("group_id", groupId+"");
        params.put("action", "join");

        String resp = Utils.postReq(Utils.USERS_URL, params);
        UserData user = gson.fromJson(resp, UserData.class);
        return user;
    }

    public static EventData joinEvent(String userId, long eventId, long groupId) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user_id", userId);
        params.put("group_id", groupId+"");
        params.put("event_id", eventId+"");
        params.put("action", "join");

        String resp = Utils.postReq(Utils.EVENTS_URL, params);
        //System.out.println(resp);
        EventData event = gson.fromJson(resp, EventData.class);
        return event;
    }

    public static EventData addEvent(String userId, long groupId, String name, String type, long date, int teams, boolean autoGen, long close) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user_id", userId);
        params.put("group_id", groupId+"");
        params.put("event_name", name);
        params.put("event_type", type);
        List<Long> dates = new ArrayList<Long>();
        dates.add(date);
        params.put("event_dates", dates);
        params.put("event_teams", teams+"");
        params.put("gen_teams", (autoGen ? "true" : "false"));
        params.put("close", close+"");

        String resp = Utils.postReq(Utils.EVENTS_URL, params);
        //System.out.println(resp);
        EventData event = gson.fromJson(resp, EventData.class);
        return event;
    }

    public static EventData setTeams(String userId, long groupId, long eventId, List<List<String>> teams) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user_id", userId);
        params.put("group_id", groupId+"");
        params.put("event_id", eventId+"");
        params.put("teams", teams);
        params.put("action", "teams");

        String resp = Utils.postReq(Utils.EVENTS_URL, params);
        //System.out.println(resp);
        EventData event = gson.fromJson(resp, EventData.class);
        return event;
    }

}
