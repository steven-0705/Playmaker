package casuals.filthy.playmaker.data;

import android.os.AsyncTask;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

/**
 * Created by Chris on 3/26/2015.
 */
public class DatastoreAdapter {

    public static final String SERVER_URL = "http://playmaker-app.appspot.com";
    public static final String SERVLET_USERS = "/users";
    public static final String SERVLET_GROUPS = "/groups";

    public static Gson gson = new Gson();


    public static void getUser(String userId, String userName, String userEmail) {
        HttpUriRequest get = new HttpGet(SERVER_URL);
        HttpParams params = new BasicHttpParams();
        params.setParameter("user_id", userId);
        params.setParameter("user_name", userName);
        params.setParameter("user_email", userEmail);
        get.setParams(params);

        // kick of async task
        ServletHttpAsyncTask request = new ServletHttpAsyncTask();
        request.execute(get);
        try {
            get.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        HttpResponse resp = request.getResponse();




    }



}
