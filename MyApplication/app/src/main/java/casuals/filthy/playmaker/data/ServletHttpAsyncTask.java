package casuals.filthy.playmaker.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;
import android.widget.Toast;

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
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 3/26/2015.
 */
class ServletHttpAsyncTask extends AsyncTask<HttpUriRequest, Void, HttpResponse> {
    //private Context context;

    private HttpResponse resp = null;

    @Override
    protected HttpResponse doInBackground(HttpUriRequest... req) {
        HttpClient httpClient = new DefaultHttpClient();
        try {
            // Execute HTTP Post Request
            HttpResponse response = httpClient.execute(req[0]);
            resp = response;
            req[0].notify();
            return response;

        } catch (ClientProtocolException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public HttpResponse getResponse() {
        synchronized (resp) {
            return resp;
        }
    }
}