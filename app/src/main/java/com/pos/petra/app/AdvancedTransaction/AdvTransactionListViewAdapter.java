package com.pos.petra.app.AdvancedTransaction;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pos.petra.app.Model.Transaction.AdvTransactions;
import com.pos.petra.app.R;
import com.pos.petra.app.Util.Constants;
import com.pos.petra.app.Util.Utils;
import com.pos.petra.app.Views.TypefacedTextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Sejal on 14-06-2017.
 */
public class AdvTransactionListViewAdapter extends BaseAdapter implements Filterable {
    private final SharedPreferences common_mypref;
    LayoutInflater inflater;
    AdvTransactionListActivity context;
    ArrayList<AdvTransactions.AdvTransaction>  jsonarray;
    ArrayList<AdvTransactions.AdvTransaction>  jsonarrayOne;


    public AdvTransactionListViewAdapter(AdvTransactionListActivity mainActivity, ArrayList<AdvTransactions.AdvTransaction> jarry) {
        context = mainActivity;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        jsonarray = jarry;
        jsonarrayOne = jarry;
        common_mypref = context.getSharedPreferences("user", 0);
    }

    @Override
    public int getCount() {
        return jsonarray.size();
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
        TypefacedTextView list_tid,txt_date, list_action, list_type;
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
            rowView = inflater.inflate(R.layout.item_retrun_transaction_list, null);

        }
        holder.txt_date=(TypefacedTextView) rowView.findViewById(R.id.list_date);
        try {
            holder.txt_date.setText(Utils.getConvetredDate(jsonarray.get(position).transaction_complete));
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.list_tid =(TypefacedTextView) rowView.findViewById(R.id.list_tid);
        try {
                    holder.list_tid.setText(jsonarray.get(position).transaction_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.list_action =(TypefacedTextView) rowView.findViewById(R.id.list_action);
        try {
            String paymethod = jsonarray.get(position).transaction_status;
            if(paymethod.equalsIgnoreCase("3"))
            {
                if(jsonarray.get(position).isRefundPossible){
                    holder.list_action.setBackgroundColor(context.getResources().getColor(R.color.light_black));
                    holder.list_action.setTextColor(context.getResources().getColor(R.color.black));
                    holder.list_action.setText("Refund");

                }
                else{
                    holder.list_action.setBackgroundColor(context.getResources().getColor(R.color.grey));
                    holder.list_action.setTextColor(context.getResources().getColor(R.color.black));
                    holder.list_action.setText("Refunded");
                }

              //  holder.list_action.setEnabled(false);
             //   holder.list_action.setClickable(false);
                holder.list_action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            context.checkCustomerOrMerchant(jsonarray.get(position));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
           else if(paymethod.equalsIgnoreCase("2"))
            {
                holder.list_action.setBackgroundColor(context.getResources().getColor(R.color.light_black));
                holder.list_action.setTextColor(context.getResources().getColor(R.color.black));
                holder.list_action.setEnabled(false);
                holder.list_action.setClickable(false);
                holder.list_action.setText("Voided");

            }
            else if (paymethod.equalsIgnoreCase("1")){
                holder.list_action.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                holder.list_action.setTextColor(context.getResources().getColor(R.color.white));
                holder.list_action.setEnabled(true);
                holder.list_action.setClickable(true);
                boolean timecheck =(Utils.checkNextday(jsonarray.get(position).transaction_complete));
                if((timecheck&&jsonarray.get(position).pay_method.equalsIgnoreCase(Constants.PAYMETHOD_CARD))||jsonarray.get(position).pay_method.equalsIgnoreCase(Constants.PAYMETHOD_CASH)||jsonarray.get(position).pay_method.equalsIgnoreCase(Constants.PAYMETHOD_EXCHANGE))
                {
                    holder.list_action.setText("Refund");
                    holder.list_action.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                context.checkCustomerOrMerchant(jsonarray.get(position));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                else
                {
                    holder.list_action.setText("Void");
                    holder.list_action.setBackgroundColor(context.getResources().getColor(R.color.green));
                    holder.list_action.setTextColor(context.getResources().getColor(R.color.white));
                    holder.list_action.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {

                                context.askForVoidPermission( jsonarray.get(position));


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }


            }


        } catch (Exception e) {
            e.printStackTrace();
            holder.list_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        context.util.customToast("Transaction seems corrupted.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        holder.list_price = (TypefacedTextView) rowView.findViewById(R.id.list_price);
        DecimalFormat fc = new DecimalFormat("#,##,##,##,##0.00");
        try {
            holder.list_price.setText("$"+fc.format(Double.parseDouble(jsonarray.get(position).total_price)));
        } catch (Exception e) {
            e.printStackTrace();

        }


        holder.list_type = (TypefacedTextView) rowView.findViewById(R.id.list_type);
        try {
            holder.list_type.setText(jsonarray.get(position).pay_method);
            LinearLayout la = (LinearLayout) rowView.findViewById(R.id.linearLayoutProdottoGenerale);
         //   la.setBackgroundResource( ((position % 2) == 0) ? R.color.grey : R.color.white);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.ly_item = (LinearLayout) rowView.findViewById(R.id.ly_item);
        holder.ly_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,TransactionDetailsActivity.class);
                try {
                    AdvTransactionListActivity.click_transaction=jsonarray.get(position);
                    intent.putExtra("data",""+(position));
                    dialogFullTransaction(AdvTransactionListActivity.click_transaction);
                } catch (Exception e) {
                    e.printStackTrace();
                }
              // context.startActivity(intent);
            }
        });
        return rowView;
    }


    ///code for Search & filter voucher from list
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();

                // perform your search here using the searchConstraint String.
                ArrayList<AdvTransactions.AdvTransaction>  filteredJsonarray = new ArrayList<>();
                try {
                    jsonarrayOne = context.transaction; // saves the original data in mOriginalValues
                    if (constraint == null || constraint.length() == 0) {
                        // set the Original result to return
                        results.count = jsonarrayOne.size();
                        results.values = jsonarrayOne;
                    }
                    else {
                        constraint = constraint.toString().toLowerCase();
                        for (int i = 0; i < jsonarrayOne.size(); i++) {
                            AdvTransactions.AdvTransaction putObj;
                            String dataTid=jsonarrayOne.get(i).transaction_id;

                            putObj= jsonarrayOne.get(i);

                            if (dataTid.contains(constraint.toString())) {
                                filteredJsonarray.add(putObj);
                            }
                        }
                        results.count = filteredJsonarray.size();
                        results.values = filteredJsonarray;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e("VALUES", results.values.toString());

                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                ArrayList<AdvTransactions.AdvTransaction> jsonarray = (ArrayList<AdvTransactions.AdvTransaction>)filterResults.values;
                context.TOTAL_LIST_ITEMS=jsonarray.size();
                context.Btnfooter(jsonarray);
                //context.CheckBtnBackGroud(0);
                context.setListView(0,jsonarray);

            }
        };
        return filter;
    }


    //Dialog for shoiwng final total of transaction
    public void dialogFullTransaction(AdvTransactions.AdvTransaction transaction) {
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
        ((TextView)rowView.findViewById(R.id.txt_t_id)).setText(transaction.transaction_id);
        ((TextView)rowView.findViewById(R.id.txt_merchant)).setText(transaction.merchant_id);
        ((TextView)rowView.findViewById(R.id.txt_cashier)).setText(transaction.cashier_id);
        ((TextView)rowView.findViewById(R.id.txt_org_price)).setText("$"+transaction.original_price);
        ((TextView)rowView.findViewById(R.id.txt_subtotal)).setText("$"+transaction.subtotal);
        ((TextView)rowView.findViewById(R.id.txt_token_used)).setText(transaction.tokens_used);
        ((TextView)rowView.findViewById(R.id.txt_total_price)).setText("$"+(transaction.total_price.equalsIgnoreCase("null")?"0.00":transaction.total_price));
        ((TextView)rowView.findViewById(R.id.txt_pay_method)).setText(transaction.pay_method);
        ((TextView)rowView.findViewById(R.id.txt_time)).setText(Utils.getConvetredDate(transaction.transaction_complete));
        String token_title = ""+transaction.tokens_bought+ " Store Credit @ $" + transaction.token_rate;
        ((TextView)rowView.findViewById(R.id.title_token_bought)).setText(token_title);
        ((TextView)rowView.findViewById(R.id.txt_token_bought)).setText(transaction.token_price);
         String vptoken_title = ""+transaction.vp_tokens+ " Cheap Store Credit @ $" + String.format("%.2f",Float.parseFloat(transaction.vp_rate));
        ((TextView)rowView.findViewById(R.id.title_vptoken_bought)).setText(vptoken_title);
        ((TextView)rowView.findViewById(R.id.txt_vptoken_bought)).setText(transaction.vp_fee);
        ((TextView)rowView.findViewById(R.id.txt_charity)).setText(transaction.charity_price);
         RecyclerView rv_list = (RecyclerView) rowView.findViewById(R.id.rv_list);
         rv_list.setLayoutManager(new StaggeredGridLayoutManager(1, RecyclerView.VERTICAL));
         ArrayList<AdvTransactions.AdvTransaction.Products.Product> array  = transaction.products;
         rv_list.setAdapter(new DlgProductAdapter(context, array));
        (rowView.findViewById(R.id.ly_pay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalCheckDialog.dismiss();
            }
        });


        finalCheckDialog.setContentView(rowView);
        finalCheckDialog.setCancelable(true);
        finalCheckDialog.setCanceledOnTouchOutside(true);
        finalCheckDialog.show();

    }


}
