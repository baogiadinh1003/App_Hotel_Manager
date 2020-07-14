package com.example.lab2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.lab2.Retrofit.IMyService;
import com.example.lab2.Retrofit.RetrofitClient;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.logging.Logger;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
     MaterialEditText ed_lg_email, ed_lg_pass;
     Button bt_lg, bt_resgis;
     String checkRes;

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
        setContentView(R.layout.activity_main);

        //Init service
        Retrofit retrofit = RetrofitClient.getInstace();
        iMyService = retrofit.create(IMyService.class);

        //Init view
        ed_lg_email = (MaterialEditText) findViewById(R.id.ed_lg_Email);
        ed_lg_pass = (MaterialEditText) findViewById(R.id.ed_lg_Password);
        bt_lg = findViewById(R.id.btLogin);
        bt_lg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(ed_lg_email.getText().toString(), ed_lg_pass.getText().toString());
            }
        });

        bt_resgis = findViewById(R.id.btRegister);
        bt_resgis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View resgister_layout = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_layout, null, false);
                new MaterialStyledDialog.Builder(MainActivity.this)
                .setTitle("Registration")
                .setCustomView(resgister_layout)
                .setNegativeText("CANCEL")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveText("REGISTER")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        EditText ed_regis_email =resgister_layout.findViewById(R.id.edEmail);
                        EditText ed_regis_name =resgister_layout.findViewById(R.id.edName);
                        EditText ed_regis_pass =resgister_layout.findViewById(R.id.edPassword);

                        if(TextUtils.isEmpty(ed_regis_email.getText().toString())){
                            Toast.makeText(getApplicationContext(), "Email is empty",Toast.LENGTH_LONG).show();
                            return;
                        }

                        if(TextUtils.isEmpty(ed_regis_name.getText().toString())){
                            Toast.makeText(getApplicationContext(), "Name is empty",Toast.LENGTH_LONG).show();
                            return;
                        }

                        if(TextUtils.isEmpty(ed_regis_pass.getText().toString())){
                            Toast.makeText(getApplicationContext(), "Password is empty",Toast.LENGTH_LONG).show();
                            return;
                        }

                        registerUser(ed_regis_email.getText().toString(), ed_regis_pass.getText().toString() , ed_regis_name.getText().toString());
                    }
                }).show();
            }
        });
    }

    private void registerUser(String email, String pass, String name) {
         compositeDisposable.add(iMyService.registerUser(email, pass, name)
         .subscribeOn(Schedulers.io())
         .observeOn(AndroidSchedulers.mainThread())
         .subscribe(new Consumer<String>() {
             @Override
             public void accept(String response) throws Exception {
                 Toast.makeText(getApplicationContext(), ""+response, Toast.LENGTH_LONG).show();
             }
         }));
    }

    private void loginUser(String email, String password) {
        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(), "Email is empty",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(), "Password is empty",Toast.LENGTH_LONG).show();
            return;
        }
        compositeDisposable.add(iMyService.loginUsers(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        checkRes = charRemoveAtFrist(response);
                        checkRes = charRemoveAtLast(checkRes);

                        Log.d("respone", ""+checkRes);
                        Toast.makeText(MainActivity.this, ""+checkRes, Toast.LENGTH_LONG).show();

                        if(checkRes.equals("Login success")){
                            Intent intent = new Intent(MainActivity.this, LoginSuccess.class);
                            startActivity(intent);
                        }
                    }
                }
                )
        );
    }

    private String charRemoveAtFrist(String str){
        return str.substring(1, str.length());
    }
    private String charRemoveAtLast(String str){
         return str.substring(0, str.length()- 1);
    }
}