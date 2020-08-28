package id.elfadl.instaapp.ui.adapter.feed;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Map;

import id.elfadl.instaapp.hepers.HeadersUtil;
import id.elfadl.instaapp.hepers.api.RetrofitApi;
import id.elfadl.instaapp.hepers.api.RetrofitApiSingleton;
import id.elfadl.instaapp.hepers.network.RequestObservableAPI;
import id.elfadl.instaapp.model.feed.Feed;
import id.elfadl.instaapp.model.feed.FeedModel;
import id.elfadl.instaapp.viewmodel.BaseObservableViewModel;
import retrofit2.Call;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class FeedDataSource extends androidx.paging.PageKeyedDataSource<Integer, Feed> {

    private RetrofitApi api;
    private BaseObservableViewModel viewModel;

    public FeedDataSource(BaseObservableViewModel viewModel) {
        this.viewModel = viewModel;
        api = RetrofitApiSingleton.getInstance().getApi();
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Feed> callback) {
        new RequestObservableAPI<FeedModel>(viewModel, FeedModel.class, true, false) {
            @Override
            public void onResult(FeedModel result) {
                if (result.getStatus() == 1) {
                    callback.onResult(result.getFeed(), null, result.getPage() == 0 ? null : (result.getPage() < result.getTotalPage() ? result.getPage() + 1 : null));
                } else {
                    callback.onResult(new ArrayList<>(), null, null);
                    error(result);
                }
            }

            @Override
            public Call<FeedModel> createCall() {
                return api.getFeed(HeadersUtil.getInstance().getHeaders(), 1);
            }
        };
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Feed> callback) {
        new RequestObservableAPI<FeedModel>(viewModel, FeedModel.class, true, true) {
            @Override
            public void onResult(FeedModel result) {
                Integer adjacentKey = params.key > 1 ? params.key - 1 : null;
                if (result != null) {
                    if (result.getStatus() == 1)
                        callback.onResult(result.getFeed(), adjacentKey);
                    else {
                        callback.onResult(new ArrayList<>(), null);
                        error(result);
                    }
                } else {
                    callback.onResult(new ArrayList<>(), null);
                    error(result);
                }
            }

            @Override
            public Call<FeedModel> createCall() {
                return api.getFeed(HeadersUtil.getInstance().getHeaders(), params.key);
            }
        };
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Feed> callback) {
        new RequestObservableAPI<FeedModel>(viewModel, FeedModel.class, true, true) {
            @Override
            public void onResult(FeedModel result) {
                if (result != null) {
                    if (result.getStatus() == 1) {
                        Integer key = params.key < result.getTotalPage() ? params.key + 1 : null;
                        callback.onResult(result.getFeed(), key);
                    } else {
                        callback.onResult(new ArrayList<>(), null);
                        error(result);
                    }
                } else {
                    callback.onResult(new ArrayList<>(), null);
                    error(result);
                }
            }

            @Override
            public Call<FeedModel> createCall() {
                return api.getFeed(HeadersUtil.getInstance().getHeaders(), params.key);
            }
        };
    }

    private void error(FeedModel result) {
        viewModel.setEmpty(true);
    }

}
