package com.fossdevs.svce;

import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.liuguangqiang.progressbar.CircleProgressBar;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class DownloadFile extends AsyncTask<String, Integer, String> {
    //public DownloadInterface deligate;
    private Context context;
    private PowerManager.WakeLock mWakeLock;
    private int rotation;
    private int position;
    Timer timer=new Timer();
    public CircleProgressBar progressBar;
    public DownloadFile(Context context,CircleProgressBar circleProgressBar,int cposition) {
        this.context = context;
        this.position=cposition;
        this.progressBar=circleProgressBar;
        this.rotation=0;
    }

    @Override
    protected String doInBackground(String... sUrl) {
        InputStream input = null;

        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                rotation=rotation+3%360;
                try{
                    progressBar.setRotation(rotation);
                }catch (Exception e){
                    //do nothing
                }
            }
        };
        timer.schedule(task,0,20);


        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(sUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();
            String filename=sUrl[0];
            String[] bits = filename.split("/");
            filename=bits[bits.length-1];
            output = new FileOutputStream("/sdcard/"+filename);
            String result="/sdcard/"+filename;

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }
                    } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return null;

    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        //deligate.onProg(progress[0]);
        try{
            progressBar.setProgress(progress[0]);

        }catch(Exception e){
            //do nothing
        }
        super.onProgressUpdate(progress);
        // if we get here, length is known, now set indeterminate to false


    }

    @Override
    protected void onPostExecute(String result) {
        mWakeLock.release();
        if (result != null){
            Log.d("background", result);
            Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();

        }
        //deligate.onFinish();
        timer.cancel();
        try{
            progressBar.setProgress(100);
        }catch(Exception e){
            //do nothing.
            //might be minimized.
        }
    }
}