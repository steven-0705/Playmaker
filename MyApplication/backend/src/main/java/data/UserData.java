package data;

import com.googlecode.objectify.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chris on 3/29/2015.
 */
@Entity
public class UserData extends DataObject{

    @Id public String id;
    public String email;
    public List<UserGroup> groups;

    private UserData() {};

    public UserData(String email, String name, String user_id) {
        this.id = user_id;
        this.name = name;
        this.email = email;

        groups = new ArrayList<UserGroup>();
    }

    protected class UserGroup {
        public String name;
        public long id;
    }

}
