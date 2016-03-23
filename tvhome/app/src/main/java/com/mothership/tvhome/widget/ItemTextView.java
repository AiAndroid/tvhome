package com.mothership.tvhome.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by wangwei on 3/23/16.
 */
public class ItemTextView extends View{
    private static TextPaint sTextPaint = null;
    private String mText;

    public ItemTextView(Context context) {
        this(context, null);
    }

    public ItemTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if(sTextPaint==null){
            initTextPaint();
        }
    }

    private void initTextPaint(){
        sTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        sTextPaint.density = getResources().getDisplayMetrics().density;
        sTextPaint.setTextSize((TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics())));
        sTextPaint.setColor(0xFFFFFFFF);
    }
/*
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSize, 43);
    }
*/
    public void setText(String text){
        mText = text;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!TextUtils.isEmpty(mText)) {
            canvas.save();
            Rect rectS = new Rect();
            sTextPaint.getTextBounds(mText, 0, mText.length(), rectS);
            if (rectS.width() < getWidth()) {
                canvas.drawText(mText, (getWidth()-getPaddingLeft()-getPaddingRight() - rectS.width()) / 2, rectS.height()+getPaddingTop(), sTextPaint);
            } else {
                canvas.drawText(mText, 5, rectS.height()+getPaddingTop(), sTextPaint);
            }
            canvas.restore();
        }
    }


}
