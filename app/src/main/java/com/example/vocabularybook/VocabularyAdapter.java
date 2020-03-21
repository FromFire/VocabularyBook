package com.example.vocabularybook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class VocabularyAdapter extends ArrayAdapter<Vocabulary> {

    private int resourceId; //该listView的id

    /**
     * 初始化整个listView
     *
     * @param context 上下文
     * @param textViewResourceId 每一项的layout模板，级wordlist_item
     * @param objects 要显示的Vocabulary列表
     */
    public VocabularyAdapter(Context context, int textViewResourceId, List<Vocabulary> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    /**
     * 设置listView单个项的显示内容
     *
     * @param position 这项在listView中的序号
     * @param convertView 用不到
     * @param parent 用不到
     *
     * @return 这一项的layout
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Vocabulary voc = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView word = view.findViewById(R.id.word);
        TextView meaning = view.findViewById(R.id.meaning);

        //所有meaning都显示
        String allMean = new String();
        List<String> meanList = voc.getMeaning();
        for(int i=0; i<meanList.size(); i++)
            allMean += (meanList.get(i) + " ");

        word.setText(voc.getWord());
        meaning.setText(allMean);

        return view;
    }
}
