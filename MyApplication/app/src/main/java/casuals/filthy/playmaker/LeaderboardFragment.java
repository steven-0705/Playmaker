package casuals.filthy.playmaker;

/**
 * Created by Shane on 3/19/2015.
 */
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class LeaderboardFragment extends ListFragment {

    List<HashMap<String,String>>  Entries = LeaderboardEntries.ITEMS;
    String[] from = {LeaderboardEntries.KEY_ICON, LeaderboardEntries.KEY_NAME, LeaderboardEntries.KEY_PLACE};
    int[] to = {R.id.item_icon, R.id.item_textName, R.id.item_textPlace};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View leaderboardView =  super.onCreateView(inflater, container, savedInstanceState);

        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), Entries , R.layout.leaderboard_item_view, from, to);

        setListAdapter(adapter);

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


}