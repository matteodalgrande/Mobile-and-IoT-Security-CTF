package com.example.maliciousapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MOBIOTSEC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new RequestAsync().execute();
    }
    public class RequestAsync extends AsyncTask<String, String, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            try {
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("answer", "4");
                RequestHandler.sendPost("http://10.0.2.2:8085/check_math.php", postDataParams);
            }
            catch(Exception e){
                Log.e(TAG, "Exception: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            if(s!=null){
                Log.i(TAG, "s: " + s);
            }
        }
    }
}