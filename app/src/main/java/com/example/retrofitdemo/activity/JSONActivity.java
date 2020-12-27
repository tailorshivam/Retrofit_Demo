package com.example.retrofitdemo.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.retrofitdemo.Inteface.APIInterface;
import com.example.retrofitdemo.R;
import com.example.retrofitdemo.adapter.JSONAdapter;
import com.example.retrofitdemo.api.APIClient;
import com.example.retrofitdemo.databinding.ActivityJsonBinding;
import com.example.retrofitdemo.model.User;
import com.example.retrofitdemo.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JSONActivity extends AppCompatActivity {

    ActivityJsonBinding jsonBinding;
    APIInterface apiInterface;
    List<User> userList;
    JSONAdapter jsonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jsonBinding = DataBindingUtil.setContentView(this, R.layout.activity_json);

        init();
        getData();
        onClick();
    }

    private void init() {
        userList = new ArrayList<>();

        jsonBinding.rvUsers.setLayoutManager(new LinearLayoutManager(this));
        jsonAdapter = new JSONAdapter(this, userList);
        jsonBinding.rvUsers.setAdapter(jsonAdapter);
    }

    private void getData() {

        jsonBinding.rvUsers.setVisibility(View.GONE);
        jsonBinding.progress.setVisibility(View.VISIBLE);
        jsonBinding.btnRetry.setVisibility(View.GONE);

        if (Constants.isNetworkAvailable(this)) {
            try {
                apiInterface = APIClient.getClient().create(APIInterface.class);

                Call<ResponseBody> stringCall = apiInterface.getUser();
                stringCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String json = response.body().string();
                                JSONArray jsonarray = new JSONArray(json);
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                    Log.d("\nJSONObject: ", "" + jsonobject.get("name").toString());
                                }
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d("Exception: ", "response not success");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        call.cancel();
                    }
                });
                Call<List<User>> call = apiInterface.getUsers();
                 call.enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        if (response.body() != null) {
                            jsonBinding.rvUsers.setVisibility(View.VISIBLE);
                            jsonBinding.progress.setVisibility(View.GONE);
                            jsonBinding.btnRetry.setVisibility(View.GONE);
                            userList.addAll(response.body());
                            jsonAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        call.cancel();
                    }
                });
            } catch (Exception e) {
                jsonBinding.rvUsers.setVisibility(View.GONE);
                jsonBinding.progress.setVisibility(View.GONE);
                jsonBinding.btnRetry.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
        } else {
            jsonBinding.rvUsers.setVisibility(View.GONE);
            jsonBinding.progress.setVisibility(View.GONE);
            jsonBinding.btnRetry.setVisibility(View.VISIBLE);
        }
    }

    private void onClick() {
        jsonBinding.btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }
}