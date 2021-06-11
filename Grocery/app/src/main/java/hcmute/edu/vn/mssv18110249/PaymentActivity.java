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
import DBUtil.CustomerDB;
import DBUtil.VoucherDB;
import Model.Bill;
import Model.BillDetail;
import Model.Branch;
import Model.Cart;
import Model.Customer;
import Provider.PaymentListViewAdapter;
import Provider.ProductListViewAdapter;
import Provider.SharedPreferenceProvider;
import Provider.UnitFormatProvider;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener{

    Customer customer;
    Branch branch;
    TextView txtViewName, txtViewPhone, txtViewVoucher, txtViewPaymentMethod, txtViewAmount, txtViewBranch;
    ListView lvPayment;
    Button btnPayment;
    ImageButton btnBack;

    Bill bill;
    List<BillDetail> billDetails;
    Intent intent;

    VoucherDB voucherDB;
    BillDB billDB;
    BillDetailDB billDetailDB;
    CustomerDB customerDB;
    CartDB cartDB;
    PaymentListViewAdapter paymentListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        voucherDB = new VoucherDB(this);
        billDB = new BillDB(this);
        billDetailDB = new BillDetailDB(this);
        customerDB = new CustomerDB(this);
        cartDB = new CartDB(this);

        getView();
        setOnClick();

        customer = (Customer) SharedPreferenceProvider.getInstance(this).get("customer");
        branch = (Branch)SharedPreferenceProvider.getInstance(this).getBranch("branch");
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
        txtViewBranch = findViewById(R.id.txtViewBranch);
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
                intent.putExtra("previous", "home");
                startActivity(intent);
                break;
        }
    }

    public void payment(){
        bill.setBranchId(branch.getId());
        System.out.println(bill.getCustomerId());
        customer.setPoint(customer.getPoint() + (int)bill.getAmount() / 200);
        customerDB.update(customer);
        SharedPreferenceProvider.getInstance(this).set("customer", customer);
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