package hcmute.edu.vn.mssv18110249;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DBUtil.AccountDB;
import DBUtil.BillDetailDB;
import DBUtil.CartDB;
import DBUtil.CategoryDB;
import DBUtil.CustomerDB;
import DBUtil.ProductDB;
import Model.*;
import Provider.BitmapConvert;
import Provider.DownloadImageTask;
import Provider.ProductListViewAdapter;
import Provider.ProductRecyclerViewAdapter;
import Provider.SharedPreferenceProvider;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class ListProductActivity extends AppCompatActivity implements View.OnClickListener{

    ListView lvProduct;
    RecyclerView rcViewProduct;
    boolean isList, isDiscount;

    ProductListViewAdapter productListViewAdapter;
    ProductRecyclerViewAdapter productRecyclerViewAdapter;

    List<Product> products;

    Intent intent;

    CustomerDB customerDB;
    CategoryDB categoryDB;
    BillDetailDB billDetailDB;
    CartDB cartDB;
    AccountDB accountDB;
    ProductDB productDB;
    Customer customer;
    Account account;
    Category category;
    ImageButton btnBack, btnSearch, btnList, btnGrid;
    EditText txtSearch;
    TextView txtViewCategory;
    Chip chipDiscount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);

        SharedPreferences sharedPref = getSharedPreferences("sort", Context.MODE_PRIVATE);
        isList = sharedPref.getBoolean("isList", true);
        isDiscount = false;

        categoryDB = new CategoryDB(this);
        productDB = new ProductDB(this);
        billDetailDB = new BillDetailDB(this);
        accountDB = new AccountDB(this);
        cartDB = new CartDB(this);

        customer = (Customer)SharedPreferenceProvider.getInstance(this).get("customer");
        account = accountDB.getAccount(customer.getAccountId());

        getView();
        setOnClick();
        setSort();

        intent = getIntent();
        getProducts();

        String key = intent.getStringExtra("key");
        txtViewCategory.setText(key.replace("_", " "));

        loadProducts(products, isList);
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name = txtSearch.getText().toString();
                if (name.equals("")){
                    getProducts();
                    loadProducts(products, isList);
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
        rcViewProduct = findViewById(R.id.rcViewProduct);
        btnBack = (ImageButton)findViewById(R.id.btnBack);
        btnSearch = (ImageButton)findViewById(R.id.btnSearchProduct);
        txtSearch = (EditText)findViewById(R.id.txtSearch);
        btnList = findViewById(R.id.btnList);
        btnGrid = findViewById(R.id.btnGrid);
        chipDiscount = findViewById(R.id.chipDiscount);
        txtViewCategory = findViewById(R.id.txtViewCategory);
    }

    public void setOnClick(){
        btnSearch.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnList.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnGrid.setOnClickListener(this);
        chipDiscount.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnSearch:
                String name = txtSearch.getText().toString();
                if (name.equals("")){
                    getProducts();
                    loadProducts(products, isList);
                }else{
                    search(name);
                }
                break;
            case  R.id.chipDiscount:
                isDiscount = chipDiscount.isChecked();
                if (!isDiscount){
                    getProducts();
                }
                else
                    products= filterDiscount(products);
                loadProducts(products, isList);
                break;
            case R.id.btnList:
                isList = true;
                SharedPreferences sharedPref = getSharedPreferences("sort", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("isList", true);
                editor.commit();
                btnList.setImageResource(R.drawable.icons8_list_48);
                btnGrid.setImageResource(R.drawable.icons8_grid_view_black);
                loadProducts(products, isList);
                break;
            case R.id.btnGrid:
                isList = false;
                SharedPreferences sharedPrefGrid = getSharedPreferences("sort", Context.MODE_PRIVATE);
                SharedPreferences.Editor editorGrid = sharedPrefGrid.edit();
                editorGrid.putBoolean("isList", false);
                editorGrid.commit();
                btnList.setImageResource(R.drawable.icons8_list_black);
                btnGrid.setImageResource(R.drawable.icons8_grid_view_48);
                loadProducts(products, isList);
                break;
        }
    }

    public void getProducts(){
        category = (Category)intent.getExtras().getSerializable("category");
        if (category != null) {
            products = productDB.getByCategoryId(category.getId());
        }else{
            String key = intent.getStringExtra("key");
            switch (key){
                case "market":products = productDB.get();break;
                case "promotion": products = productDB.getPromotion();break;
                case "best_selling":
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar cal = Calendar.getInstance();
                    products = billDetailDB.getBestSelling(dateFormat.format(cal.getTime()));
                    break;
            }
        }
    }

    public void setSort(){
        if (isList){
            btnList.setImageResource(R.drawable.icons8_list_48);
            btnGrid.setImageResource(R.drawable.icons8_grid_view_black);
        }else{
            btnList.setImageResource(R.drawable.icons8_list_black);
            btnGrid.setImageResource(R.drawable.icons8_grid_view_48);
        }
    }


    public void loadProducts(List<Product> products, boolean isList){
        products = isDiscount ? filterDiscount(products): products;
        if (isList){
            rcViewProduct.setLayoutManager(new LinearLayoutManager(this));
            productRecyclerViewAdapter = new ProductRecyclerViewAdapter(products, getApplicationContext(), true);
        }else{
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            rcViewProduct.setLayoutManager(staggeredGridLayoutManager);
            productRecyclerViewAdapter = new ProductRecyclerViewAdapter(products, getApplicationContext());
        }
        rcViewProduct.setAdapter(productRecyclerViewAdapter);
        rcViewProduct.setItemAnimator(new SlideInUpAnimator());
        rcViewProduct.setHasFixedSize(true);
    }

    public void search(String name){
        category = (Category)intent.getExtras().getSerializable("category");
        if (category != null) {
            products = productDB.search(name, category.getId());
        }else{
            String key = intent.getStringExtra("key");
            switch (key){
                case "market":products = productDB.get(name);break;
                case "promotion": products = productDB.getPromotion(name);break;
                case "best_selling":
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar cal = Calendar.getInstance();
                    products = billDetailDB.getBestSelling(dateFormat.format(cal.getTime()), name);
                    break;
            }
        }
        loadProducts(products, isList);
    }

    public List<Product> filterDiscount(List<Product> products){
        List<Product> productsFilter = new ArrayList<Product>();
        for (Product product : products){
            if (product.getDiscount() > 0)
                productsFilter.add(product);
        }
        return productsFilter;
    }
}