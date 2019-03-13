package com.yagna.petra.app.AdvncedCashier.Search;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.squareup.picasso.Picasso;
import com.yagna.petra.app.AdvncedCashier.DataKeeper;
import com.yagna.petra.app.AdvncedCashier.Product.ProductDetailActivity;
import com.yagna.petra.app.Model.Product.Products;
import com.yagna.petra.app.R;
import com.yagna.petra.app.Util.Constants;

import org.json.JSONArray;

import java.util.ArrayList;


/**
 * Fills the result line with the passed cursor
 */
public class SearchProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    Activity act;

    ArrayList<Products.Product> products;
    // Constructors ________________________________________________________________________________

    public SearchProductAdapter(Activity srch,  ArrayList<Products.Product>  products) {

        this.act =  srch;
        this.products = products;

    }

        // Callbacks ___________________________________________________________________________________
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_searchable_row_details, parent, false);
        ViewHolder holder = new ViewHolder(view);
        // UI configuration
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
       {
           viewHolder.arrow.setVisibility(View.GONE);
           final int index = position;
           viewHolder.img_product.setVisibility(View.VISIBLE);
           try {
               Picasso.get()
                       .load(products.get(index).image_full_path)
                       // .placeholder(R.mipmap.petra)
                       .error(R.mipmap.petra)
                       .into(viewHolder.img_product);
           }   catch (Exception e) {
            e.printStackTrace();
               Picasso.get()
                       .load(R.mipmap.petra)
                       // .placeholder(R.mipmap.petra)
                       .error(R.mipmap.petra)
                       .into(viewHolder.img_product);
        }


           viewHolder.header.setText(products.get(index).title);
           viewHolder.subHeader.setText("$"+String.format("%.2f",Double.parseDouble(products.get(index).price)));
           viewHolder.cv_add.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   try {




                       SearchActivity.addTag(products.get(index));


                   } catch (Exception e) {
                       e.printStackTrace();
                   }
                   notifyDataSetChanged();
               }
           });
           //boolean wannaAdd= true;
                /*for(int i = 0; i< DataKeeper.product_id_selected.size(); i++){
                     if(DataKeeper.product_id_selected.get(i).equalsIgnoreCase(products.getJSONObject(index).getString("id"))){
                         wannaAdd = false;
                         break;
                     }
                 }
                if (wannaAdd)
                {
                    viewHolder.rd_wrapper.setVisibility(View.VISIBLE);
                }
                else
                {
                    viewHolder.rd_wrapper.setVisibility(View.GONE);
                }*/
           viewHolder.rd_wrapper.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent intent = new Intent(act,ProductDetailActivity.class);
                   try {
                       DataKeeper.detail_product = (Products.Product) products.get(index).clone();
                       act.startActivity(intent);
                   } catch (CloneNotSupportedException e) {
                       e.printStackTrace();
                   }


               }
           });
       }


/*
        viewHolder.img_btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataKeeper.old_tagsArray.remove(position);
                notifyDataSetChanged();
                act.saveSearchData();

            }
        });
        viewHolder.img_btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.editDialog(position);

            }
        });*/

    }

    // Getters and Setters _________________________________________________________________________
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public Products.Product getItem (Integer position) {

        return products.get(position);
    }

    // View Holder _________________________________________________________________________________
    private class ViewHolder extends RecyclerView.ViewHolder {

        private  LinearLayout rd_wrapper;
        public TextView header;
        public TextView subHeader;
        public ImageView img_btn_edit;
        public ImageView img_btn_delete;
        public ImageView img_product,arrow;
        public AppCompatCheckBox cb_chcek;
        public CardView cv_add;

        public ViewHolder (View v) {
            super (v);
            this.img_product =  (ImageView) v.findViewById(R.id.img_product);
            this.header = (TextView) v.findViewById(R.id.rd_header_text);
            this.subHeader = (TextView) v.findViewById(R.id.rd_sub_header_text);
            this.img_btn_edit = (ImageView) v.findViewById(R.id.img_btn_edit);
            this.img_btn_delete = (ImageView) v.findViewById(R.id.img_btn_add);
            this.rd_wrapper = (LinearLayout) v.findViewById(R.id.ly_main);
            this.cb_chcek = v.findViewById(R.id.cb_chcek);
            this.cv_add = v.findViewById(R.id.cv_add);
            this.arrow =   v.findViewById(R.id.arrow);
        }
    }
}
