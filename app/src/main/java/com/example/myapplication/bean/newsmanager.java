package com.example.myapplication.bean;

import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.Integer.min;

public class newsmanager {

    public int number_start ;
    public int number_end ;
    public String type ;
    public ArrayList<news> newslist ;//越新越靠前
    public int count ;
    public int total ;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public newsmanager(String _type) {
        type = _type;
        newslist = new ArrayList<news>();
        try{
            initialize();
        }
        catch(Exception e){
            System.out.println("Initialze News Failed in newshandler Construct");
        }
    }


    static private JSONObject parsejson(String url) throws JSONException {
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

    private int getlen(){
        try {
            JSONObject jobj = parsejson("https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page=10000&size=20");//TODO确定溢出
            JSONObject jobj2 = (JSONObject) jobj.get("pagination");
            return (int) jobj2.get("total");

        }
        catch(Exception e){
            System.out.println(e);
            return 0 ;
        }

    }

    static private Object get(JSONObject jobj,String key,Object other){
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

    private boolean check_cached(String id){
        ArrayList<news> NEWS;
        List<news> books = news.listAll(news.class);
        try{
            NEWS = (ArrayList<news>) news.find(news.class, "uid = ? ", id);
        }
        catch(Exception e){
            e.printStackTrace();
            return false ;
        }
        return true ;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addlist_at_tail(JSONObject json, int start, int end){
        try {
            JSONArray list = json.getJSONArray("data");
            //(List<JSONObject>) json.get("data");

            for(int i = start ; i < min(end,list.length());i++){
                JSONObject jobj = (JSONObject) list.get(i);
                news News = new news(get(jobj,"_id","").toString(),type,get(jobj,"title","").toString(),get(jobj,"category","").toString(),get(jobj,"time","").toString(),get(jobj,"lang","").toString(),(float)get(jobj,"influence",0.f),get(jobj,"content","").toString(),check_cached(get(jobj,"_id","").toString()),get(jobj,"source","").toString());
                newslist.add(News);
                count+= 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    synchronized public void refresh(){//最新页+次新页，可以改动
        //TODO:确定一下多线程
        int nowlen = number_end ;


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
                .url("https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page="+"1"+"&size=20")
                .build();
        final Call call = okHttpClient.newCall(request);

        try {
            Response response = call.execute();
            json[0] = response.body().string();
        }
        catch (IOException e) {
            e.printStackTrace();
        }


        //        System.out.println(json.toString());
//        try {
//            while(json[0] == null){
//                Thread.sleep(50);
//                System.out.println(50);
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        JSONObject jsonobj = null ;
        try {
            jsonobj = new JSONObject(json[0]);//TODO:替换成速度更快的方式
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Fail in get jsonobj when refresh");
        }

        //System.out.println(json.toString());

        JSONObject jobj2 = null;
        try {
            jobj2 = (JSONObject) jsonobj.get("pagination");
        } catch (JSONException e) {
            e.printStackTrace();

        }

        int newlen = nowlen ;
        try {
            newlen = (int) jobj2.get("total");
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Fail in get jsonobj when refresh");
        }

        if(newlen == nowlen){
            //TODO:弹出消息。无新消息可更新
            return ;
        }
        newslist.clear();
//        Integer pagenumber = newlen/10 ;
//        int remain = newlen%10 ;
//        if(type.equals("news")){
//            number_endn = newlen;
//            number_startn = ((newlen-1)/10)*10 ;
//
//        }
//        else{
//            number_endp = newlen;
//            number_startn = ((newlen-1)/10)*10 ;
//        }



        try {
            addlist_at_tail(jsonobj,0,20);

        }

        catch(Exception e){
            System.out.println(e);
        }

        number_end = newlen;
        number_start = number_end-20 ;



    }
    public Integer calculate_pagenumber(int len){
        return ((total-number_start)/len)+1;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    synchronized public void getmore(){
        newslist.clear();
        total = number_end;
        count = 0 ;
        //TODO:确定一下多线程
        while (true) {
            if (number_start <= 0 || count >= 20) {
                return;
            }
            Integer pagenumber = calculate_pagenumber(20);


            final String[] json = new String[1];



            OkHttpClient okHttpClient = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url("https://covid-dashboard.aminer.cn/api/events/list?type=" + type + "&page=" + pagenumber.toString() + "&size=20")
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
                addlist_at_tail(jsonobj,(total-number_start)%20,20);

            }

            catch(Exception e){
                System.out.println(e);

            }

            this.number_start = this.number_start-count ;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initialize() throws IOException, JSONException {
        number_end = 0;
        number_start = 0;
        //refresh();


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
    static public news loadorstore(String id){
        ArrayList<news> NEWS;
        List<news> books = news.listAll(news.class);
        try{
            NEWS = (ArrayList<news>) news.find(news.class, "uid = ? ", id);
        }
        catch(Exception e){
            e.printStackTrace();
            NEWS = new ArrayList<news>() ;
        }

        if(NEWS.equals(null) || NEWS.size() == 0){
            try {
                JSONObject jobj2 = parsejson("https://covid-dashboard-api.aminer.cn/event/"+id);
                JSONObject jobj = (JSONObject) jobj2.get("data");

                news News = new news(get(jobj,"_id","").toString(),get(jobj,"title","").toString(),get(jobj,"title","").toString(),get(jobj,"category","").toString(),get(jobj,"time","").toString(),get(jobj,"lang","").toString(),(float)get(jobj,"influence",0.f),get(jobj,"content","").toString(),true,get(jobj,"source","").toString());

                News.save();
                return News ;

            } catch (JSONException e) {
                e.printStackTrace();
                return null ;
            }
        }
        else{
            return NEWS.get(0);
        }

    }
}

