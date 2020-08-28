package id.elfadl.instaapp.hepers.network;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.hss01248.dialog.StyledDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import id.elfadl.instaapp.application.MainApplication;
import id.elfadl.instaapp.hepers.api.ApiHelper;
import id.elfadl.instaapp.hepers.prefs.Prefs;
import id.elfadl.instaapp.viewmodel.BaseObservableViewModel;
import okhttp3.HttpUrl;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.elfadl.instaapp.application.MainApplication.SHWO_NETWORK_ERROR;


/**
 * Created by kamal on 1/31/18.
 */

public abstract class RequestObservableAPI<T extends Object> {

    private Context context;
    private Dialog dialog;
    private boolean showError = true, silence, forceRequest;
    private int retryTime = 0;
    private int retry = 0;
    private int expiryInSecond = -1;
    private OnFailureListener onFailureListener;
    private OnFinalFailureListener onFinalFailureListener;
    private OnSuccessListener onSuccessListener;
    private String key;

    public RequestObservableAPI(@NonNull final BaseObservableViewModel viewModel, Class<T> kelas, boolean forceRequest, boolean silence) {
        makeRequest(viewModel, kelas, forceRequest, silence, expiryInSecond, false);
    }

    public RequestObservableAPI(@NonNull final BaseObservableViewModel viewModel, Class<T> kelas, boolean forceRequest, boolean silence, boolean showError, boolean fast) {
        this.showError = showError;
        makeRequest(viewModel, kelas, forceRequest, silence, expiryInSecond, fast);
    }

    public RequestObservableAPI(@NonNull final BaseObservableViewModel viewModel, Class<T> kelas, boolean forceRequest, boolean silence, int expiryInSecond) {
        this.expiryInSecond = expiryInSecond;
        makeRequest(viewModel, kelas, forceRequest, silence, expiryInSecond, false);
    }

    public RequestObservableAPI(@NonNull final BaseObservableViewModel viewModel, Class<T> kelas, boolean forceRequest, boolean silence, boolean showError) {
        this.showError = showError;
        makeRequest(viewModel, kelas, forceRequest, silence, expiryInSecond, false);
    }

    public RequestObservableAPI(@NonNull Context context, Class<T> kelas, boolean forceRequest) {
        this.context = context;
        makeRequest(null, kelas, forceRequest, false, expiryInSecond, false);
    }

    public RequestObservableAPI(@NonNull Context context, Class<T> kelas, boolean forceRequest, boolean silence) {
        this.context = context;
        makeRequest(null, kelas, forceRequest, silence, expiryInSecond, false);
    }

    public void setRetryTime(int retryTime) {
        this.retryTime = retryTime;
    }

    public void setOnFailureListener(OnFailureListener onFailureListener) {
        this.onFailureListener = onFailureListener;
    }

    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    public void setOnFinalFailureListener(OnFinalFailureListener onFinalFailureListener) {
        this.onFinalFailureListener = onFinalFailureListener;
    }

    private void makeRequest(@NonNull final BaseObservableViewModel viewModel, Class<T> kelas, final boolean forceRequest, final boolean silence, int expiryInSecond, boolean fast) {
        if (viewModel != null && !silence) {
            viewModel.setLoading(true);
        } else if (!silence && context != null) {
            /*dialog = new SpotsDialog(context);
            dialog.setCancelable(false);
            dialog.show();*/
            dialog = StyledDialog.buildLoading("Loading").show();

//            dialog.show();
        }
        Call<T> callApi = createCall();
        final HttpUrl url = callApi.request().url();
        key = url.encodedPath() + (url.encodedQuery() != null ? "?" + url.encodedQuery() : "");
//        Log.i("KEY", "RequestAPI: " + key);
        try {
            final RequestBody copy = callApi.request().body();
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            String body = buffer.readUtf8();
            if (MainApplication.DEBUG_MODE)
                Log.i("BODY", "RequestAPI: " + body);
            key = key + body;
        } catch (final IOException e) {
            e.printStackTrace();
        }
        if (MainApplication.DEBUG_MODE)
            Log.i("KEY", "RequestAPI: " + key);
        final String strCache = MainApplication.getFromCache(key);
        if (MainApplication.DEBUG_MODE)
            Log.i("KEY", "RequestAPI: " + strCache);
        this.silence = silence;
        this.forceRequest = forceRequest;
        if (fast) {
            if (strCache != null && !forceRequest) {
                loadFromCache(strCache, kelas, viewModel, silence);
            }
            loadFromNetwork(callApi, key, viewModel, silence, forceRequest);
        } else if (strCache != null && !forceRequest) {
            loadFromCache(strCache, kelas, viewModel, silence);
        } else {
            loadFromNetwork(callApi, key, viewModel, silence, forceRequest);
        }
    }

