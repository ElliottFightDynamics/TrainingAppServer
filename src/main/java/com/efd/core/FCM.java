package com.efd.core;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by volodymyr on 22.06.17.
 */
public class FCM {


    final static private String FCM_URL = "https://fcm.googleapis.com/fcm/send";


    /**
     *
     * Method to send push notification to Android FireBased Cloud messaging Server.
     *
     * @param tokenId Generated and provided from Android Client Developer
     * @param server_key Key which is Generated in FCM Server
     * @param message which contains actual information.
     *
     */

    public static void send_FCM_Notification(String tokenId, String server_key, String message){
        try{
            URL url = new URL(FCM_URL);
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization","key="+server_key);
            conn.setRequestProperty("Content-Type",Constants.KEY_APPLICATION_JSON);
            JSONObject infoJson = new JSONObject();
            infoJson.put("title","Here is your notification.");
            infoJson.put("body", message);
            JSONObject json = new JSONObject();
            json.put("to",tokenId.trim());
            json.put("notification", infoJson);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();
            int status;
            status = conn.getResponseCode();
            if(status != 0){
                if(status == 200){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else if(status == 401){
                } else if(status == 501){
                } else if( status == 503){
                }
            }
        } catch (Exception ignored) {}
    }
}


