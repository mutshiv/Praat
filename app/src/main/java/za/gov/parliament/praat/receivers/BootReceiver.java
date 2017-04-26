package za.gov.parliament.praat.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import za.gov.parliament.praat.services.ChatService;

public class BootReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = BootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "onReceive");
        context.startService(new Intent(context, ChatService.class));
    }
}
