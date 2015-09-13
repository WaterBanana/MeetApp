package com.waterbanana.meetapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by Eddie on 8/30/2015.
 */
public class LocalDb {
    public static final String LOCAL_USERID_TBL = "LocalUserId";
    public static final String LOCAL_ID = "userid";
    public static final String SR_TBL = "SelfRibbons";
    public static final String SR_DATE = "date";
    public static final String SR_LINEWIDTH = "LineWidth";
    public static final String SR_VIEWWIDTH = "ViewWidth";
    public static final String SR_MINVAL = "minVal";
    public static final String SR_MAXVAL = "maxVal";
    public static final String RIBBONS_TABLE = "Ribbons";
    public static final String GROUPS_TABLE = "Groups";
    public static final String T_RIBBONS_ID = "_id";
    public static final String R_NWID = "nwid";
    public static final String R_UID = "userid";
    public static final String R_DATE = "date";
    public static final String R_START = "start";
    public static final String R_END = "end";
    public static final String R_UPDATED = "updated";

    private static final String TAG = "LocalDb.java";
    private static final String DB_NAME = "MeetApp.db";
    private static final int DB_VERSION = 3;

    private LocalDbHelper helper;
    private SQLiteDatabase db;

    public LocalDb( Context context ){
        helper = new LocalDbHelper(context);
    }

    public long newRibbon(String userid, Ribbon ribbon){
        db = helper.getWritableDatabase();

        String ribbonDate = ribbon.getDate();
        String ribbonStart = Integer.toString(ribbon.getStart());
        String ribbonEnd = Integer.toString(ribbon.getEnd());
        String currTime = Long.toString(System.currentTimeMillis());

        // Checks if ribbon is already in db.
        Cursor check = db.query(
                RIBBONS_TABLE,
                new String[]{ R_UID, R_DATE, R_START, R_END, R_UPDATED },
                R_UID + " = ? AND " +
                        R_DATE + " = ? AND " +
                        R_START + " = ? AND " +
                        R_END + " = ? AND " +
                        R_UPDATED + " = ?",
                new String[]{ userid, ribbonDate, ribbonStart, ribbonEnd, currTime },
                null, null, null
        );
        long id;
        if( check.getCount() < 1 ){
            ContentValues cv = new ContentValues();
            cv.put( R_UID, userid );
            cv.put( R_DATE, ribbonDate );
            cv.put( R_START, ribbonStart );
            cv.put( R_END, ribbonEnd );
            cv.put( R_UPDATED, currTime );
            id = db.insert( RIBBONS_TABLE, null, cv );
        }
        else{   // Ribbon already exists
            id = -2;
            // Ribbon should be updated instead.
        }

        check.close();
        db.close();
        return id;

//        Cursor check = db.query(
//                TABLE_NAME,
//                new String[]{NAME},
//                NAME + " = ? AND " + OWES_ME + " = ?",
//                new String[]{name, DbHelper.sOWES_ME},
//                null, null, null);
//        long id;
//        if (check.getCount() < 1) {
//            ContentValues cv = new ContentValues();
//            cv.put(NAME, name);
//            cv.put(OWES_ME, DbHelper.OWES_ME);
//            cv.put(AMOUNT, amount);
//            id = db.insert(TABLE_NAME, null, cv);
//        } else {
//            id = -2;
//        }
//        close();
//        return id;
    }

    public Ribbon[] getRibbonsByUserId( String userid, String date ){
        Ribbon[] ribbons = null;
        db = helper.getWritableDatabase();

        Cursor c = db.query(
                RIBBONS_TABLE,
                new String[]{ T_RIBBONS_ID, R_NWID, R_UID, R_DATE, R_START, R_END },
                R_UID + " = ? AND " + R_DATE + " = ?",
                new String[]{ userid, date },
                null, null, null
        );

        if( c.moveToFirst() ){
            ribbons = new Ribbon[c.getCount()];
            int i = 0;
            do{
                int RID = c.getInt( c.getColumnIndex(T_RIBBONS_ID) );
                int NWID = c.getInt( c.getColumnIndex(R_NWID) );
                int ribbonStart = c.getInt( c.getColumnIndex(R_START) );
                int ribbonEnd = c.getInt( c.getColumnIndex(R_END) );
                ribbons[i] = new Ribbon(RID, NWID, date, ribbonStart, ribbonEnd);
            } while( c.moveToNext() );
        }

        c.close();
        db.close();
        return ribbons;
    }

    public ArrayList<NameValuePair> getSelfRibbons( String date ){
        ArrayList<NameValuePair> result = new ArrayList<>();
        db = helper.getWritableDatabase();

        Log.d( TAG, "Loading self ribbons for: " + date );
        Cursor c = db.query(
                SR_TBL,
                new String[]{ "_id", SR_LINEWIDTH, SR_VIEWWIDTH, SR_MINVAL, SR_MAXVAL },
                SR_DATE + " = ?",
                new String[]{date}, null, null, null
        );

        if(c.moveToFirst()){
            do {
                Log.d( TAG, "Adding a self ribbon." );
                result.add(new BasicNameValuePair("_id", Integer.toString(c.getInt(c.getColumnIndex("_id")))));
                Log.d( TAG, "Found ribbon: " + Integer.toString(c.getInt(c.getColumnIndex("_id"))));
                result.add(new BasicNameValuePair(SR_LINEWIDTH, Integer.toString(c.getInt(c.getColumnIndex(SR_LINEWIDTH)))));
                result.add(new BasicNameValuePair(SR_VIEWWIDTH, Integer.toString(c.getInt(c.getColumnIndex(SR_VIEWWIDTH)))));
                result.add(new BasicNameValuePair(SR_MINVAL, Integer.toString(c.getInt(c.getColumnIndex(SR_MINVAL)))));
                result.add(new BasicNameValuePair(SR_MAXVAL, Integer.toString(c.getInt(c.getColumnIndex(SR_MAXVAL)))));
                result.add(new BasicNameValuePair("paintthisshit", "NOW"));
            } while( c.moveToNext() );
        }

        c.close();
        db.close();
        return result;
    }

