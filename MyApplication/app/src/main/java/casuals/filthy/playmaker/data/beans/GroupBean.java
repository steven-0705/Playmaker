package casuals.filthy.playmaker.data.beans;

import java.util.List;

/**
 * Created by Chris on 4/9/2015.
 */
public class GroupBean extends DataBean {

    public long id;
    public List<GroupUserBean> users;
    public List<GroupEventBean> events;

    public long getId() {
        return id;
    }

    public List<GroupUserBean> getUsers() {
        return users;
    }

    public List<GroupEventBean> getEvents() {
        return events;
    }

    public class GroupUserBean {

        public String name;
        public String id;
        public String type;
        public boolean admin;

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public boolean isAdmin() {
            return admin;
        }

    }

    public class GroupEventBean {

        public String name;
        public long date;
        public long eventId;

        public String getName() {
            return name;
        }

        public long getDate() {
            return date;
        }

        public long getEventId() {
            return eventId;
        }
    }

}
