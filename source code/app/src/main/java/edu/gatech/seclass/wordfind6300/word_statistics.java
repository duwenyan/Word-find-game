package edu.gatech.seclass.wordfind6300;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class word_statistics extends AppCompatActivity {

    private DBHelper dbHelper; // database

    private Button Button1;
    private Button Button2;
    private TextView textView08;
    private TextView textView09;
    private ListView wv;
    private word_statistics.MyAdapter adapter;
    private ArrayList<word_info> wordinfolist; // use that to generate the listview


    String highscoreword = "";
    ArrayList<String> words = new ArrayList<String>();
    ArrayList<String> words2 = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_statistics);
        this.setTitle(R.string.Team123);
        Button1  = (Button) findViewById(R.id.return_statistics);
        Button2  = (Button) findViewById(R.id.return1);
        Button1.setOnClickListener(new ButtonListener1());
        Button2.setOnClickListener(new ButtonListener1());
        textView08 = (TextView) findViewById(R.id.textView08);
        //textView09 = (TextView) findViewById(R.id.textView09);
        wv = (ListView) findViewById(R.id.word_lv); // generate listview
        getvalue(); // get values from database
        adapter = new MyAdapter();
        wv.setAdapter(adapter);
        wv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                adapter.setClickPosition(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

    class MyAdapter extends BaseAdapter {
        private int clickPosition = -1;


        public void setClickPosition(int clickPosition) {
            this.clickPosition = clickPosition;
        }

        @Override
        public int getCount() {
            return wordinfolist.size();
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
        public View getView(int position, View converView, ViewGroup arg2) {
            ViewHolder holder = null;
            if(converView==null){
                converView = View.inflate(getBaseContext(), R.layout.word_statistics_sub, null);
                holder = new ViewHolder();
                converView.setTag(holder);
            }else{
                holder = (ViewHolder) converView.getTag();
            }
            word_info wd = wordinfolist.get(position);
            TextView playedword = (TextView) converView.findViewById(R.id.most_played_word);
            TextView numberword = (TextView) converView.findViewById(R.id.number_words_played); // show the word information
            playedword.setText((wd.getWord()));
            numberword.setText((wd.getNumber()));
            //textView08.setText(wd.getWord());
            if(clickPosition==position){ // if clicked, nothing needs to be shown
                converView.setBackgroundResource((android.R.color.holo_blue_dark));
                //showSimpleDialog(position);
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

    private class ButtonListener1 implements OnClickListener{
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.return_statistics:
                    Intent intent = new Intent();
                    intent.setClass(word_statistics.this,statistics.class);
                    startActivity(intent); // return to statistics menu
                    break;

                    case R.id.return1:
                        Intent intent2 = new Intent();
                        intent2.setClass(word_statistics.this,MainActivity.class);
                        startActivity(intent2); // return to main menu
                        break;

                        default:
                            break;
            }
        }
    }

    // --------------------------Get value part-----------------------
    private void getvalue() { // get values from database

        dbHelper = new DBHelper(word_statistics.this, "WordGame.db", null, 1);
        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqliteDatabase.query("BasicInfo", null, "userid>?", new String[] { "1" }, null, null, null); // the first is used to store the game settings
        highscoreword = "";
        words = new ArrayList<String>();
        words2 = new ArrayList<String>();
        while (cursor.moveToNext()) {
            highscoreword = cursor.getString(cursor.getColumnIndex("gameword"));
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            Gson gson = new Gson();
            words = gson.fromJson(highscoreword, type);
            words2.addAll(words); // transfer Json to string array.
        }
        sqliteDatabase.close();


        Map<String, Integer> map = new HashMap<String, Integer>(); // generate a hash map to get the showed word and its times

        for (String obj : words2) {
            if (map.containsKey(obj)) {
                map.put(obj, map.get(obj).intValue() + 1);
            } else {
                map.put(obj, 1);
            }
        }
        List<Map.Entry<String, Integer>> infoIds = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
        Collections.sort(infoIds, new Comparator<Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o2.getValue()).toString().compareTo(o1.getValue().toString());
            }
        });
        wordinfolist = new ArrayList<>(); // from the most showed to the least showed word

        for (Map.Entry<String, Integer> mapping : infoIds) {
            String playedword = mapping.getKey();
            String playedwordnumber = String.valueOf(mapping.getValue());
            word_info wd = new word_info(playedword,playedwordnumber);
            wordinfolist.add(wd);
        }


        //textView09.setText(String.valueOf(maxnumber));// set the textviews to show
    }
    // --------------------------Get value part end-----------------------

}
