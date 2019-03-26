package com.pos.petra.app.AdvancedCashier.Product;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pos.petra.app.AdvancedCashier.DataKeeper;
import com.pos.petra.app.Model.Product.Discount;
import com.pos.petra.app.R;
import com.pos.petra.app.Util.CustomDialog;
import com.pos.petra.app.Util.Utils;

import java.util.ArrayList;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.ViewHolder> {

    private final Utils util;
    private final ArrayList<Discount> arrayData;
    ProductDetailActivity context;

    public DiscountAdapter(ProductDetailActivity ModifierActivity, ArrayList<Discount> arrayData) {
        context=ModifierActivity;
        util=new Utils(context.getApplicationContext());
        this.arrayData=arrayData;      
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.d_dlg_disc, parent, false);
        DiscountAdapter.ViewHolder vh = new DiscountAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.product_terms.setText(arrayData.get(position).term_conditon);
        holder.p_name.setText(arrayData.get(position).title + "("+arrayData.get(position).subtitle+"% off)");
        holder.p_description.setText(arrayData.get(position).description);
        if(arrayData.get(position).isActive.equalsIgnoreCase("1")){
            holder.isActive.setText("Active");
            holder.isActive.setTextColor(context.getResources().getColor(R.color.green));
        }
        else
        {
            holder.isActive.setText("Inactive");
            holder.isActive.setTextColor(context.getResources().getColor(R.color.red));
        }
        holder.ly_categ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrayData.get(position).isActive.equalsIgnoreCase("1")){
                    DataKeeper.detail_product.selected_discount=""+position;
                    context.dialogDiscount.dismiss();
                    context.fillDetail();
                }
                else
                {
                    util.customToast("Discount Offer is inactive right now by the Admin");
                }


            }
        });
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
        TextView p_name, isActive,p_description, product_terms,  rate;
        Button edit,apply;
        LinearLayout ly_categ;

        public ViewHolder(View rowView) {
            super(rowView);
            rate = rowView.findViewById(R.id.product_rate);
            p_name =  rowView.findViewById(R.id.product_name);
            isActive =  rowView.findViewById(R.id.product_active);
            p_description =  rowView.findViewById(R.id.product_desc);
            edit= rowView.findViewById(R.id.edit_);
            apply= rowView.findViewById(R.id.delete_);
            product_terms =  rowView.findViewById(R.id.product_terms);
            ly_categ=  rowView.findViewById(R.id.ly_categ);
        }
    }
}
