package com.example.vocabularybook;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 词汇类，由用户手动编辑添加
 *
 */
public class Vocabulary implements Serializable {
    private String word;                        //单词
    private List<String> meaning;               //释义（可有多个，例如每个词性一个释义）
    private List<String> sentence_text;         //例句原文（可以为空）
    private List<String> sentence_translation;  //例句翻译（可以为空）

    /**
     * 构造函数，只需要单词和一个释义，其他可为空的变量需要调用其他函数添加
     *
     * @param _word 单词
     * @param _meaning 单词的首个释义
     */
    public Vocabulary(String _word, String _meaning){
        assert (_word != null || _meaning != null);
        word = _word;
        meaning = new ArrayList<>();
        meaning.add(_meaning);
        sentence_text = new ArrayList<>();
        sentence_translation = new ArrayList<>();
    }

    /**
     * 设置所有变量，用于词汇编辑功能
     * 如果不一次性设置，可能设置到后面才发现参数非法，导致前面数据被污染
     *
     * @param _word 单词
     * @param _meaning 单词的所有释义
     * @param _sentence_text 全部例句的原文（可为null）
     * @param _sentence_translation 全部例句的释义（可为null）
     *
     * @exception IllegalArgumentException
     *      _word或_meaning为null
     *      类型为List的三个参数中有项为null
     *      _sentence_text和_sentence_translation列表内数目不一致
     */
    public void setVocabulary(String _word, List<String> _meaning,
                              List<String> _sentence_text, List<String> _sentence_translation)
            throws IllegalArgumentException{

        //参数合法性检查
        if(_word == null || _meaning == null || _meaning.size() == 0 || _meaning.contains(null) )
            throw new IllegalArgumentException();
        if(_sentence_text != null && _sentence_translation != null ) {
            if(_sentence_text.size() != _sentence_translation.size() ||
                    _sentence_text.contains(null) || _sentence_translation.contains(null))
                throw new IllegalArgumentException();
        }

        //赋值
        word = _word;
        meaning = _meaning;
        sentence_text.clear();
        sentence_translation.clear();
        for(int i=0; i<_sentence_text.size(); i++)
            sentence_text.add(_sentence_text.get(i));
        for(int i=0; i<_sentence_translation.size(); i++)
            sentence_translation.add(_sentence_translation.get(i));
    }

    /**
     * 向该词汇添加一条例句
     *
     * @param _text 例句原文
     * @param _translation 例句翻译
     */
    public void addSentence(String _text, String _translation){
        assert(_text != null && _translation != null);
        sentence_text.add(_text);
        sentence_translation.add(_translation);
    }

    /**
     * 向该词汇添加一条新的释义
     *
     * @param new_meaning
     */
    public void addMeaning(String new_meaning){
        assert(new_meaning != null);
        meaning.add(new_meaning);
    }

    public String getWord() {
        return word;
    }
    public List<String> getMeaning() {
        return meaning;
    }
    public List<String> getSentence_text() {
        return sentence_text;
    }
    public List<String> getSentence_translation() {
        return sentence_translation;
    }

    /**
     * 通过Log.i输出所有信息
     */
    public void showInfo() {
        //输出word
        final String TAG = "Vocabulary";
        Log.i(TAG, "word: " + word);

        //输出meaning
        String mean = new String();
        for(int i=0; i<meaning.size(); i++)
            mean += meaning.get(i) + "\n";
        Log.i(TAG, "meaning: " + mean);

        //输出sentence
        String sen = new String();
        if(sentence_text.size() != 0) {
            for(int i=0; i<sentence_text.size(); i++)
                sen += sentence_text.get(i) + "\n" + sentence_translation.get(i) + "\n";
            Log.i(TAG, "sentence: " + sen);
        }
    }
}
