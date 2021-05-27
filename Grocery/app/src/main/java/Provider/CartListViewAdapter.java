package Provider;

import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
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
            viewProduct = View.inflate(parent.getContext(), R.layout.cart_item, null);
        else
            viewProduct = convertView;

        CartDB cartDB = new CartDB(viewProduct.getContext());
        ProductDB productDB = new ProductDB(viewProduct.getContext()) ;

        Cart cart = (Cart)getItem(position);
        Product product = productDB.get(cartDB.getProductId(cart.getId()));

        ImageView imgImage = (ImageView)viewProduct.findViewById(R.id.imgImage);
        TextView txtViewName = (TextView)viewProduct.findViewById(R.id.txtViewName);
        TextView txtViewPrice = (TextView)viewProduct.findViewById(R.id.txtViewPrice);
        TextView txtViewPriceDiscount = (TextView)viewProduct.findViewById(R.id.txtViewPriceDiscount);
        TextView txtQuantity = (TextView)viewProduct.findViewById(R.id.txtQuantity);
        Button btnReduction = (Button)viewProduct.findViewById(R.id.btnReduction);
        Button btnIncrease = (Button)viewProduct.findViewById(R.id.btnIncrease);
        ImageButton btnRemove = (ImageButton)viewProduct.findViewById(R.id.btnRemove);
        CheckBox ckbState = (CheckBox)viewProduct.findViewById(R.id.ckbState);

        imgImage.setImageBitmap(BitmapConvert.StringToBitMap(product.getImage()));
        txtViewName.setText(product.getName());
        txtViewPrice.setText(UnitFormatProvider.getInstance().format(product.getPrice()));
        txtViewPriceDiscount.setText(UnitFormatProvider.getInstance().format(product.getPrice()*(1 - (double)product.getDiscount()/100)));
        txtQuantity.setText(String.valueOf(cart.getQuantity()));
        ckbState.setChecked(cart.isState());

        if (product.getDiscount() != 0) {
            String text = "<strike><font color=\'#757575\'>" + product.getPrice() + "</font></strike>";
            txtViewPrice.setText(Html.fromHtml(text));
        }else {
            txtViewPrice.setWidth(1);
        }

        btnReduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(txtQuantity.getText().toString());
                if (quantity > 1){
                    cart.setQuantity(quantity - 1);
                    cartDB.update(cart);
                    txtQuantity.setText(String.valueOf(cart.getQuantity()));
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
                    txtQuantity.setText(String.valueOf(cart.getQuantity()));
                }else{
                    //

                }
            }
        });

        ckbState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cart.setState(ckbState.isChecked());
                cartDB.update(cart);
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return viewProduct;
    }


}
