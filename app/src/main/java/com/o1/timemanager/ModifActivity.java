package com.o1.timemanager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ModifActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText usernameText;
    private EditText passwordText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modif);
        Button buttonCancel = findViewById(R.id.buttonCancel);
        Button buttonModif  = findViewById(R.id.buttonModif);
        buttonCancel.setOnClickListener(this);
        buttonModif.setOnClickListener(this);
        usernameText = findViewById(R.id.username);
        passwordText = findViewById(R.id.password);
    }

    @Override
    public void onClick(View v) {
        EditText surepasswordText = findViewById(R.id.surepassword);
        EditText surepassword2Text = findViewById(R.id.surepassword2);
        switch (v.getId()) {
            case R.id.buttonModif:
                String surepassword = surepasswordText.getText().toString();
                String surepassword2 = surepassword2Text.getText().toString();
                String regex = "^![a-zA-Z0-9]{6,12}$";
                if (surepassword.matches(regex)) {
                    Toast.makeText(ModifActivity.this, "密码长度或组成不合法", Toast.LENGTH_SHORT).show();
                }
                else if (!surepassword.equals(surepassword2)) {
                    Toast.makeText(ModifActivity.this, "两次密码不一致请重新输入", Toast.LENGTH_SHORT).show();
                    surepasswordText.setText("");
                }
                else{
                    String username = usernameText.getText().toString();
                    String password = passwordText.getText().toString();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(ModifActivity.this);
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://121.36.56.36:5000/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    Api api = retrofit.create(Api.class);
                    JsonObject body = new JsonObject();
                    body.addProperty("apicode", 4);
                    body.addProperty("userAcnt", username);
                    body.addProperty("oldPwd", password);
                    body.addProperty("newPwd",surepassword);
                    api.post(body).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                            if (response.body() != null) {
                                int state = response.body().get("statu").getAsInt();
                                switch (state) {
                                    case 0:
                                        dialog.setTitle("修改成功");
                                        dialog.setMessage("用户名" + username + " 密码" + password);
                                        dialog.setPositiveButton("现在登录", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent inter = new Intent(ModifActivity.this, LoginActivity.class);
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
                                        break;

                                    case 1:
                                        //否则报错
                                        AlertDialog.Builder dialog = new AlertDialog.Builder(ModifActivity.this);
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

                                    case 2:
                                        Toast.makeText(ModifActivity.this, "账户不存在！", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 3:
                                        Toast.makeText(ModifActivity.this, "存在多个账户！", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                            t.printStackTrace();
                            dialog.setTitle("修改失败");
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
