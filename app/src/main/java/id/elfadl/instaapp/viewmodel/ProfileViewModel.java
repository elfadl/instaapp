package id.elfadl.instaapp.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.LiveData;

import id.elfadl.instaapp.hepers.HeadersUtil;
import id.elfadl.instaapp.hepers.api.RetrofitApiSingleton;
import id.elfadl.instaapp.hepers.network.NetworkBoundResource;
import id.elfadl.instaapp.model.profile.ProfileModel;
import retrofit2.Call;

public class ProfileViewModel extends BaseObservableViewModel {

    @Bindable
    String username, imgProfile, name, bio, totalPost, totalComment, totalFollower, totalFollowing;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    public String getImgProfile() {
        return imgProfile;
    }

    public void setImgProfile(String imgProfile) {
        this.imgProfile = imgProfile;
        notifyPropertyChanged(BR.imgProfile);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
        notifyPropertyChanged(BR.bio);
    }

    public String getTotalPost() {
        return totalPost;
    }

    public void setTotalPost(String totalPost) {
        this.totalPost = totalPost;
        notifyPropertyChanged(BR.totalPost);
    }

    public String getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(String totalComment) {
        this.totalComment = totalComment;
        notifyPropertyChanged(BR.totalComment);
    }

    public String getTotalFollower() {
        return totalFollower;
    }

    public void setTotalFollower(String totalFollower) {
        this.totalFollower = totalFollower;
        notifyPropertyChanged(BR.totalFollower);
    }

    public String getTotalFollowing() {
        return totalFollowing;
    }

    public void setTotalFollowing(String totalFollowing) {
        this.totalFollowing = totalFollowing;
        notifyPropertyChanged(BR.totalFollowing);
    }

    public LiveData<ProfileModel> getProfile(){
        return getProfile(false);
    }

    public LiveData<ProfileModel> getProfile(boolean refresh){
        return new NetworkBoundResource<ProfileModel>(ProfileModel.class, this) {

            @Override
            protected boolean shouldFetch(@Nullable ProfileModel data) {
                return data == null || refresh;
            }

            @NonNull
            @Override
            protected Call<ProfileModel> createCall() {
                return RetrofitApiSingleton.getInstance().getApi().getProfile(HeadersUtil.getInstance().getHeaders());
            }


            @Override
            protected void onFetchFailed() {
                Log.i("NETWORK", "onFetchFailed: GAGAL");
            }
        }.getAsLiveData();
    }

    public LiveData<ProfileModel> getProfile(boolean refresh, Long idUser){
        return new NetworkBoundResource<ProfileModel>(ProfileModel.class, this) {

            @Override
            protected boolean shouldFetch(@Nullable ProfileModel data) {
                return data == null || refresh;
            }

            @NonNull
            @Override
            protected Call<ProfileModel> createCall() {
                return RetrofitApiSingleton.getInstance().getApi().getProfile(HeadersUtil.getInstance().getHeaders(), idUser);
            }


            @Override
            protected void onFetchFailed() {
                Log.i("NETWORK", "onFetchFailed: GAGAL");
            }
        }.getAsLiveData();
    }

}
