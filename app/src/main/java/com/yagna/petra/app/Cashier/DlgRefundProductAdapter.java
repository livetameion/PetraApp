package com.yagna.petra.app.Cashier;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yagna.petra.app.Model.AdvTransactions;
import com.yagna.petra.app.R;
import com.yagna.petra.app.Util.CustomDialog;
import com.yagna.petra.app.Util.Utils;

import org.json.JSONException;

import java.util.ArrayList;

public class DlgRefundProductAdapter extends RecyclerView.Adapter<DlgRefundProductAdapter.ViewHolder> {

    private final SharedPreferences common_mypref;
    final CustomDialog cd;
    private final Utils util;
    private final ArrayList<AdvTransactions.AdvTransaction.Products.Product> arrayData;
    private final AdvTransactions.AdvTransaction transact;
    AdvTransactionListActivity context;
    private static LayoutInflater inflater = null;

    public DlgRefundProductAdapter(AdvTransactionListActivity productActivity, ArrayList<AdvTransactions.AdvTransaction.Products.Product> arrayData, AdvTransactions.AdvTransaction transaction) {
        context = productActivity;
        util = new Utils(context.getApplicationContext());
        this.arrayData = arrayData;

        common_mypref = context.getSharedPreferences(
                "user", 0);
        cd = new CustomDialog(context);
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        transact = transaction;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dlg_refund_product, parent, false);
        DlgRefundProductAdapter.ViewHolder vh = new DlgRefundProductAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.p_name.setText(arrayData.get(position).title + "(x" + arrayData.get(position).purchased_qty + ")");
        holder.rd_header_price.setText("$" + String.format("%.2f", Double.parseDouble(arrayData.get(position).sale_price) * Integer.parseInt(arrayData.get(position).purchased_qty)));
        holder.cb_original.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    context.cb_orginal[position] = 1;
                else
                    context.cb_orginal[position] = 0;

                notifyDataSetChanged();
                context.calculateRefundTotal(transact);
            }
        });

        if(arrayData.get(position).refunded){
            holder.cb_original.setChecked(false);
            holder.cb_original.setClickable(false);
            holder.cb_original.setEnabled(false);
            int states[][] = {{android.R.attr.state_checked}, {}};
            int colors[] = {R.color.dark_grey, R.color.dark_grey};
            CompoundButtonCompat.setButtonTintList(holder.cb_original, new ColorStateList(states, colors));
            holder.p_name.setTextColor(context.getResources().getColor(R.color.dark_grey));
            holder.rd_header_price.setTextColor(context.getResources().getColor(R.color.dark_grey));
        }


        if (context.cb_orginal[position] == 1) {
            holder.cb_original.setChecked(true);
        } else {
            holder.cb_original.setChecked(false);
        }


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
        TextView p_name, p_qty, product_sku, p_description, txt_add_modif, txt_add_discount, p_stock, txt_titledescription, txt_titleprice, txt_titlepcode, txt_titlepname;
        ImageView p_img;
        LinearLayout discount, modifier, rd_wrapper;
        Button edit, delete;
        CheckBox cb_original;
        CardView cv_add;

        public ViewHolder(View rowView) {
            super(rowView);
            p_name = (TextView) rowView.findViewById(R.id.rd_header_text);
            rd_header_price = (TextView) rowView.findViewById(R.id.txt_ref_org_price);
            cb_original = (CheckBox) rowView.findViewById(R.id.cb_original);


        }
    }


}
