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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import casuals.filthy.playmaker.data.beans.EventBean;
import casuals.filthy.playmaker.data.beans.GroupBean;
import casuals.filthy.playmaker.data.beans.UserBean;

/**
 * Created by Chris on 3/26/2015.
 */
public class DatastoreAdapter {

    public static final String SERVER_URL = "http://localhost:8080"; //"http://playmaker-app.appspot.com";
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
     * @param userEmail
     * @return Returns the specified user or null of an error occurs
     */
    public void getUser(String userId, String userEmail) {
        HttpUriRequest get = new HttpGet(SERVER_URL + SERVLET_USERS
                                            + "?user_id=" + userId + "&"
                                            + "user_email=" + userEmail);
        /*HttpParams params = new BasicHttpParams();
        params.setParameter("user_id", userId);
        params.setParameter("user_name", userName);
        params.setParameter("user_email", userEmail);*/
        //get.setParams(params);
        // kick of async task
        type = UserBean.class;
        ServletHttpAsyncTask request = new ServletHttpAsyncTask();
        task = request;
        request.execute(get);
    }

    /**
     *
     * @param userId
     * @param userName
     * @param userEmail
     * @return Returns the specified user or null of an error occurs
     */
    public void createUser(String userId, String userName, String userEmail) {
        HttpUriRequest get = new HttpGet(SERVER_URL + SERVLET_USERS
                                            + "?user_id=" + userId + "&"
                                            + "user_name=" + userName + "&"
                                            + "user_email=" + userEmail);
        type = UserBean.class;
        ServletHttpAsyncTask request = new ServletHttpAsyncTask();
        task = request;
        request.execute(get);
    }

    /**
     *
     * @param groupId
     * @param userId
     * @return Returns updated version of the user
     */
    public void joinGroup(long groupId, String userId) {
        HttpUriRequest post = new HttpPost(SERVER_URL + SERVLET_USERS
                                            + "?group_id=" + groupId + "&"
                                            + "user_id=" + userId);
        type = UserBean.class;
        ServletHttpAsyncTask request = new ServletHttpAsyncTask();
        task = request;
        request.execute(post);

//        HttpPost httppost = new HttpPost(SERVER_URL + SERVLET_USERS);
//
//        try {
//            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//            nameValuePairs.add(new BasicNameValuePair("user_id", userId));
//            nameValuePairs.add(new BasicNameValuePair("group_id", groupId + ""));
//            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//            type = GroupBean.class;
//            ServletHttpAsyncTask request = new ServletHttpAsyncTask();
//            task = request;
//            request.execute(httppost);
//
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
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
        HttpUriRequest post = new HttpPost(SERVER_URL + SERVLET_GROUPS
                                            + "?group_name=" + groupName
                                            + "user_id=" + userId);
        type = GroupBean.class;
        ServletHttpAsyncTask request = new ServletHttpAsyncTask();
        task = request;
        request.execute(post);

//        HttpPost httppost = new HttpPost(SERVER_URL + SERVLET_GROUPS);
//
//        try {
//            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//            nameValuePairs.add(new BasicNameValuePair("user_id", userId));
//            nameValuePairs.add(new BasicNameValuePair("group_id", groupId + ""));
//            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//            type = GroupBean.class;
//            ServletHttpAsyncTask request = new ServletHttpAsyncTask();
//            task = request;
//            request.execute(httppost);
//
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
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
        HttpUriRequest post = new HttpPost(SERVER_URL + SERVLET_EVENTS
                                            + "?user_id=" + userId + "&"
                                            + "group_id=" + groupId + "&"
                                            + "event_name=" + eventName + "&"
                                            + "event_type" + eventType + "&"
                                            + "event_date=" + eventDate);
        type = EventBean.class;
        ServletHttpAsyncTask request = new ServletHttpAsyncTask();
        task = request;
        request.execute(post);

//        HttpPost httppost = new HttpPost(SERVER_URL + SERVLET_EVENTS);
//
//        try {
//            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//            nameValuePairs.add(new BasicNameValuePair("user_id", userId));
//            nameValuePairs.add(new BasicNameValuePair("group_id", groupId + ""));
//            nameValuePairs.add(new BasicNameValuePair("event_name", eventName));
//            nameValuePairs.add(new BasicNameValuePair("event_type", eventType));
//            nameValuePairs.add(new BasicNameValuePair("event_date", eventDate+""));
//
//            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//            type = GroupBean.class;
//            ServletHttpAsyncTask request = new ServletHttpAsyncTask();
//            task = request;
//            request.execute(httppost);
//
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
    }

    /**
     *
     * @param groupId
     * @param eventId
     * @param userId
     * @return returns the updated event data
     */
    public void joinEvent(long groupId, long eventId, String userId) {
        HttpUriRequest post = new HttpPost(SERVER_URL + SERVLET_EVENTS
                                            + "?user_id=" + userId + "&"
                                            + "group_id=" + groupId + "&"
                                            + "event_id=" + eventId);
        type = EventBean.class;
        ServletHttpAsyncTask request = new ServletHttpAsyncTask();
        task = request;
        request.execute(post);


//        HttpPost httppost = new HttpPost(SERVER_URL + SERVLET_EVENTS);
//
//        try {
//            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//            nameValuePairs.add(new BasicNameValuePair("user_id", userId));
//            nameValuePairs.add(new BasicNameValuePair("group_id", groupId+""));
//            nameValuePairs.add(new BasicNameValuePair("event_id", eventId+""));
//
//            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//            type = GroupBean.class;
//            ServletHttpAsyncTask request = new ServletHttpAsyncTask();
//            task = request;
//            request.execute(httppost);
//
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
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
