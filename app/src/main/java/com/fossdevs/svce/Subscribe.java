package com.fossdevs.svce;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class Subscribe extends Activity implements ListUserInterface {
    //    private EventBus bus = EventBus.getDefault();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        ListUsers listUsers=new ListUsers(getApplicationContext());
        listUsers.deligate=this;
        listUsers.execute(GlobalObject.baseURL + "?token=" + GlobalObject.token + "&action=list_users");
    }


    public void finishProgress(Cursor cursor){
        setContentView(R.layout.subscribe_list_user);
        final Cursor c=cursor;
        ListView lv=(ListView) findViewById(R.id.list_user_subscribe);
        String [] from={"name","username"};
        int to[]={R.id.listName,R.id.listUserName};
        c.moveToFirst();
        SimpleCursorAdapter simpleCursorAdapter=new SimpleCursorAdapter(getApplicationContext(),R.layout.list_sub_user,c,from,to,1);
        lv.setAdapter(simpleCursorAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                c.moveToPosition(pos);
                String username=c.getString(c.getColumnIndex("username"));
                String id=c.getString(c.getColumnIndex("userId"));
                //subscribe to username
                MakeRequest mr=new MakeRequest();
                DbHelper dbHelper1=new DbHelper(getApplicationContext());
                SQLiteDatabase sqLiteDatabase=dbHelper1.getReadableDatabase();
                Cursor c=sqLiteDatabase.rawQuery("SELECT * FROM device_id",null);
                c.moveToFirst();
                String dev_id=c.getString(c.getColumnIndex("device_id"));
                String url=GlobalObject.baseURL+"?token="+GlobalObject.token+"&action=subscribe&device_id="+dev_id+"&subscribeTo="+id;
                mr.execute(url);
                Toast.makeText(getApplicationContext(),"Subscribed to "+username,Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_subscribe, menu);
        getActionBar().setIcon(null);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
