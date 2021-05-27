package hcmute.edu.vn.mssv18110249;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import DBUtil.CustomerDB;
import Model.Customer;
import Provider.SharedPreferenceProvider;
import Provider.GenderSpinnerAdapter;

public class IntroduceYourselfActivity extends AppCompatActivity implements View.OnClickListener{

    String[] gender={"Male", "Female"};
    int images[] = {R.drawable.male,R.drawable.female};

    CustomerDB customerDB;
    Customer customer;
    EditText txtBod;
    Button btnSave;
    ImageButton btnBack;
    Spinner spnGender;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce_yourself);

//        initialization
        customerDB = new CustomerDB(this);
        customer = (Customer)SharedPreferenceProvider.getInstance(this).get("customer");

        getView();
        setOnClick();

//        Set text Date of birth
        txtBod.setText(customer.getDob());

//        Set gender spinner
        GenderSpinnerAdapter genderAdapter = new GenderSpinnerAdapter(getApplicationContext(),images,gender);
        spnGender.setAdapter(genderAdapter);
//        0: Male - 1: Female
        spnGender.setSelection(customer.isGender() ? 0 : 1);

    }

    public void getView(){
        //        Get parameter
        spnGender = (Spinner) findViewById(R.id.spnGender);
        txtBod = (EditText)findViewById(R.id.txtDob);
        btnSave = (Button)findViewById(R.id.btnSave);
        btnBack = (ImageButton)findViewById(R.id.btnBack);
    }

    public void setOnClick(){
        btnSave.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnSave:
                save();
                break;
            case R.id.btnBack:
                finish();
        }
    }

    public void save(){
        String dob = txtBod.getText().toString();
        boolean isMale = gender[spnGender.getSelectedItemPosition()].equals("Male");
        customer.setDob(dob);
        customer.setGender(isMale);
        if (customerDB.update(customer) > 0){
            btnSave.setTextColor(Color.parseColor("#aaffff"));
            Intent backIntent = new Intent(getApplicationContext(), ProfileActivity.class);
            SharedPreferenceProvider.getInstance(IntroduceYourselfActivity.this).set("customer", customer);
            startActivity(backIntent);
        }else{
            Toast.makeText(getApplicationContext(), "Update failed", Toast.LENGTH_SHORT).show();
        }
    }
}