package com.yagna.petra.app.Product.Product;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.yagna.petra.app.R;
import com.yagna.petra.app.Util.P_SquareImageView;
import com.yagna.petra.app.Util.Utils;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Sejal on 17-04-2017.
 */

public class ProductGridViewAdapter extends BaseAdapter {
    //Context mcontext;
    Integer image1;
    JSONArray imageArray;
    P_Product_Seemore mcontext;

    private final Utils util;
    public ProductGridViewAdapter(Context context, JSONArray imagearray) {
        mcontext = (P_Product_Seemore) context;
        imageArray = imagearray;
        util = new Utils(mcontext);

    }

    @Override
    public int getCount() {
        return imageArray.length();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ImageView imageView;
        TextView textView;
        ProductGridViewAdapter.ViewHolder holder;
        View view = convertView;
        if (convertView == null) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.p_product_gridview, viewGroup, false);
            holder = new ProductGridViewAdapter.ViewHolder();
            holder.text_name = (TextView) view.findViewById(R.id.txt_name);

            holder.text_price = (TextView) view.findViewById(R.id.txt_price);

            holder.image = (P_SquareImageView) view.findViewById(R.id.banner_img);
            try {
                Picasso.get()
                        .load(imageArray.getJSONObject(i).getJSONArray("ImageList").getJSONObject(0).getString("ImagePath").replace(" ","%20"))
                        .placeholder(R.mipmap.img_crash)
                        .error(R.mipmap.img_crash)
                        .into(holder.image);
                holder.text_name.setText(imageArray.getJSONObject(i).getString("ProductName"));
                holder.text_price.setText("\u20B9. "+imageArray.getJSONObject(i).getString("ProductPrice"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mcontext.showimage();
                }
            });

               /* holder.text.setText(nameArray[position]);*/
            view.setTag(holder);

        }

        return view;
    }void setupdateadapter(JSONArray imagearray)
    {
        imageArray = imagearray;
        notifyDataSetChanged();
    }

    public class ViewHolder {
        public P_SquareImageView image;
        public TextView text_name;
        public TextView text_price;
        public LinearLayout item;
    }
}