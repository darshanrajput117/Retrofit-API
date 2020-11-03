package com.example.retrofit_api;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retrofit_api.APIS.API_interface;
import com.example.retrofit_api.APIS.RetrofitClient;
import com.example.retrofit_api.Utility.Globals;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

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
public class Userlist extends AppCompatActivity {

    Globals globals;
    UserAdapter userAdapter;
    User dataModel;
    Gson gson;
    @BindView(R.id.toolbar_title)
    Toolbar toolbar;
    @BindView(R.id.layout_recylcerview)
    RecyclerView recyclerView;
    @BindView(R.id.ib_logout)
    AppCompatImageButton ib_logout;
    @BindView(R.id.iv_view)
    AppCompatImageView iv_view;
    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);

        init();
        switchlistview();
    }

    private void init() {
        globals = (Globals) getApplicationContext();
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        gridLayoutManager = new GridLayoutManager(Userlist.this, 2);
        gson = new GsonBuilder().create();
        setData();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void switchlistview() {
        if (globals.getGridValue() == 0) {
            gridLayoutManager.setSpanCount(2);
            iv_view.setImageDrawable(getResources().getDrawable(R.drawable.ic_listview));
        } else if (globals.getGridValue() == 1) {
            gridLayoutManager.setSpanCount(1);
            iv_view.setImageDrawable(getResources().getDrawable(R.drawable.ic_gridview));
        } else {
            gridLayoutManager.setSpanCount(1);
        }
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(userAdapter);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @OnClick({R.id.ib_logout, R.id.iv_view})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.ib_logout:
                setLogout();
                break;
            case R.id.iv_view:
                if (globals.getGridValue() == 0) {
                    globals.setGridValue(1);
                } else if (globals.getGridValue() == 1) {
                    globals.setGridValue(0);
                }
                if (dataModel != null && dataModel.data.rows.size() > 0) {
                    switchlistview();
                }
                break;
        }
    }

    private void setLogout() {
        new AlertDialog.Builder(com.example.retrofit_api.Userlist.this).setTitle("Logout").setMessage("Are Sure Want To Logout?")
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()).setPositiveButton("Logout", (dialog, which) -> {
            globals.setLoginData(null);
            Intent i = new Intent(com.example.retrofit_api.Userlist.this, MainActivity.class);
            startActivity(i);
            finish();
        }).show();
    }

    private void setData() {
        API_interface api_interface = RetrofitClient.getClient(getString(R.string.base_url)).create(API_interface.class);
        JSONObject params = new JSONObject();
        try {
            params.put("page_no", 1);
            params.put("page_record", 50);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Call<ResponseBody> call = api_interface.getUser(getString(R.string.getUsersist),
                RequestBody.create(MediaType.parse("applocation/json;"), (params).toString()));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String data = response.body().string();
                        Logger.json(data);
                        JSONObject object;
                        object = new JSONObject(data);
                        dataModel = gson.fromJson(String.valueOf(object), User.class);
                        userAdapter = new UserAdapter(Userlist.this,dataModel.data.rows);
                        recyclerView.setLayoutManager(gridLayoutManager);
                        recyclerView.setAdapter(userAdapter);
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Logger.e(Objects.requireNonNull(t.getMessage()));
            }
        });
    }
}