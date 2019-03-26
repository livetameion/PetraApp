package com.pos.petra.app.AdvancedCashier.Discount;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pos.petra.app.AdvancedCashier.DataKeeper;
import com.pos.petra.app.Model.Product.Discount;
import com.pos.petra.app.R;
import com.pos.petra.app.Util.CustomDialog;
import com.pos.petra.app.Util.Utils;

import java.util.ArrayList;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.ViewHolder> {

    final CustomDialog cd;
    private final Utils util;
    private final ArrayList<Discount> arrayData;
    DiscountActivity context;

    public DiscountAdapter(DiscountActivity ModifierActivity, ArrayList<Discount> arrayData) {
        context=ModifierActivity;
        util=new Utils(context.getApplicationContext());
        this.arrayData=arrayData;

        cd=new CustomDialog(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.d_discount_item, parent, false);
        DiscountAdapter.ViewHolder vh = new DiscountAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.rate.setText(arrayData.get(position).subtitle);
        holder.product_terms.setText(arrayData.get(position).term_conditon);
        holder.p_name.setText(arrayData.get(position).title);
        if(arrayData.get(position).isActive.equalsIgnoreCase("1")){
            holder.isActive.setText("Active");
        }
        else
        {
            holder.isActive.setText("Inactive");
        }
        holder.p_description.setText(arrayData.get(position).description);

        holder.apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrayData.get(position).isActive.equalsIgnoreCase("1")){
                    DataKeeper.products_array.get(context.posit_id).selected_discount=""+position;
                    context.finish();
                }
                else
                {
                    util.customToast("Sorry can not apply this offer.\nDiscount Offer is inactive right now by the Admin");
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
        TextView p_name, isActive,p_description, product_terms, txt_title_code, rate,txt_titledescription,txt_titleprice, txt_titlepcode, txt_titlepname;
        Button edit, apply;
        CardView ly_categ;

        public ViewHolder(View rowView) {
            super(rowView);
            rate =(TextView) rowView.findViewById(R.id.product_rate);
            p_name = (TextView) rowView.findViewById(R.id.product_name);
            isActive = (TextView) rowView.findViewById(R.id.product_active);
            p_description = (TextView) rowView.findViewById(R.id.product_desc);
            edit=(Button) rowView.findViewById(R.id.edit_);
            apply =(Button) rowView.findViewById(R.id.delete_);
            product_terms = (TextView) rowView.findViewById(R.id.product_terms);
            ly_categ= (CardView) rowView.findViewById(R.id.ly_categ);
        }
    }
}
