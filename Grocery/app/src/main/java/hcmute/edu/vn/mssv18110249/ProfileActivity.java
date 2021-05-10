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

import DBUtil.AccountDB;
import DBUtil.CustomerDB;
import Model.Account;
import Model.Customer;

public class ProfileActivity extends AppCompatActivity {

    CustomerDB customerDB;
    AccountDB accountDB;

    ImageView imgAvatar;
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
        imgAvatar = (ImageView)findViewById(R.id.imgAvatar);

        txtViewName.setText(customer.getName());
        txtViewDob.setText(customer.getDob());
        txtViewGender.setText(customer.isGender() ? "Male" : "Female");
        txtViewPhone.setText(customer.getPhone());
        txtViewEmail.setText(account.getEmail());
        if (account.isEmail()){
            new DownloadImageTask(imgAvatar)
                    .execute(customer.getAvatar());
        }



        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentNext = new Intent(getApplicationContext(), AccountActivity.class);
                intentNext.putExtra("customer", customer);
                startActivity(intentNext);
            }
        });

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}