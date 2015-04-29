package casuals.filthy.playmaker;

/**
 * Created by Shane on 3/19/2015.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

import casuals.filthy.playmaker.data.AsyncResponse;
import casuals.filthy.playmaker.data.beans.GroupBean;

public class GroupFragment extends Fragment implements AsyncResponse{

    private static String[] from = {"MESSAGE", "NAME", "DATE"};
    private static int[] to = {R.id.notify_message, R.id.notify_name, R.id.notify_date};

    public static GroupBean group;

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
        ListView notifications = (ListView) getView().findViewById(R.id.notifications);
        List<Map<String, String>> notificationList = group.getRecentNotification();
        ListAdapter notificationsAdapter = new SimpleAdapter(getActivity().getBaseContext(), notificationList, R.layout.notification_view, from, to);
        notifications.setAdapter(notificationsAdapter);



    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }
}
