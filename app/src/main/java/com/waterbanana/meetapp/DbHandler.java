package com.waterbanana.meetapp;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Eddie on 6/18/2015.
 */
public class DbHandler {
    private static final String
            get_all_users_url = "http://waterbanana.comuv.com/dbhandler/get_userinfo.php";
    private static final String
            select_user_url = "http://waterbanana.comuv.com/dbhandler/select_userinfo.php";
    private static final String
            register_user_url = "http://waterbanana.comuv.com/dbhandler/new_user.php";
    private static final String
            new_user_url = "http://waterbanana.comuv.com/dbhandler/new_userinfo.php";
    private static final String
            delete_user_url = "http://waterbanana.comuv.com/dbhandler/delete_userinfo.php";
    private static final String
            update_user_url = "http://waterbanana.comuv.com/dbhandler/update_userinfo.php";
    private static final String
            new_group_url = "http://waterbanana.comuv.com/dbhandler/new_group.php";
    private static final String
            get_all_groups_url = "http://waterbanana.comuv.com/dbhandler/get_group.php";
    private static final String
            select_group_url = "http://waterbanana.comuv.com/dbhandler/select_group.php";
    private static final String
            leave_group_url = "http://waterbanana.comuv.com/dbhandler/leave_group.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_USER = "user";
    private static final String TAG_USERID = "userid";
    private static final String TAG_DBID = "id";
    private static final String TAG_DATE = "date";
    private static final String TAG_START = "start";
    private static final String TAG_END = "end";
    private static final String TAG_GROUPS = "groups";
    private static final String TAG_GROUPID = "groupid";

    /**
     * Empty constructor. Access database functions.
     */
    public DbHandler(){  }

    /**
     * Get all timeslots of a user given user's id.
     * Note: Does not include user's groups.
     * @param userid user's id
     * @return Resulting user.
     */
    public User getAllRibbonsByUserId( String userid ){
        User user = null;
        JSONParser jsonParser = new JSONParser();
        JSONArray times;
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add( new BasicNameValuePair( TAG_USERID, userid ) );

        Log.d( this.toString(), "Getting User: " + userid );
        JSONObject json = jsonParser.makeHttpRequest( select_user_url, "GET", params );
        Log.d( this.toString(), "Get User: " + json.toString() );

        try{
            int success = json.getInt( TAG_SUCCESS );
            if( success == 1 ){     // Successfully grabbed user.
                ArrayList<Ribbon> ribbons = new ArrayList<>();
                times = json.getJSONArray( TAG_USER );
                for( int i = 0; i < times.length(); i++ ){
                    JSONObject u = times.getJSONObject( i );
                    int ribbonID = u.getInt( TAG_DBID );
                    String date = u.getString(TAG_DATE);
                    int start = u.getInt(TAG_START);
                    int end = u.getInt( TAG_END );
                    Ribbon ribbon = new Ribbon( ribbonID, date, start, end );
                    ribbons.add( ribbon );
                }
                user = new User( userid, ribbons );
                Log.d( this.toString(), "User Created: " + userid );
            }
            else{
                Log.e( this.toString(), json.getString( TAG_MESSAGE ) );
            }
        } catch( JSONException e ){
            Log.e( this.toString(), e.toString() );
        }

        return user;
    }

    /**
     * Get all groups a user is associated with.
     * @param userid user's id
     * @return int array of all group id's.
     */
    public int[] getGroupsByUserId( String userid ){
        int[] groupids = null;
        JSONParser jsonParser = new JSONParser();
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add( new BasicNameValuePair( TAG_USERID, userid ) );

        Log.d(this.toString(), "Trying getGroupsByUserId()");
        JSONObject json = jsonParser.makeHttpRequest( select_group_url, "GET", params );
        Log.d( this.toString(), json.toString() );

        try{
            int success = json.getInt(TAG_SUCCESS);
            if( success == 1 ){
                JSONArray allGroups = json.getJSONArray( TAG_GROUPS );
                int groupsSize = allGroups.length();
                groupids = new int[groupsSize];
                for( int i = 0; i < groupsSize; i++ ){
                    JSONObject entry = allGroups.getJSONObject( i );
                    groupids[i] = entry.getInt( TAG_GROUPID );
                }
            }
            else{
                Log.e( toString(), json.getString( TAG_MESSAGE ) );
            }
        } catch( JSONException e ){
            Log.e( this.toString(), e.toString() );
        }

        return groupids;
    }

    /**
     * Returns all users in the specified group.
     * @param groupid group's id
     * @return Array of users with all their ribbons.
     */
    public User[] getUsersByGroupId( int groupid ){
        User[] users = null;
        JSONParser jsonParser = new JSONParser();
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add( new BasicNameValuePair( TAG_GROUPID, Integer.toString( groupid ) ) );

        Log.d(this.toString(), "Trying getUsersByGroupId()");
        JSONObject json = jsonParser.makeHttpRequest( select_group_url, "GET", params );
        Log.d( this.toString(), json.toString() );
        try{
            int success = json.getInt(TAG_SUCCESS);
            if( success == 1 ){
                JSONArray entries = json.getJSONArray(TAG_USER);
                int numUsers = entries.length();
                users = new User[numUsers];
                for( int i = 0; i < numUsers; i++ ){
                    JSONObject user = entries.getJSONObject(i);
                    String userid = user.getString( TAG_USERID );
                    User newUser = getAllRibbonsByUserId(userid);
                    users[i] = newUser;
                }
            }
            else{
                Log.e( this.toString(), json.getString( TAG_MESSAGE ) );
            }
        } catch( JSONException e ){
            Log.e( this.toString(), e.toString() );
        }

        return users;
    }

