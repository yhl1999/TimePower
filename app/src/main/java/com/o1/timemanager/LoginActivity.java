package com.o1.timemanager;

import androidx.appcompat.app.AppCompatActivity;

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

import com.o1.timemanager.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText usernameText;
    private EditText passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonCancel = findViewById(R.id.buttonCancel);
        Button buttonRegister = findViewById(R.id.buttonRegister);
        buttonLogin.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.buttonLogin:
                usernameText = findViewById(R.id.username);
                passwordText = findViewById(R.id.password);
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();
                if(username.equals("")){
                    Toast.makeText(LoginActivity.this,"请输入用户名",Toast.LENGTH_SHORT).show();
                }
                else if(password.equals("")){
                    Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                }
                if(username.equals("123") && password.equals("123")){
                    //显示欢迎界面
//                    ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
//                    progressDialog.setTitle("登录成功，正在跳转");
//                    progressDialog.setMessage("欢迎您"+username+"用户，请稍后...");
//                    progressDialog.setCancelable(true);
//                    progressDialog.show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putBoolean("isLogin", true);
                    edit.putString("userAcnt", "ningyuv");
                    edit.putString("username", "ningyuv");
                    edit.apply();
                    finish();
                }
                else{
                    //否则报错
                    AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                    dialog.setTitle("错误");
                    dialog.setMessage("用户名或密码错误");
                    dialog.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            usernameText.setText("");
                            passwordText.setText("");
                        }
                    });
                    dialog.show();
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
                default:
                    break;
        }
    }

}
