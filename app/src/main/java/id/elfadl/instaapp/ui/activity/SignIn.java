package id.elfadl.instaapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import id.elfadl.instaapp.R;
import id.elfadl.instaapp.application.MainApplication;
import id.elfadl.instaapp.hepers.network.RequestObservableAPI;
import id.elfadl.instaapp.model.signin.SignInBody;
import id.elfadl.instaapp.model.signin.SignInModel;
import id.elfadl.instaapp.viewmodel.SignInViewModel;
import id.elfadl.instaapp.databinding.SigninBinding;
import retrofit2.Call;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class SignIn extends BaseActivity {

    private SigninBinding binding;
    private SignInViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        shouldChangeStatusBarTintToDark = true;
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.signin);
        viewModel = new ViewModelProvider(this).get(SignInViewModel.class);
        binding.setViewmodel(viewModel);

        binding.btnSignin.setOnClickListener(v -> {
            if(validate()){
                SignInBody signInBody = new SignInBody();
                signInBody.setUsername(viewModel.getUsername());
                signInBody.setPassword(viewModel.getPassword());
                new RequestObservableAPI<SignInModel>(this, SignInModel.class, true) {
                    @Override
                    public void onResult(SignInModel result) {
                        if(result != null){
                            if(result.getStatus() == 1){
                                MainApplication.putToCache("auth_key", result.getData().getAuthKey(), true);
                                if(result.getData().getProfile() == null){
                                    startActivity(new Intent(SignIn.this, CompleteProfile.class).addFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK));
                                }else {
                                    if(result.getData().getProfile().getBio() == null){
                                        startActivity(new Intent(SignIn.this, CompleteProfile.class).addFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK));
                                    }else {
                                        MainApplication.putToCache("profile", new Gson().toJson(result.getData().getProfile()));
                                        startActivity(new Intent(SignIn.this, MainActivity.class).addFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK));
                                    }
                                }
//                                MainApplication.putToCache("nama", result.getData().getNama(), true);
//                                startActivity(new Intent(Login.this, MainActivity.class).addFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK));
                            }else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignIn.this);
                                builder.setTitle("Peringatan!");
                                builder.setMessage(result.getMessage());
                                builder.setPositiveButton("OK", null);
                                builder.show();
                            }
                        }else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignIn.this);
                            builder.setTitle("Peringatan!");
                            builder.setMessage("Terjadi ganggaun pada server");
                            builder.setPositiveButton("OK", null);
                            builder.show();
                        }
                    }

                    @Override
                    public Call<SignInModel> createCall() {
                        return api.signIn(signInBody);
                    }
                };
            }
        });

        binding.btnSignup.setOnClickListener(v -> startActivity(new Intent(this, SignUp.class)));

    }

    private boolean validate(){
        if(viewModel.getUsername() != null && viewModel.getPassword() != null){
            if(viewModel.getUsername().toString().length() > 0 && viewModel.getPassword().toString().length() > 0){
                return true;
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Peringatan!");
                builder.setMessage("Username atau Password tidak boleh kosong!");
                builder.setPositiveButton("OK", null);
                builder.show();
                return false;
            }
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Peringatan!");
            builder.setMessage("Username atau Password tidak boleh kosong!");
            builder.setPositiveButton("OK", null);
            builder.show();
            return false;
        }
    }
}
