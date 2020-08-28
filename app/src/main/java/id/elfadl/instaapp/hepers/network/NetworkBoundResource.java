package id.elfadl.instaapp.hepers.network;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Calendar;

import id.elfadl.instaapp.application.MainApplication;
import id.elfadl.instaapp.hepers.prefs.Prefs;
import id.elfadl.instaapp.viewmodel.BaseObservableViewModel;
import okhttp3.HttpUrl;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class NetworkBoundResource<RequestType> {

    private MutableLiveData<RequestType> data = new MutableLiveData<>();
    private Class<RequestType> kelas;
    private Type type;
    private BaseObservableViewModel viewModel;
    private boolean fast = false;
    private int retryTime = 0;
    private int expiryInMilis = 0;

    @MainThread
    public NetworkBoundResource(Class<RequestType> kelas, BaseObservableViewModel viewModel) {
        this.kelas = kelas;
        this.viewModel = viewModel;
        retryTime = 0;
        loadData();
    }

    @MainThread
    public NetworkBoundResource(Class<RequestType> kelas, BaseObservableViewModel viewModel, boolean fast) {
        this.kelas = kelas;
        this.viewModel = viewModel;
        this.fast = fast;
        retryTime = 0;
        loadData();
    }

    @MainThread
    public NetworkBoundResource(Class<RequestType> kelas, BaseObservableViewModel viewModel, boolean fast, int milis) {
        this.kelas = kelas;
        this.viewModel = viewModel;
        this.fast = fast;
        retryTime = 0;
        expiryInMilis = milis;
        loadData();
    }

    @MainThread
    public NetworkBoundResource(Class<RequestType> kelas, BaseObservableViewModel viewModel, int milis) {
        this.kelas = kelas;
        this.viewModel = viewModel;
        this.fast = fast;
        retryTime = 0;
        expiryInMilis = milis;
        loadData();
    }

    @MainThread
    public NetworkBoundResource(Type type, BaseObservableViewModel viewModel) {
        this.type = type;
        this.viewModel = viewModel;
        retryTime = 0;
        loadData();
    }

    protected void loadData() {
        Call<RequestType> call = createCall();
        LiveData<RequestType> dbSource = loadFromCache(call);
//        if (dbSource == null)
        viewModel.setLoading(true);

        if (fast)
            viewModel.setLoading(false);

        if (shouldFetch(dbSource.getValue())) {
            /*if(dbSource.getValue() != null)
                data.setValue(dbSource.getValue());*/
            fetchFromNetwork(dbSource, call);
        } else {
            data.setValue(dbSource.getValue());
            viewModel.setLoading(false);
        }
    }

    protected void loadDataRefresh() {
        Call<RequestType> call = createCall();
        LiveData<RequestType> dbSource = loadFromCache(call);
        fetchFromNetwork(dbSource, call);
    }

    private void fetchFromNetwork(final LiveData<RequestType> dbSource, Call<RequestType> call) {
        call.enqueue(new Callback<RequestType>() {
            @Override
            public void onResponse(Call<RequestType> call, Response<RequestType> response) {
                if (response.code() == 200) {
                    saveResultAndReInit(call, response.body());
                    if (MainApplication.DEBUG_MODE) {
                        Log.i("RESPONSE", "onResponse: " + response.body().toString());
                    }
                } else {
                    onFetchFailed();
                    data.setValue(dbSource.getValue());
                    if (MainApplication.DEBUG_MODE)
                        Log.i("RESPONSE", "onResponse: " + response.message());
                }
                viewModel.setLoading(false);
            }

            @Override
            public void onFailure(Call<RequestType> call, Throwable t) {
                t.printStackTrace();
                if (retryTime < 3) {
                    retryTime++;
                    if (MainApplication.DEBUG_MODE)
                        Log.i("RETRY TIME", "onFailure: " + retryTime);
                    loadDataRefresh();
                } else {
                    onFetchFailed();
                    data.setValue(dbSource.getValue());
                    viewModel.setLoading(false);
                }
            }
        });
    }

    @MainThread
    private void saveResultAndReInit(Call<RequestType> call, RequestType response) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                HttpUrl url = call.request().url();
                String key = url.encodedPath() + (url.encodedQuery() != null ? "?" + url.encodedQuery() : "");
                try {
                    final RequestBody copy = call.request().body();
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
                    Log.i("RESPONSE", "key source " + key);
                Prefs.remove(key);
                if (expiryInMilis > 0)
                    MainApplication.putToCache(key, new Gson().toJson(response), Calendar.MILLISECOND, expiryInMilis);
                else
                    MainApplication.putToCache(key, new Gson().toJson(response));
//                saveCallResult(call, response);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                data.setValue(response);
            }
        }.execute();
    }

    /*@WorkerThread
    protected abstract void saveCallResult(Call<RequestType> call, @NonNull RequestType item);*/

    @MainThread
    protected abstract boolean shouldFetch(@Nullable RequestType data);

    @NonNull
    @MainThread
    protected LiveData<RequestType> loadFromCache(Call<RequestType> call) {
        MutableLiveData<RequestType> requestTypeLiveData = new MutableLiveData<>();
        HttpUrl url = call.request().url();
        String key = url.encodedPath() + (url.encodedQuery() != null ? "?" + url.encodedQuery() : "");
        try {
            final RequestBody copy = call.request().body();
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
        String strCache = MainApplication.getFromCache(key);
        if (MainApplication.DEBUG_MODE)
            Log.i("RESPONSE", "key cache " + key);
        if (strCache != null) {
            try {
                RequestType data = new Gson().fromJson(strCache, kelas != null ? kelas : type);
                requestTypeLiveData.setValue(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (MainApplication.DEBUG_MODE)
            Log.i("RESPONSE", "loadFromCache: berhasil ambil url " + new Gson().toJson(strCache));
        return requestTypeLiveData;
    }

    ;

    @NonNull
    @MainThread
    protected abstract Call<RequestType> createCall();

    @MainThread
    protected abstract void onFetchFailed();

    public final LiveData<RequestType> getAsLiveData() {
        return data;
    }

    public void refreshData() {
        data.setValue(null);
        loadDataRefresh();
    }

}
