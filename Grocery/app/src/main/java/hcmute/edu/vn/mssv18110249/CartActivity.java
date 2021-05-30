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
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

import DBUtil.CartDB;
import Model.Cart;
import Model.Customer;
import Provider.CartListViewAdapter;
import Provider.CartRecyclerViewAdapter;
import Provider.SharedPreferenceProvider;
import Provider.UnitFormatProvider;

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
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        customer = (Customer) SharedPreferenceProvider.getInstance(this).get("customer");
        cartDB = new CartDB(CartActivity.this);
        getView();
        carts = cartDB.get();
        load(carts);

        getView();
        setOnClick();
        txtViewAmount.setText(UnitFormatProvider.getInstance().format(cartDB.getAmount(customer.getId())));
        ckbAllProduct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ckbAllProduct.isChecked()){
                    for (Cart cart: carts){
                        if (!cart.isState()) {
                            cart.setState(true);
                            cartDB.update(cart);
                        }
                    }
                }else{
                    for (Cart cart: carts){
                        if (cart.isState()) {
                            cart.setState(false);
                            cartDB.update(cart);
                        }
                    }
                }
                load(carts);
                txtViewAmount.setText(UnitFormatProvider.getInstance().format(cartDB.getAmount(customer.getId())));
            }
        });

    }

    public void getView(){
        recyclerViewCart = (RecyclerView)findViewById(R.id.recyclerViewCart);
        txtViewVoucher = (TextView)findViewById(R.id.txtVoucher);
        txtViewAmount = (TextView)findViewById(R.id.txtViewAmount);
        ckbAllProduct = (CheckBox)findViewById(R.id.ckbAllProduct);
        btnApply = (Button)findViewById(R.id.btnApply);
        btnPurchase = (Button)findViewById(R.id.btnPurchase);
        btnBack = (ImageButton)findViewById(R.id.btnBack);
    }

    public void setOnClick(){
        btnApply.setOnClickListener(this);
        btnPurchase.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnApply:
                break;
            case R.id.btnPurchase:
                break;
            case R.id.btnBack:
                finish();
        }
    }

    public void load(List<Cart> carts){
        if (carts != null) {
            cartRecyclerViewAdapter = new CartRecyclerViewAdapter(carts);
            cartRecyclerViewAdapter.setContextCart(CartActivity.this);
            recyclerViewCart.setAdapter(cartRecyclerViewAdapter);
            recyclerViewCart.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        }
    }

}