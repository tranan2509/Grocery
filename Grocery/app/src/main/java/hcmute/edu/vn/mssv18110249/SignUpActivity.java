package hcmute.edu.vn.mssv18110249;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

import DBUtil.AccountDB;
import DBUtil.CustomerDB;
import Model.Account;
import Model.Customer;
import Provider.BitmapConvert;
import Provider.CircleImage;
import Provider.Validator;

public class SignUpActivity extends AppCompatActivity {

    TextView txtViewSignIn;
    Button btnSignUp;
    EditText txtName, txtPhone, txtEmail, txtPassword, txtConfirmPassword, txtDob, txtAddress;
    RadioButton rdoMale;
    CheckBox ckbPolicy;
    CircleImage imgAvatar;

    AccountDB accountDB;
    CustomerDB customerDB;
    Account account;
    Customer customer;
    String avatarStr;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        accountDB = new AccountDB(this);
        customerDB = new CustomerDB(this);

        avatarStr = "";

        btnSignUp = (Button)findViewById(R.id.btnSignUp);
        txtViewSignIn = (TextView)findViewById(R.id.btnSignIn);
        txtName = (EditText)findViewById(R.id.txtName);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtPhone = (EditText)findViewById(R.id.txtPhone);
        txtDob = (EditText)findViewById(R.id.txtDob);
        txtAddress = (EditText)findViewById(R.id.txtAddress);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        txtConfirmPassword = (EditText)findViewById(R.id.txtConfirmPassword);
        rdoMale = (RadioButton)findViewById(R.id.rdoMale);
        ckbPolicy = (CheckBox)findViewById(R.id.ckbPolicy);
        imgAvatar = (CircleImage)findViewById(R.id.imgAvatar);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verify() && isValidate()){
                    String name = txtName.getText().toString();
                    String email = txtEmail.getText().toString();
                    String phone = txtPhone.getText().toString();
                    String dob = txtDob.getText().toString();
                    String address = txtAddress.getText().toString();
                    boolean gender = rdoMale.isChecked();
                    String password = txtPassword.getText().toString();
                    String confirmPassword = txtConfirmPassword.getText().toString();

                    account = accountDB.getAccountByEmail(email);
                    if (account != null){
                        Toast.makeText(getApplicationContext(), "Email already exists", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (password.equals(confirmPassword)){
                        UUID uuid = UUID.randomUUID();
                        while (accountDB.getAccount(uuid.toString()) != null){
                            uuid = UUID.randomUUID();
                        }
                        account = new Account(uuid.toString(), email, password, false, 1, true);
                        customer = new Customer(uuid.toString(), uuid.toString(), name, avatarStr, phone, address, dob, gender);
                        try{
                            accountDB.add(account);
                            customerDB.add(customer);

                            Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                            intent.putExtra("customer", customer);
                            startActivity(intent);
                        }catch (Exception x){
                            accountDB.delete(uuid.toString());
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Confirmation password does not match", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        txtViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                imgAvatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            int width = photo.getWidth() < photo.getHeight() ? photo.getWidth() : photo.getHeight();
            int x = Integer.valueOf((photo.getWidth() - width)/2 );
            int y = Integer.valueOf((photo.getHeight() - width)/2 );
            Bitmap avatar = Bitmap.createBitmap(photo, x, y, width, width);
            avatarStr = BitmapConvert.BitMapToString(avatar);
            imgAvatar.setImageBitmap(avatar);
        }
    }

    public boolean isValidate(){
        if (Validator.validateEmail(txtEmail.getText().toString())){
            if (Validator.validatePhone(txtPhone.getText().toString())){
                if (Validator.validateDob(txtDob.getText().toString())){
                    if (Validator.validatePassword(txtPassword.getText().toString())) {
                        return true;
                    }else{
                        Toast.makeText(getApplicationContext(), "Password at least 6 characters", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Date of birth must be formatted in dd/mm/yyyy", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Invalid phone", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public boolean verify(){
        if (!avatarStr.equals("")){
            if (!txtName.getText().toString().equals(""))
                if (!txtPhone.getText().toString().equals("")){
                    if (!txtEmail.getText().toString().equals("")){
                        if (!txtPassword.getText().toString().equals("")){
                            if (!txtConfirmPassword.getText().toString().equals("")){
                                if (ckbPolicy.isChecked()){
                                    return true;
                                }else{
                                    Toast.makeText(getApplicationContext(), "Please accept the policy", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Please confirm password", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Please enter a password", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Please enter a email", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Please enter a phone number", Toast.LENGTH_LONG).show();
            }else{
                    Toast.makeText(getApplicationContext(), "Please enter your name", Toast.LENGTH_LONG).show();
                }
        }else{
            Toast.makeText(getApplicationContext(), "Please capture your face", Toast.LENGTH_LONG).show();
        }

        return false;
    }

}