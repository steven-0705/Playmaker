package casuals.filthy.playmaker.data;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
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
import java.util.Objects;
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
    private static final String SERVLET_GROUP_USERS = "/groups/users";

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
                    if (caller != null) {
                        Object response = gson.fromJson(task.getResponse(), type);
                        caller.response(response);
                    }
                    break;
                case 400:
                    //System.out.print(task.getResponse());
                    Log.e("DATASTORE_ADAPTER", "Error: " + task.getResponse());
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
     * @return returns a json object of type user
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

        type = UserBean.class;
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
     * @param userId
     * @return Returns json event of the event
     */
    public void createEvent(String userId, long groupId, String eventName, String eventType, long closeDate,
                            String address, boolean autogen, int numTeams, List<Long> eventDates, List<String> items) {
        HttpPost post = new HttpPost(SERVER_URL + SERVLET_EVENTS);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user_id", userId);
        params.put("group_id", groupId+"");
        params.put("event_name", eventName);
        params.put("event_type", eventType);
        params.put("event_dates", eventDates);
        params.put("gen_teams", (autogen ? "true" : "false"));
        params.put("event_address", address);
        params.put("close", closeDate+"");
        params.put("items", items);
        params.put("event_teams", numTeams+"");
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

    public void reportEventStats(String userId, long groupId, long eventId, List<Double> ups, List<Double> downs) {
        HttpPost post = new HttpPost(SERVER_URL + SERVLET_GROUP_USERS);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user_id", userId);
        params.put("group_id", groupId+"");
        params.put("event_id", eventId+"");
        params.put("results_up", ups);
        params.put("results_down", downs);
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

    public void inviteUser(long groupId, String email, String userName) {
        HttpPost post = new HttpPost(SERVER_URL + SERVLET_USERS);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user_email", email);
        params.put("group_id", groupId+"");
        params.put("action", "invite");
        params.put("user_name", userName);
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

    public void inviteRemove(long groupId, String userId) {
        HttpPost post = new HttpPost(SERVER_URL + SERVLET_USERS);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user_id", userId);
        params.put("group_id", groupId+"");
        params.put("action", "invite");
        params.put("remove", "true");
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

    public void voteOnDate(String userId, long groupId, long eventId, int index) {
        HttpPost post = new HttpPost(SERVER_URL + SERVLET_EVENTS);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user_id", userId);
        params.put("group_id", groupId+"");
        params.put("event_id", eventId+"");
        params.put("action", "vote");
        params.put("vote", index);

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

    public void sendNotification(long groupId, String message, String userName, String userId) {
        HttpPost post = new HttpPost(SERVER_URL + SERVLET_GROUPS);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id", userId);
        params.put("user_name", userName);
        params.put("group_id", groupId+"");
        params.put("message", message);
        params.put("action", "notify");
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

    public void leaveGroup(long groupId, String userId) {
        HttpDelete post = new HttpDelete(SERVER_URL + SERVLET_GROUPS
                + "?user_id="+userId+"&group_id="+groupId);

        type = UserBean.class;
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
