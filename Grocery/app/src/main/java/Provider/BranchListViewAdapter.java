package Provider;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import Model.Branch;
import Model.Product;
import hcmute.edu.vn.mssv18110249.R;

public class BranchListViewAdapter extends BaseAdapter {
    final List<Branch> branches;

    public BranchListViewAdapter (List<Branch> branches){
        this.branches = branches;
    }

    @Override
    public int getCount() {
        return branches.size();
    }

    @Override
    public Object getItem(final int position) {
        return branches.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return branches.get(position).getId();
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        View view;
        if (convertView == null)
            view = View.inflate(parent.getContext(), R.layout.branch_item, null);
        else
            view = convertView;

        Branch branch = (Branch)getItem(position);
        TextView txtViewName, txtViewAddress;
        txtViewName = view.findViewById(R.id.txtViewName);
        txtViewAddress = view.findViewById(R.id.txtViewAddress);
        txtViewName.setText(branch.getName());
        txtViewAddress.setText(branch.getAddress());

        return view;
    }
}
