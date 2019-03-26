package com.pos.petra.app.Admin.Transaction;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pos.petra.app.R;
import com.pos.petra.app.Util.Utils;
import com.pos.petra.app.Views.TypefacedTextView;

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
    JSONArray dataArray;
    JSONArray dataArrayBackup;
    public JSONObject jsonObject = null;
    private ProductAdapter adapter;


    public TransactionListViewAdapter(Activity mainActivity, JSONArray jarry) {
        context = mainActivity;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dataArray = jarry;
        dataArrayBackup = jarry;
        common_mypref = context.getSharedPreferences("user", 0);
    }

    @Override
    public int getCount() {
        return dataArray.length();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    public class Holder {
        TypefacedTextView list_cust, txt_date, list_cashier, list_type;
        public TypefacedTextView list_price;
        LinearLayout ly_item;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        Holder holder = new Holder();
        View rowView;
        rowView = view;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.item_transaction_list, null);

        }
        holder.txt_date = rowView.findViewById(R.id.list_date);
        try {
            holder.txt_date.setText(Utils.getConvetredDate(dataArray.getJSONObject(position).getString("transaction_complete")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.list_cust = rowView.findViewById(R.id.list_cust);
        try {
            if (!dataArray.getJSONObject(position).isNull("name"))
                holder.list_cust.setText(dataArray.getJSONObject(position).getString("name") +
                        (dataArray.getJSONObject(position).isNull("customer_merchant_id") ? "" : "(Merchant)"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.list_cashier = rowView.findViewById(R.id.list_action);
        try {
            holder.list_cashier.setText(dataArray.getJSONObject(position).getString("cashier_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.list_price = rowView.findViewById(R.id.list_price);
        DecimalFormat fc = new DecimalFormat("#,##,##,##,##0.00");
        try {
            holder.list_price.setText("$" + fc.format(Double.parseDouble(dataArray.getJSONObject(position).getString("total_price"))));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        holder.list_type = rowView.findViewById(R.id.list_type);
        try {
            holder.list_type.setText(dataArray.getJSONObject(position).getString("pay_method"));
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
                    dialogFullTransaction(dataArray.getJSONObject(position));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        return rowView;
    }


    //Dialog for shoiwng details of transaction
    public void dialogFullTransaction(JSONObject transaction) {
        final Dialog transactionViewDialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.activity_transaction_detail, null);
        Rect displayRectangle = new Rect();
        Window window = context.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        transactionViewDialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        LinearLayout ly_pay = rowView.findViewById(R.id.ly_pay);
        ly_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transactionViewDialog.dismiss();
            }
        });

        RecyclerView list_products = (RecyclerView) rowView.findViewById(R.id.prod_list);


        try {
            ((TextView) rowView.findViewById(R.id.txt_t_id)).setText(transaction.getString("transaction_id"));
            ((TextView) rowView.findViewById(R.id.txt_merchant)).setText(transaction.getString("name"));
            ((TextView) rowView.findViewById(R.id.txt_cashier)).setText(transaction.getString("cashier_name"));
            ((TextView) rowView.findViewById(R.id.txt_org_price)).setText("$" + transaction.getString("original_price"));
            ((TextView) rowView.findViewById(R.id.txt_subtotal)).setText("$" + transaction.getString("subtotal"));
            ((TextView) rowView.findViewById(R.id.txt_token_used)).setText(transaction.getString("tokens_used"));
            ((TextView) rowView.findViewById(R.id.txt_total_price)).setText("$" + (transaction.isNull("total_price") ? "0.00" : transaction.getString("total_price")));
            ((TextView) rowView.findViewById(R.id.txt_pay_method)).setText(transaction.getString("pay_method"));
            ((TextView) rowView.findViewById(R.id.txt_time)).setText(Utils.getConvetredDate(transaction.getString("transaction_complete")));
            String token_title = "" + transaction.getString("tokens_bought") + " Store Credit @ $" + transaction.getString("token_rate");
            ((TextView) rowView.findViewById(R.id.title_token_bought)).setText(token_title);
            ((TextView) rowView.findViewById(R.id.txt_token_bought)).setText(transaction.getString("token_price"));
            String vptoken_title = "" + transaction.getString("vp_tokens") + " Cheap Store Credit @ $" + String.format("%.2f", Float.parseFloat(transaction.getString("vp_rate")));
            ((TextView) rowView.findViewById(R.id.title_vptoken_bought)).setText(vptoken_title);
            ((TextView) rowView.findViewById(R.id.txt_vptoken_bought)).setText(transaction.getString("vp_fee"));

            ((TextView) rowView.findViewById(R.id.txt_charity)).setText(transaction.getString("charity_price"));
             (rowView.findViewById(R.id.ly_pay)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    transactionViewDialog.dismiss();
                }
            });

            StaggeredGridLayoutManager recyleLayoutManager = new StaggeredGridLayoutManager(1, RecyclerView.VERTICAL);
            list_products.setLayoutManager(recyleLayoutManager);

            JSONArray jaar = new JSONArray(transaction.getString("products"));
            if (jaar.length() > 0) {

                if (adapter == null) {
                    list_products.setAdapter(adapter = new ProductAdapter(context, jaar));
                } else {
                    adapter.notifyDataSetChanged();
                }
                list_products.setVisibility(View.VISIBLE);

            } else {

                list_products.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        transactionViewDialog.setContentView(rowView);
        transactionViewDialog.setCancelable(true);
        transactionViewDialog.setCanceledOnTouchOutside(true);
        transactionViewDialog.show();

    }


}
