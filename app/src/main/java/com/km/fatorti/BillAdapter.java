package com.km.fatorti;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.km.fatorti.R;
import com.km.fatorti.model.Bill;

import java.util.Date;
import java.util.List;

/**
 * @author Zaid Khamis
 */
public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {
    private List<Bill> billList;
    private Context context;

    public BillAdapter(Context context, List<Bill> billList) {
        this.billList = billList;
        this.context = context;
    }

    @Override
    public BillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_bill, parent, false);
        return new BillViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        Bill bill = billList.get(position);

        ImageView imageView = holder.imageView;
        TextView companyTextView = holder.companyTextView;
        TextView dateOfIssueTextView = holder.dateOfIssueTextView;
        TextView valueTextView = holder.valueTextView;

        // Determine which image to display based on the bill attributes
        int imageResource = R.drawable.bill96;
        if (bill.getCompany().equals("GAZ")) {
            //   imageResource = R.drawable.gaz;
        } else if (bill.getCompany().equals("ELECTRICITY")) {
            // imageResource = R.drawable.electricity;
        } else if (bill.getCompany().equals("WATER"))
            //imageResource = R.drawable.water;

            Glide.with(context).load(imageResource).into(imageView);

        companyTextView.setText(bill.getCompany().toString());
        Date date = bill.getDateOfIssue();
        String dateText = date.getDate() + "/" + date.getMonth() + "/" + (date.getYear() + 1900);
        dateOfIssueTextView.setText(dateText);
        valueTextView.setText(String.valueOf(bill.getValue()));
        imageView.setOnClickListener(v -> {

            // move to PayActivity

            Intent intent = new Intent(context, BillDetails.class);

            Gson gson = new Gson();
            String billObjectStringJson = gson.toJson(bill);
            intent.putExtra("billObj", billObjectStringJson);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return billList.size();
    }

    public class BillViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView companyTextView;
        TextView dateOfIssueTextView;
        TextView valueTextView;

        public BillViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.bill_image);
            companyTextView = itemView.findViewById(R.id.company_text_view);
            dateOfIssueTextView = itemView.findViewById(R.id.date_of_issue_text_view);
            valueTextView = itemView.findViewById(R.id.value_text_view);
        }
    }
}
