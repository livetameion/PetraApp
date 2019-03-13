package com.yagna.petra.app.Product.Category;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.yagna.petra.app.Model.Product.Categories;
import com.yagna.petra.app.Product.Category.CategoryActivity;
import com.yagna.petra.app.R;
import com.yagna.petra.app.Util.CustomDialog;
import com.yagna.petra.app.Util.Utils;

import java.util.ArrayList;

/**
 * Created by Sejal on 11-01-2017.
 */
public class CategoryListAdpter extends BaseAdapter {
    private final SharedPreferences common_mypref;
     final CustomDialog cd;
    private final Utils util;
    private final ArrayList<Categories.Category> arrayData;
    CategoryActivity context;
    private static LayoutInflater inflater=null;
    public CategoryListAdpter(CategoryActivity productActivity, ArrayList<Categories.Category> arrayData) {
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
    public int getCount() {
        return arrayData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class Holder{
        TextView p_name,p_price,p_description,p_code,txt_titledescription,txt_titleprice,txt_titlepcode,txt_titlepname;
        ImageView p_img;
        Button edit,delete;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.b_item_category, null);
        holder.p_name = (TextView) rowView.findViewById(R.id.product_name);
        holder.p_code = (TextView) rowView.findViewById(R.id.p_code);

        holder.p_description = (TextView) rowView.findViewById(R.id.product_desc);
        holder.edit=(Button) rowView.findViewById(R.id.edit_);
        holder.delete=(Button) rowView.findViewById(R.id.delete_);




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
        return rowView;
    }
}
