package com.jaimejahuey.realmtutorial;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.jaimejahuey.realmtutorial.models.Task;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by jaimejahuey on 12/26/17.
 */

public class TaskAdapter extends RealmBaseAdapter<Task> implements ListAdapter {

    private MainActivity mMainActivity;

    private static class ViewHolder{
        TextView mTaskName;
        CheckBox mIsTaskDone;
    }

    public TaskAdapter(MainActivity mainActivity, OrderedRealmCollection<Task> data) {
        super(data);
        this.mMainActivity = mainActivity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if(convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mTaskName = (TextView)convertView.findViewById(R.id.task_item_name);
            viewHolder.mIsTaskDone = (CheckBox) convertView.findViewById(R.id.task_item_done);
            viewHolder.mIsTaskDone.setOnClickListener(mOnClickListener);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(adapterData!=null){
            Task task = adapterData.get(position);
            viewHolder.mTaskName.setText(task.getName());
            viewHolder.mIsTaskDone.setChecked(task.isDone());
            viewHolder.mIsTaskDone.setTag(position);
        }
        return convertView;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = (Integer) v.getTag();

            if(adapterData!=null){
                Task task = adapterData.get(pos);
                mMainActivity.changeTaskDone(task.getId());
            }
        }
    };
}

