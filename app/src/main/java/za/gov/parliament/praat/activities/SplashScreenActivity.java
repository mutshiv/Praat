package za.gov.parliament.praat.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import za.gov.parliament.praat.R;
import za.gov.parliament.praat.app.ChatApp;
import za.gov.parliament.praat.utils.Constants;

public class SplashScreenActivity extends AppCompatActivity {

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mSharedPreferences = ((ChatApp)getApplication()).getSharedPreferences();
        final String nickname = mSharedPreferences.getString(Constants.NICKNAME, "");

        //run();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                Intent intent = null;


                if(TextUtils.isEmpty(nickname))
                {
                    intent = new Intent(SplashScreenActivity.this, NickNameActivity.class);
                }
                else
                {
                    intent = new Intent(SplashScreenActivity.this, ChatActivity.class);
                    intent.putExtra(Constants.NICKNAME, nickname);
                }
                startActivity(intent);
                finish();
            }
        }).start();
    }


  /*  @Override
    public void run()
    {
        try
        {
            Thread.sleep(2000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, NickNameActivity.class);
        startActivity(intent);
    }*/
}
