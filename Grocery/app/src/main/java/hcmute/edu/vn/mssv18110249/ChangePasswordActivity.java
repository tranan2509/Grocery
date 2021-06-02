package hcmute.edu.vn.mssv18110249;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import DBUtil.AccountDB;
import Model.Account;
import Provider.Validator;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener{

    EditText txtPassword, txtConfirmPassword;
    Button btnFinish;
    TextView txtViewBack;

    Account account;
    AccountDB accountDB;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getView();
        setOnClick();
        accountDB = new AccountDB(this);
        intent = getIntent();
        account = (Account)intent.getExtras().getSerializable("account");
    }

    public void getView(){
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        btnFinish = findViewById(R.id.btnFinish);
        txtViewBack = findViewById(R.id.txtViewBack);
    }

    public void setOnClick(){
        txtViewBack.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.txtViewBack:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btnFinish:
                changePassword();
                break;
        }
    }

    public void changePassword(){
        String password = txtPassword.getText().toString();
        String confirm = txtConfirmPassword.getText().toString();
        if (verify()){
            if (password.equals(confirm)){
                if (Validator.validatePassword(password)){
                    account.setPassword(password);
                    if (accountDB.update(account) > 0){
                        Toast.makeText(getApplicationContext(), "Change password successfully", Toast.LENGTH_LONG).show();
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "Change password failed", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Password at least 6 characters", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Confirm password does not match", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Please enter full information", Toast.LENGTH_LONG).show();
        }
    }

    public boolean verify(){
        if (txtPassword.getText().toString().equals("")||
        txtConfirmPassword.getText().toString().equals(""))
            return false;
        return true;
    }

}