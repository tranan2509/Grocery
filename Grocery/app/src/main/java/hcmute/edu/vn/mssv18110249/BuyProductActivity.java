package hcmute.edu.vn.mssv18110249;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Currency;

import DBUtil.CartDB;
import Model.Cart;
import Model.Customer;
import Model.Product;
import Provider.BitmapConvert;
import Provider.SharedPreferenceProvider;
import Provider.UnitFormatProvider;

public class BuyProductActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnAdd, btnIncrease, btnReduction;
    TextView txtViewName, txtViewPrice, txtViewQuantity;
    ImageButton btnClose;
    ImageView imgProduct;
    CartDB cartDB;
    Customer customer;
    Product product;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_product);

        cartDB = new CartDB(this);
        customer = (Customer) SharedPreferenceProvider.getInstance(this).get("customer");
        intent = getIntent();
        product = (Product)intent.getExtras().getSerializable("product");
        getView();
        setOnClick();
        setView();
        setAmount();
    }

    public void getView(){
        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnIncrease = (Button)findViewById(R.id.btnIncrease);
        btnReduction = (Button)findViewById(R.id.btnReduction);
        txtViewName = (TextView)findViewById(R.id.txtViewName);
        txtViewPrice = (TextView)findViewById(R.id.txtViewPrice);
        txtViewQuantity = (TextView)findViewById(R.id.txtViewQuantity);
        btnClose = (ImageButton)findViewById(R.id.btnClose);
        imgProduct = (ImageView)findViewById(R.id.imgProduct);
    }

    public void setOnClick(){
        btnAdd.setOnClickListener(this);
        btnIncrease.setOnClickListener(this);
        btnReduction.setOnClickListener(this);
        btnClose.setOnClickListener(this);
    }

    public void setView(){
        txtViewName.setText(product.getName());
        txtViewPrice.setText(UnitFormatProvider.getInstance().format(product.getPrice() * (1 - (double)product.getDiscount()/100)));
        imgProduct.setImageBitmap(BitmapConvert.StringToBitMap(product.getImage()));
    }

    public void onClick(View view){
        int quantity = Integer.parseInt(txtViewQuantity.getText().toString());
        switch (view.getId()){
            case R.id.btnAdd:
                add();
                break;
            case R.id.btnClose:
                finish();
                break;
            case R.id.btnIncrease:
                Cart cart = cartDB.get(customer.getId(), product.getId());
                int quantityCart = cart != null ? cart.getQuantity() : 0;
                if (Integer.parseInt(txtViewQuantity.getText().toString()) < product.getQuantity() + quantityCart){
                    txtViewQuantity.setText(String.valueOf(++quantity));
                    setAmount();
                }
                break;
            case R.id.btnReduction:
                if (Integer.parseInt(txtViewQuantity.getText().toString()) > 1){
                    txtViewQuantity.setText(String.valueOf(--quantity));
                    setAmount();
                }
                break;
        }
    }

    public void setAmount(){
        double amount = product.getPrice() * (1 - (double)product.getDiscount()/100) * Integer.parseInt(txtViewQuantity.getText().toString());
        btnAdd.setText("Add - " + UnitFormatProvider.getInstance().format(amount));
    }

    public void add() {
        Cart cartCheck = cartDB.get(customer.getAccountId(), product.getId());
        int quantity = Integer.parseInt(txtViewQuantity.getText().toString());
        if (cartCheck == null) {
            Cart cart = new Cart(customer.getId(), product.getId(), quantity, product.getPrice() * (1 - product.getDiscount() / 100), true);
            cartDB.add(cart);
        } else {
            cartCheck.setQuantity(cartCheck.getQuantity() + quantity);
            cartDB.update(cartCheck);
        }
        finish();
    }
}