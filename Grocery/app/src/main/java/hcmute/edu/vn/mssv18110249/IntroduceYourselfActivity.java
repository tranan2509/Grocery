package hcmute.edu.vn.mssv18110249;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import DBUtil.CustomerDB;
import Model.Customer;
import Provider.SharedPreferenceProvider;
import Provider.GenderSpinnerAdapter;

public class IntroduceYourselfActivity extends AppCompatActivity {

    String[] gender={"Male", "Female"};
    int images[] = {R.drawable.male,R.drawable.female};

    CustomerDB customerDB;
    Customer customer;
    EditText txtBod;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce_yourself);

//        initialization
        customerDB = new CustomerDB(this);

        Intent intent = getIntent();
        customer = (Customer)SharedPreferenceProvider.getInstance(this).get("customer");

//        Get parameter
        Spinner spnGender = (Spinner) findViewById(R.id.spnGender);
        txtBod = (EditText)findViewById(R.id.txtDob);
        btnSave = (Button)findViewById(R.id.btnSave);

//        Set text Date of birth
        txtBod.setText(customer.getDob());

//        On Select Spinner
        spnGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        Set gender spinner
        GenderSpinnerAdapter genderAdapter = new GenderSpinnerAdapter(getApplicationContext(),images,gender);
        spnGender.setAdapter(genderAdapter);
//        0: Male - 1: Female
        spnGender.setSelection(customer.isGender() ? 0 : 1);

//        Save data
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Get info
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
                    Toast.makeText(IntroduceYourselfActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}