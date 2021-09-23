package edu.gatech.seclass.wordfind6300;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;


public class statistics extends AppCompatActivity {
    private Button Button1;
    private Button Button2;
    private Button Button3; // only need to show the buttons

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);
        this.setTitle(R.string.Team123);
        Button1  = (Button) findViewById(R.id.return_score_statistics);
        Button2  = (Button) findViewById(R.id.return_word_statistics);
        Button3  = (Button) findViewById(R.id.return1);
        Button1.setOnClickListener(new ButtonListener());
        Button2.setOnClickListener(new ButtonListener());
        Button3.setOnClickListener(new ButtonListener());
    }

    private class ButtonListener implements OnClickListener{
        public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.return_score_statistics:
                Intent intent = new Intent();
                intent.setClass(statistics.this,score_statistics.class);
                startActivity(intent); // see the score statistics
                break;

                case R.id.return_word_statistics:
                    Intent intent2 = new Intent();
                    intent2.setClass(statistics.this,word_statistics.class);
                    startActivity(intent2); // see the word statistics
                    break;

                    case R.id.return1:
                        Intent intent3 = new Intent();
                        intent3.setClass(statistics.this,MainActivity.class);
                        startActivity(intent3); // return to main menu
                        break;

                        default:
                            break;
        }
        }
    }

}
