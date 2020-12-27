package com.example.retrofitdemo.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.retrofitdemo.Inteface.APIInterface;
import com.example.retrofitdemo.R;
import com.example.retrofitdemo.api.APIClient;
import com.example.retrofitdemo.databinding.ActivityPostapiBinding;
import com.example.retrofitdemo.postmodel.User;
import com.example.retrofitdemo.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAPIActivity extends AppCompatActivity {

    ActivityPostapiBinding postBinding;
    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postBinding = DataBindingUtil.setContentView(this, R.layout.activity_postapi);

        if(Constants.isNetworkAvailable(this)){
            apiInterface = APIClient.getClientPost().create(APIInterface.class);
            User user = new User("Shivam", "Android Developer");
            Call<User> userCall = apiInterface.createUser(user);
            userCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User user1 = response.body();
                    postBinding.txtId.setText(user1.id);
                    postBinding.txtName.setText(user1.name);
                    postBinding.txtJob.setText(user1.job);
                    postBinding.txtCreatedAt.setText(user1.createdAt);
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    call.cancel();
                }
            });    
        }else {
            Toast.makeText(this, "Please Connect Interet.", Toast.LENGTH_SHORT).show();
        }
        
    }
}