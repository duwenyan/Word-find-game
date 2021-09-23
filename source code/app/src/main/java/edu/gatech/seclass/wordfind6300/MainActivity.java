package edu.gatech.seclass.wordfind6300;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class MainActivity extends AppCompatActivity {

    private Button Button1;
    private Button Button2;
    private Button Button3;
    private Button Button4;
    private Button Button5;

    private DBHelper dbHelper; // use the SQLite
    private DBHelper dbHelper2;
    int[] weights = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}; // initial values of the weights
    private AlertDialog.Builder builder; // time out warning dialog


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle(R.string.Team123); // Can change title

        Button1 = (Button) findViewById(R.id.playgamebutton);
        Button2 = (Button) findViewById(R.id.adjustsettingbutton);
        Button3 = (Button) findViewById(R.id.viewstatisticsbutton);
        Button4 = (Button) findViewById(R.id.exit);
        Button5 = (Button) findViewById(R.id.resetdatabase);

        generatedatabase();

    }

    public void buttonClick(View v)
    {
        switch (v.getId())
        {
            case R.id.playgamebutton:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,game.class);
                startActivity(intent); // begin the game
                break;

            case R.id.adjustsettingbutton:
                Intent intent2 = new Intent();
                intent2.setClass(MainActivity.this,setting.class);
                startActivity(intent2); // set the game
                break;


            case R.id.viewstatisticsbutton:
                Intent intent3 = new Intent();
                intent3.setClass(MainActivity.this,statistics.class);
                startActivity(intent3); // view statistics
                break;

            case R.id.resetdatabase:
                showSimpleDialog(); // reset warnings
                break;

            case R.id.exit:
                //dbHelper2 = new DBHelper(MainActivity.this, "WordGame.db", null, 1);
                //SQLiteDatabase sqliteDatabase2 = dbHelper2.getWritableDatabase();
                //deleteDatabase("WordGame.db");
                android.os.Process.killProcess(android.os.Process.myPid());
                break;

            default:
                break;
        }
    }

    private void generatedatabase() {
        dbHelper = new DBHelper(MainActivity.this, "WordGame.db", null, 1); // set the SQLite database
        SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userid", 1);
        values.put("gametime", "180000");
        values.put("boardsize", "4");
        values.put("letters", "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        values.put("weights", "11111111111111111111111111");
        values.put("gamescore", 0);
        values.put("resettime", "0");
        values.put("numberofwords", "");
        values.put("gameword", "");
        //values.put("thetimes", "0");
        //sqliteDatabase.insertWithOnConflict("BasicInfo", null, values, SQLiteDatabase.CONFLICT_IGNORE); // set all initial values, need to store that as the initial default value of the game
        sqliteDatabase.insertWithOnConflict("BasicInfo", null, values, SQLiteDatabase.CONFLICT_IGNORE);
        sqliteDatabase.close();
    }

    private void showSimpleDialog() { // reset database warning
        builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("Reset the Database"); //
        builder.setMessage("The database will be reset, all the game history will lose, all the settings will become their default values"); // show the warning message
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dbHelper2 = new DBHelper(MainActivity.this, "WordGame.db", null, 1);
                SQLiteDatabase sqliteDatabase2 = dbHelper2.getWritableDatabase();
                deleteDatabase("WordGame.db"); // delete database
                generatedatabase(); // regenerate
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // nothing to do, just go back to the screen
            }
        });
        //set the dialog can be disabled
        builder.setCancelable(true);
        AlertDialog dialog=builder.create();
        dialog.show();
    }

}
