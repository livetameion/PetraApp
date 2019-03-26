package com.pos.petra.app.AdvancedCashier.BrowseAndSearch;

import android.content.Intent;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.pos.petra.app.AdvancedCashier.DataKeeper;
import com.pos.petra.app.AdvancedCashier.Product.ProductDetailActivity;
import com.pos.petra.app.Model.Product.Products;
import com.pos.petra.app.R;

import java.util.ArrayList;



public class SearchProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    ProductFragment act;
    ArrayList<Products.Product> products;
    // Constructors ________________________________________________________________________________

    public SearchProductAdapter(ProductFragment srch, ArrayList<Products.Product>  products) {
        this.act =  srch;
        this.products = products;

    }

    // Callbacks ___________________________________________________________________________________
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_product_details, parent, false);
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
                        .error(R.mipmap.petra)
                        .into(viewHolder.img_product);
            }   catch (Exception e) {
                e.printStackTrace();
                Picasso.get()
                        .load(R.mipmap.petra)
                        .error(R.mipmap.petra)
                        .into(viewHolder.img_product);
            }


            viewHolder.header.setText(products.get(index).title);
            try {
                viewHolder.subHeader.setText("$" + String.format("%.2f", Double.parseDouble(products.get(index).price)));
            }catch (Exception e){
                e.printStackTrace();
            }

            viewHolder.rd_wrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(act.getActivity(), ProductDetailActivity.class);
                    try {
                        DataKeeper.detail_product = (Products.Product) products.get(index).clone();
                        act.startActivity(intent);
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }    

        viewHolder.cv_add.setVisibility(View.GONE);

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
        public ImageView cv_add;

        public ViewHolder (View v) {
            super (v);
            this.img_product =   v.findViewById(R.id.img_product);
            this.header =  v.findViewById(R.id.rd_header_text);
            this.subHeader =  v.findViewById(R.id.rd_sub_header_text);
            this.img_btn_edit =  v.findViewById(R.id.img_btn_edit);
            this.img_btn_delete =  v.findViewById(R.id.img_btn_add);
            this.rd_wrapper =  v.findViewById(R.id.ly_main);
            this.cb_chcek = v.findViewById(R.id.cb_chcek);
            this.cv_add = v.findViewById(R.id.cv_add);
            this.arrow =   v.findViewById(R.id.arrow);
        }
    }
}
