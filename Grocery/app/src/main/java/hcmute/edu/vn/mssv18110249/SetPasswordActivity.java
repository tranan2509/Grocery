package hcmute.edu.vn.mssv18110249;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import DBUtil.AccountDB;
import Model.Account;
import Model.Customer;
import Provider.SharedPreferenceProvider;
import Provider.Validator;

public class SetPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnSave;
    ImageButton btnBack;
    EditText txtOldPassword, txtNewPassword, txtConfirmPassword;
    Customer customer;
    AccountDB accountDB;
    Account account;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        customer = (Customer) SharedPreferenceProvider.getInstance(this).get("customer");
        accountDB = new AccountDB(this);
        account = accountDB.getAccount(customer.getId());

        getView();
        setOnClick();

        if (account.isEmail()){
            txtOldPassword.setEnabled(false);
        }

        txtOldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String oldPassword = txtOldPassword.getText().toString();
                if (oldPassword.equals(account.getPassword())){
                    txtNewPassword.setEnabled(true);
                    txtConfirmPassword.setEnabled(true);
                }else{
                    txtNewPassword.setEnabled(false);
                    txtConfirmPassword.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void getView(){
        btnSave = (Button)findViewById(R.id.btnSave);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        txtOldPassword = (EditText)findViewById(R.id.txtOldPassword);
        txtNewPassword = (EditText)findViewById(R.id.txtNewPassword);
        txtConfirmPassword = (EditText)findViewById(R.id.txtConfirmPassword);
    }

    public void setOnClick(){
        btnBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnSave:
                save();
                break;
        }
    }

    public void save(){
        String password = txtNewPassword.getText().toString();
        String confirmPassword = txtConfirmPassword.getText().toString();
        if (!password.equals(confirmPassword)){
            if (Validator.validatePassword(password)){
                account.setPassword(password);
                accountDB.update(account);
                intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(), "Password at least 6 characters", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Confirmation password does not match", Toast.LENGTH_LONG).show();
        }
    }
}