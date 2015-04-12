package data;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chris on 4/6/2015.
 */
@Entity
public class EventData extends DataObject {

    @Id
    public long id;
    public long date;
    public String type;
    public Map<String, String> attending;
    public long groupId;

    public EventData(long id, long date, String type, long groupId) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.groupId = groupId;
    }

    public void addAttendee(String id, String name) {
        if (attending == null)
            attending = new HashMap<String, String>();
        attending.put(id, name);
    }

}
