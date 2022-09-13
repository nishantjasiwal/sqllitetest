package com.example.login;

import android.content.Context;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SaveLocalHost {

    String name,email,address;
    Context context;
    String res;


    private static  final String url="http://192.168.174.42/logprj/setdata.php";
    public SaveLocalHost(String name, String email, String address,Context context) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.context=context;
    }

    public String uploadTolocalHost() {

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
          res=response;

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                res=error.toString();
            }
        })
        {

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> map=new HashMap<>();
                map.put("name",name);
                map.put("email",email);
                map.put("address",address);
                return map;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        return res;
    }
}
