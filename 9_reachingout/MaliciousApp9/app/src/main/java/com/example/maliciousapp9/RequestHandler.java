package com.example.maliciousapp9;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;

import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

public class RequestHandler {

    private final static String TAG = "MOBIOTSEC";

    public Void sendPost(String pUrl, JSONObject jsonObject) throws IOException, JSONException {
        Log.i(TAG, "sendPost()1");

        String data = "";
        URL url = new URL(pUrl);

        byte[] bytesJsonObject = getBytes(jsonObject);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        // HttpURLConnection uses the GET method by default. It will use POST if setDoOutput(true) has been called.
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod( "POST" );
        urlConnection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
        urlConnection.setRequestProperty( "charset", "UTF-8");
        urlConnection.setRequestProperty( "Content-Length", Integer.toString( bytesJsonObject.toString().length() -2 )); //decrese of two caracters due to is a string and we have to delete the " "
        urlConnection.setRequestProperty("Referer", "http://localhost:8085/flag.html");
        Log.i(TAG, jsonObject.toString());
        urlConnection.setUseCaches(false);
        urlConnection.getOutputStream().write(bytesJsonObject);


        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            int inputStreamData = in.read();
            Log.i(TAG, "sendPost()2");
            while (inputStreamData != -1){
                char current = (char) inputStreamData;
                data += current;
                // update the variable
                inputStreamData = in.read();
            }
            Log.i(TAG, "response: " + data);
            return null;

        } finally {
            urlConnection.disconnect();
        }

    }

    byte[] getBytes (JSONObject mJsonObject) throws JSONException {
        Iterator<String> keys = mJsonObject.keys();
        String result = "";
        while(keys.hasNext()) {
            String key = keys.next();
            result += key + "=" + mJsonObject.get(key).toString();
        }
        return result.getBytes();
    }
}