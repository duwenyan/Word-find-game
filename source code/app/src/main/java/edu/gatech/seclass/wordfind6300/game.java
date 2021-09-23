package edu.gatech.seclass.wordfind6300;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import java.util.Random;
import java.util.*;



public class game extends AppCompatActivity {

    private DBHelper dbHelper; // database

    private Button Button1;
    private Button Button2;
    private Button Button3;
    private Button Button4;
    private TextView textViewword2; // button and textview

    private CountDownTimer timer; // set timer
    int timeStamp; // set initial value, need to transfer s to ms
    private TextView textViewTime; // show the time countdown
    private TextView textViewScore; // show the score

    int board_size; // board size
    private GridLayout gridLayout; //set the board

    Button[] button; // buttons in the board
    char[] final_button_letters; // letters shown on the board
    String letters = " "; // letters and their weight
    String weights_string = "";
    int[] flag = new int[64]; // use flag to control buttons, the max size is 8 * 8 = 64
    int[] button_sequence = new int[128]; // use this to restore clicked buttons sequence, the max size is 8 * 8 * 2 ("q" becomes "qu")= 128
    int sequence; // use this to get the re-clicked button sequence
    int sequence_len = 0; // the sequence length
    ArrayList<String> submittedwords = new ArrayList<String>(); // the words have been successfully submitted

    int[] weights = new int [26];

    int re_roll_time = 0; // re-roll times

    String final_submit_word = ""; // final word to receive score

    ArrayList<String> game_word = new ArrayList<String>();// all the words have been submitted and scored, set 9999 numbers
    int word_submit_number = 0; // the number of words have been submitted
    int game_score = 0; // game score

    int maxNum = 0; // time of word
    String maxElement = ""; // the word

    private AlertDialog.Builder builder; // time out warning dialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        this.setTitle(R.string.Team123);

