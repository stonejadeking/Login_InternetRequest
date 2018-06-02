package com.stone.login_internetrequest.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.stone.login_internetrequest.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt1, bt2, bt3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
    }

    private void initListener() {
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);

        bt3.setOnClickListener(this);
    }

    private void initView() {
        bt1 = findViewById(R.id.bt1);
        bt2 = findViewById(R.id.bt2);

        bt3 = findViewById(R.id.bt3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt1:
                startActivity(new Intent(this, RequestDemoActivity.class));
                break;
            case R.id.bt2:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.bt3:
                startActivity(new Intent(this, PictureShowActivity.class));
                break;
        }
    }
}
