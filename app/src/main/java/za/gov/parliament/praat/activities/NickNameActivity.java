package za.gov.parliament.praat.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import za.gov.parliament.praat.R;
import za.gov.parliament.praat.app.ChatApp;
import za.gov.parliament.praat.utils.Constants;

public class NickNameActivity extends AppCompatActivity
{
    private static final String TAG = NickNameActivity.class.getSimpleName();
    private SharedPreferences mSharedPreferences;
    @BindView(R.id.et_Nickname)
    EditText etNickname;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick_name);

        ButterKnife.bind(this);

        mSharedPreferences = ((ChatApp)getApplication()).getSharedPreferences();
    }

    @OnClick(R.id.btn_Join)
    public void onJoin(View view)
    {
        String nickname = etNickname.getText().toString();
        Log.d(TAG, "Join button clicked");
        if(!TextUtils.isEmpty(nickname))
        {
            Intent intent = new Intent(this, ChatActivity.class);

            intent.putExtra(Constants.NICKNAME, nickname);
            startActivity(intent);

            mSharedPreferences.edit().putString(Constants.NICKNAME, nickname).commit();
            //finish();
        }
    }
}
