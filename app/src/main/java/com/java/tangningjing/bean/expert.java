package com.java.tangningjing.bean;
import com.orm.SugarRecord;
import com.orm.dsl.Table;


public class expert extends SugarRecord {
    public String uid ;
    public profile pro;
    public indices ind ;
    boolean is_passedaway ;
    public String avatar ;
//    public double activity ;
//    public int citations ;
//    public double diversity ;
//    public double gindex ;
//    public float hindex ;
//    public float newstar ;
//    public float risingstar ;
//    public float sociability ;
    public String name ;
    public String name_zh ;



    expert(String _uid,profile _pro,indices _ind,boolean _is_passedaway,String _avator,String _name,String _name_zh){
        uid = _uid ;
        ind = _ind ;
        pro = _pro ;
        is_passedaway = _is_passedaway ;
        avatar=_avator;
        name = _name ;
        name_zh =_name_zh;
    }

    expert(){

    }

}
