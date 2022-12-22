package com.jnu.booklistapplication.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DataLoader {

    public String download(String strUrl) {

        StringBuffer stringBuffer = new StringBuffer();

        try {
            // 返回Json字符串
            URL url = new URL(strUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setUseCaches(false);
            httpURLConnection.connect();

            // 判断HTTP报文的状态码
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                // 输入流
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                // 加一层缓冲，就可以一行一行读
                String line = "";
                while (null != (line = bufferedReader.readLine())) {
                    stringBuffer.append(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new String(stringBuffer);
    }

    public ArrayList parsonJson(String strJson) {

        //解析Json
        ArrayList<ShopLocation> arrayList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(strJson);
            JSONArray shops = jsonObject.getJSONArray("shops");
            for (int i = 0; i < shops.length(); i++) {
                JSONObject shop = shops.getJSONObject(i);
                ShopLocation shopLocation = new ShopLocation(shop.getString("name"),
                        shop.getDouble("latitude"),
                        shop.getDouble("longitude"),
                        shop.getString("memo"));
                arrayList.add(shopLocation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arrayList;
    }
}
