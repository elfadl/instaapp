package id.elfadl.instaapp.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.List;

import id.elfadl.instaapp.R;
import id.elfadl.instaapp.application.MainApplication;
import id.elfadl.instaapp.hepers.api.RetrofitApi;
import id.elfadl.instaapp.hepers.api.RetrofitApiSingleton;
import id.elfadl.instaapp.hepers.prefs.Prefs;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class BaseActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {

    public boolean shouldChangeStatusBarTintToDark = false;
    public boolean gradient = false;
    public boolean solid = false;
    private static int RC_CAMERA_AND_WIFI = 7683;
    RetrofitApi api;
    public String[] perms = {
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private AlertDialog.Builder dialog;
    private AlertDialog alertDialog;

    private OnPermissionResultListener onPermissionResultListener;

    private BroadcastReceiver receiverNetworkError;
    public Dialog loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        refreshStatusBar();

        permission();

        api = RetrofitApiSingleton.getInstance().getApi();

        receiverNetworkError = setNetworkErrorReceiver();


    }

    public void permission() {
        if (!EasyPermissions.hasPermissions(this, perms)) {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "Aplikasi ini membutuhkan izin akses.",
                    RC_CAMERA_AND_WIFI, perms);
        } else {
            if (onPermissionResultListener != null)
                onPermissionResultListener.onPermissionResult(EasyPermissions.hasPermissions(this, perms));
        }
    }

    public void setOnPermissionResultListener(OnPermissionResultListener onPermissionResultListener) {
        this.onPermissionResultListener = onPermissionResultListener;
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher;
    }

    private void refreshStatusBar() {
        refreshStatusBar(shouldChangeStatusBarTintToDark);
    }

    public void refreshStatusBar(boolean tintToDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            if (tintToDark) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.white));
            } else if (gradient) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(Color.parseColor("#0F3F87"));
            } else if (solid) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            } else {
                // We want to change tint color to white again.
                // You can also record the flags in advance so that you can turn UI back completely if
                // you have set other flags before, such as translucent or full screen.
                decor.setSystemUiVisibility(0);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (tintToDark) {
                getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.white));
            } else if (gradient) {
                getWindow().setStatusBarColor(Color.parseColor("#0F3F87"));
            } else {
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            }
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        } else if (!EasyPermissions.hasPermissions(this, this.perms)) {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "Aplikasi ini membutuhkan izin akses READ EXTERNAL STORAGE, ACCESS LOCATION dan READ PHONE STATE",
                    RC_CAMERA_AND_WIFI, this.perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

        if (onPermissionResultListener != null)
            onPermissionResultListener.onPermissionResult(EasyPermissions.hasPermissions(this, perms));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
            /*Toast.makeText(this, R.string.returned_from_app_settings_to_activity, Toast.LENGTH_SHORT)
                    .show();*/
            onPermissionResultListener.onPermissionResult(EasyPermissions.hasPermissions(this, perms));
            if (!EasyPermissions.hasPermissions(this, perms)) {
                finish();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        MainApplication.setForeground(true);
        MainApplication.setBackground(false);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverNetworkError, new IntentFilter("dbmadeha_network_error"));
        super.onResume();
    }

    @Override
    protected void onPause() {
        MainApplication.setForeground(false);
        MainApplication.setBackground(true);
        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverNetworkError);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    public void onRationaleAccepted(int requestCode) {

    }

    @Override
    public void onRationaleDenied(int requestCode) {
        finish();
    }

    public interface OnPermissionResultListener {
        void onPermissionResult(boolean hasPermission);
    }

    public BroadcastReceiver setNetworkErrorReceiver() {
        receiverNetworkError = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                if (dialog == null) {
                    dialog = new AlertDialog.Builder(BaseActivity.this);
                }
                if (alertDialog != null) {
                    if (!alertDialog.isShowing()) {
                        dialog.setTitle("Peringatan!");
                        if (bundle.getInt("code") == 401) {
                            dialog.setMessage("Unauthorized.\nSilakan login ulang.");
                        } else
                            dialog.setMessage(bundle.getString("message"));
                        dialog.setPositiveButton("OK", (dialog1, which) -> {
                            if (bundle.getInt("code") == 401) {
                                Prefs.clear();
                                startActivity(new Intent(MainApplication.getContext(), SplashScreen.class).addFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK));
                            }
                        });
                        dialog.setCancelable(false);
                        if (MainApplication.isForeground())
                            alertDialog = dialog.show();
                    }
                } else {
                    dialog.setTitle("Peringatan!");
                    if (bundle.getInt("code") == 401) {
                        dialog.setMessage("Unauthorized.\nSilakan login ulang.");
                    } else
                        dialog.setMessage(bundle.getString("message"));
                    dialog.setPositiveButton(bundle.getInt("code") == 202 ? "Daftar" : "OK", (dialog1, which) -> {
                        if (bundle.getInt("code") == 401) {
                            Prefs.clear();
                            startActivity(new Intent(MainApplication.getContext(), SplashScreen.class).addFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK));
                        }
                    });
                    dialog.setCancelable(false);
                    if (MainApplication.isForeground())
                        alertDialog = dialog.show();
                }
            }
        };
        return receiverNetworkError;
    }
}
