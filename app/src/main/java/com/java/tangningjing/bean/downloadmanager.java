package com.java.tangningjing.bean;

import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class downloadmanager {
    downloadmanager(){
    }
    public static JSONObject parsejson(String url){
        final String[] json = new String[1];

        Response response = null;
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url(url)
                    .build();
            final Call call = okHttpClient.newCall(request);

            response = null;

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

        JSONObject jsonobj = null;//TODO:替换成速度更快的方式
        try {
            jsonobj = new JSONObject(json[0]);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //System.out.println(json.toString());

        return jsonobj;

    }
        public static ArrayList<entity> entitydownload(String word){
        //entity.deleteAll(entity.class);
        ArrayList<entity> entities = new ArrayList<>() ;
        String url ="https://innovaapi.aminer.cn/covid/api/v1/pneumonia/entityquery?entity="+word;
        JSONObject jobj = parsejson(url);

        try {
            JSONArray entitylist = jobj.getJSONArray("data");

            for(int i = 0 ; i < entitylist.length();i++){
                try {
                    JSONObject obj = entitylist.getJSONObject(i);
                    JSONObject abstractinfo = obj.getJSONObject("abstractInfo");
                    JSONObject covid_obj =  abstractinfo.getJSONObject("COVID");
                    JSONObject pro_obj = covid_obj.getJSONObject("properties");
                    ArrayList<pair> properties = new ArrayList<>() ;
                    Iterator it = pro_obj.keys();
                    while(it.hasNext()){
                        String key = (String) it.next();
                        String value = pro_obj.getString(key);
                        pair pa = new pair(key,value);
                        properties.add(pa);
                    }
                    JSONArray rel_obj = covid_obj.getJSONArray("relations");
                    ArrayList<relations>  rels = new ArrayList<>();
                    for(int j = 0 ; j < rel_obj.length();j++){
                        if(j >= 5){
                            break ;
                        }
                        JSONObject robj = rel_obj.getJSONObject(j);

                        relations relation = new relations(robj.getString("relation"),robj.getString("url"),robj.getString("label"),robj.getBoolean("forward"));
                        rels.add(relation);
                    }

                    entity ent = new entity(obj.getDouble("hot"),obj.getString("label"),obj.getString("url"),"",properties,rels,obj.getString("img"));
                    ent.setInfo( ((JSONObject)obj.get("abstractInfo")).getString("enwiki"),((JSONObject)obj.get("abstractInfo")).getString("baidu"),((JSONObject)obj.get("abstractInfo")).getString("zhwiki"));
                    entities.add(ent);
                    ent.save();

                }
                catch(Exception e){
                    e.printStackTrace();
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();

        }

        //        String id = jobj.get("data")
//        expert exp = new expert();
        return entities;

    }
    private static Object get(JSONObject jobj,String key,Object other){
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

    public static ArrayList<expert> expertdownload(boolean ispassedaway){
        ArrayList<expert> experts = new ArrayList<expert>();
        String url ="https://innovaapi.aminer.cn/predictor/api/v1/valhalla/highlight/get_ncov_expers_list?v=2";
        JSONObject jobj = parsejson(url);
        try {
            JSONArray expertlist = jobj.getJSONArray("data");

            for(int i = 0 ; i < expertlist.length();i++){
                try {
                    JSONObject obj = (JSONObject) expertlist.get(i);
                    JSONObject profile_obj = (JSONObject) obj.get("profile");
                    profile pro = new profile();
                    //pro.setAddress(profile_obj.get("address").toString());
                    pro.setAffiliation_zh(get(obj,"affiliation_zh","").toString());
                    pro.setBio(get(obj,"bio","").toString());
                    pro.setEdu(get(obj,"edu","").toString());
                    pro.setHomepage(get(obj,"homepage","").toString());
                    //pro.setNote(profile_obj.getString("note"));
                    pro.setPosition(get(obj,"position","").toString());
                    pro.setWork(get(obj,"work","").toString());

                    JSONObject indices_obj = (JSONObject) obj.get("indices");
                    indices ind = new indices();
                    ind.setAcitivity(indices_obj.getDouble("activity"));
                    ind.setCitations(indices_obj.getInt("citations"));
                    ind.setDiversity(indices_obj.getDouble("diversity"));
                    ind.setGindex(indices_obj.getInt("gindex"));
                    ind.setHindex(indices_obj.getInt("hindex"));
                    ind.setNewStar(indices_obj.getDouble("newStar"));
                    ind.setPubs(indices_obj.getInt("pubs"));
                    ind.setRisingStar(indices_obj.getDouble("risingStar"));
                    ind.setSociability(indices_obj.getDouble("sociability"));
                    if(obj.getBoolean("is_passedaway")==ispassedaway) {
                        expert exp = new expert(get(obj,"id","").toString(), pro, ind, obj.getBoolean("is_passedaway"), get(obj,"avatar","").toString(),  get(obj,"name","").toString(),  get(obj,"name_zh","").toString());
                        experts.add(exp);
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }

        //        String id = jobj.get("data")
//        expert exp = new expert();
        return experts ;

    }


}
