package com.example.reciteword;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ReciteViewModel extends ViewModel {
    private WordRepository wordRepository;
    private MutableLiveData<Word> currentWord = new MutableLiveData<>();
    private MutableLiveData<Boolean> isFirstClick = new MutableLiveData<>(true);

    public ReciteViewModel(WordRepository repository) {
        this.wordRepository = repository;
    }

    public LiveData<Word> getCurrentWord() {
        return currentWord;
    }

    public LiveData<Boolean> getIsFirstClick() {
        return isFirstClick;
    }

    public void loadRandomWord() {
        List<Word> wordList = wordRepository.getAllWords();
        if (!wordList.isEmpty()) {
            int randomIndex = (int) (Math.random() * wordList.size());
            currentWord.setValue(wordList.get(randomIndex));
        }
    }

    public void markAsKnown() {
        Word word = currentWord.getValue();
        if (word != null && isFirstClick.getValue()) {
            // 增加单词的展示次数
            word.setShowNum(word.getShowNum() + 1);

            // 更新数据库中的单词
            wordRepository.updateWord(word);
        }
        isFirstClick.setValue(!isFirstClick.getValue());

    }

    public void markAsUnknown() {
        Word word = currentWord.getValue();
        if (word != null&&isFirstClick.getValue()) {
            // 增加单词的展示次数
            word.setShowNum(word.getShowNum() + 1);

            // 增加 flag（表示没记住）
            word.setFlag(word.getFlag() + 1);

            // 更新数据库中的单词
            wordRepository.updateWord(word);

        }
        isFirstClick.setValue(!isFirstClick.getValue());
    }

    public void marktipsButton(Context context) {

        // 弹出确认对话框
        new AlertDialog.Builder(context)
                .setTitle("确认重置所有数据")
                .setMessage("您确定要重置所有数据吗？此操作不可撤销。")
                .setPositiveButton("确定", (dialog, which) -> {
                    // 用户确认后执行重置操作
                    wordRepository = new WordRepository(context);

                    // 清空数据库并重新加载数据
                    wordRepository.clearDatabase();

                    // 从文件读取单词数据并插入到数据库
                    List<Word> wordList = FileUtils.readWordsFromFile(context, "word.txt");
                    for (Word word : wordList) {
                        wordRepository.insertWord(word);
                    }

                    // 初始化数据列表
                    Data.initWordList(context);

                    // 提示用户数据已重置
                    Toast.makeText(context, "数据已重置", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("取消", null) // 取消按钮
                .show();
    }
}
