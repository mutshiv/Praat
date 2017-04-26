package za.gov.parliament.praat.utils;

import android.provider.BaseColumns;

/**
 * Created by vmutshinya on 4/24/2017.
 */

public class Constants
{
    public static final String NICKNAME = "nickname";
    public static final String ACTION_ADD_USER = "add user";
    public static final String CHAT_SERVER_URL = "http://chat.peruzal.com";
    public static final String NEW_MESSAGE = "new message";
    public static final String ACTION_BROADCAST_INCOMING_MESSAGE = "za.gov.parliament.praat.ACTION_BROADCAST_INCOMING_MESSAGE";
    public static final String MESSAGE = "message";
    public static final String ACTION_BROADCAST_OUTGOING_MESSAGE = "za.gov.parliament.praat.ACTION_BROADCAST_OUTGOING_MESSAGE";
    public static final String DATABASE_NAME = "chat.db";
    public static final String TABLE_NAME = "chats";
    public static final String ACTION_BROADCAST_OUTGOING_MESSAGE_SELF = "za.gov.parliament.praat.ACTION_BROADCAST_OUTGOING_MESSAGE_SELF";
    public static final String ACTION_MESSAGE_INSERT = "message inserted";
    public static int DATABASE_VERSION = 3;

    public static class Columns {

        public static final String ID = BaseColumns._ID;
        public static final String NICKNAME = "nickname";
        public static final String MESSAGE = "message";
        public static final String CREATED_AT = "createdAt";
    }
}
