package com.example.reciteword;

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
        if (word != null ) {
            // 增加单词的展示次数
            word.setShowNum(word.getShowNum() + 1);

            // 更新数据库中的单词
            wordRepository.updateWord(word);
        }
        isFirstClick.setValue(!isFirstClick.getValue());

    }

    public void markAsUnknown() {
        Word word = currentWord.getValue();
        if (word != null) {
            // 增加单词的展示次数
            word.setShowNum(word.getShowNum() + 1);

            // 增加 flag（表示没记住）
            word.setFlag(word.getFlag() + 1);

            // 更新数据库中的单词
            wordRepository.updateWord(word);

        }
        isFirstClick.setValue(!isFirstClick.getValue());
    }

}
