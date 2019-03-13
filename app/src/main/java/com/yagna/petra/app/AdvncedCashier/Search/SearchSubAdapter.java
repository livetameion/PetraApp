package com.yagna.petra.app.AdvncedCashier.Search;

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
import com.yagna.petra.app.AdvncedCashier.SubCategory.SubCategoryActivity;
import com.yagna.petra.app.Model.Product.SubCategories;
import com.yagna.petra.app.R;
import com.yagna.petra.app.Util.CustomDialog;
import com.yagna.petra.app.Util.Utils;

import java.util.ArrayList;

public class SearchSubAdapter extends RecyclerView.Adapter<SearchSubAdapter.ViewHolder> {
    private final SharedPreferences common_mypref;
    final CustomDialog cd;
    private final Utils util;
    private final ArrayList<SubCategories.SubCategory> arrayData;
    private final boolean mTwoPane;
    private final int cat_posit;
    SearchActivity context;
    private static LayoutInflater inflater = null;

    public SearchSubAdapter(SearchActivity productActivity, ArrayList<SubCategories.SubCategory> arrayData, boolean mTwoPane, int position) {
        context = productActivity;
        util = new Utils(context.getApplicationContext());
        this.arrayData = arrayData;
        common_mypref = context.getSharedPreferences(
                "user", 0);
        cd = new CustomDialog(context);
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mTwoPane = mTwoPane;
        this.cat_posit = position;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_list_item_1, parent, false);
        SearchSubAdapter.ViewHolder vh = new SearchSubAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.title.setText(arrayData.get(position).title);
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int j = 0; j < context.cate_items.size(); j++) {
                    if(j==cat_posit) {
                        for (int i = 0; i < context.cate_items.get(j).subCategories.size(); i++) {
                            if (i == position) {
                                if (context.cate_items.get(j).subCategories.get(i).isSelected) {
                                    context.cate_items.get(j).subCategories.get(i).isSelected = false;
                                    context.fillProducts(null,false);
                                } else {
                                    arrayData.get(position).isSelected = true;
                                    context.fillProducts(context.cate_items.get(j).subCategories.get(i).id,false);
                                }
                            } else
                                context.cate_items.get(j).subCategories.get(i).isSelected = false;
                        }
                    }
                    else {
                        for (int i = 0; i < context.cate_items.get(j).subCategories.size(); i++) {
                            context.cate_items.get(j).subCategories.get(i).isSelected = false;
                        }
                    }
                }
                    context.cat_adapter.notifyDataSetChanged();

            }
        });
        if (arrayData.get(position).isSelected) {
            holder.title.setTextColor(context.getResources().getColor(R.color.ink_blue));
            context.txt_all.setTextColor(context.getResources().getColor(R.color.black));
        } else
            holder.title.setTextColor(context.getResources().getColor(R.color.dark_grey));

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
        TextView title;

        public ViewHolder(View rowView) {
            super(rowView);
            title = (TextView) rowView.findViewById(android.R.id.text1);

        }
    }


}
