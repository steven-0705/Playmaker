package casuals.filthy.playmaker.data.beans;

import java.util.List;

/**
 * Created by Chris on 4/5/2015.
 */
public class UserBean extends DataBean {

    public String id;
    public String email;
    public List<UserGroupBean> groups;

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public List<UserGroupBean> getGroups() {
        return groups;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", groups=" + groups +
                '}';
    }

    protected class UserGroupBean {
        public String name;
        public long id;

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

}
