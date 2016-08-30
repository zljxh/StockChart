package com.test.stockchart.chartview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zl on 2016/8/28.
 */
public class DayKView extends GridChartView {

    private int default_lines = 50;//一屏内k线的个数
    private int color_green = 0x008B00;
    private int color_red = 0xCD2626;
    private float itemK_width;//每个线的宽度
    private float item_between_distance;//每两个k线间的距离
    private static final float RATING = 4;//两个k线间的间距与k先宽度比

    private Double ratingHeight;

    private Paint paint_line;
    private Paint paint_rect;
    private ArrayList<StockDataEntity> initDatas = new ArrayList<>();//总数居
    private ArrayList<StockDataEntity> paintDatas = new ArrayList<>();//只需要default_lines条数据
    private List<Double> maxPriceDatas = new ArrayList<>();
    private List<Double> minPriceDatas = new ArrayList<>();
    private List<Double> maxTradeDatas = new ArrayList<>();

    private float x;
    private int startTemp = 0;
    private int end = 0;
    private ArrayList<StockDataEntity> moveDatas = new ArrayList<>();


    public DayKView(Context context) {
        super(context);
    }

    public DayKView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        paint_line = new Paint();
        paint_line.setAntiAlias(true);
        paint_line.setStrokeWidth(1);

        paint_rect = new Paint();
        paint_rect.setAntiAlias(true);
        paint_rect.setStyle(Paint.Style.FILL);
    }

    public DayKView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DayKView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        drawKLine();
        super.onDraw(canvas);
        drawKLine_Rect(canvas);
    }


    private void setItemWidth(int default_lines) {
        this.default_lines = default_lines;
        itemK_width = (width * RATING / (1 + RATING)) / default_lines;
        item_between_distance = (width / (1 + RATING)) / default_lines;

    }

    private void drawKLine() {
        paintDatas.clear();
        maxPriceDatas.clear();
        minPriceDatas.clear();
        maxTradeDatas.clear();

        if (moveDatas != null) {
            if (moveDatas.size() <= default_lines) {
//                paintDatas.addAll(datas);
                for (int i = moveDatas.size() - 1; i >= 0; i--) {
                    paintDatas.add(moveDatas.get(i));
                    maxPriceDatas.add(moveDatas.get(i).high);
                    minPriceDatas.add(moveDatas.get(i).low);
                    maxTradeDatas.add(moveDatas.get(i).dealnum);
                }
                setItemWidth(moveDatas.size());

            } else {
                setItemWidth(default_lines);
                for (int i = moveDatas.size() - 1; i > moveDatas.size() - default_lines - 1; i--) {
                    paintDatas.add(moveDatas.get(i));
                    maxPriceDatas.add(moveDatas.get(i).high);
                    minPriceDatas.add(moveDatas.get(i).low);
                    maxTradeDatas.add(moveDatas.get(i).dealnum);
                }
            }
        } else {
            return;
        }
        setMaxAndMinPrice();


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int start = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                start = 0;
                break;

            case MotionEvent.ACTION_MOVE:

                start = (int) ((event.getX() - x) % (itemK_width + item_between_distance));
                end = start + default_lines;

                if (initDatas.size() > default_lines && startTemp+start > 0 && end +startTemp< initDatas.size()) {
                    moveDatas = getMoveDate(startTemp+start);
                    postInvalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
                startTemp=startTemp+start;

                if (initDatas.size() > default_lines && startTemp> 0 && end+startTemp < initDatas.size()) {

                    moveDatas = getMoveDate(startTemp);
                    postInvalidate();
                }
                break;
        }
        return true;
    }

    private void drawKLine_Rect(Canvas canvas) {
        for (int i = 0; i < paintDatas.size(); i++) {
            StockDataEntity entity = paintDatas.get(i);
            double min;
            double max;
            if (entity.open > entity.close) {
                paint_rect.setColor(Color.GREEN);
                paint_line.setColor(Color.GREEN);
                min = entity.close;
                max = entity.open;
            } else if (entity.open == entity.close) {
                paint_rect.setColor(Color.RED);
                paint_line.setColor(Color.RED);
                min = entity.close;
                max = min + 5;
            } else {
                paint_rect.setColor(Color.RED);
                paint_line.setColor(Color.RED);
                min = entity.open;
                max = entity.close;
            }
            drawItem(i, min, max, entity.high, entity.low, canvas, entity.dealnum);
        }
    }

    private void drawItem(int i, double min, double max, double high, double low, Canvas canvas, double dealnum) {
        canvas.drawLine((float) (rectWidth - (rectWidth / default_lines) * (0.5 + i) + textWidth), (float) ((max_price - high) * ratingHeight + rowHeight / 2), (float) (rectWidth - (rectWidth / default_lines) * (0.5 + i) + textWidth), (float) ((max_price - low) * ratingHeight + rowHeight / 2), paint_line);
        Rect rect = new Rect((int) (rectWidth - (rectWidth / default_lines) * i - itemK_width + textWidth), (int) ((max_price - max) * ratingHeight + rowHeight / 2), (int) (rectWidth - (rectWidth / default_lines) * i + textWidth), (int) ((max_price - min) * ratingHeight + rowHeight / 2));
        canvas.drawRect(rect, paint_rect);
        Rect rectTrade = new Rect((int) (rectWidth - (rectWidth / default_lines) * i - itemK_width + textWidth), (int) (height - (dealnum / max_trade) * rowHeight * XIA_ORIENTATION_LINE) - 2, (int) (rectWidth - (rectWidth / default_lines) * i + textWidth), height - 2);
        canvas.drawRect(rectTrade, paint_rect);
    }

    //最低价,最高价,最大成交量
    private void setMaxAndMinPrice() {

        max_price = Collections.max(maxPriceDatas);
        min_price = Collections.min(minPriceDatas);
        max_trade = Collections.max(maxTradeDatas);
        ratingHeight = (height * (SHANG_ORIENTATION_LINE - 1) / (SHANG_ORIENTATION_LINE + XIA_ORIENTATION_LINE)) / (max_price - min_price);
    }

    public void setDatas(ArrayList<StockDataEntity> date) {
        initDatas.addAll(date);

        moveDatas = date;
        postInvalidate();
    }

    private ArrayList<StockDataEntity> getMoveDate(int start) {
        moveDatas.clear();
        ArrayList<StockDataEntity> data = new ArrayList<>();
        for (int i = default_lines - 1; i >= 0; i--) {
            data.add(initDatas.get(initDatas.size() - 1 - start - i));
        }

        return data;
    }


}
