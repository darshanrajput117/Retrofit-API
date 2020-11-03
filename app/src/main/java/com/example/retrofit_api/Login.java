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
public class Login extends AppCompatActivity {

    Globals globals;
    Gson gson;
    LoginModel loginModel;

    @BindView(R.id.et_uname)
    AppCompatEditText et_uname;
    @BindView(R.id.et_password)
    AppCompatEditText et_password;
    @BindView(R.id.btn_login)
    AppCompatButton btn_login;
    @BindView(R.id.action_register)
    AppCompatTextView action_register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        ButterKnife.bind(this);
        globals = (Globals) getApplicationContext();
        gson = new GsonBuilder().create();
        loginModel = new LoginModel();
    }

    @OnClick({R.id.btn_login, R.id.action_register})
    public void OnClickEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (LoginValidation()) {
                    userlogin();
                } else {
                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.action_register:
                startActivity(new Intent(Login.this, Registration.class));
                break;
        }
    }

    private void userlogin() {
        if (!ConnectionDetector.internetCheck(Login.this, true)) {
            return;
        }
        API_interface apiService = RetrofitClient.getClient("https://apis.biz-insights.com/api-biz-insights/api/").create(API_interface.class);
        JSONObject postData = new JSONObject();
        try {
            postData.put("username", et_uname.getText().toString().trim());
            postData.put("password", et_password.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Call<ResponseBody> call = apiService.getUser("validateLogin", RequestBody.create(MediaType.parse("application/json;"), (postData).toString()));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    loginModel = gson.fromJson(jsonObject.toString(), LoginModel.class);
                    globals.setLoginData(loginModel);
                    Logger.json(response.body().string());
                    if (globals.getLoginData() != null) {
                        startActivity(new Intent(Login.this, Userlist.class));
                        finish();
                    } else {
                        Toast.makeText(Login.this, "Something went wrong....", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                Logger.e(t.getMessage());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private boolean LoginValidation() {
        if (!new FormValidation().checkEmptyET(et_uname)) {
            if (!new FormValidation().checkEmptyET(et_password)) {
                if (new FormValidation().checkpassword(et_password)) {
                    //Toast.makeText(getApplicationContext(), "Successfully Login...", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    //Toast.makeText(getApplicationContext(), "Password must be 6 characters,1 Uppercase,1 Lowercase,1 Number.", Toast.LENGTH_SHORT).show();
                    et_password.requestFocus();
                    et_password.setError("Password must be 6 characters,1 Uppercase,1 Lowercase,1 Number.");
                    return false;
                }
            } else {
                //Toast.makeText(getApplicationContext(), "Password is empty!!", Toast.LENGTH_SHORT).show();
                et_password.requestFocus();
                et_password.setError("Password is empty!!");
                return false;
            }
        } else {
            //Toast.makeText(getApplicationContext(), "EmailId is emplty!!!", Toast.LENGTH_SHORT).show();
            et_uname.requestFocus();
            et_uname.setError("Username is empty!!!");
            return false;
        }
    }
}