package com.yagna.petra.app.Product;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yagna.petra.app.Product.Category.CategoryActivity;
import com.yagna.petra.app.Product.Product.ProductActivity;
import com.yagna.petra.app.Product.SubCategory.SubCategoryActivity;
import com.yagna.petra.app.R;
import com.yagna.petra.app.Util.CustomDialog;
import com.yagna.petra.app.Util.Utils;

import java.util.ArrayList;

/**
 * Created by Sejal on 11-01-2017.
 */
public class ActionListAdpter extends BaseAdapter {

     final CustomDialog cd;
    private final Utils util;
    private final ArrayList<String> arrayData;

    ActionListActivity context;
    private static LayoutInflater inflater=null;
    public ActionListAdpter(ActionListActivity productActivity, ArrayList<String> arrayData) {
        context=productActivity;
        util=new Utils(context.getApplicationContext());
        this.arrayData=arrayData;
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
        return arrayData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class Holder{
        TextView id_text;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.item_actionlist, null);
        holder.id_text = (TextView) rowView.findViewById(R.id.id_text);

        holder.id_text.setText(arrayData.get(position));




        holder.id_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.ButtonClickEffect(v);
                Intent i ;
              switch (position){
                  case 0 :
                      i = new Intent(context,CategoryActivity.class);
                      context.startActivity(i);
                      break;
                  case 1 :
                      i = new Intent(context,SubCategoryActivity.class);
                      context.startActivity(i);
                      break;
                  case 2 :
                      i = new Intent(context,ProductActivity.class);
                      context.startActivity(i);
                      break;
                  case 3 :
                      break;
              }

            }
        });
        return rowView;
    }
}
