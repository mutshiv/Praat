package za.gov.parliament.praat.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import za.gov.parliament.praat.app.ChatApp;
import za.gov.parliament.praat.utils.Constants;


public class ChatService extends Service {

    private static final String TAG = ChatService.class.getSimpleName();
    private String nickname;
    private Socket socket;

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

        nickname = ((ChatApp) getApplication()).getSharedPreferences().getString(Constants.NICKNAME, "Guest");

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
                        String message = jsonObject.getString("message");

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
}
