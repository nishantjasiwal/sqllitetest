package com.example.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class myadapter extends RecyclerView.Adapter<myadapter.holder>{
ArrayList<model> arrayList;
Context context;
MyDB myDB;



    public myadapter(ArrayList<model> arrayList, Context context) {
    this.arrayList=arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_alert,null);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
    holder.name.setText(arrayList.get(position).getName());
    holder.email.setText(arrayList.get(position).getEmail());
    holder.name.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

           String name=arrayList.get(position).getName();
         MyDB myDB=new MyDB(context);

         myDB.deleteColomByName(name);
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(context,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        }
    });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    class holder extends RecyclerView.ViewHolder {
        TextView name,email;
        public holder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.nameTextView);
           email=itemView.findViewById(R.id.emailTextView);

        }
    }
}
