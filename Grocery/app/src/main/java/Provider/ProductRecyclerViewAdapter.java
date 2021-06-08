package Provider;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import Model.Cart;
import Model.Product;
import hcmute.edu.vn.mssv18110249.BuyProductActivity;
import hcmute.edu.vn.mssv18110249.ListProductActivity;
import hcmute.edu.vn.mssv18110249.R;

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder>{

    private List<Product>products;
    private Context context;
    private boolean isList;

    public List<Product> getProducts() {
        return this.products;
    }

    public void setProducts(final List<Product> mProducts) {
        this.products = mProducts;
        notifyDataSetChanged();
    }


    public ProductRecyclerViewAdapter(List<Product> products){
        this.products = products;
    }

    public ProductRecyclerViewAdapter(List<Product> products, Context context){
        this.products = products;
        this.context = context;
    }

    public ProductRecyclerViewAdapter(List<Product> products, Context context, boolean isList){
        this.products = products;
        this.context = context;
        this.isList = isList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View productView;
        if (isList)
            productView = inflater.inflate(R.layout.product_view, parent, false);
        else
            productView = inflater.inflate(R.layout.product_item_recycler, parent, false);
        ProductRecyclerViewAdapter.ViewHolder viewHolder = new ProductRecyclerViewAdapter.ViewHolder(productView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductRecyclerViewAdapter.ViewHolder holder, int position) {
        Product product = products.get(position);

        holder.imgProduct.setImageBitmap(ArrayByteConvert.ConverttoBitmap(product.getImage()));
        holder.txtViewName.setText(product.getName());
        holder.txtViewPrice.setText(UnitFormatProvider.getInstance().format(product.getPrice()));
        holder.txtViewPriceDiscount.setText(UnitFormatProvider.getInstance().format(product.getPrice()*(1 - (double)product.getDiscount()/100)));
        holder.txtViewRate.setText(product.getRate() + " of " + product.getReviewers());
        if (isList)
            holder.txtViewDescription.setText(product.getDescription());
        if (isList)
            holder.layoutProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentBuy = new Intent(context.getApplicationContext(), BuyProductActivity.class);
                    intentBuy.putExtra("product", product);
                    intentBuy.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentBuy);
                }
            });
        else
            holder.cardViewProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentBuy = new Intent(context.getApplicationContext(), BuyProductActivity.class);
                    intentBuy.putExtra("product", product);
                    intentBuy.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentBuy);
                }
            });

        if (product.getDiscount() != 0) {
            String text = "<strike><font color=\'#757575\'>" + UnitFormatProvider.getInstance().format(product.getPrice()) + "</font></strike>";
            holder.txtViewPrice.setText(Html.fromHtml(text));
        }else {
            holder.txtViewPrice.setVisibility(TextView.GONE);
        }

    }

    @Override
    public int getItemCount() {
        if (products != null)
            return products.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imgProduct;
        private TextView txtViewName, txtViewPrice, txtViewPriceDiscount, txtViewRate, txtViewDescription;
        private CardView cardViewProduct;
        private ConstraintLayout layoutProduct;

        public ViewHolder(View itemView){
            super(itemView);
            if (isList){
                layoutProduct = itemView.findViewById(R.id.layoutProduct);
                txtViewDescription = itemView.findViewById(R.id.txtViewDescription);
            }
            else
                cardViewProduct = itemView.findViewById(R.id.cardViewProduct);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtViewName = itemView.findViewById(R.id.txtViewName);
            txtViewPrice = itemView.findViewById(R.id.txtViewPrice);
            txtViewPriceDiscount = itemView.findViewById(R.id.txtViewPriceDiscount);
            txtViewRate = itemView.findViewById(R.id.txtViewRate);
        }

        @Override
        public void onClick(View view){
//            Toast.makeText(view.getContext(), "position = " + getLayoutPosition(), Toast.LENGTH_SHORT).show();
        }
    }
}
