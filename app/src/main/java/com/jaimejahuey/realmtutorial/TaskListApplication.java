package com.jaimejahuey.realmtutorial;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by jaimejahuey on 12/26/17.
 */

//Initializing realm. Only needs to be done once in the app's lifecycle.
public class TaskListApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();

        Realm.init(this);

        //RealmConfig oject controls all aspects of realm
        //Creates a file named default.realm located in Context.getFilesDir(), but we named it JahueyRealm
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("JahueyRealm")
                .schemaVersion(0)
                .build();

        Realm.setDefaultConfiguration(realmConfig);
    }
}
