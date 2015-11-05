package com.fossdevs.svce;



import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.app.TaskStackBuilder;

import java.util.Calendar;


public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            String action=intent.getAction();
            Log.d("tagd","BROADCAST");
            if(action.equals("com.google.android.c2dm.intent.REGISTRATION")){
                String registrationID=intent.getStringExtra("registration_id");
                String error=intent.getStringExtra("error");
                String unregistered=intent.getStringExtra("unregistered");
            }else {
                if (action.equals("com.google.android.c2dm.intent.RECEIVE")) {
                    String message = intent.getStringExtra("message");
                    String name=intent.getStringExtra("name");
                    String sender = intent.getStringExtra("sender");
                    String type=intent.getStringExtra("type");
                    if(type==null){
                        type="text";
                    }else{
                        type="file";
                    }
                    Calendar calendar=Calendar.getInstance();
                    String Date=calendar.get(Calendar.DATE)+":"+calendar.get(Calendar.MONTH)+":"+calendar.get(Calendar.YEAR);
                    String Time=calendar.get(Calendar.HOUR)+":"+calendar.get(Calendar.MINUTE);
                    String DateTime=Date+" "+Time;

                    DbHelper dbHelper=new DbHelper(context);
                    SQLiteDatabase db=dbHelper.getWritableDatabase();
                    SQLiteStatement stmt = db.compileStatement("INSERT INTO notices(senderName,sentBy,message,sentAt,type) VALUES(?,?,?,?,?);");

                    stmt.bindString(1,name);
                    stmt.bindString(2,sender);
                    stmt.bindString(3,message);
                    stmt.bindString(4,DateTime);
                    stmt.bindString(5,type);
                    stmt.execute();

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(context)
                                    .setSmallIcon(R.drawable.ic_launcher)
                                    .setContentTitle(name)
                                    .setAutoCancel(true)
                                    .setDefaults(Notification.DEFAULT_ALL)
                                    .setContentText(message);
                    Intent resultIntent = new Intent(context, MainActivity.class);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                    stackBuilder.addNextIntent(resultIntent);

                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(
                                    0,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                    mBuilder.setContentIntent(resultPendingIntent);
                    NotificationManager mNotificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(11211, mBuilder.build());

                }
            }
        }catch (Exception e){
            Log.d("background",e.toString());
        }
    }
}
