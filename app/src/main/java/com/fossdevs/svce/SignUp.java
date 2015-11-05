package com.fossdevs.svce;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import de.greenrobot.event.EventBus;


public class SignUp extends Activity {
    private EventBus bus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus.register(this);
        setContentView(R.layout.activity_sign_up);
        RegisterGcm rg=new RegisterGcm(getApplicationContext());
        rg.execute();
    }


    @Override
    protected void onDestroy() {
        bus.unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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

    public void onEvent(RegisterEvent event){
        String msg=event.getData();
//        Log.d("eventcame",msg);
        if(msg.equals("SUCCESS")){
            Toast.makeText(getApplicationContext(),"Initialization Done!",Toast.LENGTH_LONG).show();
            finish();
        }
        if(msg.equals("FAIL")){
            failInitializing();
        }
//        Log.d("tagd",msg);
    }

    private void failInitializing() {
        TextView tv=(TextView) findViewById(R.id.initializingMessage);
        tv.setText(R.string.registerFail);
    }
}
