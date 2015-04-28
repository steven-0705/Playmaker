package casuals.filthy.playmaker;

/**
 * Created by Shane on 3/19/2015.
 */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import casuals.filthy.playmaker.data.AsyncResponse;
import casuals.filthy.playmaker.data.DatastoreAdapter;
import casuals.filthy.playmaker.data.beans.GroupBean;
import casuals.filthy.playmaker.data.beans.GroupUserBean;

public class LeaderboardFragment extends ListFragment implements AsyncResponse {

    List<HashMap<String,String>>  Entries = LeaderboardEntries.ITEMS;
    String[] from = {LeaderboardEntries.KEY_ICON, LeaderboardEntries.KEY_NAME, LeaderboardEntries.KEY_PLACE, LeaderboardEntries.KEY_POINTS};
    int[] to = {R.id.item_icon, R.id.item_textName, R.id.item_textPlace, R.id.item_textDate};
    private GroupBean group;
    private List<GroupUserBean> ranks;
    private String type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View leaderboardView =  super.onCreateView(inflater, container, savedInstanceState);

        if (group == null) {
            DatastoreAdapter adapter = new DatastoreAdapter(this);
            adapter.getGroup(GroupActivity.getGroupId());
        }

        return leaderboardView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        View header = getLayoutInflater(savedInstanceState).inflate(R.layout.leaderboard_view, null);
        super.onViewCreated(view, savedInstanceState);
        registerForContextMenu(getListView());
        setHasOptionsMenu(true);
        getListView().addHeaderView(header);
        getListView().setDividerHeight(10);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        super.onListItemClick(l, v, position, id);
        if(position == 0)
        {
            return;
        }
        /*GroupActivity.viewpager.setPagingEnabled(false);
        LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.leaderboard_popout, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(null);

        Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
        btnDismiss.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popupWindow.dismiss();
                GroupActivity.viewpager.setPagingEnabled(true);

            }});
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);*/
        GroupUserBean user = ranks.get(position - 1);

        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
        final EditText input= new EditText(v.getContext());
        input.setImeOptions(EditorInfo.IME_ACTION_DONE);
        input.setLines(3);
        alert.setTitle(user.getName());

        View stats = getActivity().getLayoutInflater().inflate(R.layout.leaderboard_alert, null);

        alert.setView(stats);
        TextView attended = ((TextView) stats.findViewById(R.id.alert_num_attended));
        attended.setText(String.valueOf(user.getStats().get(type).getNumPlayed()));
        ((TextView) stats.findViewById(R.id.alert_score)).setText(String.valueOf(user.getStats().get(type).computeScore()));
        alert.setPositiveButton("OK", null).show();

    }

    @Override
    public void response(Object o) {
        if (!(o instanceof GroupBean))
            return;

        group = (GroupBean) o;
        refreshView();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshView();
    }

    private void refreshView() {
        if (group == null || getView() == null)
            return;

        if (group.getEventTypes().size() == 0) {
            setListAdapter(new SimpleAdapter(getActivity().getBaseContext(), new ArrayList<Map<String, String>>(), R.layout.leaderboard_item_view, from, to));
            return;
        }


        Spinner spin = (Spinner) getListView().findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = group.getEventTypes().get(position);
                List<Map<String, String>> results = getRanks(group.getEventTypes().get(position));
                SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), results , R.layout.leaderboard_item_view, from, to);
                setListAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> dataAdapater = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,group.getEventTypes());
        dataAdapater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapater);

        List<Map<String, String>> results = getRanks(group.getEventTypes().get(0));
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), results , R.layout.leaderboard_item_view, from, to);
        setListAdapter(adapter);
        /*SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), Entries , R.layout.leaderboard_item_view, from, to);
        setListAdapter(adapter);*/
    }

    public List<Map<String, String>> getRanks(String type) {

        ranks = group.getRanksList(type);

        List<Map<String, String>> results = new ArrayList<Map<String, String>>();

        int i = 1;
        for (GroupUserBean user: ranks) {
            Map<String, String> entry = new HashMap<String, String>();
            entry.put(LeaderboardEntries.KEY_NAME, user.getName());
            entry.put(LeaderboardEntries.KEY_POINTS, user.getStats().get(type).computeScore()+"");
            switch (i) {
                case 1:
                    entry.put(LeaderboardEntries.KEY_PLACE, "1st Place");
                    entry.put(LeaderboardEntries.KEY_ICON,  String.valueOf(R.drawable.first_place));
                    break;
                case 2:
                    entry.put(LeaderboardEntries.KEY_PLACE, "2nd Place");
                    entry.put(LeaderboardEntries.KEY_ICON,  String.valueOf(R.drawable.second_place));
                    break;
                case 3:
                    entry.put(LeaderboardEntries.KEY_PLACE, "3rd Place");
                    entry.put(LeaderboardEntries.KEY_ICON,  String.valueOf(R.drawable.third_place));
                    break;
                default:
                    entry.put(LeaderboardEntries.KEY_PLACE, i + "th Place");
                    entry.put(LeaderboardEntries.KEY_ICON,  String.valueOf(R.drawable.std_ribbon));
                    break;
            }

            results.add(entry);
            i++;
        }

        return results;
    }
}