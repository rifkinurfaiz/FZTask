package com.example.administrator.fztask;

/**
 * Created by Administrator on 3/20/2018.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AdapterTask extends RecyclerView.Adapter<AdapterTask.MyViewHolder> {

    private List<Task> taskList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTaskId, textViewTaskType, textViewCity;

        public MyViewHolder(View view) {
            super(view);
            textViewTaskId = (TextView) view.findViewById(R.id.textViewTaskId);
            textViewTaskType = (TextView) view.findViewById(R.id.textViewTaskType);
            textViewCity = (TextView) view.findViewById(R.id.textViewCity);
        }
    }

    public AdapterTask(List<Task> taskList) {
        this.taskList = taskList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_task_content, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.textViewTaskId.setText("ID: " + task.getTaskAssignmentId());
        holder.textViewCity.setText("Tujuan: " + task.getCity());
        holder.textViewTaskType.setText("Jenis task: " + task.gettaskType());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}