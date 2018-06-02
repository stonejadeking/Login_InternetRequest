package com.stone.login_internetrequest.utils;

import android.util.Log;

import com.stone.login_internetrequest.UserBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Stone on 2018/6/1.
 */

public class HTTPUtils {

    private static final String TAG = "HTTPUtils";
    private static OkHttpClient client = null;
    private UserBean userBean;


    public static JSONObject postRequestForLogin(String account, String password) {

        final JSONObject[] jsonObject = {null};

        String url = "http://192.168.31.104:8888/JiangGe/LoginServlet";

        //String url = "http://192.168.31.104:8888/JiangGe/RegisterServlet";

        client = new OkHttpClient();

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

                    try {
                        jsonObject[0] = new JSONObject(resp);
                        Log.d(TAG, jsonObject[0].toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                response.body().close();
            }
        });

        Log.d(TAG, jsonObject[0].toString());
        return jsonObject[0];
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
