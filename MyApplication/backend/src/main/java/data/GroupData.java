package data;

import com.googlecode.objectify.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 3/30/2015.
 */
@Entity
public class GroupData extends DataObject {

    @Id
    public long id;
    public List<GroupUserData> users;
    public List<GroupEventData> events;
    public List<String> eventTypes;
    public List<PollData> polls;
    public List<Notification> notifications;

    public GroupData() {};

    public GroupData(long id, String name) {
        this.id = id;
        this.name = name;
        users = new ArrayList<GroupUserData>();
        events = new ArrayList<GroupEventData>();
        eventTypes = new ArrayList<String>();
        polls = new ArrayList<PollData>();
        notifications = new ArrayList<Notification>();
    }


    public void addUser(UserData user) {
        users.add(new GroupUserData(user.id, user.name));
    }

    public void addAdmin(UserData user) {
        users.add(new GroupUserData(user.id, user.name, true));
    }

    public void addEvent(EventData event) {
        if (events == null)
            events = new ArrayList<GroupEventData>();
        events.add(new GroupEventData(event.getName(), event.getDate(), event.getId()));
        addEventType(event.getType());
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

    public void addEventType(String type) {
        if (eventTypes == null) {
            eventTypes = new ArrayList<String>();
        }
        eventTypes.add(type);
    }

    public PollData getPoll(long id) {
        for (PollData poll: polls) {
            if (poll.getId() == id)
                return poll;
        }
        return null;
    }

    public void addPoll(PollData poll) {
        polls.add(poll);
    }

    public void addNotification(String name, String body) {
        if (notifications == null) {
            notifications = new ArrayList<>();
        }

        notifications.add(new Notification(name, System.currentTimeMillis(), body));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public static class GroupUserData {

        public String name;
        public String id;
        public String type;
        public boolean admin;

        public GroupUserData() {};

        public GroupUserData(String userId, String name) {
            id = userId;
            this.name = name;
            admin = false;
        }

        public GroupUserData(String userId, String name, boolean admin) {
            id = userId;
            this.name = name;
            this.admin = admin;
        }



    }

    public static class GroupEventData {
        public String name;
        public long date;
        public long eventId;

        public GroupEventData() {};

        public GroupEventData(String name, long date, long id) {
            this.name = name;
            this.date = date;
            this.eventId = id;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Notification {
        public String name;
        public String body;
        public long date;

        public Notification(String name, long date, String body) {
            this.name = name;
            this.date = date;
            this.body = body;
        }

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
