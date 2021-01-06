package com.example.retrofitdemo.movie.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.retrofitdemo.R;
import com.example.retrofitdemo.movie.model.Poster;

import java.io.Serializable;
import java.util.List;

import static com.example.retrofitdemo.movie.utils.Constants.IMAGES_PATH;

public class MovieFragment extends Fragment {

    List<Poster> posterList;
    private int page;

    public static MovieFragment newInstance(int page, List<Poster> posterList) {
        MovieFragment movieFragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putSerializable("images", (Serializable) posterList);
        movieFragment.setArguments(args);
        return movieFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        posterList = (List<Poster>) getArguments().get("images");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        ImageView ivPoster = view.findViewById(R.id.ivPoster);
        Glide.with(getContext()).load(IMAGES_PATH + posterList.get(page).getFilePath()).into(ivPoster);
        return view;
    }
}
