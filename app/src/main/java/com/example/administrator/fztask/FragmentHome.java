package com.example.administrator.fztask;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FragmentHome extends Fragment {

    private TextView textViewName;
    private LinearLayout linearLayout;
    private static final String loginAPI = "";
    private String agentId, agentName, taskAssignmentId, taskParent, taskType, siteName, lon, lat, address,  city = "";
    private DBHelper db;
    private List<Task> taskList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterTask mAdapter;
    private TextView textViewTargetKunjung;
    private TextView textViewTerkunjung;

    public static FragmentHome newInstance() {
        FragmentHome fragment = new FragmentHome();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, null, false);

        SharedPreferences sp = getActivity().getSharedPreferences("Login", 0);
        agentName = sp.getString("agentName", null);
        agentId = sp.getString("agentId", null);
        taskAssignmentId = sp.getString("taskAssignmentId", null);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mAdapter = new AdapterTask(taskList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        if (agentName == null) {
            //Open login activity
            Intent myIntent = new Intent(getActivity(), ActivityLogin.class);
            startActivity(myIntent);
        } else {
            linearLayout = (LinearLayout) view.findViewById(R.id.linearlayoutTask);
            textViewTargetKunjung = (TextView) view.findViewById(R.id.textViewTargetKunjung);
            textViewTerkunjung = (TextView) view.findViewById(R.id.textViewTerkunjung);
            textViewName = (TextView) view.findViewById(R.id.textViewName);
            textViewName.setText(agentName + "!");

            //User has task
            if (taskAssignmentId != null) {
                db = new DBHelper(getContext());
                ArrayList<HashMap<String, String>> al = db.getData(taskAssignmentId);

                String taskTypes = "";
                int numOfVisitTarget = 0;
                for (int i = 0; i < al.size(); i++) {
                    HashMap<String, String> hm = al.get(i);
                    agentName = hm.get("agentName");
                    agentId = hm.get("agentId");
                    taskAssignmentId = hm.get("taskAssignmentId");
                    taskParent = hm.get("taskParent");
                    taskType = hm.get("taskType");
                    siteName = hm.get("siteName");
                    lon = hm.get("lon");
                    lat = hm.get("lat");
                    address = hm.get("address");
                    city = hm.get("city");
                    if(!taskTypes.contains(taskType)) {
                        taskTypes += taskType + ", ";
                    }
                    if(i == al.size()-1) {
                        numOfVisitTarget++;
                        Task task = new Task(taskAssignmentId, taskTypes.substring(0, taskTypes.length()-2), city);
                        taskList.add(task);
                        textViewTargetKunjung.setText("" + numOfVisitTarget);
                    }
                }
                mAdapter.notifyDataSetChanged();
            } else {
                LinearLayout layout = new LinearLayout(getActivity());
                layout.setGravity(Gravity.CENTER);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setBackgroundColor(Color.WHITE);
                layout.setPadding(20, 20, 20, 20);
                layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                ImageView imageView = new ImageView(getActivity());
                imageView.setImageResource(R.drawable.icon_no_task);

                TextView textView = new TextView(getActivity());
                textView.setText(" Kamu belum memiliki tugas hari ini");

                layout.addView(imageView);
                layout.addView(textView);

                linearLayout.addView(layout);
            }
        }
        return view;
    }
}
