package casuals.filthy.playmaker.data.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupBean extends DataBean {

    public long id;
    public List<GroupUserData> users;
    public List<GroupEventData> events;
    public List<String> eventTypes;
    public List<PollBean> polls;
    public List<Notification> notifications;

    public GroupBean() {};

    public GroupBean(long id, String name) {
        this.id = id;
        this.name = name;
        users = new ArrayList<GroupUserData>();
        events = new ArrayList<GroupEventData>();
        eventTypes = new ArrayList<String>();
        polls = new ArrayList<PollBean>();
        notifications = new ArrayList<Notification>();
    }

    public boolean isUserAdmin(String userId) {
        for (GroupUserData user: users) {
            if (user.id.equals(userId))
                return true;
        }

        return false;
    }

    public GroupEventData getEvent(long id) {
        for (GroupEventData event: events) {
            if (event.eventId == id)
                return event;
        }

        return null;
    }

    public PollBean getPoll(long id) {
        for (PollBean poll: polls) {
            if (poll.getId() == id)
                return poll;
        }
        return null;
    }

    public List<GroupEventData> getEventsUpcoming() {
        Collections.sort(events);
        List<GroupEventData> result = new ArrayList<GroupEventData>();
        for (int i = 0; i < events.size(); i++) {
            if (System.currentTimeMillis() < events.get(i).date + 1000 * 60 * 60 * 12) {
                result.add(events.get(i));
            }
        }

        return result;
    }

    public long getId() {
        return id;
    }

    public List<GroupUserData> getUsers() {
        return users;
    }

    public List<GroupEventData> getEvents() {
        return events;
    }

    public List<String> getEventTypes() {
        return eventTypes;
    }

    public List<PollBean> getPolls() {
        return polls;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public static class GroupUserData {

        public String name;
        public String id;
        public String type;
        public boolean admin;

        public GroupUserData() {};

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

    public static class GroupEventData implements Comparable<GroupEventData>{
        public String name;
        public long date;
        public long eventId;

        public GroupEventData() {};

        public String getName() {
            return name;
        }

        public long getDate() {
            return date;
        }

        public long getEventId() {
            return eventId;
        }

        @Override
        public int compareTo(GroupEventData another) {
            return (int) (this.date - another.date);
        }
    }

    public static class Notification {
        public String name;
        public String body;
        public long date;

        public String getName() {
            return name;
        }

        public String getBody() {
            return body;
        }

        public long getDate() {
            return date;
        }
    }
}
