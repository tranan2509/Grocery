package hcmute.edu.vn.mssv18110249;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import DBUtil.BillDB;
import DBUtil.BillDetailDB;
import DBUtil.CartDB;
import DBUtil.VoucherDB;
import Model.Bill;
import Model.BillDetail;
import Model.Cart;
import Model.Customer;
import Provider.PaymentListViewAdapter;
import Provider.ProductListViewAdapter;
import Provider.SharedPreferenceProvider;
import Provider.UnitFormatProvider;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener{

    Customer customer;

    TextView txtViewName, txtViewPhone, txtViewVoucher, txtViewPaymentMethod, txtViewAmount;
    ListView lvPayment;
    Button btnPayment;
    ImageButton btnBack;

    Bill bill;
    List<BillDetail> billDetails;
    Intent intent;

    VoucherDB voucherDB;
    BillDB billDB;
    BillDetailDB billDetailDB;
    CartDB cartDB;

    PaymentListViewAdapter paymentListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        voucherDB = new VoucherDB(this);
        billDB = new BillDB(this);
        billDetailDB = new BillDetailDB(this);
        cartDB = new CartDB(this);

        getView();
        setOnClick();

        customer = (Customer) SharedPreferenceProvider.getInstance(this).get("customer");

        intent = getIntent();
        bill = (Bill)intent.getExtras().getSerializable("bill");

        billDetails = (List<BillDetail>)intent.getSerializableExtra("billDetails");

        loadView();

        paymentListViewAdapter = new PaymentListViewAdapter(billDetails);
        lvPayment.setAdapter(paymentListViewAdapter);

        lvPayment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    public void getView(){
        txtViewName = findViewById(R.id.txtViewName);
        txtViewPhone = findViewById(R.id.txtViewPhone);
        txtViewVoucher = findViewById(R.id.txtViewVoucher);
        txtViewPaymentMethod = findViewById(R.id.txtViewPaymentMethod);
        txtViewAmount = findViewById(R.id.txtViewAmount);
        lvPayment = findViewById(R.id.lvPayment);
        btnPayment = findViewById(R.id.btnPayment);
        btnBack = findViewById(R.id.btnBack);
    }

    public void loadView(){
        txtViewName.setText(customer.getName());
        txtViewPhone.setText(customer.getPhone());
        String paymentMethod = bill.getPayment() == 1 ? "Cash" : "Other";
        txtViewPaymentMethod.setText(paymentMethod);
        String voucher = bill.getVoucherId() == 0 ? "None" : voucherDB.get(bill.getVoucherId()).getName();
        txtViewVoucher.setText(voucher);
        txtViewAmount.setText(UnitFormatProvider.getInstance().format(bill.getAmount()));
    }

    public void setOnClick(){
        btnBack.setOnClickListener(this);
        btnPayment.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnPayment:
                payment();
                intent = new Intent(getApplicationContext(), BillActivity.class);
                intent.putExtra("bill", bill);
                startActivity(intent);
                break;
        }
    }

    public void payment(){
        billDB.add(bill);
        int billId = billDB.getMaxID();
        bill.setId(billId);
        for (BillDetail billDetail : billDetails){
            billDetail.setBillId(billId);
            billDetailDB.add(billDetail);
            cartDB.delete(customer.getId(), billDetail.getProductId());
        }
    }
}