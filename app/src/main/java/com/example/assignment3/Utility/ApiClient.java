package com.example.assignment3.Utility;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ApiClient {

    private static final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public static void get(String url, Callback callBack){
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(callBack);
    }

}
