package xyz.awqingnian.study.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import xyz.awqingnian.study.R;

/**
 * Created 2018/7/18.
 *
 * @author HongTao
 */
public class NewCalendar extends LinearLayout {
    /**
     * 上一个月的 ImageView，点击绘制上一个月的日历
     */
    private ImageView mIvPrev;

    /**
     * 下一个月的 ImageView，点击绘制下一个月的日历
     */
    private ImageView mIvNext;

    /**
     * 当前月份及年份的展示
     */
    private TextView mTvDate;

    /**
     *  6 * 7 的 GridView 展示 42 天（包含当前月以及上个月和下个月几天的日历）
     */
    private GridView mGvDate;

    /**
     * 获取当前系统日历情况
     */
    private Calendar mCalendar = Calendar.getInstance();

    /**
     * 默认日期的格式化方式（可通过 attrs 中的 dateFormat 进行自定义）
     */
    private String displayFormat = "yyyy M";

    /**
     * 日历中的事件回调
     */
    public NewCalendarListener mNewCalendarListener;

    public NewCalendar(Context context) {
        super(context);
    }

    public NewCalendar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
    }

    public NewCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }

    private void initControl(Context context, AttributeSet attrs) {
        binView(context);
        bindViewEvent();
        // 获取对应控件的属性
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.NewCalendar);
        // R.styleable.NewCalendar_dateFormat 此值是 Android 通过 attrs.xml 拼接生成的值
        String format = typedArray.getString(R.styleable.NewCalendar_dateFormat);
        if (format != null) {
            displayFormat = format;
        }
        typedArray.recycle();

        renderCalender();
    }

    /**
     * 加载布局文件，并且 findViewById 找到布局文件中各控件
     *
     * @param context
     */
    private void binView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.calendar_view, this);

        mIvPrev = view.findViewById(R.id.calendar_iv_prev);
        mIvNext = view.findViewById(R.id.calendar_iv_next);
        mTvDate = view.findViewById(R.id.calendar_tv_date);
        mGvDate = view.findViewById(R.id.calender_gv_date);
    }

    /**
     * 设置监听事件的接口回调
     */
    private void bindViewEvent() {
        mIvPrev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendar.add(Calendar.MONTH, -1);
                renderCalender();
            }
        });
        mIvNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendar.add(Calendar.MONTH, 1);
                renderCalender();
            }
        });
    }

    /**
     * 绘制日历的具体逻辑方法实现
     */
    private void renderCalender() {
        SimpleDateFormat sdf = new SimpleDateFormat(displayFormat);
        mTvDate.setText(sdf.format(mCalendar.getTime()) + "月");

        List<Date> cells = new ArrayList<>();
        // 克隆一份当前系统月份的日历
        Calendar calendar = (Calendar) mCalendar.clone();
        // 将克隆日历设置成当月第一天（1号）
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        // 计算上个月剩余的天数要在这个月显示（假如当前 1 号是星期三，则返回 4 - 1，即上个月还有 3 天要在这个月显示）
        int pervDays = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        // 克隆日历向前移 prevDays （则移到 UI 开始绘制的第一天）
        calendar.add(Calendar.DAY_OF_MONTH, -pervDays);
        // 整个 GridView 需要绘制的最大行数
        int maxCellCount = 6 * 7;
        // 填充需要绘制的 Date 实例,经过向前移的克隆日历添加到 cells 集合中.
        while (cells.size() < maxCellCount) {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        mGvDate.setAdapter(new CalendarAdapter(getContext(), R.layout.day_textview, cells));
        //日历中日期长按监听监听回调
        mGvDate.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (mNewCalendarListener == null) {
                    return false;
                } else {
                    mNewCalendarListener.onItemLongPress((Date) parent.getItemAtPosition(position));
                    return true;
                }
            }
        });
    }

    /**
     * 日历控件的 Adapter
     */
    private class CalendarAdapter extends ArrayAdapter<Date> {

        LayoutInflater mLayoutInflater;

        public CalendarAdapter(@NonNull Context context, int resource, List<Date> dates) {
            super(context, resource, dates);
            mLayoutInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Date date = getItem(position);
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.day_textview, parent, false);
            }
            int day = date.getDate();
            TextView calendarTvDay = convertView.findViewById(R.id.date_tv_calendar_day);
            calendarTvDay.setText(String.valueOf(day));

            Date now = new Date();
            if (date.getYear() == now.getYear() && date.getMonth() == now.getMonth()) {
                // 若是系统当前月份的日期，则字体为黑色
                calendarTvDay.setTextColor(Color.parseColor("#000000"));
            } else {
                // 若不是是系统当前月份的日期，则字体为灰色
                calendarTvDay.setTextColor(Color.parseColor("#666666"));
            }
            // 若是当天，则字体为蓝色
            if (date.getYear() == now.getYear() && date.getMonth() == now.getMonth() && date.getDate() == now.getDate()) {
                calendarTvDay.setTextColor(Color.BLUE);
                ((CalendarDayTextView) calendarTvDay).isToday = true;
            }
            return convertView;
        }
    }

    /**
     * 日历监听回调接口
     */
    public interface NewCalendarListener {
        /**
         * 日历长按监听回调方法
         *
         * @param date
         */
        void onItemLongPress(Date date);
    }

}
