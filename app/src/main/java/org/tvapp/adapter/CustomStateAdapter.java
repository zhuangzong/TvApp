package org.tvapp.adapter;


import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import java.util.List;

public class CustomStateAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> fragments;
    private final List<String> titles;


    public CustomStateAdapter(@NonNull FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        this.fragments=fragments;
        this.titles=titles;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments==null?0:fragments.size();
    }

}
