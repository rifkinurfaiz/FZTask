package com.example.administrator.fztask;

import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 3/21/2018.
 */

public class FragmentOthers extends Fragment {

    String[] othersItem = {"Log out", "About"};

    public static FragmentOthers newInstance() {
        FragmentOthers fragment = new FragmentOthers();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_others, null, false);

        ArrayAdapter adapter = new ArrayAdapter<String>(getContext(), R.layout.list_view_item_fragment_others, othersItem);
        ListView listView = (ListView) view.findViewById(R.id.listViewFragmentOthers);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = ((TextView) view.findViewById(R.id.label)).getText().toString();
                if(item.equals("Log out")) {
                    new AlertDialog.Builder(getContext())
                            .setMessage("Anda yakin akan logout?")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    //Delete session
                                    SharedPreferences sp = getActivity().getSharedPreferences("Login", 0);
                                    SharedPreferences.Editor ed = sp.edit();
                                    ed.remove("agentName");
                                    ed.remove("agentId");
                                    ed.remove("taskAssignmentId");
                                    ed.commit();

                                    //Open login activity
                                    Intent myIntent = new Intent(getActivity(), ActivityLogin.class);
                                    startActivity(myIntent);
                                    getActivity().finish();
                                }
                            })
                            .setNegativeButton("Tidak", null).show();
                } else {

                }
            }
        });

        return view;
    }
}
