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

public class CompleteProfileViewModel extends BaseObservableViewModel {

    @Bindable
    String nama, bio, noHp, imageProfile;

    @Bindable
    int gender;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
        notifyPropertyChanged(BR.nama);
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
        notifyPropertyChanged(BR.bio);
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
        notifyPropertyChanged(BR.noHp);
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
        notifyPropertyChanged(BR.imageProfile);
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
        notifyPropertyChanged(BR.gender);
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

}
