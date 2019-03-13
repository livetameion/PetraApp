package com.yagna.petra.app.Product.Product;

import android.content.Context;
import android.graphics.Typeface;
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
import com.yagna.petra.app.Util.TouchImageView;
import com.yagna.petra.app.Util.Utils;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Sejal on 11-04-2017.
 */
public class FullImageProductAdapter extends PagerAdapter {
    private Context _activity;
    //private ArrayList<String> _imagePaths;
    JSONArray _imageArray;

    private LayoutInflater inflater;
    private final Utils util;
    public FullImageProductAdapter(Context activity, JSONArray imageArray) {
        this._activity = activity;
        this._imageArray = imageArray;
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
        View viewLayout = inflater.inflate(R.layout.p_product_fullbanner, container,
                false);

        TouchImageView imgview = (TouchImageView)viewLayout.findViewById(R.id.image_detail);
        TextView product_name=(TextView)viewLayout.findViewById(R.id.product_name) ;
        TextView product_price = (TextView) viewLayout.findViewById(R.id.product_price);
        TextView product_desc = (TextView) viewLayout.findViewById(R.id.product_desc);


      /*      BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;*/
        try {

            product_name.setText(_imageArray.getJSONObject(position).getString("ProductName"));
            product_desc.setText(_imageArray.getJSONObject(position).getString("ProductDescription"));
            product_price.setText("\u20B9. "+_imageArray.getJSONObject(position).getString("ProductPrice"));
            Picasso.get()
                    .load(_imageArray.getJSONObject(position).getJSONArray("ImageList").getJSONObject(0).getString("ImagePath").replace(" ","%20"))
                    .placeholder(R.mipmap.product_img)
                    .error(R.mipmap.img_crash)
                    .into(imgview);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        imgview.setMaxZoom(4f);



        // close button click even

        container.addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}
