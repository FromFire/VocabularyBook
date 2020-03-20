package com.example.vocabularybook;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    final private String TAG = "DBHelper";

    //对应Vocabulary类的三个表的表名
    final private String TABLE_VOCABULARY = "Vocabulary";
    final private String TABLE_MEANING = "Meaning";
    final private String TABLE_SENTENCE = "Sentence";

    //对应Vocabulary类的四个属性的列名
    final private String COLUMN_WORD = "word";
    final private String COLUMN_MEANING = "meaning";
    final private String COLUMN_SENTENCE_TEXT = "sentenceText";
    final private String COLUMN_SENTENCE_TRANSLATION = "sentenceTranslation";

    DBHelper(@Nullable Context context) {
        super(context, "db_peopleInfo", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //词汇表，仅存储单词
        db.execSQL("Create table Vocabulary(word text primary key)");
        //词汇-释义表（1-n）
        db.execSQL("Create table Meaning(" +
                "word text references Vocabulary(word) not null," +
                "meaning text not null)");
        //词汇-例句表（1-n，其中例句的原文和翻译一对一）
        db.execSQL("Create table Sentence(" +
                "word text references Vocabulary(word) not null," +
                "sentenceText text not null," +
                "sentenceTranslation text not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 向表中新添一条词汇
     *
     * @param voc 新增的词汇
     */
    public void addVocabulary(Vocabulary voc) {
        SQLiteDatabase db = null;
        boolean insertSuccessful = true;
        try {
            //批量插入，速度更快
            db = getWritableDatabase();
            db.beginTransaction();

            //插入到Vocabulary表
            String vocWord = voc.getWord();
            ContentValues cv_word = new ContentValues();
            cv_word.put(COLUMN_WORD, vocWord);
            insertSuccessful = (db.insert(TABLE_VOCABULARY, null, cv_word) >= 0);
            if(insertSuccessful)
                Log.i(TAG, "name insert successful");
            else
                throw new Exception();

            //插入到Meaning表
            List<String> meaningList = voc.getMeaning();
            for(int i=0; i<meaningList.size(); i++) {
                ContentValues cv_mean = new ContentValues();
                cv_mean.put(COLUMN_WORD, vocWord);
                cv_mean.put(COLUMN_MEANING, meaningList.get(i));
                insertSuccessful = (db.insert(TABLE_MEANING, null, cv_mean) >= 0);
                if(!insertSuccessful)
                    break;
            }
            if(insertSuccessful)
                Log.i(TAG, "meaning insert successful");
            else
                throw new Exception();

            //插入到Sentence表
            List<String> textList = voc.getSentence_text();
            List<String> transList = voc.getSentence_translation();
            for(int i=0; i<textList.size(); i++) {
                ContentValues cv_sen = new ContentValues();
                cv_sen.put(COLUMN_WORD, vocWord);
                cv_sen.put(COLUMN_SENTENCE_TEXT, textList.get(i));
                cv_sen.put(COLUMN_SENTENCE_TRANSLATION, transList.get(i));
                insertSuccessful = (db.insert(TABLE_SENTENCE, null, cv_sen) >= 0);
                if(!insertSuccessful)
                    break;
            }
            if(insertSuccessful)
                Log.i(TAG, "sentence insert successful");
            else
                throw new Exception();

            //插入成功才写入数据库
            if(insertSuccessful)
                db.setTransactionSuccessful();

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }
    
}
