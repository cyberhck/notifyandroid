package com.fossdevs.svce;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MakeRequest extends AsyncTask <String, Integer, String>{


    @Override
    protected String doInBackground(String... path) {
        String response = null;
        try {
            path[0]=path[0].replace(" ","+");
            URL url = new URL(path[0]);
            URLConnection connection=url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode=httpConnection.getResponseCode();
            if(responseCode==HttpURLConnection.HTTP_OK){
                InputStream in;
                in=httpConnection.getInputStream();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                response = r.readLine();
            }else{
                response=null;
            }
        }catch(Exception e){
            //Log.d("tagd","exception", e);
        }
        return response;
    }
}