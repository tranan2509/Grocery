package Provider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import hcmute.edu.vn.mssv18110249.R;

public class GenderSpinnerAdapter extends BaseAdapter {
    Context context;
    int images[];
    String[] gender;
    LayoutInflater inflater;

    public GenderSpinnerAdapter(Context applicationContext, int[] flags, String[] gender){
        this.context = applicationContext;
        this.images = flags;
        this.gender = gender;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(final int position) {
        return null;
    }

    @Override
    public long getItemId(final int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        view = inflater.inflate(R.layout.gender_item, null);
        ImageView icon = (ImageView)view.findViewById(R.id.imgGender);
        TextView name = (TextView)view.findViewById(R.id.txtViewGender);
        icon.setImageResource(images[position]);
        name.setText(gender[position]);
        return view;
    }
}

