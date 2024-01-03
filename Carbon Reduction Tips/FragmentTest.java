package com.example.madtest1;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentTest extends Fragment {

    public FragmentTest() {
        // Required empty public constructor
    }
//
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_layout, container, false);

        // Check if the container is already created
        if (savedInstanceState == null) {
            // Perform the fragment transaction to add the Slider fragment
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.midContent, new Slider())
                    .commit();
        }

        return view;
    }

}
