package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class fetchData extends AppCompatActivity {
RecyclerView recyclerView;
MyDB myDB;

ArrayList<model> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_data);
        myDB = new MyDB(getApplicationContext());
        setTitle("data base");
        arrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.recView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Cursor cursor = myDB.readData();
        while (cursor.moveToNext())
        {
            arrayList.add(new model(cursor.getString(1),cursor.getString(2)));
        }
        myadapter myadapter=new myadapter(arrayList,getApplicationContext());
        recyclerView.setAdapter(myadapter);
    }
}