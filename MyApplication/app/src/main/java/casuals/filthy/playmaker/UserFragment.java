package casuals.filthy.playmaker;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by Shane on 3/27/2015.
 */
public class UserFragment extends Fragment{
private CheckBox checkBox1, checkBox2, checkBox3, checkBox4;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View userView = inflater.inflate(R.layout.user, container,false);
            Bundle args = getArguments();

            // TODO Auto-generated method stub
            return userView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState)
       {
            super.onViewCreated(view, savedInstanceState);
           TextView display = (TextView) getView().findViewById(R.id.user_display);
           display.setText(MainActivity.getName());
           TextView name = (TextView) getView().findViewById(R.id.user_favoriteText);
           name.setText(MainActivity.getEmail());
           TextView id = (TextView) getView().findViewById(R.id.user_user);
           id.setText(MainActivity.getId());
//            Button test = (Button) getView().findViewById(R.id.button);
//            test.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    BaseActivity.login=3;
//
//                    Intent i = new Intent(getActivity().getApplicationContext(), BaseActivity.class);
//                    startActivity(i);
//
//                    getActivity().finish();
//
//
//                }
//            });
       }

}
