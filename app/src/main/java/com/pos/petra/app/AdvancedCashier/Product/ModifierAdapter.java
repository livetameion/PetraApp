package com.pos.petra.app.AdvancedCashier.Product;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.pos.petra.app.AdvancedCashier.DataKeeper;
import com.pos.petra.app.Model.Product.Modifier;
import com.pos.petra.app.R;
import com.pos.petra.app.Util.CustomDialog;
import com.pos.petra.app.Util.Utils;

import java.util.ArrayList;

public class ModifierAdapter extends RecyclerView.Adapter<ModifierAdapter.ViewHolder> {

    private final ArrayList<Modifier> arrayData;
    ProductDetailActivity context;

    public ModifierAdapter(ProductDetailActivity ModifierActivity, ArrayList<Modifier> arrayData) {
        context=ModifierActivity;
        this.arrayData=arrayData;

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

            img = rowView.findViewById(R.id.img);
            p_name =  rowView.findViewById(R.id.tile);



        }
    }





}
