package com.example.foodcarboncalculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//for adding goals
public class QuantityAdapter extends RecyclerView.Adapter<QuantityAdapter.ViewHolder>{

    View view;
    Context context;
    ArrayList<String> arrayList;

    public QuantityAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public void updateData(ArrayList<String> newData) {
        this.arrayList = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.goal_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(arrayList != null && arrayList.size() > 0){
            holder.check_box.setText(arrayList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox check_box;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            check_box = itemView.findViewById(R.id.checkBoxGoal);
        }
    }

}
