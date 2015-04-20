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

    public void addGroup(GroupData group) {
        if (groups == null) {
            groups = new ArrayList<UserGroup>();
        }
        groups.add(new UserGroup(group.name, group.id));
    }

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

    protected static class UserGroup {
        public String name;
        public long id;

        public UserGroup(String name, long id) {
            this.name = name;
            this.id = id;
        }
    }

}
