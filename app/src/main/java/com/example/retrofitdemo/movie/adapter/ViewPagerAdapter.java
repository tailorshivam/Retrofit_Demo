package com.example.retrofitdemo.movie.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.retrofitdemo.movie.fragment.MovieFragment;
import com.example.retrofitdemo.movie.model.Poster;

import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    List<Poster> posterList;

    public ViewPagerAdapter(@NonNull FragmentManager fm, List<Poster> posterList) {
        super(fm);
        this.posterList = posterList;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return MovieFragment.newInstance(position, posterList);
    }

    @Override
    public int getCount() {
        return posterList.size();
    }
}
