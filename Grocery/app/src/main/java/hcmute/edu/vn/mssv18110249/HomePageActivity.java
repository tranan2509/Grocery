package hcmute.edu.vn.mssv18110249;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import Model.Customer;

public class HomePageActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Intent intent, intentNext;
    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        intent = getIntent();
        customer = (Customer) intent.getExtras().getSerializable("customer");

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        return true;
                    case R.id.account:
                        intentNext = new Intent(getApplicationContext(), AccountActivity.class);
                        intentNext.putExtra("customer", customer);
                        startActivity(intentNext);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.history:
                        intentNext = new Intent(getApplicationContext(), HistoryActivity.class);
                        intentNext.putExtra("customer", customer);
                        startActivity(intentNext);
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }
}