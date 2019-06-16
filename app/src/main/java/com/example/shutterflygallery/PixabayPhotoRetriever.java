package com.example.shutterflygallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class PixabayPhotoRetriever {

    final int photosPerPage;
    private PhotoResultLisetner resultListner;

    public PixabayPhotoRetriever(int photosPerPage) {
        this.photosPerPage = photosPerPage;
    }

    public void setResultListner(PhotoResultLisetner resultListner){
        this.resultListner = resultListner;
    }

    public void search(String query, int pageNum){
        PixabayRestClient.executeQuery(query, Integer.toString(pageNum), Integer.toString(photosPerPage), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                final int resultCount = PixabayResponseUtils.retrieveResultCount(response);
                List<String> fileNames = new ArrayList<>();
                PixabayResponseUtils.retrieveFileNames(response, fileNames);

                final CountDownLatch cdl = new CountDownLatch(resultCount);
                for (int i = 0; i < resultCount; i++) {
                    PixabayRestClient.getURI(fileNames.get(i), new BinaryHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inMutable = true;
                            Bitmap bmp = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length, options);
                            if (resultListner != null){
                                resultListner.onResult(bmp);
                            }
                            cdl.countDown();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {
                            cdl.countDown();
                        }

                    });
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean allRcvd = false;
                        try {
                            allRcvd = cdl.await(2 * resultCount, TimeUnit.SECONDS);
                            if (resultListner != null){
                                resultListner.onFinished(allRcvd);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });


    }



}