        Button1 = (Button) findViewById(R.id.re_roll);
        Button2 = (Button) findViewById(R.id.submitword);
        Button3 = (Button) findViewById(R.id.return1);
        Button4 = (Button) findViewById(R.id.re_choice);
        textViewword2 = (TextView) findViewById(R.id.textViewword2);
        Button1.setOnClickListener(new ButtonListener3());
        Button2.setOnClickListener(new ButtonListener3());
        Button3.setOnClickListener(new ButtonListener3());
        Button4.setOnClickListener(new ButtonListener3()); // buttons on listener
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        textViewScore = (TextView) findViewById(R.id.textViewScore);
        textViewScore.setText(String.valueOf(game_score));
        getvalue(); // get the default and initial value from the database
        startgame(); // reset all variables
        setTimer(); // begin game
        chooseletter(); // set letters
        setBoard(); // generate board
    }

        private class ButtonListener3 implements View.OnClickListener { // buttons on the screen
            public void onClick(View v) {
                switch (v.getId())
                {
                    case R.id.return1:
                        timer.cancel();
                        //changevalue();
                        showSimpleDialog2(); // exit dialog
                        break;

                    case R.id.re_roll:
                        chooseletter();
                        //setBoard();
                        for (int row = 0; row < board_size; row++) {
                            for (int col = 0; col < board_size; col++) {
                                if (final_button_letters[row * board_size + col] == 'q')
                                    button[row * board_size + col].setText("qu");
                                else
                                    button[row * board_size + col].setText(String.valueOf(final_button_letters[row * board_size + col]));
                            }
                        } // reset button letters
                        for (int i=0; i<board_size*board_size; i++){
                            button[i].setActivated(false);
                        } // un-choose all the buttons
                        final_submit_word = "";
                        flag = new int[64];
                        button_sequence = new int[128]; // use this to restore clicked buttons sequence, the max size is 8 * 8 * 2 ("q" becomes "qu")= 128
                        sequence = 0; // use this to get the re-clicked button sequence
                        sequence_len = 0; // set sequence length to 0
                        submittedwords = new ArrayList<String>();
                        textViewword2.setText(final_submit_word.toUpperCase());
                        re_roll_time = re_roll_time + 1;// re-roll and re-set the board
                        game_score = game_score - 5; // calculate the score
                        textViewScore.setText(String.valueOf(game_score));
                        break;

                    case R.id.re_choice:
                        for (int i=0; i<board_size*board_size; i++){
                            button[i].setActivated(false);
                        } // un-choose all the buttons
                        final_submit_word = "";
                        flag = new int[64];
                        button_sequence = new int[128]; // use this to restore clicked buttons sequence, the max size is 8 * 8 * 2 ("q" becomes "qu")= 128
                        sequence = 0; // use this to get the re-clicked button sequence
                        sequence_len = 0; // set sequence length to 0
                        textViewword2.setText(final_submit_word.toUpperCase());
                        break;

                    case R.id.submitword:
                        switch (final_submit_word.length())
                        {
                            case 0:
                                showSimpleDialog3(); // null warnings
                                break;
                            case 1:
                                showSimpleDialog3();  // 1 letter warnings
                                break;
                            default:
                                int count = 0;
                                StringBuffer sb = new StringBuffer();
                                for(int i=0;i<button_sequence.length;i++){
                                    sb.append(button_sequence[i]);
                                } // record the last button sequence
                                for (int i = 0; i < submittedwords.size(); i++) {
                                        if (submittedwords.get(i).equals(sb.toString())) {
                                            count = 1;
                                            break;
                                        }
                                    }
                                if( count == 1){ // the word has been genereated by the same location letters
                                    showSimpleDialog5(); // show the warnings
                                }
                                else {
                                    for (int i=0; i<board_size*board_size; i++){
                                        button[i].setActivated(false);
                                        flag[i] = 0;
                                    }
                                    textViewword2.setText("Word submitted successfully"); // show the message
                                    submittedwords.add(sb.toString());
                                    //game_word[word_submit_number] = final_submit_word;
                                    game_word.add(final_submit_word);// store the submitted word
                                    word_submit_number = word_submit_number + 1; // store word time
                                    game_score = game_score + final_submit_word.length(); // calculate the score
                                    button_sequence = new int[128]; // use this to restore clicked buttons sequence, the max size is 8 * 8 * 2 ("q" becomes "qu")= 128
                                    sequence = 0; // use this to get the re-clicked button sequence
                                    sequence_len = 0; // set sequence length to 0
                                    final_submit_word="";
                                }
                                break;
                        }
                        textViewScore.setText(String.valueOf(game_score)); // show the score
                        break;
                }
            }
    }
    // --------------------------Get value part-----------------------
    private void getvalue() { // get value from database
        dbHelper = new DBHelper(game.this, "WordGame.db", null, 1);
        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqliteDatabase.query("BasicInfo", null,  "userid=?", new String[] { "1" }, null, null, null); // all the settings are all ways stored in the first one
        while (cursor.moveToNext()) {
            timeStamp = Integer.parseInt(cursor.getString(cursor.getColumnIndex("gametime"))); // get game end time
            board_size = Integer.parseInt(cursor.getString(cursor.getColumnIndex("boardsize"))); // get board size
            letters = cursor.getString(cursor.getColumnIndex("letters")); // no change
            weights_string = cursor.getString(cursor.getColumnIndex("weights")); // get weights
        }
        sqliteDatabase.close();
        for (int i=0; i<weights_string.length(); i++){
            weights[i] = Integer.parseInt(String.valueOf(weights_string.charAt(i)));
        }
    }
    // --------------------------Get value part end-----------------------

    // --------------------------Output value part-----------------------
    private void changevalue() { // out put game value to the database
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<weights.length;i++){
            sb.append(weights[i]);
        }
        String weightsupdate = sb.toString(); // get weights and transfer to strings
        Gson gson = new Gson();
        String inputString= gson.toJson(game_word); // game word are lists, transfer to Json string
        dbHelper = new DBHelper(game.this, "WordGame.db", null, 1);
        SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("gametime", String.valueOf(timeStamp));
        values.put("boardsize", String.valueOf(board_size));
        values.put("letters", "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        values.put("weights", weightsupdate);
        values.put("gamescore", game_score); // output game score
        values.put("resettime", String.valueOf(re_roll_time)); // output re-roll time
        values.put("numberofwords", String.valueOf(word_submit_number)); // output number of word
        values.put("gameword", inputString);
        sqliteDatabase.insert("BasicInfo", null, values);
        sqliteDatabase.close();
    }
    // --------------------------Output value part end-----------------------

    // --------------------------Timer part-----------------------
    private void setTimer(){ // set the timer counter down

        timer = new CountDownTimer(timeStamp, 1000) { // need to transfer s to ms
            @Override
            public void onTick(long millisUntilFinished) {
                long day = millisUntilFinished / (1000 * 60 * 60 * 24);
                long hour = (millisUntilFinished - day * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                long minute = (millisUntilFinished - day * (1000 * 60 * 60 * 24) - hour * (1000 * 60 * 60)) / (1000 * 60);
                long second = (millisUntilFinished - day * (1000 * 60 * 60 * 24) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000;
                textViewTime.setText(minute + " min " + second + " sec");
            }

            @Override
            public void onFinish() {
                // when time countdown to zero
                //changevalue();
                showSimpleDialog(); // show the warning dialog
            }
        };
        timer.start();
    }
    // --------------------------Timer part end-----------------------

    // --------------------------Choose letter part-----------------------
    private void chooseletter(){
        int number_vowels = (int) Math.ceil((float) board_size * board_size / 5.0); // number of vowels
        int number_consonants = board_size * board_size - number_vowels; // number of consonants
        //a e i o u
        int[] vowels_weights = {weights[0],weights[4],weights[8],weights[14],weights[20]};
        char[] vowels_letters = {'a','e','i','o','u'}; // vowels and their weights
        char[] vowels_choose_letters = new char[number_vowels];
        int totalsum = 0;
        for (int value : vowels_weights) {
            totalsum += value;
        }
        for (int number_vowel = 0; number_vowel < number_vowels; number_vowel++) {
            Random generate = new Random();
            int index = generate.nextInt(totalsum);
            int sum = 0;
            int i=0;
            while(sum < index ) {
                sum = sum + vowels_weights[i]; // use the weights
                i = i+1;
            }
            vowels_choose_letters[number_vowel] = vowels_letters[(Math.max(0,i-1))]; // choose the vowels according to the weights
        }

        // b c d f g h j k l m n p q r s t v w x y z
        int[] consonants_weights = {weights[1],weights[2],weights[3],weights[5],weights[6],weights[7],
                weights[9],weights[10],weights[11],weights[12],weights[13],
                weights[15],weights[16],weights[17],weights[18],weights[19],
                weights[21],weights[22],weights[23],weights[24],weights[25]};
        char[] consonants_letters = {'b','c','d','f','g','h','j','k','l','m','n','p','q','r','s','t','v','w','x','y','z'}; // consonants and their weights
        char[] consonants_choose_letters = new char[number_consonants];
        int totalsum2 = 0;
        for (int value : consonants_weights) {
            totalsum2 += value;
        }
        for (int number_consonant = 0; number_consonant < number_consonants; number_consonant++) {
            Random generate2 = new Random();
            int index = generate2.nextInt(totalsum2);
            int sum = 0;
            int i=0;
            while(sum < index ) {
                sum = sum + consonants_weights[i];
                i = i+1;
            }
            consonants_choose_letters[number_consonant] = consonants_letters[(Math.max(0,i-1))]; // choose the consonants according to the weights
        }

        String chooseletters1 = String.valueOf(vowels_choose_letters);
        String chooseletters2 = String.valueOf(consonants_choose_letters);
        String chooseletters = chooseletters1.concat(chooseletters2); // combine vowels letters and consonants letters
        char[] button_letters = chooseletters.toCharArray();
        Random generate3 = new Random();
        for (int i = 0; i < button_letters.length; i++) {
            int randomIndexToSwap = generate3.nextInt(button_letters.length);
            char temp = button_letters[randomIndexToSwap];
            button_letters[randomIndexToSwap] = button_letters[i];
            button_letters[i] = temp;
        }
        final_button_letters = button_letters; // re-shuffle the letters
    }
    // --------------------------Choose letter part end-----------------------

    // --------------------------Board part-----------------------
    private void setBoard() {
        gridLayout = (GridLayout) findViewById(R.id.grid_layout); // Get GridLayout
        button = new Button[board_size*board_size]; // generate buttons

        for (int row = 0; row < board_size; row++) {
            for (int col = 0; col < board_size; col++) {
                GridLayout.Spec rowSpec = GridLayout.spec(row);
                GridLayout.Spec colSpec = GridLayout.spec(col);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, colSpec); // arrange buttons
                //Drawable drawable = getResources().getDrawable(R.drawable.bg_check_enable_disable,null);
                //drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                button[row * board_size + col] = new Button(this);
                button[row * board_size + col].setId( (row+1) * 10 + col); // set the buttons ids, use this to determine the buttons rows and cols
                button[row * board_size + col].setBackgroundResource(R.drawable.bg_blue_to_cc); // set the background color of buttons: pressed: green; unpressed: gray
                //button[row * board_size + col].setTextColor(mContext.getResources().getColorStateList(R.color.text_color_blue_to_cc,null));
                //btn.setCompoundDrawables(drawable, null, null, null);
                //btn.setCompoundDrawablePadding(7);
                if (final_button_letters[row * board_size + col] == 'q')
                    button[row * board_size + col].setText("qu");
                else
                    button[row * board_size + col].setText(String.valueOf(final_button_letters[row * board_size + col])); // show the letters on the buttons
                WindowManager mWindowManager  = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
                DisplayMetrics metrics = new DisplayMetrics();
                mWindowManager.getDefaultDisplay().getMetrics(metrics);
                params.width = metrics.widthPixels / board_size;
                params.height = ((int)(metrics.heightPixels/1.8)) / board_size ; // get the phone pixels and use that to decide the buttons size
                //params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                //params.width = GridLayout.LayoutParams.WRAP_CONTENT;
                //params.height = dip2px(game.this, 290 / board_size);
                //params.width = dip2px(game.this, 350 / board_size);
                params.rowSpec = GridLayout.spec(row, 1f);
                params.columnSpec = GridLayout.spec(col, 1f); // need to set weight as 1.0 for all the buttons
                params.setGravity(Gravity.CENTER); // generate board
                params.setMargins(5, 5, 5, 5);
                gridLayout.addView(button[row * board_size + col], params);
            }
        }
        for (int k = 0; k <= button.length-1; k++){ // show the letters on the screen textview
            button[k].setActivated(false);
            button[k].setTag(k);
            button[k].setOnClickListener(new Button.OnClickListener() {
                // when press the button, show the letters have been chosen

                @Override
                public void onClick(View v) {
                    int i = (Integer) v.getTag(); // get the button tag, get the button sequences
                    switch(flag[i]){ // clicked button: get letters, re-clicked button: delete letters
                        case 0: // when button clicked
                            //check if the clicked letter is allowed
                            if (sequence_len > 0){
                                int pre = button_sequence[sequence_len-1]; // previous clicked button position
                                if(Math.abs(pre % board_size - i % board_size) > 1 || Math.abs(pre / board_size - i / board_size) > 1) { // no adjacent to the previous letter
                                    Toast.makeText(game.this, "Only adjacent letters can be selected", Toast.LENGTH_SHORT).show(); // get warnings
                                    break;
                                }
                                else {
                                    flag[pre] = 2;
                                }
                            }
                            button_sequence[sequence_len] = i;
                            button[i].setActivated(true);// set the button status as been chosen
                            sequence_len++;
                            flag[i] = 1;
                            final_submit_word = final_submit_word.concat(button[i].getText().toString());
                            break;

                        case 1: // when the recent clicked button re-clicked
                            button[i].setActivated(false); // un-choose the button
                            sequence_len--;
                            flag[i] = 0;
                            button_sequence[sequence_len] = 0;
                            if (button[i].getText().toString()=="qu")
                                final_submit_word = final_submit_word.substring(0,final_submit_word.length()-2);
                            else
                                final_submit_word = final_submit_word.substring(0,final_submit_word.length()-1);
                            if (sequence_len > 0){
                                int pre = button_sequence[sequence_len-1];
                                flag[pre] = 1;
                            }
                            break;

                        case 2:
                            Toast.makeText(game.this, "Only the latest letter can be unclicked", Toast.LENGTH_SHORT).show(); // get warnings
                            break;

                    }
                    textViewword2.setText(final_submit_word.toUpperCase());
                    //textViewword2.setText(button_order);
                    //sequence = 0;
                    //zeronumber = 0;
                }
            });
        }
    }
    // --------------------------Board part end-----------------------

    // --------------------------Change dp to pixel part-----------------------
    private int dip2px(Context context, float dipValue) { // some size value in the function is pixel, need to transfer to dp
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
    }
    // --------------------------Change dp to pixel part end-----------------------

    // --------------------------Time countdown to zero warning part-----------------------
    private void showSimpleDialog() { // when the end time countdown to zero, show a warning dialog
        builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("Game score is: " + Integer.toString(game_score)); // show the game score on screen when time is up
        builder.setMessage(R.string.dialog_message); // show the warning message
        builder.setPositiveButton(R.string.postive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // nothing to do, just go back to the screen
                changevalue();
                Intent intent = new Intent();
                intent.setClass(game.this,MainActivity.class);
                startActivity(intent); // return to the main menu
            }
        });
        builder.setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // restart the game
                changevalue();
                startgame();
                setTimer(); // begin game
                chooseletter(); // set letters
                //setBoard(); // generate board
                for (int row = 0; row < board_size; row++) {
                    for (int col = 0; col < board_size; col++) {
                        if (final_button_letters[row * board_size + col] == 'q')
                            button[row * board_size + col].setText("qu");
                        else
                            button[row * board_size + col].setText(String.valueOf(final_button_letters[row * board_size + col]));
                    }
                }
                for (int ii=0; ii<board_size*board_size; ii++){
                    button[ii].setActivated(false);
                }
                final_submit_word = "";
                flag = new int[64];
                button_sequence = new int[128]; // use this to restore clicked buttons sequence, the max size is 8 * 8 * 2 ("q" becomes "qu")= 128
                sequence = 0; // use this to get the re-clicked button sequence
                sequence_len = 0;
                submittedwords = new ArrayList<String>();
                textViewword2.setText(final_submit_word.toUpperCase());
                textViewScore.setText(String.valueOf(0));
            }
        });
        //set the dialog can be disabled
        builder.setCancelable(true);
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    // --------------------------Time countdown to zero warning part end-----------------------

    // --------------------------Time countdown to zero warning part-----------------------
    private void showSimpleDialog2() { // when the end time countdown to zero, show a warning dialog
        builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("Game score is: " + Integer.toString(game_score)); // show the game score on screen when time is up
        //builder.setMessage(R.string.dialog_message); // show the warning message
        builder.setNeutralButton(R.string.statistics_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                changevalue();
                Intent intent = new Intent();
                intent.setClass(game.this,statistics.class);
                startActivity(intent); // return to the statistics menu
            }
        });
        builder.setPositiveButton(R.string.postive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                changevalue();
                Intent intent = new Intent();
                intent.setClass(game.this,MainActivity.class);
                startActivity(intent); // return to the main menu
            }
        });
        builder.setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // restart the game
                changevalue();
                startgame();
                setTimer(); // begin game
                chooseletter(); // set letters
                //setBoard(); // generate board
                for (int row = 0; row < board_size; row++) {
                    for (int col = 0; col < board_size; col++) {
                        if (final_button_letters[row * board_size + col] == 'q')
                            button[row * board_size + col].setText("qu");
                        else
                            button[row * board_size + col].setText(String.valueOf(final_button_letters[row * board_size + col]));
                    }
                }
                for (int ii=0; ii<board_size*board_size; ii++){
                    button[ii].setActivated(false);
                }
                final_submit_word = "";
                flag = new int[64];
                button_sequence = new int[128]; // use this to restore clicked buttons sequence, the max size is 8 * 8 * 2 ("q" becomes "qu")= 128
                sequence = 0; // use this to get the re-clicked button sequence
                sequence_len = 0;
                submittedwords = new ArrayList<String>();
                textViewword2.setText(final_submit_word.toUpperCase());
                textViewScore.setText(String.valueOf(0));
            }
        });
        //set the dialog can be disabled
        builder.setCancelable(true);
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    // --------------------------Time countdown to zero warning part end-----------------------

    // --------------------------Game start initiation part-----------------------
    private void startgame(){
        game_word = new ArrayList<String>();
        submittedwords = new ArrayList<String>();
        re_roll_time = 0;
        word_submit_number = 0;
        game_score = 0;
        final_submit_word = "";
        maxNum = 0; // time of word
        maxElement = ""; // the word
        flag = new int[64]; // use flag to control buttons, the max size is 8 * 8 = 64
        button_sequence = new int[128]; // use this to restore clicked buttons sequence, the max size is 8 * 8 * 2 ("q" becomes "qu")= 128
        sequence = 0; // use this to get the re-clicked button sequence
        textViewword2.setText(final_submit_word.toUpperCase()); // reset all the variables
    }
    // --------------------------Game start initiation part end-----------------------

    // --------------------------Find the most showed word part-----------------------
    //private void findword(){ // use hash map to find
    //    Map<String,Integer> map = new HashMap();
    //    for (int i=0; i<word_submit_number;i++){
    //        if(map.containsKey(game_word[i])){
    //            int value = map.get(game_word[i]);
    //            map.put(game_word[i],value+1);
    //    }else {
    //       map.put(game_word[i],1); // generate the hashmap
    //    }
    //}
    //
    //    for(Map.Entry<String, Integer> entry : map.entrySet()){
    //    String key = entry.getKey();
    //    int value = entry.getValue();
    //    if(value > maxNum){
    //        maxNum = value;
    //        maxElement = key; // find the most showed word and its shown time
    //    }
    //    }
    //}
    // --------------------------Find the most showed word part end-----------------------

    private void showSimpleDialog3() { // null and one letter warnings
        builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("Can Not Submit Word"); //
        builder.setMessage("Word must contains at least two letters"); // show the warning message
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

    private void showSimpleDialog4() { // change to use a toast to show the no-adjacent warning
        builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("Can Not Submit Word"); //
        builder.setMessage("Word must contains letters that are adjacent to each other"); // show the warning message
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

    private void showSimpleDialog5() { // same word warning
        builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("Can Not Submit Word"); //
        builder.setMessage("The same word generated by these letters has been submitted"); // show the warning message
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

}
