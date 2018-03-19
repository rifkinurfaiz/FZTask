package com.example.administrator.fztask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
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

public class LoginActivity extends AppCompatActivity {

    private EditText textViewUsername;
    private EditText textViewPassword;
    private ProgressBar progressBar;
    private Button buttonLogin;
    private String status, agentId, agentName, taskAssignmentId, taskParent, taskType, siteName, lon, lat = "";
    private static final String loginAPI = "http://192.168.43.19/android/login.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = this.getSharedPreferences("Login", 0);
        //If already login then open homepage directly
        if(sp.getString("name", null) != null) {
            //Open driver homepage activity
            Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
            LoginActivity.this.startActivity(myIntent);
            finish();
        } else {
            setContentView(R.layout.login_page);

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
                        ed.putString("username", uname);
                        ed.putInt("numOfTask", 0);
                        ed.commit();

                        //Open driver homepage activity
                        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                        LoginActivity.this.startActivity(myIntent);
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
                JSONObject jsonObject = new JSONObject(result);
                JSONArray loginJsonArray = jsonObject.getJSONArray("login");

                JSONObject statusJsonObject = loginJsonArray.getJSONObject(0);
                status = statusJsonObject.getString("status");
                Log.d("STATUS", status);
                if (status != null && status.equals("200")) {
                    JSONObject agentIdJsonObject = loginJsonArray.getJSONObject(1);
                    JSONObject agentNameObject = loginJsonArray.getJSONObject(2);
                    agentId = agentIdJsonObject.getString("agentId");
                    agentName = agentNameObject.getString("agentName");

                    JSONArray taskJsonArray = jsonObject.getJSONArray("task");
                    JSONObject taskAssignmentIdJsonObject = taskJsonArray.getJSONObject(0);
                    JSONObject taskParentObject = taskJsonArray.getJSONObject(1);
                    JSONObject taskTypeObject = taskJsonArray.getJSONObject(2);
                    JSONObject siteNameObject = taskJsonArray.getJSONObject(3);
                    JSONObject lonObject = taskJsonArray.getJSONObject(4);
                    JSONObject latObject = taskJsonArray.getJSONObject(5);
                    taskAssignmentId = taskAssignmentIdJsonObject.getString("taskAssignmentId");
                    taskParent = taskParentObject.getString("taskParent");
                    taskType = taskTypeObject.getString("taskType");
                    siteName = siteNameObject.getString("siteName");
                    lon = lonObject.getString("lon");
                    lat = latObject.getString("lat");


                    //Set session
                    SharedPreferences sp = getSharedPreferences("Login", 0);
                    SharedPreferences.Editor ed = sp.edit();
                    ed.putString("name", agentName);
                    ed.putInt("numOfTask", 0);
                    ed.commit();

                    //Open driver homepage activity
                    Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(myIntent);
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
