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
        isFirstClick.setValue(!isFirstClick.getValue());
    }

    public void markAsUnknown() {
        isFirstClick.setValue(!isFirstClick.getValue());
    }

    public void setWordList(List<Word> words) {
        
    }
}
