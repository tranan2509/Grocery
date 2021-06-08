package hcmute.edu.vn.mssv18110249;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import DBUtil.BillDB;
import DBUtil.BillDetailDB;
import DBUtil.CustomerDB;
import DBUtil.DatabaseHandler;
import DBUtil.ProductDB;
import DBUtil.ReviewDB;
import Model.BillDetail;
import Model.Customer;
import Model.Product;
import Model.Review;
import Provider.ProductRecyclerViewAdapter;
import Provider.ReviewRecyclerViewAdapter;
import Provider.SharedPreferenceProvider;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class ReviewActivity extends AppCompatActivity implements View.OnClickListener {

    ConstraintLayout layoutReviewContent;
    ImageButton btnBack, btnStar01, btnStar02, btnStar03, btnStar04, btnStar05;
    Button btnSubmit;
    RecyclerView recyclerViewReview;
    EditText txtDescription;
    ReviewRecyclerViewAdapter reviewRecyclerViewAdapter;
    List<Review> reviews;
    ReviewDB reviewDB;
    CustomerDB customerDB;
    BillDB billDB;
    ProductDB productDB;
    Customer customer;
    Intent intent;
    Product product;
    int rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        customerDB = new CustomerDB(this);
        reviewDB = new ReviewDB(this);
        billDB = new BillDB(this);
        productDB = new ProductDB(this);
        intent = getIntent();

        customer = (Customer)SharedPreferenceProvider.getInstance(this).get("customer");
        product = (Product)intent.getExtras().getSerializable("product");

        rate = 0;
        getView();
        setOnclick();

        getReview();

        reviews = reviewDB.getReviews(product.getId());
        loadRecyclerViewReview(reviews);

        boolean isBought = billDB.isBought(customer.getId(), product.getId());
        if (!isBought){
            btnSubmit.setVisibility(View.GONE);
            layoutReviewContent.setVisibility(View.GONE);
        }
    }

    public void getView(){
        layoutReviewContent = findViewById(R.id.layoutReviewContent);
        btnBack = findViewById(R.id.btnBack);
        btnSubmit = findViewById(R.id.btnSubmit);
        txtDescription = findViewById(R.id.txtDescription);
        recyclerViewReview = findViewById(R.id.recyclerViewReview);
        btnStar01 = findViewById(R.id.btnStar01);
        btnStar02 = findViewById(R.id.btnStar02);
        btnStar03 = findViewById(R.id.btnStar03);
        btnStar04 = findViewById(R.id.btnStar04);
        btnStar05 = findViewById(R.id.btnStar05);
    }

    public void setOnclick(){
        btnBack.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnStar01.setOnClickListener(this);
        btnStar02.setOnClickListener(this);
        btnStar03.setOnClickListener(this);
        btnStar04.setOnClickListener(this);
        btnStar05.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnSubmit:
                submit();
                break;
            case R.id.btnStar01:
                setRate(1);
                break;
            case R.id.btnStar02:
                setRate(2);
                break;
            case R.id.btnStar03:
                setRate(3);
                break;
            case R.id.btnStar04:
                setRate(4);
                break;
            case R.id.btnStar05:
                setRate(5);
                break;
        }
    }

    public void getReview(){
        Review review = reviewDB.get(customer.getId(), product.getId());
        if (review != null){
            txtDescription.setText(review.getDescription());
            setRate(review.getRate());
            setReview("Submit");
        }
    }

    public void setRate(int ratePosition){
        rate = ratePosition;
        ImageView[] imgStars = {btnStar01, btnStar02, btnStar03, btnStar04, btnStar05};
        for (int i = 0; i < rate; i++){
            imgStars[i].setImageResource(R.drawable.ic_baseline_star_48);
        }
        for (int i = rate; i < 5; i++){
            imgStars[i].setImageResource(R.drawable.ic_baseline_star_48_none);
        }
    }

    public void submit(){
        if (btnSubmit.getText().toString().equals("Submit")) {
            String description = txtDescription.getText().toString();
            if (!description.equals("") || rate == 0) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Calendar cal = Calendar.getInstance();
                String date = dateFormat.format(cal.getTime());
                Review review = reviewDB.get(customer.getId(), product.getId());
                if (review == null) {
                    review = new Review(customer.getId(), product.getId(), rate, description, date);
                    reviewDB.add(review);

                    double newRate = (product.getRate() * product.getReviewers() + rate)/(product.getReviewers() + 1);
                    product.setReviewers(product.getReviewers() + 1);
                    product.setRate(newRate);
                    productDB.update(product);
                } else {
                    double newRate = (product.getRate() * product.getReviewers() + rate - review.getRate())/(product.getReviewers());
                    product.setRate(newRate);
                    productDB.update(product);
                    review.setRate(rate);
                    review.setDescription(description);
                    review.setDate(date);
                    reviewDB.update(review);
                }
                reviews = reviewDB.getReviews(product.getId());
                loadRecyclerViewReview(reviews);
                setReview("Submit");
            } else {
                Toast.makeText(getApplicationContext(), "Please enter your rating!", Toast.LENGTH_LONG).show();
            }
        }else{
            setReview("Edit");
        }
    }

    public void setReview(String type){
        if (type.equals("Edit")){
            btnSubmit.setText("Submit");
            txtDescription.setEnabled(true);
            btnStar01.setEnabled(true);
            btnStar02.setEnabled(true);
            btnStar03.setEnabled(true);
            btnStar04.setEnabled(true);
            btnStar05.setEnabled(true);
        }else{
            txtDescription.setEnabled(false);
            btnSubmit.setText("Edit");
            btnStar01.setEnabled(false);
            btnStar02.setEnabled(false);
            btnStar03.setEnabled(false);
            btnStar04.setEnabled(false);
            btnStar05.setEnabled(false);
        }
    }

    public void loadRecyclerViewReview(List<Review> reviews){
        if (reviews != null && reviews.size() > 0) {
            recyclerViewReview.setLayoutManager(new LinearLayoutManager(this));
            reviewRecyclerViewAdapter = new ReviewRecyclerViewAdapter(reviews, getApplicationContext());
            recyclerViewReview.setAdapter(reviewRecyclerViewAdapter);
            recyclerViewReview.setItemAnimator(new SlideInUpAnimator());
        }
    }

}