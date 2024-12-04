package com.example.reciteword;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class brushFragment extends Fragment {

    private List<Word> wordList = new ArrayList<>();
    private WordAdapter adapter;
    private int pageSize = 20; // 每次加载的条目数
    private int currentPage = 0; // 当前加载的页数

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_brush, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 初始化 ListView 和 Adapter
        ListView listView = (ListView) getActivity().findViewById(R.id.word_list_view);
        adapter = new WordAdapter(getActivity(), R.layout.word_item, wordList);
        listView.setAdapter(adapter);

        // 加载初始数据
        loadMoreData();

        // 监听滚动事件
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 不做任何操作
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // 如果滑动到底部且没有正在加载数据
                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                    // 加载更多数据
                    loadMoreData();
                }
            }
        });
    }

    private void loadMoreData() {
        // 判断是否还有数据加载
        int totalWords = Data.getWordListSize();
        if (currentPage * pageSize < totalWords) {
            // 如果还有更多数据，加载下一批
            int start = currentPage * pageSize;
            int end = Math.min(start + pageSize, totalWords);

            // 加载新的数据
            for (int i = start; i < end; i++) {
                Word word = new Word(
                        Data.getWord(i),
                        Data.getPron(i),
                        Data.getwordDefine(i),
                        getNum(3),
                        0
                );
                wordList.add(word);
            }

            // 更新页数
            currentPage++;

            // 通知 Adapter 数据已更新
            adapter.notifyDataSetChanged();
        } else {
            // 如果没有更多数据，显示提示
            Toast.makeText(getActivity(), "没有更多单词了", Toast.LENGTH_SHORT).show();
        }
    }

    private static int getNum(int endNum) {
        if (endNum > 0) {
            Random random = new Random();
            return random.nextInt(endNum);
        }
        return 0;
    }
}
