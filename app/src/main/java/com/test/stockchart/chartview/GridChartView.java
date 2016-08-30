package com.test.stockchart.chartview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.test.stockchart.R;

/**
 * Created by zl on 2016/8/28.
 */
public class GridChartView extends View {
    private Paint linePaint;
    protected int width;
    protected int height;
    protected static final int SHANG_ORIENTATION_LINE = 6;
    protected static final int XIA_ORIENTATION_LINE = 2;
    protected static final int VERTICAL_LINE = 3;
    protected float rowHeight;
    protected float columnWidth;
    protected Double min_price;//最低价
    protected Double max_price;//最高价
    protected Double max_trade;//成交量
    private Paint paintText;
    protected int rectWidth;//矩形区域的宽度
    protected int textWidth;//文字区域的宽度

    public GridChartView(Context context) {
        super(context);
    }

    public GridChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DayKView);
        for (int i = 0; i < typedArray.length(); i++) {
//            typedArray.getType
            if (typedArray.getIndex(i) == R.styleable.DayKView_dayk_height) {
                height = typedArray.getDimensionPixelSize(R.styleable.DayKView_dayk_height, 240);
            }
        }
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        paintText = new Paint();

    }

    public GridChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GridChartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        width = widthSize;
        if (heightMode == MeasureSpec.EXACTLY) {
            height = Math.min(height, heightSize);
        }
        setMeasuredDimension(widthSize, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.rowHeight = h / (XIA_ORIENTATION_LINE + SHANG_ORIENTATION_LINE);
        this.columnWidth = w / (VERTICAL_LINE + 1);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
//        max_price = 34.96;
//        min_price = 27.62;
//        max_trade = 58.63;
        rectWidth = width;
        if (max_price != null && min_price != null) {
            Rect rectText = new Rect();
            paintText.setColor(Color.GRAY);
            paintText.setTextSize(15);
            paintText.getTextBounds(Utils.getDouble(max_price) + "", 0, Utils.getDouble(max_price).toString().length(), rectText);


            textWidth = rectText.width() + 12;
            rectWidth = width - textWidth;
            this.columnWidth = rectWidth / (VERTICAL_LINE + 1);
            drawShangText(canvas, rectText);
        }
        drawRect(canvas);
        drawShangLine(canvas);
        drawXia(canvas);
    }

    private void drawShangText(Canvas canvas, Rect rectText) {

        for (int i = 0; i < SHANG_ORIENTATION_LINE; i++) {
            if (i == SHANG_ORIENTATION_LINE - 1) {
                canvas.drawText(Utils.getDouble(min_price) + "", 0, Utils.getDouble(min_price).toString().length(), 6, rowHeight / 2 + rowHeight * i, paintText);
            } else {
                canvas.drawText(Utils.getDouble(max_price - (max_price - min_price) * i / (SHANG_ORIENTATION_LINE - 1)) , 0, Utils.getDouble(max_price - (max_price - min_price) * i / (SHANG_ORIENTATION_LINE - 1)).toString().length(), 6, rowHeight / 2 + rectText.height() + rowHeight * i, paintText);
            }
        }
        for (int i = 0; i < XIA_ORIENTATION_LINE + 1; i++) {
            if (i == XIA_ORIENTATION_LINE) {
                canvas.drawText("万手", 0, "万手".toString().length(), 6, rowHeight * SHANG_ORIENTATION_LINE + rowHeight * i - 3, paintText);
            } else {
                canvas.drawText(Utils.getDouble(max_trade - (max_trade * i) / (XIA_ORIENTATION_LINE)) , 0, Utils.getDouble(max_trade - (max_trade * i) / (XIA_ORIENTATION_LINE)).toString().length(), 6, rowHeight * SHANG_ORIENTATION_LINE + rowHeight * i + rectText.height(), paintText);
            }
        }
    }

    private void drawXia(Canvas canvas) {
        linePaint.setColor(Color.GRAY);
        linePaint.setStrokeWidth(1);
        for (int i = 0; i < XIA_ORIENTATION_LINE; i++) {
            canvas.drawLine(textWidth, (i + SHANG_ORIENTATION_LINE) * rowHeight, width, (i + SHANG_ORIENTATION_LINE) * rowHeight, linePaint);
        }
        for (int i = 0; i < VERTICAL_LINE; i++) {
            canvas.drawLine((i + 1) * columnWidth + textWidth, rowHeight * SHANG_ORIENTATION_LINE + 2, (i + 1) * columnWidth + textWidth, (SHANG_ORIENTATION_LINE + XIA_ORIENTATION_LINE) * rowHeight + 2, linePaint);
        }

    }

    private void drawShangLine(Canvas canvas) {
        linePaint.setColor(Color.GRAY);
        linePaint.setStrokeWidth(1);
        for (int i = 0; i < SHANG_ORIENTATION_LINE; i++) {
            canvas.drawLine(textWidth, i * rowHeight + rowHeight / 2, width, i * rowHeight + rowHeight / 2, linePaint);
        }
        for (int i = 0; i < VERTICAL_LINE; i++) {
            canvas.drawLine((i + 1) * columnWidth + textWidth, rowHeight / 2, (i + 1) * columnWidth + textWidth, SHANG_ORIENTATION_LINE * rowHeight - rowHeight / 2, linePaint);
        }
    }

    private void drawRect(Canvas canvas) {
        linePaint.setColor(Color.GRAY);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(2);
        Rect rect = new Rect(textWidth, 2, width - 2, height - 2);
        canvas.drawRect(rect, linePaint);
    }


}
