package com.java.tangningjing.bean;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Pair;
import android.widget.Toast;


import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.java.tangningjing.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.Math.max;

public class datamanager {

    final String path = Environment.getDataDirectory().toString()+File.separator+"data"+File.separator+"com.java.tangningjing"+File.separator+"cache"+File.separator+"data.json";
    String mark ;

    public JSONObject storedjson ;
    final String url = "https://covid-dashboard.aminer.cn/api/dist/epidemic.json";

    public datamanager(){
        storedjson = null ;
    };
    synchronized public boolean getalldata(){

        BufferedWriter writer = null ;
        File file = new File(path);

        if(file.exists()){
            try{
                file.delete();
            }
            catch(Exception e){
                return false ;
            }
        }
        try {
            file.createNewFile();
        }catch(Exception e){
            e.printStackTrace();
            return false ;
        }

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

        try{
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,false)));
            writer.write(json[0]);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return false ;
        }


        JSONObject jsonobj = null;//TODO:替换成速度更快的方式
        try {
            jsonobj = new JSONObject(json[0]);
        } catch (JSONException e) {
            e.printStackTrace();
            return false ;
        }

        storedjson = jsonobj ;

        return true ;
    }
    synchronized public boolean load(Context context){
        BufferedReader reader = null;
        String laststr = "";
        File file = new File(path);
        if(!file.exists()){
            Toast.makeText(context,"下载未完成，请稍后重试",Toast.LENGTH_LONG).show();
            return false ;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "utf-8");
            reader = new BufferedReader(inputStreamReader);

            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                laststr += tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        JSONObject jsonobj = null;//TODO:替换成速度更快的方式
        try {
            storedjson = new JSONObject(laststr);
        } catch (JSONException e) {
            e.printStackTrace();
            return false ;
        }
        return true ;

    }

    public void getchart(Context context, String country, String region, String classify, LineChart linechart, Drawable drawable,String type) {//CURE DEAD CONFIRMED
        if (storedjson == null) {
            if(!load(context)) {
                Toast.makeText(context, "没有信息", Toast.LENGTH_LONG).show();
                return;
            }
        }
        mark = "";
        int index = 0;
        if (classify.equals("CONFIRMED")) {
            index = 0;
        } else if (classify.equals("CURED")) {
            index = 1;
        } else if (classify.equals("DEAD")) {
            index = 2;
        }


        Pair<ArrayList<int[]>, ArrayList<int[]>> ans = Search(country, region);
        if (ans == null) {
            Toast.makeText(context, "没有/未找到此地区信息",
                    Toast.LENGTH_SHORT).show();
            return;
        }


        Description des = new Description();
        des.setText(mark);
        linechart.setDescription(des);
        ArrayList<int[]> totallist;
        if(type .equals( "total")){
        totallist = ans.first;}
        else{
        totallist = ans.second;}
        

        //    ArrayList<String> xvalues = new ArrayList<String>();
//        for(int i = 0 ; i < 100; i++){
//            xvalues.add("第"+(i)+"天");
//        }
        ArrayList<Entry> yValues1 = new ArrayList<Entry>();
        //  ArrayList<Entry> yValues2 = new ArrayList<Entry>();
        //  ArrayList<Entry> yValues3 = new ArrayList<Entry>();

        for(int i = 0; i < totallist.size(); i++) {
            yValues1.add(new Entry(i, ((int[]) totallist.get(i))[index]));
        }
        final int len = totallist.size();
//        for (int i = 0; i < 100; i++) {
//            yValues2.add(new Entry((float) (0.5*i),i));
//
//        }
//        for(int i = 0 ; i < 100 ; i++){
//            yValues3.add(new Entry(i,i));
//        }
        LineDataSet dataSet1 = new LineDataSet(yValues1, classify);
        // LineDataSet dataSet2 = new LineDataSet(yValues1,"这是什么2");
        // LineDataSet dataSet3 = new LineDataSet(yValues1,"这是什么3");
//        ArrayList<LineDataSet> dataSets = new ArrayList<>();
//        dataSets.add(dataSet1);
        //initLineDataSet(lineDataSet, color, LineDataSet.Mode.LINEAR);
        LineData lineData = new LineData(dataSet1);
        //  lineData.addDataSet(dataSet2);
        //  lineData.addDataSet(dataSet3);

        linechart.setData(lineData);
        linechart.setDrawBorders(false); //在折线图上添加边框
        //linechart.setDescription("时间/数据"); //数据描述
        linechart.setDrawGridBackground(false); //表格颜色
        linechart.setGridBackgroundColor(Color.GRAY & 0x70FFFFFF); //表格的颜色，设置一个透明度
        linechart.setTouchEnabled(true); //可点击
        linechart.setDragEnabled(true);  //可拖拽
        linechart.setScaleEnabled(true);  //可缩放
        linechart.setPinchZoom(false);
        linechart.setBackgroundColor(Color.WHITE); //设置背景颜色

        linechart.setData(lineData);


//            Legend mLegend = linechart.getLegend(); //设置标示，就是那个一组y的value的
//            mLegend.setForm(Legend.LegendForm.SQUARE); //样式
//            mLegend.setFormSize(6f); //字体
//            mLegend.setTextColor(Color.GRAY); //颜色
        //linechart.setVisibleXRange(len-20,len);   //x轴可显示的坐标范围

        //x轴可显示的坐标范围
        XAxis xAxis = linechart.getXAxis();  //x轴的标示

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int IValue = (int) value;
                CharSequence format = DateFormat.format("MM/dd",
                        System.currentTimeMillis() - (long) (len - 1 - IValue) * 24 * 60 * 60 * 1000);
                return format.toString();
            }
        });
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x轴位置
        xAxis.setTextColor(Color.GRAY);    //字体的颜色
        xAxis.setTextSize(10f); //字体大小
        xAxis.setGridColor(Color.GRAY);//网格线颜色
        xAxis.setDrawGridLines(false); //不显示网格线
        YAxis axisLeft = linechart.getAxisLeft(); //y轴左边标示
        YAxis axisRight = linechart.getAxisRight(); //y轴右边
        axisLeft.setTextColor(Color.GRAY); //字体颜色
        axisLeft.setTextSize(10f); //字体大小
        axisLeft.setTextSize(10f); //字体大小
        //axisLeft.setAxisMaxValue(800f); //最大值
        axisLeft.setLabelCount(5, true); //显示格数
        axisLeft.setGridColor(Color.GRAY); //网格线颜色

        axisRight.setDrawAxisLine(false);
        axisRight.setDrawGridLines(false);
        axisRight.setDrawLabels(false);

        //设置动画效果
        System.out.println(len);
        linechart.setVisibleXRange(10, 10);


        linechart.animateY(2000, Easing.EasingOption.Linear);
        linechart.animateX(2000, Easing.EasingOption.Linear);
        linechart.moveViewToX(max(0, len - 10));
        if (linechart.getData() != null && linechart.getData().getDataSetCount() > 0) {
            LineDataSet lineDataSet = (LineDataSet) linechart.getData().getDataSetByIndex(0);
            //避免在 initLineDataSet()方法中 设置了 lineDataSet.setDrawFilled(false); 而无法实现效果
            lineDataSet.setDrawFilled(true);

            lineDataSet.setFillDrawable(drawable);
            linechart.invalidate();

        }
    }





    synchronized public Pair<ArrayList<int[]>,ArrayList<int[]>> Search(String country, String region){
        Pair<ArrayList<int[]>,ArrayList<int[]>> result = null;
        String key = country + "|"+ region ;
        JSONObject jobj ;

        if(storedjson.isNull(key) && storedjson.isNull(country)){
            return result ;
        }
        try{
            jobj = this.storedjson.getJSONObject(key);
            this.mark = key ;
        }
        catch(Exception e){
            e.printStackTrace();
            try{
                jobj = this.storedjson.getJSONObject(country);
                this.mark = country ;
            } catch (JSONException ex) {
                ex.printStackTrace();
                return result ;
            }
        }
        if(jobj==null){
            return result ;
        }
        if(jobj.isNull("data")){
            return result ;
        }
        JSONArray datalist = null ;
        try {
            datalist = jobj.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
            return result ;
        }
        ArrayList<int[]> listtotal = new ArrayList<>();
        ArrayList<int[]> listincrease = new ArrayList<>();
        for(int i = 0 ; i < datalist.length();i++){
            try {
                int[] shuzu = new int[3];
                JSONArray temp = datalist.getJSONArray(i);
                shuzu[0] = temp.getInt(0);
                shuzu[1] = temp.getInt(2);
                shuzu[2] = temp.getInt(3);
                listtotal.add(shuzu);


                if(i != 0){
                    int[] inc = new int[3];
                    inc[0] = listtotal.get(i)[0]-listtotal.get(i-1)[0];
                    inc[1] = listtotal.get(i)[1] - listtotal.get(i-1)[1];
                    inc[2] = listtotal.get(i)[2] - listtotal.get(i-1)[2];
                    listincrease.add(inc);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Pair<ArrayList<int[]>,ArrayList<int[]>> newresult = new Pair<>(listtotal,listincrease);



//
//        ArrayList<JSONObject> listtotal = datalist.toJavaList(JSONObject.class);
//        studentList = JSON.parseArray(datalist.toJSONString(), JSONObject.class);

        return newresult ;


    }

}
