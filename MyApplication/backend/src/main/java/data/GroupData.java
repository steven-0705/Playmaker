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
    protected long id;
    protected List<GroupUserDetailed> users;
    protected List<GroupEventData> events;
    protected List<String> eventTypes;
    protected List<PollData> polls;
    protected List<Notification> notifications;

    public GroupData() {};

    public GroupData(long id, String name) {
        this.id = id;
        this.name = name;
        users = new ArrayList<GroupUserDetailed>();
        events = new ArrayList<GroupEventData>();
        eventTypes = new ArrayList<String>();
        polls = new ArrayList<PollData>();
        notifications = new ArrayList<Notification>();
    }


    public void addUser(UserData user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId()))
                return;
        }
        users.add(new GroupUserDetailed(user));

        // make a notification
        addNotification(getName() , user.getName()+ " has joined the group");
    }

    public void addAdmin(UserData user) {
        users.add(new GroupUserDetailed(user, true));
    }

    public void addEvent(EventData event) {
        if (events == null)
            events = new ArrayList<GroupEventData>();
        events.add(new GroupEventData(event.getName(), event.getDate(), event.getId(), event.getType()));
        addEventType(event.getType());

        // make a notification
        addNotification(getName() ,"Event '" + event.getName()+ "' of type '" + event.getType() + "' was created.");
    }

    public boolean isUserAdmin(String userId) {
        for (GroupUserDetailed user: users) {
            if (user.getId().equals(userId))
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
        if (!eventTypes.contains(type))
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

    public List<GroupUserDetailed> getUsers() {
        return users;
    }

    public List<GroupEventData> getEvents() {
        return events;
    }

    public List<String> getEventTypes() {
        return eventTypes;
    }

    public List<PollData> getPolls() {
        return polls;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public GroupUserDetailed getUserById(String userId) {
        for (GroupUserDetailed user: users) {
            if (user.getId().equals(userId))
                return user;
        }

        return null;
    }

    /*public static class GroupUserData {

        public String name;
        public String id;
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

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public boolean isAdmin() {
            return admin;
        }
    }*/

    public static class GroupEventData {
        public String name;
        public long date;
        public long eventId;
        public String type;
        protected boolean reported = false;

        public GroupEventData() {};

        public GroupEventData(String name, long date, long id, String type) {
            this.name = name;
            this.date = date;
            this.eventId = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public long getDate() {
            return date;
        }

        public long getEventId() {
            return eventId;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setEventId(long eventId) {
            this.eventId = eventId;
        }

        public void reported() {
            reported = true;
        }
    }

    public static class Notification {
        public String name;
        public String body;
        public long date;

        public Notification() {};

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
