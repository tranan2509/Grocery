package Provider;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import DBUtil.BillDetailDB;
import DBUtil.ProductDB;
import Model.Bill;
import Model.BillDetail;
import Model.Product;
import hcmute.edu.vn.mssv18110249.R;

public class PaymentListViewAdapter extends BaseAdapter {
    final List<BillDetail> billDetails;

    public PaymentListViewAdapter(final List<BillDetail> billDetails) {
        this.billDetails = billDetails;
    }

    @Override
    public int getCount() {
        return billDetails.size();
    }

    @Override
    public Object getItem(final int position) {
        return billDetails.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return billDetails.get(position).getId();
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        View view;
        if (convertView == null)
            view = View.inflate(parent.getContext(), R.layout.payment_item, null);
        else
            view = convertView;

        Context context = view.getContext();
        BillDetailDB billDetailDB = new BillDetailDB(context);
        ProductDB productDB = new ProductDB(context);

        BillDetail billDetail = (BillDetail)getItem(position);

        TextView txtViewName = (TextView) view.findViewById(R.id.txtViewName);
        TextView txtViewPrice = (TextView)view.findViewById(R.id.txtViewPrice);
        TextView txtViewQuantity = (TextView)view.findViewById(R.id.txtViewQuantity);
        TextView txtViewAmount = (TextView)view.findViewById(R.id.txtViewAmount);

        Product product = productDB.get(billDetail.getProductId());
        txtViewName.setText(product.getName());
        txtViewPrice.setText(UnitFormatProvider.getInstance().format(product.getPrice() * (1 - (double)product.getDiscount()/100)));
        txtViewQuantity.setText("x" + billDetail.getQuantity());
        txtViewAmount.setText(UnitFormatProvider.getInstance().format(billDetail.getAmount()));

        return view;
    }
}
