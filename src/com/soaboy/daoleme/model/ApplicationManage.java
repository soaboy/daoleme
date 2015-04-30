package com.soaboy.daoleme.model;

import android.app.Application;

/**
 * Created by liuyun on 2015/4/27.
 */
public class ApplicationManage extends Application {
    private String username;
    private String HttpUrl;
    private String updatetime;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHttpUrl() {
        return HttpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        HttpUrl = httpUrl;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }
}
