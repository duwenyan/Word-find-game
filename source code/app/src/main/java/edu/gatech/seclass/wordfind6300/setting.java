package edu.gatech.seclass.wordfind6300;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class setting extends AppCompatActivity {

    private DBHelper dbHelper;

    private Button Button1;
    private Button Button2;
    private Button Button3;

    private TextView textview02;
    private TextView textview04; // buttons and textviews

    private Spinner spinner1;
    private List<String> dataList1;
    private ArrayAdapter<String> adapter1;
    String Case_Game_End_Time = "3 min"; // setting the game end time

    private Spinner spinner2;
    private List<String> dataList2;
    private ArrayAdapter<String> adapter2;
    String Case_Board_Size = "4 * 4"; // setting the board size

    private Spinner spinner3;
    private List<String> dataList3;
    private ArrayAdapter<String> adapter3;
    String Case_Letter = ""; // setting the letters weight

    private Spinner spinner4;
    private List<String> dataList4;
    private ArrayAdapter<String> adapter4;
    String Case_Letter_weights = ""; // setting the letters weight

    int board_size = 4;
    long timeStamp = 180000; // initial value

    String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // letters and their weight
    int[] weights = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};

    private GridLayout grid_table; //set the table to show the setting result
    TextView[] textview; // textviews in the table


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        this.setTitle(R.string.Team123);

        setTable(); // generate the table to show the setting result
        Button1 = (Button) findViewById(R.id.buttonsubmitsetting);
        Button2 = (Button) findViewById(R.id.buttonrestsetting);
        Button3 = (Button) findViewById(R.id.return1);

        Button1.setOnClickListener(new ButtonListener4());
        Button2.setOnClickListener(new ButtonListener4());
        Button3.setOnClickListener(new ButtonListener4()); // buttons on listener

        textview02 = (TextView) findViewById(R.id.textView02);
        textview04 = (TextView) findViewById(R.id.textView04);

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        dataList1 = new ArrayList<String>();
        dataList1.add("1 min");
        dataList1.add("2 min");
        dataList1.add("3 min");
        dataList1.add("4 min");
        dataList1.add("5 min"); // spinner to set game time

        adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,dataList1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setSelection(2,true);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Case_Game_End_Time = parent.getItemAtPosition(position).toString(); // get the game end time from spinner
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        dataList2 = new ArrayList<String>();
        dataList2.add("4 * 4");
        dataList2.add("5 * 5");
        dataList2.add("6 * 6");
        dataList2.add("7 * 7");
        dataList2.add("8 * 8"); // spinner to set the board size

        adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,dataList2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Case_Board_Size = parent.getItemAtPosition(position).toString(); // get the board size from spinner
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });

        spinner3 = (Spinner) findViewById(R.id.spinner3);
        dataList3 = new ArrayList<String>();
        dataList3.add("a");dataList3.add("b");dataList3.add("c");dataList3.add("d");dataList3.add("e");dataList3.add("f");
        dataList3.add("g");dataList3.add("h");dataList3.add("i");dataList3.add("j");dataList3.add("k");dataList3.add("l");
        dataList3.add("m");dataList3.add("n");dataList3.add("o");dataList3.add("p");dataList3.add("q");dataList3.add("r");
        dataList3.add("s");dataList3.add("t");dataList3.add("u");dataList3.add("v");dataList3.add("w");dataList3.add("x");
        dataList3.add("y");dataList3.add("z"); // spinner to set the letters

        adapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,dataList3);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Case_Letter = parent.getItemAtPosition(position).toString(); // get the letters from spinner
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });

        spinner4 = (Spinner) findViewById(R.id.spinner4);
        dataList4 = new ArrayList<String>();
        dataList4.add("1");
        dataList4.add("2");
        dataList4.add("3");
        dataList4.add("4");
        dataList4.add("5"); // spinner to set the letters weights

        adapter4 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,dataList4);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(adapter4);
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Case_Letter_weights = parent.getItemAtPosition(position).toString(); // get the weights from spinner
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });

    }

    private class ButtonListener4 implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.return1:
                    //timer.cancel();
                    Intent intent = new Intent();
                    intent.setClass(setting.this,MainActivity.class);
                    startActivity(intent); // return to the main menu
                    break;

                case R.id.buttonsubmitsetting:
                    setsize();
                    settime();
                    setweight();
                    textview02.setText(Case_Game_End_Time);
                    textview04.setText(Case_Board_Size);
                    for (int row = 0; row < 4; row++) {
                        for (int col = 0; col < 13; col++) {
                            if (row == 0) {
                                textview[row * 13 + col].setText(String.valueOf(letters.charAt(row * 13 + col)));
                            } // show the letters on the buttons
                            else if (row == 1) {
                                textview[row * 13 + col].setText(String.valueOf(weights[(row-1) * 13 + col]));
                            }
                            else if (row == 2) {
                                textview[row * 13 + col].setText(String.valueOf(letters.charAt((row-1) * 13 + col)));
                            }
                            else {
                                textview[row * 13 + col].setText(String.valueOf(weights[(row-2) * 13 + col])); // set letters and weights
                            }
                        }
                    }
                    changevalue(); // update the database, need to transfer these setting values to game
                    break;

                case R.id.buttonrestsetting:
                    for (int i = 0; i < weights.length; i++) {
                        weights[i] = 1;
                    } // reset weights
                    board_size = 4; // reset board size
                    timeStamp = 180000; // reset game time
                    //resetTable();
                    textview02.setText("3 min");
                    textview04.setText("4 * 4");
                    //setTable();
                    for (int row = 0; row < 4; row++) {
                        for (int col = 0; col < 13; col++) {
                            if (row == 0) {
                                textview[row * 13 + col].setText(String.valueOf(letters.charAt(row * 13 + col)));
                            } // show the letters on the buttons
                            else if (row == 1) {
                                textview[row * 13 + col].setText(String.valueOf(weights[(row-1) * 13 + col]));
                            }
                            else if (row == 2) {
                                textview[row * 13 + col].setText(String.valueOf(letters.charAt((row-1) * 13 + col)));
                            }
                            else {
                                textview[row * 13 + col].setText(String.valueOf(weights[(row-2) * 13 + col])); // set letters and weights
                            }
                        }
                    }
                    changevalue(); // update the database
                    break;
            }
        }
    }
    // --------------------------Update the database part-----------------------
    private void changevalue() {
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<weights.length;i++){
            sb.append(weights[i]);
        }
        String weightsupdate = sb.toString(); // get weights and transfer to strings
        dbHelper = new DBHelper(setting.this, "WordGame.db", null, 1);
        SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("gametime", String.valueOf(timeStamp));
        values.put("boardsize", String.valueOf(board_size));
        values.put("letters", "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        values.put("weights", weightsupdate); // update the information
        sqliteDatabase.update("BasicInfo", values, "userid=?", new String[] { "1" }); // only change the first one
        sqliteDatabase.close();
    }
    // --------------------------Update the database part end-----------------------

    // --------------------------Set board size part-----------------------
    private void setsize(){
            switch (Case_Board_Size) {
        case("4 * 4") :
            board_size = 4;
            break;
        case("5 * 5") :
            board_size = 5;
            break;
        case("6 * 6"):
            board_size = 6;
            break;
        case("7 * 7") :
            board_size = 7;
            break;
        case("8 * 8") :
            board_size = 8;
            break;
            } // set the board size according to the choice
    }
    // --------------------------Set board size part end-----------------------

    // --------------------------Set game time part-----------------------
    private void settime(){
        switch (Case_Game_End_Time) {
        case("1 min") :
            timeStamp = 60000;
            break;
        case("2 min"):
            timeStamp = 120000;
            break;
        case("3 min") :
            timeStamp = 180000;
            break;
        case("4 min") :
            timeStamp = 240000;
            break;
        case("5 min") :
            timeStamp = 300000;
            break;
            } // set the game time according to the choice
    }
    // --------------------------Set game time part end-----------------------

    // --------------------------Set letters weight part-----------------------
    private void setweight(){
        switch (Case_Letter) {
            case ("a") :
                weights[0] = Integer.parseInt(Case_Letter_weights);
                break;
            case ("b") :
                weights[1] = Integer.parseInt(Case_Letter_weights);
                break;
            case ("c") :
                weights[2] = Integer.parseInt(Case_Letter_weights);
                break;
            case ("d") :
                weights[3] = Integer.parseInt(Case_Letter_weights);
                break;
            case ("e") :
                weights[4] = Integer.parseInt(Case_Letter_weights);
                break;
            case ("f") :
                weights[5] = Integer.parseInt(Case_Letter_weights);
                break;
            case ("g") :
                weights[6] = Integer.parseInt(Case_Letter_weights);
                break;
            case ("h") :
                weights[7] = Integer.parseInt(Case_Letter_weights);
                break;
            case ("i") :
                weights[8] = Integer.parseInt(Case_Letter_weights);
                break;
            case ("j") :
                weights[9] = Integer.parseInt(Case_Letter_weights);
                break;
            case ("k") :
                weights[10] = Integer.parseInt(Case_Letter_weights);
                break;
            case ("l") :
                weights[11] = Integer.parseInt(Case_Letter_weights);
                break;
            case ("m") :
                weights[12] = Integer.parseInt(Case_Letter_weights);
                break;
            case ("n") :
                weights[13] = Integer.parseInt(Case_Letter_weights);
                break;
            case ("o") :
                weights[14] = Integer.parseInt(Case_Letter_weights);
                break;
            case ("p") :
                weights[15] = Integer.parseInt(Case_Letter_weights);
                break;
            case ("q") :
                weights[16] = Integer.parseInt(Case_Letter_weights);
                break;
            case ("r") :
                weights[17] = Integer.parseInt(Case_Letter_weights);
                break;
            case ("s") :
                weights[18] = Integer.parseInt(Case_Letter_weights);
                break;
            case ("t") :
                weights[19] = Integer.parseInt(Case_Letter_weights);
                break;
            case ("u") :
                weights[20] = Integer.parseInt(Case_Letter_weights);
                break;
            case ("v") :
                weights[21] = Integer.parseInt(Case_Letter_weights);
                break;
            case ("w") :
                weights[22] = Integer.parseInt(Case_Letter_weights);
                break;
            case ("x") :
                weights[23] = Integer.parseInt(Case_Letter_weights);
                break;
            case ("y") :
                weights[24] = Integer.parseInt(Case_Letter_weights);
                break;
            case ("z") :
                weights[25] = Integer.parseInt(Case_Letter_weights);
                break;
        }
    }
    // --------------------------Set letters weight part end-----------------------

    // --------------------------Table part-----------------------
    private void setTable() { // set the table to show the setting result

        grid_table = (GridLayout) findViewById(R.id.grid_table); // Get GridLayout
        //grid_table.setLayoutParams(new GridLayout.LayoutParams(new LinearLayout.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT)));

        textview = new TextView[4 * 13]; // generate textviews
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 13; col++) {
                GridLayout.Spec rowSpec = GridLayout.spec(row);
                GridLayout.Spec colSpec = GridLayout.spec(col);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, colSpec); // arrange buttons
                //Drawable drawable = getResources().getDrawable(R.drawable.bg_check_enable_disable,null);
                //drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                textview[row * 13 + col] = new TextView(this);
                textview[row * 13 + col].setId(2000 + row * 13 + col); // set the buttons ids
                if (row == 0) {
                textview[row * 13 + col].setText(String.valueOf(letters.charAt(row * 13 + col)));
                } // show the letters on the buttons
                else if (row == 1) {
                    textview[row * 13 + col].setText(String.valueOf(weights[(row-1) * 13 + col]));
                }
                else if (row == 2) {
                    textview[row * 13 + col].setText(String.valueOf(letters.charAt((row-1) * 13 + col)));
                }
                else {
                    textview[row * 13 + col].setText(String.valueOf(weights[(row-2) * 13 + col])); // set letters and weights
                }
                WindowManager mWindowManager  = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
                DisplayMetrics metrics = new DisplayMetrics();
                mWindowManager.getDefaultDisplay().getMetrics(metrics); // get the phone's pixels to decide the table size
                params.width =  metrics.widthPixels / 13;
                params.height = (int)(metrics.heightPixels / 6.058) / 4 ;
                //params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                //params.width = GridLayout.LayoutParams.WRAP_CONTENT;
                //params.height = dip2px(setting.this, 90 / 4);
                //params.width = dip2px(setting.this, 350 / 13);
                params.rowSpec = GridLayout.spec(row, 1f);
                params.columnSpec = GridLayout.spec(col, 1f);
                params.setGravity(Gravity.CENTER); // generate board
                params.setMargins(1, 1, 1, 1);
                grid_table.addView(textview[row * 13 + col], params);
            }
        }
    }
    // --------------------------Table part end-----------------------

    // --------------------------Reset table part-----------------------
    //private void resetTable() {

        //for (int i = 0; i < textview.length; i++) {
                //textview[i].setText("");
        //}
    //} // reset the table shown result
    // --------------------------Reset table part end-----------------------

    // --------------------------Change dp to pixel part-----------------------
    private int dip2px(Context context, float dipValue) { // some size value in the function is pixel, need to transfer to dp
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
    }
    // --------------------------Change dp to pixel part end-----------------------

}
