package com.example.vocabularybook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView wordView;      //显示单词的列表
    List<Vocabulary> list;  //所有显示单词所在列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wordView = findViewById(R.id.WordList);

//        Vocabulary voc1 = new Vocabulary("feeble", "adj. 虚弱的");
//        voc1.addSentence("I am feeble.", "我很虚弱。");
//        voc1.addMeaning("adj. 无力的");
//        voc1.addSentence("He is feeble.", "他很虚弱。");
//
//        Vocabulary voc2 = new Vocabulary("apple", "n. 苹果");
//        voc2.addSentence("I like apple.", "我喜欢苹果。");
//
//        Vocabulary voc3 = new Vocabulary("figure", "n. (代表数量，尤指官方资料中的)数字;数字符号;字码;位数;算术");
//        voc3.addMeaning("v. 是重要部分;是…的部分;认为，认定(某事将发生或属实);计算(数量或成本)");
//        voc3.addSentence("It would be very nice if we had a true figure of how many people in this country haven't got a job.",
//                "如果我们能得到该国失业人口的真实数字就好了。");
//        voc3.addSentence("Alistair saw the dim figure of Rose in the chair.",
//                "阿利斯泰尔看见了坐在椅子里的罗丝的模糊身影。");

        DBHelper dbHelper = new DBHelper(this);
//        dbHelper.removeAllVocabulary();
//        dbHelper.addVocabulary(voc1);
//        dbHelper.addVocabulary(voc2);
//        dbHelper.addVocabulary(voc3);
        list = dbHelper.queryVocabulary();
        VocabularyAdapter vocabularyAdapter = new VocabularyAdapter(this, R.layout.wordlist_item, list);
        wordView.setAdapter(vocabularyAdapter);
        ((TextView)findViewById(R.id.NumberOfWords)).setText(list.size() + " WORDS");

        wordView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * 单词列表某项的点击函数，点击时跳转到该单词的显示界面
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ViewWord.class);
                intent.putExtra("voc", list.get(position));
                startActivity(intent);
            }
        });
    }
}
