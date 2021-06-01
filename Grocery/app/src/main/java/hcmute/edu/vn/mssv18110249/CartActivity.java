package hcmute.edu.vn.mssv18110249;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import DBUtil.CartDB;
import DBUtil.ProductDB;
import DBUtil.VoucherDB;
import Model.Bill;
import Model.BillDetail;
import Model.Cart;
import Model.Customer;
import Model.Product;
import Model.Voucher;
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
    VoucherDB voucherDB;
    ProductDB productDB;

    Bill bill;
    List<BillDetail> billDetails;

    TextView txtViewVoucher, txtViewAmount;
    CheckBox ckbAllProduct;
    Button btnApply, btnPurchase;
    ImageView btnBack;
    EditText txtVoucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        customer = (Customer) SharedPreferenceProvider.getInstance(this).get("customer");
        cartDB = new CartDB(this);
        voucherDB = new VoucherDB(this);
        productDB = new ProductDB(this);
        getView();
        carts = cartDB.get(customer.getId());
        load(carts);

        billDetails = new ArrayList<BillDetail>();

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
        txtVoucher = (EditText)findViewById(R.id.txtVoucher);
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
                applyVoucher();
                break;
            case R.id.btnPurchase:
                if (carts.size() > 0) {
                    purchase();
                    Intent intent = new Intent(this, PaymentActivity.class);
                    intent.putExtra("billDetails", (Serializable)billDetails);
                    intent.putExtra("bill", bill);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "There are no products in the cart" , Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnBack:
                finish();
        }
    }

    public void purchase(){
        double amount = 0;
        carts = cartDB.get(customer.getId());
        billDetails = new ArrayList<BillDetail>();
        for (Cart cart : carts){
            if (cart.isState()){
            Product product = productDB.get(cart.getProductId());
            double price = product.getPrice() * (1 - (double)product.getDiscount()/100);
            amount += price * cart.getQuantity();
            BillDetail billDetail = new BillDetail(1, cart.getProductId(), cart.getQuantity(), price, price * cart.getQuantity());
            billDetails.add(billDetail);}
        }
        try{
            SimpleDateFormat formatter =new SimpleDateFormat("dd/MM/yyyy");
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Calendar cal = Calendar.getInstance();
            Date now = formatter.parse(dateFormat.format(cal.getTime()));
            Voucher voucher = voucherDB.get(txtVoucher.getText().toString());
            int voucherId = voucher == null ? 0 : voucher.getId();
            amount = voucher == null ? amount : amount * (1 - (double)voucher.getDiscount()/100);
            bill = new Bill(customer.getId(), 1, now.toString(), voucherId, amount, "unpaid");
        }catch (Exception ex){

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

    public void applyVoucher() {
        txtViewAmount.setText(UnitFormatProvider.getInstance().format(cartDB.getAmount(customer.getId())));
        SimpleDateFormat formatter =new SimpleDateFormat("dd/MM/yyyy");
        String voucherName = txtVoucher.getText().toString();
        if (!voucherName.equals("")){
            Voucher voucher = voucherDB.get(voucherName);
            if (voucher != null){
                try {
                    Date startDate = formatter.parse(voucher.getStartDate());
                    Date endDate = formatter.parse(voucher.getEndDate());
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Calendar cal = Calendar.getInstance();
                    Date now = formatter.parse(dateFormat.format(cal.getTime()));

                    if (now.compareTo(startDate) < 0){
                        txtVoucher.setText("");
                        Toast.makeText(getApplicationContext(), "vouchers start from " + startDate.toString() , Toast.LENGTH_SHORT).show();
                    }else if (now.compareTo(endDate) > 1){
                        txtVoucher.setText("");
                        Toast.makeText(getApplicationContext(), "vouchers has expired from " + endDate.toString() , Toast.LENGTH_SHORT).show();
                    }else if (!voucher.isState()){
                        txtVoucher.setText("");
                        Toast.makeText(getApplicationContext(), "Voucher is not available" , Toast.LENGTH_SHORT).show();
                    }else if (cartDB.getAmount(customer.getId()) < voucher.getCondition()){
                        txtVoucher.setText("");
                        Toast.makeText(getApplicationContext(), "Minimum order value must be over " + voucher.getCondition() , Toast.LENGTH_SHORT).show();
                    } else{
                        double amount = cartDB.getAmount(customer.getId()) * (1 - (double)voucher.getDiscount()/100);
                        txtViewAmount.setText(UnitFormatProvider.getInstance().format(amount));
                    }
                }catch (Exception x){
                    txtVoucher.setText("");
                }
            }else{
                txtVoucher.setText("");
                Toast.makeText(getApplicationContext(), "Voucher not found" , Toast.LENGTH_SHORT).show();
            }
        }
    }

}