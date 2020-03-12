package com.example.mohammed.ScanNSav;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class SavedProducts extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DBHelper dbHelper;
    SavedAdapter adapter;
    ArrayList<savedItem> array_list = new ArrayList<savedItem>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_products);


        dbHelper=new DBHelper(this);

        recyclerView=findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        array_list=dbHelper.getAllSubject();
        adapter=new SavedAdapter(array_list,SavedProducts.this);
        recyclerView.setAdapter(adapter);


    }
}
