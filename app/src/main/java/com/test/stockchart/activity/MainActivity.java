package com.test.stockchart.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.test.stockchart.R;
import com.test.stockchart.chartview.Data;
import com.test.stockchart.chartview.DayKView;
import com.test.stockchart.chartview.StockDataEntity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DayKView dayKView;
    private ArrayList<StockDataEntity> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dayKView = (DayKView) findViewById(R.id.daykView);
        initData();
//        Log.i("zl", "dayKView=" + dayKView);

        dayKView.setDatas(arrayList);
    }

    private void initData() {
        try {
            JSONArray array = new JSONArray(Data.data);
//            Log.i("zl", "length=" + array.length());
            for (int i = 0; i < array.length(); i++) {
                JSONArray jsonArray = new JSONArray(array.get(i).toString());
                StockDataEntity entity = null;
//                for (int j = 0; j < jsonArray.length(); j++) {
                    entity = new StockDataEntity();
                    entity.low = jsonArray.getDouble(3);
                    entity.open = jsonArray.getDouble(1);
                    entity.high = jsonArray.getDouble(2);
                    entity.close = jsonArray.getDouble(4);
                    entity.dealnum = jsonArray.getDouble(6)/1000;

                    if (i==0){
//                        Log.i("zl","entity.high="+entity.high);
//                    }
                }
                arrayList.add(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



}
