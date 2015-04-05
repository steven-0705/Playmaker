package casuals.filthy.playmaker.data.beans;

/**
 * Created by Chris on 4/5/2015.
 */
public class DataBean {

    public String name;
    public long dateCreated;

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
