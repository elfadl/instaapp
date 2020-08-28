package id.elfadl.instaapp.hepers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;

import java.util.List;

import id.elfadl.instaapp.widget.camera.FrontCameraImagePicker;
import pub.devrel.easypermissions.EasyPermissions;

public abstract class PickImageHelper {

    String outputPath = "";

    public PickImageHelper(final Context context, final CameraImagePicker cameraPicker, final ImagePicker imagePicker) {
        String[] menu = {"Ambil Foto", "Ambil dari Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Pilih salah satu");
        builder.setOnCancelListener(dialog -> {
            onCancel();
        });
        builder.setItems(menu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        cameraPicker.setImagePickerCallback(new ImagePickerCallback() {

                            @Override
                            public void onImagesChosen(List<ChosenImage> list) {
                                onResult(outputPath);
                            }

                            @Override
                            public void onError(String s) {
                                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                            }
                        });
                        outputPath = cameraPicker.pickImage();
                        break;
                    case 1:
                        imagePicker.setImagePickerCallback(new ImagePickerCallback() {
                            @Override
                            public void onImagesChosen(List<ChosenImage> list) {
                                onResult(list.get(0).getOriginalPath());
                                /*outputPath = list.get(0).getOriginalPath();
                                pic = BitmapHelper.decodeFile(outputPath);
//                                        binding.profilePhoto.setImageBitmap(pic);
                                Glide.with(CompleteAccount.this).load(pic).apply(RequestOptions.bitmapTransform(new MultiTransformation<Bitmap>(new CenterCrop(), new CircleCrop()))).into(binding.profilePhoto);*/
                            }

                            @Override
                            public void onError(String s) {
                                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                            }
                        });
                        imagePicker.pickImage();
                        break;
                }
            }
        });

        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        int RC_CAMERA_AND_WIFI = 5370;

        if (!EasyPermissions.hasPermissions(context, perms)) {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions((Activity) context, "Aplikasi ini membutuhkan izin akses KAMERA",
                    RC_CAMERA_AND_WIFI, perms);
        }else {
            builder.show();
        }
    }

    public PickImageHelper(final Context context, final FrontCameraImagePicker cameraPicker, final ImagePicker imagePicker) {
        String[] menu = {"Ambil Foto", "Ambil dari Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Pilih salah satu");
        builder.setItems(menu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        cameraPicker.setImagePickerCallback(new ImagePickerCallback() {

                            @Override
                            public void onImagesChosen(List<ChosenImage> list) {
                                onResult(outputPath);
                            }

                            @Override
                            public void onError(String s) {
                                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                            }
                        });
                        outputPath = cameraPicker.pickImage();
                        break;
                    case 1:
                        imagePicker.setImagePickerCallback(new ImagePickerCallback() {
                            @Override
                            public void onImagesChosen(List<ChosenImage> list) {
                                onResult(list.get(0).getOriginalPath());
                                /*outputPath = list.get(0).getOriginalPath();
                                pic = BitmapHelper.decodeFile(outputPath);
//                                        binding.profilePhoto.setImageBitmap(pic);
                                Glide.with(CompleteAccount.this).load(pic).apply(RequestOptions.bitmapTransform(new MultiTransformation<Bitmap>(new CenterCrop(), new CircleCrop()))).into(binding.profilePhoto);*/
                            }

                            @Override
                            public void onError(String s) {
                                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                            }
                        });
                        imagePicker.pickImage();
                        break;
                }
            }
        });

        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        int RC_CAMERA_AND_WIFI = 5370;

        if (!EasyPermissions.hasPermissions(context, perms)) {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions((Activity) context, "Aplikasi ini membutuhkan izin akses KAMERA",
                    RC_CAMERA_AND_WIFI, perms);
        } else {
            builder.show();
        }
    }

    public PickImageHelper(final Context context, final FrontCameraImagePicker cameraPicker) {
        cameraPicker.setImagePickerCallback(new ImagePickerCallback() {

            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                onResult(outputPath);
            }

            @Override
            public void onError(String s) {
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            }
        });
        outputPath = cameraPicker.pickImage();

        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        int RC_CAMERA_AND_WIFI = 5370;

        if (!EasyPermissions.hasPermissions(context, perms)) {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions((Activity) context, "Aplikasi ini membutuhkan izin akses KAMERA",
                    RC_CAMERA_AND_WIFI, perms);
        }
    }

    public abstract void onResult(String outputPath);

    public abstract void onCancel();

}
