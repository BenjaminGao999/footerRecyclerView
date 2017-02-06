package com.qingmu.footerrecyclerview.module;

        import java.io.Serializable;

/**
 * Author:　Created by benjamin
 * DATE :  2017/1/24 15:55
 */

public class PostJson implements Serializable{
    public int size;
    public int page;
    public int receiverId;
    public String appName;

    public PostJson(int size, int page, int receiverId, String appName) {
        this.size = size;
        this.page = page;
        this.receiverId = receiverId;
        this.appName = appName;
    }
}
