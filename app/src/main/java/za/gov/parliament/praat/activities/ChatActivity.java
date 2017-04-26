package za.gov.parliament.praat.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import za.gov.parliament.praat.R;
import za.gov.parliament.praat.app.ChatApp;
import za.gov.parliament.praat.data.DatabaseHelper;
import za.gov.parliament.praat.services.ChatService;
import za.gov.parliament.praat.utils.Constants;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = ChatActivity.class.getSimpleName();
    private SharedPreferences mSharedPreferences;
    private SimpleCursorAdapter mAdapter;
    private IntentFilter mIntentFilter = new IntentFilter(Constants.ACTION_BROADCAST_INCOMING_MESSAGE);
    private NewMessageReceiver mNewMessageReceiver;
    private SQLiteDatabase mDatabase;
    private Cursor mCursor;
    private String[] from = {Constants.Columns.NICKNAME, Constants.Columns.MESSAGE, Constants.Columns.ID};
    private int[] to = {android.R.id.text1, android.R.id.text2};

    @BindView(R.id.et_message)
    EditText etMessage;
    @BindView(R.id.lv_msg)
    ListView lvChatMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        Log.d(TAG, "Chat activity onCreate");

        mDatabase = new DatabaseHelper(this).getWritableDatabase();
        mCursor = mDatabase.query(Constants.TABLE_NAME, from, null, null, null, null, null);
        mAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_expandable_list_item_2, mCursor, from, to, 0);
        lvChatMessage.setAdapter(mAdapter);

        Intent intent = getIntent();

        if (intent != null) {
            mSharedPreferences = ((ChatApp) getApplication()).getSharedPreferences();
        }

        mNewMessageReceiver = new NewMessageReceiver();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mNewMessageReceiver,mIntentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mNewMessageReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intentService = new Intent(this, ChatService.class);

        switch (id)
        {
            case R.id.action_settings:
                Intent intent = new Intent(this, PrefsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_start_service:
                startService(intentService);
                return true;
            case R.id.action_stop_service:
                stopService(intentService);
                return  true;
        }
        return super.onOptionsItemSelected(item);

    }

    @OnClick(R.id.btn_send)
    public void sendMsg(View view)
    {
        Intent outMsgIntent = new Intent(Constants.ACTION_BROADCAST_OUTGOING_MESSAGE);
        String msg = etMessage.getText().toString();

        outMsgIntent.putExtra(Constants.MESSAGE, msg);

        LocalBroadcastManager.getInstance(this).sendBroadcast(outMsgIntent);
        etMessage.setText("");
        Log.d(TAG, msg);

        mAdapter.notifyDataSetChanged();
    }

    public class NewMessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Uri notification = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION);

            if(intent != null)
            {
                if(intent.hasExtra(Constants.ACTION_MESSAGE_INSERT)) {
                    mCursor.requery();
                    mAdapter.notifyDataSetChanged();
                }
                else if(intent.hasExtra(Constants.NICKNAME) && intent.hasExtra(Constants.MESSAGE)){
                    String nickname = intent.getStringExtra(Constants.NICKNAME);
                    String message = intent.getStringExtra(Constants.MESSAGE);

                    Log.d(TAG, String.format("%s %s", nickname, message));

                    mCursor.requery();
                    mAdapter.notifyDataSetChanged();

                    Ringtone ringtone = RingtoneManager.getRingtone(context, notification);
                    ringtone.play();

                   // MediaPlayer mediaPlayer = MediaPlayer.create(context, notification);
                    //mediaPlayer.start();

                  //  Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                  //  vibrator.vibrate(3000);
                }
            }
        }
    }
}
