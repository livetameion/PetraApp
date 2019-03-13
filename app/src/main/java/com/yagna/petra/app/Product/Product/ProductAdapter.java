package com.yagna.petra.app.Product.Product;

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

import com.squareup.picasso.Picasso;
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
    ProductActivity context;
    private static LayoutInflater inflater=null;

    public ProductAdapter(ProductActivity productActivity, ArrayList<Products.Product> arrayData) {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.b_product_item, parent, false);
        ProductAdapter.ViewHolder vh = new ProductAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(arrayData.get(position).image_full_path.trim().length()!=0)
        {
            Picasso.get()
                    .load(arrayData.get(position).image_full_path.replace(" ","%20"))
                   // .placeholder(R.mipmap.petra)
                  //  .error(R.mipmap.petra)
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
        holder.product_sku.setText(arrayData.get(position).sku);
        holder.p_name.setText(arrayData.get(position).title);
        holder.p_price.setText("$ "+arrayData.get(position).price);
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
                cd.showAlertmulti(false,"Do you want to delete \""+holder.p_name.getText().toString()+"\" Product?");
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

        holder.discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DiscountActivity.class);
                i.putExtra("id",""+(position));
                context.startActivity(i);
            }
        });

        holder.modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ModifierActivity.class);
                i.putExtra("id",""+(position));
                context.startActivity(i);
            }
        });
        holder.txt_add_modif.setText(" "+(arrayData.get(position).modifier.size()==0?"(0)":"("+arrayData.get(position).modifier.size()+")"));
        holder.txt_add_discount.setText(" "+(arrayData.get(position).discount.size()==0?"(0)":"("+arrayData.get(position).discount.size()+")"));


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
        LinearLayout discount,modifier;
        Button edit,delete;

        public ViewHolder(View rowView) {
            super(rowView);
            p_stock =(TextView) rowView.findViewById(R.id.p_stock);
            p_img =(ImageView) rowView.findViewById(R.id.p_img);
            p_name = (TextView) rowView.findViewById(R.id.product_name);
            p_price = (TextView) rowView.findViewById(R.id.product_price);
            p_description = (TextView) rowView.findViewById(R.id.product_desc);
            edit=(Button) rowView.findViewById(R.id.edit_);
            delete=(Button) rowView.findViewById(R.id.delete_);
            discount=(LinearLayout) rowView.findViewById(R.id.discount);
            modifier=(LinearLayout) rowView.findViewById(R.id.modifier);
            product_sku= (TextView) rowView.findViewById(R.id.product_sku);
            txt_add_modif =rowView.findViewById(R.id.txt_add_modif);
            txt_add_discount =rowView.findViewById(R.id.txt_add_discount);



        }
    }





}
