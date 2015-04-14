package data;

import com.googlecode.objectify.annotation.*;

import java.util.Date;

/**
 * Created by Chris on 3/29/2015.
 */

public abstract class DataObject {

    public String name;
    public long dateCreated = System.currentTimeMillis();
    public long createdByUser;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(long createdByUser) {
        this.createdByUser = createdByUser;
    }
}
