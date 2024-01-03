package com.example.madtest1;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.madtest1.CustomPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Slider extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_slider, container, false);

        ViewPager viewPager = view.findViewById(R.id.view_pager);
        TabLayout tabLayout = view.findViewById(R.id.tabDots);
        CustomPagerAdapter adapter = new CustomPagerAdapter(getActivity());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager, true);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) tab.setIcon(R.drawable.tab_dot);
        }

        return view;
    }
}
