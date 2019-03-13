package com.yagna.petra.app.Cashier;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yagna.petra.app.GlobalActivity;
import com.yagna.petra.app.Model.Cashiers;
import com.yagna.petra.app.Model.Transaction;
import com.yagna.petra.app.Model.Transactions;
import com.yagna.petra.app.Payment.PaymentActivity;
import com.yagna.petra.app.R;
import com.yagna.petra.app.SplashActivity;
import com.yagna.petra.app.Util.Constants;
import com.yagna.petra.app.Util.CustomDialog;
import com.yagna.petra.app.Util.PrefUtil;
import com.yagna.petra.app.Util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CashierActivity extends GlobalActivity {

    private SharedPreferences common_mypref;

    private Utils util;
    public TextView edt_Amount;
    private CustomDialog cd;
    private RecyclerView rl_today_list;
    private ArrayList<Transaction> transaction;
    private TextView no_transaction;
    private Button btn_enter;
    private Dialog finalCheckDialog;
    private CardView logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
         if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
         }
        super.onCreate(savedInstanceState);

        common_mypref = getApplicationContext().getSharedPreferences("user", 0);
        setContentView(R.layout.activity_cashier);
        util=new Utils(this);
        cd = new CustomDialog(this);
        GridView gv_numpad = findViewById(R.id.gv_numpad);
        NumPadAdapter numAdapter = new NumPadAdapter(this);
        gv_numpad.setAdapter(numAdapter);

        btn_enter=findViewById(R.id.btn_enter);
        btn_enter.setEnabled(false);
        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTransactionApi();
            }
        });
        Button btn_clear = findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_Amount.setText("");
            }
        });
        edt_Amount = findViewById(R.id.edt_pin);
        edt_Amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!(edt_Amount.getText().toString().replace("$","").trim().length()>0)){
                    btn_enter.setEnabled(false);
                    btn_enter.setBackgroundColor(getResources().getColor(R.color.dark_grey));
                }
                else if(!(Double.parseDouble(edt_Amount.getText().toString().replace("$","").trim())>0)){

                    btn_enter.setEnabled(false);
                    btn_enter.setBackgroundColor(getResources().getColor(R.color.dark_grey));
                }
                else
                {
                    btn_enter.setEnabled(true);
                    btn_enter.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        String cash_name="";
        try {
            JSONObject jobj = new JSONObject(PrefUtil.getLoginData(common_mypref));
            cash_name=jobj.getJSONObject("cashier").getString("name");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toolbar toolbar =  findViewById(R.id.toolbar);

        Spannable text = new SpannableStringBuilder("Hello, "+cash_name);
        util.setSpans(text, "Hello, ".length(),"Hello, ".length()+cash_name.length());

        toolbar.setTitle( text);
        setSupportActionBar(toolbar);

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrefUtil.saveLoginData(common_mypref,"");
                Intent intent = new Intent(CashierActivity.this,SplashActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void showTodayData() {
        if(transaction.size()==0){
            rl_today_list.setVisibility(View.GONE);
            no_transaction.setVisibility(View.VISIBLE);
        }
        else{
            rl_today_list.setVisibility(View.VISIBLE);
            no_transaction.setVisibility(View.GONE);
            TodayTransactionAdpter mAdapter = new TodayTransactionAdpter(this, transaction);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            rl_today_list.setLayoutManager(mLayoutManager);
            rl_today_list.setAdapter(mAdapter);
        }
    }

    private void loadTrans() {
        if (util.checkInternetConnection()) {
            JSONObject jar = new JSONObject();
            cd.show("");
            Log.e("loadTrans", Constants.transactionsUrl);
            Log.e("loadTrans", ""+jar);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.transactionsUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            cd.hide();
                            Log.e("loadTrans", ""+response);
                            try {
                                transaction=(new Transactions(new JSONObject(response).getJSONObject("data").getJSONArray("transactions"))).array;
                                showTodayTransaction();
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
                            Toast.makeText(CashierActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                    @Override
                    protected Map<String, String> getParams() {
                        String cash_id="";
                        try {
                            JSONObject jobj = new JSONObject(PrefUtil.getLoginData(common_mypref));
                            cash_id= new Cashiers(jobj.getJSONObject("cashier")).cashier_id;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Map<String, String> params = new HashMap<>();
                        params.put("load","transactions");
                        params.put("cashierid",cash_id);


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
                public void retry(VolleyError error) {
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }else {
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.dataconnection),Toast.LENGTH_LONG).show();
        }
    }

    private void startTransactionApi() {

        if (util.checkInternetConnection()) {
            String merchantid="",cashierid="",locationid="";
            try {
                JSONObject jobj = new JSONObject(PrefUtil.getLoginData(common_mypref));
                Cashiers cashier = new Cashiers(jobj.getJSONObject("cashier"));
                merchantid=cashier.merchant_id;
                cashierid=cashier.cashier_id;
                locationid=cashier.merchant_location_id;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            cd.show("");
            Log.e("startTransactionApi", Constants.transactionsUrl);
            final String finalMerchantid = merchantid;
            final String finalCashierid = cashierid;
            final String finalLocationid = locationid;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.transactionsUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            cd.hide();
                            Log.e("startTransactionApi", ""+response);
                            try {
                                JSONObject deviceData=new JSONObject(response);
                                Intent intent = new Intent(CashierActivity.this,PaymentActivity.class);
                                intent.putExtra("data",""+deviceData);
                                intent.putExtra("price",""+ edt_Amount.getText().toString().trim().replace("$",""));
                                intent.putExtra("merchantid",""+finalMerchantid);
                                intent.putExtra("locationid",""+finalLocationid);
                                startActivity(intent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            cd.hide();
                            Toast.makeText(CashierActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();

                    params.put("transaction","start");
                    params.put("price",""+ edt_Amount.getText().toString().replace("$",""));
                    params.put("merchantid", finalMerchantid);
                    params.put("cashierid", finalCashierid);
                    params.put("transactiontype","Store");
                    params.put("locationid", finalLocationid);



                    Log.e("getParams", String.valueOf(params));
                    return params;
                }
                @Override
                public Map<String, String> getHeaders(){
                    Map<String, String> params = new HashMap< >();
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

    private void showTodayTransaction() {
        finalCheckDialog = new Dialog(CashierActivity.this, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View rowView =  inflater.inflate(R.layout.dialog_today_transaction, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        Objects.requireNonNull(finalCheckDialog.getWindow()).setBackgroundDrawableResource(R.color.translucent_black);
        rl_today_list = rowView.findViewById(R.id.rl_today_list);
        no_transaction= rowView.findViewById(R.id.no_transaction);
        LinearLayout ly_dialog_main = rowView.findViewById(R.id.ly_dialog_main);
        ly_dialog_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalCheckDialog.dismiss();
            }
        });
        TextView btn_close = rowView.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalCheckDialog.dismiss();
            }
        });
        showTodayData();
        finalCheckDialog.setContentView(rowView);
        finalCheckDialog.setCancelable(true);
        finalCheckDialog.setCanceledOnTouchOutside(true);
        finalCheckDialog.show();
    }




    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
     //   getMenuInflater().inflate(R.menu.admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       /* switch (item.getItemId()){
            case R.id.action_logout :
                PrefUtil.saveLoginData(common_mypref,"");
                Intent intent = new Intent(AdvCashierActivity.this,LogInActivity.class);
                startActivity(intent);
                finish();
                break;
        }*/

        return super.onOptionsItemSelected(item);
    }


    public void returnAmount(View view) {
        if(view.getId()==R.id.btn_do_return) {
            Intent intent = new Intent(CashierActivity.this, TransactionListActivity.class);
            intent.putExtra("type","refund");
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(CashierActivity.this, TransactionListActivity.class);
            intent.putExtra("type","void");
            startActivity(intent);
        }

    }
}