    /**
     * Registers a new user into the db.
     * @param encPhoneNum User's encrypted phone number (aka userid)
     * @return -1 upon failure
     */
    public int registerUser(String encPhoneNum){
        int success = -1;
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add( new BasicNameValuePair( TAG_USERID, encPhoneNum ) );

        JSONParser jsonParser = new JSONParser();
        Log.d( this.toString(), "Registering User: " + encPhoneNum );
        JSONObject json = jsonParser.makeHttpRequest( register_user_url, "POST", params );
        Log.d( this.toString(), json.toString() );

        try{
            success = json.getInt( TAG_SUCCESS );
            if( success == 1 ){
                return success;
            }
            else{
                success = -1;
            }
        } catch( JSONException e ){
            Log.e( this.toString(), e.toString() );
        }
        
        return success;
    }

    /**
     * Returns all users in UserInfo table (all ribbons).
     * @return User array.
     */
    public User[] getAllUsers(){
        User[] users = null;
        ArrayList<NameValuePair> params = new ArrayList<>();

        JSONParser jsonParser = new JSONParser();
        Log.d(this.toString(), "Getting All Users");
        JSONObject json = jsonParser.makeHttpRequest( get_all_users_url, "GET", params );
        Log.d( this.toString(), json.toString() );

        try{
            int success = json.getInt( TAG_SUCCESS );
            if( success == 1){
                JSONArray u = json.getJSONArray( TAG_USER );
                ArrayList<User> userList = new ArrayList<>();
                String prevUserid = "";
                User tmpUser = null;
                for( int i = 0; i < u.length(); i++ ){
                    JSONObject currUser = u.getJSONObject( i );
                    String userid = currUser.getString( TAG_USERID );
                    int ribbonid = currUser.getInt( TAG_DBID );
                    String date = currUser.getString( TAG_DATE );
                    int start = currUser.getInt( TAG_START );
                    int end = currUser.getInt( TAG_END );
                    Ribbon tmpRibbon = new Ribbon( ribbonid, date, start, end );

                    Log.d( this.toString(), "Getting all users: Entry " + i + " - User: " + userid );
                    if( userid.equals( prevUserid ) ) {
                        Log.d(this.toString(), "Same user: Adding timeslot " + start + end );
                        int numTimes = tmpUser.addTimeSlot( tmpRibbon );
                        Log.d( this.toString(), userid + " has " + numTimes + " entries." );
                    }
                    else{
                        Log.d(this.toString(), "Different user: Making new user");
                        if( i != 0 )
                            userList.add( tmpUser );
                        tmpUser = new User(userid);
                        tmpUser.addTimeSlot(tmpRibbon);
                    }
                    prevUserid = userid;
                    if( i == (u.length() - 1) )
                        userList.add( tmpUser );
                }
                users = new User[userList.size()];
                for( int i = 0; i < userList.size(); i++ ){
                    users[i] = userList.get( i );
                }
            }

        } catch( JSONException e ){
            Log.e( this.toString(), e.toString() );
        }


        return users;
    }

    /**
     * Inserts a new ribbon into the database.
     * @param userid don't forget to encrypt
     * @param ribbon date, start, end in ribbon are required
     * @return online ribbonid upon success, -1 if failed
     * @throws DBException
     */
    public int insertTimeslot( String userid, Ribbon ribbon ) throws DBException{
        int id = -1;    // RibbonID acquired by DB
        String date = ribbon.getDate();
        String start = Integer.toString( ribbon.getStart() );
        String end = Integer.toString( ribbon.getEnd() );
        JSONParser jsonParser = new JSONParser();
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add( new BasicNameValuePair( TAG_USERID, userid ) );
        params.add( new BasicNameValuePair( TAG_DATE, date ) );
        params.add( new BasicNameValuePair( TAG_START, start ) );
        params.add(new BasicNameValuePair(TAG_END, end));

        Log.d( this.toString(), "New Timeslot: User ID - " + userid );

        JSONObject json = jsonParser.makeHttpRequest( new_user_url, "POST", params );
        Log.d( this.toString(), json.toString() );

        try{
            int success = json.getInt( TAG_SUCCESS );
            if( success == 1 ){
                id = json.getInt( TAG_DBID );
                Log.d( this.toString(), "Added new timeslot into database." );
            }
            else{
                throw new DBException( json.getString( TAG_MESSAGE ) );
            }
        } catch( JSONException e ){
            Log.e( this.toString(), e.toString() );
        }

        return id;
    }

