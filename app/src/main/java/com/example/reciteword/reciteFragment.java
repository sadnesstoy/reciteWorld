package com.example.reciteword;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class reciteFragment extends Fragment {

    private ReciteViewModel reciteViewModel;
    private Button knowButton, unknowButton;
    private ImageButton tipsButton;
    private TextView wordText, definitionText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recite, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 初始化 ViewModel
        reciteViewModel = new ViewModelProvider(this, new ReciteViewModelFactory(getActivity().getApplication())).get(ReciteViewModel.class);

        // 初始化 View
        knowButton = getView().findViewById(R.id.knowButton);
        unknowButton = getView().findViewById(R.id.unknowButton);
        tipsButton = getView().findViewById(R.id.tipsButton);
        wordText = getView().findViewById(R.id.wordText);
        definitionText = getView().findViewById(R.id.definitionText);

        // 监听 LiveData
        reciteViewModel.getCurrentWord().observe(getViewLifecycleOwner(), word -> {
            if (word != null) {
                wordText.setText(word.getWord());
                definitionText.setText(""); // 清空定义
            }
        });

        reciteViewModel.getIsFirstClick().observe(getViewLifecycleOwner(), isFirstClick -> {
            if (isFirstClick) {
                knowButton.setText("认识");
                unknowButton.setText("不认识");
            } else {
                knowButton.setText("下一词");
                unknowButton.setText("记错了");
            }
        });

        // 设置按钮点击事件
        knowButton.setOnClickListener(v -> reciteViewModel.markAsKnown());
        unknowButton.setOnClickListener(v -> reciteViewModel.markAsUnknown());
        tipsButton.setOnClickListener(v -> {
            Word currentWord = reciteViewModel.getCurrentWord().getValue();
            if (currentWord != null) {
                definitionText.setText(currentWord.getPron() + "\n" + currentWord.getDefinition());
            }
        });

        // 加载第一个单词
        reciteViewModel.loadRandomWord();
    }
}
