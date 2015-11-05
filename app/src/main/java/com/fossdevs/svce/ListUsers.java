package com.fossdevs.svce;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
//import android.util.Log;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;



public class ListUsers extends AsyncTask<String, Integer, Cursor> {
    Context m_context;
    public ListUserInterface deligate;
    Cursor c;
    public ListUsers(Context context){
        m_context = context;
    }
    @Override
    protected Cursor doInBackground(String... path) {

        String response = null;
        try {
            path[0] = path[0].replace(" ", "+");
            URL url = new URL(path[0]);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in;
                in = httpConnection.getInputStream();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                response = r.readLine();
            } else {
                response = null;
            }
        } catch (Exception e) {
//            Log.d("tagd", "exception", e);
        }
        DbHelper dbHelper1=new DbHelper(m_context);
        SQLiteDatabase db1=dbHelper1.getWritableDatabase();
        db1.execSQL("DELETE FROM listViewUser;");
        try{
            JSONArray jsonArray=new JSONArray(response);
            for(int i=0;i<jsonArray.length();i++){
                SQLiteStatement stmt = db1.compileStatement("INSERT INTO listViewUser (userId, name, username) VALUES(?,?,?);");
                JSONObject jo=jsonArray.getJSONObject(i);
                String userId=jo.get("id").toString();
                String name=jo.get("name").toString();
                String username=jo.get("username").toString();
                stmt.bindString(1,userId);
                stmt.bindString(2,name);
                stmt.bindString(3,username);
                stmt.execute();
            }
            c=db1.rawQuery("SELECT rowid as _id,userId,name,username FROM listViewUser",null);
        }catch(Exception e){
//            Log.d("JSON exception",e.toString());
        }
        return c;
    }

    @Override
    protected void onPostExecute(Cursor s) {
        deligate.finishProgress(c);
        super.onPostExecute(c);
    }
}