package Provider;

import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

import DBUtil.CartDB;
import DBUtil.ProductDB;
import Model.Cart;
import Model.Product;
import hcmute.edu.vn.mssv18110249.R;

public class CartListViewAdapter extends BaseAdapter {
    final List<Cart> carts;

    public CartListViewAdapter (List<Cart> carts){
        this.carts = carts;
    }

    @Override
    public int getCount() {
        return carts.size();
    }

    @Override
    public Object getItem(final int position) {
        return carts.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return carts.get(position).getId();
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        View viewProduct;
        if (convertView == null)
            viewProduct = View.inflate(parent.getContext(), R.layout.product_view, null);
        else
            viewProduct = convertView;

        ProductDB productDB = new ProductDB(convertView.getContext()) ;
        CartDB cartDB = new CartDB(convertView.getContext());

        Cart cart = (Cart)getItem(position);
        Product product = productDB.get(cartDB.getProductId(cart.getId()));

        ImageView imgImage = (ImageView)viewProduct.findViewById(R.id.imgImage);
        TextView txtViewName = (TextView)viewProduct.findViewById(R.id.txtViewName);
        TextView txtViewPrice = (TextView)viewProduct.findViewById(R.id.txtViewPrice);
        TextView txtViewPriceDiscount = (TextView)viewProduct.findViewById(R.id.txtViewPriceDiscount);
        TextView txtQuantity = (TextView)viewProduct.findViewById(R.id.txtQuantity);
        Button btnReduction = (Button)viewProduct.findViewById(R.id.btnReduction);
        Button btnIncrease = (Button)viewProduct.findViewById(R.id.btnIncrease);
        CheckBox ckbState = (CheckBox)viewProduct.findViewById(R.id.ckbState);

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("$"));

        imgImage.setImageBitmap(BitmapConvert.StringToBitMap(product.getImage()));
        txtViewName.setText(product.getName());
        txtViewPrice.setText(format.format(product.getPrice()));
        txtViewPriceDiscount.setText(format.format(product.getPrice()*(1 - product.getDiscount()/100)));
        txtQuantity.setText(cart.getQuantity());
        ckbState.setChecked(cart.isState());

        if (product.getDiscount() != 0) {
            String text = "<strike><font color=\'#757575\'>" + product.getPrice() + "</font></strike>";
            txtViewPrice.setText(Html.fromHtml(text));
        }else {
            txtViewPrice.setWidth(0);
            ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 0, 0);
            txtViewPriceDiscount.setLayoutParams(lp);
        }

        btnReduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(txtQuantity.getText().toString());
                if (quantity > 1){
                    cart.setQuantity(quantity - 1);
                    cartDB.update(cart);
                }else{
                    //

                    cartDB.delete(cart);
                }
            }
        });

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(txtQuantity.getText().toString());
                if (quantity < product.getQuantity()){
                    cart.setQuantity(quantity + 1);
                    cartDB.update(cart);
                }else{
                    //

                }
            }
        });


        return viewProduct;
    }
}
