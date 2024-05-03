package com.example.integrationprojectapi21;

import android.util.Log;

import com.example.integrationprojectapi21.util.CustomAdapter;
import com.example.mysdklib.Excal;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkRequests {

    static String errResponse(String e){
        return "{" +
                "\"error\":\""+e+"\""
                +"}";
    }
    static OkHttpClient client = new OkHttpClient
            .Builder()
            .addInterceptor(Excal.getTokenSettingInterceptor())
            .build();


    public String run(String url) throws IOException {

        Log.d("REQUEST","control came to RUN");
        Request request = new Request.Builder().url(url).addHeader("newone","yep").build();

        try (Response response = client.newCall(request).execute()) {
            String res = response.body().string();
            Log.d("REQUEST","DONE to RUN resp = ["+res+"]");
            return res;
        }catch (Exception e){
            Log.d("REQESt","some error");
            return errResponse(e.toString());
        }

    }



    public static void sendRequest(String URL,MainActivity obj){
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Log.d("REQUES","call done");
                    // Your code goes here
                    NetworkRequests netreq = new NetworkRequests();
                    String res = netreq.run(URL);
                    obj.updateList(res);

                } catch (Exception e) {
                    Log.d("ERROR on req","call done");
                    obj.updateList(errResponse(e.toString()));
                }
            }
        });
        thread.start();
    }

//    public static void main(String[] args) throws IOException {
//        OkHttpGet example = new OkHttpGet();
//        String response = example.run("http://localhost:8080/api/v1/employees/1");
//        System.out.println(response);
//    }
}
