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

    final private int REQUEST_ADDWORD = 1; //requestCode，表示新增单词

    ListView wordView;      //显示单词的列表
    List<Vocabulary> list;  //所有显示单词所在列表
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wordView = findViewById(R.id.WordList);
        dbHelper = new DBHelper(this);

        setListView();
    }

    /**
     * 查找数据库中所有单词并将其显示到ListView
     * 为ListView中项目添加点击函数，点击时跳转至详情页
     */
    private void setListView() {
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

    /**
     * addView按钮的点击函数，效果是跳转到新增单词页面
     *
     * @param v 用不到
     */
    public void addWord(View v) {
        Intent intent = new Intent(this, AddWord.class);
        startActivityForResult(intent, REQUEST_ADDWORD);
    }

    /**
     * 回调方法
     * 若从addView回调，将新增的单词其添加到数据库中
     *
     * @param requestCode 回调识别码
     * @param ResultCode 返回码
     * @param data 回调的intent
     */
    @Override
    protected void onActivityResult(int requestCode, int ResultCode, Intent data) {
        super.onActivityResult(requestCode, ResultCode, data);
        switch (requestCode) {
            case REQUEST_ADDWORD:
                Vocabulary voc = (Vocabulary) data.getSerializableExtra("voc");
                dbHelper.addVocabulary(voc);
        }
    }

    /**
     * 初始化database，并添加一些样例数据
     */
    private void setDatabase() {
        Vocabulary voc1 = new Vocabulary("feeble", "adj. 虚弱的");
        voc1.addSentence("I am feeble.", "我很虚弱。");
        voc1.addMeaning("adj. 无力的");
        voc1.addSentence("He is feeble.", "他很虚弱。");

        Vocabulary voc2 = new Vocabulary("apple", "n. 苹果");
        voc2.addSentence("I like apple.", "我喜欢苹果。");

        Vocabulary voc3 = new Vocabulary("figure", "n. (代表数量，尤指官方资料中的)数字;数字符号;字码;位数;算术");
        voc3.addMeaning("v. 是重要部分;是…的部分;认为，认定(某事将发生或属实);计算(数量或成本)");
        voc3.addSentence("It would be very nice if we had a true figure of how many people in this country haven't got a job.",
                "如果我们能得到该国失业人口的真实数字就好了。");
        voc3.addSentence("Alistair saw the dim figure of Rose in the chair.",
                "阿利斯泰尔看见了坐在椅子里的罗丝的模糊身影。");

        dbHelper.removeAllVocabulary();
        dbHelper.addVocabulary(voc1);
        dbHelper.addVocabulary(voc2);
        dbHelper.addVocabulary(voc3);
    }
}
