package casuals.filthy.playmaker.data.beans;

import java.util.ArrayList;
import java.util.List;

public class UserBean extends DataBean{

    protected String id;
    protected String email;
    protected List<UserGroupBean> groups;
    protected List<Invite> invites;

    private UserBean() {
        groups = new ArrayList<UserGroupBean>();
    };

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

    public static class UserGroupBean {
        public String name;
        public long id;

        public UserGroupBean(String name, long id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public long getId() {
            return id;
        }

        @Override
        public String toString() {
            return "UserGroupBean{" +
                    "name='" + name + '\'' +
                    ", id=" + id +
                    '}';
        }
    }

    public static class Invite {
        protected String inviter;
        protected long groupId;
        protected long date;

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
