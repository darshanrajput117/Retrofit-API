package com.example.retrofit_api.APIS;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface API_interface {

    @Headers("Content-Type:application/json")
    @POST
    Call<ResponseBody> getUser(@Url String base_url, @Body RequestBody requestBody);
}
