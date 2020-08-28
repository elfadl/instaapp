package id.elfadl.instaapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import id.elfadl.instaapp.R;
import id.elfadl.instaapp.application.MainApplication;
import id.elfadl.instaapp.databinding.ProfileBinding;
import id.elfadl.instaapp.hepers.HeadersUtil;
import id.elfadl.instaapp.hepers.network.RequestObservableAPI;
import id.elfadl.instaapp.hepers.prefs.Prefs;
import id.elfadl.instaapp.model.follower.FollowerBody;
import id.elfadl.instaapp.model.follower.FollowerModel;
import id.elfadl.instaapp.ui.adapter.MyPostAdapter;
import id.elfadl.instaapp.ui.fragment.BaseFragment;
import id.elfadl.instaapp.viewmodel.ProfileViewModel;
import retrofit2.Call;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ProfileMember extends BaseActivity {

    private ProfileBinding binding;
    private ProfileViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        shouldChangeStatusBarTintToDark = true;
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.profile);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        binding.setViewmodel(viewModel);

        init(getIntent().getExtras().getLong("id_user"));

        binding.btnSignout.setOnClickListener(v -> {
            MainApplication.putToCache("profile", "");
            Prefs.clear();
            startActivity(new Intent(this, SplashScreen.class).addFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK));
        });
    }

    private void init(Long idUser) {
        viewModel.getProfile(true, idUser).observe(this, profileModel -> {
            if(profileModel != null){
                if(profileModel.getStatus() == 1){
                    id.elfadl.instaapp.model.profile.Profile profile = profileModel.getProfile();
                    if(profile != null) {
                        viewModel.setUsername(profile.getUsername());
                        viewModel.setImgProfile(profile.getFoto());
                        viewModel.setName(profile.getNama());
                        viewModel.setBio(profile.getBio());
                        viewModel.setTotalComment(profile.getTotalComment());
                        viewModel.setTotalPost(profile.getTotalPost());
                        viewModel.setTotalFollower(profile.getTotalFollower());
                        viewModel.setTotalFollowing(profile.getTotalFollowing());
                        MyPostAdapter adapter = new MyPostAdapter(profile.getMyPost());
                        binding.recyclerMyPost.setLayoutManager(new GridLayoutManager(this, 3));
                        binding.recyclerMyPost.setAdapter(adapter);
                        binding.btnEditProfile.setText(profile.isFollowed() ? "Unfollow" : "Follow");
                        binding.btnEditProfile.setOnClickListener(v -> {
                            FollowerBody followerBody = new FollowerBody();
                            followerBody.setIdUser(idUser);
                            new RequestObservableAPI<FollowerModel>(this, FollowerModel.class, true, true) {
                                @Override
                                public void onResult(FollowerModel result) {
                                    if(result == null){
                                        Toast.makeText(ProfileMember.this, "Terjadi kesalahan dengan Server.", Toast.LENGTH_SHORT).show();
                                    }else {
                                        if(result.getStatus() != 1){
                                            Toast.makeText(ProfileMember.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                                        }else {
                                            init(idUser);
                                        }
                                    }
                                }

                                @Override
                                public Call<FollowerModel> createCall() {
                                    return api.follow(HeadersUtil.getInstance().getHeaders(), followerBody);
                                }
                            };
                        });
                    }
                }
            }
        });
    }
}
