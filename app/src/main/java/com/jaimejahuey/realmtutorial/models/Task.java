package com.jaimejahuey.realmtutorial.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by jaimejahuey on 12/26/17.
 */

public class Task extends RealmObject {

    //'Required' enforces checks on these fields and disallow null values.
    //Primary key implies that field is indexed, it queries faster but it is slower on creating/updating the object
    @Required @PrimaryKey private String mId;
    @Required private String mName;
    private boolean mDone;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public boolean isDone() {
        return mDone;
    }

    public void setDone(boolean done) {
        mDone = done;
    }
}
