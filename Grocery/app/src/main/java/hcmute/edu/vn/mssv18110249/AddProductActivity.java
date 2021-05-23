package hcmute.edu.vn.mssv18110249;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import DBUtil.CategoryDB;
import DBUtil.ProductDB;
import Model.Category;
import Model.Product;
import Provider.BitmapConvert;
import Provider.CategorySpinnerAdapter;
import Provider.Validator;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CAMERA_REQUEST = 111;
    private static final int PICK_IMAGE = 222;

    private static String strImgProduct;

    private List<String> units;
    private List<Category> categories;

    Button btnSave;
    ImageButton btnChoose, btnBack;
    ImageView imgProduct;
    EditText txtName, txtImportDate, txtImportPrice, txtPrice, txtDiscount, txtQuantity, txtDescription;
    Spinner spnCategory, spnUnit;

    CategoryDB categoryDB;
    ProductDB productDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        strImgProduct = "";
        checkPermissionReadExternalStorage();
        setDBUtil();
        getView();
        setOnclickView();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        txtImportDate.setText(dateFormat.format(cal.getTime()));

        units = new ArrayList<String>();
        units.add("USA");
        units.add("VND");

//        categoryDB.add(new Category("Fruit"));
//        categoryDB.add(new Category("Fresh Food"));
        categories = categoryDB.get();

        setSpinner();

    }

    public void setDBUtil(){
        categoryDB = new CategoryDB(this);
        productDB = new ProductDB(this);
    }

    public void getView(){
        btnSave = (Button)findViewById(R.id.btnSave);
        imgProduct = (ImageView)findViewById(R.id.imgProduct);
        btnChoose = (ImageButton)findViewById(R.id.btnChoose);
        btnBack = (ImageButton)findViewById(R.id.btnBack);
        txtName = (EditText)findViewById(R.id.txtName);
        txtImportDate = (EditText)findViewById(R.id.txtImportDate);
        txtImportPrice = (EditText)findViewById(R.id.txtImportPrice);
        txtPrice = (EditText)findViewById(R.id.txtPrice);
        txtDiscount = (EditText)findViewById(R.id.txtDiscount);
        txtQuantity = (EditText)findViewById(R.id.txtQuantity);
        txtDescription = (EditText)findViewById(R.id.txtDescription);
        spnCategory = (Spinner)findViewById(R.id.spnCategory);
        spnUnit = (Spinner)findViewById(R.id.spnUnit);
    }

    private void setOnclickView() {
        btnChoose.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }


    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnSave:
                addProduct();
                break;
            case R.id.btnChoose:
                getImageGallery();
                break;
            case R.id.btnBack:
                break;
        }
    }

    public void setSpinner(){
        ArrayAdapter<String> unitsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, units);
        spnUnit.setAdapter(unitsAdapter);
        unitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnUnit.setSelection(0);
        SpinnerAdapter categoriesAdapter = new CategorySpinnerAdapter(getApplicationContext(), categories);
        spnCategory.setAdapter(categoriesAdapter);
        spnCategory.setSelection(0);
    }

    public boolean addProduct(){
        if (isVerify() && isValidate()){
            String name = txtName.getText().toString();
            int categoryId = categories.get(spnCategory.getSelectedItemPosition()).getId();
            String importDate = txtImportDate.getText().toString();
            double importPrice = Double.parseDouble(txtImportPrice.getText().toString());
            double price = Double.parseDouble(txtPrice.getText().toString());
            String unit = spnUnit.getSelectedItem().toString();
            int discount = Integer.parseInt(txtDiscount.getText().toString());
            int quantity = Integer.parseInt(txtQuantity.getText().toString());
            String description = txtDescription.getText().toString();
            Product product = new Product(categoryId, name, strImgProduct, importDate, importPrice, price, unit, discount, quantity, description);
            productDB.add(product);
            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    public boolean isValidate(){
        if (Validator.validateDob(txtImportDate.getText().toString())) {
            return true;
        }else
            Toast.makeText(getApplicationContext(), "Import date must be formatted in dd/mm/yyyy", Toast.LENGTH_LONG).show();
        return false;
    }

    public boolean isVerify(){
        if (!strImgProduct.equals("")){
            if (!txtName.getText().toString().equals(""))
                if (!txtImportDate.getText().toString().equals("")){
                    if (!txtImportPrice.getText().toString().equals("")){
                        if (!txtPrice.getText().toString().equals("")){
                            if (!txtDiscount.getText().toString().equals("")){
                                if (!txtQuantity.getText().toString().equals("")){
                                    return true;
                                }else{
                                    Toast.makeText(getApplicationContext(), "Please enter quantity", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Please enter discount", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Please enter price", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Please enter import price", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Please enter import date", Toast.LENGTH_LONG).show();
                }else{
                Toast.makeText(getApplicationContext(), "Please enter name of product", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Please choose image of product", Toast.LENGTH_LONG).show();
        }

        return false;
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
                strImgProduct = BitmapConvert.BitMapToString(photo);
                imgProduct.setImageBitmap(photo);
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