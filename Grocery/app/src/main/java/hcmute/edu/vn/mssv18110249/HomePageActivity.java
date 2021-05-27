package hcmute.edu.vn.mssv18110249;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import DBUtil.CategoryDB;
import Model.Customer;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {

    BottomNavigationView bottomNavigationView;
    Intent intent, intentNext;
    Customer customer;
    ImageButton btnFruit, btnCart, btnOrganic;
    CategoryDB categoryDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        categoryDB = new CategoryDB(this);

        getViews();
        setOnclickViews();

        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        return true;
                    case R.id.account:
                        intentNext = new Intent(getApplicationContext(), AccountActivity.class);
                        startActivity(intentNext);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.history:
                        intentNext = new Intent(getApplicationContext(), HistoryActivity.class);
                        startActivity(intentNext);
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    public void getViews(){
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigation);
        btnFruit = (ImageButton)findViewById(R.id.btnFruit);
        btnCart = (ImageButton)findViewById(R.id.btnCart);
        btnOrganic = (ImageButton)findViewById(R.id.btnOrganic);
    }

    public void setOnclickViews(){
        btnFruit.setOnClickListener(this);
        btnCart.setOnClickListener(this);
        btnOrganic.setOnClickListener(this);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnFruit:
                intentNext = new Intent(this, ListProductActivity.class);
                intentNext.putExtra("category", categoryDB.get("Fruit"));
                startActivity(intentNext);
                break;
            case R.id.btnCart:
                intentNext = new Intent(this, CartActivity.class);
                startActivity(intentNext);
                break;
            case R.id.btnOrganic:
                intentNext = new Intent(this, AddProductActivity.class);
                startActivity(intentNext);
                break;
        }
    }
}