package com.test.stockchart.chartview;

import java.text.DecimalFormat;

/**
 * Created by user on 2016/8/30.
 */
public class Utils {
    public static String getDouble(Double d) {
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(d);
    }
}
