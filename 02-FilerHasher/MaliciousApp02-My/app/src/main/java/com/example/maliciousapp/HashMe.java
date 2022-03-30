package com.example.maliciousapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.bouncycastle.util.encoders.Hex;

public class HashMe extends Activity {

    private final static String TAG = "MOBIOTSEC";
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart(); //costruttore del padre
        Intent intent = getIntent();
        String filePath = intent.getDataString();

        Log.i(TAG, "Dat: " + filePath);// mi prendo i dati e li stampo nei log
        String hash = "";

        try {
            hash = hexHashTransform(filePath);
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e);
        }

        //I have two activities: main activity and child activity.
        // When I press a button in the main activity, the child activity is launched.
        // Now I want to send some data back to the main screen.
        // https://stackoverflow.com/questions/920306/sending-data-back-to-the-main-activity-in-android
        // There are a couple of ways to achieve what you want, depending on the circumstances.
        // The most common scenario (which is what yours sounds like) is when a child Activity is used to get user input - such as choosing a contact from a list or entering data in a dialog box. In this case you should use startActivityForResult to launch your child Activity.
        // This provides a pipeline for sending data back to the main Activity using setResult. The setResult method takes an int result value and an Intent that is passed back to the calling Activity.
        // Intent resultIntent = new Intent();
        // TO DO Add extras or a data URI to this intent as appropriate.
        // resultIntent.putExtra("some_key", "String data");
        // setResult(Activity.RESULT_OK, resultIntent);
        // finish();
        // To access the returned data in the calling Activity override onActivityResult. The requestCode corresponds to the integer passed in in the startActivityForResult call, while the resultCode and data Intent are returned from the child Activity.
        //@Override
        //public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //  super.onActivityResult(requestCode, resultCode, data);
        //  switch(requestCode) {
        //    case (MY_CHILD_ACTIVITY) : {
        //      if (resultCode == Activity.RESULT_OK) {
        //        // TO DO Extract the data returned from the child Activity.
        //        String returnValue = data.getStringExtra("some_key");
        //      }
        //      break;
        //    }
        //  }
        //}

        //return hexHash in a "result" intent
        Intent resultIntent = new Intent();
        resultIntent.putExtra("hash", hash); //You can add extra data with various putExtra() methods, each accepting two parameters: the key name and the value. You can also create a Bundle object with all the extra data, then insert the Bundle in the Intent with putExtras().
        setResult(Activity.RESULT_OK, resultIntent); //The setResult method takes an int result value and an Intent that is passed back to the calling Activity.
        finish();
    }

    protected String hexHashTransform(String fileUrl) throws NoSuchAlgorithmException {
        String hash = "";
        Uri uri = Uri.parse(fileUrl); /// The fileUrl is a string URL, such as "file:///root/file/boh/file.txt"
        Log.i(TAG, "Uri of file: " + uri.toString());

        try {

            //How can I read a text file from the SD card in Android?
            //You should have READ_EXTERNAL_STORAGE permission for reading sdcard. Add permission in manifest.xml
            //<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />

            //Find the directory for the SD Card
            // File sdcard = Environment.getExternalStorageDirectory();

            //Get the text file
            File file = new File(uri.getPath()); // use new File(uri.getPath()); and not new File(uri.toString()); // uri.toString() returns a String in the format: "file:///mnt/sdcard/myPicture.jpg", whereas uri.getPath() returns a String in the format: "/mnt/sdcard/myPicture.jpg".

            StringBuilder text = new StringBuilder();// StringBuilder objects are like String objects, except that they can be modified. Internally, these objects are treated like variable-length arrays that contain a sequence of characters. At any point, the length and content of the sequence can be changed through method invocations.
            // Strings should always be used unless string builders offer an advantage in terms of simpler code (see the sample program at the end of this section) or better performance. For example, if you need to concatenate a large number of strings, appending to a StringBuilder object is more efficient.

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                    //text.append('\n');
                }
                br.close();
            }
            catch (IOException e) {
                Log.e(TAG, "Error: during Reading a File " + e);
            }
            //Set the text
            hash = text.toString();
            Log.i(TAG, "Hash String: " + hash);
        } catch (Exception e) {
            Log.i(TAG, "Error: during open the SDcard " + e);
        }

        //calculate hash
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] sha256 = md.digest(hash.getBytes());// getBytes() method encodes a given String into a sequence of bytes and returns an array of bytes.
        //  digest(byte[] input) Performs a final update on the digest using the specified array of bytes, then completes the digest computation.

        hash = Hex.toHexString(sha256); // String toHexString(bytes[])
        Log.i(TAG, "Hash SHA256: " + hash);
        return hash;
    }
}