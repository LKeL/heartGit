package com.heartblood.heartgit.common;

import org.json.JSONObject;

/**
 * Created by heartblood on 16/5/25.
 * 保存全局变量及方法
 */
public class Global {
    public static String GETSFBLOG = "http://119.29.58.43/api/getSfBlog/getPage=";
    private static JSONObject newsData;

    public static void setNewsData(JSONObject newsData) {
        Global.newsData = newsData;
    }

    public static JSONObject getNewsData() {
        return newsData;
    }
}
