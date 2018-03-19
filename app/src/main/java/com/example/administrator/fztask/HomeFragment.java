package com.example.administrator.fztask;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeFragment extends Fragment {

    private TextView textViewName;
    private LinearLayout linearLayout;
    private Button buttonLogout;
    private static final String loginAPI = "";
    private int numOfTask = 0;
    private String name = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, null, false);

        SharedPreferences sp = getActivity().getSharedPreferences("Login", 0);
        name = sp.getString("name", null);
        numOfTask = sp.getInt("numOfTask", 0);

        linearLayout = (LinearLayout) getActivity().findViewById(R.id.linearLayoutDriverHomepage);
        textViewName = (TextView) getActivity().findViewById(R.id.textViewName);
        textViewName.setText(name + "!");
        buttonLogout = (Button) getActivity().findViewById(R.id.buttonLogout);

        if (name == null) {
            //Open login activity
            Intent myIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(myIntent);
        }

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Delete session
//                SharedPreferences sp = getActivity().getSharedPreferences("Login", 0);
//                SharedPreferences.Editor ed = sp.edit();
//                ed.remove("name");
//                ed.commit();

                //Open login activity
                Intent myIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(myIntent);
            }
        });

//        if (numOfTask > 0) {
//            LinearLayout layout = new LinearLayout(getActivity());
//            layout.setGravity(Gravity.CENTER);
//            layout.setOrientation(LinearLayout.HORIZONTAL);
//            layout.setBackgroundColor(Color.WHITE);
//            layout.setPadding(20, 20, 20, 20);
//            layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        } else {
//            LinearLayout layout = new LinearLayout(getActivity());
//            layout.setGravity(Gravity.CENTER);
//            layout.setOrientation(LinearLayout.HORIZONTAL);
//            layout.setBackgroundColor(Color.WHITE);
//            layout.setPadding(20, 20, 20, 20);
//            layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//
//            ImageView imageView = new ImageView(getActivity());
//            imageView.setImageResource(R.drawable.icon_no_task);
//
//            TextView textView = new TextView(getActivity());
//            textView.setText(" Kamu belum memiliki tugas hari ini");
//            textView.setGravity(Gravity.CENTER);
//
//            layout.addView(imageView);
//            layout.addView(textView);
//
//            linearLayout.addView(layout);
//        }
        return view;
    }
}
