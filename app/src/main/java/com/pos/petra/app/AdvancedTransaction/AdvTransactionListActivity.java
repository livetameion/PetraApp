package com.pos.petra.app.AdvancedTransaction;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pos.petra.app.GlobalActivity;
import com.pos.petra.app.Model.Transaction.AdvTransactions;
import com.pos.petra.app.Model.Wallet.Wallet;
import com.pos.petra.app.Model.Wallet.Wallets;
import com.pos.petra.app.R;
import com.pos.petra.app.Util.Constants;
import com.pos.petra.app.Util.CustomDialog;
import com.pos.petra.app.Util.PrefUtil;
import com.pos.petra.app.Util.Utils;
import com.pos.petra.app.Views.TypefacedButton;
import com.pos.petra.app.Views.TypefacedEditText;
import com.pos.petra.app.Views.TypefacedTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This Screen  will show the recent two month's transaction.
 * User have to select the start and end date if he/she wants to visit more transactions.
 */
public class AdvTransactionListActivity extends GlobalActivity {
    private static int TOTAL_PAGE = 0;
    private ListView listView;
    private ImageView txt_previous;
    private ImageView txt_next;
    private TypefacedTextView txt_page_no;
    public int TOTAL_LIST_ITEMS;
    public int NUM_ITEMS_PAGE;
    private AdvTransactionListViewAdapter listAdapter;
    private SharedPreferences common_mypref;
    public Utils util;
    private CustomDialog progressbar;
    public ArrayList<AdvTransactions.AdvTransaction> transaction;
    public static String voidOrRefund = "refund";
    private SearchView item_search;
    public static final String EXTRA_TRANSACTION_ACTION = "transactionAction";
    private static final String EXTRA_TRANSACTION_RESULT = "transactionResult";
    public static final String EXTRA_TRANSACTION_STATUS_MESSAGE = "transactionStatusMessage";
    private static final String EXTRA_TRANSACTION_UNIQUE_ID = "transactionUniqueId";
    private int currentPage = 0;
    private Dialog finalCheckDialog;
    private TypefacedTextView txt_token, txt_token_detail, txt_vptoken, txt_vptoken_detail, txt_you_refund, txt_you_pay;
    private AppCompatCheckBox cb_token, cb_tip, cb_charity, cb_used_token, cb_vp_token;
    private String feathers;
    private TypefacedTextView txt_charity_amount, txt_store_credit;
    private AdvTransactions.AdvTransaction ref_transaction;
    public static AdvTransactions.AdvTransaction click_transaction;
    private String ref_token = "0.0";
    private String ref_vp_token = "0.0";
    private TypefacedTextView txt_tip_amount;
    private int page_no = 1;
    public int[] cb_orginal;
    private TypefacedTextView txt_start_date, txt_end_date;
    private CompoundButton.OnCheckedChangeListener checkChange;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Transaction History");
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        voidOrRefund = getIntent().getStringExtra("type");
        common_mypref = getSharedPreferences("user", 0);
        util = new Utils(this);
        progressbar = new CustomDialog(this);
        listView = findViewById(R.id.listView);
        txt_previous = findViewById(R.id.previous);
        txt_next = findViewById(R.id.next);
        txt_page_no = findViewById(R.id.txt_page_no);
        Date c = Calendar.getInstance().getTime();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -2);
        Date fd = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todaydate = df.format(c);
        String oastmonth_date = df.format(fd);
        txt_end_date = findViewById(R.id.txt_end_date);
        txt_end_date.setText(todaydate);
        txt_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endDatePicker();
            }
        });
        txt_start_date = findViewById(R.id.txt_start_date);
        txt_start_date.setText(oastmonth_date);
        txt_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDatePicker();
            }
        });

        loadTrans();
    }

    //Fuction for open start date picker 
    private void startDatePicker() {
        final Calendar c = Calendar.getInstance();
        String[] str = txt_start_date.getText().toString().trim().split("-");
        int mYear = Integer.parseInt(str[0]);
        int mMonth = Integer.parseInt(str[1]);
        int mDay = Integer.parseInt(str[2]);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        txt_start_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        page_no = 1;
                        loadTrans();

                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    //Function for getting end date
    private void endDatePicker() {

        String[] str = txt_end_date.getText().toString().trim().split("-");
        int mYear = Integer.parseInt(str[0]);
        int mMonth = Integer.parseInt(str[1]);
        int mDay = Integer.parseInt(str[2]);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        txt_end_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        page_no = 1;
                        loadTrans();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    //Function to set pagination buttons
    public void Btnfooter(final ArrayList<AdvTransactions.AdvTransaction> array) {
        if (page_no == TOTAL_PAGE)
            txt_next.setEnabled(false);
        else
            txt_next.setEnabled(true);

        if (page_no == 1)
            txt_previous.setEnabled(false);
        else
            txt_previous.setEnabled(true);

        txt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (page_no < TOTAL_PAGE) {
                    page_no++;
                    loadTrans();
                }
                if (page_no == TOTAL_PAGE)
                    txt_next.setEnabled(false);
                else
                    txt_next.setEnabled(true);

                if (page_no == 1)
                    txt_previous.setEnabled(false);
                else
                    txt_previous.setEnabled(true);

            }
        });
        txt_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (page_no > 1) {
                    page_no--;
                    loadTrans();

                }
                if (page_no == TOTAL_PAGE)
                    txt_next.setEnabled(false);
                else
                    txt_next.setEnabled(true);

                if (page_no == 1)
                    txt_previous.setEnabled(false);
                else
                    txt_previous.setEnabled(true);
            }
        });

    }

    //Filling data to listview
    public void setListView(int number, ArrayList<AdvTransactions.AdvTransaction> array) {
        currentPage = number;
        ArrayList<AdvTransactions.AdvTransaction> sort = new ArrayList<>();
        try {
            int start = number * NUM_ITEMS_PAGE;
            for (int i = start; i < (start) + NUM_ITEMS_PAGE; i++) {
                if (i < array.size()) {
                    sort.add(array.get(i));
                } else {
                    break;
                }
            }
            listAdapter = new AdvTransactionListViewAdapter(AdvTransactionListActivity.this, sort);
            listView.setAdapter(listAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //API call to load transaction data in between start and end date
    private void loadTrans() {
        if (util.checkInternetConnection()) {
            JSONObject jar = new JSONObject();
            progressbar.show("");
            Log.e("loadTrans", Constants.petratransactions);
            //  Log.e("loadTrans", ""+jar);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.petratransactions,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressbar.hide();
                            Log.e("loadTrans", "" + response);

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                //   JSONArray simple = (new JSONObject(response)).getJSONObject("data").getJSONArray("transactions");
                                ArrayList<AdvTransactions.AdvTransaction> simple = (new AdvTransactions(new JSONObject(response).getJSONArray("transactions"))).array;
                                transaction = new ArrayList<>();
                                String cashier_id = "";
                                try {
                                    JSONObject jobj = new JSONObject(PrefUtil.getLoginData(common_mypref));
                                    cashier_id = jobj.getJSONObject("cashier").getString("cashier_id");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                for (int i = 0; i < simple.size(); i++) {
                                    String cashid = "", tid = "";
                                    try {
                                        cashid = simple.get(i).cashier_id;
                                        tid = simple.get(i).transaction_id;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (simple.get(i).products != null && simple.get(i).products.size() != 0)
                                        if (tid != null && cashid.equalsIgnoreCase(cashier_id) && tid.trim().length() != 0) {
                                            transaction.add(simple.get(i));
                                        }
                                }
                                NUM_ITEMS_PAGE = 10;
                                int len = transaction.size();
                                TOTAL_LIST_ITEMS = transaction.size();
                                TOTAL_PAGE = jsonObject.getInt("pages");
                                Btnfooter(transaction);
                                txt_page_no.setText("" + page_no + "/" + TOTAL_PAGE);
                                //CheckBtnBackGroud(currentPage);
                                setListView(currentPage, transaction);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressbar.hide();
                            Toast.makeText(AdvTransactionListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    JSONObject jobj = null;
                    try {
                        jobj = new JSONObject(PrefUtil.getLoginData(common_mypref));
                        String merchant_location_id = jobj.getJSONObject("cashier").getString("merchant_location_id");
                        JSONObject job = new JSONObject();
                        job.put("merchant_location_id", "" + merchant_location_id);
                        job.put("page", page_no);
                        job.put("start_date", txt_start_date.getText().toString().trim());
                        job.put("end_date", txt_end_date.getText().toString().trim());
                        params.put("action", "view");
                        params.put("data", job.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    params.put("load", "transactions");
                    Log.e("getParams", String.valueOf(params));
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
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
            RequestQueue requestQueue = Volley.newRequestQueue(AdvTransactionListActivity.this);
            requestQueue.add(stringRequest);

        } else {
            Toast.makeText(AdvTransactionListActivity.this, getResources().getString(R.string.dataconnection), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //Dialog for alert before voiding Payanywhere tansaction
    public void askForVoidPermission(final AdvTransactions.AdvTransaction transaction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdvTransactionListActivity.this);
        ref_transaction = transaction;
        builder.setCancelable(true);
        String title = "Void";
        String text = "Are sure want to void the transaction?";
        voidOrRefund = "void";
        builder.setTitle(title)
                .setMessage(text)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (transaction.pay_method.equalsIgnoreCase("Card")) {
                            payAnyWhereRefundVoidCall(transaction.pa_transaction_id, transaction.pa_receipt_id, transaction.original_price);
                        } else {
                            voidApiCall();
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
        TypefacedTextView TypefacedTextView = alert11.findViewById(android.R.id.message);
        TypefacedTextView.setTextSize(getResources().getDimension(R.dimen.size_dialog_msg));
    }

    //Calll to Payanywhere SDK for initiating the REFUND
    private void payAnyWhereRefundVoidCall(String patransaction_id, String pareceipt_id, String amount) {
        String mTransactionActionUniqueId = patransaction_id;
        String mRefundAmount = amount;
        String receiptId = pareceipt_id;
        if (voidOrRefund.equalsIgnoreCase("refund")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.REFUND_URL + "?transactionUniqueId=" + mTransactionActionUniqueId + "&receiptId=" + receiptId + "&refundAmount=" + mRefundAmount));
            startActivityForResult(intent, Constants.REFUND_REQUEST_CODE);
        } else if (voidOrRefund.equalsIgnoreCase("void")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.VOID_URL + "?transactionUniqueId=" + mTransactionActionUniqueId + "&receiptId=" + receiptId));
            startActivityForResult(intent, Constants.VOID_REQUEST_CODE);
        }
    }

    // PayAnywhere result will be received here
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REFUND_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.d("PETRA", "result ok");
                String transactionAction = data.getStringExtra(EXTRA_TRANSACTION_ACTION);
                String transactionResult = data.getStringExtra(EXTRA_TRANSACTION_RESULT);
                String transactionStatusMessage = data.getStringExtra(EXTRA_TRANSACTION_STATUS_MESSAGE);
                String transactionUniqueId = data.getStringExtra(EXTRA_TRANSACTION_UNIQUE_ID);
                Toast.makeText(AdvTransactionListActivity.this, "action:" + transactionAction + "\nResult:" + transactionResult + "\nMessage:" + transactionStatusMessage + "\nTrn_id" +
                        transactionUniqueId, Toast.LENGTH_LONG).show();
                if (voidOrRefund.equalsIgnoreCase("refund"))
                    RefundApiCall(transactionUniqueId, transactionStatusMessage);
                else
                    voidApiCall();

            } else if (resultCode == RESULT_CANCELED) {
                Log.d("PETRA", "result cancelled");
                Toast.makeText(AdvTransactionListActivity.this, "Refunding Process Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }


    // API call to set Refund data
    public void RefundApiCall(final String patransaction_id, final String patransactionStatusMessage) {
        if (util.checkInternetConnection()) {
            progressbar.show("");
            Log.e("loadTrans", Constants.transactionsUrl);
            //  Log.e("loadTrans", ""+jar);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.transactionsUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressbar.hide();
                            Log.e("retunOrRefund", "" + response);
                            try {
                                finalCheckDialog.dismiss();
                                loadTrans();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressbar.hide();
                            Toast.makeText(AdvTransactionListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    String total = txt_you_refund.getText().toString().replace("$", "").replace("Tokens", "").trim();
                    String charity = (cb_charity.isChecked()) ?
                            txt_charity_amount.getText().toString().replace("$", "").replace("", "").trim() : "0.00";
                    String tip = (cb_tip.isChecked()) ? ref_transaction.tip : "0.00";
                    params.put("action", "refund");
                    params.put("transaction_id", ref_transaction.transaction_id);
                    params.put("refund_total_amount", total);
                    if (Double.parseDouble(total) >= Double.parseDouble(ref_transaction.tokens_used)) {
                        boolean yes_refund_token = true;
                        for (int i = 0; i < ref_transaction.refunds.size(); i++) {
                            if (!ref_transaction.refunds.get(i).refund_token_used.equalsIgnoreCase("0")) {
                                yes_refund_token = false;
                            }
                        }
                        if (yes_refund_token)
                            params.put("refund_token_used", ref_transaction.tokens_used);
                    }
                    params.put("refund_token_price", String.format("%.2f", Double.parseDouble(ref_token) * Double.parseDouble(ref_transaction.token_rate)));
                    params.put("refund_token_rate", ref_transaction.token_rate);
                    params.put("refund_token_bought", ref_token);
                    params.put("refund_vp_fee", String.format("%.2f", Double.parseDouble(ref_vp_token) * Double.parseDouble(ref_transaction.vp_rate)));
                    params.put("refund_vp_rate", ref_transaction.vp_rate);
                    params.put("refund_vp_token", ref_vp_token);
                    params.put("refund_charity", charity);
                    params.put("refund_tip", tip);
                    params.put("refund_pay_method", ref_transaction.pay_method);
                    params.put("refund_patransaction_id", patransaction_id);
                    params.put("refund_patransaction_status", patransactionStatusMessage);
                    JSONArray jaar = new JSONArray();
                    for (int i = 0; i < cb_orginal.length; i++) {
                        if (cb_orginal[i] == 1) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("order_id", ref_transaction.products.get(i).order_id);
                                jsonObject.put("product_id", ref_transaction.products.get(i).id);
                                jsonObject.put("qty", ref_transaction.products.get(i).qty);
                                jsonObject.put("original_price", ref_transaction.products.get(i).price);
                                jsonObject.put("product_price", ref_transaction.products.get(i).sale_price);
                                jsonObject.put("charity_id", "1");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            jaar.put(jsonObject);
                        }
                    }
                    params.put("products", jaar.toString());

                    Log.e("getParams", String.valueOf(params));
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
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
            RequestQueue requestQueue = Volley.newRequestQueue(AdvTransactionListActivity.this);
            requestQueue.add(stringRequest);

        } else {
            Toast.makeText(AdvTransactionListActivity.this, getResources().getString(R.string.dataconnection), Toast.LENGTH_LONG).show();
        }
    }

    // API call to set Void data
    public void voidApiCall() {
        if (util.checkInternetConnection()) {

            JSONObject jar = new JSONObject();
            progressbar.show("");
            Log.e("loadTrans", Constants.transactionsUrl);
            //  Log.e("loadTrans", ""+jar);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.transactionsUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressbar.hide();
                            Log.e("retunOrRefund", "" + response);
                            try {
                                loadTrans();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressbar.hide();
                            Toast.makeText(AdvTransactionListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    JSONObject jobj = null;
                    params.put("action", "void");
                    params.put("transaction_id", ref_transaction.transaction_id);
                    Log.e("getParams", String.valueOf(params));
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
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
            RequestQueue requestQueue = Volley.newRequestQueue(AdvTransactionListActivity.this);
            requestQueue.add(stringRequest);

        } else {
            Toast.makeText(AdvTransactionListActivity.this, getResources().getString(R.string.dataconnection), Toast.LENGTH_LONG).show();
        }
    }

    // Search filter for the transaction
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        item_search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        item_search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        item_search.setQueryHint(getResources().getString(R.string.search_hint));
        EditText searchEditText = (EditText) item_search.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.black));
        searchEditText.setHintTextColor(getResources().getColor(R.color.dark_grey));
        item_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                AdvTransactionListActivity.this.listAdapter.getFilter().filter(query.toString());
                return true;
            }
        });
        return true;
    }

    /// Function to identify merchant or customer-merchant
    public void checkCustomerOrMerchant(final AdvTransactions.AdvTransaction transaction) {
        if (!transaction.customer_id.equalsIgnoreCase("null")) {
            if (!transaction.customer_id.equalsIgnoreCase("0")) {

                callCustomerPointDetailsApi(transaction);
            } else if (!transaction.customer_merchant_id.equalsIgnoreCase("null")) {

                callMerchantPointDetailsApi(transaction);
            } else {
                dialogRefund(transaction, "0");
            }
        } else if (!transaction.customer_merchant_id.equalsIgnoreCase("null")) {

            callMerchantPointDetailsApi(transaction);
        } else {
            dialogRefund(transaction, "0");

        }
    }

    //Load Customer points if login type is customer
    private void callCustomerPointDetailsApi(final AdvTransactions.AdvTransaction transaction) {
        if (util.checkInternetConnection()) {
            progressbar.show("");
            String url = Constants.customerUrl + transaction.customer_id;
            Log.e("callLoadPointApi", url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressbar.hide();
                            Log.e("callLoadPointApi", "" + response);
                            try {
                                ArrayList<Wallet> wallets = new ArrayList<>();
                                try {
                                    wallets = new Wallets((new JSONObject(response)).getJSONObject("data").getJSONArray("wallet")).array;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                dialogRefund(transaction, wallets.get(0).feathers);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressbar.hide();
                            Toast.makeText(AdvTransactionListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<>();
                    Log.e("getParams", String.valueOf(params));
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
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
                public void retry(VolleyError error) {

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.dataconnection), Toast.LENGTH_LONG).show();
        }
    }

    //Load merchant's points if login type is Merchant
    private void callMerchantPointDetailsApi(final AdvTransactions.AdvTransaction transaction) {
        if (util.checkInternetConnection()) {
            JSONObject jar = new JSONObject();
            progressbar.show("");
            String url = Constants.merchantDataUrl;
            Log.e("callLoadPointApi", url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressbar.hide();
                            Log.e("loadTrans", "" + response);
                            try {

                                JSONObject data = new JSONObject(response);
                                dialogRefund(transaction, data.getJSONObject("data").getJSONObject("merchant_wallet").getString("feathers"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressbar.hide();
                            Toast.makeText(AdvTransactionListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "buyingpotential");
                    params.put("customermerchantid", transaction.customer_merchant_id);

                    Log.e("getParams", String.valueOf(params));
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
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
                public void retry(VolleyError error) {

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.dataconnection), Toast.LENGTH_LONG).show();
        }
    }

    // Dialg for showing the  refund option for the transction
    public void dialogRefund(final AdvTransactions.AdvTransaction transaction, String sc) {
        voidOrRefund = "refund";
        ref_transaction = transaction;
        feathers = sc;
        finalCheckDialog = new Dialog(AdvTransactionListActivity.this, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.dialog_refund_totalcheck, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        finalCheckDialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        LinearLayout ly_pay = rowView.findViewById(R.id.ly_pay);
        LinearLayout ly_tip = rowView.findViewById(R.id.ly_tip);
        ly_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalCheckDialog.dismiss();
            }
        });
        txt_you_refund = rowView.findViewById(R.id.txt_you_refund);

        // Set Checkbox Listner.
        setCheckboxLisnter(transaction);

        int states[][] = {{android.R.attr.state_checked}, {}};
        int colors[] = {R.color.dark_grey, R.color.dark_grey};
        cb_token = rowView.findViewById(R.id.cb_token);
        cb_token.setOnCheckedChangeListener(checkChange);

        if (transaction.pay_method.equalsIgnoreCase(Constants.PAYMETHOD_EXCHANGE))
            cb_token.setVisibility(View.INVISIBLE);

        cb_vp_token = rowView.findViewById(R.id.cb_vp_token);
        cb_vp_token.setOnCheckedChangeListener(checkChange);

        cb_used_token = rowView.findViewById(R.id.cb_used_token);
        cb_used_token.setOnCheckedChangeListener(checkChange);

        TypefacedTextView txt_inserted_token = rowView.findViewById(R.id.txt_inserted_token);
        TypefacedTextView txt_title_insert_token = rowView.findViewById(R.id.txt_title_insert_token);

        cb_charity = rowView.findViewById(R.id.cb_charity);
        cb_charity.setOnCheckedChangeListener(checkChange);


        cb_tip = rowView.findViewById(R.id.cb_tip);
        cb_tip.setOnCheckedChangeListener(checkChange);
        txt_tip_amount = rowView.findViewById(R.id.txt_tip_amount);

        try {
            if (Double.parseDouble(transaction.tip) > 0) {
                ly_tip.setVisibility(View.VISIBLE);
                txt_tip_amount.setText("$" + String.format("%.2f", Double.parseDouble(transaction.tip)));
            } else {
                ly_tip.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        txt_token_detail = rowView.findViewById(R.id.txt_ref_token_detail);
        txt_token = rowView.findViewById(R.id.txt_token);
        LinearLayout ly_normal_token = rowView.findViewById(R.id.ly_normal_token);

        LinearLayout ly_charity = rowView.findViewById(R.id.ly_charity);
        if (transaction.charity_accepted.equalsIgnoreCase("1"))
            ly_charity.setVisibility(View.VISIBLE);
        else
            ly_charity.setVisibility(View.GONE);

        txt_charity_amount = rowView.findViewById(R.id.txt_charity_amount);
        txt_charity_amount.setText("$" + String.format("%.2f", Double.parseDouble(transaction.charity_price)));

        TypefacedTextView txt_charity_place = rowView.findViewById(R.id.txt_charity_place);
        txt_charity_place.setText("Charity");
        txt_charity_place.setTextColor(getResources().getColor(R.color.dark_grey));
        txt_charity_amount.setTextColor(getResources().getColor(R.color.dark_grey));

        LinearLayout ly_insert_credit = rowView.findViewById(R.id.ly_insert_credit);
        txt_store_credit = rowView.findViewById(R.id.txt_inserted_token);
        txt_store_credit.setText("-" + String.format("%.2f", Double.parseDouble(transaction.tokens_used)));

        TypefacedTextView txt_pay_method = rowView.findViewById(R.id.txt_pay_method);
        txt_pay_method.setText(transaction.pay_method);
        txt_you_pay = rowView.findViewById(R.id.txt_you_pay);

        if (transaction.pay_method.equalsIgnoreCase(Constants.PAYMETHOD_EXCHANGE)) {
            txt_token_detail.setText("Exchange Price");
            txt_token.setText(Math.round(Double.parseDouble(transaction.total_price)) + " Tokens");
            txt_you_pay.setText(Math.round(Double.parseDouble(transaction.total_price)) + " Tokens");
            txt_you_refund.setText("0 Tokens");

            ly_insert_credit.setVisibility(View.GONE);
        } else {
            if (Double.parseDouble(transaction.tokens_bought) > 0) {
                ly_normal_token.setVisibility(View.VISIBLE);
                txt_token_detail.setText("Refundable " + transaction.tokens_bought + " Store Credit @ $" + transaction.token_rate);
                txt_token.setText("$" + transaction.token_price);
            } else {
                ly_normal_token.setVisibility(View.GONE);
            }
            txt_you_pay.setText("$" + String.format("%.2f", Double.parseDouble(transaction.total_price)));
            if (Double.parseDouble(transaction.tokens_used) > 0) {
                ly_insert_credit.setVisibility(View.VISIBLE);

            } else
                ly_insert_credit.setVisibility(View.GONE);
        }

        LinearLayout ly_vp_token = rowView.findViewById(R.id.ly_vp_token);
        txt_vptoken_detail = rowView.findViewById(R.id.txt_vptoken_detail);
        txt_vptoken = rowView.findViewById(R.id.txt_vptoken);
        if (transaction.vp_accepted.equalsIgnoreCase("1")) {
            txt_vptoken_detail.setText("Refundable " + transaction.vp_tokens + " Store Credit @ $" + transaction.vp_rate);
            txt_vptoken.setText("$" + transaction.vp_fee);
            ly_vp_token.setVisibility(View.VISIBLE);
        } else {
            ly_vp_token.setVisibility(View.GONE);
        }

        TypefacedButton btn_refund = rowView.findViewById(R.id.btn_refund);
        btn_refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (transaction.pay_method.equalsIgnoreCase(Constants.PAYMETHOD_CARD)) {
                    payAnyWhereRefundVoidCall(transaction.pa_transaction_id, transaction.pa_receipt_id, transaction.original_price);
                } else {
                    RefundApiCall("0", "No");
                }

            }
        });
        RecyclerView rv_list = (RecyclerView) rowView.findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new StaggeredGridLayoutManager(1, RecyclerView.VERTICAL));
        ArrayList<AdvTransactions.AdvTransaction.Products.Product> array = transaction.products;
        cb_orginal = new int[array.size()];
        for (int i = 0; i < cb_orginal.length; i++)
            cb_orginal[i] = 0;
        rv_list.setAdapter(new DlgRefundProductAdapter(AdvTransactionListActivity.this, array, transaction));
        /////////////

        if (transaction.refunds != null && transaction.refunds.size() != 0) {
            for (int i = 0; i < transaction.refunds.size(); i++) {

                try {
                    if (!transaction.refunds.get(i).refund_tokens_bought.equalsIgnoreCase("0")) {
                        cb_token.setChecked(false);
                        cb_token.setClickable(false);
                        cb_token.setEnabled(false);
                        CompoundButtonCompat.setButtonTintList(cb_token, new ColorStateList(states, colors));
                        txt_token.setTextColor(getResources().getColor(R.color.dark_grey));
                        txt_token_detail.setTextColor(getResources().getColor(R.color.dark_grey));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (!transaction.refunds.get(i).refund_vp_token.equalsIgnoreCase("0")) {
                        cb_vp_token.setChecked(false);
                        cb_vp_token.setClickable(false);
                        cb_vp_token.setEnabled(false);
                        CompoundButtonCompat.setButtonTintList(cb_vp_token, new ColorStateList(states, colors));
                        txt_vptoken.setTextColor(getResources().getColor(R.color.dark_grey));
                        txt_vptoken_detail.setTextColor(getResources().getColor(R.color.dark_grey));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (!transaction.refunds.get(i).refund_token_used.equalsIgnoreCase("0")) {
                        cb_used_token.setChecked(false);
                        cb_used_token.setClickable(false);
                        cb_used_token.setEnabled(false);
                        CompoundButtonCompat.setButtonTintList(cb_used_token, new ColorStateList(states, colors));
                        txt_inserted_token.setTextColor(getResources().getColor(R.color.dark_grey));
                        txt_title_insert_token.setTextColor(getResources().getColor(R.color.dark_grey));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        cb_tip.setChecked(false);
        cb_tip.setClickable(false);
        cb_tip.setEnabled(false);
        CompoundButtonCompat.setButtonTintList(cb_tip, new ColorStateList(states, colors));

        cb_charity.setChecked(false);
        cb_charity.setClickable(false);
        cb_charity.setEnabled(false);
        CompoundButtonCompat.setButtonTintList(cb_charity, new ColorStateList(states, colors));

        double refunding = (Double.parseDouble(transaction.total_price) - Double.parseDouble(transaction.charity_price) - Double.parseDouble(transaction.tip));
        if (!transaction.isRefundPossible) {
            btn_refund.setBackgroundColor(getResources().getColor(R.color.grey));
            btn_refund.setTextColor(getResources().getColor(R.color.black));
            btn_refund.setEnabled(false);
            btn_refund.setEnabled(false);
        }
        if (Double.parseDouble(transaction.refunded_already) > 0) {
            TextView txt_you_refunded = rowView.findViewById(R.id.txt_you_refunded);
            txt_you_refunded.setText("$" + transaction.refunded_already);
        }

        ///////////

        finalCheckDialog.setContentView(rowView);
        finalCheckDialog.setCancelable(true);
        finalCheckDialog.setCanceledOnTouchOutside(true);
        finalCheckDialog.show();

    }

    //All selction and deselection of check box will be handled in below listner
    private void setCheckboxLisnter(final AdvTransactions.AdvTransaction transaction) {

         checkChange = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switch (compoundButton.getId()) {
                    case R.id.cb_token:
                        if (b) {
                            if (Double.parseDouble(feathers) >= Double.parseDouble(transaction.tokens_bought)) {
                                ref_token = transaction.tokens_bought;
                                calculateRefundTotal(transaction);

                            } else {
                                dialogAlertBoughtToken(transaction);
                            }
                        } else {
                            ref_token = "0.0";
                            txt_token_detail.setText("Refundable " + transaction.tokens_bought + " Store Credit @ $" + transaction.token_rate);
                            txt_token.setText("$" + String.format("%.2f", Double.parseDouble(transaction.tokens_bought) * Double.parseDouble(transaction.token_rate)));
                            calculateRefundTotal(transaction);

                        }


                        break;
                    case R.id.cb_vp_token:
                        if (b) {
                            if (Double.parseDouble(feathers) >= Double.parseDouble(transaction.vp_tokens)) {
                                ref_vp_token = transaction.vp_tokens;
                                calculateRefundTotal(transaction);
                            } else {
                                dialogAlertVPToken(transaction);
                            }
                        } else {
                            ref_vp_token = "0.0";
                            txt_vptoken_detail.setText("Refundable " + transaction.vp_tokens + " Store Credit @ $" + transaction.vp_rate);
                            txt_vptoken.setText("$" + transaction.vp_fee);
                            calculateRefundTotal(transaction);
                        }
                        break;
                    case R.id.cb_used_token:
                        calculateRefundTotal(transaction);
                        break;
                    case R.id.cb_charity:
                        calculateRefundTotal(transaction);
                        break;
                    case R.id.cb_tip:
                        calculateRefundTotal(transaction);
                        break;
                }


            }

        };

    }

    //dialog will be shown when customer have not enough vp tokens to refund than he bought
    private void dialogAlertVPToken( final AdvTransactions.AdvTransaction transaction) {

        final Dialog pinDialog = new Dialog(AdvTransactionListActivity.this, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.dialog_custom_refund_token, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        pinDialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        final TypefacedEditText input = rowView.findViewById(R.id.edt_pin);
        TypefacedTextView btn_ok = rowView.findViewById(R.id.btn_ok);
        TypefacedTextView msg = rowView.findViewById(R.id.msg);
        msg.setText(getResources().getString(R.string.less_feathers) +
                "\n\n We can refund only : "
                + feathers + "token/s\n\nFor how many remaining tokens," +
                " the customer want to be refunded?");
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Long ref_token = Math.round(Double.parseDouble(feathers) - Double.parseDouble(input.getText().toString()));
                if (ref_token >= 0) {
                    pinDialog.dismiss();
                    feathers = "" + Math.round(ref_token);
                    ref_vp_token = input.getText().toString();
                    txt_vptoken_detail.setText("Refundable " + input.getText().toString() + " Store Credit @ $" + transaction.vp_rate);
                    txt_vptoken.setText("$" + String.format("%.2f", Double.parseDouble(input.getText().toString()) * Double.parseDouble(transaction.vp_rate)));
                    calculateRefundTotal(transaction);
                } else {
                    ref_vp_token = "0.0";
                    cb_vp_token.setChecked(false);
                    calculateRefundTotal(transaction);
                    Toast.makeText(AdvTransactionListActivity.this, getResources().getString(R.string.invalid), Toast.LENGTH_LONG).show();
                }
            }
        });
        TypefacedTextView btn_cancel = rowView.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinDialog.dismiss();
                ref_vp_token = "0.0";
                txt_vptoken_detail.setText("Refundable " + transaction.vp_tokens + " Store Credit @ $" + transaction.token_rate);
                txt_vptoken.setText("$" + String.format("%.2f", Double.parseDouble(transaction.tokens_bought) * Double.parseDouble(transaction.token_rate)));
                cb_vp_token.setChecked(false);
                calculateRefundTotal(transaction);

            }
        });
        LinearLayout ly_pay = rowView.findViewById(R.id.ly_pay);
        ly_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinDialog.dismiss();
            }
        });
        pinDialog.setContentView(rowView);
        pinDialog.setCancelable(true);
        pinDialog.setCanceledOnTouchOutside(true);
        pinDialog.show();
    }

    //dialog will be shown when customer have not enough tokens to refund than he bought
    private void dialogAlertBoughtToken(final AdvTransactions.AdvTransaction transaction) {
        final Dialog alertDialog = new Dialog(AdvTransactionListActivity.this, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.dialog_custom_refund_token, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        final TypefacedEditText input = rowView.findViewById(R.id.edt_pin);
        TypefacedTextView btn_ok = rowView.findViewById(R.id.btn_ok);
        TypefacedTextView msg = rowView.findViewById(R.id.msg);
        msg.setText(getResources().getString(R.string.less_feathers) +
                "\n\n We can refund only : " +
                feathers + "token/s\n\nFor how many remaining tokens, " +
                "the customer want to be refunded?");
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Long total_token = Math.round(Double.parseDouble(feathers) - Double.parseDouble(input.getText().toString()));
                if (total_token >= 0) {
                    alertDialog.dismiss();
                    feathers = "" + Math.round(total_token);
                    ref_token = input.getText().toString();
                    txt_token_detail.setText("Refundable " + input.getText().toString() + " Store Credit @ $" + transaction.token_rate);
                    txt_token.setText("$" + String.format("%.2f", Double.parseDouble(input.getText().toString()) * Double.parseDouble(transaction.token_rate)));
                    calculateRefundTotal(transaction);
                } else {
                    ref_token = "0.0";
                    cb_token.setChecked(false);
                    calculateRefundTotal(transaction);
                    Toast.makeText(AdvTransactionListActivity.this, getResources().getString(R.string.invalid), Toast.LENGTH_LONG).show();
                }
            }
        });
        TypefacedTextView btn_cancel = rowView.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref_token = "0.0";

                txt_token_detail.setText("Refundable " + transaction.tokens_bought + " Store Credit @ $" + transaction.token_rate);
                txt_token.setText("$" + String.format("%.2f", Double.parseDouble(transaction.tokens_bought) * Double.parseDouble(transaction.token_rate)));
                cb_token.setChecked(false);
                calculateRefundTotal(transaction);
            }
        });
        LinearLayout ly_pay = rowView.findViewById(R.id.ly_pay);
        ly_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setContentView(rowView);
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

     //Fuction for making total of refund amount
    public void calculateRefundTotal(AdvTransactions.AdvTransaction transaction) {
        Double total = 0.00;
        for (int i = 0; i < cb_orginal.length; i++) {
            if (cb_orginal[i] == 1) {
                total = total +
                        (Double.parseDouble(transaction.products.get(i).sale_price) * Integer.parseInt(transaction.products.get(i).purchased_qty));
            }
        }
        if (cb_token.isChecked()) {
            if (!transaction.pay_method.equalsIgnoreCase(Constants.PAYMETHOD_EXCHANGE))
                total = total + Double.parseDouble(txt_token.getText().toString().replace("$", "").replace(" ", "").replace("null", "0.0").replace("Tokens", ""));

        }
        if (cb_vp_token.isChecked()) {
            if (!transaction.pay_method.equalsIgnoreCase(Constants.PAYMETHOD_EXCHANGE))
                total = total + Double.parseDouble(txt_vptoken.getText().toString().replace("$", "").replace(" ", "").replace("null", "0.0"));

        }
        if (cb_used_token.isChecked()) {
            total = total - Double.parseDouble(txt_store_credit.getText().toString().replace("$", "").replace("-", "").replace("null", "0.0"));
            if (total < 0) {
                total = total + Double.parseDouble(txt_store_credit.getText().toString().replace("$", "").replace("-", "").replace("null", "0.0"));
            }
        }
        if (cb_tip.isChecked()) {

            total = total + Double.parseDouble(txt_tip_amount.getText().toString().replace("$", "").replace("-", "").replace("null", "0.0"));

        }
        if (cb_charity.isChecked()) {
            total = total + Double.parseDouble(txt_charity_amount.getText().toString().replace("$", "").replace("-", "").replace("null", "0.0"));

        }
        if (!transaction.pay_method.equalsIgnoreCase(Constants.PAYMETHOD_EXCHANGE))
            txt_you_refund.setText("$" + String.format("%.2f", total));
        else {
            Double total_tokan = 0.0;
            for (int i = 0; i < cb_orginal.length; i++) {
                if (cb_orginal[i] == 1) {
                    total_tokan = total_tokan + (Double.parseDouble(transaction.products.get(i).sale_price) * Integer.parseInt(transaction.products.get(i).purchased_qty));
                }
            }
            txt_you_refund.setText("" + Math.round(total_tokan) + " Tokens");
        }
    }

}
