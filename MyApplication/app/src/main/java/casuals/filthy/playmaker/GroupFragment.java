package casuals.filthy.playmaker;

/**
 * Created by Shane on 3/19/2015.
 */
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import casuals.filthy.playmaker.data.AsyncResponse;
import casuals.filthy.playmaker.data.DatastoreAdapter;
import casuals.filthy.playmaker.data.beans.GroupBean;

public class GroupFragment extends Fragment implements AsyncResponse{

    private static String[] from = {"MESSAGE", "NAME", "DATE"};
    private static int[] to = {R.id.notify_message, R.id.notify_name, R.id.notify_date};

    private GroupBean group;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View groupView = inflater.inflate(R.layout.group, container,false);
        // TODO Auto-generated method stub
        return groupView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        
    }

    @Override
    public void response(Object o) {
        if (!(o instanceof GroupBean)) {
            return;
        }
        group = (GroupBean) o;
        updateView();
    }

    private void updateView() {
        if (group == null || getView() == null)
            return;

        ((TextView) getView().findViewById(R.id.group_name)).setText(group.getName());
        ListView notifications = (ListView) getView().findViewById(R.id.notifications);
        List<Map<String, String>> notificationList = group.getRecentNotification();
        ListAdapter notificationsAdapter = new SimpleAdapter(getActivity().getBaseContext(), notificationList, R.layout.notification_view, from, to);
        notifications.setAdapter(notificationsAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}