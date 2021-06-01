package hcmute.edu.vn.mssv18110249;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import DBUtil.BillDB;
import Model.Bill;
import Model.Customer;
import Provider.HistoryListViewAdapter;
import Provider.SharedPreferenceProvider;
import Provider.UnitFormatProvider;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener{

    BottomNavigationView bottomNavigationView;
    Intent intent, intentNext;
    Customer customer;
    TextView txtViewTotalDate, txtViewTotalMonth;
    ListView lvHistory;

    List<Bill> bills;
    BillDB billDB;

    HistoryListViewAdapter historyListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        billDB = new BillDB(this);
        customer = (Customer) SharedPreferenceProvider.getInstance(this).get("customer");
        getView();
        setOnClick();

        bills = billDB.get(customer.getId());
        if (bills != null && bills.size() > 0) {
            historyListViewAdapter = new HistoryListViewAdapter(bills);
            lvHistory.setAdapter(historyListViewAdapter);
        }

        lvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(getApplicationContext(), BillActivity.class);
                intent.putExtra("bill", bills.get(position));
                intent.putExtra("previous", "history");
                startActivity(intent);
            }
        });

        loadView();

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.history);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.history:
                        return true;
                    case R.id.account:
                        intentNext = new Intent(getApplicationContext(), AccountActivity.class);
                        startActivity(intentNext);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        intentNext = new Intent(getApplicationContext(), HomePageActivity.class);
                        startActivity(intentNext);
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    public void getView() {
        txtViewTotalDate = findViewById(R.id.txtViewTotalDate);
        txtViewTotalMonth = findViewById(R.id.txtViewTotalMonth);
        lvHistory = findViewById(R.id.lvHistory);
    }

    public void setOnClick(){

    }

    public void loadView(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String now = dateFormat.format(cal.getTime());
        double totalDate = billDB.totalOfDate(now, customer.getId());
        txtViewTotalDate.setText(UnitFormatProvider.getInstance().format(totalDate));
        double totalMonth = billDB.totalOfMonth(now, customer.getId());
        txtViewTotalMonth.setText(UnitFormatProvider.getInstance().format(totalMonth));
    }

    public void onClick(View view){

    }
}