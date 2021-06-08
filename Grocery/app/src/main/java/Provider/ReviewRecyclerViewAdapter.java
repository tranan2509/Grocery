package Provider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import DBUtil.AccountDB;
import DBUtil.CustomerDB;
import Model.Account;
import Model.Customer;
import Model.Product;
import Model.Review;
import hcmute.edu.vn.mssv18110249.R;

public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.ViewHolder>{


    private List<Review> reviews;
    private Context context;

    public ReviewRecyclerViewAdapter(List<Review> reviews){
        this.reviews = reviews;
    }
    public ReviewRecyclerViewAdapter(List<Review> reviews, Context context){
        this.reviews = reviews;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View reviewView;
        reviewView = inflater.inflate(R.layout.review_item, parent, false);
        ReviewRecyclerViewAdapter.ViewHolder viewHolder = new ReviewRecyclerViewAdapter.ViewHolder(reviewView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewRecyclerViewAdapter.ViewHolder holder, int position) {
        Review review = reviews.get(position);
        CustomerDB customerDB = new CustomerDB(context);
        AccountDB accountDB = new AccountDB(context);
        Customer customer = customerDB.getCustomer(review.getCustomerId());
        Account account = accountDB.getAccount(customer.getAccountId());

        holder.txtViewEmail.setText(account.getEmail().split("@")[0]);
        holder.txtViewDescription.setText(review.getDescription());
        holder.txtViewDate.setText(review.getDate());
        if (account.isEmail()){
            new DownloadImageTask(holder.imgAvatar)
                    .execute(customer.getAvatar());
        }else{
            holder.imgAvatar.setImageBitmap(BitmapConvert.StringToBitMap(customer.getAvatar()));
        }

        ImageView[] imgStars = {holder.imgStar01, holder.imgStar02, holder.imgStar03, holder.imgStar04, holder.imgStar05};
        for (int i = 0; i < review.getRate(); i++){
            imgStars[i].setImageResource(R.drawable.ic_baseline_star_24);
        }
        for (int i = review.getRate(); i < 5; i++){
            imgStars[i].setImageResource(R.drawable.ic_baseline_star_48_none);
        }
    }

    @Override
    public int getItemCount() {
        if (reviews != null)
            return reviews.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imgAvatar, imgStar01, imgStar02, imgStar03, imgStar04, imgStar05;
        private TextView txtViewEmail, txtViewDescription, txtViewDate;

        public ViewHolder(View itemView){
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            imgStar01 = itemView.findViewById(R.id.imgStar01);
            imgStar02 = itemView.findViewById(R.id.imgStar02);
            imgStar03 = itemView.findViewById(R.id.imgStar03);
            imgStar04 = itemView.findViewById(R.id.imgStar04);
            imgStar05 = itemView.findViewById(R.id.imgStar05);
            txtViewEmail = itemView.findViewById(R.id.txtViewEmail);
            txtViewDescription = itemView.findViewById(R.id.txtViewDescription);
            txtViewDate = itemView.findViewById(R.id.txtViewDate);
        }

        @Override
        public void onClick(View view){
            Toast.makeText(view.getContext(), "position = " + getLayoutPosition(), Toast.LENGTH_SHORT).show();
        }
    }

}
