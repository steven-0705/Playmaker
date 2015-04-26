package casuals.filthy.playmaker.backend;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

import data.EventData;
import data.GroupData;
import data.GroupUserDetailed;
import data.PollData;
import data.UserData;

/**
 * Created by Chris on 3/29/2015.
 */
public class OfyService {
    static {
        ObjectifyService.register(UserData.class);
        ObjectifyService.register(GroupData.class);
        ObjectifyService.register(EventData.class);
        ObjectifyService.register(GroupUserDetailed.class);
        //ObjectifyService.register(PollData.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}
