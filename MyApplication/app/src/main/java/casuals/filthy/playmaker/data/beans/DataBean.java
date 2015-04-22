package casuals.filthy.playmaker.data.beans;

public abstract class DataBean {

    public String name;
    public long dateCreated;
    public long createdByUser;

    public String getName() {
        return name;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public long getCreatedByUser() {
        return createdByUser;
    }

}
