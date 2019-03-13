package com.yagna.petra.app.Cashier;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yagna.petra.app.Model.Transaction;
import com.yagna.petra.app.R;
import com.yagna.petra.app.Util.Utils;

import org.json.JSONException;

import java.util.ArrayList;

public class TodayTransactionAdpter extends RecyclerView.Adapter<TodayTransactionAdpter.MyViewHolder> {

    private final Activity ctx;
    private ArrayList<Transaction> transactions;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_details, txt_price,txt_credit,txt_time;

        public MyViewHolder(View view) {
            super(view);
            txt_details = (TextView) view.findViewById(R.id.txt_details);
            txt_price = (TextView) view.findViewById(R.id.txt_price);
            txt_credit = (TextView) view.findViewById(R.id.txt_credit);
            txt_time= (TextView) view.findViewById(R.id.txt_time);
        }
    }


    public TodayTransactionAdpter(Activity mainActivity,ArrayList<Transaction> transactions) {
        this.transactions = transactions;
        ctx=mainActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_today_list, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.txt_details.setText("Payment Method: "+ transactions.get(position).pay_method);
        holder.txt_price.setText("$"+ transactions.get(position).total_price);

        holder.txt_credit.setText(transactions.get(position).tokens_used);

        holder.txt_time.setText(Utils.getConvetredDate(transactions.get(position).transaction_complete));

    }



    @Override
    public int getItemCount() {
        return transactions.size();
    }
}