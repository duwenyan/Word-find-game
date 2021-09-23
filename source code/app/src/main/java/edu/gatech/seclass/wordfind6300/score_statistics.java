package edu.gatech.seclass.wordfind6300;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

public class score_statistics extends AppCompatActivity {

    private DBHelper dbHelper; // database

    private Button Button1;
    private Button Button2;
    private ListView lv;
    private MyAdapter adapter;
    private AlertDialog.Builder builder; // dialog

    //private TextView textViewblank;
    //private TextView textView06;
    //private TextView textView07;

    private ArrayList<game_info> gameinfolist; // store the game info

    //String gamescore;
    //String rerolltime;
    //String wordnumber; // variables need to show
    ArrayList<String> timeStamps; // store time setting
    ArrayList<String> board_sizes; // board setting
    ArrayList<String> highscorewords;
    int timeStamp;
    int board_size;
    String highscoreword;
    ArrayList<String> words = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_statistics);
        this.setTitle(R.string.Team123);
        //textViewblank = (TextView) findViewById(R.id.textViewblank);
        //textView06 = (TextView) findViewById(R.id.textView06);
        //textView07 = (TextView) findViewById(R.id.textView07);
        lv = (ListView) findViewById(R.id.game_lv); // generate the listview
        Button1 = (Button) findViewById(R.id.return_statistics);
        Button2 = (Button) findViewById(R.id.return1);
        Button1.setOnClickListener(new ButtonListener2());
        Button2.setOnClickListener(new ButtonListener2());
        lv = (ListView) findViewById(R.id.game_lv); // generate the listview
        initData();
        adapter = new MyAdapter();
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                adapter.setClickPosition(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initData() {  // get the values from database
        dbHelper = new DBHelper(score_statistics.this, "WordGame.db", null, 1);
        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
        gameinfolist = new ArrayList<>();
        timeStamps = new ArrayList<String>();
        board_sizes = new ArrayList<String>();
        highscorewords = new ArrayList<String>();
        Cursor cursor = sqliteDatabase.query("BasicInfo", null, "userid>?", new String[] { "1" }, null, null, "gamescore desc");
        while (cursor.moveToNext()) {
            String gamescore = String.valueOf((cursor.getInt(cursor.getColumnIndex("gamescore"))));
            String resettime = (cursor.getString(cursor.getColumnIndex("resettime")));
            String numberofwords = (cursor.getString(cursor.getColumnIndex("numberofwords"))); // get all tha data
            timeStamps.add(cursor.getString(cursor.getColumnIndex("gametime"))); // get game end time
            board_sizes.add(cursor.getString(cursor.getColumnIndex("boardsize"))); // get board size
            highscorewords.add(cursor.getString(cursor.getColumnIndex("gameword"))); // store the corresponding settings and words
            game_info st = new game_info(gamescore,resettime,numberofwords);
            gameinfolist.add(st);
        }
        sqliteDatabase.close();
    }

    class MyAdapter extends BaseAdapter{
        private int clickPosition = -1;


        public void setClickPosition(int clickPosition) {
            this.clickPosition = clickPosition;
        }

        @Override
        public int getCount() {
            return gameinfolist.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View converView, ViewGroup arg2) { // get the listview
            ViewHolder holder = null;
            if(converView==null){
                converView = View.inflate(getBaseContext(), R.layout.score_statistics_sub, null);
                holder = new ViewHolder();
                converView.setTag(holder);
            }else{
                holder = (ViewHolder) converView.getTag();
            }
            game_info st = gameinfolist.get(position);
            TextView score = (TextView) converView.findViewById(R.id.final_game_score);
            TextView resettimes = (TextView) converView.findViewById(R.id.board_reset_time);
            TextView wordnumber = (TextView) converView.findViewById(R.id.number_words_entered);
            score.setText((st.getScore()));
            resettimes.setText((st.getReroll()));
            wordnumber.setText((st.getNumber())); // show the information got from database
            if(clickPosition==position){
                converView.setBackgroundResource((android.R.color.holo_blue_dark));
                showSimpleDialog(position); // if click the list, need to show other information
            }else{
                converView.setBackgroundResource((android.R.color.white));
            }
            //holder.tvContent.setText(gameinfolist.get(position));
            return converView;
        }

        class ViewHolder{
            TextView tvContent;
            RelativeLayout rlRoot;
        }
    }

    private void showSimpleDialog(int position) { // when the end time countdown to zero, show a warning dialog

        timeStamp = Integer.parseInt(timeStamps.get(position)); // get game end time
        board_size = Integer.parseInt(board_sizes.get(position)); // get board size
        highscoreword = highscorewords.get(position); // get all the submitted words
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        Gson gson = new Gson();
        words = gson.fromJson(highscoreword, type); // transfer Json to string array
        Iterator it = words.iterator();
        String temp = null;
        String str = "";
        while(it.hasNext())
        {
            temp = (String)it.next();
            if(temp.length()>str.length())
            {
                str = temp;
            }
        } // get the first longest words
        builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("Detailed information"); //
        builder.setMessage("Game's board size is:   " + Integer.toString(board_size) + " * " + Integer.toString(board_size) + "\n" +
                           "Number of minutes are:   " + Integer.toString(timeStamp/1000/60) + " mins" + "\n" +
                           "Highest scoring word played is:   " + str.toUpperCase() ); // show the information
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
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

    private class ButtonListener2 implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.return_statistics:
                    Intent intent = new Intent();
                    intent.setClass(score_statistics.this,statistics.class);
                    startActivity(intent); // return to statistics menu
                    break;

                case R.id.return1:
                    Intent intent2 = new Intent();
                    intent2.setClass(score_statistics.this,MainActivity.class);
                    startActivity(intent2); // return to main menu
                    break;

                default:
                    break;
            }
        }
    }
    // --------------------------Get value part-----------------------
   // private void getvalue() { // get values from database

        //textViewblank.setText((gamescore));
        //textView06.setText(String.valueOf(rerolltime));
        //textView07.setText(String.valueOf(wordnumber)); // set the textviews to show
    //}
    // --------------------------Get value part end-----------------------

}
