package Provider;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import DBUtil.BranchDB;
import Model.Bill;
import Model.Branch;
import Model.Product;
import hcmute.edu.vn.mssv18110249.R;

public class HistoryListViewAdapter extends BaseAdapter {
    final List<Bill> bills;

    public HistoryListViewAdapter (List<Bill> bills){
        this.bills = bills;
    }

    @Override
    public int getCount() {
        return bills.size();
    }

    @Override
    public Object getItem(final int position) {
        return bills.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return bills.get(position).getId();
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        View view;
        if (convertView == null)
            view = View.inflate(parent.getContext(), R.layout.history_item, null);
        else
            view = convertView;

        Bill bill = (Bill)getItem(position);
        Context context = view.getContext();
        Branch branch = new BranchDB(context).get(bill.getBranchId());

        TextView txtViewBranch, txtViewTime, txtViewState, txtViewAmount;
        txtViewBranch = view.findViewById(R.id.txtViewBranch);
        txtViewTime = view.findViewById(R.id.txtViewTime);
        txtViewState = view.findViewById(R.id.txtViewState);
        txtViewAmount = view.findViewById(R.id.txtViewAmount);

        txtViewBranch.setText(branch.getName());
        txtViewTime.setText(bill.getDate());
        txtViewState.setText(bill.getState());
        txtViewAmount.setText(UnitFormatProvider.getInstance().format(bill.getAmount()));

        return view;
    }
}
