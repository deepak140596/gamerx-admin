package com.example.gamerxadmin.utils;


import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SMSUtils {
    public static void sendSMS(String message, String phoneNumbers){
        Thread thread = new Thread(() -> Log.e("===========>", sendSms(message, phoneNumbers)));
        thread.start();

    }
    private static String sendSms(String message, String phoneNumbers) {
        try {
            // Construct data
            String apiKey = "apikey=" + "ruUa0Ag+UWI-WPgG40AC24HtdSHQTjCsPndk3ohBLT";
            message = "&message=" + message;
            String sender = "&sender=" + "TXTLCL";
            String numbers=  "&numbers=" + phoneNumbers;

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
            String data = apiKey + numbers + message + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();

            return stringBuffer.toString();
        } catch (Exception e) {
            Log.e("===========>","Error SMS " + e);
            return "Error "+e;
        }
    }
}
