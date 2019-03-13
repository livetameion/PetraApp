package com.yagna.petra.app.AdvncedCashier.Product;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yagna.petra.app.AdvncedCashier.DataKeeper;
import com.yagna.petra.app.AdvncedCashier.Modifer.ModifierActivity;
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
    ProductDetailActivity context;
    private static LayoutInflater inflater=null;

    public ModifierAdapter(ProductDetailActivity ModifierActivity, ArrayList<Modifier> arrayData) {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dlg_modif, parent, false);
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
                    .into(holder.img );
        }
        else {
            Picasso.get()
                    .load(R.mipmap.petra)
                    .placeholder(R.mipmap.petra)
                    .error(R.mipmap.petra)
                    .into(holder.img );
        }

        holder.p_name.setText(arrayData.get(position).title);

        holder.ly_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataKeeper.detail_product.selected_modifier=""+position;
                context.fillDetail();
                context.dialogModifer.dismiss();
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
        TextView p_name;
        ImageView img;
        LinearLayout ly_main;

        public ViewHolder(View rowView) {
            super(rowView);
            ly_main = rowView.findViewById(R.id.ly_main);

            img =(ImageView) rowView.findViewById(R.id.img);
            p_name = (TextView) rowView.findViewById(R.id.tile);



        }
    }





}
