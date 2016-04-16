package io.evolution.downtohang;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Bill Ezekiel on 3/1/2016.
 */
public class LocalDB {
    // database constants
    public static final String DB_NAME = "friends.db";
    public static final int DB_VERSION = 1;

    // list table constants
    public static final String RECENTS_TABLE = "recent_hangout_users";

    public static final String UUID = "uuid";
    public static final int UUID_COL = 0;
    public static final String USERNAME = "username";
    public static final int USERNAME_COL = 1;
    public static final String STATUS = "status";
    public static final int STATUS_COL = 2;
    public static final String HANGOUT_STATUS = "hangout_status";
    public static final int HANGOUT_STATUS_COL = 3;
    public static final String LATITUDE = "latitude";
    public static final int LATITUDE_COL = 4;
    public static final String LONGITUDE = "longitude";
    public static final int LONGITUDE_COL = 5;

    private static final String TEXT_TYPE = " TEXT";
    private static final String REAL_TYPE = " REAL";
    private static final String UNIQUE = " UNIQUE";
    private static final String COMMA_SEP = ",";


    public static final String CREATE_FRIENDS_TABLE =
            "CREATE TABLE " + RECENTS_TABLE + " (" +
                     UUID + TEXT_TYPE + UNIQUE + COMMA_SEP +
                    USERNAME + TEXT_TYPE + COMMA_SEP +
                    STATUS + TEXT_TYPE + COMMA_SEP +
                    HANGOUT_STATUS + TEXT_TYPE + COMMA_SEP +
                    LATITUDE + REAL_TYPE + COMMA_SEP +
                    LONGITUDE + REAL_TYPE +
                    ");";

    public static final String DROP_FRIENDS_TABLE = "DROP TABLE IF EXISTS " + RECENTS_TABLE;

    private static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context,name,factory,version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_FRIENDS_TABLE);

            //db.execSQL("INSERT INTO friends VALUES (\"000-00\",\"SuperPieGuy\",\"1\",\"0\",80,-180)");
            //db.execSQL("INSERT INTO friends VALUES (\"111-1\",\"RWeedle\",\"0\",\"0\",0,0)");
            //db.execSQL("INSERT INTO friends VALUES (\"222-22\",\"Yoonix\",\"0\",\"Mike\",1,0)");
            //db.execSQL("INSERT INTO friends VALUES (\"22-22\",\"Pat\",\"2\",\"0\",1,0)");
            //db.execSQL("INSERT INTO friends VALUES (\"2-22\",\"Cake\",\"-1\",\"0\",1,0)");
            //db.execSQL("INSERT INTO friends VALUES (\"-22\",\")(&*#)%\",\"-3\",\"Mike\",1,0)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d("FriendDB","Upgrading db from version " + oldVersion + " to " + newVersion);

            db.execSQL(LocalDB.DROP_FRIENDS_TABLE);
            onCreate(db);
        }
    }

    // database object and database helper object
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    // constructor
    public LocalDB(Context context) {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    // private methods
    private void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }

    private void openWriteableDB() {
        db = dbHelper.getWritableDatabase();
    }

    private void closeDB() {
        if (db != null) {
            db.close();
        }
    }

    public ArrayList<User> getAllUsers() {
        this.openReadableDB();
        Cursor cursor = db.query(RECENTS_TABLE,null,null,null,null,null,null);
        ArrayList<User> friends = new ArrayList<User>();
        while(cursor.moveToNext()) {
            friends.add(getUserFromCursor(cursor));
        }
        if(cursor != null) {
            cursor.close();
        }
        this.closeDB();

        return friends;
    }

    private static User getUserFromCursor(Cursor cursor) {
        if(cursor == null || cursor.getCount() == 0) {
            return null;
        }
        else {
            try {
                User user = new User(
                        cursor.getString(UUID_COL),
                        cursor.getString(USERNAME_COL),
                        cursor.getInt(STATUS_COL),
                        cursor.getString(HANGOUT_STATUS_COL),
                        cursor.getDouble(LATITUDE_COL),
                        cursor.getDouble(LONGITUDE_COL)
                );
                return user;
            }
            catch(Exception e) {
                return null;
            }
        }
    }

    public boolean addFriend(User user) {
        this.openReadableDB();
        ContentValues cv = new ContentValues();
        cv.put(UUID,user.getUUID());
        cv.put(USERNAME,user.getUsername());
        cv.put(STATUS,user.getStatus());
        cv.put(HANGOUT_STATUS,user.getHangStatus());
        cv.put(LATITUDE,user.getLat());
        cv.put(LONGITUDE, user.getLong());
        this.openWriteableDB();
        db.insert(RECENTS_TABLE, null,cv);
        this.closeDB();
        return true;
    }

    public boolean removeFriend(User user) {
        this.openReadableDB();
        String uuid = user.getUUID();
        String where = UUID + "= ?";
        String[] whereArgs = { uuid };
        this.openWriteableDB();
        db.delete(RECENTS_TABLE, where, whereArgs);
        this.closeDB();
        return true;
    }


    public boolean updateFriends(List<User> users) {
        for(User user : users) {
            updateFriend(user);
        }
        return true;
    }

    public boolean updateFriend(User user) {
        List<User> allUsers = getAllUsers();
        if(!allUsers.contains(user)) {
            return addFriend(user);
        }
        else {
            this.openReadableDB();
            // WHERE
            String uuid = user.getUUID();
            String where = UUID + "=?";
            String[] whereArgs = {uuid};
            // SET
            ContentValues cv = new ContentValues();
            cv.put(USERNAME,user.getUsername());
            cv.put(STATUS,user.getStatus());
            cv.put(HANGOUT_STATUS,user.getHangStatus());
            cv.put(LATITUDE,user.getLat());
            cv.put(LONGITUDE,user.getLong());
            db.update(RECENTS_TABLE,cv,where,whereArgs);
            this.closeDB();
        }
        return true;
    }
}
