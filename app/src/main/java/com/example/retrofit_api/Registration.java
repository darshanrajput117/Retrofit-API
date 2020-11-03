package com.example.retrofit_api;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.retrofit_api.APIS.API_interface;
import com.example.retrofit_api.APIS.ConnectionDetector;
import com.example.retrofit_api.APIS.RetrofitClient;
import com.example.retrofit_api.Model.LoginModel;
import com.example.retrofit_api.Utility.Globals;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("NonConstantResourceId")
public class Registration extends AppCompatActivity {

    Globals globals;
    LoginModel loginModel;
    Gson gson;

    @BindView(R.id.et_fname)
    AppCompatEditText et_fname;
    @BindView(R.id.et_lname)
    AppCompatEditText et_lname;
    @BindView(R.id.et_address)
    AppCompatEditText et_address;
    @BindView(R.id.et_emailid)
    AppCompatEditText et_emailid;
    @BindView(R.id.et_password)
    AppCompatEditText et_password;
    @BindView(R.id.et_cpassword)
    AppCompatEditText et_cpassword;
    @BindView(R.id.et_mobileno)
    AppCompatEditText et_mobileno;
    @BindView(R.id.btn_register)
    AppCompatButton btn_register;
    @BindView(R.id.action_login)
    AppCompatTextView action_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        ButterKnife.bind(this);
        globals= (Globals) getApplicationContext();
        loginModel=new LoginModel();
        gson=new GsonBuilder().create();
    }

    @OnClick({R.id.btn_register, R.id.action_login})
    public void OnclickRegister(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                if(RegistrationValidation()){
                    userRegistration();
                }else{
                    Toast.makeText(Registration.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.action_login:
                startActivity(new Intent(Registration.this,Login.class));
                break;
        }
    }

    private void userRegistration() {
        if (!ConnectionDetector.internetCheck(Registration.this, true)) {
            return;
        }
        API_interface apiService = RetrofitClient.getClient("https://apis.biz-insights.com/api-biz-insights/api/").create(API_interface.class);
        JSONObject postData = new JSONObject();
        try {
            postData.put("first_name",et_fname.getText().toString().trim());
            postData.put("last_name",et_lname.getText().toString().trim());
            postData.put("email_id",et_emailid.getText().toString().trim());
            postData.put("mobile_no",et_mobileno.getText().toString().trim());
            postData.put("address",et_address.getText().toString().trim());
            postData.put("password",et_password.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Call<ResponseBody> call = apiService.getUser(getString(R.string.register), RequestBody.create(MediaType.parse("application/json;"), (postData).toString()));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,@NonNull Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    loginModel=gson.fromJson(String.valueOf(jsonObject),LoginModel.class);
                    if("Registered successfully.".equals(loginModel.msg)){
                        Toast.makeText(Registration.this, "Login successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Registration.this,Userlist.class));
                        Logger.json(response.body().string());
                        finish();
                    }else{
                        Toast.makeText(Registration.this, loginModel.msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call,@NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                Logger.e(t.getMessage());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private boolean RegistrationValidation() {
        if (!new FormValidation().checkEmptyET(et_fname)) {
            if (new FormValidation().checkname(et_fname)) {
                if (!new FormValidation().checkEmptyET(et_fname)) {
                    if (new FormValidation().checkname(et_fname)) {
                        if (!new FormValidation().checkEmptyET(et_emailid)) {
                            if (new FormValidation().checkemailid(et_emailid)) {
                                if (!new FormValidation().checkEmptyET(et_password)) {
                                    if (new FormValidation().checkpassword(et_password)) {
                                        if (!new FormValidation().checkEmptyET(et_cpassword)) {
                                            if (new FormValidation().checkconfirmpassword(et_cpassword, et_password)) {
                                                if (!new FormValidation().checkEmptyET(et_mobileno))
                                                    if (new FormValidation().checkMobileNumber(et_mobileno)) {
                                                        //Toast.makeText(getApplicationContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
                                                        return true;
                                                    } else {
                                                        et_mobileno.requestFocus();
                                                        et_mobileno.setError("Mobile no must be 10 digits");
                                                        return false;
                                                    }
                                                else {
                                                    et_mobileno.requestFocus();
                                                    et_mobileno.setError("Mobile No is empty.");
                                                    return false;
                                                }
                                            } else {
                                                et_cpassword.requestFocus();
                                                et_cpassword.setError("Password and Confirm Password are not matched!!");
                                                return false;
                                            }
                                        } else {
                                            et_cpassword.requestFocus();
                                            et_cpassword.setError("Confirm Password is empty.");
                                            return false;
                                        }
                                    } else {
                                        et_password.requestFocus();
                                        et_password.setError("Password must be 6 characters,1 Uppercase,1 Lowercase,1 Number.");
                                        return false;
                                    }
                                } else {
                                    et_password.requestFocus();
                                    et_password.setError("Password is empty.");
                                    return false;
                                }
                            } else {
                                et_emailid.requestFocus();
                                et_emailid.setError("Emailid must be xyz@abc.xyz");
                                return false;
                            }
                        } else {
                            et_emailid.requestFocus();
                            et_emailid.setError("EmailId is empty.");
                            return false;
                        }
                    } else {
                        et_lname.requestFocus();
                        et_lname.setError("Last Name must be xyz not any inputed digit or speacial characters.");
                        return false;
                    }
                } else {
                    et_lname.requestFocus();
                    et_lname.setError("Last Name is empty.");
                    return false;
                }
            } else {
                et_fname.requestFocus();
                et_fname.setError("First Name must be xyz not any inputed digit or speacial characters.");
                return false;
            }
        } else {
            et_fname.requestFocus();
            et_fname.setError("First Name is empty.");
            return false;
        }
    }
}