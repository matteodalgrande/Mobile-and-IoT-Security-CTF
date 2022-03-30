package com.example.maliciousapp7;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Set;

public class MyBroadcastReceiver extends BroadcastReceiver {
    String TAG = "MOBIOTSEC";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG,intent.getExtras().toString());
        Bundle b = (Bundle) intent.getExtras();

        //to know the name of the keys
        Set<String> keys = b.keySet();
        for (String k:keys
             ) {
            Log.i(TAG,k);
        }

        Log.i(TAG, b.getString("flag"));
    }

}