package id.elfadl.instaapp.viewmodel;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class LayoutFeedViewModel extends BaseObservableViewModel {

    @Bindable
    String imgProfile, username, imagePost, totalLikes, totalComments, caption, postAt;

    @Bindable
    boolean adaKomentar, adaLike, like;

    public String getImgProfile() {
        return imgProfile;
    }

    public void setImgProfile(String imgProfile) {
        this.imgProfile = imgProfile;
        notifyPropertyChanged(BR.imageProfile);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    public String getImagePost() {
        return imagePost;
    }

    public void setImagePost(String imagePost) {
        this.imagePost = imagePost;
        notifyPropertyChanged(BR.imagePost);
    }

    public String getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(String totalLikes) {
        this.totalLikes = totalLikes;
        notifyPropertyChanged(BR.totalLikes);
    }

    public String getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(String totalComments) {
        this.totalComments = totalComments;
        notifyPropertyChanged(BR.totalComments);
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
        notifyPropertyChanged(BR.caption);
    }

    public String getPostAt() {
        return postAt;
    }

    public void setPostAt(String postAt) {
        this.postAt = postAt;
        notifyPropertyChanged(BR.postAt);
    }

    public boolean isAdaKomentar() {
        return adaKomentar;
    }

    public void setAdaKomentar(boolean adaKomentar) {
        this.adaKomentar = adaKomentar;
        notifyPropertyChanged(BR.adaKomentar);
    }

    public boolean isAdaLike() {
        return adaLike;
    }

    public void setAdaLike(boolean adaLike) {
        this.adaLike = adaLike;
        notifyPropertyChanged(BR.adaLike);
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
        notifyPropertyChanged(BR.like);
    }
}
