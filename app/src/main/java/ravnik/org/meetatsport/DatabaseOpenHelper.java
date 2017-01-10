package ravnik.org.meetatsport;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nejc Ravnik on 04-Jan-17.
 */
public  class DatabaseOpenHelper extends SQLiteOpenHelper {

    public DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String createQuery = "CREATE TABLE IF NOT EXISTS users" +
                "(_id integer primary key autoincrement, " +
                "name TEXT, " +
                "email TEXT, " +
                "age TEXT," +
                "password TEXT, " +
                "gender TEXT, " +
                "fbprofile TEXT);";
        db.execSQL(createQuery);




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "TEMPLATE");
        onCreate(db);
    }

}
