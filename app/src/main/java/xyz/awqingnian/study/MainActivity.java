package xyz.awqingnian.study;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import xyz.awqingnian.study.view.NewCalendar;

public class MainActivity extends AppCompatActivity implements NewCalendar.NewCalendarListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NewCalendar newCalendar = findViewById(R.id.main_nc_calendar);
        newCalendar.mNewCalendarListener = this;
    }

    @Override
    public void onItemLongPress(Date date) {
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        Toast.makeText(this, dateFormat.format(date), Toast.LENGTH_SHORT).show();
    }
}
