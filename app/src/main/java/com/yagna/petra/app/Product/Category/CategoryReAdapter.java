package com.yagna.petra.app.Product.Category;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yagna.petra.app.Model.Product.Categories;
import com.yagna.petra.app.Model.Product.Modifier;
import com.yagna.petra.app.Product.Modifer.ModifierActivity;
import com.yagna.petra.app.R;
import com.yagna.petra.app.Util.CustomDialog;
import com.yagna.petra.app.Util.Utils;

import java.util.ArrayList;

public class CategoryReAdapter extends RecyclerView.Adapter<CategoryReAdapter.ViewHolder> {

    private final SharedPreferences common_mypref;
    final CustomDialog cd;
    private final Utils util;
    private final ArrayList<Categories.Category> arrayData;
    CategoryActivity context;
    private static LayoutInflater inflater=null;

    public CategoryReAdapter(CategoryActivity productActivity, ArrayList<Categories.Category> arrayData) {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.b_item_category, parent, false);
        CategoryReAdapter.ViewHolder vh = new CategoryReAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.p_name.setText(arrayData.get(position).title);
        holder.p_code.setText(arrayData.get(position).code);

        holder.p_description.setText(arrayData.get(position).description);


        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.addData(true,position);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.position=position;

                cd.showAlertmulti(false,"Do you want to delete "+holder.p_name.getText().toString()+" category?");
                context.categ_id =arrayData.get(position).id;

                notifyDataSetChanged();


                if(arrayData.size()==0){
                    context.listview.setVisibility(View.GONE);
                    context.tv_no_record.setVisibility(View.VISIBLE);
                }
                else {
                    context.listview.setVisibility(View.VISIBLE);
                    context.tv_no_record.setVisibility(View.GONE);
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
        TextView p_name,p_price,p_description,p_code,txt_titledescription,txt_titleprice,txt_titlepcode,txt_titlepname;
        ImageView p_img;
        Button edit,delete;

        public ViewHolder(View rowView) {
            super(rowView);
            p_name = (TextView) rowView.findViewById(R.id.product_name);
            p_code = (TextView) rowView.findViewById(R.id.p_code);

            p_description = (TextView) rowView.findViewById(R.id.product_desc);
            edit=(Button) rowView.findViewById(R.id.edit_);
            delete=(Button) rowView.findViewById(R.id.delete_);

        }
    }





}
