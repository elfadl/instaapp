package id.elfadl.instaapp.viewmodel.factory;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Map;

import id.elfadl.instaapp.viewmodel.CommentViewModel;

public class CommentViewModelFactory implements ViewModelProvider.Factory {

    private Long idPost;

    public CommentViewModelFactory(Long idPost) {
        this.idPost = idPost;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CommentViewModel(idPost);
    }
}
