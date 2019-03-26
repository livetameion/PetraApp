package com.pos.petra.app.AdvancedTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pos.petra.app.Model.Transaction.AdvTransactions;
import com.pos.petra.app.R;
import com.pos.petra.app.Util.CustomDialog;
import com.pos.petra.app.Util.Utils;

import java.util.ArrayList;

public class DlgProductAdapter extends RecyclerView.Adapter<DlgProductAdapter.ViewHolder> {

    private final ArrayList<AdvTransactions.AdvTransaction.Products.Product> arrayData;
    Activity context;

    public DlgProductAdapter(Activity productActivity, ArrayList<AdvTransactions.AdvTransaction.Products.Product> arrayData) {
        context=productActivity;
        this.arrayData=arrayData;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dl_product_item, parent, false);
        DlgProductAdapter.ViewHolder vh = new DlgProductAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.p_name.setText(arrayData.get(position).title+"(x"+arrayData.get(position).purchased_qty+")");
        holder.rd_header_price.setText("$"+String.format("%.2f",Double.parseDouble(arrayData.get(position).sale_price)));


    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return arrayData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView rd_header_price;
        TextView p_name;

        public ViewHolder(View rowView) {
            super(rowView);
            p_name =  rowView.findViewById(R.id.rd_header_text);
            rd_header_price =  rowView.findViewById(R.id.rd_header_price);


        }
    }





}
