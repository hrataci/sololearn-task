package task.sololearn.com.task.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PinnedItem extends RealmObject {
    @PrimaryKey
    private int id;
    private NewsModel model;

    public PinnedItem(){

    }
    public PinnedItem(NewsModel model){
        this();
        setModel(model);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public NewsModel getModel() {
        return model;
    }

    public void setModel(NewsModel model) {
        this.model = model;
        setId(model.getRealmId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PinnedItem that = (PinnedItem) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
