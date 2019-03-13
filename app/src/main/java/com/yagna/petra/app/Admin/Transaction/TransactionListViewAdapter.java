package com.yagna.petra.app.Admin.Transaction;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yagna.petra.app.R;
import com.yagna.petra.app.Util.Utils;
import com.yagna.petra.app.views.TypefacedTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

/**
 * Created by Sejal on 14-06-2017.
 */
public class TransactionListViewAdapter extends BaseAdapter {
    private final SharedPreferences common_mypref;
    LayoutInflater inflater;
    Activity context;
    JSONArray jsonarray;
    JSONArray jsonarrayOne;
    public JSONObject jsonObject= null;
    private com.yagna.petra.app.views.TypefacedTextView txt_refundType;
    private com.yagna.petra.app.views.TypefacedTextView txt_cancel;
    private LinearLayout ly_printDelete;
    private com.yagna.petra.app.views.TypefacedTextView txt_voucherNo;
    private CheckBox cb_consolidate;

    public TransactionListViewAdapter(Activity mainActivity, JSONArray jarry) {
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
        TypefacedTextView list_cust,txt_date, list_cashier, list_type;
        public TypefacedTextView list_price;
        LinearLayout ly_item;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {


        Holder holder=new Holder();
        View rowView;
        rowView=view;
        if(rowView==null)
        {
            rowView = inflater.inflate(R.layout.item_transaction_list, null);

        }
        holder.txt_date= rowView.findViewById(R.id.list_date);
        try {
            holder.txt_date.setText(Utils.getConvetredDate(jsonarray.getJSONObject(position).getString("transaction_complete")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.list_cust =rowView.findViewById(R.id.list_cust);
        try {
            if(!jsonarray.getJSONObject(position).isNull("name"))
                    holder.list_cust.setText(jsonarray.getJSONObject(position).getString("name")+
                    (jsonarray.getJSONObject(position).isNull("customer_merchant_id")?"":"(Merchant)"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.list_cashier = rowView.findViewById(R.id.list_action);
        try {
            holder.list_cashier.setText(jsonarray.getJSONObject(position).getString("cashier_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.list_price =  rowView.findViewById(R.id.list_price);
        DecimalFormat fc = new DecimalFormat("#,##,##,##,##0.00");
        try {
            holder.list_price.setText("$"+fc.format(Double.parseDouble(jsonarray.getJSONObject(position).getString("total_price"))));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        holder.list_type =  rowView.findViewById(R.id.list_type);
        try {
            holder.list_type.setText(jsonarray.getJSONObject(position).getString("pay_method"));
            LinearLayout la = (LinearLayout) rowView.findViewById(R.id.linearLayoutProdottoGenerale);
           // la.setBackgroundResource( ((position % 2) == 0) ? R.color.grey : R.color.white);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.ly_item = (LinearLayout) rowView.findViewById(R.id.ly_item);
        holder.ly_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    dialogFullTransaction(jsonarray.getJSONObject(position));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

              /*  Intent intent = new Intent(context,TransactionDetailsActivity.class);
                try {
                    intent.putExtra("data",""+jsonarray.getJSONObject(position));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
               context.startActivity(intent);*/
            }
        });
        return rowView;
    }


    //Dialog for shoiwng final total of transaction
    public void dialogFullTransaction(JSONObject transaction) {
        final Dialog finalCheckDialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.activity_transaction_detail, null);
        Rect displayRectangle = new Rect();
        Window window = context.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        finalCheckDialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        LinearLayout ly_pay = rowView.findViewById(R.id.ly_pay);
        ly_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalCheckDialog.dismiss();
            }
        });

        try {
            ((TextView)rowView.findViewById(R.id.txt_t_id)).setText(transaction.getString("transaction_id"));
            ((TextView)rowView.findViewById(R.id.txt_merchant)).setText(transaction.getString("name"));
            ((TextView)rowView.findViewById(R.id.txt_cashier)).setText(transaction.getString("cashier_name"));
            ((TextView)rowView.findViewById(R.id.txt_org_price)).setText("$"+transaction.getString("original_price"));
            ((TextView)rowView.findViewById(R.id.txt_subtotal)).setText("$"+transaction.getString("subtotal"));
            ((TextView)rowView.findViewById(R.id.txt_token_used)).setText(transaction.getString("tokens_used"));
            ((TextView)rowView.findViewById(R.id.txt_total_price)).setText("$"+(transaction.isNull("total_price")?"0.00":transaction.getString("total_price")));
            ((TextView)rowView.findViewById(R.id.txt_pay_method)).setText(transaction.getString("pay_method"));
            ((TextView)rowView.findViewById(R.id.txt_time)).setText(Utils.getConvetredDate(transaction.getString("transaction_complete")));
            String token_title = ""+transaction.getString("tokens_bought")+ " Store Credit @ $" + transaction.getString("token_rate");
            ((TextView)rowView.findViewById(R.id.title_token_bought)).setText(token_title);
            ((TextView)rowView.findViewById(R.id.txt_token_bought)).setText(transaction.getString("token_price"));
            String vptoken_title = ""+transaction.getString("vp_tokens")+ " Cheap Store Credit @ $" + String.format("%.2f",Float.parseFloat(transaction.getString("vp_rate")));
            ((TextView)rowView.findViewById(R.id.title_vptoken_bought)).setText(vptoken_title);
            ((TextView)rowView.findViewById(R.id.txt_vptoken_bought)).setText(transaction.getString("vp_fee"));

            ((TextView)rowView.findViewById(R.id.txt_charity)).setText(transaction.getString("charity_price"));


            (rowView.findViewById(R.id.ly_pay)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finalCheckDialog.dismiss();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

        finalCheckDialog.setContentView(rowView);
        finalCheckDialog.setCancelable(true);
        finalCheckDialog.setCanceledOnTouchOutside(true);
        finalCheckDialog.show();

    }





}