    public long saveSelfRibbon(int ribbonId, String date, int lineWidth, int viewWidth,
                               int minValue, int maxValue){
        db = helper.getWritableDatabase();

        Cursor c = db.query(
                SR_TBL,
                new String[]{ "_id" },
                "_id = ?",
                new String[]{ Integer.toString(ribbonId) },
                null, null, null
        );

        long id;
        // Create a new one
        if( c.getCount() < 1 && ribbonId == -1 ){
            ContentValues cv = new ContentValues();
            cv.put( SR_DATE, date );
            cv.put( SR_LINEWIDTH, lineWidth );
            cv.put( SR_VIEWWIDTH, viewWidth );
            cv.put( SR_MINVAL, minValue );
            cv.put( SR_MAXVAL, maxValue );
            id = db.insert( SR_TBL, null, cv );
            Log.d( TAG, "Saved self ribbon. Id: " + id +
                    "minValue: " + minValue + "maxValue: " + maxValue );
        }
        // Update an existing one
        else{
            //Log.d( TAG, "Found ribbon: " + ribbonId );
            id = ribbonId;
            c.moveToFirst();
            if( c.getInt(c.getColumnIndex("_id") ) == ribbonId ){
                ContentValues cv = new ContentValues();
                cv.put( SR_MINVAL, minValue );
                cv.put( SR_MAXVAL, maxValue );
                db.update( SR_TBL, cv, "_id = ?", new String[]{Integer.toString(ribbonId)} );
            }
        }

        c.close();
        db.close();

        return id;
    }

    public void removeSelfRibbon( int ribbonId ){
        db = helper.getWritableDatabase();

        Cursor c = db.query(
                SR_TBL,
                new String[]{"_id"},
                "_id = ?",
                new String[]{ Integer.toString(ribbonId) },
                null, null, null
        );

        if( c.getCount() > 0 ){
            db.delete( SR_TBL, "_id = ?", new String[]{ Integer.toString(ribbonId) } );
        }

        c.close();
        db.close();
    }

    public void saveLocalId( String userid ){
        db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(LOCAL_ID, userid);
        db.insert(LOCAL_USERID_TBL, null, cv);

        db.close();
    }

    public String getLocalId(){
        String userid = "";

        db = helper.getWritableDatabase();
        Cursor c = db.query(
                LOCAL_USERID_TBL,
                new String[]{LOCAL_ID},
                null,
                null,
                null, null, null
        );

        if( c.moveToLast() )
            userid = c.getString( c.getColumnIndex(LOCAL_ID) );

        c.close();
        db.close();
        return userid;
    }

    public String encryptId( String userid ) {
        String result = "";
        for( int i = 0; i < userid.length(); i++ ){
            int encKey = (((int)userid.charAt(i)) + 2) * 2;
            result += (char) encKey;
        }

        return result;
    }

    public String decryptId( String encUserid ){
        String result = "";
        for( int i = 0; i < encUserid.length(); i++ ){
            int decKey = (((int)encUserid.charAt(i)) / 2) - 2;
            result += (char) decKey;
        }

        return result;
    }

    class LocalDbHelper extends SQLiteOpenHelper {
        public LocalDbHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String SAVE_LOCAL_PHONENUM = "CREATE TABLE " + LOCAL_USERID_TBL + "(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    LOCAL_ID + " VARCHAR(32))";

            String CREATE_SELF_RIBBONS_TBL = "CREATE TABLE " + SR_TBL + "(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SR_DATE + " VARCHAR(10), " +
                    SR_LINEWIDTH + " INTEGER, " +
                    SR_VIEWWIDTH + " INTEGER, " +
                    SR_MINVAL + " INTEGER, " +
                    SR_MAXVAL + " INTEGER)";

//            String CREATE_RIBBONS_TBL = "CREATE TABLE " + RIBBONS_TABLE + "(" +
//                    T_RIBBONS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                    R_NWID + " INTEGER, " +
//                    R_UID + " VARCHAR(32), " +
//                    R_DATE + " VARCHAR(32), " +
//                    R_START + " INTEGER, " +
//                    R_END + " INTEGER, " +
//                    R_UPDATED + " INTEGER)";
            try{
                db.execSQL(SAVE_LOCAL_PHONENUM);
                db.execSQL(CREATE_SELF_RIBBONS_TBL);
                //db.execSQL(CREATE_RIBBONS_TBL);
            } catch( SQLException e ){
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String DROP_LOCALID_TBL = "DROP TABLE IF EXISTS " + LOCAL_USERID_TBL;
            String DROP_SELFRIBBONS_TBL = "DROP TABLE IF EXISTS " + SR_TBL;
            //String DROP_RIBBONS_TBL = "DROP TABLE IF EXISTS " + RIBBONS_TABLE;
            db.execSQL(DROP_LOCALID_TBL);
            db.execSQL(DROP_SELFRIBBONS_TBL);
            //db.execSQL(DROP_RIBBONS_TBL);
            onCreate(db);
        }
    }
}
