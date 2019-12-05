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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameText;
    private EditText passwordText;
    private EditText surepasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regsiter);
        Button buttonCancel = findViewById(R.id.buttonCancel);
        Button buttonRegister = findViewById(R.id.buttonRegister);
        buttonCancel.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRegister:
                usernameText = findViewById(R.id.username);
                passwordText = findViewById(R.id.password);
                surepasswordText = findViewById(R.id.surepassword);
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();
                String surepassword = surepasswordText.getText().toString();
                String regex = "^[a-zA-Z0-9]{6,12}$";
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
                    dialog.setTitle("注册成功");
                    dialog.setMessage("用户名" + username + " 密码" + password);
                    dialog.setPositiveButton("现在登录", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent inter = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(inter);
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
