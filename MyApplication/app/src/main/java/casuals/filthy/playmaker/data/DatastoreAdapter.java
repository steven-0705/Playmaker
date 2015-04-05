package casuals.filthy.playmaker.data;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import casuals.filthy.playmaker.data.beans.UserBean;

/**
 * Created by Chris on 3/26/2015.
 */
public class DatastoreAdapter {

    public static final String SERVER_URL = "http://playmaker-app.appspot.com";
    public static final String SERVLET_USERS = "/users";
    public static final String SERVLET_GROUPS = "/groups";

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
        HttpUriRequest get = new HttpGet(SERVER_URL + SERVLET_USERS
                                            + "?user_id=" + userId + "&"
                                            + "user_name=" + userName + "&"
                                            + "user_email=" + userEmail);
        HttpParams params = new BasicHttpParams();
        params.setParameter("user_id", userId);
        params.setParameter("user_name", userName);
        params.setParameter("user_email", userEmail);
        //get.setParams(params);
        // kick of async task
        type = UserBean.class;
        ServletHttpAsyncTask request = new ServletHttpAsyncTask();
        task = request;
        request.execute(get);
    }




    class ServletHttpAsyncTask extends AsyncTask<HttpUriRequest, Void, HttpResponse> {
        //private Context context;

        private String respString = null;

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
