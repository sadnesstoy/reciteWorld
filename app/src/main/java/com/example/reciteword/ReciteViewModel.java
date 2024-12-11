package com.example.reciteword;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Comparator;
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
        // 获取所有单词
        List<Word> wordList = wordRepository.getAllWords();

        if (wordList.size() > 5) {
            // 随机选择 20 个单词
            List<Word> randomWords = new ArrayList<>();
            while (randomWords.size() < 5) {
                int randomIndex = (int) (Math.random() * wordList.size());
                Word randomWord = wordList.get(randomIndex);
                if (!randomWords.contains(randomWord)) {
                    randomWords.add(randomWord);
                }
            }

            // 按照错误率（错误次数 / 显示次数）排序
            Word highestErrorRateWord = randomWords.stream()
                    .filter(word -> word.getShowNum() > 0) // 避免除以 0
                    .max(Comparator.comparingDouble(word -> (double) word.getFlag() / word.getShowNum()))
                    .orElse(null);

            // 如果所有错误率为 0，随机选择一个单词
            if (highestErrorRateWord == null) {
                int randomIndex = (int) (Math.random() * randomWords.size());
                highestErrorRateWord = randomWords.get(randomIndex);
            }

            // 设置当前单词
            currentWord.setValue(highestErrorRateWord);
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
