package com.example.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.login.Receiver.Receiver;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {
    MyDB myDB;
    Receiver receiver;
    EditText name, email;
    Button button, read, delete, deleteAll;
    String res;
    SaveLocalHost saveLocalHost;
//CustomAlert customAlert;
AlertDialog.Builder alertDialog;
    int resultName, resultEmail;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkpermission();

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        button = findViewById(R.id.button);
        read = findViewById(R.id.readButton);
        delete = findViewById(R.id.delButton);

        deleteAll = findViewById(R.id.delButtonAll);
        myDB = new MyDB(this);
      //  customAlert=new CustomAlert(MainActivity.this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        email.setBackgroundColor(R.color.teal_200);
        name.setBackgroundColor(R.color.teal_200);
            String regx="";


            //for network check all the time
            receiver=new Receiver();
            registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String nam_val = name.getText().toString();
                String email_val = email.getText().toString();
                chkPermission();

                if (nam_val.isEmpty()) {
                    Toasty.error(getApplicationContext(),"Name is empty",Toast.LENGTH_LONG).show();
                        //sweetAlert();
                    sweetAlert("Name is empty",SweetAlertDialog.ERROR_TYPE);

                } else if (email_val.isEmpty()) {
                    Toasty.error(getApplicationContext(),"Email is empty",Toast.LENGTH_LONG).show();
                    sweetAlert("Email is empty",SweetAlertDialog.ERROR_TYPE);
                    if (!nam_val.isEmpty()) {
                        name.setBackgroundColor(R.color.teal_200);
                    }
                    email.setBackgroundColor(Color.RED);

                } else if (!Patterns.EMAIL_ADDRESS.matcher(email_val).matches()) {
                    if (!nam_val.isEmpty()) {
                        name.setBackgroundColor(R.color.teal_200);
                    }
                    Toasty.error(getApplicationContext(),"Email not in order",Toast.LENGTH_LONG).show();
                    sweetAlert("Email Not In Order",SweetAlertDialog.ERROR_TYPE);

                    email.setBackgroundColor(Color.RED);

                } else {
                    email.setBackgroundColor(R.color.teal_200);
                    name.setBackgroundColor(R.color.teal_200);
                    resultName = myDB.chkSamedata(nam_val, "NAME", MainActivity.this);
                    if (resultName == 1) {
                        sweetAlert("same name found",SweetAlertDialog.ERROR_TYPE);

                        Toasty.error(getApplicationContext(),"Same name found",Toast.LENGTH_LONG).show();
                        name.setBackgroundColor(Color.RED);

                    } else {

                    }

                    resultEmail = myDB.chkSamedata(email_val, "EMAIL", MainActivity.this);
                    if (resultEmail == 1) {
                        sweetAlert("Same Email Found",SweetAlertDialog.ERROR_TYPE);

                        Toasty.error(getApplicationContext(),"Same email found",Toast.LENGTH_LONG).show();
                        email.setBackgroundColor(Color.RED);
                    } else {

                    }

                    if (resultName != 1 && resultEmail != 1) {
                        boolean res = myDB.insertData( nam_val, email_val);
                        if (res == true) {
                            Toasty.success(getApplicationContext(),"Name insterted",Toast.LENGTH_LONG).show();
                            name.setText("");
                            email.setText("");
                            name.setBackgroundColor(R.color.teal_200);
                            email.setBackgroundColor(R.color.teal_200);
                        } else {
                            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }


                }


            }
        });

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor= myDB.readData();

                if (cursor.getCount() == 0) {
                    Toasty.error(getApplicationContext(),"Nothing to show",Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(MainActivity.this, "showing", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,fetchData.class));


                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nam = name.getText().toString();


                if (nam.isEmpty()) {
                    sweetAlert("Fill name to delete data",SweetAlertDialog.WARNING_TYPE);
                    Toasty.error(getApplicationContext(),"Fill name to delete data",Toast.LENGTH_LONG).show();
                } else {
                    alert(nam);

                }
            }
        });
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, 1)
                        .setTitle("DELETE").setMessage("ARE YOU SURE?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                myDB.deleteAll(MainActivity.this);


                                Toasty.success(getApplicationContext(),"Deleted all data",Toast.LENGTH_LONG).show();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).show();



            }
        });
    }

    private void checkpermission() {
        LocationManager locationManager;
        Context context;
        context=this;
        locationManager=(LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            Boolean islocationenabled=locationManager.isLocationEnabled();
            if (islocationenabled==true){
                Toasty.success(this,"location is enabled");
            }
            else {
                Toasty.success(this,"location is not enabled"); }
        }


    }

    public void alert(String nam) {

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, 1)
                .setTitle("DELETE").setMessage("ARE YOU SURE?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myDB.deleteColomByName(nam);
                        name.setText("");
                        email.setText("");
                        Toasty.success(getApplicationContext(),"Deleted Selected Data",Toast.LENGTH_LONG).show();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();

    }
        public  void sweetAlert(String msg, int type)
        {

            SweetAlertDialog pDialog = new SweetAlertDialog(this, type);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText(msg);
            pDialog.setCancelable(false);
            pDialog.show();
        }
    public String uploadTolocalHost() {

        StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://localhost/logprj/setdata.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, "done", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
            }
        })
        {

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> map=new HashMap<>();
                map.put("name",name.getText().toString());
                map.put("email",email.getText().toString());
                map.put("address",email.getText().toString());
                return map;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        return res;
    }
    void chkPermission()
    {
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.INTERNET)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        sweetAlert("permission granted",SweetAlertDialog.SUCCESS_TYPE);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    sweetAlert("permission denied",SweetAlertDialog.ERROR_TYPE);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                          permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public void onBackPressed() {
    finish();

    }
}



