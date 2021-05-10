package hcmute.edu.vn.mssv18110249;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import Model.Customer;

public class AccountActivity extends AppCompatActivity {

    private GoogleApiClient mGoogleApiClient;

    BottomNavigationView bottomNavigationView;
    Button btnLogout, btnSetting;
    TextView txtViewInfo;
    Intent intent, intentNext;
    Customer customer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        intent = getIntent();
        customer = (Customer) intent.getExtras().getSerializable("customer");
        Log.d("Name ACCOUNT ", customer.getName());
        txtViewInfo = (TextView)findViewById(R.id.txtViewInfo);
        btnSetting = (Button)findViewById(R.id.btnSetting);

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
                startActivity(intentNext);
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
                                Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                                Intent i =new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(i);
                            }
                        });
            }
        });
    }
}