package casuals.filthy.playmaker.backend;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.servlet.ServletInputStream;

/**
 * Created by Chris on 4/20/2015.
 */
public class ServletUtils {

    public static Gson gson = new Gson();

    public static HashMap<String, String> getParams(ServletInputStream input) {
        BufferedReader read = new BufferedReader(new InputStreamReader(input));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = read.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        HashMap<String, String> params = gson.fromJson(sb.toString(), HashMap.class);
        return params;
    }
}
