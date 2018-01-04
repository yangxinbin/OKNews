package com.baihui.yangxb.startpage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baihui.yangxb.R;
import com.baihui.yangxb.startapp.BubbleStartActivity;
import com.baihui.yangxb.startpage.utils.Titanic;
import com.baihui.yangxb.startpage.utils.TitanicTextView;
import com.baihui.yangxb.startpage.utils.Typefaces;

import java.util.Timer;
import java.util.TimerTask;

public class StartPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        TitanicTextView tv = (TitanicTextView) findViewById(R.id.startpage);
        // set fancy typeface
        tv.setTypeface(Typefaces.get(this, "Satisfy-Regular.ttf"));
        // start animation
        new Titanic().start(tv);
        Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                Intent intent1=new Intent(StartPage.this,BubbleStartActivity.class);
                startActivity(intent1);
                StartPage.this.finish();
            }
        };
        timer.schedule(timerTask,1000*3);
    }
}
