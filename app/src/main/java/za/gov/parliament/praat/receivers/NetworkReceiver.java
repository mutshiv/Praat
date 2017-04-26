package za.gov.parliament.praat.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import za.gov.parliament.praat.services.ChatService;

public class NetworkReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = NetworkReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "checking network connection");

        boolean isConnected = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

        Log.d(LOG_TAG, isConnected + " *************");
        Intent intentChatService = new Intent(context, ChatService.class);

        if(!isConnected)
        {
            context.startService(intentChatService);
            Log.d(LOG_TAG, " Starting the service in the network receiver*************");
        }
        else
        {
            context.stopService(intentChatService);
            Log.d(LOG_TAG, " Stopping the service in the network receiver*************");
        }
    }
}
