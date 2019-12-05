package com.o1.timemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameText;
    private EditText passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regsiter);
        Button buttonCancel = findViewById(R.id.buttonCancel);
        Button buttonRegister = findViewById(R.id.buttonRegister);
        buttonCancel.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
        usernameText = findViewById(R.id.username);
        passwordText = findViewById(R.id.password);

    }

    @Override
    public void onClick(View v) {
        EditText surepasswordText;
        switch (v.getId()) {
            case R.id.buttonRegister:
                surepasswordText = findViewById(R.id.surepassword);
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();
                String surepassword = surepasswordText.getText().toString();
                String regex = "^![a-zA-Z0-9]{6,12}$";
                if (username.matches(regex)) {
                    Toast.makeText(RegisterActivity.this, "用户名长度或组成不合法", Toast.LENGTH_SHORT).show();
                } else if (password.matches(regex)) {
                    Toast.makeText(RegisterActivity.this, "密码长度或组成不合法", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(surepassword)) {
                    Toast.makeText(RegisterActivity.this, "两次密码不一致请重新输入", Toast.LENGTH_SHORT).show();
                    surepasswordText.setText("");
                } else {
                    //显示欢迎界面
                    AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://121.36.56.36:5000/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    Api api = retrofit.create(Api.class);
                    JsonObject body = new JsonObject();
                    body.addProperty("apicode", 1);
                    body.addProperty("newAcnt", username);
                    body.addProperty("newPwd", password);
                    api.post(body).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                            if (response.body() != null) {
                                System.out.println(response.body().get("statu").getAsInt());
                                int state = response.body().get("statu").getAsInt();
                                if(state == -1){
                                    Toast.makeText(RegisterActivity.this,"该账户已经存在！请换一个用户名",Toast.LENGTH_SHORT).show();
                                }
                                else if(state == 0){
                                    Toast.makeText(RegisterActivity.this,"用户创建失败！",Toast.LENGTH_SHORT).show();
                                }
                                else if(state == 1){
                                    dialog.setTitle("注册成功");
                                    dialog.setMessage("用户名" + username + " 密码" + password);
                                    dialog.setPositiveButton("现在登录", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent inter = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(inter);
                                            finish();
                                        }
                                    });
                                    dialog.setNegativeButton("等会登录", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    dialog.show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                            t.printStackTrace();
                            dialog.setTitle("注册失败");
                            dialog.setMessage("请联系管理员");
                            dialog.show();
                        }
                    });
                }
                break;
            //清空输入
            case R.id.buttonCancel:
                usernameText.setText("");
                passwordText.setText("");
                break;
            default:
                break;
        }
    }
}
