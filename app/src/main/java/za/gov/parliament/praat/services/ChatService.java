package za.gov.parliament.praat.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Date;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import za.gov.parliament.praat.app.ChatApp;
import za.gov.parliament.praat.data.DatabaseHelper;
import za.gov.parliament.praat.utils.Constants;


public class ChatService extends Service {

    private static final String TAG = ChatService.class.getSimpleName();
    private String nickname;
    private Socket socket;
    SQLiteDatabase mDatabase;
    DatabaseHelper mDatabaaseHelper;
    String message;
    private IntentFilter mIntentFilter = new IntentFilter(Constants.ACTION_BROADCAST_OUTGOING_MESSAGE);
    private OutgoingMessageReceiver mOutgoingMessageReceiver;

    public ChatService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate ChatService");

        mDatabaaseHelper = new DatabaseHelper(this);
        mDatabase = mDatabaaseHelper.getWritableDatabase();

        nickname = ((ChatApp) getApplication()).getSharedPreferences().getString(Constants.NICKNAME, "Guest");
        mOutgoingMessageReceiver = new OutgoingMessageReceiver();

        try {
            socket = IO.socket(Constants.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socket.emit(Constants.ACTION_ADD_USER, nickname);
                Log.d(TAG, "connected");
                LocalBroadcastManager.getInstance(ChatService.this).registerReceiver(mOutgoingMessageReceiver, mIntentFilter);
            }
        });

        socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener(){

            @Override
            public void call(Object... args) {
                LocalBroadcastManager.getInstance(ChatService.this).unregisterReceiver(mOutgoingMessageReceiver);
            }
        });

        socket.on(Socket.EVENT_CONNECTING, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "connecting");
            }
        });

        socket.on(Constants.NEW_MESSAGE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if(args != null && args.length > 0)
                {
                    Log.d(TAG, "New message " + args[0]);
                    try
                    {
                        JSONObject jsonObject = new JSONObject(args[0].toString());
                        String username = jsonObject.getString("username");
                        message = jsonObject.getString("message");

                        Intent intent = new Intent(Constants.ACTION_BROADCAST_INCOMING_MESSAGE);
                        intent.putExtra(Constants.NICKNAME, username);
                        intent.putExtra(Constants.MESSAGE, message);
                        LocalBroadcastManager.getInstance(ChatService.this).sendBroadcast(intent);

                        ContentValues values = new ContentValues();
                        values.put(Constants.Columns.NICKNAME, username);
                        values.put(Constants.Columns.MESSAGE, message);
                        values.put(Constants.Columns.CREATED_AT, new Date().getTime());

                        long id = mDatabase.insert(Constants.TABLE_NAME, null, values);

                        if(id > 0)
                        {
                            Log.d(TAG, id + " ");
                            intent.putExtra(Constants.ACTION_MESSAGE_INSERT, true);
                            LocalBroadcastManager.getInstance(ChatService.this).sendBroadcast(new Intent(Constants.ACTION_BROADCAST_OUTGOING_MESSAGE_SELF));
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });

        socket.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class OutgoingMessageReceiver extends BroadcastReceiver
    {
        private final String TAG = OutgoingMessageReceiver.class.getCanonicalName();
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive ");
            socket.emit(Constants.NEW_MESSAGE, intent.getStringExtra(Constants.MESSAGE));


            ContentValues values = new ContentValues();
            values.put(Constants.Columns.NICKNAME, nickname);
            values.put(Constants.Columns.MESSAGE, intent.getStringExtra(Constants.MESSAGE));
            values.put(Constants.Columns.CREATED_AT, new Date().getTime());

            long id = mDatabase.insert(Constants.TABLE_NAME, null, values);
            if(id > 0)
            {
                Intent intentMessageInsert = new Intent(Constants.ACTION_BROADCAST_INCOMING_MESSAGE);
                intentMessageInsert.putExtra(Constants.ACTION_MESSAGE_INSERT, true);
                LocalBroadcastManager.getInstance(ChatService.this).sendBroadcast(intentMessageInsert);
            }
        }
    }
}
