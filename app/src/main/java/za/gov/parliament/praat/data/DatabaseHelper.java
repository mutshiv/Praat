package za.gov.parliament.praat.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import za.gov.parliament.praat.utils.Constants;

/**
 * Created by vmutshinya on 4/26/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper
{

    private static final String LOG_TAG = DatabaseHelper.class.getCanonicalName();

    public DatabaseHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "onCreate");

        String sql = String.format("CREATE TABLE %s(%s integer primary key autoincrement, %s text, %s text, %s integer)",
                                    Constants.TABLE_NAME,
                                    Constants.Columns.ID,
                                    Constants.Columns.NICKNAME,
                                    Constants.Columns.MESSAGE,
                                    Constants.Columns.CREATED_AT);
        Log.d(LOG_TAG, sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOG_TAG, "upgrade");
        db.execSQL("DROP TABLE IF EXISTS chats");
        onCreate(db);
    }
}
