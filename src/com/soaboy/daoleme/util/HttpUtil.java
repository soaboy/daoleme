package com.soaboy.daoleme.util;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by liuyun on 2015/4/19.
 */
public class HttpUtil {
        public HttpUtil() {
            // TODO Auto-generated constructor stub
        }
        public static String getJsonContentByPsot(String url_path, List<NameValuePair> paramsList) {
            HttpPost httpRequest = new HttpPost(url_path);
            try {
                httpRequest.setEntity(new UrlEncodedFormEntity(paramsList,
                        HTTP.UTF_8));
                try {
                    HttpResponse httpResponse = new DefaultHttpClient()
                            .execute(httpRequest);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                    } else {
                        return "Error Response"
                                + httpResponse.getStatusLine().toString();
                    }
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return "error";
        }

        public static String getJsonContent(String url_path) {
            try {
                URL url = new URL(url_path);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(3000); // 等待时间3s
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                int code = connection.getResponseCode(); //
                if (code == 200) {
                    return changeInputStream(connection.getInputStream());
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            return "123";
        }

        private static String changeInputStream(InputStream inputStream) {
            // TODO Auto-generated method stub
            String jsonString = "";
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int length = 0;
            byte[] data = new byte[1024];
            try {
                while (-1 != (length = inputStream.read(data))) {
                    outputStream.write(data, 0, length);
                }
                jsonString = new String(outputStream.toByteArray());
            } catch (Exception e) {
                // TODO: handle exception
            }
            return jsonString;
        }
    }