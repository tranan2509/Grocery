package hcmute.edu.vn.mssv18110249;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import DBUtil.AccountDB;
import DBUtil.CategoryDB;
import DBUtil.CustomerDB;
import DBUtil.ProductDB;
import Model.*;
import Provider.BitmapConvert;
import Provider.DownloadImageTask;
import Provider.ProductListViewAdapter;

public class ListProductActivity extends AppCompatActivity {

    ListView lvProduct;

    ProductListViewAdapter productListViewAdapter;

    List<Product> products;

    CustomerDB customerDB;
    CategoryDB categoryDB;
    AccountDB accountDB;
    ProductDB productDB;
    Customer customer;
    Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);

        categoryDB = new CategoryDB(this);
        productDB = new ProductDB(this);
        accountDB = new AccountDB(this);

        customer = (Customer)getIntent().getSerializableExtra("customer");
        System.out.println(customer.getAccountId());
        account = accountDB.getAccount(customer.getAccountId());

        products = new ArrayList<Product>();

        lvProduct = (ListView)findViewById(R.id.lvProduct);

//        categoryDB.add(new Category("Fruit"));
//
//        for (int i = 0; i < 20; i++){
//            Product product = new Product(1, "Name " + i + 1, customer.getAvatar(),
//                    "25/09/2020", 25000, 30000, 0, 100, "Description " + i + 1, 4, 100);
//            productDB.add(product);
//        }

        products= productDB.get();

        productListViewAdapter = new ProductListViewAdapter(products);
        lvProduct.setAdapter(productListViewAdapter);

        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = (Product) productListViewAdapter.getItem(position);
                Toast.makeText(ListProductActivity.this, product.getName(), Toast.LENGTH_LONG).show();
            }
        });
    }
}