package com.yagna.petra.app.Cashier;

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

import com.yagna.petra.app.Model.AdvTransactions;
import com.yagna.petra.app.Model.Product.Products;
import com.yagna.petra.app.R;
import com.yagna.petra.app.Util.CustomDialog;
import com.yagna.petra.app.Util.Utils;

import java.util.ArrayList;

public class DlgProductAdapter extends RecyclerView.Adapter<DlgProductAdapter.ViewHolder> {

    private final SharedPreferences common_mypref;
    final CustomDialog cd;
    private final Utils util;
    private final ArrayList<AdvTransactions.AdvTransaction.Products.Product> arrayData;
    Activity context;
    private static LayoutInflater inflater=null;

    public DlgProductAdapter(Activity productActivity, ArrayList<AdvTransactions.AdvTransaction.Products.Product> arrayData) {
        context=productActivity;
        util=new Utils(context.getApplicationContext());
        this.arrayData=arrayData;

        common_mypref = context.getSharedPreferences(
                "user", 0);
        cd=new CustomDialog(context);
        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        TextView p_name, p_qty,product_sku,p_description, txt_add_modif,txt_add_discount,p_stock,txt_titledescription,txt_titleprice,txt_titlepcode,txt_titlepname;
        ImageView p_img;
        LinearLayout discount,modifier,rd_wrapper;
        Button edit,delete;
        AppCompatCheckBox cb_chcek;
        CardView cv_add;

        public ViewHolder(View rowView) {
            super(rowView);
            p_name = (TextView) rowView.findViewById(R.id.rd_header_text);
            rd_header_price = (TextView) rowView.findViewById(R.id.rd_header_price);


        }
    }





}
