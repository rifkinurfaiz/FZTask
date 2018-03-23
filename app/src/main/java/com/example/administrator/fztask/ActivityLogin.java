package com.example.administrator.fztask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ActivityLogin extends AppCompatActivity {

    private EditText textViewUsername;
    private EditText textViewPassword;
    private ProgressBar progressBar;
    private Button buttonLogin;
    private String status, agentId, agentName, taskAssignmentId, taskParent, taskType, siteName, lon, lat, address, city = "";
    private static final String loginAPI = "http://192.168.43.19/android/login.php";
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = this.getSharedPreferences("Login", 0);
        //If already login then open homepage directly
        if (sp.getString("agentName", null) != null) {
            //Open driver homepage activity
            Intent myIntent = new Intent(ActivityLogin.this, ActivityMain.class);
            ActivityLogin.this.startActivity(myIntent);
            finish();
        } else {
            setContentView(R.layout.activity_login);

            textViewUsername = (EditText) findViewById(R.id.editTextUsername);
            textViewPassword = (EditText) findViewById(R.id.editTextPassword);
            buttonLogin = (Button) findViewById(R.id.buttonLogin);
            progressBar = (ProgressBar) findViewById(R.id.loginProgressBar);
            progressBar.setVisibility(View.GONE);

            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String uname = textViewUsername.getText().toString();
                    String pass = textViewPassword.getText().toString();
                    if (uname.equals("") || pass.equals("")) {
                        Toast.makeText(getApplicationContext(), "Username dan password harus diisi", Toast.LENGTH_LONG).show();
                    } else if (uname.equals("admin") && pass.equals("driver")) {
                        //Set session
                        SharedPreferences sp = getSharedPreferences("Login", 0);
                        SharedPreferences.Editor ed = sp.edit();
                        ed.putString("agentName", uname);
                        ed.putString("agentId", null);
                        ed.putString("taskAssignmentId", null);
                        ed.commit();

                        //Open driver homepage activity
                        Intent myIntent = new Intent(ActivityLogin.this, ActivityMain.class);
                        ActivityLogin.this.startActivity(myIntent);
                        finish();
                    } else {
                        JSONObject postData = new JSONObject();
                        try {
                            postData.put("username", textViewUsername.getText().toString());
                            postData.put("password", textViewPassword.getText().toString());

                            new login().execute(loginAPI, postData.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private class login extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";
            HttpURLConnection httpURLConnection = null;

            try {
                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes("PostData=" + params[1]);
                wr.flush();
                wr.close();

                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data += current;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return data;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = (ProgressBar) findViewById(R.id.loginProgressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("TAG", result);
            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject statusLoginObj = jsonArray.getJSONObject(0);
                String statusLogin = statusLoginObj.getString("status");
                Log.d("STATUS", statusLogin);
                if (statusLogin.equals("200")) {
                    for (int i = 1; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        agentName = jsonObject.getString("agent_name");
                        agentId = jsonObject.getString("agent_id");
                        taskAssignmentId = jsonObject.getString("task_assignment_id");
                        taskParent = jsonObject.getString("task_parent");
                        taskType = jsonObject.getString("task_type").substring(0,3).toUpperCase();
                        siteName = jsonObject.getString("site_name");
                        lon = jsonObject.getString("lon");
                        lat = jsonObject.getString("lat");
                        address = jsonObject.getString("address");
                        city = jsonObject.getString("city");
                        //Store to local db
                        try {
                            db = new DBHelper(getApplicationContext());
                            db.insertTaskAssignment(agentName, agentId, taskAssignmentId, taskParent,
                                    taskType, siteName, lon, lat, address, city);
                        } catch (SQLException e) {
                            Log.d("Error input to local db", taskAssignmentId);
                        }
                    }

                    //Set session
                    SharedPreferences sp = getSharedPreferences("Login", 0);
                    SharedPreferences.Editor ed = sp.edit();
                    ed.putString("agentName", agentName);
                    ed.putString("agentId", agentId);
                    ed.putString("taskAssignmentId", taskAssignmentId);
                    ed.commit();

                    //Open driver homepage activity
                    Intent myIntent = new Intent(ActivityLogin.this, ActivityMain.class);
                    ActivityLogin.this.startActivity(myIntent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Username atau password salah!", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.GONE);
        }
    }
}