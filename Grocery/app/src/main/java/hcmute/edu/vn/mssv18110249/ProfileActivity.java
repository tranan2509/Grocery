package hcmute.edu.vn.mssv18110249;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import DBUtil.AccountDB;
import DBUtil.CustomerDB;
import Model.Account;
import Model.Customer;
import Provider.ArrayByteConvert;
import Provider.BitmapConvert;
import Provider.DownloadImageTask;
import Provider.SharedPreferenceProvider;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int CAMERA_REQUEST = 111;
    private static final int PICK_IMAGE = 222;

    CustomerDB customerDB;
    AccountDB accountDB;

    ImageView imgAvatar;
    ImageButton btnBack;
    TextView txtViewName, txtViewLiving, txtViewDob, txtViewGender, txtViewEmail, txtViewPhone, txtViewAddress;
    TextView txtViewEditIntroduce, txtViewEditContactInfo;

    Intent intent, intentNext;
    Customer customer;
    Account account;
    String avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        accountDB = new AccountDB(this);
        customerDB = new CustomerDB(this);

        intent = getIntent();
        customer = (Customer)SharedPreferenceProvider.getInstance(this).get("customer");
        account = accountDB.getAccount(customer.getAccountId());

        getView();
        setOnClick();

        txtViewName.setText(customer.getName());
        setIntroduce();
        setContactInfo();

        if (account.isEmail()){
            new DownloadImageTask(imgAvatar)
                    .execute(customer.getAvatar());
        }else{
            imgAvatar.setImageBitmap(BitmapConvert.StringToBitMap(customer.getAvatar()));
        }

    }

    public void getView(){
        btnBack = (ImageButton)findViewById(R.id.btnBack);
        txtViewName = (TextView)findViewById(R.id.txtViewName);
        txtViewLiving = (TextView)findViewById(R.id.txtViewLivingIn);
        txtViewDob = (TextView)findViewById(R.id.txtViewDob);
        txtViewGender = (TextView)findViewById(R.id.txtViewGender);
        txtViewEmail = (TextView)findViewById(R.id.txtViewEmail);
        txtViewPhone = (TextView)findViewById(R.id.txtViewPhone);
        txtViewAddress = (TextView)findViewById(R.id.txtViewAddress);
        imgAvatar = (ImageView)findViewById(R.id.imgAvatar);

        txtViewEditIntroduce = (TextView)findViewById(R.id.txtViewEditIntroduce);
        txtViewEditContactInfo = (TextView)findViewById(R.id.txtViewEditContactInfo);
    }

    public void setOnClick(){
        btnBack.setOnClickListener(this);
        txtViewEditIntroduce.setOnClickListener(this);
        txtViewEditContactInfo.setOnClickListener(this);
        imgAvatar.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnBack:
                Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(intent);
                break;
            case R.id.txtViewEditIntroduce:
                intentNext = new Intent(getApplicationContext(), IntroduceYourselfActivity.class);
                startActivity(intentNext);
                break;
            case R.id.txtViewEditContactInfo:
                intentNext = new Intent(getApplicationContext(), ContactInformationActivity.class);
                startActivity(intentNext);
                break;
            case R.id.imgAvatar:
                if (!account.isEmail()) {
                    getImageGallery();
                }
                break;
        }
    }

    public void saveCustomer(Customer customer){
        customer.setAvatar(avatar);
        customerDB.update(customer);
        SharedPreferenceProvider.getInstance(this).set("customer",customer);
    }


    public void setIntroduce(){
        txtViewDob.setText(customer.getDob());
        txtViewGender.setText(customer.isGender() ? "Male" : "Female");
    }

    public void setContactInfo(){
        txtViewPhone.setText(customer.getPhone());
        txtViewEmail.setText(account.getEmail());
        txtViewAddress.setText(customer.getAddress());
        List<String> partAddresses = new ArrayList<String>();
        partAddresses = Arrays.asList(customer.getAddress().split(","));
        String city = partAddresses.size() > 0 ? partAddresses.get(partAddresses.size() - 1) : "";
        txtViewLiving.setText(city);
    }

    private void getImageGallery(){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);getIntent.setType("image/*");
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        Intent chooserIntent = Intent.createChooser(getIntent, "Choose Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                Uri imageUri = data.getData();
                Bitmap photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                avatar = BitmapConvert.BitMapToString(photo);
                imgAvatar.setImageBitmap(photo);
                saveCustomer(customer);
            } catch (IOException e) {

            }
            return;
        }
    }

    private boolean checkPermissionReadExternalStorage() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;        } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;        }
        } else {
            return true;    }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            //xử lý ở đây
        }
    }
}