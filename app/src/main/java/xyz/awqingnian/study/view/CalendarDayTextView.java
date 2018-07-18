package xyz.awqingnian.study.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created 2018/7/18.
 * 继承 Android 系统已有的控件（AppCompatTextView）
 *
 * @author HongTao
 */

public class CalendarDayTextView extends android.support.v7.widget.AppCompatTextView {
    /**
     * 自定义控件的数据部分
     */
    public boolean isToday = false;
    private Paint mPaint = new Paint();

    public CalendarDayTextView(Context context) {
        super(context);
        initControl();
    }

    public CalendarDayTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initControl();
    }

    public CalendarDayTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl();
    }

    private void initControl() {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 自定义控件的数据逻辑操作部分
        if (isToday) {
            canvas.translate(getWidth() / 2, getHeight() / 2);
            canvas.drawCircle(0, 0, getWidth() / 2, mPaint);
        }
    }
}
