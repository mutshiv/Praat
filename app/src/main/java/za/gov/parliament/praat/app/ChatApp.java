package za.gov.parliament.praat.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by vmutshinya on 4/24/2017.
 */

public class ChatApp extends Application
{
    private static final String TAG = ChatApp.class.getSimpleName();
    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Chat App started");
    }

    public SharedPreferences getSharedPreferences()
    {
        if(mSharedPreferences != null)
        {
            return mSharedPreferences;
        }
        else
        {
            return mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        }
    }
}
