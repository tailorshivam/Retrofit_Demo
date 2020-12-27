package com.example.retrofitdemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.retrofitdemo.Inteface.APIInterface;
import com.example.retrofitdemo.R;
import com.example.retrofitdemo.adapter.SimpleAPIAdapter;
import com.example.retrofitdemo.api.APIClient;
import com.example.retrofitdemo.databinding.ActivitySimpleBinding;
import com.example.retrofitdemo.model.Todos;
import com.example.retrofitdemo.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SimpleActivity extends AppCompatActivity {

    ActivitySimpleBinding simpleBinding;
    APIInterface apiInterface;
    List<Todos> todosList;
    SimpleAPIAdapter simpleAPIAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        simpleBinding = DataBindingUtil.setContentView(this, R.layout.activity_simple);

        init();
        getData();
        onClick();
    }

    private void init() {
        todosList = new ArrayList<>();

        simpleBinding.rvPosts.setLayoutManager(new LinearLayoutManager(this));
        simpleAPIAdapter = new SimpleAPIAdapter(this, todosList);
        simpleBinding.rvPosts.setAdapter(simpleAPIAdapter);
    }

    private void getData() {

        simpleBinding.rvPosts.setVisibility(View.GONE);
        simpleBinding.progress.setVisibility(View.VISIBLE);
        simpleBinding.btnRetry.setVisibility(View.GONE);

        if (Constants.isNetworkAvailable(this)) {
            try {
                apiInterface = APIClient.getClient().create(APIInterface.class);
                Call<List<Todos>> userCall = apiInterface.getTodos();
                userCall.enqueue(new Callback<List<Todos>>() {

                    @Override
                    public void onResponse(Call<List<Todos>> call, Response<List<Todos>> response) {
                        if (response.body() != null) {
                            simpleBinding.rvPosts.setVisibility(View.VISIBLE);
                            simpleBinding.progress.setVisibility(View.GONE);
                            simpleBinding.btnRetry.setVisibility(View.GONE);
                            todosList.addAll(response.body());
                            simpleAPIAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Todos>> call, Throwable t) {
                        call.cancel();
                    }
                });
            } catch (Exception e) {
                simpleBinding.rvPosts.setVisibility(View.GONE);
                simpleBinding.progress.setVisibility(View.GONE);
                simpleBinding.btnRetry.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
        } else {
            simpleBinding.rvPosts.setVisibility(View.GONE);
            simpleBinding.progress.setVisibility(View.GONE);
            simpleBinding.btnRetry.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Please Connect Internet", Toast.LENGTH_SHORT).show();
        }

    }

    private void onClick() {
        simpleBinding.btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }
}