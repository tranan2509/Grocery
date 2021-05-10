package hcmute.edu.vn.mssv18110249;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import DBUtil.AccountDB;
import DBUtil.CustomerDB;
import Model.Account;
import Model.Customer;

public class ProfileActivity extends AppCompatActivity {

    CustomerDB customerDB;
    AccountDB accountDB;

    ImageButton btnBack;
    TextView txtViewName, txtViewLiving, txtViewDob, txtViewGender, txtViewEmail, txtViewPhone;

    Intent intent, intentNext;
    Customer customer;
    Account account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        accountDB = new AccountDB(this);

        intent = getIntent();
        customer = (Customer)intent.getExtras().getSerializable("customer");
        account = accountDB.getAccount(customer.getAccountId());

        btnBack = (ImageButton)findViewById(R.id.btnBack);
        txtViewName = (TextView)findViewById(R.id.txtViewName);
        txtViewLiving = (TextView)findViewById(R.id.txtViewLivingIn);
        txtViewDob = (TextView)findViewById(R.id.txtViewDob);
        txtViewGender = (TextView)findViewById(R.id.txtViewGender);
        txtViewEmail = (TextView)findViewById(R.id.txtViewEmail);
        txtViewPhone = (TextView)findViewById(R.id.txtViewPhone);

        txtViewName.setText(customer.getName());
        txtViewDob.setText(customer.getDob().toString());
        txtViewName.setText(customer.isGender() == true ? "Male" : "Female");
        txtViewPhone.setText(customer.getPhone());
        txtViewEmail.setText(account.getEmail());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentNext = new Intent(getApplicationContext(), AccountActivity.class);
                intentNext.putExtra("customer", customer);
                startActivity(intentNext);
            }
        });

    }
}