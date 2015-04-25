package casuals.filthy.playmaker.data;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import casuals.filthy.playmaker.data.beans.EventBean;
import casuals.filthy.playmaker.data.beans.GroupBean;
import casuals.filthy.playmaker.data.beans.PollBean;
import casuals.filthy.playmaker.data.beans.UserBean;

/**
 * Created by Chris on 3/26/2015.
 */
public class DatastoreAdapter {

    public static final String SERVER_URL = "http://playmaker-app.appspot.com";
    public static final String SERVLET_USERS = "/users";
    public static final String SERVLET_GROUPS = "/groups";
    public static final String SERVLET_EVENTS = "/groups/events";

    public Gson gson = new Gson();
    public AsyncResponse caller;
    public Class type;
    public ServletHttpAsyncTask task;

    public DatastoreAdapter(AsyncResponse caller) {
        this.caller = caller;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 200:
                    Object response = gson.fromJson(task.getResponse(), type);
                    caller.response(response);
                    break;
                case 400:
                    System.out.print(task.getResponse());
                    // todo do something about the error
                    break;
                default:
                    break;
            }
        }
    };

    /**
     *
     * @param userId
     * @param userName
     * @param userEmail
     * @return Returns the specified user or null of an error occurs
     */
    public void getUser(String userId, String userName, String userEmail) {
        HttpPost post = new HttpPost(SERVER_URL + SERVLET_USERS);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id", userId);
        params.put("user_name", userName);
        params.put("user_email", userEmail);
        params.put("action", "login");
        post.setHeader("Content-Type", "application/json");
        try {
            post.setEntity(new StringEntity(gson.toJson(params)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        type = UserBean.class;
        ServletHttpAsyncTask request = new ServletHttpAsyncTask();
        task = request;
        request.execute(post);
    }

    /**
     *
     * @param groupId
     * @param userId
     * @return Returns updated version of the user
     */
    public void joinGroup(long groupId, String userId) {
        HttpPost post = new HttpPost(SERVER_URL + SERVLET_USERS);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id", userId);
        params.put("group_id", groupId+"");
        params.put("action", "join");
        post.setHeader("Content-Type", "application/json");
        try {
            post.setEntity(new StringEntity(gson.toJson(params)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        type = UserBean.class;
        ServletHttpAsyncTask request = new ServletHttpAsyncTask();
        task = request;
        request.execute(post);
    }

    /**
     *
     * @param groupId
     * @return returns a json object of type group
     */
    public void getGroup(long groupId) {
        HttpUriRequest get = new HttpGet(SERVER_URL + SERVLET_GROUPS
                                            + "?group_id=" + groupId);
        type = GroupBean.class;
        ServletHttpAsyncTask request = new ServletHttpAsyncTask();
        task = request;
        request.execute(get);
    }

    /**
     *
     * @param groupName
     * @param userId
     * @return returns a json object of type group
     */
    public void createGroup(String groupName, String userId) {
        HttpPost post = new HttpPost(SERVER_URL + SERVLET_GROUPS);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id", userId);
        params.put("group_name", groupName);
        params.put("action", "create");
        post.setHeader("Content-Type", "application/json");
        try {
            post.setEntity(new StringEntity(gson.toJson(params)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        type = GroupBean.class;
        ServletHttpAsyncTask request = new ServletHttpAsyncTask();
        task = request;
        request.execute(post);
    }


    /**
     *
     * @param groupId       group_id, user_id, action (=create), poll_name, at least two options*
     * @param userId
     * @param pollName
     *
     * @return Returns json object of the poll
     */
    public void createPoll(String userId, long groupId, String pollName, ArrayList<String> pollElements) {
        HttpPost post = new HttpPost(SERVER_URL + SERVLET_EVENTS);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id", userId);
        params.put("group_id", groupId+"");
        params.put("event_name", pollName);

        for(int i = 0; i < pollElements.size(); i++) {
            params.put("option" + i, pollElements.get(i));
        }

        params.put("action", "create");
        post.setHeader("Content-Type", "application/json");
        try {
            post.setEntity(new StringEntity(gson.toJson(params)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        type = PollBean.class;
        ServletHttpAsyncTask request = new ServletHttpAsyncTask();
        task = request;
        request.execute(post);
    }


    /**
     *
     * @param groupId
     * @param groupId
     * @return Returns json event of the event
     */
    public void voteOnPoll(String userId, long groupId, int voteIndex) {
        HttpPost post = new HttpPost(SERVER_URL + SERVLET_EVENTS);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id", userId);
        params.put("group_id", groupId+"");
        params.put("vote", voteIndex + "");

        params.put("action", "vote");
        post.setHeader("Content-Type", "application/json");
        try {
            post.setEntity(new StringEntity(gson.toJson(params)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        type = PollBean.class;
        ServletHttpAsyncTask request = new ServletHttpAsyncTask();
        task = request;
        request.execute(post);
    }




    /**
     *
     * @param groupId
     * @param eventId
     * @return Returns json event of the event
     */
    public void getEvent(long groupId, long eventId) {
        HttpUriRequest get = new HttpGet(SERVER_URL + SERVLET_EVENTS
                                            + "?group_id=" + groupId + "&"
                                            + "event_id=" + eventId);
        type = EventBean.class;
        ServletHttpAsyncTask request = new ServletHttpAsyncTask();
        task = request;
        request.execute(get);
    }

    /**
     * @param userId
     * @param groupId
     * @param eventName
     * @param eventType
     * @param eventDate
     * @param userId
     * @return Returns json event of the event
     */
    public void createEvent(String userId, long groupId, String eventName, String eventType, long eventDate) {
        HttpPost post = new HttpPost(SERVER_URL + SERVLET_EVENTS);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id", userId);
        params.put("group_id", groupId+"");
        params.put("event_name", eventName);
        params.put("event_type", eventType);
        params.put("event_date", eventDate+"");
        params.put("action", "create");
        post.setHeader("Content-Type", "application/json");
        try {
            post.setEntity(new StringEntity(gson.toJson(params)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        type = EventBean.class;
        ServletHttpAsyncTask request = new ServletHttpAsyncTask();
        task = request;
        request.execute(post);
    }

    /**
     *
     * @param groupId
     * @param eventId
     * @param userId
     * @return returns the updated event data
     */
    public void joinEvent(long groupId, long eventId, String userId) {
        HttpPost post = new HttpPost(SERVER_URL + SERVLET_EVENTS);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id", userId);
        params.put("group_id", groupId+"");
        params.put("event_id", eventId+"");
        params.put("action", "join");
        post.setHeader("Content-Type", "application/json");
        try {
            post.setEntity(new StringEntity(gson.toJson(params)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        type = EventBean.class;
        ServletHttpAsyncTask request = new ServletHttpAsyncTask();
        task = request;
        request.execute(post);
    }




    class ServletHttpAsyncTask extends AsyncTask<HttpUriRequest, Void, HttpResponse> {
        //private Context context;

        private String respString = "";

        @Override
        protected HttpResponse doInBackground(HttpUriRequest... req) {
            HttpClient httpClient = new DefaultHttpClient();
            try {
                // Execute HTTP Post Request
                HttpResponse response = httpClient.execute(req[0]);
                HttpResponse resp = response;
                BufferedReader reader = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                    sb.append(line + "\n");
                respString = sb.toString();

                if (resp.getStatusLine().getStatusCode() == 200)
                    handler.sendEmptyMessage(200);
                else
                    handler.sendEmptyMessage(400);

                return response;

            } catch (ClientProtocolException e) {
                return null;
            } catch (IOException e) {
                return null;
            }
        }

        public String getResponse() {
            synchronized (respString) {
                return respString;
            }
        }
    }

}
