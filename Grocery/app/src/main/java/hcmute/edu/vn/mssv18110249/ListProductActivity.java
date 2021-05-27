package hcmute.edu.vn.mssv18110249;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import DBUtil.AccountDB;
import DBUtil.CartDB;
import DBUtil.CategoryDB;
import DBUtil.CustomerDB;
import DBUtil.ProductDB;
import Model.*;
import Provider.BitmapConvert;
import Provider.DownloadImageTask;
import Provider.ProductListViewAdapter;
import Provider.SharedPreferenceProvider;

public class ListProductActivity extends AppCompatActivity implements View.OnClickListener{

    ListView lvProduct;

    ProductListViewAdapter productListViewAdapter;

    List<Product> products;

    Intent intent;

    CustomerDB customerDB;
    CategoryDB categoryDB;
    CartDB cartDB;
    AccountDB accountDB;
    ProductDB productDB;
    Customer customer;
    Account account;
    Category category;
    ImageButton btnBack, btnSearch;
    EditText txtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);

        categoryDB = new CategoryDB(this);
        productDB = new ProductDB(this);
        accountDB = new AccountDB(this);
        cartDB = new CartDB(this);

        customer = (Customer)SharedPreferenceProvider.getInstance(this).get("customer");
        account = accountDB.getAccount(customer.getAccountId());

        getView();
        setOnClick();

        intent = getIntent();
        category = (Category)intent.getExtras().getSerializable("category");

        products= productDB.getByCategoryId(category.getId());
        loadProducts(products);

        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = (Product) productListViewAdapter.getItem(position);

                Intent intentBuy = new Intent(getApplicationContext(), BuyProductActivity.class);
                intentBuy.putExtra("product", product);
                startActivity(intentBuy);
            }
        });

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name = txtSearch.getText().toString();
                if (name.equals("")){
                    products= productDB.getByCategoryId(category.getId());
                    loadProducts(products);
                }else{
                    search(name);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void getView(){
        lvProduct = (ListView)findViewById(R.id.lvProduct);
        btnBack = (ImageButton)findViewById(R.id.btnBack);
        btnSearch = (ImageButton)findViewById(R.id.btnSearch);
        txtSearch = (EditText)findViewById(R.id.txtSearch);

    }

    public void setOnClick(){
        btnSearch.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnSearch:
                String name = txtSearch.getText().toString();
                if (name.equals("")){
                    products= productDB.getByCategoryId(category.getId());
                    loadProducts(products);
                }else{
                    search(name);
                }
                break;
        }
    }

    public void loadProducts(List<Product> products){
        productListViewAdapter = new ProductListViewAdapter(products);
        lvProduct.setAdapter(productListViewAdapter);
    }

    public void search(String name){
        List<Product> products = productDB.search(name, category.getId());
        loadProducts(products);
    }
}