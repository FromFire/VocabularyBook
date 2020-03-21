package com.example.vocabularybook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import static android.view.View.GONE;

public class ViewWord extends AppCompatActivity {

    LinearLayout wordShow; //用于显示单词的layout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_word);
        wordShow = findViewById(R.id.wordShow);

        //设置word显示
        Intent intent = getIntent();
        Vocabulary voc = (Vocabulary) intent.getSerializableExtra("voc");
        ((TextView)findViewById(R.id.word)).setText(voc.getWord());

        //设置meaning显示
        findViewById(R.id.meaning_sample).setVisibility(GONE);
        List<String> meaning = voc.getMeaning();
        for(int i=0; i<meaning.size(); i++) {
            TextView tv = new TextView(this);

            //设置居中，上间距
            LinearLayout.LayoutParams param = new
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            param.topMargin = 30;
            param.gravity = Gravity.CENTER;
            tv.setLayoutParams(param);

            tv.setText(meaning.get(i));
            tv.setTextSize(20);
            wordShow.addView(tv, i+1);
        }

        //设置sentence显示
        List<String> text = voc.getSentence_text();
        List<String> trans = voc.getSentence_translation();
        findViewById(R.id.text_sample).setVisibility(GONE);
        findViewById(R.id.translation_sample).setVisibility(GONE);
        if(text.size() != 0) {
            findViewById(R.id.NoSentence).setVisibility(GONE);
            for(int i=0; i<text.size(); i++) {
                TextView tv_text = new TextView(this);
                TextView tv_trans = new TextView(this);

                //设置translation上下间距
                LinearLayout.LayoutParams param = new
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                param.topMargin = 10;
                param.bottomMargin = 50;
                tv_trans.setLayoutParams(param);

                tv_text.setText(text.get(i));
                tv_trans.setText(trans.get(i));
                tv_text.setTextSize(18);
                tv_trans.setTextSize(18);

                wordShow.addView(tv_text);
                wordShow.addView(tv_trans);
            }
        }
    }
}
