package com.example.reciteword;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class wrongFragment extends Fragment {

    private List<Word> wordList = new ArrayList<>();
    private WordAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wrong, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 加载错误单词数据
        loadWrongWords();

        // 设置适配器和 ListView
        adapter = new WordAdapter(getActivity(), R.layout.word_item, wordList);
        ListView listView = getActivity().findViewById(R.id.wrong_list_view);
        listView.setAdapter(adapter);
    }

    /**
     * 从 SharedPreferences 加载错误单词数据
     */
    private void loadWrongWords() {
        SharedPreferences sharedPre = getActivity().getSharedPreferences("t", Context.MODE_PRIVATE);
        int wrongNum = sharedPre.getInt("wrongNum", 0);

        if (wrongNum == 0) {
            Toast.makeText(getActivity(), "没有错误单词", Toast.LENGTH_SHORT).show();
            return;
        }

        wordList.clear();
        for (int i = 1; i <= wrongNum; i++) {
            int wordIndex = sharedPre.getInt("wrong" + i, -1); // 使用默认值 -1 检查无效索引
            if (wordIndex >= 0) {
                Word word = new Word(
                        Data.getWord(wordIndex),
                        Data.getPron(wordIndex),
                        Data.getwordDefine(wordIndex),
                        sharedPre.getInt("word" + wordIndex, 1),
                        0
                );
                wordList.add(word);
            } else {
                // 记录错误索引以便调试
                System.out.println("Invalid word index for wrong" + i);
            }
        }
    }
}
