package data.metadata;

import com.googlecode.objectify.annotation.*;

/**
 * Created by Chris on 3/29/2015.
 */
@Entity
public abstract class MetaDataObject {

    public static final long META_ID = 100;
    @Id final long data_id = META_ID;
    protected long version;
    protected boolean modified = false;

    public long getVersion() {
        return version;
    }

    public boolean isModified() {
        return modified;
    }

}
