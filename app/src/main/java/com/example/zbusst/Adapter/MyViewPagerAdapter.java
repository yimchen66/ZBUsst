package com.example.zbusst.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyViewPagerAdapter extends FragmentStateAdapter {

    List<Fragment> list_fragment = new ArrayList<>();

    public MyViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,List<Fragment> list) {
        super(fragmentManager, lifecycle);
        this.list_fragment = list;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return list_fragment.get(position);
    }

    @Override
    public int getItemCount() {
        return list_fragment.size();
    }
}
