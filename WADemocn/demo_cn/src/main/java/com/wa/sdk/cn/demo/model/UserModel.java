package com.wa.sdk.cn.demo.model;

import com.wa.sdk.user.model.WALoginResult;

/**
 * 用户信息
 * Created by hank on 16/9/1.
 */
public class UserModel {
    private static UserModel userModel = null;

    private String userId = null;
    private String token = null;
    private String platformUserId = null;
    private String platformToken = null;
    private String platform = null;

    private UserModel() {
    }

    public void setDatas(WALoginResult loginResult) {
        userId = loginResult.getUserId();
        token = loginResult.getToken();
        platformUserId = loginResult.getPlatformUserId();
        platformToken = loginResult.getPlatformToken();
        platform = loginResult.getPlatform();
    }

    public static UserModel getInstance() {
        if (userModel == null)
            userModel = new UserModel();

        return userModel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPlatformUserId() {
        return platformUserId;
    }

    public void setPlatformUserId(String platformUserId) {
        this.platformUserId = platformUserId;
    }

    public String getPlatformToken() {
        return platformToken;
    }

    public void setPlatformToken(String platformToken) {
        this.platformToken = platformToken;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public boolean isLogin() {
        return token != null && !token.equals("");
    }

    public void clear() {
        userId = null;
        token = null;
        platformUserId = null;
        platformToken = null;
        platform = null;
    }
}
