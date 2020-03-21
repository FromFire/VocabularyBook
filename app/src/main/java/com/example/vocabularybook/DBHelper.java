package com.example.vocabularybook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @return 添加词汇是否成功
     */
    public Boolean addVocabulary(Vocabulary voc) {
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

            //写入数据库
            db.setTransactionSuccessful();
            Log.i(TAG, "vocabulary insert successful");
            return true;

        } catch(Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    /**
     * 更新表中一条词汇
     * 这里有个问题，如果删除成功了而插入没成功怎么办？但似乎也不能在这里也加一个transaction。。。。
     *
     * @param oldWord 要更新的词汇的word值
     * @param voc 该词汇应当更新成的值
     */
    public Boolean updateVocabulary(String oldWord, Vocabulary voc) {
        //若用户修改了word值，则查找新word值是否已在数据库中，如果是，则驳回请求
        SQLiteDatabase db = getWritableDatabase();
        if(!oldWord.equals(voc.getWord())) {
            Cursor cursor = db.query(TABLE_VOCABULARY, null, COLUMN_WORD + "=?",
                    new String[]{voc.getWord()}, null, null, null, null);
            if(cursor.getCount() != 0) {
                Log.i(TAG, "update failed! word exists");
                return false;
            }
        }

        //删除三个表中该单词所有信息
        removeVocabulary(new String[]{oldWord});

        //再将更新后的单词信息存入表中
        addVocabulary(voc);
        return true;
    }

    /**
     * 从表中删除若干词汇
     *
     * @param words 要删除的词汇列表（用词汇的word值来表示）
     */
    public void removeVocabulary(String[] words) {
        SQLiteDatabase db = getWritableDatabase();
        int id = db.delete(TABLE_MEANING, COLUMN_WORD + "=?", words);
        Log.i(TAG, id + "items removed from " + TABLE_MEANING);
        id = db.delete(TABLE_SENTENCE, COLUMN_WORD + "=?", words);
        Log.i(TAG, id + "items removed from " + TABLE_SENTENCE);
        id = db.delete(TABLE_VOCABULARY, COLUMN_WORD + "=?", words);
        Log.i(TAG, id + "items removed from " + TABLE_VOCABULARY);
        db.close();
    }

    /**
     * 删除表中所有词汇
     */
    public void removeAllVocabulary() {
        SQLiteDatabase db = getWritableDatabase();
        int id = db.delete(TABLE_MEANING, null, null);
        Log.i(TAG, id + "items removed from " + TABLE_MEANING);
        id = db.delete(TABLE_SENTENCE, null, null);
        Log.i(TAG, id + "items removed from " + TABLE_SENTENCE);
        id = db.delete(TABLE_VOCABULARY, null, null);
        Log.i(TAG, id + "items removed from " + TABLE_VOCABULARY);
        db.close();
    }

    /**
     * 查询表中所有词汇
     *
     * @return 查询到的词汇
     */
    public List<Vocabulary> queryVocabulary() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cs_word = db.query(TABLE_VOCABULARY, null, null,
                null, null, null, null, null);
        Cursor cs_mean = db.query(TABLE_MEANING, null, null,
                null, null, null, null, null);
        Cursor cs_sen = db.query(TABLE_SENTENCE, null, null,
                null, null, null, null, null);
        return CursorToVocabulary(cs_word, cs_mean, cs_sen);
    }

    /**
     * 将查询结果转化为Vocabulary类
     *
     * @param cs_word VOCABULARY表的查询结果
     * @param cs_mean MEANING表的查询结果
     * @param cs_sen SENTENCE表的查询结果
     * @return 转化成的Vocabulary类
     */
    private List<Vocabulary> CursorToVocabulary(Cursor cs_word, Cursor cs_mean, Cursor cs_sen) {
        int size = cs_word.getCount();
        if(size == 0)
            return null;

        //使用hashmap组织查询结果
        Map<String, Vocabulary> map = new HashMap<String, Vocabulary>();

        //首先登录所有word值
        cs_word.moveToFirst();
        do {
            map.put(cs_word.getString(0), null);
        } while(cs_word.moveToNext());

        //其次通过meaning值构建每个word对应的Vocabulary类，将map中所有对象实体化
        cs_mean.moveToFirst();
        do {
            String name = cs_mean.getString(0);
            String meaning = cs_mean.getString(1);
            if(map.get(name) == null)
                map.put(name, new Vocabulary(name, meaning));
            else
                map.get(name).addMeaning(meaning);
        } while(cs_mean.moveToNext());

        //最终插入sentence
        cs_sen.moveToFirst();
        do {
            String name = cs_sen.getString(0);
            map.get(name).addSentence(cs_sen.getString(1), cs_sen.getString(2));
        } while(cs_sen.moveToNext());

        //将map转化为List
        List<Vocabulary> list = new ArrayList<>();
        for(String key: map.keySet())
            list.add(map.get(key));
        return list;
    }

}
