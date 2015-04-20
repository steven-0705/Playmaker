package tests;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import data.GroupData;
import data.UserData;

import static org.junit.Assert.assertFalse;

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
            assertFalse("IO exceoption", false);
            return null;
        }
    }

    public static String postReq(String urlString, Map<String, String> params) {
        try {
            URL url = new URL(urlString + makeParams(params));
            //System.out.println(url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            connection.getOutputStream().flush();

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
            assertFalse("IO exceoption", false);
            return null;
        }
    }

    public static UserData addUser(String id, String name, String email) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id", id);
        params.put("user_name", name);
        params.put("user_email", email);

        String resp = Utils.getReq(Utils.USERS_URL, params);
        UserData user = gson.fromJson(resp, UserData.class);
        return user;
    }

    public static GroupData addGroup(String userId, String groupName) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id", userId);
        params.put("group_name", groupName);

        String resp = Utils.postReq(Utils.GROUPS_URL, params);
        GroupData group = gson.fromJson(resp, GroupData.class);
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
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id", userId);
        params.put("group_id", groupId+"");

        String resp = Utils.postReq(Utils.USERS_URL, params);
        UserData user = gson.fromJson(resp, UserData.class);
        return user;
    }

}
