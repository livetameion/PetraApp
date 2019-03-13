package com.yagna.petra.app.Util;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Sejal on 10-04-2017.
 */

public class P_SquareImageView extends android.support.v7.widget.AppCompatImageView {
    public P_SquareImageView(Context context) {
        super(context);
    }

    public P_SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public P_SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); // Snap to width
    }
}