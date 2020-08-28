package id.elfadl.instaapp.widget.camera;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import com.kbeanie.multipicker.api.exceptions.PickerException;

public class FrontCameraImagePicker extends FrontImagePickerImpl {
    public FrontCameraImagePicker(Activity activity) {
        super(activity, 4222);
    }

    public FrontCameraImagePicker(Fragment fragment) {
        super(fragment, 4222);
    }

    public FrontCameraImagePicker(android.app.Fragment appFragment) {
        super(appFragment, 4222);
    }

    public FrontCameraImagePicker(Activity activity, String path) {
        super(activity, 4222);
        this.reinitialize(path);
    }

    public FrontCameraImagePicker(Fragment fragment, String path) {
        super(fragment, 4222);
        this.reinitialize(path);
    }

    public FrontCameraImagePicker(android.app.Fragment appFragment, String path) {
        super(appFragment, 4222);
        this.reinitialize(path);
    }

    public String pickImage() {
        String path = null;

        try {
            path = this.pick();
        } catch (PickerException var3) {
            var3.printStackTrace();
            if (this.callback != null) {
                this.callback.onError(var3.getMessage());
            }
        }

        return path;
    }
}
