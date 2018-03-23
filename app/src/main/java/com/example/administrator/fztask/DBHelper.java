package com.example.administrator.fztask;

/**
 * Created by Administrator on 3/20/2018.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TMS.db";
    public static final String TASK_ASSIGNMENT_TABLE_NAME = "task_assignment";

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table task_assignment (" +
                        "agent_name text, " +
                        "agent_id text, " +
                        "task_assignment_id text, " +
                        "task_parent text, " +
                        "task_type text, " +
                        "site_name text, " +
                        "lon text, " +
                        "lat text, " +
                        "address text, " +
                        "city text, " +
                        "PRIMARY KEY (task_assignment_id, task_type))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS task_assignment");
        onCreate(db);
    }

    public boolean insertTaskAssignment(String agentName, String agnetId, String taskAssignmentId,
                                        String taskParent, String taskType, String siteName,
                                        String lon, String lat, String address, String city) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("agent_name", agentName);
        contentValues.put("agent_id", agnetId);
        contentValues.put("task_assignment_id", taskAssignmentId);
        contentValues.put("task_parent", taskParent);
        contentValues.put("task_type", taskType);
        contentValues.put("site_name", siteName);
        contentValues.put("lon", lon);
        contentValues.put("lat", lat);
        contentValues.put("address", address);
        contentValues.put("city", city);
        db.insert("task_assignment", null, contentValues);
        return true;
    }

    public ArrayList<HashMap<String, String>> getData(String taskId) {
        ArrayList<HashMap<String, String>> al = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from task_assignment where task_assignment_id = '" + taskId + "'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            HashMap<String, String> hm = new HashMap<>();
            hm.put("agentName", res.getString(res.getColumnIndex("agent_name")));
            hm.put("agentId", res.getString(res.getColumnIndex("agent_id")));
            hm.put("taskAssignmentId", res.getString(res.getColumnIndex("task_assignment_id")));
            hm.put("taskParent", res.getString(res.getColumnIndex("task_parent")));
            hm.put("taskType", res.getString(res.getColumnIndex("task_type")));
            hm.put("siteName", res.getString(res.getColumnIndex("site_name")));
            hm.put("lon", res.getString(res.getColumnIndex("lon")));
            hm.put("lat", res.getString(res.getColumnIndex("lat")));
            hm.put("address", res.getString(res.getColumnIndex("address")));
            hm.put("city", res.getString(res.getColumnIndex("city")));
            al.add(hm);
            res.moveToNext();
        }
        db.close();
        return al;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TASK_ASSIGNMENT_TABLE_NAME);
        return numRows;
    }

    public boolean updateTaskAssignment(String agentName, String agentId, String taskAssignmentId,
                                        String taskParent, String taskType, String siteName, String lon,
                                        String lat, String address, String city) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("agent_name", agentName);
        contentValues.put("agent_id", agentId);
        contentValues.put("task_assignment_id", taskAssignmentId);
        contentValues.put("task_parent", taskParent);
        contentValues.put("task_type", taskType);
        contentValues.put("site_name", siteName);
        contentValues.put("lon", lon);
        contentValues.put("lat", lat);
        contentValues.put("address", address);
        contentValues.put("city", city);
        db.update("task_assignment", contentValues, "task_assignment_id = ? ", new String[]{taskAssignmentId});
        return true;
    }

    public Integer deleteTaskAssignment(Integer taskAssignmentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("task_assignment",
                "task_assignment_id = ? ",
                new String[]{Integer.toString(taskAssignmentId)});
    }

    public ArrayList<String> getAllTaskAssignment() {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from task_assignment", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("agent_name")));
            array_list.add(res.getString(res.getColumnIndex("agent_id")));
            array_list.add(res.getString(res.getColumnIndex("task_assignment_id")));
            array_list.add(res.getString(res.getColumnIndex("task_parent")));
            array_list.add(res.getString(res.getColumnIndex("task_type")));
            array_list.add(res.getString(res.getColumnIndex("site_name")));
            array_list.add(res.getString(res.getColumnIndex("lon")));
            array_list.add(res.getString(res.getColumnIndex("lat")));
            array_list.add(res.getString(res.getColumnIndex("address")));
            array_list.add(res.getString(res.getColumnIndex("city")));
            res.moveToNext();
        }
        return array_list;
    }
}
