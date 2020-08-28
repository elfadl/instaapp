package id.elfadl.instaapp.viewmodel;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;


public class LayoutCommentViewModel extends BaseObservableViewModel {

    @Bindable
    String imgProfileTS, usernameTS, captionTS, postAtTS, imgProfile, postAt;

    @Bindable
    boolean first;

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

    public String getPostAtTS() {
        return postAtTS;
    }

    public void setPostAtTS(String postAtTS) {
        this.postAtTS = postAtTS;
        notifyPropertyChanged(BR.postAtTS);
    }

    public String getImgProfile() {
        return imgProfile;
    }

    public void setImgProfile(String imgProfile) {
        this.imgProfile = imgProfile;
        notifyPropertyChanged(BR.imgProfile);
    }

    public String getPostAt() {
        return postAt;
    }

    public void setPostAt(String postAt) {
        this.postAt = postAt;
        notifyPropertyChanged(BR.postAt);
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
        notifyPropertyChanged(BR.first);
    }
}
