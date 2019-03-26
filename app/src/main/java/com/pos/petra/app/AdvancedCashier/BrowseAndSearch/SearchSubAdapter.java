package com.pos.petra.app.AdvancedCashier.BrowseAndSearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pos.petra.app.Model.Product.SubCategories;
import com.pos.petra.app.R;
import com.pos.petra.app.Util.CustomDialog;
import com.pos.petra.app.Util.Utils;

import java.util.ArrayList;

public class SearchSubAdapter extends RecyclerView.Adapter<SearchSubAdapter.ViewHolder> {
    final CustomDialog cd;
    private final ArrayList<SubCategories.SubCategory> arrayData;
    private final int cat_posit;
    SearchActivity context;

    public SearchSubAdapter(SearchActivity productActivity, ArrayList<SubCategories.SubCategory> arrayData, boolean mTwoPane, int position) {
        context = productActivity;
        this.arrayData = arrayData;       
        cd = new CustomDialog(context);       
        this.cat_posit = position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_categ, parent, false);
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
                    if (j == cat_posit) {
                        for (int i = 0; i < context.cate_items.get(j).subCategories.size(); i++) {
                            if (i == position) {
                                if (context.cate_items.get(j).subCategories.get(i).isSelected) {
                                    context.cate_items.get(j).subCategories.get(i).isSelected = false;
                                    //context.fillProducts(null,false);
                                    context.ProdsAndSubCategories(position);
                                    context.selected_subcate_id = "";

                                } else {
                                    arrayData.get(position).isSelected = true;
                                    context.fillProducts(context.cate_items.get(j).subCategories.get(i).id, false);
                                    context.selected_subcate_id = context.cate_items.get(j).subCategories.get(i).id;
                                }
                            } else
                                context.cate_items.get(j).subCategories.get(i).isSelected = false;
                        }
                    } else {
                        for (int i = 0; i < context.cate_items.get(j).subCategories.size(); i++) {
                            context.cate_items.get(j).subCategories.get(i).isSelected = false;
                        }
                    }
                }
                context.cat_adapter.notifyDataSetChanged();

            }
        });
        if (arrayData.get(position).isSelected) {
            holder.title.setTextColor(context.getResources().getColor(R.color.black));
            context.txt_all.setTextColor(context.getResources().getColor(R.color.black));
        } else
            holder.title.setTextColor(context.getResources().getColor(R.color.dark_grey));

        holder.btn_edit_subcateg.setVisibility(View.GONE);

        holder.btn_delete_subcateg.setVisibility(View.GONE);

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
        ImageView btn_delete_subcateg, btn_edit_subcateg;

        public ViewHolder(View rowView) {
            super(rowView);
            title = rowView.findViewById(android.R.id.text1);
            btn_edit_subcateg = rowView.findViewById(R.id.btn_edit_subcateg);
            btn_delete_subcateg = rowView.findViewById(R.id.btn_delete_subcateg);

        }
    }


}
