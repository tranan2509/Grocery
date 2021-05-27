package Provider;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

import DBUtil.CartDB;
import DBUtil.ProductDB;
import Model.Cart;
import Model.Customer;
import Model.Product;
import hcmute.edu.vn.mssv18110249.CartActivity;
import hcmute.edu.vn.mssv18110249.R;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> {

    private List<Cart> carts;
    private Context context;
    private Context contextCart;
    private ViewGroup parent;
    private CartDB cartDB;
    private ProductDB productDB;
    private Customer customer;

    public Context getContextCart() {
        return this.contextCart;
    }

    public void setContextCart(final Context contextCart) {
        this.contextCart = contextCart;
    }

    public CartRecyclerViewAdapter(List<Cart> carts){
        this.carts = carts;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgImage;
        public TextView txtViewName, txtViewPrice, txtViewPriceDiscount, txtQuantity;
        public Button btnReduction, btnIncrease;
        public ImageButton btnRemove;
        public CheckBox ckbState;

        public ViewHolder(View itemView){
            super(itemView);

            imgImage = (ImageView)itemView.findViewById(R.id.imgImage);
            txtViewName = (TextView)itemView.findViewById(R.id.txtViewName);
            txtViewPrice = (TextView)itemView.findViewById(R.id.txtViewPrice);
            txtViewPriceDiscount = (TextView)itemView.findViewById(R.id.txtViewPriceDiscount);
            txtQuantity = (TextView)itemView.findViewById(R.id.txtQuantity);
            btnReduction = (Button)itemView.findViewById(R.id.btnReduction);
            btnIncrease = (Button)itemView.findViewById(R.id.btnIncrease);
            btnRemove = (ImageButton)itemView.findViewById(R.id.btnRemove);
            ckbState = (CheckBox)itemView.findViewById(R.id.ckbState);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        this.context = context;
        this.parent = parent;
        LayoutInflater inflater = LayoutInflater.from(context);
        View cartView = inflater.inflate(R.layout.cart_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(cartView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){

        productDB = new ProductDB(context);
        cartDB = new CartDB(context);
        Cart cart = carts.get(position);
        Product product = productDB.get(cart.getProductId());

        customer = (Customer)SharedPreferenceProvider.getInstance(context).get("customer");

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("USD"));

        holder.imgImage.setImageBitmap(BitmapConvert.StringToBitMap(product.getImage()));
        holder.txtViewName.setText(product.getName());
        holder.txtViewPrice.setText(format.format(product.getPrice()));
        holder.txtViewPriceDiscount.setText(format.format(product.getPrice()*(1 - (double)product.getDiscount()/100)));
        holder.txtQuantity.setText(String.valueOf(cart.getQuantity()));
        holder.ckbState.setChecked(cart.isState());

        if (product.getDiscount() != 0) {
            String text = "<strike><font color=\'#757575\'>" + product.getPrice() + "</font></strike>";
            holder.txtViewPrice.setText(Html.fromHtml(text));
        }else {
            holder.txtViewPrice.setWidth(1);
        }

        holder.btnReduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.txtQuantity.getText().toString());
                if (quantity > 1){
                    cart.setQuantity(quantity - 1);
                    cartDB.update(cart);
                    holder.txtQuantity.setText(String.valueOf(cart.getQuantity()));
                }else{
//                    setDialog(cart, position);
//                    dialog.show();
                    cartDB.delete(cart);
                    removeItem(position);
                }
                updateAmount(carts);
            }
        });

        holder.btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.txtQuantity.getText().toString());
                if (quantity < product.getQuantity()){
                    cart.setQuantity(quantity + 1);
                    cartDB.update(cart);
                    holder.txtQuantity.setText(String.valueOf(cart.getQuantity()));
                }else{
                    //
                }
                updateAmount(carts);
            }
        });

        holder.ckbState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cart.setState(holder.ckbState.isChecked());
                cartDB.update(cart);
                updateAmount(carts);
            }
        });

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartDB.delete(cart);
                removeItem(position);
                updateAmount(carts);
            }
        });
    }
    private Dialog dialog;

    private void setDialog(Cart cart, int position){
        LayoutInflater inflater = LayoutInflater.from(context);
        View cartView = inflater.inflate(R.layout.dialog_basic, parent, false);
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_basic);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            dialog.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.bg_dialog));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation_dialog_basic;

        TextView txtViewContent = (TextView)cartView.findViewById(R.id.txtViewContent);
        Button btnOk = (Button)cartView.findViewById(R.id.btnOk);
        Button btnCancel = (Button)cartView.findViewById(R.id.btnCancel);

        txtViewContent.setText("You want to remove the product from the cart? ");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartDB.delete(cart);
                removeItem(position);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Cancel", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    private void removeItem(int position) {
        carts.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, carts.size());
    }

    public void updateAmount(List<Cart> carts){
        TextView txtViewAmount = (TextView)((Activity)contextCart).findViewById(R.id.txtViewAmount);
        ProductDB productDBAdd = new ProductDB(contextCart);
        CartDB cartDB = new CartDB(contextCart);
        Customer customer = (Customer)SharedPreferenceProvider.getInstance(contextCart).get("customer");

//        double amount = 0;
//        for (Cart cart: carts){
//            if (cart.isState()) {
//                Product product = productDBAdd.get(cart.getProductId());
//                amount += cart.getQuantity() * (product.getPrice() * (1 - (double) product.getDiscount() / 100));
//            }
//        }
        txtViewAmount.setText("$" + cartDB.getAmount(customer.getId()));
    }
}
