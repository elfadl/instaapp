package id.elfadl.instaapp.viewmodel;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class AddPostViewModel extends BaseObservableViewModel {

    @Bindable
    String caption;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
        notifyPropertyChanged(BR.caption);
    }
}
