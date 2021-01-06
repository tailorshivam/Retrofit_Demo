package com.example.retrofitdemo.movie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.retrofitdemo.R;
import com.example.retrofitdemo.databinding.ActivityMovieListBinding;
import com.example.retrofitdemo.movie.Interface.APIInterface;
import com.example.retrofitdemo.movie.Interface.ItemClickListener;
import com.example.retrofitdemo.movie.adapter.MovieAdapter;
import com.example.retrofitdemo.movie.api.APIClient;
import com.example.retrofitdemo.movie.model.Movie;
import com.example.retrofitdemo.movie.model.Result;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.retrofitdemo.movie.utils.Constants.API_KEY;
import static com.example.retrofitdemo.movie.utils.Constants.isNetworkAvailable;

public class MovieListActivity extends AppCompatActivity {

    ActivityMovieListBinding movieListBinding;
    APIInterface apiInterface;
    List<Result> resultList;
    MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieListBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_list);

        setUpToolbar();
        init();
        getData();
    }

    private void setUpToolbar() {
        setSupportActionBar(movieListBinding.toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    private void init() {
        resultList = new ArrayList<>();

        movieListBinding.rvMovie.setLayoutManager(new LinearLayoutManager(this));
        movieAdapter = new MovieAdapter(this, resultList, new ItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MovieListActivity.this, MovieDetailsActivity.class);
                intent.putExtra("id", resultList.get(position).getId());
                startActivity(intent);
//                Toast.makeText(MovieListActivity.this, "" + resultList.get(position).getBackdropPath(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(MovieListActivity.this, "" + resultList.get(position).getId(), Toast.LENGTH_SHORT).show();
            }
        });
        movieListBinding.rvMovie.setAdapter(movieAdapter);
    }

    private void getData() {
        movieListBinding.rvMovie.setVisibility(View.GONE);
        movieListBinding.progress.setVisibility(View.VISIBLE);
        movieListBinding.btnRetry.setVisibility(View.GONE);

        if (isNetworkAvailable(this)) {
            try {
                apiInterface = APIClient.getClient().create(APIInterface.class);
                Call<Movie> movieCall = apiInterface.getMovieList(API_KEY);
                movieCall.enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        if (response.body() != null) {
                            movieListBinding.rvMovie.setVisibility(View.VISIBLE);
                            movieListBinding.progress.setVisibility(View.GONE);
                            movieListBinding.btnRetry.setVisibility(View.GONE);
                            Movie movie = response.body();
                         /*   if (movie != null) {
                                Toast.makeText(MovieListActivity.this, "" + movie.getTotalResults(), Toast.LENGTH_SHORT).show();
//                                Toast.makeText(MovieListActivity.this, ""+movie.getResults().size(), Toast.LENGTH_SHORT).show();
                            }*/
                            resultList.addAll(movie.getResults());
                            movieAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<Movie> call, Throwable t) {
                        call.cancel();
                        Log.e("Movie", t.getMessage());
                    }
                });
            } catch (Exception e) {
                movieListBinding.rvMovie.setVisibility(View.GONE);
                movieListBinding.progress.setVisibility(View.GONE);
                movieListBinding.btnRetry.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
        } else {
            movieListBinding.rvMovie.setVisibility(View.GONE);
            movieListBinding.progress.setVisibility(View.GONE);
            movieListBinding.btnRetry.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Please Connect Internet", Toast.LENGTH_SHORT).show();
        }
    }
}