package id.elfadl.instaapp.ui.adapter.comment;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import id.elfadl.instaapp.hepers.HeadersUtil;
import id.elfadl.instaapp.hepers.api.RetrofitApi;
import id.elfadl.instaapp.hepers.api.RetrofitApiSingleton;
import id.elfadl.instaapp.hepers.network.RequestObservableAPI;
import id.elfadl.instaapp.model.comment.Comment;
import id.elfadl.instaapp.model.comment.CommentModel;
import id.elfadl.instaapp.model.feed.Feed;
import id.elfadl.instaapp.model.feed.FeedModel;
import id.elfadl.instaapp.viewmodel.BaseObservableViewModel;
import retrofit2.Call;

public class CommentDataSource extends androidx.paging.PageKeyedDataSource<Integer, Comment> {

    private RetrofitApi api;
    private BaseObservableViewModel viewModel;
    private Long idPost;

    public CommentDataSource(BaseObservableViewModel viewModel, Long idPost) {
        this.viewModel = viewModel;
        api = RetrofitApiSingleton.getInstance().getApi();
        this.idPost = idPost;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Comment> callback) {
        new RequestObservableAPI<CommentModel>(viewModel, CommentModel.class, true, false) {
            @Override
            public void onResult(CommentModel result) {
                if (result.getStatus() == 1) {
                    callback.onResult(result.getComment(), null, result.getPage() == 0 ? null : (result.getPage() < result.getTotalPage() ? result.getPage() + 1 : null));
                    viewModel.setEmpty(result.getComment().size() == 0);
                } else {
                    callback.onResult(new ArrayList<>(), null, null);
                    error(result);
                }
            }

            @Override
            public Call<CommentModel> createCall() {
                return api.getComment(HeadersUtil.getInstance().getHeaders(), 1, idPost);
            }
        };
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Comment> callback) {
        new RequestObservableAPI<CommentModel>(viewModel, CommentModel.class, true, true) {
            @Override
            public void onResult(CommentModel result) {
                Integer adjacentKey = params.key > 1 ? params.key - 1 : null;
                if (result != null) {
                    if (result.getStatus() == 1) {
                        callback.onResult(result.getComment(), adjacentKey);
                        viewModel.setEmpty(result.getComment().size() == 0);
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
            public Call<CommentModel> createCall() {
                return api.getComment(HeadersUtil.getInstance().getHeaders(), params.key, idPost);
            }
        };
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Comment> callback) {
        new RequestObservableAPI<CommentModel>(viewModel, CommentModel.class, true, true) {
            @Override
            public void onResult(CommentModel result) {
                if (result != null) {
                    if (result.getStatus() == 1) {
                        Integer key = params.key < result.getTotalPage() ? params.key + 1 : null;
                        callback.onResult(result.getComment(), key);
                        viewModel.setEmpty(result.getComment().size() == 0);
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
            public Call<CommentModel> createCall() {
                return api.getComment(HeadersUtil.getInstance().getHeaders(), params.key, idPost);
            }
        };
    }

    private void error(CommentModel result) {
        viewModel.setEmpty(true);
    }

}
