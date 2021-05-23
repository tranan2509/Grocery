package hcmute.edu.vn.mssv18110249;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import DBUtil.CartDB;
import Model.Cart;
import Model.Customer;
import Provider.CartListViewAdapter;
import Provider.SharedPreferenceProvider;

public class CartActivity extends AppCompatActivity {

    Customer customer;
    CartListViewAdapter cartListViewAdapter;
    List<Cart> carts;

    ListView lvCart;
    CartDB cartDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
//
//
        customer = (Customer) SharedPreferenceProvider.getInstance(this).get("customer");
        System.out.println("===========" + customer.getName());
        cartDB = new CartDB(this);
//
        getView();
//
//        carts = cartDB.get();
//        System.out.println("===========" + carts.size());
//        cartListViewAdapter = new CartListViewAdapter(carts);
//        lvCart.setAdapter(cartListViewAdapter);
//
//        lvCart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
    }

    public void getView(){
        lvCart = (ListView)findViewById(R.id.lvCart);
    }
}