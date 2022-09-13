package com.example.login.Receiver;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.login.MainActivity;
import com.example.login.R;

public class Receiver extends BroadcastReceiver {

    Dialog dialog;
    ConnectivityManager connectivityManager;
    NetworkInfo networkinfo;
    String status;


    @Override
    public void onReceive(Context context, Intent intent) {
       connectivityManager =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
      networkinfo= connectivityManager.getActiveNetworkInfo();
            if (networkinfo!=null) {


                if (networkinfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    status = "yes";
                } else if (networkinfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    status = "yes";
                }
            }else {

                status = "no";
            }


       dialog= new Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        dialog.setContentView(R.layout.layout_no_network_lay);
        dialog.setCancelable(false);

        TextView btn= (TextView) dialog.findViewById(R.id.okbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(context, MainActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent1);
            }

        });
        if (status=="no"){
            dialog.show();
        }
    }
}
