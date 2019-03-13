package com.yagna.petra.app.AdvncedCashier.Product;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yagna.petra.app.AdvncedCashier.DataKeeper;
import com.yagna.petra.app.Model.Product.Products;
import com.yagna.petra.app.Product.Discount.DiscountActivity;
import com.yagna.petra.app.Product.Modifer.ModifierActivity;
import com.yagna.petra.app.R;
import com.yagna.petra.app.Util.CustomDialog;
import com.yagna.petra.app.Util.Utils;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private final SharedPreferences common_mypref;
    final CustomDialog cd;
    private final Utils util;
    private final ArrayList<Products.Product> arrayData;
    private final ArrayList<Integer> select_array;
    ProductActivity context;
    private static LayoutInflater inflater=null;


    public ProductAdapter(ProductActivity productActivity, ArrayList<Products.Product> arrayData, ArrayList<Integer> selec_array) {
        context=productActivity;
        util=new Utils(context.getApplicationContext());
        this.arrayData=arrayData;

        common_mypref = context.getSharedPreferences(
                "user", 0);
        cd=new CustomDialog(context);
          select_array = selec_array;
        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.c_item_category, parent, false);
        ProductAdapter.ViewHolder vh = new ProductAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.p_name.setText(arrayData.get(position).title);
        holder.p_price.setText("$ "+arrayData.get(position).price);
        holder.cb_chcek.setVisibility(View.GONE);
        holder.cv_add.setVisibility(View.VISIBLE);
        boolean wannaAdd= true;
        /*for(int i = 0; i< DataKeeper.product_id_selected.size(); i++){
            if(arrayData.get(position).id.equalsIgnoreCase(DataKeeper.product_id_selected.get(i))){
                wannaAdd = false;
                break;
            }
        }*/
        /*if (wannaAdd)
        {
            holder.rd_wrapper.setVisibility(View.VISIBLE);

        }
        else
        {
            holder.rd_wrapper.setVisibility(View.GONE);

        }*/

        holder.rd_wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.addTag(arrayData.get(position));
                notifyDataSetChanged();
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
        TextView p_name,p_price,product_sku,p_description, txt_add_modif,txt_add_discount,p_stock,txt_titledescription,txt_titleprice,txt_titlepcode,txt_titlepname;
        ImageView p_img;
        LinearLayout discount,modifier,rd_wrapper;
        Button edit,delete;
        AppCompatCheckBox cb_chcek;
        CardView cv_add;

        public ViewHolder(View rowView) {
            super(rowView);
            p_name = (TextView) rowView.findViewById(R.id.rd_header_text);
            p_price = (TextView) rowView.findViewById(R.id.rd_sub_header_text);
            cb_chcek = (AppCompatCheckBox) rowView.findViewById(R.id.cb_chcek);
            rd_wrapper= (LinearLayout) rowView.findViewById(R.id.ly_main);
            cv_add = rowView.findViewById(R.id.cv_add);
        }
    }
}
