package com.example.madtest1;//package com.example.slider;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.madtest1.R;

public class FragmentTry extends Fragment {

    public FragmentTry() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.page_layout, container, false);
    }

    // Here you can add other methods if needed, such as initializing the slider
}