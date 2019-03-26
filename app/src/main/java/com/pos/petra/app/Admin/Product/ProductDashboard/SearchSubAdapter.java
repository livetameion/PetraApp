package com.pos.petra.app.Admin.Product.ProductDashboard;

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
    private final SharedPreferences common_mypref;
    final CustomDialog cd;
    private final Utils util;
    private final ArrayList<SubCategories.SubCategory> arrayData;
    private final boolean mTwoPane;
    private final int cat_posit;
    ProductDashboardActivity context;
    private static LayoutInflater inflater = null;

    public SearchSubAdapter(ProductDashboardActivity productActivity, ArrayList<SubCategories.SubCategory> arrayData, boolean mTwoPane, int position) {
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
                    if(j==cat_posit) {
                        for (int i = 0; i < context.cate_items.get(j).subCategories.size(); i++) {
                            if (i == position) {
                                if (context.cate_items.get(j).subCategories.get(i).isSelected) {
                                    context.cate_items.get(j).subCategories.get(i).isSelected = false;
                                    //context.fillProducts(null,false);
                                    context.ProdsAndSubCategories(j);
                                    context.selected_subcate_id="";
                                    context.selected_sub_posit= "";

                                } else {
                                    arrayData.get(position).isSelected = true;
                                    context.fillProducts(context.cate_items.get(j).subCategories.get(i).id,false);
                                    context.selected_subcate_id=context.cate_items.get(j).subCategories.get(i).id;
                                    context.selected_sub_posit= ""+i;
                                }
                            } else
                            {
                                context.cate_items.get(j).subCategories.get(i).isSelected = false;
                            }
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
            holder.title.setTextColor(context.getResources().getColor(R.color.black));
            context.txt_all.setTextColor(context.getResources().getColor(R.color.black));
        } else
            holder.title.setTextColor(context.getResources().getColor(R.color.dark_grey));

        holder.btn_edit_subcateg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.showAddSubCategNewDialog(true,cat_posit,position);
            }
        });
        holder.btn_delete_subcateg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.position=position;
                context.cd.showAlertmulti(false,"Do you want to delete "+holder.title.getText().toString()+" Subcategory?");
                context.categ_id =arrayData.get(position).id;
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
        TextView title;
        ImageView btn_delete_subcateg,btn_edit_subcateg;
        public ViewHolder(View rowView) {
            super(rowView);
            title = (TextView) rowView.findViewById(android.R.id.text1);
            btn_edit_subcateg= rowView.findViewById(R.id.btn_edit_subcateg);
            btn_delete_subcateg = rowView.findViewById(R.id.btn_delete_subcateg);

        }
    }


}
