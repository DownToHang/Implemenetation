package io.evolution.downtohang;

import android.provider.BaseColumns;

/**
 * Created by michael on 4/9/2016.
 */
public class LocalDB {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.

    /* Inner class that defines the table contents */
    public static abstract class dbEntry implements BaseColumns {

        //id UUD username status hangoutStatus latitude longitude
        public static final String TABLE_NAME = "userEntry";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_UUD = "UUD";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_HANGOUT_STATUS = "hangout_status";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + dbEntry.TABLE_NAME + " (" +
                    dbEntry._ID + " INTEGER PRIMARY KEY," +
                    dbEntry.COLUMN_ID + TEXT_TYPE + COMMA_SEP +
                    dbEntry.COLUMN_UUD + TEXT_TYPE + COMMA_SEP +
                    dbEntry.COLUMN_USERNAME + TEXT_TYPE + COMMA_SEP +
                    dbEntry.COLUMN_STATUS + TEXT_TYPE + COMMA_SEP +
                    dbEntry.COLUMN_HANGOUT_STATUS + TEXT_TYPE + COMMA_SEP +
                    dbEntry.COLUMN_LATITUDE + TEXT_TYPE + COMMA_SEP +
                    dbEntry.COLUMN_LONGITUDE + TEXT_TYPE + COMMA_SEP +
                    " )";



    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + dbEntry.TABLE_NAME;


}
