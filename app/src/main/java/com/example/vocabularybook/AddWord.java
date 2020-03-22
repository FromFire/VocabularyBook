package com.example.vocabularybook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class AddWord extends AppCompatActivity {

    private LinearLayout pageLinear;    //展示所有内容的linear layout
    private int meaning = 1;            //页面上meaning的数量
    private int sentence = 1;           //页面上sentence的数量
    private int meaningAddIndex;        //新增meaning应插入的位置
    private int sentenceAddIndex;       //新增sentence应插入的位置


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

        pageLinear = findViewById(R.id.ParentLayout);

        //初始化meaningAddIndex和sentenceAddIndex
        int size = pageLinear.getChildCount();
        for(int i=0; i<size; i++)
            if (pageLinear.getChildAt(i).getId() == R.id.addMeaning) {
                meaningAddIndex = i;
                break;
            }
        for(int i=meaningAddIndex; i<size; i++)
            if (pageLinear.getChildAt(i).getId() == R.id.addSentence) {
                sentenceAddIndex = i;
                break;
            }
        Log.i("init", ""+meaningAddIndex);
        Log.i("init", ""+sentenceAddIndex);
    }

    /**
     * 点击函数："addMeaning" meaning区域下方的绿色加号小按钮
     * 添加一个新的空白释义
     *
     * @param v 用不到
     */
    public void addMeaning(View v) {
        //创建输入meaning的EditText
        EditText edit = getInputEditText();
        edit.setTextSize(18);

        //创建用于删除该meaning的减号小按钮，点击函数即deleteMeaning
        ImageButton button = getDelButton();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMeaning(v);
            }
        });

        //创建容纳上述控件的Constraint Layout
        ConstraintLayout layout = new ConstraintLayout(this);
        layout.setId(View.generateViewId());
        layout.addView(edit);
        layout.addView(button);

        //设置Constraint Layout的布局，参照layout中meaning_sample的布局
        int editId = edit.getId();
        int buttonId = button.getId();
        ConstraintSet set = new ConstraintSet();
        set.clone(layout);
        //设置输入框位于布局左侧
        set.connect(editId, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        set.connect(editId, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        set.connect(editId, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        //设置按钮位于布局右侧
        set.connect(buttonId, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        set.connect(buttonId, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        set.connect(buttonId, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        //设置输入框位于按钮左侧
        set.connect(editId, ConstraintSet.END, buttonId, ConstraintSet.START);
        //应用布局
        set.applyTo(layout);

        //设置layout上间距
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        param.setMargins(0, 20, 0, 0);
        layout.setLayoutParams(param);

        //插入和维护Linear Layout
        pageLinear.addView(layout, meaningAddIndex);
        meaningAddIndex++;
        meaning++;
        sentenceAddIndex++;
    }

    /**
     * 点击函数："meaning_delete"和手动添加的释义右侧的按钮，主题色减号小按钮
     * 检查要删除的这个释义是否是唯一一个释义，如果是，什么都不做
     * 如果不是，删除这个释义
     *
     * @param v
     */
    public void deleteMeaning(View v) {
        if(meaning == 1)
            return;
        View toDel = (View)v.getParent();
        for(int i=0; i<pageLinear.getChildCount(); i++) {
            if(pageLinear.getChildAt(i).getId() == toDel.getId())
                pageLinear.removeViewAt(i);
        }

        //维护成员函数
        meaning--;
        meaningAddIndex--;
        sentenceAddIndex--;
    }

    /**
     * 点击函数："addSentence" sentence区域下方的绿色加号小按钮
     * 添加一组新的空白例句
     *
     * @param v 用于定位新空白例句插入到LinearLayout哪个位置
     */
    public void addSentence(View v) {
        //创建输入text和translation的EditText
        EditText text = getInputEditText();
        EditText trans = getInputEditText();

        //创建用于删除该sentence的减号小按钮,点击函数即deleteSentence
        ImageButton button = getDelButton();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSentence(v);
            }
        });

        //创建容纳上述控件的Constraint Layout
        ConstraintLayout layout = new ConstraintLayout(this);
        layout.setId(View.generateViewId());
        layout.addView(text);
        layout.addView(trans);
        layout.addView(button);

        //设置Constraint Layout的布局，参照layout中meaning_sample的布局
        int textId = text.getId();
        int transId = trans.getId();
        int buttonId = button.getId();
        ConstraintSet set = new ConstraintSet();
        set.clone(layout);
        //设置text输入框位于布局左上角
        set.connect(textId, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        set.connect(textId, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        //设置trans输入框位于text正下方，即左下角
        set.connect(transId, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        set.connect(transId, ConstraintSet.TOP, textId, ConstraintSet.BOTTOM);
        //设置按钮位于布局右侧
        set.connect(buttonId, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        set.connect(buttonId, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        set.connect(buttonId, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        //设置两个输入框位于按钮左侧
        set.connect(textId, ConstraintSet.END, buttonId, ConstraintSet.START);
        set.connect(transId, ConstraintSet.END, buttonId, ConstraintSet.START);
        //应用布局
        set.applyTo(layout);

        //设置layout上间距
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        param.setMargins(0, 20, 0, 0);
        layout.setLayoutParams(param);

        //插入和维护Linear Layout
        pageLinear.addView(layout, sentenceAddIndex);
        sentenceAddIndex++;
        sentence++;
    }

    /**
     * 点击函数："sentence_delete"和手动添加的释义右侧的按钮，主题色减号小按钮
     * 删除这组例句
     *
     * @param v
     */
    public void deleteSentence(View v) {
        View toDel = (View)v.getParent();
        for(int i=0; i<pageLinear.getChildCount(); i++) {
            if(pageLinear.getChildAt(i).getId() == toDel.getId())
                pageLinear.removeViewAt(i);
        }

        //维护成员函数
        sentence--;
        sentenceAddIndex--;
    }

    /**
     * 点击函数："add" 页面最下方的大按钮
     * 检查新单词的合法性，若不合法，提示输入不合法
     * 若合法，将新增的单词回传给MainActivity
     *
     * @param v 用不到
     */
    public void commitAdd(View v) {

    }

    /**
     * 生成一个可多行输入，宽度为0的EditText，并为其分配id
     * 用于meaning和sentence的输入
     *
     * @return 生成的EditText
     */
    public EditText getInputEditText() {
        EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        editText.setSingleLine(false);
        LinearLayout.LayoutParams paramEdit = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(paramEdit);
        editText.setId(View.generateViewId());
        return editText;
    }

    /**
     * 生成一个主题色的减号小按钮，并为其分配id
     * 用于删除meaning和sentence输入框
     *
     * @return 生成的ImageButton
     */
    public ImageButton getDelButton() {
        ImageButton button = new ImageButton(this);
        button.setBackground(getDrawable(R.drawable.round_button));
        button.setImageResource(R.mipmap.icon_del);
        button.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout.LayoutParams paramButton = new LinearLayout.LayoutParams(80, 80);
        button.setLayoutParams(paramButton);
        button.setId(View.generateViewId());
        return button;
    }
}
