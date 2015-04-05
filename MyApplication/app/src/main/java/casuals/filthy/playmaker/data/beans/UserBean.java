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

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<UserGroupBean> getGroups() {
        return groups;
    }

    public void setGroups(List<UserGroupBean> groups) {
        this.groups = groups;
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

        public void setName(String name) {
            this.name = name;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
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

}
