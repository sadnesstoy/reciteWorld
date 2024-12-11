package com.example.reciteword;

import static com.example.reciteword.Data.wordRepository;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

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
        Spinner wordLibrarySpinner = getView().findViewById(R.id.spinner);

        // 创建一个包含所有词库的列表，包含“词库”项作为默认项
        List<String> wordLibraries = new ArrayList<>();
        wordLibraries.add("词库"); // 默认项
        wordLibraries.add("TOEFL");
        wordLibraries.add("GRE");
        wordLibraries.add("IELTS");

        // 创建一个适配器，将词库列表与 Spinner 关联
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, wordLibraries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wordLibrarySpinner.setAdapter(adapter);

        // 设置默认选择项为“词库” (第一个项)
        wordLibrarySpinner.setSelection(0);

        // 监听 Spinner 选择变化
        wordLibrarySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedLibrary = parentView.getItemAtPosition(position).toString();
                if (!selectedLibrary.equals("词库")) {
                    // 根据选择的词库文件加载词汇
                    wordRepository.clearDatabase();
                    String fileName = selectedLibrary.toLowerCase() + ".txt"; // 假设文件名与词库名称相同
                    List<Word> wordList = FileUtils.readWordsFromFile(getContext(), fileName);
                    // 从文件读取单词数据并插入到数据库
                    for (Word word : wordList) {
                        wordRepository.insertWord(word);
                    }
                    Data.initWordList(getContext());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 可选：处理没有选择任何项的情况
            }
        });

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
                // 加载第一个单词
                reciteViewModel.loadRandomWord();
            } else {
                knowButton.setText("下一词");
                unknowButton.setText("记错了");
                Word currentWord = reciteViewModel.getCurrentWord().getValue();
                if (currentWord != null) {
                    definitionText.setText(currentWord.getPron() + "\n" + currentWord.getDefinition());
                }
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
