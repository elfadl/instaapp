package id.elfadl.instaapp.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import id.elfadl.instaapp.R;
import id.elfadl.instaapp.databinding.SignupBinding;
import id.elfadl.instaapp.hepers.network.RequestObservableAPI;
import id.elfadl.instaapp.model.signup.SignUpBody;
import id.elfadl.instaapp.model.signup.SignUpModel;
import id.elfadl.instaapp.viewmodel.SignUpViewModel;
import retrofit2.Call;

public class SignUp extends BaseActivity{

    private SignupBinding binding;
    private SignUpViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        shouldChangeStatusBarTintToDark = true;
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.signup);
        viewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        binding.setViewmodel(viewModel);

        binding.btnSignup.setOnClickListener(v -> {
            if(validate()){
                SignUpBody signUpBody = new SignUpBody();
                signUpBody.setEmail(viewModel.getEmail());
                signUpBody.setUsername(viewModel.getUsername());
                signUpBody.setPassword(viewModel.getPassword());
                new RequestObservableAPI<SignUpModel>(this, SignUpModel.class, true) {
                    @Override
                    public void onResult(SignUpModel result) {
                        if(result != null){
                            if(result.getStatus() == 1){
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                builder.setTitle("Berhasil!");
                                builder.setMessage(result.getMessage());
                                builder.setPositiveButton("OK", (dialog, which) -> finish());
                                builder.show();
                            }
                        }
                    }

                    @Override
                    public Call<SignUpModel> createCall() {
                        return api.signUp(signUpBody);
                    }
                };
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Peringatan!");
                builder.setMessage("Username atau Password tidak boleh kosong!");
                builder.setPositiveButton("OK", null);
                builder.show();
            }
        });


    }

    private boolean validate(){
        if(viewModel.getEmail() != null && viewModel.getUsername() != null && viewModel.getPassword() != null && viewModel.getRepassword() != null){
            if(TextUtils.isEmpty(viewModel.getEmail()) && !Patterns.EMAIL_ADDRESS.matcher(viewModel.getEmail()).matches()){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Peringatan!");
                builder.setMessage("Email tidak valid!");
                builder.setPositiveButton("OK", null);
                builder.show();
                return false;
            }
            if(TextUtils.isEmpty(viewModel.getUsername())){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Peringatan!");
                builder.setMessage("Username tidak boleh kosong!");
                builder.setPositiveButton("OK", null);
                builder.show();
                return false;
            }
            if(TextUtils.isEmpty(viewModel.getPassword())){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Peringatan!");
                builder.setMessage("Password tidak boleh kosong!");
                builder.setPositiveButton("OK", null);
                builder.show();
                return false;
            }
            if(TextUtils.isEmpty(viewModel.getRepassword())){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Peringatan!");
                builder.setMessage("Retype Password tidak boleh kosong!");
                builder.setPositiveButton("OK", null);
                builder.show();
                return false;
            }
            if(!viewModel.getPassword().equals(viewModel.getRepassword())){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Peringatan!");
                builder.setMessage("Password dan Retype Password tidak cocok!");
                builder.setPositiveButton("OK", null);
                builder.show();
                return false;
            }
            return true;
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Peringatan!");
            builder.setMessage("Email, Username, Password atau Retype Password tidak boleh kosong!");
            builder.setPositiveButton("OK", null);
            builder.show();
            return false;
        }
    }

}
