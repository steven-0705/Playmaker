package casuals.filthy.playmaker;

/**
 * Created by Shane on 3/19/2015.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

public class GroupFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View groupView = inflater.inflate(R.layout.group, container,false);
        Bundle args = getArguments();
        ((CheckBox) groupView.findViewById(R.id.groupCheckBox1)).setText("Basketball");
        ((CheckBox) groupView.findViewById(R.id.groupCheckBox2)).setText("Baseball");
        ((CheckBox) groupView.findViewById(R.id.groupCheckBox3)).setText("LAN Party");
        ((TextView) groupView.findViewById(R.id.notification1)).setText("Variable Notification1");
        ((TextView) groupView.findViewById(R.id.notification2)).setText("Variable Notification2");
        // TODO Auto-generated method stub
        return groupView;
    }
}