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
import DBUtil.CartDB;
import DBUtil.CategoryDB;
import DBUtil.CustomerDB;
import DBUtil.ProductDB;
import Model.*;
import Provider.BitmapConvert;
import Provider.DownloadImageTask;
import Provider.ProductListViewAdapter;
import Provider.SharedPreferenceProvider;

public class ListProductActivity extends AppCompatActivity {

    ListView lvProduct;

    ProductListViewAdapter productListViewAdapter;

    List<Product> products;

    CustomerDB customerDB;
    CategoryDB categoryDB;
    CartDB cartDB;
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
        cartDB = new CartDB(this);

        customer = (Customer)SharedPreferenceProvider.getInstance(this).get("customer");
        account = accountDB.getAccount(customer.getAccountId());

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
//                Cart cartCheck = cartDB.get(1);
                Cart cartCheck = cartDB.get(customer.getAccountId(), product.getId());
                if (cartCheck == null) {
                    Cart cart = new Cart(customer.getId(), product.getId(), 1, product.getPrice() * (1 - product.getDiscount() / 100), true);
                    cartDB.add(cart);
                }else{
                    cartCheck.setQuantity(cartCheck.getQuantity() + 1);
                    cartDB.update(cartCheck);
                }
//                                Toast.makeText(ListProductActivity.this, product.getName(), Toast.LENGTH_LONG).show();

            }
        });
    }
}