package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lab2.Retrofit.IMyService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginSuccess extends AppCompatActivity {
    TextView tvemail, tvName;
    Button btEdit;
    String b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);
        tvemail = findViewById(R.id.tvEmail);
        tvName = findViewById(R.id.tvName);
        btEdit = findViewById(R.id.btEdit);
        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "is coding", Toast.LENGTH_SHORT).show();
            }
        });
        Intent intent = getIntent();
        Bundle a = intent.getExtras();
        b = a.getString("email");
        getJSON();
    }

    public void getJSON(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String strurl = "http://192.168.1.3:3000/api/user";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(strurl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        if (b.equals(jsonObject.getString("email"))){
                            String id = jsonObject.getString("_id");
                            String name  = jsonObject.getString("name");
                            String email = jsonObject.getString("email");
                            tvemail.setText(" Email: "+email);
                            tvName.setText(" Name: "+name);
                        }
                    } catch (Exception e){
                        Log.e(e.toString(), "/"+e.toString());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    public void getJSONObject(){
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String strurl = "http://192.168.1.3:3000/api/user/delete:id";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(strurl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        if (b.equals(jsonObject.getString("email"))){
                            String id = jsonObject.getString("_id");
                            String name  = jsonObject.getString("name");
                            String email = jsonObject.getString("email");
                            tvemail.setText("Email: "+email);
                            tvName.setText("Name: "+name);
                        }
                    } catch (Exception e){
                        Log.e(e.toString(), "/"+e.toString());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.removeRequestFinishedListener((RequestQueue.RequestFinishedListener<Object>) jsonArrayRequest);
    }
}