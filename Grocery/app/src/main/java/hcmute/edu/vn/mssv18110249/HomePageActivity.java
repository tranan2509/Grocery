package hcmute.edu.vn.mssv18110249;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import DBUtil.BranchDB;
import DBUtil.CategoryDB;
import DBUtil.VoucherDB;
import Model.Branch;
import Model.Category;
import Model.Customer;
import Model.Voucher;
import Provider.SharedPreferenceProvider;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {

    BottomNavigationView bottomNavigationView;
    Intent intent, intentNext;
    Customer customer;
    ImageButton btnFruit, btnCart, btnOrganic, btnFreshFood, btnMarket, btnPromotion, btnBestSelling, btnHighlyRated;
    CategoryDB categoryDB;
    BranchDB branchDB;
    VoucherDB voucherDB;
    Button btnLocation, btnSearch;
    List<Branch> branches;
    Branch branch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        categoryDB = new CategoryDB(this);
        branchDB = new BranchDB(this);
        voucherDB = new VoucherDB(this);

        getViews();
        setOnclickViews();

        addData();


        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        return true;
                    case R.id.account:
                        intentNext = new Intent(getApplicationContext(), AccountActivity.class);
                        startActivity(intentNext);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.history:
                        intentNext = new Intent(getApplicationContext(), HistoryActivity.class);
                        startActivity(intentNext);
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    public void addData(){
        addBranch();
        addCategory();
        addVoucher();
    }

    public void addBranch(){
        branches = branchDB.getActives();
        if (branches.size() == 0){
            Branch branch1 = new Branch("The Grocery - Thu Duc, TP. Ho Chi Minh", "01 Vo Van Ngan, Thu Duc, TP.Ho Chi Minh", true);
            Branch branch2 = new Branch("The Grocery - Quan 9, TP. Ho Chi Minh", "32 Le Van Viet, Quan 9, TP.Ho Chi Minh", true);
            Branch branch3 = new Branch("The Grocery - Quan 10, TP. Ho Chi Minh", "34 Le Dinh Phong, Quan 10, TP.Ho Chi Minh", true);
            branchDB.add(branch1);
            branchDB.add(branch2);
            branchDB.add(branch3);
            branches = branchDB.getActives();
        }

        try{
            branch = (Branch)SharedPreferenceProvider.getInstance(this).getBranch("branch");
            btnLocation.setText(branch.getName());
        }catch (Exception ex){
            SharedPreferenceProvider.getInstance(this).set("branch", branches.get(0));
            btnLocation.setText(branches.get(0).getName());
        }
    }

    public void addCategory(){
        if (categoryDB.get().isEmpty()) {
            categoryDB.add(new Category("Fruit"));
            categoryDB.add(new Category("Fresh Food"));
            categoryDB.add(new Category("Organic"));
            categoryDB.add(new Category("Import"));
        }
    }

    public void addVoucher(){
        if (voucherDB.get().isEmpty()){
            voucherDB.add(new Voucher("GIAMGIASOC", 10, 20000, "2021-06-01", "2021-07-01", true));
            voucherDB.add(new Voucher("KHUYENMAI", 15, 20000, "2021-06-01", "2021-07-01", true));
            voucherDB.add(new Voucher("K18", 15, 20000, "2021-05-01", "2021-06-01", true));
        }
    }

    public void getViews(){
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigation);
        btnFruit = (ImageButton)findViewById(R.id.btnFruit);
        btnCart = (ImageButton)findViewById(R.id.btnCart);
        btnOrganic = (ImageButton)findViewById(R.id.btnOrganic);
        btnLocation = findViewById(R.id.btnLocation);
        btnFreshFood = findViewById(R.id.btnFreshFood);
        btnMarket = findViewById(R.id.btnMarket);
        btnPromotion = findViewById(R.id.btnPromotion);
        btnBestSelling = findViewById(R.id.btnBestSelling);
        btnSearch = findViewById(R.id.btnSearch);
        btnHighlyRated = findViewById(R.id.btnHighlyRated);
    }

    public void setOnclickViews(){
        btnFruit.setOnClickListener(this);
        btnCart.setOnClickListener(this);
        btnOrganic.setOnClickListener(this);
        btnLocation.setOnClickListener(this);
        btnFreshFood.setOnClickListener(this);
        btnMarket.setOnClickListener(this);
        btnPromotion.setOnClickListener(this);
        btnBestSelling.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnHighlyRated.setOnClickListener(this);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnFruit:
                goListProduct("Fruit", "fruit");
                break;
            case R.id.btnFreshFood:
                goListProduct("Fresh Food", "fresh_food");
                break;
            case R.id.btnMarket: case R.id.btnSearch:
               goListProduct("Market", "market");
                break;
            case R.id.btnPromotion:
                goListProduct("Promotion", "promotion");
                break;
            case R.id.btnBestSelling:
                goListProduct("Best Selling", "best_selling");
                break;
            case R.id.btnHighlyRated:
                goListProduct("Highly Rated", "highly_rated");
                break;
            case R.id.btnCart:
                intentNext = new Intent(this, CartActivity.class);
                startActivity(intentNext);
                break;
            case R.id.btnOrganic:
                intentNext = new Intent(this, AddProductActivity.class);
                startActivity(intentNext);
                break;
            case R.id.btnLocation:
                intentNext = new Intent(this, BranchActivity.class);
                startActivity(intentNext);
                break;
        }
    }

    public void goListProduct(String category, String key){
        intentNext = new Intent(this, ListProductActivity.class);
        intentNext.putExtra("category", categoryDB.get(category));
        intentNext.putExtra("key", key);
        startActivity(intentNext);
    }
}