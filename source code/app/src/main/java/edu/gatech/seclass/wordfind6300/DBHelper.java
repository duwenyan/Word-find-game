package edu.gatech.seclass.wordfind6300;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static Integer Version = 1;

    public DBHelper(){
        super(null, null, null, 1); // use that to generate a SQLite database
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BasicInfo);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { // did not use the database update

    }

    private static final String BasicInfo = "create table BasicInfo("  // the structure of the database
                    + "userid integer primary key autoincrement,"
                    + "gametime,"
                    + "boardsize,"
                    + "letters,"
                    + "weights,"
                    + "gamescore,"
                    + "resettime,"
                    + "numberofwords,"
                    + "gameword)";
}
