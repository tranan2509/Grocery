package hcmute.edu.vn.mssv18110249;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import DBUtil.CartDB;
import Model.Cart;
import Model.Customer;
import Provider.CartListViewAdapter;
import Provider.CartRecyclerViewAdapter;
import Provider.SharedPreferenceProvider;

public class CartActivity extends AppCompatActivity implements View.OnClickListener{

    Customer customer;
    CartListViewAdapter cartListViewAdapter;
    CartRecyclerViewAdapter cartRecyclerViewAdapter;
    List<Cart> carts;

    ListView lvCart;
    RecyclerView recyclerViewCart;
    CartDB cartDB;

    TextView txtViewVoucher, txtViewAmount;
    CheckBox ckbAllProduct;
    Button btnApply, btnPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        customer = (Customer) SharedPreferenceProvider.getInstance(this).get("customer");
        cartDB = new CartDB(CartActivity.this);
        getView();
        carts = cartDB.get();
        if (carts != null) {

            cartRecyclerViewAdapter = new CartRecyclerViewAdapter(carts);
            recyclerViewCart.setAdapter(cartRecyclerViewAdapter);
            recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        }

        getView();
        setOnClick();

        txtViewAmount.setText(String.valueOf(cartDB.getAmount(customer.getId())));

    }

    public void getView(){
        recyclerViewCart = (RecyclerView)findViewById(R.id.recyclerViewCart);
        txtViewVoucher = (TextView)findViewById(R.id.txtVoucher);
        txtViewAmount = (TextView)findViewById(R.id.txtViewAmount);
        ckbAllProduct = (CheckBox)findViewById(R.id.ckbAllProduct);
        btnApply = (Button)findViewById(R.id.btnApply);
        btnPurchase = (Button)findViewById(R.id.btnPurchase);
    }

    public void setOnClick(){
        btnApply.setOnClickListener(this);
        btnPurchase.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnApply:
                break;
            case R.id.btnPurchase:
                break;
        }
    }

}