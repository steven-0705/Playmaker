package casuals.filthy.playmaker.backend;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

import data.DataObject;
import data.GroupData;
import data.UserData;
import data.metadata.MetaDataObject;

/**
 * Created by Chris on 3/29/2015.
 */
public class OfyService {
    static {
        ObjectifyService.register(UserData.class);
        ObjectifyService.register(GroupData.class);
        ObjectifyService.register(MetaDataObject.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}
