package id.elfadl.instaapp.ui.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import com.google.gson.Gson;
import com.hss01248.dialog.StyledDialog;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;

import id.elfadl.instaapp.R;
import id.elfadl.instaapp.application.MainApplication;
import id.elfadl.instaapp.databinding.CompleteProfileBinding;
import id.elfadl.instaapp.hepers.BitmapHelper;
import id.elfadl.instaapp.hepers.HeadersUtil;
import id.elfadl.instaapp.hepers.ImageCompression;
import id.elfadl.instaapp.hepers.PickImageHelper;
import id.elfadl.instaapp.hepers.UploadHelper;
import id.elfadl.instaapp.hepers.network.RequestObservableAPI;
import id.elfadl.instaapp.model.profile.Profile;
import id.elfadl.instaapp.model.profile.ProfileBody;
import id.elfadl.instaapp.model.profile.ProfileModel;
import id.elfadl.instaapp.model.upload_image_profile.UploadImageProfileModel;
import id.elfadl.instaapp.viewmodel.CompleteProfileViewModel;
import id.elfadl.instaapp.widget.camera.FrontCameraImagePicker;
import retrofit2.Call;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class CompleteProfile extends BaseActivity {

    private CompleteProfileBinding binding;
    private CompleteProfileViewModel viewModel;
    private CameraImagePicker cameraPicker;
    private ImagePicker imagePicker;
    private String outputPath = "";
    private Bitmap pic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        shouldChangeStatusBarTintToDark = true;
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.complete_profile);
        viewModel = new ViewModelProvider(this).get(CompleteProfileViewModel.class);
        binding.setViewmodel(viewModel);

        cameraPicker = new CameraImagePicker(this);
        imagePicker = new ImagePicker(this);

        init();

        binding.btnChangePhoto.setOnClickListener(v -> pickImage());

        binding.btnSelesai.setOnClickListener(v -> {
            ProfileBody body = new ProfileBody();
            body.setNama(viewModel.getNama());
            body.setBio(viewModel.getBio());
            body.setJenisKelamin(binding.rdbgGender.getCheckedRadioButtonId() == R.id.rdb_pria ? 1 : 0);
            body.setNoHp(viewModel.getNoHp());
            new RequestObservableAPI<ProfileModel>(this, ProfileModel.class, true) {
                @Override
                public void onResult(ProfileModel result) {
                    if(result != null){
                        if(result.getStatus() == 1){
                            MainApplication.putToCache("profile", new Gson().toJson(result.getProfile()));
                            startActivity(new Intent(CompleteProfile.this, MainActivity.class).addFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK));
                        }else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(CompleteProfile.this);
                            builder.setTitle("Peringatan!");
                            builder.setMessage(result.getMessage());
                            builder.setPositiveButton("OK", null);
                            builder.show();
                        }
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CompleteProfile.this);
                        builder.setTitle("Peringatan!");
                        builder.setMessage("Terjadi ganggaun pada server");
                        builder.setPositiveButton("OK", null);
                        builder.show();
                    }
                }

                @Override
                public Call<ProfileModel> createCall() {
                    return api.updateProfile(HeadersUtil.getInstance().getHeaders(), body);
                }
            };

        });

    }

    private void init() {
        viewModel.getProfile(true).observe(this, profileModel -> {
            if(profileModel != null){
                if(profileModel.getStatus() == 1){
                    Profile profile = profileModel.getProfile();
                    if(profile != null) {
                        viewModel.setImageProfile(profile.getFoto());
                        viewModel.setNama(profile.getNama());
                        viewModel.setBio(profile.getBio());
                        viewModel.setGender(profile.getJenisKelamin());
                        viewModel.setNoHp(profile.getNoHp());
                    }
                }
            }
        });
    }

    private void pickImage() {
        PickImageHelper pickImageHelper = new PickImageHelper(this, cameraPicker, imagePicker) {
            @Override
            public void onResult(String outputPath) {
                loading = StyledDialog.buildLoading("Loading").show();
                CompleteProfile.this.outputPath = outputPath;
                pic = BitmapHelper.decodeFile(outputPath);
                Uri imageUri = Uri.fromFile(new File(outputPath));
//                Uri imageUri = FileProvider.getUriForFile(CompleteProfile.this, getApplicationContext().getPackageName() + ".fileprovider", new File(outputPath));

                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .setActivityTitle("Crop Foto")
                        .setAllowFlipping(false)
                        .start(CompleteProfile.this);
            }

            @Override
            public void onCancel() {

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
                    CropImage.ActivityResult res = CropImage.getActivityResult(data);
                    new ImageCompression(this) {
                        @Override
                        public void onResult(File file) {
                            RequestObservableAPI<UploadImageProfileModel> request = new RequestObservableAPI<UploadImageProfileModel>(CompleteProfile.this, UploadImageProfileModel.class, true) {
                                @Override
                                public void onResult(UploadImageProfileModel result) {
                                    StyledDialog.dismiss(loading);
                                    if(result != null){
                                        if(result.getStatus() == 1){
                                            viewModel.setImageProfile(result.getUrl());
                                            Toast.makeText(CompleteProfile.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                                        }else {
                                            AlertDialog.Builder dialog = new AlertDialog.Builder(CompleteProfile.this);
                                            dialog.setTitle("Peringatan!");
                                            dialog.setMessage(result.getMessage());
                                            dialog.setPositiveButton("OK", null);
                                            dialog.setCancelable(false);
                                            if (!isFinishing())
                                                dialog.show();
                                        }
                                    }else {
                                        AlertDialog.Builder dialog = new AlertDialog.Builder(CompleteProfile.this);
                                        dialog.setTitle("Peringatan!");
                                        dialog.setMessage("Terjadi gangguan pada server");
                                        dialog.setPositiveButton("OK", null);
                                        dialog.setCancelable(false);
                                        if (!isFinishing())
                                            dialog.show();
                                    }
                                }

                                @Override
                                public Call<UploadImageProfileModel> createCall() {
                                    return api.uploadImageProfile(HeadersUtil.getInstance().getHeaders(), UploadHelper.prepareFilePart("file", file));
                                }
                            };
                            request.setRetryTime(0);
                            request.setOnFailureListener(() -> StyledDialog.dismiss(loading));
                        }
                    }.execute(res.getUri().getPath());
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
