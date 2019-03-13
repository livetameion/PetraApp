package com.yagna.petra.app.AdvncedCashier.SubCategory;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yagna.petra.app.AdvncedCashier.Product.ProductActivity;
import com.yagna.petra.app.Model.Product.SubCategories;
import com.yagna.petra.app.R;
import com.yagna.petra.app.Util.CustomDialog;
import com.yagna.petra.app.Util.Utils;

import org.json.JSONException;

import java.util.ArrayList;

public class SubCategoryReAdapter extends RecyclerView.Adapter<SubCategoryReAdapter.ViewHolder> {
    private final SharedPreferences common_mypref;
    final CustomDialog cd;
    private final Utils util;
    private final ArrayList<SubCategories.SubCategory> arrayData;
    SubCategoryActivity context;
    private static LayoutInflater inflater=null;

    public SubCategoryReAdapter(SubCategoryActivity productActivity, ArrayList<SubCategories.SubCategory> arrayData) {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.c_item_category, parent, false);
        SubCategoryReAdapter.ViewHolder vh = new SubCategoryReAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.p_name.setText(arrayData.get(position).title);
        holder.p_price.setVisibility(View.GONE);
        holder.rd_wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ProductActivity.class);
                intent.putExtra("subcateg_id",""+arrayData.get(position).id);
                intent.putExtra("subcateg_name",""+arrayData.get(position).title);
                intent.putExtra("categ_id",""+context.cate_id);
                intent.putExtra("categ_name",""+context.cate_name);
                context.startActivity(intent);
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
        TextView p_name,p_price,p_description,sub_categ_name,p_code,txt_titledescription,txt_titleprice,txt_titlepcode,txt_titlepname;
        ImageView p_img;
        LinearLayout ly_sub_categ, ly_categ_view,rd_wrapper;

        Button edit,delete;

        public ViewHolder(View rowView) {
            super(rowView);
            p_name = (TextView) rowView.findViewById(R.id.rd_header_text);
            p_price= (TextView) rowView.findViewById(R.id.rd_sub_header_text);
            rd_wrapper = (LinearLayout) rowView.findViewById(R.id.rd_wrapper);
        }
    }





}
