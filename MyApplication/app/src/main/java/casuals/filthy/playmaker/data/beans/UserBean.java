package casuals.filthy.playmaker.data.beans;

import java.util.ArrayList;
import java.util.List;

public class UserBean extends DataBean{

    public String id;
    public String email;
    public List<UserGroupBean> groups;
    public List<Invite> invites;

    private UserBean() {};

    public UserBean(String email, String name, String user_id) {
        this.id = user_id;
        this.name = name;
        this.email = email;

        groups = new ArrayList<UserGroupBean>();
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public List<UserGroupBean> getGroups() {
        return groups;
    }

    public List<Invite> getInvites() {
        return invites;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", groups=" + groups +
                '}';
    }

    protected static class UserGroupBean {
        public String name;
        public long id;

        public UserGroupBean(String name, long id) {
            this.name = name;
            this.id = id;
        }

        @Override
        public String toString() {
            return "UserGroupBean{" +
                    "name='" + name + '\'' +
                    ", id=" + id +
                    '}';
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
