package com.stone.login_internetrequest.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.stone.login_internetrequest.R;
import com.stone.login_internetrequest.UserBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestDemoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RequestDemoActivity";
    private static final int GET_REQUEST = 1;
    private static final int POST_REQUEST = 2;
    private static final int FAIL = 2;
    private TextView tv,tv2;
    private Button bt_get, bt_post;

    private OkHttpClient client = null;

    private UserBean userBean;

    private Message message;

    Handler handler = new Handler() {
        Object object = null;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case GET_REQUEST:
                    object = msg.obj;
                    tv.setText((String) object);
                    break;
                case POST_REQUEST:
                    object = msg.obj;
                    if(msg.arg2 != FAIL){
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject((String)object);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String decodeJsonString = decodeJson(jsonObject);
                        String infoString = jsonObject.toString();
                        tv2.setText("\n\n"+infoString + "\n" + decodeJsonString);
                        Log.d(TAG,"handle_if");

                    }else {
                        Log.d(TAG,"handle_else");
                        tv2.setText((String)object);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_demo);

        initView();
        initListener();

    }

    private void initListener() {
        bt_get.setOnClickListener(this);
        bt_post.setOnClickListener(this);
    }

    private void initView() {
        tv = findViewById(R.id.tv);
        tv2 = findViewById(R.id.tv2);
        bt_get = findViewById(R.id.bt_get);
        bt_post = findViewById(R.id.bt_post);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_get:
                getRequest();
                break;
            case R.id.bt_post:
                postRequest();
                break;
        }
    }

    private void getRequest() {
        new Thread() {
            @Override
            public void run() {

                client = new OkHttpClient();

                String url = "https://www.baidu.com";
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = null;
                try {
                    response = client.newCall(request).execute();

                    if (response.isSuccessful()) {
                        Log.d(TAG, "response.code()==" + response.code());
                        Log.d(TAG, "response.message()==" + response.message());
                        if (response.code() == 200) {
                            String resp = response.body().string();
                            Log.d(TAG, "res==" + resp);

                            message = new Message();
                            message.arg1 = GET_REQUEST;
                            message.obj = resp;
                            handler.sendMessage(message);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (response != null) {
                        response.body().close();
                    }
                }

            }
        }.start();
    }

    private void postRequest() {
        new Thread() {
            @Override
            public void run() {

                //String url = "http://192.168.31.104:8888/JiangGe/LoginServlet";

                String url = "http://192.168.31.104:8888/JiangGe/RegisterServlet";

                client = new OkHttpClient();

                FormBody formBody = new FormBody
                        .Builder()
                        .add("user_name", "cunzhou")
                        .add("user_password","123")
                        .add("user_type","0")
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();


                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    Log.d(TAG,response.toString());

                    if (response.isSuccessful()) {
                        Log.d(TAG, "response.code()==" + response.code());
                        Log.d(TAG, "response.message()==" + response.message());

                        if (response.code() == 200) {
                            String resp = response.body().string();
                            Log.d(TAG, TAG+" res==" + resp);
                            message = new Message();
                            message.arg1 = POST_REQUEST;
                            message.obj = resp;
                            handler.sendMessage(message);
                        }
                    } else {
                        Log.d(TAG, "连接失败");
                        message = new Message();
                        message.arg1 = POST_REQUEST;
                        message.arg2 = FAIL;
                        message.obj = "连接失败";
                        handler.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    private String decodeJson(JSONObject jsonObject) {
        userBean = new UserBean();

//        private int user_id;
//        private String user_name;
//        private String user_password;
//        private String user_nickname;
//        private int user_sex;
//        private int user_age;
//        private String user_header_img;

            userBean.setUser_id(jsonObject.optInt("user_id"));
            userBean.setUser_name(jsonObject.optString("user_name"));
            userBean.setUser_password(jsonObject.optString("user_password"));
            userBean.setUser_nickname(jsonObject.optString("user_nickname"));
            userBean.setUser_sex(jsonObject.optInt("user_sex"));
            userBean.setUser_age(jsonObject.optInt("user_age"));
            userBean.setUser_header_img(jsonObject.optString("user_header_img"));
            return userBean.toString();
    }

}
