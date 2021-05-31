package Provider;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import Model.Product;
import hcmute.edu.vn.mssv18110249.R;

public class ProductListViewAdapter extends BaseAdapter {
    final List<Product> products;

    public ProductListViewAdapter (List<Product> products){
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(final int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return products.get(position).getId();
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        View viewProduct;
        if (convertView == null)
            viewProduct = View.inflate(parent.getContext(), R.layout.product_view, null);
        else
            viewProduct = convertView;

        Product product = (Product)getItem(position);
        ImageView imgImage = (ImageView)viewProduct.findViewById(R.id.imgImage);
        TextView txtViewName = (TextView)viewProduct.findViewById(R.id.txtViewName);
        TextView txtViewDescription = (TextView)viewProduct.findViewById(R.id.txtViewDescription);
        TextView txtViewRate = (TextView)viewProduct.findViewById(R.id.txtViewRate);
        TextView txtViewPrice = (TextView)viewProduct.findViewById(R.id.txtViewPrice);

        imgImage.setImageBitmap(ArrayByteConvert.ConverttoBitmap(product.getImage()));
        txtViewName.setText(product.getName());
        txtViewDescription.setText(product.getDescription());
        txtViewRate.setText(product.getRate() + " of " + product.getReviewers());
        txtViewPrice.setText(UnitFormatProvider.getInstance().format(product.getPrice()));

        return viewProduct;
    }
}
