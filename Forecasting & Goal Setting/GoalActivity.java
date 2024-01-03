package com.example.foodcarboncalculator;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GoalActivity extends AppCompatActivity{

    TextView addGoal;
    Dialog popAdd;
    RecyclerView recyclerView;
    EditText editGoal;
    Button btnGoal;
    QuantityAdapter adapter;
    ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        popAdd = new Dialog(this);
        popUp();
        recyclerView = findViewById(R.id.goalRecyclerView);
        addGoal = findViewById(R.id.btnSetGoal);
        addGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAdd.show();
            }
        });
    }

    private void popUp(){
        popAdd.setContentView(R.layout.popup_goal);
        popAdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAdd.getWindow().getAttributes().gravity = Gravity.CENTER;

        btnGoal = popAdd.findViewById(R.id.btnGoal);
        editGoal = popAdd.findViewById(R.id.editGoal);

        //add goal
        btnGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = String.valueOf(editGoal.getText());
                arrayList.add(text);
                //arrayList = getQuantityData(text);
                setRecyclerView();
                popAdd.dismiss();
            }
        });
    }

    private void setRecyclerView() {
        Log.d("GoalActivity", "Updated data: " + arrayList.toString());

        //if(adapter == null){
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new QuantityAdapter(this,arrayList);
            recyclerView.setAdapter(adapter);
        //}else{
          //  adapter.updateData(arrayList);
        //}

    }

    //private ArrayList<String> getQuantityData(String text){
      //  arrayList.add(text);
        //return arrayList;
    //}
}