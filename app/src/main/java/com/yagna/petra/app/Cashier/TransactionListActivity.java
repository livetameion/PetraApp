package com.yagna.petra.app.Cashier;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yagna.petra.app.Admin.AdminListActivity;
import com.yagna.petra.app.GlobalActivity;
import com.yagna.petra.app.Model.Customer;
import com.yagna.petra.app.Model.Transaction;
import com.yagna.petra.app.Model.Transactions;
import com.yagna.petra.app.Model.Wallet;
import com.yagna.petra.app.Model.Wallets;
import com.yagna.petra.app.R;
import com.yagna.petra.app.Util.Constants;
import com.yagna.petra.app.Util.CustomDialog;
import com.yagna.petra.app.Util.PrefUtil;
import com.yagna.petra.app.Util.Utils;
import com.yagna.petra.app.views.TypefacedButton;
import com.yagna.petra.app.views.TypefacedTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link AdminListActivity}.
 */
public class TransactionListActivity extends GlobalActivity {
    public static final String ARG_ITEM_ID = "item_id";
    private ListView listView;
    private LinearLayout ly_btn;
    private ImageView txt_previous;
    private ImageView txt_next;
    private TypefacedTextView txt_page_no;
    private TypefacedTextView txtview;
    private HorizontalScrollView hsv;
    private int increment;
    public int TOTAL_LIST_ITEMS;
    public int NUM_ITEMS_PAGE;
    private int noOfBtns;
    private TypefacedTextView[] btns;
    private ArrayList<Transaction>  lastVoucherArrary;
    private TransactionListViewAdapter listAdapter;
    private SharedPreferences common_mypref;
    private Utils util;
    private CustomDialog cd;
    public ArrayList<Transaction>  transaction;
    private View rootView;
    public static String voidOrRefund="refund";
    private SearchView item_search;
    public static final String EXTRA_TRANSACTION_ACTION = "transactionAction";
    private static final String EXTRA_TRANSACTION_RESULT = "transactionResult";
    public static final String EXTRA_TRANSACTION_STATUS_MESSAGE = "transactionStatusMessage";
    private static final String EXTRA_TRANSACTION_UNIQUE_ID = "transactionUniqueId";
    private String process_trn_id="";
    private int currentPage=0;
    private Dialog finalCheckDialog;
    private TypefacedTextView txt_token,txt_token_detail,txt_vptoken,txt_vptoken_detail,txt_you_refund,txt_you_pay;
    private AppCompatCheckBox cb_token,cb_tip,cb_charity,cb_used_token,cb_vp_token, cb_original;
    private String feathers;
    private TypefacedTextView txt_charity_amount,txt_store_credit;
    private Transaction ref_transaction;
    public static Transaction click_transaction;
    private String ref_token="0.0";
    private String ref_vp_token="0.0";
    private TypefacedTextView txt_tip_amount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
         if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
         }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        voidOrRefund=getIntent().getStringExtra("type");
        common_mypref = getSharedPreferences("user", 0);
        util = new Utils(this);
        cd = new CustomDialog(this);
        listView = (ListView) findViewById(R.id.listView);
        ly_btn = (LinearLayout) findViewById(R.id.ly_btn);
        txt_previous =  findViewById(R.id.previous);
        txt_next =  findViewById(R.id.next);
        txtview = (TypefacedTextView) findViewById(R.id.txtview);
        hsv = (HorizontalScrollView) findViewById(R.id.horizontalScrollview);
        ly_btn = (LinearLayout) findViewById(R.id.ly_btn);
        txt_page_no =(TypefacedTextView) findViewById(R.id.txt_page_no);

        loadTrans();
    }
    //Function to set pagination buttons
    public void Btnfooter(final ArrayList<Transaction> array) {
        increment=0;
        ly_btn.removeAllViews();
        int val = TOTAL_LIST_ITEMS % NUM_ITEMS_PAGE;
        val = val == 0 ? 0 : 1;
        noOfBtns = TOTAL_LIST_ITEMS / NUM_ITEMS_PAGE + val;

        btns = new TypefacedTextView[noOfBtns];
        for (int i = 0; i < noOfBtns; i++) {
            btns[i] = new TypefacedTextView(TransactionListActivity.this);
            btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
            btns[i].setText("" + (i + 1));
            btns[i].setPadding(15,5,15,5);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(android.widget.Toolbar.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(3,3,3,3);
            ly_btn.addView(btns[i], lp);

            final int j = i;

            btns[j].setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    setListView(j,array);
                    CheckBtnBackGroud(j);
                    increment = j;
                    CheckEnable(increment);
                }
            });

        }
        CheckEnable(increment);

        txt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(increment != (noOfBtns-1)) {
                    increment++;
                    setListView(increment,array);
                    CheckBtnBackGroud(increment);
                    CheckEnable(increment);
                    hsv.scrollTo(btns[increment].getLeft(),0);
                }
                else
                    CheckEnable(increment);

            }
        });
        txt_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(increment != 0) {
                    increment--;
                    setListView(increment, array);
                    CheckBtnBackGroud(increment);
                    CheckEnable(increment);
                    hsv.scrollTo(btns[increment].getLeft(), 0);
                }
                else
                    CheckEnable(increment);

            }
        });

    }
    private void CheckEnable(int increment)
    {
        if(increment == (noOfBtns-1))
        {
            txt_next.setEnabled(false);
        }
        else{
            txt_next.setEnabled(true);
        }

        if(increment == 0)
        {
            txt_previous.setEnabled(false);
        }
        else
        {
            txt_previous.setEnabled(true);
        }
    }
    public void CheckBtnBackGroud(int index) {
        txtview.setText("Showing Page " + (index + 1) + " of " + noOfBtns);
        for (int i = 0; i < noOfBtns; i++) {
            if (i == index) {
                btns[i].setBackgroundColor(getResources().getColor(R.color.dark_blue));
                btns[i].setTextColor(getResources().getColor(android.R.color.white));
                txt_page_no.setText(""+btns[i].getText().toString());

            } else {
                btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                btns[i].setTextColor(getResources().getColor(android.R.color.black));
            }
        }
    }
    //Filling data to listview
    public void setListView(int number,ArrayList<Transaction> array) {
        currentPage=number;
        lastVoucherArrary=array;
        ArrayList<Transaction> sort = new ArrayList<>();
        try {
            int start = number * NUM_ITEMS_PAGE;
            for(int i=start;i<(start)+NUM_ITEMS_PAGE;i++)
            {
                if(i<array.size())
                {
                    sort.add(array.get(i));
                }
                else
                {
                    break;
                }
            }
            listAdapter = new TransactionListViewAdapter(TransactionListActivity.this, sort);
            listView.setAdapter(listAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadTrans() {
        if (util.checkInternetConnection()) {


            JSONObject jar = new JSONObject();
            cd.show("");
            Log.e("loadTrans", Constants.transactionsUrl);
            //  Log.e("loadTrans", ""+jar);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.transactionsUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            cd.hide();
                            Log.e("loadTrans", ""+response);
                            try {
                             //   JSONArray simple = (new JSONObject(response)).getJSONObject("data").getJSONArray("transactions");
                                ArrayList<Transaction> simple =(new Transactions(new JSONObject(response).getJSONObject("data").getJSONArray("transactions"))).array;
                                transaction= new ArrayList<>();
                                String cashier_id="";
                                try {
                                    JSONObject jobj = new JSONObject(PrefUtil.getLoginData(common_mypref));
                                    cashier_id = jobj.getJSONObject("cashier").getString("cashier_id");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                for(int i = 0; i<simple.size();i++){
                                    String cashid= "",tid="";
                                    try {
                                        cashid = simple.get(i).cashier_id;
                                        tid =simple.get(i).transaction_id;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                        if(cashid.equalsIgnoreCase(cashier_id)&& tid.trim().length()!=0 ){
                                      transaction.add(simple.get(i));
                                    }
                                }

                                NUM_ITEMS_PAGE = 8;
                                int len = transaction.size();
                                TOTAL_LIST_ITEMS=transaction.size();
                                Btnfooter(transaction);
                                CheckBtnBackGroud(currentPage);
                                setListView(currentPage,transaction);

                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            cd.hide();
                            Toast.makeText(TransactionListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    JSONObject jobj = null;
                    try {
                        jobj = new JSONObject(PrefUtil.getLoginData(common_mypref));
                        String merchant_location_id = jobj.getJSONObject("cashier").getString("merchant_location_id");
                        params.put("locationid",merchant_location_id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    params.put("load","transactions");
                    Log.e("getParams", String.valueOf(params));
                    return params;
                }
                @Override
                public Map<String, String> getHeaders(){
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type","application/x-www-form-urlencoded");
                    return params;
                }
            };

            stringRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 100000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 100000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(TransactionListActivity.this);
            requestQueue.add(stringRequest);

        }else {
            Toast.makeText(TransactionListActivity.this,getResources().getString(R.string.dataconnection),Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            //navigateUpTo(new Intent(this, AdminListActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void dialogforRefundvoid(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TransactionListActivity.this);
        if(voidOrRefund.equalsIgnoreCase("refund"))
            alertDialog.setTitle("Want to refund ?");
        else
            alertDialog.setTitle("Want to void the Transaction?");

        alertDialog.setMessage("Enter Transaction number");

        final EditText input = new EditText(TransactionListActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(R.mipmap.app_icon);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }


    public void askForVoidPermission(final Transaction transaction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TransactionListActivity.this);
       ref_transaction =transaction;
        builder.setCancelable(true);
        String title = "Void";
        String text = "Are sure want to void the transaction?";
        voidOrRefund ="void";

        builder.setTitle(title)
                .setMessage(text)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(transaction.pay_method.equalsIgnoreCase("Card")){
                            process_trn_id=transaction.transaction_id;
                             payAnyWhereRefunVoidCall(transaction.pa_transaction_id,transaction.pa_receipt_id,transaction.original_price);
                        }
                        else {
                            voidApiCall("0","No");

                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);
        AlertDialog alert11 = builder.create();
        alert11.show();
        TypefacedTextView TypefacedTextView = (TypefacedTextView) alert11.findViewById(android.R.id.message);
        TypefacedTextView.setTextSize(getResources().getDimension(R.dimen.size_dialog_msg));
    }

    private void payAnyWhereRefunVoidCall(String patransaction_id, String pareceipt_id,String amount) {
        String mTransactionActionUniqueId = patransaction_id;
        String mRefundAmount = amount;
        String receiptId = pareceipt_id;
        if(voidOrRefund.equalsIgnoreCase("refund")){
            Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse(Constants.REFUND_URL +  "?transactionUniqueId=" + mTransactionActionUniqueId + "&receiptId=" + receiptId +"&refundAmount=" + mRefundAmount));
            startActivityForResult(intent, Constants.REFUND_REQUEST_CODE);
        }
        else if(voidOrRefund.equalsIgnoreCase("void")){
            Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse(Constants.VOID_URL +  "?transactionUniqueId=" + mTransactionActionUniqueId + "&receiptId=" + receiptId ));
            startActivityForResult(intent, Constants.VOID_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.REFUND_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Log.d("PETRA","result ok");
                String transactionAction = data.getStringExtra(EXTRA_TRANSACTION_ACTION);
                String transactionResult = data.getStringExtra(EXTRA_TRANSACTION_RESULT);
                String transactionStatusMessage = data.getStringExtra(EXTRA_TRANSACTION_STATUS_MESSAGE);
                String transactionUniqueId = data.getStringExtra(EXTRA_TRANSACTION_UNIQUE_ID);
                Toast.makeText(TransactionListActivity.this, "action:"+transactionAction+"\nResult:"+transactionResult+"\nMessage:"+transactionStatusMessage+"\nTrn_id"+
                        transactionUniqueId, Toast.LENGTH_LONG).show();
                if(voidOrRefund.equalsIgnoreCase("refund"))
                    retunOrRefundApiCall(transactionUniqueId,transactionStatusMessage);
                else
                    voidApiCall(transactionUniqueId,transactionStatusMessage);

            } else if(resultCode == RESULT_CANCELED){
                Log.d("PETRA","result cancelled");
                Toast.makeText(TransactionListActivity.this, "Refunding Process Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void retunOrRefundApiCall(final String patransaction_id, final String patransactionStatusMessage) {
        if (util.checkInternetConnection()) {

            JSONObject jar = new JSONObject();
            cd.show("");
            Log.e("loadTrans", Constants.transactionsUrl);
            //  Log.e("loadTrans", ""+jar);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.transactionsUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            cd.hide();
                            Log.e("retunOrRefund", ""+response);
                            try {
                                finalCheckDialog.dismiss();
                                loadTrans();

                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            cd.hide();
                            Toast.makeText(TransactionListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    JSONObject jobj = null;
                    String total = txt_you_refund.getText().toString().replace("$","").replace("Tokens","").trim();
                    String charity=(cb_charity.isChecked())?
                        txt_charity_amount.getText().toString().replace("$","").replace("","").trim():"0.00";
                    String tip=(cb_tip.isChecked())? ref_transaction.tip:"0.00";

                    params.put("action","refund");
                    params.put("transaction_id",ref_transaction.transaction_id);
                    params.put("refund_total_amount",total);
                    params.put("refund_token_used",ref_transaction.tokens_used);
                    params.put("refund_token_price",String.format("%.2f",Double.parseDouble(ref_token)*Double.parseDouble(ref_transaction.token_rate)));
                    params.put("refund_token_rate",ref_transaction.token_rate);
                    params.put("refund_token_bought", ref_token);
                    params.put("refund_vp_fee",String.format("%.2f",Double.parseDouble(ref_vp_token)*Double.parseDouble(ref_transaction.vp_rate)));
                    params.put("refund_vp_rate",ref_transaction.vp_rate);
                    params.put("refund_vp_token",ref_vp_token);
                    params.put("refund_charity",charity);
                    params.put("refund_tip",tip);
                    params.put("refund_pay_method",ref_transaction.pay_method);
                    params.put("refund_patransaction_id",patransaction_id);
                    params.put("refund_patransaction_status",patransactionStatusMessage);
                    Log.e("getParams", String.valueOf(params));
                    return params;
                }
                @Override
                public Map<String, String> getHeaders(){
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type","application/x-www-form-urlencoded");
                    return params;
                }
            };

            stringRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 100000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 100000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(TransactionListActivity.this);
            requestQueue.add(stringRequest);

        }else {
            Toast.makeText(TransactionListActivity.this,getResources().getString(R.string.dataconnection),Toast.LENGTH_LONG).show();
        }
    }
    public void voidApiCall(final String patransaction_id, final String patransactionStatusMessage) {
        if (util.checkInternetConnection()) {

            JSONObject jar = new JSONObject();
            cd.show("");
            Log.e("loadTrans", Constants.transactionsUrl);
            //  Log.e("loadTrans", ""+jar);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.transactionsUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            cd.hide();
                            Log.e("retunOrRefund", ""+response);
                            try {
                                loadTrans();
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            cd.hide();
                            Toast.makeText(TransactionListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    JSONObject jobj = null;
                    params.put("action","void");
                    params.put("transaction_id",ref_transaction.transaction_id);
                 //   params.put("refund_patransaction_id",patransaction_id);
                   // params.put("refund_patransaction_status",patransactionStatusMessage);
                    Log.e("getParams", String.valueOf(params));
                    return params;
                }
                @Override
                public Map<String, String> getHeaders(){
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type","application/x-www-form-urlencoded");
                    return params;
                }
            };

            stringRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 100000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 100000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(TransactionListActivity.this);
            requestQueue.add(stringRequest);

        }else {
            Toast.makeText(TransactionListActivity.this,getResources().getString(R.string.dataconnection),Toast.LENGTH_LONG).show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);


        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        item_search = (SearchView)menu.findItem(R.id.action_search).getActionView();
        item_search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        item_search.setQueryHint(getResources().getString(R.string.search_hint));
        EditText searchEditText = (EditText)item_search.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.black));
        searchEditText.setHintTextColor(getResources().getColor(R.color.dark_grey));
        item_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                TransactionListActivity.this.listAdapter.getFilter().filter(query.toString());
                return true;
            }

        });

        return true;
    }


    public void checkCustomerOrMerchant(final Transaction transaction) {
        if(!transaction.customer_id.equalsIgnoreCase("null")){
            if(!transaction.customer_id.equalsIgnoreCase("0")){

                callCustomerPointDetailsApi(transaction);
            }
            else  if(!transaction.customer_merchant_id.equalsIgnoreCase("null")){

                callMerchantPointDetailsApi(transaction);
            }
            else{
                askForRefundPermission(transaction,"0");
            }
        }
        else  if(!transaction.customer_merchant_id.equalsIgnoreCase("null")){

            callMerchantPointDetailsApi(transaction);
        }
        else{
            askForRefundPermission(transaction,"0");

        }
    }

    //Load Customer points if login type is customer

    private void callCustomerPointDetailsApi(final Transaction transaction) {
        if (util.checkInternetConnection()) {
            cd.show("");
            String url= Constants.customerUrl + transaction.customer_id;
            Log.e("callLoadPointApi",url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            cd.hide();
                            Log.e("callLoadPointApi", ""+response);
                            try {
                                Customer customer=new Customer((new JSONObject(response)).getJSONObject("data").getJSONObject("customer"));
                                ArrayList<Wallet> wallets =new ArrayList<>();
                                try {
                                    wallets=new Wallets((new JSONObject(response)).getJSONObject("data").getJSONArray("wallet")).array;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                askForRefundPermission(transaction,wallets.get(0).feathers);

                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            cd.hide();
                            Toast.makeText(TransactionListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<>();


                    Log.e("getParams", String.valueOf(params));
                    return params;
                }
                @Override
                public Map<String, String> getHeaders(){
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type","application/x-www-form-urlencoded");
                    return params;
                }
            };

            stringRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 100000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 100000;
                }

                @Override
                public void retry(VolleyError error)  {

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }else {
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.dataconnection),Toast.LENGTH_LONG).show();
        }
    }

    //Load merchant's points if login type is Merchant
    private void callMerchantPointDetailsApi(final Transaction transaction) {
        if (util.checkInternetConnection()) {
            JSONObject jar = new JSONObject();
            cd.show("");
            String url= Constants.merchantDataUrl;
            Log.e("callLoadPointApi",url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            cd.hide();
                            Log.e("loadTrans", ""+response);
                            try {

                                JSONObject data=new JSONObject(response);
                                askForRefundPermission(transaction, data.getJSONObject("data").getJSONObject("merchant_wallet").getString("feathers"));

                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            cd.hide();
                            Toast.makeText(TransactionListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("action","buyingpotential");
                    params.put("customermerchantid",transaction.customer_merchant_id);

                    Log.e("getParams", String.valueOf(params));
                    return params;
                }
                @Override
                public Map<String, String> getHeaders(){
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type","application/x-www-form-urlencoded");
                    return params;
                }
            };

            stringRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 100000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 100000;
                }

                @Override
                public void retry(VolleyError error)  {

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }else {
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.dataconnection),Toast.LENGTH_LONG).show();
        }
    }


    public void askForRefundPermission(final Transaction transaction, String sc ) {
        voidOrRefund = "refund";
        ref_transaction =transaction;
        feathers = sc;
        finalCheckDialog = new Dialog(TransactionListActivity.this, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.dialog_refund_totalcheck, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        finalCheckDialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);

        LinearLayout ly_pay =  rowView.findViewById(R.id.ly_pay);
        LinearLayout ly_tip =  rowView.findViewById(R.id.ly_tip);

        ly_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalCheckDialog.dismiss();
            }
        });

        txt_you_refund = rowView.findViewById(R.id.txt_you_refund);
        CompoundButton.OnCheckedChangeListener checkChange= new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               switch (compoundButton.getId()){
                   case R.id.cb_original:
                       calculateRefundTotal();

                       break;
                   case R.id.cb_token:
                       if(b)
                       {
                           if(Double.parseDouble(feathers)>=Double.parseDouble(transaction.tokens_bought))
                           {
                               ref_token = transaction.tokens_bought;
                               calculateRefundTotal();

                           }
                           else{
                               AlertDialog.Builder builder = new AlertDialog.Builder(TransactionListActivity.this);
                               final EditText input = new EditText(TransactionListActivity.this);
                               input.setInputType(InputType.TYPE_CLASS_NUMBER);
                               input.setHint("$0.00");
                               input.setTextColor(getResources().getColor(R.color.black));
                               LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                       LinearLayout.LayoutParams.MATCH_PARENT,
                                       LinearLayout.LayoutParams.MATCH_PARENT);
                               lp.setMargins(30, 5, 30, 5);
                               input.setLayoutParams(lp);
                               builder.setView(input);
                               builder.setCancelable(true);
                               builder.setTitle(getResources().getString(R.string.notice))
                                       .setMessage(getResources().getString(R.string.less_feathers)+
                                               "\n\n We can refund are only : "+feathers+ "token/s\n\nFor how many remaining tokens, the customer want to be refunded?")
                                       .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                           public void onClick(DialogInterface dialog, int which) {

                                               Long total_token = Math.round(Double.parseDouble(feathers) - Double.parseDouble(input.getText().toString()));
                                               if(total_token>=0){
                                                   dialog.dismiss();
                                                   feathers= ""+Math.round(total_token);
                                                    ref_token = input.getText().toString();
                                                   txt_token_detail.setText("Refundable "+input.getText().toString()+" Store Credit @ $"+ transaction.token_rate);
                                                   txt_token.setText("$"+String.format("%.2f",Double.parseDouble(input.getText().toString())*Double.parseDouble(transaction.token_rate)));
                                                   calculateRefundTotal();
                                               }
                                               else{
                                                   ref_token="0.0";
                                                   cb_token.setChecked(false);
                                                   calculateRefundTotal();
                                                   Toast.makeText(TransactionListActivity.this,getResources().getString(R.string.invalid),Toast.LENGTH_LONG).show();
                                               }
                                           }
                                       })
                                       .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                           public void onClick(DialogInterface dialog, int which) {
                                               dialog.dismiss();
                                               // do nothing
                                               ref_token = "0.0";

                                               txt_token_detail.setText("Refundable "+transaction.tokens_bought+" Store Credit @ $"+ transaction.token_rate);
                                               txt_token.setText("$"+String.format("%.2f",Double.parseDouble(transaction.tokens_bought)*Double.parseDouble(transaction.token_rate)));
                                               cb_token.setChecked(false);
                                               calculateRefundTotal();
                                           }
                                       })
                                       .setIcon(android.R.drawable.ic_dialog_alert);
                               AlertDialog alert11 = builder.create();
                               alert11.show();
                               TypefacedTextView TypefacedTextView =  alert11.findViewById(android.R.id.message);
                               TypefacedTextView.setTextSize(getResources().getDimension(R.dimen.size_dialog_msg));
                           }
                       }
                       else{
                           ref_token = "0.0";
                           txt_token_detail.setText("Refundable "+transaction.tokens_bought+" Store Credit @ $"+ transaction.token_rate);
                           txt_token.setText("$"+String.format("%.2f",Double.parseDouble(transaction.tokens_bought)*Double.parseDouble(transaction.token_rate)));
                           calculateRefundTotal();

                       }


                       break;
                   case R.id.cb_vp_token:
                       if(b)
                       {
                           if(Double.parseDouble(feathers)>=Double.parseDouble(transaction.vp_tokens))
                           {
                               ref_vp_token =transaction.vp_tokens;
                               calculateRefundTotal();

                           }
                           else{
                               AlertDialog.Builder builder = new AlertDialog.Builder(TransactionListActivity.this);
                               final EditText input = new EditText(TransactionListActivity.this);
                               input.setInputType(InputType.TYPE_CLASS_NUMBER);
                               input.setHint("$0.00");
                               input.setTextColor(getResources().getColor(R.color.black));
                               LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                       LinearLayout.LayoutParams.MATCH_PARENT,
                                       LinearLayout.LayoutParams.MATCH_PARENT);
                               lp.setMargins(30, 5, 30, 5);
                               input.setLayoutParams(lp);
                               builder.setView(input);
                               builder.setCancelable(true);
                               builder.setTitle(getResources().getString(R.string.notice))
                                       .setMessage(getResources().getString(R.string.less_feathers)+
                                               "\n\n We can refund are only : "+feathers+ "token/s\n\nFor how many remaining tokens, the customer want to be refunded?")
                                       .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                           public void onClick(DialogInterface dialog, int which) {

                                               Long ref_token = Math.round(Double.parseDouble(feathers) - Double.parseDouble(input.getText().toString()));
                                               if(ref_token>=0){
                                                   dialog.dismiss();
                                                   feathers= ""+Math.round(ref_token);
                                                   ref_vp_token =input.getText().toString();
                                                   txt_vptoken_detail.setText("Refundable "+input.getText().toString()+" Store Credit @ $"+ transaction.vp_rate);
                                                   txt_vptoken.setText("$"+String.format("%.2f",Double.parseDouble(input.getText().toString())*Double.parseDouble(transaction.vp_rate)));
                                                   calculateRefundTotal();
                                               }
                                               else{
                                                   ref_vp_token="0.0";
                                                   cb_vp_token.setChecked(false);
                                                   calculateRefundTotal();
                                                   Toast.makeText(TransactionListActivity.this,getResources().getString(R.string.invalid),Toast.LENGTH_LONG).show();
                                               }
                                           }
                                       })
                                       .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                           public void onClick(DialogInterface dialog, int which) {
                                               dialog.dismiss();
                                               ref_vp_token ="0.0";
                                               txt_vptoken_detail.setText("Refundable "+transaction.vp_tokens+" Store Credit @ $"+ transaction.token_rate);
                                               txt_vptoken.setText("$"+String.format("%.2f",Double.parseDouble(transaction.tokens_bought)*Double.parseDouble(transaction.token_rate)));
                                               cb_vp_token.setChecked(false);
                                               calculateRefundTotal();
                                           }
                                       })
                                       .setIcon(android.R.drawable.ic_dialog_alert);
                               AlertDialog alert11 = builder.create();
                               alert11.show();
                               TypefacedTextView TypefacedTextView =  alert11.findViewById(android.R.id.message);
                               TypefacedTextView.setTextSize(getResources().getDimension(R.dimen.size_dialog_msg));
                           }
                       }
                       else{
                           ref_vp_token ="0.0";
                           txt_vptoken_detail.setText("Refundable "+transaction.vp_tokens+" Store Credit @ $"+ transaction.vp_rate);
                           txt_vptoken.setText("$"+transaction.vp_fee);
                           calculateRefundTotal();

                       }

                       break;
                   case R.id.cb_used_token:

                       calculateRefundTotal();
                       break;
                   case R.id.cb_charity:
                       calculateRefundTotal();
                       break;
                   case R.id.cb_tip:
                       calculateRefundTotal();
                       break;
               }


            }

            private void calculateRefundTotal() {
                Double total=0.00;
                if(cb_original.isChecked()){
                       total= total+Double.parseDouble(transaction.original_price);
                }
                if(cb_token.isChecked()){
                    if(!transaction.pay_method.equalsIgnoreCase(Constants.PAYMETHOD_EXCHANGE))
                       total= total+Double.parseDouble(txt_token.getText().toString().replace("$","").replace(" ","").replace("null","0.0").replace("Tokens",""));

                }
                if(cb_vp_token.isChecked()){
                    if(!transaction.pay_method.equalsIgnoreCase(Constants.PAYMETHOD_EXCHANGE))
                       total= total+Double.parseDouble(txt_vptoken.getText().toString().replace("$","").replace(" ","").replace("null","0.0"));

                }
                if(cb_used_token.isChecked()){
                    total= total-Double.parseDouble(txt_store_credit.getText().toString().replace("$","").replace("-","").replace("null","0.0"));
                    if(total<0){
                        total= total+Double.parseDouble(txt_store_credit.getText().toString().replace("$","").replace("-","").replace("null","0.0"));
                    }
                }
                if(cb_tip.isChecked()){

                    total= total+Double.parseDouble(txt_tip_amount.getText().toString().replace("$","").replace("-","").replace("null","0.0"));

                }
                if(cb_charity.isChecked()){
                    total= total+Double.parseDouble(txt_charity_amount.getText().toString().replace("$","").replace("-","").replace("null","0.0"));

                }
                if(!transaction.pay_method.equalsIgnoreCase(Constants.PAYMETHOD_EXCHANGE))
                    txt_you_refund.setText("$"+ String.format("%.2f",total));
                else
                {
                    if(cb_original.isChecked())
                        txt_you_refund.setText(""+ Math.round(Double.parseDouble(transaction.total_price))+" Tokens");
                    else
                        txt_you_refund.setText("0 Tokens");
                }
            }
        };
         cb_original = rowView.findViewById(R.id.cb_original);
         cb_original.setOnCheckedChangeListener(checkChange);

         cb_token = rowView.findViewById(R.id.cb_token);
         cb_token.setOnCheckedChangeListener(checkChange);
          if(transaction.pay_method.equalsIgnoreCase(Constants.PAYMETHOD_EXCHANGE))
            cb_token.setVisibility(View.INVISIBLE);

        cb_vp_token = rowView.findViewById(R.id.cb_vp_token);
        cb_vp_token.setOnCheckedChangeListener(checkChange);

        cb_used_token = rowView.findViewById(R.id.cb_used_token);
        cb_used_token.setOnCheckedChangeListener(checkChange);

        cb_charity = rowView.findViewById(R.id.cb_charity);
        cb_charity.setOnCheckedChangeListener(checkChange);

        cb_tip = rowView.findViewById(R.id.cb_tip);
        cb_tip.setOnCheckedChangeListener(checkChange);
        txt_tip_amount = rowView.findViewById(R.id.txt_tip_amount);

        try {
            if (Double.parseDouble(transaction.tip) > 0) {
                ly_tip.setVisibility(View.VISIBLE);
                txt_tip_amount.setText("$"+String.format("%.2f",Double.parseDouble(transaction.tip)));
            } else {
                ly_tip.setVisibility(View.GONE);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }



        TypefacedTextView txt_pay_org_price =  rowView.findViewById(R.id.txt_ref_org_price);
        txt_pay_org_price.setText("$"+String.format("%.2f",Double.parseDouble(transaction.original_price)));

         txt_token_detail =  rowView.findViewById(R.id.txt_ref_token_detail);
         txt_token =  rowView.findViewById(R.id.txt_token);
         LinearLayout ly_normal_token =  rowView.findViewById(R.id.ly_normal_token);


        LinearLayout ly_charity =  rowView.findViewById(R.id.ly_charity);
        if(transaction.charity_accepted.equalsIgnoreCase("1"))
            ly_charity.setVisibility(View.VISIBLE);
        else
            ly_charity.setVisibility(View.GONE);

         txt_charity_amount =  rowView.findViewById(R.id.txt_charity_amount);
        txt_charity_amount.setText("$"+String.format("%.2f",Double.parseDouble(transaction.charity_price)));

        TypefacedTextView txt_charity_place =  rowView.findViewById(R.id.txt_charity_place);
        txt_charity_place.setText("Refundable "+"Charity");

        LinearLayout ly_insert_credit =  rowView.findViewById(R.id.ly_insert_credit);
         txt_store_credit =  rowView.findViewById(R.id.txt_inserted_token);
        txt_store_credit.setText("-$"+String.format("%.2f",Double.parseDouble(transaction.tokens_used)));

        TypefacedTextView txt_pay_method =  rowView.findViewById(R.id.txt_pay_method);
        txt_pay_method.setText(transaction.pay_method);
         txt_you_pay =  rowView.findViewById(R.id.txt_you_pay);


        if(transaction.pay_method.equalsIgnoreCase(Constants.PAYMETHOD_EXCHANGE)){
            txt_token_detail.setText("Exchange Price");
            txt_token.setText(Math.round(Double.parseDouble(transaction.total_price))+" Tokens");
            txt_you_pay.setText(Math.round(Double.parseDouble(transaction.total_price))+ " Tokens");
            txt_you_refund.setText("0 Tokens");

            ly_insert_credit.setVisibility(View.GONE);
        }
        else{
            if (Double.parseDouble(transaction.tokens_bought)>0){
                ly_normal_token.setVisibility(View.VISIBLE);
                txt_token_detail.setText("Refundable "+transaction.tokens_bought+" Store Credit @ $"+ transaction.token_rate);
                txt_token.setText("$"+transaction.token_price);
            }
            else{
                ly_normal_token.setVisibility(View.GONE);
            }
            txt_you_pay.setText("$"+String.format("%.2f",Double.parseDouble(transaction.total_price)));
            if(Double.parseDouble(transaction.tokens_used)>0){
                ly_insert_credit.setVisibility(View.VISIBLE);

            }
            else
                ly_insert_credit.setVisibility(View.GONE);
        }



        LinearLayout ly_vp_token =  rowView.findViewById(R.id.ly_vp_token);
         txt_vptoken_detail =  rowView.findViewById(R.id.txt_vptoken_detail);
         txt_vptoken =  rowView.findViewById(R.id.txt_vptoken);
        if(transaction.vp_accepted.equalsIgnoreCase("1")){
            txt_vptoken_detail.setText("Refundable "+transaction.vp_tokens+" Store Credit @ $"+ transaction.vp_rate);
            txt_vptoken.setText("$"+transaction.vp_fee);
            ly_vp_token.setVisibility(View.VISIBLE);
        }
        else{
            ly_vp_token.setVisibility(View.GONE);
        }

        TypefacedButton btn_refund =  rowView.findViewById(R.id.btn_refund);
        btn_refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(transaction.pay_method.equalsIgnoreCase(Constants.PAYMETHOD_CARD)) {
                    payAnyWhereRefunVoidCall(transaction.pa_transaction_id,transaction.pa_receipt_id,transaction.original_price);
                }
                else{
                    retunOrRefundApiCall("0","No");
                }
                //  callFinalPayment(amount_final,paymethod);
            }
        });



        finalCheckDialog.setContentView(rowView);
        finalCheckDialog.setCancelable(true);
        finalCheckDialog.setCanceledOnTouchOutside(true);
        finalCheckDialog.show();

    }

}
