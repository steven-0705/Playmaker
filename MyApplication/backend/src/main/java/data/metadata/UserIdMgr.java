package data.metadata;

import com.googlecode.objectify.annotation.Subclass;

/**
 * Created by Chris on 3/29/2015.
 */
@Subclass
public class UserIdMgr extends MetaDataObject {

    // reserve 0 for null
    private long nextId = 1;

    public long getNextId() {
        long next = nextId;
        increment();
        return next;
    }

    public long peakNextId() {
        return nextId;
    }

    public void increment() {
        nextId++;
        modified = true;
    }

}
