package com.example.mobilesec_frontdoor;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MyRequest extends Activity {
    String TAG = "MOBIOTSEC";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new WebService().execute();
    }


    public class WebService extends AsyncTask<String, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params) {
            try {

                URL mUrl = new URL("http://10.0.2.2:8085");
                String urlParameters = "username=testuser&password=passtestuser123";
                int postDataLength = urlParameters.getBytes(StandardCharsets.UTF_8).length;
                HttpURLConnection conn = (HttpURLConnection) new URL(mUrl + "?" + urlParameters).openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("charset", "utf-8");
                conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
                conn.setUseCaches(false);
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String content = "";
                while (true) {
                    String readLine = rd.readLine();
                    String line = readLine;
                    if (readLine == null) {
                        return content;
                    }
                    content = content + line + "\n";
                }
            } catch (Exception e) {
                Log.i(TAG, Log.getStackTraceString(e));
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i(TAG, s);
        }
}
}