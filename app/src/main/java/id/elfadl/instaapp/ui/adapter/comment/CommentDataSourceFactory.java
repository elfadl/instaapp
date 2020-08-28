package id.elfadl.instaapp.ui.adapter.comment;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import id.elfadl.instaapp.model.comment.Comment;
import id.elfadl.instaapp.viewmodel.BaseObservableViewModel;

public class CommentDataSourceFactory extends DataSource.Factory<Integer, Comment> {

    private BaseObservableViewModel viewModel;
    private CommentDataSource commentDataSource;
    private MutableLiveData<PageKeyedDataSource<Integer, Comment>> commentLiveData = new MutableLiveData<>();
    private Long idPost;

    public CommentDataSourceFactory(BaseObservableViewModel viewModel, Long idPost) {
        this.viewModel = viewModel;
        this.idPost = idPost;
    }

    @Override
    public DataSource<Integer, Comment> create() {
        commentDataSource = new CommentDataSource(viewModel, idPost);

        commentLiveData.postValue(commentDataSource);

        return commentDataSource;
    }

    public void refresh() {
        if (commentDataSource != null)
            commentDataSource.invalidate();
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Comment>> getCommentLiveData() {
        return commentLiveData;
    }
}