    private void loadFromCache(String strCache, Class<T> kelas, BaseObservableViewModel viewModel, boolean silence) {
        if (viewModel != null && !silence) {
            viewModel.setLoading(false);
        } else if (!silence && context != null) {
//            dialog.dismiss();
            StyledDialog.dismiss(dialog);
        }
        T data = new Gson().fromJson(strCache, kelas);
        if (MainApplication.DEBUG_MODE)
            Log.i("LOAD FROM CACHE", "loadFromCache: " + strCache);
        onResult(data);
    }

    private void loadFromNetwork(Call<T> callApi, String key, BaseObservableViewModel viewModel, boolean silence, boolean forceRequest) {
        if (callApi.request().method().equals("GET")) retryTime = -1;
        String finalKey = key;
        ApiHelper.enqueueWithRetry(callApi, retryTime != -1 ? retryTime : 3, new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.code() == 200) {
                    if (MainApplication.DEBUG_MODE)
                        Log.i("RESPONSE MESSAGE", "onResponse: " + response.message() + " " + expiryInSecond);
                    Prefs.remove(finalKey);
                    try {
                        if (MainApplication.DEBUG_MODE)
                            Log.i("RESPONSE BODY", "onResponse: " + response.body());
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        if (object.has("status")) {
                            if (object.getString("status").toLowerCase().equals("ok")) {
                                if (expiryInSecond < 0)
                                    MainApplication.putToCache(finalKey, new Gson().toJson(response.body()));
                                else
                                    MainApplication.putToCache(finalKey, new Gson().toJson(response.body()), Calendar.SECOND, expiryInSecond);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (viewModel != null && !silence) {
                        viewModel.setLoading(false);
                    } else if (!silence && context != null) {
//                    dialog.dismiss();
                        StyledDialog.dismiss(dialog);
                    }
                    onResult(response.body());
                    if (onSuccessListener != null)
                        onSuccessListener.onResult(response.body(), finalKey);
                }/*else if(!response.isSuccessful()){
                    Log.i("FAILR", "onResponse: "+response.body());
                }*/
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                boolean loop = false;
                Log.i("FAILURE", "onFailure: " + new Gson().toJson(t));
                if (retry < 3) {
                    if (t.getCause() != null) {
                        if (t.getCause().getMessage() != null)
                            if (!t.getCause().getMessage().contains("BEGIN_OBJECT")) {
                                call.cancel();
                                retry++;
                                loop = true;
//                    Log.i("RES BODY", "onFailure: " + new Gson().toJson(t.getCause().getMessage()));
                                try {
                                    JSONObject object = new JSONObject(new Gson().toJson(call.request().body()));
                                    if (!object.has("status")) {
                                        loadFromNetwork(createCall(), key, viewModel, silence, forceRequest);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    loadFromNetwork(createCall(), key, viewModel, silence, forceRequest);
                                }
                            }
                    } else {
                        call.cancel();
                        retry++;
                        loop = true;
//                    Log.i("RES BODY", "onFailure: " + new Gson().toJson(t.getCause().getMessage()));
                        try {
                            JSONObject object = new JSONObject(new Gson().toJson(call.request().body()));
                            if (!object.has("status")) {
                                loadFromNetwork(createCall(), key, viewModel, silence, forceRequest);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadFromNetwork(createCall(), key, viewModel, silence, forceRequest);
                        }
                    }
                } else {
                    loop = false;
                    if (viewModel != null && !silence) {
                        viewModel.setLoading(false);
                    }
                    if (showError && SHWO_NETWORK_ERROR) {
                        Toast toast = Toast.makeText(MainApplication.getContext(), "Periksa koneksi internet Anda.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                    if (onFinalFailureListener != null)
                        onFinalFailureListener.onFinalFailureListener();
                    //onFail(call, t);
                }
                if (dialog != null && !loop) {
                    StyledDialog.dismiss(dialog);
                }
                if (onFailureListener != null)
                    onFailureListener.onFailureListener();
            }
        });
    }

    public abstract void onResult(T result);

    public abstract Call<T> createCall();

//    public abstract void onFail(Call<T> call, Throwable t);

    public interface OnFailureListener {
        void onFailureListener();
    }

    public interface OnFinalFailureListener {
        void onFinalFailureListener();
    }

    public interface OnSuccessListener<T extends Object> {
        void onResult(T result, String key);
    }

}
