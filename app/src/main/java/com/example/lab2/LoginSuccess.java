package com.example.lab2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lab2.Retrofit.IMyService;
import com.example.lab2.Retrofit.RetrofitClient;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class LoginSuccess extends AppCompatActivity {
    TextView tvemail, tvName;
    Button btEdit;
    String b, strEmail, strName, strPass;
    String checkUpdate;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    @Override
    protected void onStop(){
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);

        Retrofit retrofit = RetrofitClient.getInstace();
        iMyService = retrofit.create(IMyService.class);

        tvemail = findViewById(R.id.tvEmail);
        tvName = findViewById(R.id.tvName);
        btEdit = findViewById(R.id.btEdit);

        Intent intent = getIntent();
        Bundle a = intent.getExtras();
        b = a.getString("email");
        getJSON();

        strEmail = tvemail.getText().toString();
        strName = tvName.getText().toString();

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View update_layout = LayoutInflater.from(LoginSuccess.this).inflate(R.layout.edit_layout, null, false);
                new MaterialStyledDialog.Builder(LoginSuccess.this)
                        .setTitle("UPDATE")
                        .setCustomView(update_layout)
                        .setNegativeText("CANCEL")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveText("UPDATE")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                EditText ed_update_email =update_layout.findViewById(R.id.ed_fix_Email);
                                EditText ed_update_name =update_layout.findViewById(R.id.ed_fixName);
                                EditText ed_update_pass =update_layout.findViewById(R.id.ed_fix_Password);
                                EditText ed_confirm_pass =update_layout.findViewById(R.id.ed_confirm_Password);
                                Button bt_show = update_layout.findViewById(R.id.bt_onoff_pass);

                                Log.d("str",strEmail + " " + strName + " "+strPass);
                                ed_update_email.setText(strEmail);
                                ed_update_name.setText(strName);
                                ed_update_pass.setText(strPass);

                                if(TextUtils.isEmpty(ed_update_email.getText().toString())){
                                    Toast.makeText(getApplicationContext(), "Email is empty",Toast.LENGTH_LONG).show();
                                    return;
                                }

                                if(TextUtils.isEmpty(ed_update_name.getText().toString())){
                                    Toast.makeText(getApplicationContext(), "Name is empty",Toast.LENGTH_LONG).show();
                                    return;
                                }

                                if(TextUtils.isEmpty(ed_update_pass.getText().toString())){
                                    Toast.makeText(getApplicationContext(), "Password is empty",Toast.LENGTH_LONG).show();
                                    return;
                                }

                                if(ed_update_pass.getText().toString().equals(ed_confirm_pass.getText().toString())){
                                    updateUser(ed_update_email.getText().toString(), ed_update_pass.getText().toString(), ed_update_name.getText().toString());
                                    Toast.makeText(getApplicationContext(), "Check password", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "2 pass !=", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).show();
                Toast.makeText(getApplicationContext(), "is coding", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateUser(String email, String pass, String name) {
        compositeDisposable.add(iMyService.editUser(email, pass, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        checkUpdate = charRemoveAtFrist(response);
                        checkUpdate = charRemoveAtLast(checkUpdate);
                        Toast.makeText(getApplicationContext(), checkUpdate, Toast.LENGTH_LONG).show();
                    }
                }));
    }

    public void getJSON(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String strurl = "http://192.168.1.3:3000/api/user";
        String strurl = "http://192.168.31.9:3000/api/user";
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
                            strPass = jsonObject.getString("pass");
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

    private String charRemoveAtFrist(String str){
        return str.substring(1, str.length());
    }
    private String charRemoveAtLast(String str){
        return str.substring(0, str.length()- 1);
    }
}