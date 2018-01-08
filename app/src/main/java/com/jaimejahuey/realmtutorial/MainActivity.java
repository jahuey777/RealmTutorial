package com.jaimejahuey.realmtutorial;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.jaimejahuey.realmtutorial.models.Task;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

//Main
public class MainActivity extends AppCompatActivity {

    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRealm = Realm.getDefaultInstance();

        // RealmResults are "live" views, that are automatically kept up to date, even when changes happen
        // on a background thread. The RealmBaseAdapter will automatically keep track of changes and will
        // automatically refresh when a change is detected.
        RealmResults<Task> tasks = mRealm.where(Task.class).findAll();
        tasks = tasks.sort("mTimeStamp");
        final TaskAdapter adapter = new TaskAdapter(this, tasks);

        ListView listView = (ListView) findViewById(R.id.task_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Task task = (Task) parent.getAdapter().getItem(position);
                final EditText taskEditText = new EditText(MainActivity.this);

                taskEditText.setText(task.getName());
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Edit Task")
                        .setView(taskEditText)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Save edited task
                                changeTaskName(task.getId(), String.valueOf(taskEditText.getText()));
                            }
                        })
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Delete task
                                deleteTask(task.getId());
                            }
                        })
                        .create();

                dialog.show();
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText taskEditText = new EditText(MainActivity.this);
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Add task")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mRealm.executeTransactionAsync(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        Task task = realm.createObject(Task.class, UUID.randomUUID().toString());
                                        task.setTimeStamp(System.currentTimeMillis());
                                        task.setName(String.valueOf(taskEditText.getText()));
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();

                dialog.show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    public void changeTaskDone(final String taskId) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Task task = realm.where(Task.class).equalTo("mId", taskId).findFirst();
                task.setDone(!task.isDone());
            }
        });
    }

    private void changeTaskName(final String taskId, final String name){
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Task task = mRealm.where(Task.class).equalTo("mId", taskId).findFirst();
                task.setName(name);
            }
        });
    }

    private void deleteTask(final String taskId) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Task.class).equalTo("mId", taskId)
                        .findFirst()
                        .deleteFromRealm();
            }
        });
    }

    private void deleteAllDone() {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Task.class).equalTo("mDone", true)
                        .findAll()
                        .deleteAllFromRealm();
            }
        });
    }

}
