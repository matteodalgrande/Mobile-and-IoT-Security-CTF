package com.example.maliciousapp7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    String TAG = "MOBIOTSEC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Note: If your app targets API level 26 or higher, you cannot use the manifest to declare
        // a receiver for implicit broadcasts (broadcasts that do not target your app specifically),
        // except for a few implicit broadcasts that are exempted from that restriction. In most
        // cases, you can use scheduled jobs instead.[in code]

        BroadcastReceiver mbr = new MyBroadcastReceiver();
        this.registerReceiver(mbr, new IntentFilter("victim.app.FLAG_ANNOUNCEMENT"));
        Log.i(TAG, "onCreate");
    }



}