package com.yagna.petra.app.Product.Product;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yagna.petra.app.Model.Product.Products;
import com.yagna.petra.app.R;
import com.yagna.petra.app.Util.CustomDialog;
import com.yagna.petra.app.Util.Utils;

import java.util.ArrayList;

/**
 * Created by Sejal on 11-01-2017.
 */
public class AddProductListAdpter extends BaseAdapter {
    private final SharedPreferences common_mypref;
     final CustomDialog cd;
    private final Utils util;
    private final ArrayList<Products.Product> arrayData;
    ProductActivity context;
    private static LayoutInflater inflater=null;
    public AddProductListAdpter(ProductActivity productActivity, ArrayList<Products.Product> arrayData) {
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
        TextView p_name,p_price,p_description, p_stock,txt_titledescription,txt_titleprice,txt_titlepcode,txt_titlepname;
        ImageView p_img;
        Button edit,delete;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.b_product_item, null);
        holder.p_stock =(TextView) rowView.findViewById(R.id.p_stock);
        holder.p_img =(ImageView) rowView.findViewById(R.id.p_img);
        holder.p_name = (TextView) rowView.findViewById(R.id.product_name);
        holder.p_price = (TextView) rowView.findViewById(R.id.product_price);
        holder.p_description = (TextView) rowView.findViewById(R.id.product_desc);
        holder.edit=(Button) rowView.findViewById(R.id.edit_);
        holder.delete=(Button) rowView.findViewById(R.id.delete_);

        holder.txt_titledescription=(TextView) rowView.findViewById(R.id.txt_titledescription);
        holder.txt_titleprice=(TextView) rowView.findViewById(R.id.txt_titleprice);
        holder.txt_titlepcode=(TextView) rowView.findViewById(R.id.txt_titlepcode);
        holder.txt_titlepname=(TextView) rowView.findViewById(R.id.txt_titlepname);

      //  holder.p_img.setImageBitmap(imageData.get(position));
        if(arrayData.get(position).image.trim().length()!=0)
        {
            Picasso.get()
                    .load(arrayData.get(position).image.replace(" ","%20"))
                    .placeholder(R.mipmap.petra)
                    .error(R.mipmap.petra)
                    .into(holder.p_img );
        }
        else{
            Picasso.get()
                    .load(R.mipmap.petra)
                    .placeholder(R.mipmap.petra)
                    .error(R.mipmap.petra)
                    .into(holder.p_img );
        }
        holder.p_stock.setText(arrayData.get(position).stock);
        holder.p_name.setText(arrayData.get(position).title);
        holder.p_price.setText("\u20B9. "+arrayData.get(position).price);
        holder.p_description.setText(arrayData.get(position).description);
        holder.p_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.showimage(position);
            }
        });
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
                cd.showAlertmulti(false,"Do you want to delete "+holder.p_name.getText().toString()+" product ?");
                context.productid=arrayData.get(position).id;
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
