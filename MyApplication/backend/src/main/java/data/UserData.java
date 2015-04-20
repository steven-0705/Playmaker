package data;

import com.googlecode.objectify.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chris on 3/29/2015.
 */
@Entity
public class UserData extends DataObject{

    @Id public String id;
    public String email;
    public List<UserGroup> groups;
    public List<Invite> invites;

    private UserData() {};

    public UserData(String email, String name, String user_id) {
        this.id = user_id;
        this.name = name;
        this.email = email;

        groups = new ArrayList<UserGroup>();
    }

    public void addGroup(GroupData group) {
        if (groups == null) {
            groups = new ArrayList<UserGroup>();
        }
        groups.add(new UserGroup(group.getName(), group.getId()));
    }

    public void invite(String inviter, long groupId) {
        if (invites == null)
            invites = new ArrayList<Invite>();

        invites.add(new Invite(groupId, inviter, System.currentTimeMillis()));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<UserGroup> getGroups() {
        return groups;
    }

    public List<Invite> getInvites() {
        return invites;
    }

    protected static class UserGroup {
        public String name;
        public long id;

        public UserGroup(String name, long id) {
            this.name = name;
            this.id = id;
        }
    }

    protected static class Invite {
        public String inviter;
        public long groupId;
        public long date;

        public Invite(long groupId, String inviter, long date) {
            this.groupId = groupId;
            this.inviter = inviter;
            this.date = date;
        }

        public String getInviter() {
            return inviter;
        }

        public long getDate() {
            return date;
        }

        public long getGroupId() {
            return groupId;
        }
    }

}
