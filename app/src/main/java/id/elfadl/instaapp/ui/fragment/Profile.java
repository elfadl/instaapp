package id.elfadl.instaapp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import id.elfadl.instaapp.R;
import id.elfadl.instaapp.application.MainApplication;
import id.elfadl.instaapp.databinding.ProfileBinding;
import id.elfadl.instaapp.hepers.prefs.Prefs;
import id.elfadl.instaapp.ui.activity.CompleteProfile;
import id.elfadl.instaapp.ui.activity.MainActivity;
import id.elfadl.instaapp.ui.activity.SplashScreen;
import id.elfadl.instaapp.ui.adapter.MyPostAdapter;
import id.elfadl.instaapp.viewmodel.ProfileViewModel;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class Profile extends BaseFragment {

    private ProfileBinding binding;
    private ProfileViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.profile, container, false);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        binding.setViewmodel(viewModel);

        init();

        binding.btnEditProfile.setOnClickListener(v -> startActivity(new Intent(getContext(), CompleteProfile.class)));

        binding.btnSignout.setOnClickListener(v -> {
            MainApplication.putToCache("profile", "");
            Prefs.clear();
            startActivity(new Intent(getContext(), SplashScreen.class).addFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK));
        });

        return binding.getRoot();
    }

    private void init() {
        viewModel.getProfile(true).observe(getViewLifecycleOwner(), profileModel -> {
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
                        binding.recyclerMyPost.setLayoutManager(new GridLayoutManager(getContext(), 3));
                        binding.recyclerMyPost.setAdapter(adapter);
                    }
                }
            }
        });
    }
}
