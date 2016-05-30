package com.example.zhengsuren.padapp;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by zhengsuren on 16/5/30.
 */
public class AdsThread extends Thread{
    private String url,id;
    private Handler handler;
    private Context context;

    public AdsThread(String url,String id,Handler handler,Context context)
    {
        this.url = url;
        this.id = id;
        this.handler = handler;
        this.context = context;
    }

    private String request()
    {
        url = url + "?id=" + id;

        try {
            URL HttpURL = new URL(url);

            HttpURLConnection conn = (HttpURLConnection) HttpURL.openConnection();
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String str;
            while ((str = reader.readLine()) != null)
            {
                sb.append(str);
            }

            System.out.println("This is the result~~~~~~~~!!!!" + sb.toString());

            String result = parse(sb.toString());

            return result;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    private String parse(String json)
    {
        try
        {
            JSONObject object = new JSONObject(json);
            int errno = object.getInt("errno");
            String errmsg = object.getString("errmsg");

            if (0 == errno && errmsg.isEmpty())
            {
                String data = object.getString("data");

                return data;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void doGet() throws IOException
    {
        String adsInfo = request();

        if (adsInfo != null)
        {
            handler.sendEmptyMessage(0);
            //需要数据传递，用下面方法；
            Message msg =new Message();
            msg.obj = adsInfo;//可以是基本类型，可以是对象，可以是List、map等；
            handler.sendMessage(msg);
        }
    }

    @Override
    public void run() {
        try {
            doGet();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
