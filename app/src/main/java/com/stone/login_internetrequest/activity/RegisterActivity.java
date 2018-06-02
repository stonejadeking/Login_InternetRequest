package com.stone.login_internetrequest.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";
    private static final int ERROR_CODE0 = 0;
    private static final int ERROR_CODE1 = 1;
    private static final int ERROR_CODE2 = 2;
    private EditText et_account, et_password1, et_password2;
    private Button bt_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {
        et_account = findViewById(R.id.et_account);
        et_password1 = findViewById(R.id.et_password1);
        et_password2 = findViewById(R.id.et_password2);
        bt_register = findViewById(R.id.bt_register);
        bt_register.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        register();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String err_msg;
            switch (msg.arg1) {
                case ERROR_CODE0:
                    err_msg = (String) msg.obj;
                    Toast.makeText(RegisterActivity.this, err_msg, Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_CODE1:
                    err_msg = (String) msg.obj;
                    Toast.makeText(RegisterActivity.this, err_msg, Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_CODE2:
                    err_msg = (String) msg.obj;
                    Toast.makeText(RegisterActivity.this, err_msg, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void register() {
        String account = et_account.getText().toString().trim();
        String password1 = et_password1.getText().toString().trim();
        String password2 = et_password2.getText().toString().trim();
        String password = null;

        if (!account.equals("")  && !password1.equals("") && !password2.equals("") ) {
            if (password1.equals(password2)) {
                password = password1;
            } else {
                Toast.makeText(this, "两次密码输入不一致，请重新填写！", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(this, "填写信息有误或将信息补充完整！", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, account + "---" + password);

        //String url = "http://192.168.31.104:8888/JiangGe/LoginServlet";
        String url = "http://192.168.31.104:8888/JiangGe/RegisterServlet";
        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody
                .Builder()
                .add("user_name", account)
                .add("user_password", password)
                .add("user_type", "0")   // user_type == 0 对应表示注册新用户；user_type == 1 对应表示忘记密码
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
                            Message message = new Message();
                            message.arg1 = ERROR_CODE0;
                            message.obj = err_msg;
                            handler.sendMessage(message);
                            //Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, MainPageActivity.class));
                        } else if (err_code == 1) {
                            Message message = new Message();
                            message.arg1 = ERROR_CODE1;
                            message.obj = err_msg;
                            handler.sendMessage(message);
                            //Toast.makeText(LoginActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
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
    }
}
