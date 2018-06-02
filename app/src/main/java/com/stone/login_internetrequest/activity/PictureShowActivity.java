package com.stone.login_internetrequest.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.stone.login_internetrequest.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PictureShowActivity extends AppCompatActivity implements View.OnClickListener {

    protected static final int SET_IMAGE = 1;
    protected static final int REQUEST_ERROR = 2;
    protected static final int NETWORK_ERROR = 3;

    private EditText et_path;
    private ImageView iv_image;
    private Button bt_pic_show, bt_close_activity;

    //创建一个android下的消息处理器  在主线程里面创建 属于主线程
    //1. 主线程里面创建一个消息处理器
    private Handler handler = new Handler() {
        //当子线程把消息发送出来后 主线程就会调用 handlemessage方法处理这个消息.
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SET_IMAGE:
                    //3.在主线程里面接受到了消息  ,处理这个消息
                    System.out.println("主线程得到了消息,更新ui界面");
                    Bitmap bitmap = (Bitmap) msg.obj;
                    iv_image.setImageBitmap(bitmap);
                    PictureShowActivity.this.finish();
                    break;
                case REQUEST_ERROR:
                    Toast.makeText(PictureShowActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    break;
                case NETWORK_ERROR:
                    Toast.makeText(PictureShowActivity.this, "网络访问失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_show);

        et_path = findViewById(R.id.et_path);
        iv_image = findViewById(R.id.iv_image);

        bt_pic_show = findViewById(R.id.bt_pic_show);
        bt_close_activity = findViewById(R.id.bt_close_activity);

        bt_pic_show.setOnClickListener(this);
        bt_close_activity.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_pic_show:
                picShow();
                break;
            case R.id.bt_close_activity:
                finish();
                break;
        }
    }

    public void picShow() {
        final String path = et_path.getText().toString().trim();
        if (TextUtils.isEmpty(path)) {
            Toast.makeText(this, "路径不能为空", 0).show();
            return;
        } else {
            //访问这个路径把图片获取出来,显示到界面上.
            //生成一个http请求, 把服务器的数据给显示出来.
            new Thread() {
                public void run() {
                    try {
                        System.out.println("开启子线程 ,访问网络,获取图片");
                        URL url = new URL(path);
                        //获取到一个连接
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setConnectTimeout(5000);
                        int code = conn.getResponseCode(); //200 ok  302 重定向
                        if (code == 200) {
                            InputStream is = conn.getInputStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(is);
                            //iv_image.setImageBitmap(bitmap);
                            //2.子线程不可以直接修改ui ,只能发消息给主线程 让主线程帮忙修改ui
                            System.out.println("子线程不能直接修改ui,发送一个消息给主线程");
                            Message msg = new Message();//实例化一个消息.
                            msg.what = SET_IMAGE;
                            //把要显示的bitmap 放在msg的消息里面.
                            msg.obj = bitmap;
                            //发送消息给主线程.
                            handler.sendMessage(msg);
                        } else {
                            //Toast.makeText(MainActivity.this, "请求失败",0).show();
                            //发送消息让主线程 打印土司
                            Message msg = new Message();
                            msg.what = REQUEST_ERROR;
                            handler.sendMessage(msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Message msg = new Message();
                        msg.what = NETWORK_ERROR;
                        handler.sendMessage(msg);
                        //Toast.makeText(MainActivity.this, "访问网络异常",0).show();
                    }
                }
            }.start();
        }
    }

}
