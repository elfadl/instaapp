package id.elfadl.instaapp.viewmodel;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import id.elfadl.instaapp.model.comment.Comment;
import id.elfadl.instaapp.ui.adapter.comment.CommentDataSourceFactory;

public class CommentViewModel extends BaseObservableViewModel {

    @Bindable
    String imgProfile, comment, imgProfileTS, usernameTS, captionTS, postAtTs;

    public String getImgProfile() {
        return imgProfile;
    }

    public void setImgProfile(String imgProfile) {
        this.imgProfile = imgProfile;
        notifyPropertyChanged(BR.imgProfile);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
        notifyPropertyChanged(BR.comment);
    }

    public String getImgProfileTS() {
        return imgProfileTS;
    }

    public void setImgProfileTS(String imgProfileTS) {
        this.imgProfileTS = imgProfileTS;
        notifyPropertyChanged(BR.imgProfileTS);
    }

    public String getUsernameTS() {
        return usernameTS;
    }

    public void setUsernameTS(String usernameTS) {
        this.usernameTS = usernameTS;
        notifyPropertyChanged(BR.usernameTS);
    }

    public String getCaptionTS() {
        return captionTS;
    }

    public void setCaptionTS(String captionTS) {
        this.captionTS = captionTS;
        notifyPropertyChanged(BR.captionTS);
    }

    public String getPostAtTs() {
        return postAtTs;
    }

    public void setPostAtTs(String postAtTs) {
        this.postAtTs = postAtTs;
        notifyPropertyChanged(BR.postAtTS);
    }

    LiveData<PagedList<Comment>> commentPagedList;
    LiveData<PageKeyedDataSource<Integer, Comment>> commentDataSource;
    CommentDataSourceFactory commentDataSourceFactory;

    public LiveData<PagedList<Comment>> getCommentPagedList() {
        return commentPagedList;
    }

    public void refresh() {
        if(commentDataSourceFactory != null)
            commentDataSourceFactory.refresh();
    }

    public CommentViewModel(Long idPost) {
        commentDataSourceFactory = new CommentDataSourceFactory(this, idPost);

        commentDataSource = commentDataSourceFactory.getCommentLiveData();

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .build();

        commentPagedList = new LivePagedListBuilder(commentDataSourceFactory, config).build();

    }

}
