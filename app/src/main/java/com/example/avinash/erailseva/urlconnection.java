package com.example.avinash.erailseva;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by avinash on 19/11/16.
 */

public class urlconnection {
    URL url;
    JSONObject jsonresult;

    public String urlreturn( String _url, JSONObject _jsondata, String _method, String login) {
        HttpURLConnection conn = null;
        try {
            url = new URL(_url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(_method);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("User-Agent", "Android");
            if (LoginActivity.msCookieManager.getCookieStore().getCookies().size() > 0) {
                // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
                conn.setRequestProperty("Cookie",
                        TextUtils.join(";",  LoginActivity.msCookieManager.getCookieStore().getCookies()));
            }
            if(_method.equals("POST")){
                String query = String.valueOf(_jsondata);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
            }

            conn.connect();

            if(login.equals("login")){
                Map<String, List<String>> headerFields = conn.getHeaderFields();
                List<String> cookiesHeader = headerFields.get(LoginActivity.COOKIES_HEADER);

                if (cookiesHeader != null) {
                    for (String cookie : cookiesHeader) {
                        LoginActivity.msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                    }
                }
            }

            if(login.equals("logout")){
                LoginActivity.msCookieManager.getCookieStore().removeAll();
            }

            int status = conn.getResponseCode();
            System.out.println(status);
            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
            }
        } catch (Exception ex) {
            return ex.toString();
        }
        return  null;
    }
}
