package id.elfadl.instaapp.application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.gson.Gson;
import com.hss01248.dialog.ActivityStackManager;
import com.hss01248.dialog.StyledDialog;

import java.text.NumberFormat;

import id.elfadl.instaapp.hepers.FontCache;
import id.elfadl.instaapp.hepers.api.RetrofitApiSingleton;
import id.elfadl.instaapp.hepers.cache.CacheObject;
import id.elfadl.instaapp.hepers.prefs.Prefs;
import id.elfadl.instaapp.ui.activity.SplashScreen;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MainApplication extends MultiDexApplication {

    static {
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);
    }

    public final static String MAIN_URL = "https://instaapp.esuka.id/";
    public final static String URL_SIGN_IN = "user/login";
    public final static String URL_SIGN_UP = "user/register";
    public final static String URL_UPLOAD_IMAGE_PROFILE = "member/upload-image-profile";
    public final static String URL_UPDATE_PROFILE = "member/update-profile";
    public final static String URL_GET_PROFILE = "member/get-profile";
    public final static String URL_FEED = "feed/get-feed";
    public final static String URL_LIKE = "feed/like";
    public final static String URL_GET_COMMENT = "feed/get-comment";
    public final static String URL_COMMENT = "feed/comment";
    public final static String URL_POST = "feed/post";
    public final static String URL_HAPUS_FEED = "feed/hapus";
    public final static String URL_FOLLOW = "member/follow";
    public final static String URL_GET_DETAIL_KELAS = "siswa/get-jadwal-by-kelas";
    public final static String URL_RIWAYAT_BONUS = "bonus";
    public final static String URL_TIKET = "tiket";
    public final static String URL_TRANSFER = "transfer-saldo";
    public final static String URL_MUTASI = "mutasi";
    public final static String URL_TRANSAKSI_PENDING = "transaksi-pending";
    public final static String URL_SEMUA_TRANSAKSI = "history-transaksi";
    public final static String URL_PELANGGAN = "pelanggan";
    public final static String URL_KOLEKTIF = "kolektif";
    public final static String URL_BERITA = "berita";
    public final static String URL_FIREBASE = "firebase";
    public final static String URL_AKUN = "akun";
    public final static String URL_STATISTIK = "statistik";
    public final static String URL_UPDATE_FOTO_PROFIL = "foto";
    public final static String URL_HARGA_PRODUK = "harga";
    public final static String URL_PROVIDER = "provider";
    public final static String URL_PRODUK = "produk";
    public final static String URL_LOG_PELANGGAN = "pelanggan";
    public final static String URL_LIST_PENGIRIM = "list-pengirim";
    public final static String URL_UBAH_MARKUP = "markup";
    public final static String URL_TRANSAKSI = "transaksi";
    public final static String URL_STRUK = "struk";
    public final static String URL_CHAT = "chat";
    public final static String URL_NOTIFIKASI = "notif";
    public final static String URL_BANTUAN = "bantuan";
    public final static String URL_OTP_MANUAL = "request-otp-login";
    public final static String URL_OTP_MANUAL_PIN = "request-otp-pin";
    public static final int REQUEST_CODE = 1;
    public static final int NOTIFICATION_ID = 6578;
    public static final String NOTIFICATION_CHANNEL = "kios_pulsa";
    public static final String RAJA_ONGKIR_DEV_KEY = "274dd6e5cc51fa4bb2917509b971435d";
    public final static String headers = "INOV-KEY: inov-kiospulsa";
    public final static String headersRajaOngkir = "key: " + RAJA_ONGKIR_DEV_KEY;
    public static final String password = "1n0v4t1v0kiospul"; // max 16 char
    public static final String DEVELOPER_KEY = "AIzaSyCyLRo9q7_lK2XJKOB8A5rfKLyIprBE45A";
    public static final boolean DEBUG_MODE = true;
    public static final boolean EMULATOR_MODE = false;
    public static final boolean SHWO_NETWORK_ERROR = false;
    private static NumberFormat format;
    private static Context context;

    private static boolean foreground = false, background = false;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        MainApplication.context = this;

        StyledDialog.init(this);

        RetrofitApiSingleton.getInstance().init();

        Prefs.initPrefs(this);

        FontCache.getInstance(this).addFont("bold", "Poppins-Bold.ttf");
        FontCache.getInstance(this).addFont("regular", "SourceSansPro-Regular.ttf");
        FontCache.getInstance(this).addFont("semibold", "SourceSansPro-SemiBold.ttf");

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                ActivityStackManager.getInstance().addActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ActivityStackManager.getInstance().removeActivity(activity);
            }
        });
    }

    public static Context getContext() {
        return context;
    }

    public static void removeFromCache(String key) {
        Prefs.remove(key);
    }

    public static void putToCache(String key, String val) {
        Prefs.putString(key, new CacheObject(key, val).toJson());
    }

    public static void putToCache(String key, String val, boolean noExpiry) {
        Prefs.putString(key, new CacheObject(key, val, noExpiry).toJson());
    }

    public static void putToCache(String key, String val, int filed, int amount) {
        Prefs.putString(key, new CacheObject(key, val, filed, amount).toJson());
    }

    @Nullable
    public static String getFromCache(String key) {
        if (!Prefs.getString(key, "").equals("")) {
            return new Gson().fromJson(Prefs.getString(key, ""), CacheObject.class).getValue();
        }
        return null;
    }

    public static String getAuth() {
        if(getFromCache("auth_key") == null){
            Prefs.clear();
            context.startActivity(new Intent(MainApplication.getContext(), SplashScreen.class).addFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK));
        }
        return getFromCache("auth_key");
    }

    public static boolean isForeground() {
        return foreground;
    }

    public static boolean isBackground() {
        return background;
    }

    public static void setForeground(boolean foreground) {
        MainApplication.foreground = foreground;
    }

    public static void setBackground(boolean background) {
        MainApplication.background = background;
    }
}
