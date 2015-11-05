package com.fossdevs.svce;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.liuguangqiang.progressbar.CircleProgressBar;

public class DisplayNotices extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_notices);
        Intent intent=getIntent();
        final String user=intent.getStringExtra("username");
        DbHelper dbHelper=new DbHelper(getApplicationContext());
        final SQLiteDatabase db=dbHelper.getWritableDatabase();
        final Cursor c=db.rawQuery("SELECT rowid as _id,senderName,message,sentAt,type FROM notices WHERE sentBy='" + user + "';", null);
        c.moveToFirst();
        String fullName=c.getString(c.getColumnIndex("senderName"));
        ActionBar ab=getActionBar();
        try{
            ab.setTitle(fullName);
            ab.setSubtitle(user);
            ab.setIcon(null);
        }catch(Exception e){
            Log.d("actionbar exception",e.toString());
        }
        c.moveToFirst();
        final ListView listView=(ListView) findViewById(R.id.notices_display_list);
        //int to[]={R.id.note_bubble};
        //String [] from={"message"};
        final CustomCursorAdapter customCursorAdapter=new CustomCursorAdapter(getApplicationContext(),c,1);
        //final SimpleCursorAdapter simpleCursorAdapter=new SimpleCursorAdapter(getApplicationContext(),R.layout.list_display_notices,c,from,to,1);
        listView.setDivider(null);
        listView.setDividerHeight(10);
        listView.setClickable(false);
        listView.setAdapter(customCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                c.moveToPosition(pos);
                final int position=pos;
                String data=c.getString(c.getColumnIndex("message"));
                if(c.getString(c.getColumnIndex("type")).equals("file")){
//                    Intent intent=new Intent(getApplicationContext(),Download.class);
//                    intent.putExtra("file",data);
//                    startActivity(intent);
                    //download stuff.
                    CircleProgressBar progressBar=(CircleProgressBar)view.findViewById(R.id.download_progress_bar);
                    progressBar.setVisibility(View.VISIBLE);
                    int cPosition=c.getPosition();
                    DownloadFile df=new DownloadFile(getApplicationContext(),progressBar,cPosition);
                    df.execute(GlobalObject.url+data);



                }else{
                    //do nothing
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long l) {
                final int position = pos;
                AlertDialog.Builder builder = new AlertDialog.Builder(DisplayNotices.this)
                        .setPositiveButton(R.string.confirmOkDeleteButton, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                c.moveToPosition(position);
                                String id = c.getString(c.getColumnIndex("_id"));
                                String sql = "DELETE FROM notices WHERE rowid=" + id;
                                db.execSQL(sql);
                                Toast.makeText(getApplicationContext(), "DELETED", Toast.LENGTH_LONG).show();
                                Cursor cur = db.rawQuery("SELECT rowid as _id,senderName,message,sentAt,type FROM notices WHERE sentBy='" + user + "';", null);
                                customCursorAdapter.swapCursor(cur);
                            }
                        })
                        .setTitle("Delete this notice?")
                        .setMessage(R.string.deleteNoticeMessage)
                        .setNegativeButton(R.string.confirmCancelDeleteButton, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

                return true;
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_notices, menu);
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
