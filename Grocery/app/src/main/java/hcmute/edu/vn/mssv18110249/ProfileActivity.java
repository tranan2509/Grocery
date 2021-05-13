package hcmute.edu.vn.mssv18110249;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import DBUtil.AccountDB;
import DBUtil.CustomerDB;
import Model.Account;
import Model.Customer;
import Provider.BitmapConvert;
import Provider.DownloadImageTask;

public class ProfileActivity extends AppCompatActivity {

    CustomerDB customerDB;
    AccountDB accountDB;

    ImageView imgAvatar;
    ImageButton btnBack;
    TextView txtViewName, txtViewLiving, txtViewDob, txtViewGender, txtViewEmail, txtViewPhone, txtViewAddress;
    TextView txtViewEditIntroduce;

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
        txtViewAddress = (TextView)findViewById(R.id.txtViewAddress);
        imgAvatar = (ImageView)findViewById(R.id.imgAvatar);

        txtViewEditIntroduce = (TextView)findViewById(R.id.txtViewEditIntroduce);

        txtViewName.setText(customer.getName());
        setIntroduce();
        setContactInfo();

        if (account.isEmail()){
            new DownloadImageTask(imgAvatar)
                    .execute(customer.getAvatar());
        }else{
            imgAvatar.setImageBitmap(BitmapConvert.StringToBitMap(customer.getAvatar()));
        }


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBack = new Intent(getApplicationContext(), ProfileActivity.class);
                intentBack.putExtra("customer", customer);
                setResult(RESULT_OK, intentBack);
                finish();
            }
        });

        txtViewEditIntroduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentNext = new Intent(getApplicationContext(), IntroduceYourselfActivity.class);
                intentNext.putExtra("customer", customer);
                startActivityForResult(intentNext, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            customer = (Customer) data.getExtras().getSerializable("customer");
            setIntroduce();
        }

    }

    public void setIntroduce(){
        txtViewDob.setText(customer.getDob());
        txtViewGender.setText(customer.isGender() ? "Male" : "Female");
    }

    public void setContactInfo(){
        txtViewPhone.setText(customer.getPhone());
        txtViewEmail.setText(account.getEmail());
        txtViewAddress.setText(customer.getAddress());
        List<String> partAddresses = new ArrayList<String>();
        partAddresses = Arrays.asList(customer.getAddress().split(","));
        String city = partAddresses.size() > 0 ? partAddresses.get(partAddresses.size() - 1) : "";
        txtViewLiving.setText(city);
    }
}