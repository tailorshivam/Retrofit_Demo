package com.example.retrofitdemo.Inteface;

import com.example.retrofitdemo.model.Todos;
import com.example.retrofitdemo.model.User;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIInterface {

    @GET("todos")
    Call<List<Todos>> getTodos();

    @GET("users")
    Call<List<User>> getUsers();

    @GET("users")
    Call<ResponseBody> getUser();

    @POST("/api/users")
    Call<com.example.retrofitdemo.postmodel.User> createUser(@Body com.example.retrofitdemo.postmodel.User user);
}
