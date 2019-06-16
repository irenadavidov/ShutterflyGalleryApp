package com.example.shutterflygallery;

import com.loopj.android.http.*;

public class PixabayRestClient {
    private static final String BASE_URL = "https://pixabay.com/api/";
    private static final String KEY = "12175339-7048b7105116d7fa1da74220c";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void executeQuery(String searchQuery, String pageNum, String pageSize, ResponseHandlerInterface responseHandler) {
        RequestParams rp = new RequestParams();
        rp.add("key", KEY);
        rp.add("image_type", "photo");
        rp.add("q", searchQuery);
        rp.add("page", pageNum);
        rp.add("per_page", pageSize);

        client.get(BASE_URL, rp, responseHandler);
    }

    public static void getURI(String uri, ResponseHandlerInterface responseHandler){
        RequestParams rp = new RequestParams();
        client.get(uri, rp, responseHandler);
    }
}
