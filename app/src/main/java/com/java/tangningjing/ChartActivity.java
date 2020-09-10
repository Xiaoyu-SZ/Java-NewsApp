package com.java.tangningjing;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.java.tangningjing.bean.datamanager;

public class ChartActivity extends MainActivity {
    LineChart charta;
    LineChart chartb;
    datamanager Datamanager;
    Spinner spinner;
    ArrayAdapter<String> adapter;
    String type;
    EditText country;
    EditText region;
    Button btn;
    private static final String [] selectable ={"CURED","COMFIRMED","DEAD"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.chart_layout, null, false);
        drawer.addView(contentView, 0);
        final Drawable drawable = getResources().getDrawable(R.drawable.fade_blue);
        Datamanager = new datamanager();
        charta = contentView.findViewById(R.id.line_chart_first);
        chartb = contentView.findViewById(R.id.line_chart_second);
        country=contentView.findViewById(R.id.country);
        region=contentView.findViewById(R.id.region);
        spinner = contentView.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                type = selectable[arg2];

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        btn=contentView.findViewById(R.id.commitButton);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
              Datamanager.getchart(ChartActivity.this,country.getText().toString(),region.getText().toString(), type,charta,drawable,"total");
                Datamanager.getchart(ChartActivity.this,country.getText().toString(),region.getText().toString(), type,chartb,drawable,"all");
            }
        });
    }
}
