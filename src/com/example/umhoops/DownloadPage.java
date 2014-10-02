package com.example.umhoops;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import android.util.Log;

public class DownloadPage {
    static final String HTTP_DEBUG = "Download Page";

    public static String download(String url) {

        URL urlObj = null;
        String result = null;
        HttpURLConnection urlConnection = null;
        try {
            urlObj = new URL(url);
            urlConnection = (HttpURLConnection) urlObj.openConnection();
            result = convertStreamToString(urlConnection.getInputStream());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

        // HttpClient httpclient = new DefaultHttpClient();
        //
        // // Prepare a request object
        // HttpGet httpget = new HttpGet(url);
        // String result = null;
        // // Execute the request
        // HttpResponse response;
        // try {
        // response = httpclient.execute(httpget);
        // // Examine the response status
        // Log.d(HTTP_DEBUG, response.getStatusLine().toString());
        //
        // // Get hold of the response entity
        // HttpEntity entity = response.getEntity();
        // // If the response does not enclose an entity, there is no need
        // // to worry about connection release
        //
        // if (entity != null) {
        //
        // result = EntityUtils.toString(entity);
        //
        // /* // A Simple JSON Response Read
        // InputStream instream = entity.getContent();
        // result = convertStreamToString(instream);
        // // now you have the string representation of the HTML request
        // instream.close();*/
        // }
        //
        // } catch (Exception e) {
        // Log.e(HTTP_DEBUG, "response failed");
        // }
        return result;
    }

    private static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            Log.e(HTTP_DEBUG, "convert to string failed");
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e(HTTP_DEBUG, "close stream buffer failed");
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
