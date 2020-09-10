package com.java.tangningjing.bean;

import android.util.Pair;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.ArrayList;

public class entity extends SugarRecord implements Serializable {
    public double hot ;
    public String label ;
    public String url ;
    public String info ;//在三个里面找；
    public String img ;
    public ArrayList<pair> properties ;
    public ArrayList<relations> rel ;

    public entity(){

    }
    public entity(double _hot, String _label,String _url,String _info,ArrayList<pair>_properties,ArrayList<relations> _rel,String _img){
        this.hot = _hot ;
        this.info = _info ;
        this.url = _url ;
        this.rel = _rel ;
        this.properties = _properties;
        this.label = _label;
        this.img = _img;


    }

    public String getImg() {
        return img;
    }

    public String getUrl() {
        return url;
    }

    public String getLabel() {
        return label;
    }

    public double getHot() {
        return hot;
    }

    public ArrayList<pair> getProperties() {
        return properties;
    }

    public String getInfo() {
        return info;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setHot(double hot) {
        this.hot = hot;
    }
    public void setInfo(String info1,String info2,String info3) {
        if(!info1.equals("")){
            this.info = info1 ;
            return ;

        }
        if(!info2.equals("")){
            this.info = info2 ;
            return ;
        }
        if(!info3.equals("")){
            this.info = info2 ;
            return ;
        }

    }

    public void setProperties(ArrayList<pair> properties) {
        this.properties = properties;
    }

    public void setRel(ArrayList<relations> rel) {
        this.rel = rel;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
