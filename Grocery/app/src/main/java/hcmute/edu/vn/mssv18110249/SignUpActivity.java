package hcmute.edu.vn.mssv18110249;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListPopupWindow;
import android.widget.Spinner;

import java.lang.reflect.Field;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    Spinner spnDay, spnMonth, spnYear;
    ImageButton btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        spnDay = (Spinner)findViewById(R.id.spnDay);
        spnMonth = (Spinner)findViewById(R.id.spnMonth);
        spnYear = (Spinner)findViewById(R.id.spnYear);

        final List<Integer> days = getDays(31);
        ArrayAdapter<Integer> daysAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, days);
        spnDay.setAdapter(daysAdapter);

        final List<Integer> months = getMonths();
        ArrayAdapter<Integer> monthsAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, months);
        spnMonth.setAdapter(monthsAdapter);

        final List<Integer> years = getYears(2021);
        ArrayAdapter<Integer> yearsAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, years);
        spnYear.setAdapter(yearsAdapter);

        btnClose = (ImageButton) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public List<Integer> getDays(int maxDay){
        List<Integer> days = new ArrayList<Integer>();
        for (int day = 1; day <= maxDay; day++)
            days.add(day);
        return days;
    }

    public List<Integer> getMonths(){
        List<Integer> months = new ArrayList<Integer>();
        for (int month = 1; month <= 12; month++)
            months.add(month);
        return months;
    }

    public List<Integer> getYears(int now){
        List<Integer> years = new ArrayList<Integer>();
        for (int year = now - 40; year <= now; year++)
            years.add(year);
        return years;
    }
}