    /**
     * Deletes a ribbon from the database given the ribbonid.
     * @param ribbonid generated by database
     * @return -1 on failure
     * @throws DBException
     */
    public int removeTimeslot( int ribbonid ) throws DBException{
        int success = -1;
        ArrayList<NameValuePair> params = new ArrayList<>();
        String id = Integer.toString( ribbonid );
        params.add(new BasicNameValuePair(TAG_DBID, id));
        JSONParser jsonParser = new JSONParser();

        Log.d( this.toString(), "Removing Ribbon ID: " + id );
        JSONObject json = jsonParser.makeHttpRequest( delete_user_url, "POST", params );
        Log.d( this.toString(), json.toString() );

        try {
            success = json.getInt( TAG_SUCCESS );
            if (success == 1) {
                Log.d( this.toString(), "Successfully removed timeslot." );
            }
            else{
                success = -1;
                throw new DBException( json.getString(TAG_MESSAGE) );
            }
        } catch( JSONException e ){
            Log.e( this.toString(), e.toString() );
        }

        return success;
    }

    /**
     * Returns all entries in Groups table.
     * @return User array.
     */
    public User[] getAllGroups(){
        User[] groups = null;
        JSONParser jsonParser = new JSONParser();
        ArrayList<NameValuePair> params = new ArrayList<>();

        Log.d( this.toString(), "Getting all groups" );
        JSONObject json = jsonParser.makeHttpRequest( get_all_groups_url, "GET", params );
        Log.d( this.toString(), json.toString() );

        try{
            int success = json.getInt( TAG_SUCCESS );
            if( success == 1){
                JSONArray entries = json.getJSONArray( TAG_GROUPS );
                String prevuserid = "";
                User tmpUser = null;
                ArrayList<User> userList = new ArrayList<>();

                for( int i = 0; i < entries.length(); i++ ){
                    JSONObject entry = entries.getJSONObject( i );
                    String userid = entry.getString( TAG_USERID );
                    if( userid.equals( prevuserid ) ){
                        // Same user
                        tmpUser.addToGroupList(entry.getInt(TAG_GROUPID));
                    }
                    else{
                        if( i != 0 )
                            userList.add( tmpUser );
                        tmpUser = new User( userid );
                        tmpUser.addToGroupList(entry.getInt(TAG_GROUPID));
                    }
                    if( i == (entries.length() - 1) ){
                        userList.add( tmpUser );
                    }
                }
                int numEntries = userList.size();
                groups = new User[numEntries];
                for( int i = 0; i < numEntries; i++ ){
                    groups[i] = userList.get( i );
                }
            }
        } catch( JSONException e ){
            Log.e( this.toString(), e.toString() );
        }

        return groups;
    }

    /**
     * Saves user's new group in the database.
     * @param userid don't forget to encrypt
     * @param groupid generated by application
     * @return -1 upon failure.
     */
    public int insertToGroupsTable( String userid, int groupid ){
        int success = -1;
        JSONParser jsonParser = new JSONParser();
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add( new BasicNameValuePair( TAG_USERID, userid ) );
        params.add( new BasicNameValuePair( TAG_GROUPID, Integer.toString( groupid ) ) );

        Log.d( this.toString(), "Inserting " + userid + " to group " + Integer.toString( groupid ) );
        JSONObject json = jsonParser.makeHttpRequest( new_group_url, "POST", params );
        Log.d( this.toString(), json.toString() );

        try{
            success = json.getInt( TAG_SUCCESS );
            if( success == 1 ){
                Log.d( this.toString(), "User successfully inserted into group." );
            }
            else{
                success = -1;
                Log.e( this.toString(), json.getString( TAG_MESSAGE ) );
            }
        } catch( JSONException e ){
            Log.e( this.toString(), e.toString() );
        }

        return success;
    }

    /**
     * Removes user from a group in the database.
     * @param userid user's id
     * @param groupid group's id
     * @return -1 upon failure.
     */
    public int removeFromGroupsTable( String userid, int groupid ) throws DBException{
        int success = -1;
        JSONParser jsonParser = new JSONParser();
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add( new BasicNameValuePair( TAG_USERID, userid ) );
        params.add( new BasicNameValuePair( TAG_GROUPID, Integer.toString( groupid ) ) );

        Log.d( this.toString(), "Removing " + userid + " from group " + Integer.toString( groupid ) );
        JSONObject json = jsonParser.makeHttpRequest( leave_group_url, "POST", params );
        Log.d( this.toString(), json.toString() );

        try{
            success = json.getInt( TAG_SUCCESS );
            if( success == 1 ){
                Log.d( this.toString(), "User successfully removed from group." );
            }
            else{
                success = -1;
                throw new DBException( json.getString( TAG_MESSAGE ) );
            }
        } catch( JSONException e ){
            Log.e( this.toString(), e.toString() );
        }

        return success;
    }

    /**
     *
     * @return "DbHandler"
     */
    public String toString(){
        return "DbHandler";
    }

    class DBException extends Exception{
        public DBException( String m ){
            super(m);
        }
    }
}
