package com.stone.login_internetrequest.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.stone.login_internetrequest.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private static final int ERROR_CODE1 = 1;
    private static final int ERROR_CODE2 = 2;
    private Button bt_register, bt_forget_password, login;
    private EditText et_account, et_password;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initListener();
    }

    private void initView() {
        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);

        login = findViewById(R.id.login);
        bt_register = findViewById(R.id.bt_register);
        bt_forget_password = findViewById(R.id.bt_forget_password);

        checkBox = findViewById(R.id.checkBox);
    }

    private void initListener() {
        login.setOnClickListener(this);
        bt_register.setOnClickListener(this);
        bt_forget_password.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                login();
                break;
            case R.id.bt_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.bt_forget_password:
                startActivity(new Intent(this, ForgetActivity.class));
                break;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String err_msg;
            switch (msg.arg1) {
                case ERROR_CODE1:
                    err_msg = (String) msg.obj;
                    Toast.makeText(LoginActivity.this, "登陆失败:" + err_msg, Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_CODE2:
                    err_msg = (String) msg.obj;
                    Toast.makeText(LoginActivity.this, "登陆失败，其他错误:" + err_msg, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void login() {
        String account = et_account.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        Log.d(TAG, account + "---" + password);

        if (!account.equals("")  && !password.equals("")) {
            String url = "http://192.168.31.104:8888/JiangGe/LoginServlet";
            //String url = "http://192.168.31.104:8888/JiangGe/RegisterServlet";
            OkHttpClient client = new OkHttpClient();
            FormBody formBody = new FormBody
                    .Builder()
                    .add("user_name", account)
                    .add("user_password", password)
                    //.add("user_type","0")
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "连接失败");
                    Log.d(TAG, e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // 此方法运行在子线程中，不能在此方法中进行UI操作。
                    String resp = response.body().string();
                    Log.d(TAG, resp);
                    Log.d(TAG, "response.code()==" + response.code());
                    Log.d(TAG, "response.message()==" + response.message());
                    if (response.code() == 200) {
                        Log.d(TAG, TAG + " res==" + resp);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(resp);
                            Log.d(TAG, jsonObject.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        int err_code;
                        String err_msg;
                        try {
                            Log.d(TAG, jsonObject.toString());
                            err_code = jsonObject.getInt("err_code");
                            err_msg = jsonObject.getString("err_msg");

                            if (err_code == 0) {
                                startActivity(new Intent(LoginActivity.this, MainPageActivity.class));
                            } else if (err_code == 1) {
                                Message message = new Message();
                                message.arg1 = ERROR_CODE1;
                                message.obj = err_msg;
                                handler.sendMessage(message);
                                //Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();

                            } else if (err_code == 2) {
                                Message message = new Message();
                                message.arg1 = ERROR_CODE2;
                                message.obj = err_msg;
                                handler.sendMessage(message);
                                //Toast.makeText(LoginActivity.this, "登陆失败，其他错误", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    response.body().close();
                }
            });
        } else {
            Toast.makeText(LoginActivity.this, "完整填写登陆信息", Toast.LENGTH_SHORT).show();
        }
    }
}
