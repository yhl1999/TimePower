package com.o1.timemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameText;
    private EditText passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonCancel = findViewById(R.id.buttonCancel);
        Button buttonRegister = findViewById(R.id.buttonRegister);
        TextView textView = findViewById(R.id.textViewModif);
        buttonLogin.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);

        textView.setOnClickListener(this);
        usernameText = findViewById(R.id.username);
        passwordText = findViewById(R.id.password);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();
                if (username.equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                }
                else if (password.equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                }
                else {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://121.36.56.36:5000/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    Api api = retrofit.create(Api.class);
                    JsonObject body = new JsonObject();
                    body.addProperty("apicode", 3);
                    body.addProperty("userAcnt", username);
                    body.addProperty("userPwd", password);
                    api.post(body).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                            if (response.body() == null) {
                                return;
                            }
                            int state = response.body().get("statu").getAsInt();
                            switch (state){
                                case 0:
                                    //否则报错
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                                    dialog.setTitle("错误");
                                    dialog.setMessage("密码错误");
                                    dialog.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            passwordText.setText("");
                                        }
                                    });
                                    dialog.show();
                                    break;
                                case 1:
                                    //显示欢迎界面
                                    ProgressDialog proDialog = new ProgressDialog(LoginActivity.this);
                                    proDialog.setTitle("登录成功，正在跳转");
                                    proDialog.setMessage("欢迎您" + username + "用户，请稍后...");
                                    proDialog.setCancelable(true);
                                    proDialog.show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
                                    SharedPreferences.Editor edit = sp.edit();
                                    edit.putBoolean("isLogin", true);
                                    edit.putString("userAcnt", username);
                                    edit.putString("username", username);
                                    edit.apply();
                                    finish();
                                    break;
                                case 2:
                                    Toast.makeText(LoginActivity.this, "账户不存在！", Toast.LENGTH_SHORT).show();
                                    break;
                                case 3:
                                    Toast.makeText(LoginActivity.this, "存在多个账户！", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                            Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
            //清空输入
            case R.id.buttonCancel:
                usernameText.setText("");
                passwordText.setText("");
                break;
            case R.id.buttonRegister:
                Intent inter = new Intent(LoginActivity.this, com.o1.timemanager.RegisterActivity.class);
                startActivity(inter);
                finish();
                break;
            case R.id.textViewModif:
                Intent inters = new Intent(LoginActivity.this, com.o1.timemanager.ModifActivity.class);
                startActivity(inters);
                finish();
                break;
            default:
                break;
        }
    }

}
