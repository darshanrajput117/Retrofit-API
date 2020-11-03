package com.example.retrofit_api;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.retrofit_api.Utility.Globals;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_login)
    AppCompatButton btn_Login;
    @BindView(R.id.btn_register)
    AppCompatButton btn_registration;
    Globals globals;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        globals= (Globals) getApplicationContext();
        ButterKnife.bind(this);
        if (globals.getLoginData() != null) {
            startActivity(new Intent(MainActivity.this, Userlist.class));
            finish();
        }
    }

    @OnClick({R.id.btn_login, R.id.btn_register})
    public void OnclickAction(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                startActivity(new Intent(MainActivity.this,Login.class));
                finish();
                break;
            case R.id.btn_register:
                startActivity(new Intent(MainActivity.this,Registration.class));
                finish();
                break;
        }
    }
}