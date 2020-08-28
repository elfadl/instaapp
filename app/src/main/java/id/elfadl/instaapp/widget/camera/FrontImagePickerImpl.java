package id.elfadl.instaapp.widget.camera;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.kbeanie.multipicker.api.exceptions.PickerException;
import com.kbeanie.multipicker.core.ImagePickerImpl;
import com.kbeanie.multipicker.utils.LogUtils;

import java.io.File;

public class FrontImagePickerImpl extends ImagePickerImpl {
    private static final String TAG = ImagePickerImpl.class.getSimpleName();

    public FrontImagePickerImpl(Activity activity, int pickerType) {
        super(activity, pickerType);
    }

    public FrontImagePickerImpl(Fragment fragment, int pickerType) {
        super(fragment, pickerType);
    }

    public FrontImagePickerImpl(android.app.Fragment appFragment, int pickerType) {
        super(appFragment, pickerType);
    }

    @Override
    protected String takePictureWithCamera() throws PickerException {
        Uri uri = null;
        String tempFilePath;
        if (Build.VERSION.SDK_INT < 24 && this.cacheLocation != 400) {
            tempFilePath = this.buildFilePath("jpeg", Environment.DIRECTORY_PICTURES);
            uri = Uri.fromFile(new File(tempFilePath));
        } else {
            tempFilePath = this.getNewFileLocation("jpeg", Environment.DIRECTORY_PICTURES);
            File file = new File(tempFilePath);
            uri = FileProvider.getUriForFile(this.getContext(), this.getFileProviderAuthority(), file);
            LogUtils.d(TAG, "takeVideoWithCamera: Temp Uri: " + uri.getPath());
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        intent.putExtra("output", uri);
        if (this.extras != null) {
            intent.putExtras(this.extras);
        }

        LogUtils.d(TAG, "Temp Path for Camera capture: " + tempFilePath);
        this.pickInternal(intent, 4222);
        return tempFilePath;
    }
}
