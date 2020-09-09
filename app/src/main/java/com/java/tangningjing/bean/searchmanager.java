package com.java.tangningjing.bean;


import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.RequiresApi;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.Integer.min;

public class searchmanager {
    public int number_latest ;//最新的编号，已经搞好了
    public int number_oldest ;//最旧的编号，就还没有
    public String keyword ;
    public ArrayList<news> newslist ;//
    private int count ;
    private int total ;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public searchmanager() {

        newslist = new ArrayList<news>();
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
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            json[0] = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }


        JSONObject jsonobj = new JSONObject(json[0]);//TODO:替换成速度更快的方式

        //System.out.println(json.toString());

        return jsonobj;
    }

    private int getlen(){
        try {
            JSONObject jobj = parsejson("https://covid-dashboard.aminer.cn/api/events/list?type=" + "all" + "&page=10000&size=100");//TODO确定溢出
            JSONObject jobj2 = (JSONObject) jobj.get("pagination");
            return (int) jobj2.get("total");

        }
        catch(Exception e){
            System.out.println(e);
            return 0 ;
        }

    }

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

    private boolean contain_keyword(JSONObject jsobj,String keyword){

        if(get(jsobj,"title","").toString().contains(keyword)){
            return true ;
        }
        if(get(jsobj,"content","").toString().contains(keyword)){
            return true ;
        }
        if(get(jsobj,"seg_text","").toString().contains(keyword)){
            return true ;
        }

        if(get(jsobj,"source","").toString().contains(keyword)){
            return true ;
        }

        if(get(jsobj,"time","").toString().contains(keyword)){
            return true ;
        }
        return false ;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addlist_at_tail(JSONObject json, int start, int end){
        try {
            JSONArray list = json.getJSONArray("data");
            //(List<JSONObject>) json.get("data");

            for(int i = start ; i < min(end,list.length());i++){
                if(count >= 20){
                    return;
                }
                this.number_oldest -= 1;
                boolean contain = false ;
                //搜索标题
                contain = contain_keyword((JSONObject) list.get(i),keyword);

                if(!contain){//不包含

                    continue ;

                }
                JSONObject jobj = (JSONObject) list.get(i);
                news News = new news(get(jobj,"_id","").toString(),get(jobj,"type","").toString(),get(jobj,"title","").toString(),get(jobj,"category","").toString(),get(jobj,"time","").toString(),get(jobj,"lang","").toString(),get(jobj,"content","").toString(),false,get(jobj,"source","").toString());
                newslist.add(News);
                count++ ;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Integer calculate_pagenumber(int len){
        return ((total-number_oldest)/len)+1;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    synchronized public void getmore(){
        newslist.clear();
        count = 0 ;

        //TODO:确定一下多线程
        while (true) {
            System.out.println(this.number_oldest);
            if (number_oldest <= 0 || count >= 20) {
                return;
            }
            Integer pagenumber = calculate_pagenumber(100);


            final String[] json = new String[1];

            OkHttpClient okHttpClient = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url("https://covid-dashboard.aminer.cn/api/events/list?type=" + "all" + "&page=" + pagenumber.toString() + "&size=100")
                    .build();
            final Call call = okHttpClient.newCall(request);


            try {
                Response response = call.execute();
                json[0] = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            JSONObject jsonobj = null ;
            try {
                jsonobj = new JSONObject(json[0]);//TODO:替换成速度更快的方式
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("Fail in get jsonobj when refresh");
            }

            JSONObject jobj2 = null;
            int newtotal = 0 ;
            try {
                jobj2 = (JSONObject) jsonobj.get("pagination");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                newtotal = (int) jobj2.get("total");
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("Fail in get jsonobj when refresh");
            }

            if(newtotal != total){
                total= newtotal ;
                continue ;
            }
            try {
                addlist_at_tail(jsonobj,(total-number_oldest)%100,100);
                for (news ns:newslist){
                    System.out.println(ns.title);
                }

                System.out.println("===========");
            }

            catch(Exception e){
                System.out.println(e);
            };

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    synchronized public void search(String keyword){//从start搜
        this.keyword = keyword ;
        this.number_oldest = getlen();
        this.total = this.number_oldest ;
        this.number_latest = this.number_oldest;
        getmore();


        //拉取100条

    }

}
