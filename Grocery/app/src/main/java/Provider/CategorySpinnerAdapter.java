package Provider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

import Model.Category;
import Model.Product;
import hcmute.edu.vn.mssv18110249.R;

public class CategorySpinnerAdapter extends BaseAdapter {
    final List<Category> categories;
    Context context;
    LayoutInflater inflter;
    public CategorySpinnerAdapter(Context context, List<Category> categories){
        this.context = context;
        this.categories = categories;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(final int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return categories.get(position).getId();
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        view = inflter.inflate(R.layout.spn_item, null);
        TextView names = (TextView) view.findViewById(R.id.txtViewText);
        names.setText(categories.get(position).getName());
        return view;
    }
}
