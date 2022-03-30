package com.example.maliciousapp9;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public static final java.lang.String TAG = "MOBIOTSEC";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.INTERNET};
        if (!hasPermissions(this, PERMISSIONS)) {   // check if there exist the INTERNET permission
            Log.i(TAG, "there is no permission");
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        String url = "http://10.0.2.2:8085/check_math.php";
        JSONObject postData = new JSONObject();

        try {
            postData.put("answer","4");
       //     postData.put("val1","11");
          //  postData.put("oper","-");
           // postData.put("val2","7");
            Log.i(TAG, "onCreate() 1");

            new DownloadFilesTask().execute(url, postData.toString());
            Log.i(TAG, "onCreate() 2");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "error in onCreate():" + e.toString());
        }


    }

    //The three types used by an asynchronous task are the following:
    //
    //    Params, the type of the parameters sent to the task upon execution.
    //    Progress, the type of the progress units published during the background computation.
    //    Result, the type of the result of the background computation.
    //
    //Not all types are always used by an asynchronous task. To mark a type as unused, simply use the type Void
                                                     // params progress result
    private class DownloadFilesTask extends AsyncTask<String, String, Void> {

        protected Void doInBackground(String... params) {
            if(params.length == 2){
                Log.i(TAG, "doInBackground: params.length == " + params.length);
                String url = params[0];
                RequestHandler mRequestHandler = new RequestHandler();
                JSONObject mJSON = null;
                try {
                    mJSON = new JSONObject(params[1]);
                } catch (JSONException e) {
                    Log.i(TAG, "Error: ~ exception in JSONObject / doInBackground");
                    e.printStackTrace();
                    Log.i(TAG, "try-catch 1");
                }
                try {
                    mRequestHandler.sendPost(url, mJSON);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "try-catch 2" + e.toString());
                }
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

    public static boolean hasPermissions(Context context, String... permissions)
    {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null)
        {
            for (String permission : permissions)
            {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                {
                    return false;
                }
            }
        }
        return true;
    }

}