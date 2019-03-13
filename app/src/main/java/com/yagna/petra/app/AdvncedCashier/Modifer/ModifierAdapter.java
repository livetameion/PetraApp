package com.yagna.petra.app.AdvncedCashier.Modifer;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yagna.petra.app.AdvncedCashier.DataKeeper;
import com.yagna.petra.app.Model.Product.Modifier;
import com.yagna.petra.app.Model.Product.Products;
import com.yagna.petra.app.R;
import com.yagna.petra.app.Util.CustomDialog;
import com.yagna.petra.app.Util.Utils;

import java.util.ArrayList;

public class ModifierAdapter extends RecyclerView.Adapter<ModifierAdapter.ViewHolder> {

    private final SharedPreferences common_mypref;
    final CustomDialog cd;
    private final Utils util;
    private final ArrayList<Modifier> arrayData;
    ModifierActivity context;
    private static LayoutInflater inflater=null;

    public ModifierAdapter(ModifierActivity ModifierActivity, ArrayList<Modifier> arrayData) {
        context=ModifierActivity;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.d_modifier_item, parent, false);
        ModifierAdapter.ViewHolder vh = new ModifierAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(arrayData.get(position).image_full_path!=null&&arrayData.get(position).image_full_path.trim().length()!=0)
        {
            Picasso.get()
                    .load(arrayData.get(position).image_full_path.replace(" ","%20"))
                    .placeholder(R.mipmap.petra)
                    .error(R.mipmap.petra)
                    .into(holder.p_image_full_path );
        }
        else {
            Picasso.get()
                    .load(R.mipmap.petra)
                    .placeholder(R.mipmap.petra)
                    .error(R.mipmap.petra)
                    .into(holder.p_image_full_path );
        }
        holder.p_stock.setText(arrayData.get(position).stock);

        holder.p_name.setText(arrayData.get(position).title);
        holder.p_price.setText("$ "+arrayData.get(position).price);
        holder.p_description.setText(arrayData.get(position).description);
        holder.p_image_full_path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.showimage(position);
            }
        });
        holder.add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Products.Product p = null;
                try {
                    p = (Products.Product) DataKeeper.products_array.get(context.posit_id).clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                p.selected_modifier=""+position;
                DataKeeper.product_id_selected.add(0,DataKeeper.products_array.get(context.posit_id).id);
                DataKeeper.product_name_selected.add(0,DataKeeper.products_array.get(context.posit_id).title);
                DataKeeper.products_array.add(0,p);
                context.finish();
            }
        });
        holder.apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataKeeper.products_array.get(context.posit_id).selected_modifier=""+position;
                context.finish();
            }
        });
        holder.modifier_type.setText(arrayData.get(position).type);

        holder.ly_categ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataKeeper.products_array.get(context.posit_id).selected_modifier=""+position;
                context.finish();
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
        CardView ly_categ;
        TextView p_name,p_price,p_description, modifier_type, txt_title_code,p_stock,txt_titledescription,txt_titleprice, txt_titlepcode, txt_titlepname;
        ImageView p_image_full_path;
        Button add_new, apply;

        public ViewHolder(View rowView) {
            super(rowView);
            p_stock =(TextView) rowView.findViewById(R.id.p_stock);
            p_image_full_path =(ImageView) rowView.findViewById(R.id.p_img);
            p_name = (TextView) rowView.findViewById(R.id.product_name);
            p_price = (TextView) rowView.findViewById(R.id.product_price);
            p_description = (TextView) rowView.findViewById(R.id.product_desc);
            add_new =(Button) rowView.findViewById(R.id.edit_);
            apply =(Button) rowView.findViewById(R.id.delete_);
            modifier_type = (TextView) rowView.findViewById(R.id.product_sku);
            ly_categ= (CardView) rowView.findViewById(R.id.ly_categ);


        }
    }





}