package casuals.filthy.playmaker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import casuals.filthy.playmaker.data.AsyncResponse;
import casuals.filthy.playmaker.data.DatastoreAdapter;
import casuals.filthy.playmaker.data.beans.GroupBean;
import casuals.filthy.playmaker.data.beans.UserBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shane on 3/27/2015.
 */
public class UserActivity extends BaseActivity implements AsyncResponse{

    private String userId;
    private String userName;
    private String userEmail;
    private List<Long> groupIds;
    private ProgressDialog progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            userId = extras.getString("ID");
            userEmail = extras.getString("EMAIL");
            userName = extras.getString("DISPLAY_NAME");
        }
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Retrieving your data...");
        progress.show();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.user);
        final TextView user_name = (TextView) findViewById(R.id.user_display);
        final TextView user_email = (TextView) findViewById(R.id.user_user);
        final ImageView user_image = (ImageView) findViewById(R.id.user_image);
        final TextView user_group = (TextView) findViewById(R.id.user_groups);
        final Button createGroup = (Button) findViewById(R.id.groupCreate);
        final ListView groupList = (ListView) findViewById(R.id.user_group_list);
        user_name.setText(userName);
        user_email.setText(userEmail);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                final EditText input= new EditText(v.getContext());
                input.setImeOptions(EditorInfo.IME_ACTION_DONE);
                input.setSingleLine();
                alert.setTitle("Enter a name for your group.");
                alert.setView(input);
                alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText().toString().matches("")) {
                            Toast.makeText(getApplicationContext(), "You did not enter anything", Toast.LENGTH_SHORT).show();
                        } else if (input.getText().toString().length() < 3) {
                            Toast.makeText(getApplicationContext(), "Group name must be at least 3 letters long", Toast.LENGTH_SHORT).show();
                        } else {
                            String temp = input.getText().toString();
                            DatastoreAdapter adapter = new DatastoreAdapter(UserActivity.this);
                            adapter.createGroup(temp, userId);

                        }
                    }
                });
                alert.show();
            }
        });

        DatastoreAdapter adapter = new DatastoreAdapter(this);
        adapter.getUser(userId, userName, userEmail);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        BaseActivity.login=0;
    }

    @Override
    public void signOut() {
        super.signOut();
    }


       @Override
       public void response(Object o) {
           if(!(o instanceof UserBean)) {
               return;
           }
           UserBean user = (UserBean) o;
           List<UserBean.UserGroupBean> groupList = user.getGroups();
           ListView listView = (ListView) findViewById(R.id.user_group_list);
           List<String> list = new ArrayList<String>();
           List<Long> idList = new ArrayList<Long>();
           for(UserBean.UserGroupBean group: groupList) {
               list.add(group.getName());
               idList.add(group.getId());
           }
           setGroupIds(idList);
           ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
           listView.setAdapter(arrayAdapter);
           listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   Long groupId = getGroupIds().get(position);

                   Intent i = new Intent(getApplicationContext(), GroupActivity.class);
                   i.putExtra("USER_ID", getId());
                   i.putExtra("GROUP_ID",groupId);
                   i.putExtra("USER_NAME", userName);
                   startActivity(i);
               }
           });
           progress.dismiss();
       }


    public String getName(){
        return userName;
    }
    public String getEmail(){
        return userEmail;
    }
    public String getId(){
        return userId;
    }
    public List<Long> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<Long> list) {
        groupIds = list;
    }
}
