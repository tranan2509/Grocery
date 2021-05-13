package hcmute.edu.vn.mssv18110249;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.w3c.dom.Text;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import DBUtil.*;
import Model.*;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;

    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9001;

    Button btnSignIn;
    ImageButton imgBtnShow;
    TextView btnSignUp, txtViewForgotPassword;
    EditText txtPassword, txtEmail;
    private boolean isShow;
    CustomerDB customerDB;
    AccountDB accountDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customerDB = new CustomerDB(this);
        accountDB = new AccountDB(this);

        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        btnSignUp = (TextView)findViewById(R.id.btnSignUp);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        imgBtnShow = (ImageButton)findViewById(R.id.btnShow);
        txtViewForgotPassword = (TextView)findViewById(R.id.textViewForgotPassword);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        isShow = false;

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verify()){
                    String email = txtEmail.getText().toString();
                    String password = txtPassword.getText().toString();
                    Account accountLogin= accountDB.accountLogged(email, password);
                    if (accountLogin != null){
                        Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                        Customer customer = customerDB.getCustomer(accountLogin.getId(), true);
                        intent.putExtra("customer", customer);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "Email or password is incorrect", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        imgBtnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isShow)
                {
                    txtPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    isShow = true;
                }
                else
                {
                    txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isShow = false;
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        txtViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });


        // Button listeners
        findViewById(R.id.btnGmail).setOnClickListener(this);

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]

        // [START customize_button]
        // Customize sign-in button. The sign-in button can be displayed in
        SignInButton signInButton = (SignInButton) findViewById(R.id.btnGmail);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());
        // [END customize_button]
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            updateUI(true);
            Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
            Account account = accountDB.getAccount(acct.getId());

            if (account == null){
                if (accountDB.getAccountByEmail(acct.getEmail()) == null) {
                    account = new Account(acct.getId(), acct.getEmail(), "", true, 1, true);
                    Customer customer = new Customer(acct.getId(), acct.getId(), acct.getDisplayName(), acct.getPhotoUrl().toString(), "", "", "", true);
                    accountDB.add(account);
                    customerDB.add(customer);
                }else{
                    Toast.makeText(getApplicationContext(), "Email already exists", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            Customer customer = customerDB.getCustomer(acct.getId());
            intent.putExtra("customer", customer);
            if (customer != null)
                startActivity(intent);
            else
                Toast.makeText(getApplicationContext(), "Not found you information!", Toast.LENGTH_LONG).show();
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.btnGmail).setVisibility(View.GONE);
        } else {
            findViewById(R.id.btnGmail).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGmail:
                signIn();
                break;
        }
    }

    public boolean verify(){
        if (!txtEmail.getText().toString().equals("")){
            if (!txtPassword.getText().toString().equals("")){
                return true;
            }else{
                Toast.makeText(getApplicationContext(), "Please enter a password", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Please enter a email", Toast.LENGTH_LONG).show();
        }
        return false;
    }

}