package com.example.reciteword;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ReciteViewModelFactory implements ViewModelProvider.Factory {

    private Application application;

    public ReciteViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ReciteViewModel.class)) {
            WordRepository repository = new WordRepository(application);
            return (T) new ReciteViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
