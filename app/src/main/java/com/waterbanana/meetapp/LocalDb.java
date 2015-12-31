package com.waterbanana.meetapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Currently used for local db transactions.
 *
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
    public static final String G_GID = "GroupId";
    public static final String G_USER = "userid";
    public static final String T_RIBBONS_ID = "_id";
    public static final String R_NWID = "nwid";
    public static final String R_UID = "userid";
    public static final String R_DATE = "date";
    public static final String R_START = "start";
    public static final String R_END = "end";
    public static final String R_UPDATED = "updated";

    private static final String TAG = "LocalDb.java";
    private static final String DB_NAME = "MeetApp.db";
    private static final int DB_VERSION = 7;    // EV 28DEC2015|2046

    private LocalDbHelper helper;
    private SQLiteDatabase db;
    private Context _context;

    public LocalDb( Context context ){
        _context = context;
        helper = new LocalDbHelper(context);
        db = helper.getWritableDatabase();
    }

    /**
     * Purpose was to clone online db
     * @param userid
     * @param ribbon
     * @return
     */
    public long newRibbon(String userid, Ribbon ribbon){
        

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
        
        return ribbons;
    }

    public ArrayList<NameValuePair> getSelfRibbons( String date ){
        ArrayList<NameValuePair> result = new ArrayList<>();
        

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
        
        return result;
    }

    /**
     *
     * @param ribbonId
     * @param date
     * @param lineWidth
     * @param viewWidth
     * @param minValue
     * @param maxValue
     * @return online ribbon ID, -1 upon some type of error
     */
    public long saveSelfRibbon(int ribbonId, String date, int lineWidth, int viewWidth,
                               int minValue, int maxValue, boolean connectToNetwork){

        

        Cursor check = db.query(
                SR_TBL,
                new String[]{ "_id" },
                "_id = ?",
                new String[]{ Integer.toString(ribbonId) },
                null, null, null
        );

        long id = -1;
        // Create a new one
        if( check.getCount() < 1 && ribbonId == -1 ){

            try {
                id = new SendToServer().execute("new", date, Integer.toString(minValue), Integer.toString(maxValue)).get();
                
                ContentValues cv = new ContentValues();
                cv.put("_id", id);
                cv.put(SR_DATE, date);
                cv.put(SR_LINEWIDTH, lineWidth);
                cv.put(SR_VIEWWIDTH, viewWidth);
                cv.put(SR_MINVAL, minValue);
                cv.put(SR_MAXVAL, maxValue);
                db.insert(SR_TBL, null, cv);
                //success = 1;

                Log.d(TAG, "Saved self ribbon. Id: " + id +
                        "minValue: " + minValue + "maxValue: " + maxValue);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }
        // Update an existing one
        else{
            //Log.d( TAG, "Found ribbon: " + ribbonId );
            id = ribbonId;
            check.moveToFirst();
            if( check.getInt(check.getColumnIndex("_id") ) == ribbonId ){
                ContentValues cv = new ContentValues();
                cv.put( SR_MINVAL, minValue );
                cv.put( SR_MAXVAL, maxValue );
                db.update( SR_TBL, cv, "_id = ?", new String[]{Integer.toString(ribbonId)} );
            }
            if( connectToNetwork ) {
                new SendToServer().execute("update",
                        Integer.toString(ribbonId), Integer.toString(minValue), Integer.toString(maxValue));
            }
        }

        check.close();
        //

        return id;
    }

    public void setRibbonNWId( int ribbonid, int nwid ){
        

        Cursor c = db.query(
                SR_TBL,
                new String[]{"_id"},
                "_id = ?",
                new String[]{Integer.toString(ribbonid)},
                null, null, null
        );

        if( c.getCount() > 0 ){
            ContentValues cv = new ContentValues();
            cv.put(R_NWID, nwid);
            db.update(
                    SR_TBL,
                    cv,
                    "_id = ?",
                    new String[]{Integer.toString(ribbonid)}
            );
        }

        c.close();
        
    }

    public void removeSelfRibbon( int ribbonId ){

        Cursor c = db.query(
                SR_TBL,
                new String[]{"_id"},
                "_id = ?",
                new String[]{Integer.toString(ribbonId)},
                null, null, null
        );

        if( c.getCount() > 0 ){
            db.delete(SR_TBL, "_id = ?", new String[]{Integer.toString(ribbonId)});

            new SendToServer().execute("delete", Integer.toString(ribbonId));
        }

        c.close();
        
    }

    public void newGroupEntry( int groupid, String userid ){

        Cursor c = db.query(
                GROUPS_TABLE,
                new String[]{"_id"},
                G_GID + " = ? AND " + G_USER + " = ?",
                new String[]{Integer.toString(groupid), userid},
                null, null, null
        );

        if( c.getCount() < 1 ) {
            ContentValues cv = new ContentValues();
            cv.put( G_GID, groupid );
            cv.put(G_USER, userid);
            db.insert(GROUPS_TABLE, null, cv);
        }

        c.close();
        
    }

    /**
     * Currently has no proper duplication of online correspondence.
     * @param connectToNetwork
     * @return
     */
    public int[] getSelfGroups(boolean connectToNetwork){
        String encLocId = encryptId(getLocalId());
        int[] groupIdsArray = null;
        ArrayList<Integer> groupsList = new ArrayList<>();
        if(connectToNetwork) {
            try {
                groupIdsArray = new ReceiveGroups().execute(encLocId).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        else{
            Cursor c = db.query(
                    GROUPS_TABLE,
                    new String[]{G_GID, G_USER},
                    G_USER + " = ?",
                    new String[]{encLocId}, null, null, null
            );

            if(c.moveToFirst()){
                do {
                    groupsList.add(c.getInt(c.getColumnIndex(G_GID)));
                } while( c.moveToNext() );
                groupIdsArray = new int[groupsList.size()];
                for( int i = 0; i < groupsList.size(); i++ ){
                    groupIdsArray[i] = groupsList.get(i);
                }
            }
            else{
                Log.e(TAG, "Error populating self groups.");
            }
            c.close();
        }

        return groupIdsArray;

    }

    public int leaveGroup(int groupid){
        Log.d(TAG, "leaveGroup -> " + groupid);
        int success = -1;

        try{
            success = new SendToServer().execute("leavegroup", Integer.toString(groupid)).get();
        } catch( InterruptedException e ){
            e.printStackTrace();
        } catch( ExecutionException e ){
            e.printStackTrace();
        }

        return success;
    }

    public void saveLocalId( String userid ){
        

        ContentValues cv = new ContentValues();
        cv.put(LOCAL_ID, userid);
        db.insert(LOCAL_USERID_TBL, null, cv);

        
    }

    /**
     *
     * @return Non encrypted local id
     */
    public String getLocalId(){
        String userid = "";

        
        Cursor localIdCurs = db.query(
                LOCAL_USERID_TBL,
                new String[]{LOCAL_ID},
                null,
                null,
                null, null, null
        );

        if( localIdCurs.moveToLast() )
            userid = localIdCurs.getString( localIdCurs.getColumnIndex(LOCAL_ID) );

        localIdCurs.close();
        //
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
    
    public void close(){
        
    }

    class LocalDbHelper extends SQLiteOpenHelper {
        public LocalDbHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String SAVE_LOCAL_PHONENUM = "CREATE TABLE " + LOCAL_USERID_TBL + " IF NOT EXISTS(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    LOCAL_ID + " VARCHAR(32))";

            String CREATE_SELF_RIBBONS_TBL = "CREATE TABLE " + SR_TBL + " IF NOT EXISTS(" +
                    "row_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "_id INTEGER, " +
                    //R_NWID + "INTEGER, " +
                    SR_DATE + " VARCHAR(10), " +
                    SR_LINEWIDTH + " INTEGER, " +
                    SR_VIEWWIDTH + " INTEGER, " +
                    SR_MINVAL + " INTEGER, " +
                    SR_MAXVAL + " INTEGER)";

            String CREATE_SELF_GROUPS_TBL = "CREATE TABLE " + GROUPS_TABLE + " IF NOT EXISTS(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    G_GID + " INTEGER, " +
                    G_USER + " VARCHAR(32))";

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
                db.execSQL(CREATE_SELF_GROUPS_TBL);
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
            //db.execSQL(DROP_LOCALID_TBL);
            //db.execSQL(DROP_SELFRIBBONS_TBL);
            //db.execSQL(DROP_RIBBONS_TBL);
            onCreate(db);
        }
    }

    class SendToServer extends AsyncTask<String, String, Integer>{
        private DbHandler db = new DbHandler();
        String localId = getLocalId();
        String encLocalId = encryptId(localId);

//        boolean isLeavingGroup = false, deletionSucceeded = false;

        @Override
        protected Integer doInBackground(String... params) {
            int returnCode = -1;
            if(params[0].equals("new")) {
                Ribbon newRibbon = new Ribbon(-1, params[1],
                        Integer.parseInt(params[2]), Integer.parseInt(params[3]));
                try {
                    returnCode = db.insertTimeslot(encLocalId, newRibbon);
                } catch (DbHandler.DBException e) {
                    e.printStackTrace();
                }
            }
            else if(params[0].equals("update")){
                try{
                    returnCode = Integer.parseInt(params[1]);
                    db.updateTimeslot(returnCode, Integer.parseInt(params[2]), Integer.parseInt(params[3]));
                } catch( DbHandler.DBException e ){
                    e.printStackTrace();
                }
            }
            else if(params[0].equals("delete")){
                try{
                    returnCode = Integer.parseInt(params[1]);
                    db.removeTimeslot(returnCode);
                } catch( DbHandler.DBException e ){
                    e.printStackTrace();
                }
            }
            else if(params[0].equals("leavegroup")){
                int groupid = Integer.parseInt(params[1]);
//                isLeavingGroup = true;

                try{
                    Log.d("SendToServer", "Removing " + groupid );
                    returnCode = db.removeFromGroupsTable(encLocalId, groupid);
                } catch( DbHandler.DBException e ){
                    e.printStackTrace();
                }
            }

            return returnCode;
        }

//        @Override
//        protected void onPostExecute(Integer integer) {
//            super.onPostExecute(integer);
//
//            if(isLeavingGroup){
//                if(deletionSucceeded){
//                    Toast.makeText(_context, "Left group", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Toast.makeText(_context, "Error leaving group", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
    }

    class ReceiveGroups extends AsyncTask<String, String, int[]>{
        private DbHandler db = new DbHandler();

        @Override
        protected int[] doInBackground(String... params) {
            String encLocalId = params[0];

            return db.getGroupsByUserId(encLocalId);
        }
    }
}
