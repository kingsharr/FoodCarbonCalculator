package com.example.madtest1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.example.madtest1.R;

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    private int[] imageIds = new int[]{R.drawable.rubbishmad, R.drawable.compostmad, R.drawable.cookingmad};
    private String[] descriptions = new String[]{"Reduce food waste by using leftovers creatively",
            "Compost kitchen scraps and organic waste to reduce landfill methane emissions",
            "Cook homemade food using recipes that reduces the release of carbon"};

    public CustomPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.slide_layout, collection, false);

        ImageView imageView = layout.findViewById(R.id.imageView);
        //imageView.
        TextView textView = layout.findViewById(R.id.textView);

        imageView.setImageResource(imageIds[position]);
        textView.setText(descriptions[position]);

        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return imageIds.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
