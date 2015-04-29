package casuals.filthy.playmaker;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import casuals.filthy.playmaker.data.DatastoreAdapter;
import casuals.filthy.playmaker.data.AsyncResponse;
import casuals.filthy.playmaker.data.beans.GroupBean;
import casuals.filthy.playmaker.data.beans.GroupUserBean;

import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends BaseActivity implements ActionBar.TabListener, AsyncResponse{
    ActionBar actionbar;
    static CustomViewPager viewpager;
    FragmentPageAdapter ft;
    private static long groupId;
    private static String gUserId;
    private static String userName;
    private static List<Long> eventIds;
    private ProgressDialog progress;
    private static boolean admin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
           gUserId = extras.getString("USER_ID");
            groupId = extras.getLong("GROUP_ID");
            userName = extras.getString("USER_NAME");
        }
        setContentView(R.layout.activity_main);
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Retrieving your data...");
        progress.show();

        viewpager = (CustomViewPager) findViewById(R.id.custompager);
        ft = new FragmentPageAdapter(getSupportFragmentManager());
        actionbar = getActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayUseLogoEnabled(false);
        actionbar.setDisplayShowCustomEnabled(true);
        actionbar.setDisplayShowHomeEnabled(false);
        viewpager.setAdapter(ft);
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        actionbar.addTab(actionbar.newTab().setIcon(R.drawable.event_icon).setText("Events").setTabListener(this));
        actionbar.addTab(actionbar.newTab().setIcon(R.drawable.group_icon).setText("Group").setTabListener(this));
        actionbar.addTab(actionbar.newTab().setIcon(R.drawable.leaderboard_icon).setText("Leader Board").setTabListener(this));
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                actionbar.setSelectedNavigationItem(arg0);
                checkforSignOut();
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
                checkforSignOut();

            }
            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
                checkforSignOut();
            }
        });

        DatastoreAdapter adapter = new DatastoreAdapter(this);
        adapter.getGroup(GroupActivity.getGroupId());

    }


    @Override
    public void onResume() {
        super.onResume();
        DatastoreAdapter adapter = new DatastoreAdapter(this);
        adapter.getGroup(GroupActivity.getGroupId());
    }


    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
        checkforSignOut();

    }
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        viewpager.setCurrentItem(tab.getPosition());
        checkforSignOut();

    }
    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
        checkforSignOut();
    }
    public static Button mSignOut;
    public void checkforSignOut(){
        if(mSignOut == null)
        {
            try{

                mSignOut = (Button) findViewById(R.id.signOut);
                mSignOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseActivity.login=3;
                        mSignOut = null;
                        signOut();
                        Intent i = new Intent(getApplicationContext(), BaseActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
            mSignOut=null;
            }
            catch(Exception e)
            {
                return;
            }
        }
    }


    @Override
    public void signOut() {
        super.signOut();
    }

    @Override
    public void response(Object o) {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        List<android.support.v4.app.Fragment> frags = fm.getFragments();

        for (android.support.v4.app.Fragment frag: frags) {
            if (frag instanceof AsyncResponse && frag.getActivity() instanceof GroupActivity) {
                ((AsyncResponse) frag).response(o);
            }
        }

        if (o instanceof GroupBean) {
            admin = ((GroupBean)o).isUserAdmin(getUserId());
        }

        progress.dismiss();

    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }


    public static long getGroupId()
    {
        return groupId;
    }

    public static String getUserId()
    {
        return gUserId;
    }


    public static List<Long> getEventIds() {
        return eventIds;
    }

    public static boolean isAdmin() {
        return admin;
    }

    public static void setEventIds(List<Long> list) {
        eventIds = list;
    }

    public void inviteMember(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
        final EditText input= new EditText(v.getContext());
        input.setImeOptions(EditorInfo.IME_ACTION_DONE);
        input.setSingleLine();
        alert.setTitle("Enter your friend's email address.");
        alert.setView(input);
        alert.setPositiveButton("Invite", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(input.getText().toString()).matches()) {
                    Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
                } else {
                    DatastoreAdapter adapter = new DatastoreAdapter(null);
                    adapter.inviteUser(groupId, input.getText().toString(), userName);
                    Toast.makeText(getApplicationContext(), "Invitation Sent", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.setNegativeButton("Cancel", null).show();
    }

    public void sendNotification(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
        final EditText input= new EditText(v.getContext());
        input.setImeOptions(EditorInfo.IME_ACTION_DONE);
        input.setLines(3);
        alert.setTitle("Your Message:");
        alert.setView(input);
        alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().length() < 4) {
                    Toast.makeText(getApplicationContext(), "Message too short", Toast.LENGTH_SHORT).show();
                } else {
                    DatastoreAdapter adapter = new DatastoreAdapter(GroupActivity.this);
                    adapter.sendNotification(groupId, input.getText().toString(), userName, getUserId());
                    Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.setNegativeButton("Cancel", null).show();
    }

    public void showMembers(View v) {

        GroupBean group = GroupFragment.group;
        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
        alert.setTitle("Here are all The Members");
        ListView members = new ListView(v.getContext());
        List<GroupUserBean> membersLista = group.getUsers();
        List<String> userNames = new ArrayList<String>();
        for(GroupUserBean user: membersLista){
            userNames.add(user.getName());
        }
       String[] from = {"MESSAGE", "NAME", "DATE"};
        int[] to = {R.id.notify_message, R.id.notify_name, R.id.notify_date};
        ListAdapter membersAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.members_view,R.id.member_name ,userNames);
        members.setAdapter(membersAdapter);
        alert.setView(members);
        alert.setPositiveButton("Done", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();


    }

}









