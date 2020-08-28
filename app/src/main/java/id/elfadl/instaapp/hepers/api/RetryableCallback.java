package id.elfadl.instaapp.hepers.api;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kamal on 1/31/18.
 */

public abstract class RetryableCallback<T> implements Callback<T> {

    private int totalRetries = 0;
    private static final String TAG = RetryableCallback.class.getSimpleName();
    private final Call<T> call;
    private int retryCount = 0;

    public RetryableCallback(Call<T> call, int totalRetries) {
        this.call = call;
        this.totalRetries = totalRetries;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (!ApiHelper.isCallSuccess(response))
            if (retryCount++ < totalRetries) {
                Log.v(TAG, "Retrying API Call -  (" + retryCount + " / " + totalRetries + ")");
                retry();
            } else
                onFinalResponse(call, response);
        else
            onFinalResponse(call,response);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.e(TAG, "> "+t.getMessage());
        if (retryCount++ < totalRetries) {
            Log.v(TAG, "Retrying API Call -  (" + retryCount + " / " + totalRetries + ")");
            retry();
        } else
            onFinalFailure(call, t);
    }

    public abstract void onFinalResponse(Call<T> call, Response<T> response);

    public abstract void onFinalFailure(Call<T> call, Throwable t);

    private void retry() {
        Call<T> callback = call.clone();
        call.cancel();
        callback.enqueue(this);
    }

}
