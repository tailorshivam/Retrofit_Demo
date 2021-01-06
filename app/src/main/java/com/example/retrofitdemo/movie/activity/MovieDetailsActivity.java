package com.example.retrofitdemo.movie.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.example.retrofitdemo.R;
import com.example.retrofitdemo.databinding.ActivityMovieDetailsBinding;
import com.example.retrofitdemo.movie.Interface.APIInterface;
import com.example.retrofitdemo.movie.adapter.ViewPagerAdapter;
import com.example.retrofitdemo.movie.api.APIClient;
import com.example.retrofitdemo.movie.model.MovieDetails;
import com.example.retrofitdemo.movie.model.MovieImages;
import com.example.retrofitdemo.movie.model.Poster;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.retrofitdemo.movie.utils.Constants.API_KEY;
import static com.example.retrofitdemo.movie.utils.Constants.IMAGES_PATH;
import static com.example.retrofitdemo.movie.utils.Constants.isNetworkAvailable;

public class MovieDetailsActivity extends AppCompatActivity {

    ActivityMovieDetailsBinding movieDetailsBinding;
    APIInterface apiInterface;
    String id;
    List<Poster> posterList;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);

        setUpToolbar();
        init();
        getData();
    }

    private void setUpToolbar() {
        setSupportActionBar(movieDetailsBinding.toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        movieDetailsBinding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init() {

        posterList = new ArrayList<>();
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), posterList);
        movieDetailsBinding.images.setAdapter(viewPagerAdapter);

        if (getIntent() != null && getIntent().getExtras() != null) {
            if (getIntent().getExtras().get("id") != null) {
                id = String.valueOf(getIntent().getExtras().getInt("id"));
            }
        }
    }

    private void getData() {

        movieDetailsBinding.rlDetails.setVisibility(View.GONE);
        movieDetailsBinding.progress.setVisibility(View.VISIBLE);
        movieDetailsBinding.btnRetry.setVisibility(View.GONE);

        if (isNetworkAvailable(this)) {
            try {
                apiInterface = APIClient.getClient().create(APIInterface.class);
                Call<MovieDetails> movieDetailsCall = apiInterface.getMovieDetails(id, API_KEY);
                movieDetailsCall.enqueue(new Callback<MovieDetails>() {
                    @Override
                    public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                        if (response.body() != null) {
                            MovieDetails movieDetails = response.body();
                            Glide.with(MovieDetailsActivity.this).load(IMAGES_PATH + movieDetails.getBackdropPath()).into(movieDetailsBinding.ivPoster);
                            movieDetailsBinding.toolBar.setTitle(movieDetails.getTitle());
                            movieDetailsBinding.txtReleaseDate.setText(String.format("Release Date: %s", movieDetails.getReleaseDate()));
                            movieDetailsBinding.txtOverview.setText(movieDetails.getOverview());
                            movieDetailsBinding.rateBar.setRating((float) (movieDetails.getVoteAverage() / 2));

                            movieDetailsBinding.rlDetails.setVisibility(View.VISIBLE);
                            movieDetailsBinding.progress.setVisibility(View.GONE);
                            movieDetailsBinding.btnRetry.setVisibility(View.GONE);

                            getImages();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieDetails> call, Throwable t) {
                        call.cancel();
                        movieDetailsBinding.rlDetails.setVisibility(View.GONE);
                        movieDetailsBinding.progress.setVisibility(View.GONE);
                        movieDetailsBinding.btnRetry.setVisibility(View.VISIBLE);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "No Internet connection", Toast.LENGTH_SHORT).show();
                movieDetailsBinding.rlDetails.setVisibility(View.GONE);
                movieDetailsBinding.progress.setVisibility(View.GONE);
                movieDetailsBinding.btnRetry.setVisibility(View.VISIBLE);
            }
        }
    }

    private void getImages() {
        try {
            Call<MovieImages> movieImagesCall = apiInterface.getMovieImages(id, API_KEY);
            movieImagesCall.enqueue(new Callback<MovieImages>() {
                @Override
                public void onResponse(Call<MovieImages> call, Response<MovieImages> response) {
                    if (response.body() != null) {
                        MovieImages movieImages = response.body();
                        if (movieImages != null) {
                            posterList.addAll(movieImages.getPosters());
                            viewPagerAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(Call<MovieImages> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}