package com.java.tangningjing.bean;

import com.orm.SugarRecord;

import java.io.Serializable;

public class relations extends SugarRecord implements Serializable {
    public String relation ;
    public String url ;
    public String label ;
    public boolean forward ;

    relations(){

    }

    relations(String _relation,String _url,String _label,boolean _forward){
        relation = _relation ;
        url = _url ;
        label = _label ;
        forward = _forward;
    }

    public String getLabel() {
        return label;
    }

    public String getRelation() {
        return relation;
    }

    public String getUrl() {
        return url;
    }

    public boolean isForward() {
        return forward;
    }

    public void setForward(boolean forward) {
        this.forward = forward;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
