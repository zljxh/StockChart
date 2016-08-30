package com.test.stockchart.chartview;

/**
 * Created by user on 2016/8/30.
 */
public class StockDataEntity {
    public double open;// 开盘价
    public double high;// 最高价
    public double low;// 最低价
    public double close;// 收盘价
    public double dealnum;// 成交量
    public String date;// 日期，如：2013-09-18

    public StockDataEntity() {
    }

    public StockDataEntity(double open, double high, double low, double close, double dealnum, String date) {
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.dealnum = dealnum;
        this.date = date;
    }
}
