package com.example.myapplication.bean;

import com.orm.SugarRecord;
import com.orm.dsl.Table;


public class news extends SugarRecord {
    public String uid ;
    public String type ;
    public String title ;
    public String category ;
    public String time ;
    public String lang ;
    public float influence ;
    public boolean cached ;
    public String context ;
    public String source ;

    news(){};
    news(String _id,String _type,String _title,String _category,String _time,String _lang,float _influence,String _context,boolean _cached){
        uid = _id ;
        type = _type ;
        title = _title ;
        category = _category ;
        time = _time ;
        lang = _lang ;
        influence = _influence;
        cached = false ;
        context = _context ;
    }


}
