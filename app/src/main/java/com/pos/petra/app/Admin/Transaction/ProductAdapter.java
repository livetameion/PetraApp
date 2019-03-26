package com.pos.petra.app.Admin.Transaction;

import android.app.Activity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pos.petra.app.R;

import org.json.JSONArray;
import org.json.JSONException;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private final JSONArray arrayData;
    Activity context;

    public ProductAdapter(Activity productActivity, JSONArray arrayData) {
        context=productActivity;
        this.arrayData=arrayData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.p_product_item, parent, false);
        ProductAdapter.ViewHolder vh = new ProductAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            holder.p_name.setText("ProductId: "+ arrayData.getJSONObject(position).getString("product_id")+ " (X"+arrayData.getJSONObject(position).getString("qty")+")");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            holder.rd_header_price.setText("$"+String.format("%.2f",Double.parseDouble(arrayData.getJSONObject(position).getString("product_price"))));

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.p_qty.setVisibility(View.GONE);
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return arrayData.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView rd_header_price;
        TextView p_name, p_qty;
        LinearLayout rd_wrapper;
        AppCompatCheckBox cb_chcek;
        CardView cv_add;

        public ViewHolder(View rowView) {
            super(rowView);
            p_name = rowView.findViewById(R.id.rd_header_text);
            rd_header_price =  rowView.findViewById(R.id.rd_header_price);
            p_qty =  rowView.findViewById(R.id.rd_sub_header_text);
            cb_chcek =  rowView.findViewById(R.id.cb_chcek);
            rd_wrapper= rowView.findViewById(R.id.ly_main);
            cv_add = rowView.findViewById(R.id.cv_add);
            (rowView.findViewById(R.id.cv_extra)).setVisibility(View.GONE);
        }
    }





}
