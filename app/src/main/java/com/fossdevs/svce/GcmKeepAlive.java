package com.fossdevs.svce;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;

public class GcmKeepAlive  {

    protected CountDownTimer timer;
    protected Context mContext;
    protected Intent gTalkHeartBeatIntent;
    protected Intent mcsHeartBeatIntent;

    public GcmKeepAlive(Context context) {
        mContext = context;
        gTalkHeartBeatIntent = new Intent(
                "com.google.android.intent.action.GTALK_HEARTBEAT");
        mcsHeartBeatIntent = new Intent(
                "com.google.android.intent.action.MCS_HEARTBEAT");
    }

    public void broadcastIntents() {
        mContext.sendBroadcast(gTalkHeartBeatIntent);
        mContext.sendBroadcast(mcsHeartBeatIntent);
    }

}