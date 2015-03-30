package data;

import com.googlecode.objectify.annotation.*;

/**
 * Created by Chris on 3/30/2015.
 */
@Entity
public class GroupData extends DataObject {

    @Id
    public long id;


    public void addUser(UserData user) {
        //TODO finish
    }
}
