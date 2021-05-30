package hcmute.edu.vn.mssv18110249;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import DBUtil.AccountDB;
import DBUtil.CustomerDB;
import Model.Account;
import Model.Customer;
import Provider.SharedPreferenceProvider;
import Provider.Validator;

public class ContactInformationActivity extends AppCompatActivity implements View.OnClickListener{

    Customer customer;
    CustomerDB customerDB;
    AccountDB accountDB;
    EditText txtAddress, txtEmail, txtPhone;
    ImageButton btnBack;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_information);

        customer = (Customer) SharedPreferenceProvider.getInstance(this).get("customer");
        accountDB = new AccountDB(this);
        customerDB = new CustomerDB(this);

        getView();
        setOnClick();
        Account account = accountDB.getAccount(customer.getAccountId());
        txtEmail.setText(account.getEmail());
        txtAddress.setText(customer.getAddress());
        txtPhone.setText(customer.getPhone());

    }

    public void getView(){
        txtAddress = (EditText)findViewById(R.id.txtAddress);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtPhone = (EditText)findViewById(R.id.txtPhone);
        btnBack = (ImageButton)findViewById(R.id.btnBack);
        btnSave = (Button)findViewById(R.id.btnSave);
    }

    public void setOnClick(){
        btnBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnBack:
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.btnSave:
                save();
                break;
        }
    }

    public void save(){
        if (!txtAddress.getText().toString().equals("")){
            if (!txtPhone.getText().toString().equals("")){
                if (Validator.validatePhone(txtPhone.getText().toString())){
                    customer.setAddress(txtAddress.getText().toString());
                    customer.setPhone(txtPhone.getText().toString());
                    customerDB.update(customer);
                    SharedPreferenceProvider.getInstance(this).set("customer", customer);
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Please enter a valid phone number!", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Please enter phone information!", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Please enter address information!", Toast.LENGTH_LONG).show();
        }
    }

}