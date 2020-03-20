package com.example.vocabularybook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
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
        db.execSQL("Create table Vocabulary(" +
                "word text references Vocabulary(word) not null," +
                "sentenceText text not null," +
                "sentenceTranslation text not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
