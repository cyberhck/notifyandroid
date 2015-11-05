package com.fossdevs.svce;




import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import de.greenrobot.event.EventBus;


public class RegisterGcm extends AsyncTask <String, Integer, String>{
    Context m_context;

    public RegisterGcm(Context context){
        m_context = context;
    }
    @Override
    protected String doInBackground(String... path) {
        Looper.prepare();
        String regid = null;
        try{
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(m_context);
            regid = gcm.register(GlobalObject.senderID);
            //Log.d("background",regid);
            String urli= GlobalObject.baseURL+"?token="+GlobalObject.token+"&action=signup&device_id="+regid;
            String response = null;
            try {

                urli=urli.replace(" ","+");
                URL url = new URL(urli);
                URLConnection connection=url.openConnection();
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                int responseCode=httpConnection.getResponseCode();
                if(responseCode==HttpURLConnection.HTTP_OK){
                    InputStream in;
                    in=httpConnection.getInputStream();
                    BufferedReader r = new BufferedReader(new InputStreamReader(in));
                    response = r.readLine();
                    EventBus eb=EventBus.getDefault();
                    String message;
                    if(response.equals("OK")){
                        message="SUCCESS";
                        DbHelper dbHelper=new DbHelper(m_context);
                        SQLiteDatabase db=dbHelper.getWritableDatabase();
                        SQLiteStatement stmt = db.compileStatement("INSERT INTO device_id(device_id) VALUES(?);");
                        stmt.bindString(1,regid);
                        stmt.execute();
                    }else{
                        message="FAIL";
                    }
                    RegisterEvent e=new RegisterEvent(message);
                    eb.post(e);

                }else{
                    response=null;
                }

            }catch(Exception e){
                //Log.d("background",e.toString());
            }
        }catch(Exception e){
            //Log.d("background",e.toString());
            Toast.makeText(m_context,"This device is not supported",Toast.LENGTH_LONG).show();
        }
        return regid;
    }
}
