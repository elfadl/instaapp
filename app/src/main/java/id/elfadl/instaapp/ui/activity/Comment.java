package id.elfadl.instaapp.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

import id.elfadl.instaapp.R;
import id.elfadl.instaapp.application.MainApplication;
import id.elfadl.instaapp.databinding.CommentBinding;
import id.elfadl.instaapp.hepers.HeadersUtil;
import id.elfadl.instaapp.hepers.network.RequestObservableAPI;
import id.elfadl.instaapp.model.comment.CommentBody;
import id.elfadl.instaapp.model.comment.CommentModel;
import id.elfadl.instaapp.model.feed.Feed;
import id.elfadl.instaapp.model.profile.Profile;
import id.elfadl.instaapp.ui.adapter.comment.CommentAdapter;
import id.elfadl.instaapp.viewmodel.CommentViewModel;
import id.elfadl.instaapp.viewmodel.factory.CommentViewModelFactory;
import retrofit2.Call;

public class Comment extends BaseActivity {

    private CommentBinding binding;
    private CommentViewModel viewModel;
    private CommentAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        shouldChangeStatusBarTintToDark = true;
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.comment);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        Feed feed = new Gson().fromJson(getIntent().getExtras().getString("data"), Feed.class);
        viewModel = new ViewModelProvider(this, new CommentViewModelFactory(feed.getId())).get(CommentViewModel.class);
        binding.setViewmodel(viewModel);
        Profile profile = new Gson().fromJson(MainApplication.getFromCache("profile"), Profile.class);
        Log.i("PROFILE", "onCreate: "+MainApplication.getFromCache("profile"));
        viewModel.setImgProfile(profile.getFoto());
        viewModel.setImgProfileTS(feed.getImageProfile());
        viewModel.setUsernameTS(feed.getUsername());
        viewModel.setCaptionTS(feed.getCaption());
        PrettyTime prettyTime = new PrettyTime();
        Date d = new Date(feed.getPostAt()*1000);
        viewModel.setPostAtTs(prettyTime.format(d));

        adapter = new CommentAdapter(feed);
        binding.recyclerComment.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerComment.setAdapter(adapter);

        init();

        binding.kirimKomentar.setOnClickListener(v -> {
            if(viewModel.getComment() != null) {
                if (!TextUtils.isEmpty(viewModel.getComment())) {
                    viewModel.setLoading(true);
                    CommentBody body = new CommentBody();
                    body.setIdPost(feed.getId());
                    body.setComment(viewModel.getComment());
                    new RequestObservableAPI<CommentModel>(viewModel, CommentModel.class, true, true) {
                        @Override
                        public void onResult(CommentModel result) {
                            if(result == null){
                                Toast.makeText(Comment.this, "Terjadi kesalahan dengan server", Toast.LENGTH_SHORT).show();
                            }else {
                                if(result.getStatus() != 1){
                                    Toast.makeText(Comment.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                                }else {
                                    viewModel.refresh();
                                }
                            }
                        }

                        @Override
                        public Call<CommentModel> createCall() {
                            return api.comment(HeadersUtil.getInstance().getHeaders(), body);
                        }
                    };
                    viewModel.setComment("");
                }
            }
        });

    }

    private void init() {
        viewModel.getCommentPagedList().observe(this, comments -> adapter.submitList(comments));
    }
}
