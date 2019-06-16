package com.example.shutterflygallery;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class  PixabayResponseUtils {

    private PixabayResponseUtils(){

    }

    public static void retrieveFileNames(JSONObject response, List<String> imageNamesForQuery) {
        try {
            JSONArray hits = response.getJSONArray("hits");
            for(int i=0; i < hits.length(); i++){
                JSONObject hit = hits.getJSONObject(i);
                String previewURL = hit.getString("previewURL");
                imageNamesForQuery.add(previewURL);
            }

        } catch (JSONException e) {
            Log.e("e","error parsing hits");
        }
    }

    public static int retrieveResultCount(JSONObject response) {
        try {
            JSONArray hits = response.getJSONArray("hits");
            return hits.length();
        } catch (JSONException e) {
            Log.e("e","error parsing hit count");
        }
        return 0;
    }
}
