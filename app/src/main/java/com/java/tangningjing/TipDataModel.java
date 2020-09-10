package com.java.tangningjing;

import com.easytagdragview.bean.SimpleTitleTip;
import com.easytagdragview.bean.Tip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/30 0030.
 */
public class TipDataModel {
    private static String[] dragTips ={"魅力13","魅力12","魅力11"};
    private static String[] addTips ={"数码","移动互联","云课堂","家居"};
    public static List<Tip>  getDragTips(){
        List<Tip> result = new ArrayList<>();
        for(int i=0;i<dragTips.length;i++){
            String temp =dragTips[i];
            SimpleTitleTip tip = new SimpleTitleTip();
            tip.setTip(temp);
            tip.setId(i);
            if (i==0){
                tip.setIsSelected(true);
            }else{
                tip.setIsSelected(false);
            }
            result.add(tip);
        }
        return result;
    }
    public static List<Tip> getAddTips(){
        List<Tip> result = new ArrayList<>();
        for(int i=0;i<addTips.length;i++){
            String temp =addTips[i];
            SimpleTitleTip tip = new SimpleTitleTip();
            tip.setTip(temp);
            tip.setId(i+dragTips.length);
            result.add(tip);
        }
        return result;
    }
}
