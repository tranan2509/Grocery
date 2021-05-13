package hcmute.edu.vn.mssv18110249;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.io.InputStream;

import DBUtil.AccountDB;
import Model.*;
import Provider.BitmapConvert;
import Provider.DownloadImageTask;

public class AccountActivity extends AppCompatActivity {

    private GoogleApiClient mGoogleApiClient;

    BottomNavigationView bottomNavigationView;
    Button btnLogout, btnSetting;
    ImageView imgAvatar;
    TextView txtViewInfo, txtViewName;
    Intent intent, intentNext;
    Customer customer;
    Account account;
    Account accountModel;
    AccountDB accountDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        accountDB = new AccountDB(this);

        intent = getIntent();
        customer = (Customer) intent.getExtras().getSerializable("customer");
        account = accountDB.getAccount(customer.getAccountId());

        txtViewInfo = (TextView)findViewById(R.id.txtViewInfo);
        btnSetting = (Button)findViewById(R.id.btnSetting);
        imgAvatar = (ImageView)findViewById(R.id.imgAvatar);
        txtViewName = (TextView)findViewById(R.id.txtViewName);

        accountModel = accountDB.getAccount(customer.getAccountId());
        if (accountModel.isEmail()){
            new DownloadImageTask(imgAvatar)
                    .execute(customer.getAvatar());
        }else{
            imgAvatar.setImageBitmap(BitmapConvert.StringToBitMap(customer.getAvatar()));
        }

        txtViewName.setText(customer.getName());

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentNext = new Intent(getApplicationContext(), SettingActivity.class);
                intentNext.putExtra("customer", customer);
                startActivity(intentNext);
            }
        });

        txtViewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentNext = new Intent(getApplicationContext(), ProfileActivity.class);
                intentNext.putExtra("customer", customer);
                startActivityForResult(intentNext, 1);
            }
        });


        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.account);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.account:
                        return true;
                    case R.id.home:
                        intentNext = new Intent(getApplicationContext(), HomePageActivity.class);
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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();


        findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                // ...
                                Intent i =new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(i);
                            }
                        });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            customer = (Customer) data.getExtras().getSerializable("customer");
        }

    }
}