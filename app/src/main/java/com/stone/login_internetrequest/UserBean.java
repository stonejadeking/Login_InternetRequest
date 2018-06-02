package com.stone.login_internetrequest;

import org.json.JSONObject;

/**
 * Created by Stone on 2018/5/26.
 */

public class UserBean {
    private int user_id;
    private String user_name;
    private String user_password;
    private String user_nickname;
    private int user_sex;
    private int user_age;
    private String user_header_img;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public int getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(int user_sex) {
        this.user_sex = user_sex;
    }

    public int getUser_age() {
        return user_age;
    }

    public void setUser_age(int user_age) {
        this.user_age = user_age;
    }

    public String getUser_header_img() {
        return user_header_img;
    }

    public void setUser_header_img(String user_header_img) {
        this.user_header_img = user_header_img;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                ", user_password='" + user_password + '\'' +
                ", user_nickname='" + user_nickname + '\'' +
                ", user_sex=" + user_sex +
                ", user_age=" + user_age +
                ", user_header_img='" + user_header_img + '\'' +
                '}';
    }
}
