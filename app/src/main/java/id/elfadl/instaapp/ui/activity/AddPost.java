package id.elfadl.instaapp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.hss01248.dialog.StyledDialog;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.theartofdev.edmodo.cropper.CropImage;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import id.elfadl.instaapp.R;
import id.elfadl.instaapp.application.MainApplication;
import id.elfadl.instaapp.databinding.AddPostBinding;
import id.elfadl.instaapp.hepers.BitmapHelper;
import id.elfadl.instaapp.hepers.HeadersUtil;
import id.elfadl.instaapp.hepers.ImageCompression;
import id.elfadl.instaapp.hepers.PickImageHelper;
import id.elfadl.instaapp.hepers.UploadHelper;
import id.elfadl.instaapp.hepers.network.RequestObservableAPI;
import id.elfadl.instaapp.model.post.PostBody;
import id.elfadl.instaapp.model.post.PostModel;
import id.elfadl.instaapp.viewmodel.AddPostViewModel;
import okhttp3.RequestBody;
import retrofit2.Call;

public class AddPost extends BaseActivity {

    private static final int REQUEST_CODE_CHOOSE = 8735;
    private List<Uri> mSelected;
    private AddPostBinding binding;
    private AddPostViewModel viewModel;
    private CameraImagePicker cameraPicker;
    private ImagePicker imagePicker;
    private CropImage.ActivityResult res;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        shouldChangeStatusBarTintToDark = true;
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.add_post);
        viewModel = new ViewModelProvider(this).get(AddPostViewModel.class);
        binding.setViewmodel(viewModel);

        cameraPicker = new CameraImagePicker(this);
        imagePicker = new ImagePicker(this);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        pickImage();

        binding.btnBagikan.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(viewModel.getCaption()) && res != null) {
                loading = StyledDialog.buildLoading("Loading").show();
                new ImageCompression(this) {
                    @Override
                    public void onResult(File file) {
                        RequestObservableAPI<PostModel> request = new RequestObservableAPI<PostModel>(AddPost.this, PostModel.class, true) {
                            @Override
                            public void onResult(PostModel result) {
                                StyledDialog.dismiss(loading);
                                if(result != null){
                                    if(result.getStatus() == 1){
                                        setResult(RESULT_OK);
                                        finish();
                                        Toast.makeText(AddPost.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                                    }else {
                                        AlertDialog.Builder dialog = new AlertDialog.Builder(AddPost.this);
                                        dialog.setTitle("Peringatan!");
                                        dialog.setMessage(result.getMessage());
                                        dialog.setPositiveButton("OK", null);
                                        dialog.setCancelable(false);
                                        if (!isFinishing())
                                            dialog.show();
                                    }
                                }else {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(AddPost.this);
                                    dialog.setTitle("Peringatan!");
                                    dialog.setMessage("Terjadi gangguan pada server");
                                    dialog.setPositiveButton("OK", null);
                                    dialog.setCancelable(false);
                                    if (!isFinishing())
                                        dialog.show();
                                }
                            }

                            @Override
                            public Call<PostModel> createCall() {
                                return api.post(HeadersUtil.getInstance().getHeaders(), UploadHelper.createPartFromString(viewModel.getCaption()), UploadHelper.prepareFilePart("file", file));
                            }
                        };
                        request.setRetryTime(0);
                        request.setOnFailureListener(() -> StyledDialog.dismiss(loading));
                    }
                }.execute(res.getUri().getPath());
            }
        });

    }

    private void pickImage() {
        PickImageHelper pickImageHelper = new PickImageHelper(this, cameraPicker, imagePicker) {
            @Override
            public void onResult(String outputPath) {
                loading = StyledDialog.buildLoading("Loading").show();
                Uri imageUri = Uri.fromFile(new File(outputPath));
//                Uri imageUri = FileProvider.getUriForFile(AddPost.this, getApplicationContext().getPackageName() + ".fileprovider", new File(outputPath));

                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .setActivityTitle("Crop Foto")
                        .setAllowFlipping(false)
                        .start(AddPost.this);
            }

            @Override
            public void onCancel() {
                finish();
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (MainApplication.DEBUG_MODE)
            Log.i("CAMERA", "onActivityResult: " + resultCode + " " + requestCode);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Picker.PICK_IMAGE_CAMERA:
                    cameraPicker.submit(data);
                    break;
                case Picker.PICK_IMAGE_DEVICE:
                    imagePicker.submit(data);
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    StyledDialog.dismiss(loading);
                    res = CropImage.getActivityResult(data);
                    Glide.with(AddPost.this).load(res.getUri()).into(binding.image);
                    break;
            }
        } else {
            StyledDialog.dismiss(loading);
            switch (requestCode) {
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    pickImage();
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Exception error = result.getError();
                    break;
            }

        }
    }

}
