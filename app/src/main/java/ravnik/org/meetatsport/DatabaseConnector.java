package ravnik.org.meetatsport;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


/**
 * Created by Nejc Ravnik on 04-Jan-17.
 */
public class DatabaseConnector {
    private static final String DATABASE_NAME = "MeetAtSport";
    private static final int DATABASE_VERSION = 1;
    // database object
    private SQLiteDatabase database;

    // database helper
    private DatabaseOpenHelper databaseOpenHelper;


    public DatabaseConnector(Context context) {
        databaseOpenHelper = new DatabaseOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open() throws SQLException {
        database = databaseOpenHelper.getWritableDatabase();
    }

    public void close() {if (database != null) database.close();}


    public void insertUser(String name, String email, String age, String gender, String password,String fbProfileID) {
        final ContentValues User = new ContentValues();
        User.put("name", name);
        User.put("email", email);
        User.put("age", age);
        User.put("gender", gender);
        User.put("password", password);
        User.put("fbprofile",fbProfileID);


        open();
        database.insert("users", null, User);
        close();

    }

    public void updateUser(long id,String name, String email, String age, String gender, String password,String fbProfileID){
        final ContentValues User = new ContentValues();
        User.put("name", name);
        User.put("email", email);
        User.put("age", age);
        User.put("gender", gender);
        User.put("password", password);
        User.put("fbprofile",fbProfileID);

        open();
        database.update("users", User, "_id=" + id, null);
        close();


    }

    public void updateUserPassword(long id, String password){
        final ContentValues User = new ContentValues();

        User.put("password", password);;

        open();
        database.update("users", User,"_id="+id,null);
        close();


    }



    public boolean checkEmailExistence(String email){

        Cursor cursor=database.query("users", null, " email=?", new String[]{email}, null, null, null);

        if(cursor.getCount()==1){
            return true;
        }

        else{
            return false;
        }

    }

    public String getSHAHashIfExist(String email)
    {
        Cursor cursor=database.query("users", null, " email=?", new String[]{email}, null, null, null);
        if(cursor.getCount()<1)
        {
            cursor.close();
            return "EMAIL NOT EXISTS";
        }
        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex("password"));
        cursor.close();
        return password;
    }


    public String getEmailFromAccesToken(String fbProfileID)
    {
        Cursor cursor=database.query("users", null, " fbprofile=?", new String[]{fbProfileID}, null, null, null);
        if(cursor.getCount()<1)
        {
            cursor.close();
            return "USER NOT EXISTS";
        }
        cursor.moveToFirst();
        String email= cursor.getString(cursor.getColumnIndex("email"));
        cursor.close();
        return email;
    }




    public long getCurrentID(String email)
    {
        Cursor cursor=database.query("users", null, " email=?", new String[]{email}, null, null, null);

        cursor.moveToFirst();
        long id= cursor.getLong(cursor.getColumnIndex("_id"));

        cursor.close();
        return id;
    }


    public String getCurrentName(String email)
    {
        Cursor cursor=database.query("users", null, " email=?", new String[]{email}, null, null, null);

        cursor.moveToFirst();
        String name= cursor.getString(cursor.getColumnIndex("name"));

        cursor.close();
        return name;
    }
    public String getCurrentfbProfileID(String email)
    {
        Cursor cursor=database.query("users", null, " email=?", new String[]{email}, null, null, null);

        cursor.moveToFirst();
        String fbProfileID= cursor.getString(cursor.getColumnIndex("fbprofile"));

        cursor.close();
        return fbProfileID;
    }
    public String getCurrentAge(String email)
    {
        Cursor cursor=database.query("users", null, " email=?", new String[]{email}, null, null, null);

        cursor.moveToFirst();
        String age= cursor.getString(cursor.getColumnIndex("age"));

        cursor.close();
        return age;
    }
    public String getCurrentGender(String email)
    {
        Cursor cursor=database.query("users", null, " email=?", new String[]{email}, null, null, null);

        cursor.moveToFirst();
        String gender= cursor.getString(cursor.getColumnIndex("gender"));

        cursor.close();
        return gender;
    }







}
