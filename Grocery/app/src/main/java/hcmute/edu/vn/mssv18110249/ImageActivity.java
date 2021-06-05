package hcmute.edu.vn.mssv18110249;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import Model.Product;
import Provider.ArrayByteConvert;

public class ImageActivity extends Activity implements View.OnClickListener{

    ViewGroup layoutImageProduct;
    ImageView imgProduct;
    ImageButton btnClose;
    Product product;
    Intent intent;

    private int xDelta;
    private int yDelta;

    private ScaleGestureDetector mScaleGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        intent = getIntent();
        product = (Product)intent.getExtras().getSerializable("product");

        getView();
        setOnClick();

        setView();

        mScaleGestureDetector = new ScaleGestureDetector(imgProduct.getContext(), onScaleGestureListener);
//        imgProduct.setOnTouchListener(onTouchListener());
    }

    private View.OnTouchListener onTouchListener() {
        return new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        xDelta = x - lParams.leftMargin;
                        yDelta = y - lParams.topMargin;
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        layoutParams.leftMargin = x - xDelta;
                        layoutParams.topMargin = y - yDelta;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;
                        view.setLayoutParams(layoutParams);
                        break;
                }
                layoutImageProduct.invalidate();
                return true;
            }
        };
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        return true;
    }

    public void getView(){
        imgProduct = findViewById(R.id.imgProduct);
        btnClose = findViewById(R.id.btnClose);
        layoutImageProduct = findViewById(R.id.layoutImageProduct);
    }

    public void setOnClick(){
        btnClose.setOnClickListener(this);
    }

    public void setView(){
        imgProduct.setImageBitmap(ArrayByteConvert.ConverttoBitmap(product.getImage()));
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnClose:
                finish();
        }
    }

    private float mScaleFactor = 1.0f;
    private ScaleGestureDetector.SimpleOnScaleGestureListener onScaleGestureListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(1f, Math.min(mScaleFactor, 5.5f));
            imgProduct.setScaleX(mScaleFactor);
            imgProduct.setScaleY(mScaleFactor);
            return true;
        }
    };
}