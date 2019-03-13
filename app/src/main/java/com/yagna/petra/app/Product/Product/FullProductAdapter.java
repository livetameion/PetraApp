package com.yagna.petra.app.Product.Product;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yagna.petra.app.R;
import com.yagna.petra.app.Util.Utils;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Sejal on 11-04-2017.
 */
public class FullProductAdapter extends PagerAdapter {
    private Context _activity;
    //private ArrayList<String> _imagePaths;
    JSONArray _imageArray;
    private LayoutInflater inflater;
    private final Utils util;

    public FullProductAdapter(Context context, JSONArray imagearray1) {
        this._activity = context;
        this._imageArray = imagearray1;
        util = new Utils(this._activity);

    }

    @Override
    public int getCount() {
        return _imageArray.length();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imgDisplay;
        Button btnClose;

        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.db_product_item, container,
                false);

        TextView product_name = (TextView) viewLayout.findViewById(R.id.p_name);
        TextView p_price =(TextView) viewLayout.findViewById(R.id.p_price);
        TextView p_code =(TextView) viewLayout.findViewById(R.id.p_stock);
        TextView p_description =(TextView) viewLayout.findViewById(R.id.product_desc);
        TextView txt_p_name = (TextView) viewLayout.findViewById(R.id.txt_p_nam);
        TextView txt_p_price =(TextView) viewLayout.findViewById(R.id.txt_p_price);
        TextView txt_p_code =(TextView) viewLayout.findViewById(R.id.txt_p_code);
        TextView txt_p_description =(TextView) viewLayout.findViewById(R.id.txt_p_description);
        ImageView p_image = (ImageView) viewLayout.findViewById(R.id.p_img);
        p_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showimage();
            }
        });



        try {
            product_name.setText(_imageArray.getJSONObject(position).getString("ProductName"));
            p_code.setText(_imageArray.getJSONObject(position).getString("ProductCode"));
            p_description.setText(_imageArray.getJSONObject(position).getString("ProductDescription"));
            p_price.setText("\u20B9. "+_imageArray.getJSONObject(position).getString("ProductPrice"));
            Picasso.get()
                    .load(_imageArray.getJSONObject(position).getJSONArray("ImageList").getJSONObject(0).getString("ImagePath").replace(" ","%20"))
                    .placeholder(R.mipmap.img_crash)
                    .error(R.mipmap.img_crash)
                    .into(p_image);

        } catch (JSONException e) {
            e.printStackTrace();
        }




        Button back = (Button) viewLayout.findViewById(R.id.delete_);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        container.addView(viewLayout);

        return viewLayout;
    }

    private void showimage() {
        final Dialog Dialog = new Dialog(_activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        LayoutInflater inflater = LayoutInflater.from(_activity);
        View rowView = (View) inflater.inflate(R.layout.p_viewpager_full, null);
        Rect displayRectangle = new Rect();
        ViewPager viewPager2 = (ViewPager)rowView.findViewById(R.id.vp_catlog);
        FullImageProductAdapter mAdapter = new FullImageProductAdapter(_activity,_imageArray);
        viewPager2.setAdapter(mAdapter);
        Button backfullimg = (Button) rowView.findViewById(R.id.btn_close);
        backfullimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 /*   _activity.finish();*/
                Dialog.dismiss();
            }
        });
        Dialog.setContentView(rowView);
        Dialog.setCancelable(true);
        Dialog.setCanceledOnTouchOutside(true);
        Dialog.show();

    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}
