package hcmute.edu.vn.mssv18110249;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import DBUtil.BillDB;
import DBUtil.BillDetailDB;
import DBUtil.VoucherDB;
import Model.Bill;
import Model.BillDetail;
import Model.Branch;
import Model.Customer;
import Model.Voucher;
import Provider.BillListViewAdapter;
import Provider.SharedPreferenceProvider;
import Provider.UnitFormatProvider;

public class BillActivity extends AppCompatActivity implements View.OnClickListener{

    Customer customer;
    Branch branch;
    TextView txtViewBranch, txtViewState, txtViewDate, txtViewName, txtViewPhone, txtViewBill;
    TextView txtViewVoucher, txtViewSubTotal, txtViewDiscount, txtViewAmount;
    ImageView imgLogo;
    ListView lvBill;
    ImageButton btnBack;

    Bill bill;
    List<BillDetail> billDetails;
    Intent intent;

    VoucherDB voucherDB;
    BillDB billDB;
    BillDetailDB billDetailDB;
    BillListViewAdapter billListViewAdapter;
    String previous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        voucherDB = new VoucherDB(this);
        billDetailDB = new BillDetailDB(this);
        billDB = new BillDB(this);

        getView();
        setOnClick();

        customer = (Customer) SharedPreferenceProvider.getInstance(this).get("customer");
        branch = (Branch)SharedPreferenceProvider.getInstance(this).getBranch("branch");
        intent = getIntent();
        previous = intent.getStringExtra("previous");
        bill = (Bill) intent.getExtras().getSerializable("bill");
        billDetails = billDetailDB.getByBillId(bill.getId());
        txtViewBranch.setText(branch.getName());
        billListViewAdapter = new BillListViewAdapter(billDetails);
        lvBill.setAdapter(billListViewAdapter);
        if (previous.equals("home")) {
            bill.setState("Completed");
            billDB.update(bill);
        }
        loadView();
    }

    public void getView(){
        txtViewBill = findViewById(R.id.txtViewBill);
        txtViewBranch = findViewById(R.id.txtViewBranch);
        txtViewState = findViewById(R.id.txtViewState);
        txtViewDate = findViewById(R.id.txtViewTime);
        txtViewName = findViewById(R.id.txtViewName);
        txtViewPhone = findViewById(R.id.txtViewPhone);
        txtViewVoucher = findViewById(R.id.txtViewVoucher);
        txtViewSubTotal = findViewById(R.id.txtViewSubTotal);
        txtViewDiscount = findViewById(R.id.txtViewPriceDiscount);
        txtViewAmount = findViewById(R.id.txtViewAmount);
        imgLogo = findViewById(R.id.imgLogo);
        lvBill = findViewById(R.id.lvBill);
        btnBack = findViewById(R.id.btnBack);
    }

    public void setOnClick(){
        btnBack.setOnClickListener(this);
    }

    public void loadView(){
        txtViewName.setText(customer.getName());
        txtViewPhone.setText(customer.getPhone());
        txtViewAmount.setText(UnitFormatProvider.getInstance().format(bill.getAmount()));
        if (previous.equals("home")) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Calendar cal = Calendar.getInstance();
            bill.setDate(dateFormat.format(cal.getTime()));
            billDB.update(bill);
            txtViewDate.setText(dateFormat.format(cal.getTime()));
        }else{
            txtViewDate.setText(bill.getDate());
        }

        int voucherId = bill.getVoucherId();
        if (voucherId == 0){
            txtViewSubTotal.setText(UnitFormatProvider.getInstance().format(bill.getAmount()));
            txtViewDiscount.setText(UnitFormatProvider.getInstance().format(0));
        }else{
            Voucher voucher = voucherDB.get(voucherId);
            double discount = bill.getAmount() * voucher.getDiscount() / 100;
            txtViewVoucher.setText(voucher.getName());
            txtViewSubTotal.setText(UnitFormatProvider.getInstance().format(bill.getAmount() + discount));
            txtViewDiscount.setText(UnitFormatProvider.getInstance().format(discount));
        }
        txtViewBill.setText("Bill #" + bill.getId());
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnBack:
                if (intent.getStringExtra("previous").equals("home")) {
                    Intent intentNext = new Intent(this, HomePageActivity.class);
                    startActivity(intentNext);
                }else{
                    finish();
                }
                break;
        }
    }
}