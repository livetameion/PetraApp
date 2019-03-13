package com.yagna.petra.app.Product.SubCategory;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yagna.petra.app.Model.Product.SubCategories;
import com.yagna.petra.app.R;
import com.yagna.petra.app.Util.CustomDialog;
import com.yagna.petra.app.Util.Utils;

import java.util.ArrayList;

/**
 * Created by Sejal on 11-01-2017.
 */
public class SubCategoryListAdpter extends BaseAdapter {
    private final SharedPreferences common_mypref;
     final CustomDialog cd;
    private final Utils util;
    private final ArrayList<SubCategories.SubCategory> arrayData;
    SubCategoryActivity context;
    private static LayoutInflater inflater=null;
    public SubCategoryListAdpter(SubCategoryActivity productActivity, ArrayList<SubCategories.SubCategory> arrayData) {
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
        TextView p_name,p_price,p_description,sub_categ_name,p_code,txt_titledescription,txt_titleprice,txt_titlepcode,txt_titlepname;
        ImageView p_img;
        LinearLayout ly_sub_categ, ly_categ_view;

        Button edit,delete;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.b_item_category, null);
        holder.p_name = (TextView) rowView.findViewById(R.id.product_name);
        holder.p_description = (TextView) rowView.findViewById(R.id.product_desc);
        holder.edit=(Button) rowView.findViewById(R.id.edit_);
        holder.delete=(Button) rowView.findViewById(R.id.delete_);
        holder.p_code=(TextView) rowView.findViewById(R.id.p_code);

        holder.p_code.setText(arrayData.get(position).code);
        holder.p_name.setText(arrayData.get(position).title);
        holder.p_description.setText(arrayData.get(position).description);
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.addData(true,position);
            }
        });
        holder.ly_sub_categ = (LinearLayout) rowView.findViewById(R.id.ly_sub_categ);
        holder.ly_sub_categ.setVisibility(View.VISIBLE);
        holder.ly_categ_view = (LinearLayout) rowView.findViewById(R.id.ly_categ_view);
        holder.ly_categ_view.setVisibility(View.GONE);
        holder.sub_categ_name = (TextView) rowView.findViewById(R.id.sub_categ_name);
        holder.sub_categ_name.setText(arrayData.get(position).title);



        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.position=position;
                cd.showAlertmulti(false,"Do you want to delete "+holder.p_name.getText().toString()+" SubCategory?");
                context.subcateg_id =arrayData.get(position).id;
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
