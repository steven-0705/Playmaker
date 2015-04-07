package data;

import com.googlecode.objectify.annotation.Id;

/**
 * Created by Chris on 4/6/2015.
 */
public class EventData extends DataObject {

    @Id
    public long id;
    public long date;
    public String type;


}
