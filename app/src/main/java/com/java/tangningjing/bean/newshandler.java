package com.java.tangningjing.bean;
import android.util.Log;

import com.orm.dsl.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL ;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class newshandler {
    //上拉：添加
    //下拉：直接刷新,两页
    public int number_startp ;//正在显示的新闻的是第多少条
    public int number_endp ;
    public int number_startn ;
    public int number_endn ;
    public ArrayList<news> news_p ;//
    public ArrayList<news> news_n ;
    public newshandler() {
        news_p = new ArrayList<news>();
        news_n = new ArrayList<news>();
        try{
            initialize();
        }
        catch(Exception e){
            System.out.println("Initialze News Failed in newshandler Construct");
        }
    };

    private JSONObject parsejson(String url) throws JSONException {
        final String[] json = new String[1];
        //            URL urlObject = new URL(url);
//            HttpURLConnection uc = (HttpURLConnection) urlObject
//                    .openConnection();
//            //int contentLength = uc.getContentLength();
//            //System.out.println(contentLength);
//            BufferedReader in = new BufferedReader(new InputStreamReader(uc
//                    .getInputStream(), "utf-8"));
//            String inputLine = null;
//            while ((inputLine = in.readLine()) != null) {
//                json.append(inputLine);
//            }
//            in.close();
//            uc.disconnect();
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = okHttpClient.newCall(request);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Response response = call.execute();
                    json[0] = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //        System.out.println(json.toString());
        try {
            while(json[0] == null){
                Thread.sleep(50);
                System.out.println(50);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JSONObject jsonobj = new JSONObject(json[0]);//TODO:替换成速度更快的方式

        //System.out.println(json.toString());

        return jsonobj;
    }

    private int getlen(String type){
        try {
            JSONObject jobj = parsejson("https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page=10000&size=100");//TODO确定溢出
            JSONObject jobj2 = (JSONObject) jobj.get("pagination");
            return (int) jobj2.get("total");

        }
        catch(Exception e){
            System.out.println(e);
            return 0 ;
        }

    }

//    private void getnewest(){
//
//    }
    private Object get(JSONObject jobj,String key,Object other){
        try {
            Object ans =  jobj.get(key);
            if(ans.equals(null)){
                return other ;

            }
            else{
                return ans ;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return other ;

    }
    private void addlist_at_tail(JSONObject json,String type){
        try {
            JSONArray list = json.getJSONArray("data");
                    //(List<JSONObject>) json.get("data");
            /*
            for(int i = 0 ; i < list.length();i++){
                JSONObject jobj = (JSONObject) list.get(i);
                news News = new news(get(jobj,"_id","").toString(),type,get(jobj,"title","").toString(),get(jobj,"category","").toString(),get(jobj,"time","").toString(),get(jobj,"lang","").toString(),(float)get(jobj,"influence",0.f),get(jobj,"content","").toString(),false);
                if(type.equals("news")){
                    news_n.add(News);
                }
                else{
                    news_p.add(News);
                }
            }
            */
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
    private void refresh(String type){//最新页+次新页，可以改动
        int nowlen ;
        if(type.equals("news")){
            nowlen = number_endn;
            news_n.clear();
        }
        else{
            nowlen = number_endp;
            news_p.clear();
        }
        int newlen = getlen(type);

        if(newlen == nowlen){
            //TODO:弹出消息。无新消息可更新
            return ;
        }
        Integer pagenumber = newlen/10 ;
        int remain = newlen%10 ;
        if(type.equals("news")){
            number_endn = newlen;
            number_startn = ((newlen-1)/10)*10 ;

        }
        else{
            number_endp = newlen;
            number_startn = ((newlen-1)/10)*10 ;
        }



        try {
            addlist_at_tail(parsejson("https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page="+pagenumber.toString()+"&size=10"),type);
            if(remain != 0){
                addlist_at_tail(parsejson("https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page="+((Integer)(pagenumber+1)).toString()+"&size=10"),type);

            }
        }
        catch(Exception e){
            System.out.println(e);
        };





    }

    public void initialize() throws IOException, JSONException {
        number_endp = 0;
        number_endn = 0;
        refresh("news");
        refresh("paper");

        //初始化lenth和newsids，把news除了context之外的信息都存起来。context设为空
//        InputStream is = new URL("https://covid-dashboard.aminer.cn/api/dist/events.json").openStream();
//        try {
//            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
//            String jsonText = readAll(rd);
//            JSONObject json = new JSONObject(jsonText);
//            System.out.println(json.toString());
//
//
//        } finally {
//            is.close();
//            System.out.println("Initialze News Failed in newshandler");
//        }
//        return ;

    }
}
