package com.yagna.petra.app.Admin.Wallet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yagna.petra.app.Admin.Users.UserDetailsActivity;
import com.yagna.petra.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sejal on 14-06-2017.
 */
public class WalletListViewAdapter extends BaseAdapter {
    private final SharedPreferences common_mypref;
    LayoutInflater inflater;
    Activity context;
    JSONArray jsonarray;
    JSONArray jsonarrayOne;
    public JSONObject jsonObject= null;
    private TextView txt_refundType;
    private TextView txt_cancel;
    private LinearLayout ly_printDelete;
    private TextView txt_voucherNo;
    private CheckBox cb_consolidate;

    public WalletListViewAdapter(Activity mainActivity, JSONArray jarry) {
        context = mainActivity;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        jsonarray = jarry;
        jsonarrayOne = jarry;
        common_mypref = context.getSharedPreferences("user", 0);
    }

    @Override
    public int getCount() {
        return jsonarray.length();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }



    public class Holder
    {
        TextView list_merchant,txt_date, list_industry, list_email,list_isactive;
        public TextView list_store_credit;
        LinearLayout ly_item;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {


        Holder holder=new Holder();
        View rowView;
        rowView=view;
        if(rowView==null)
        {
            rowView = inflater.inflate(R.layout.item_wallet_list, null);

        }
       /* holder.txt_date=(TextView) rowView.findViewById(R.id.list_date);
        try {
            holder.txt_date.setText(Utils.getConvetredDate(jsonarray.getJSONObject(position).getString("transaction_complete")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/
        holder.list_merchant =(TextView) rowView.findViewById(R.id.list_merchant);
        try {
            holder.list_merchant.setText(jsonarray.getJSONObject(position).getJSONObject("merchant").getString("dba"));
                   // (jsonarray.getJSONObject(position).isNull("name")?"":"(Merchant)"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.list_industry =(TextView) rowView.findViewById(R.id.list_industry);
        try {
            holder.list_industry.setText(jsonarray.getJSONObject(position).getJSONObject("merchant").getString("industry"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.list_store_credit = (TextView) rowView.findViewById(R.id.list_store_credit);
        try {
            holder.list_store_credit.setText(jsonarray.getJSONObject(position).getString("feathers"));
            LinearLayout la = (LinearLayout) rowView.findViewById(R.id.linearLayoutProdottoGenerale);
          //  la.setBackgroundResource( ((position % 2) == 0) ? R.color.grey : R.color.white);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        holder.ly_item = (LinearLayout) rowView.findViewById(R.id.ly_item);
        holder.ly_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        return rowView;
    }

}
