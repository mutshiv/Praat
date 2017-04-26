package za.gov.parliament.praat.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import za.gov.parliament.praat.R;
import za.gov.parliament.praat.app.ChatApp;
import za.gov.parliament.praat.services.ChartService;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = ChatActivity.class.getSimpleName();

    private SharedPreferences mSharedPreferences;
    private List<String> mChatMessages;
    private ArrayAdapter<String> mAdapter;

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

        mChatMessages = new ArrayList<>();
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, mChatMessages);
        lvChatMessage.setAdapter(mAdapter);


        Intent intent = getIntent();

        if (intent != null) {
            mSharedPreferences = ((ChatApp) getApplication()).getSharedPreferences();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intentService = new Intent(this, ChartService.class);

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
        String msg = etMessage.getText().toString();
    }
}
