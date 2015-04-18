package tests;

import com.google.appengine.repackaged.com.google.api.client.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static org.junit.Assert.assertFalse;

/**
 * Created by Chris on 4/17/2015.
 */
public class Utils {

    public static final String URL = "http://localhost:8080";
    public static final String USERS_URL = URL + "/users";
    public static final String GROUPS_URL = URL + "/groups";
    public static final String EVENTS_URL = URL + "/groups/events";
    public static final String POLLS_URL = URL + "/groups/polls";
    public static final String GROUP_USERS_URL = URL + "/groups/users";

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
            System.out.println(url.toString());
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

}
