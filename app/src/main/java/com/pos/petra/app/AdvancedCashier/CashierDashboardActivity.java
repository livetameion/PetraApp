package com.pos.petra.app.AdvancedCashier;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.pos.petra.app.Customer.CustomerActivity;
import com.pos.petra.app.AdvancedCashier.BrowseAndSearch.SearchActivity;
import com.pos.petra.app.AdvancedTransaction.AdvTransactionListActivity;
import com.pos.petra.app.GlobalActivity;
import com.pos.petra.app.Model.Roles.Cashiers;
import com.pos.petra.app.Payment.AdvPaymentActivity;
import com.pos.petra.app.R;
import com.pos.petra.app.SplashActivity;
import com.pos.petra.app.Util.Constants;
import com.pos.petra.app.Util.CustomDialog;
import com.pos.petra.app.Util.PrefUtil;
import com.pos.petra.app.Util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CashierDashboardActivity extends GlobalActivity implements NavigationView.OnNavigationItemSelectedListener {

    public int position;
    private SharedPreferences common_mypref;
    private Utils util;
    public TextView edt_total_amount;
    private CustomDialog prodgressbar;
    private Button btn_enter;
    private CardView logout;
    private FloatingActionButton fab_add, fab_add_center;
    public RecyclerView list_products;
    private RecyclerView.LayoutManager storiesLayoutManager;
    public TextView tv_no_record_;
    private DashProductAdapter adapter;
    private TextView btn_proceed;
    private double total_amount;
    private TextView nav_header_text;
    private TextView nav_subheader_text;
    private LinearLayout ly_subtotal;
    private TextView txt_subtotal;
    private LinearLayout ly_no_records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        super.onCreate(savedInstanceState);
        common_mypref = getApplicationContext().getSharedPreferences("user", 0);
        setContentView(R.layout.activity_main);
        util = new Utils(this);
        prodgressbar = new CustomDialog(this);
        list_products = findViewById(R.id.gv_numpad);
        tv_no_record_ = findViewById(R.id.tv_no_record_);
        ly_no_records = findViewById(R.id.ly_no_records);

        fab_add =  findViewById(R.id.fab_add);
        fab_add_center =  findViewById(R.id.fab_add_center);
        btn_proceed = findViewById(R.id.btn_proceed);
        ly_subtotal = findViewById(R.id.ly_subtotal);
        btn_enter = findViewById(R.id.btn_enter);
        btn_enter.setEnabled(false);
        txt_subtotal = findViewById(R.id.txt_subtotal);
        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTransactionApi();
            }
        });

        edt_total_amount = findViewById(R.id.edt_pin);
        edt_total_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!(edt_total_amount.getText().toString().replace("$", "").trim().length() > 0)) {
                    btn_enter.setEnabled(false);
                    btn_enter.setBackgroundColor(getResources().getColor(R.color.dark_grey));
                } else if (!(Double.parseDouble(edt_total_amount.getText().toString().replace("$", "").trim()) > 0)) {

                    btn_enter.setEnabled(false);
                    btn_enter.setBackgroundColor(getResources().getColor(R.color.dark_grey));
                } else {
                    btn_enter.setEnabled(true);
                    btn_enter.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        String cash_name = "", cash_id = "";
        try {
            JSONObject jobj = new JSONObject(PrefUtil.getLoginData(common_mypref));
            cash_name = jobj.getJSONObject("cashier").getString("name");
            cash_id = jobj.getJSONObject("cashier").getString("email");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrefUtil.saveLoginData(common_mypref, "");
                Intent intent = new Intent(CashierDashboardActivity.this, SplashActivity.class);
                startActivity(intent);
                finish();
            }
        });

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CashierDashboardActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        fab_add_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CashierDashboardActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        storiesLayoutManager = createLayoutManager();
        list_products.setLayoutManager(storiesLayoutManager);
        Spannable text = new SpannableStringBuilder("Hello, " + cash_name);
        util.setSpans(text, "Hello, ".length(), "Hello, ".length() + cash_name.length());
        nav_header_text = view.findViewById(R.id.nav_header_text);
        nav_header_text.setText(text);
        nav_subheader_text = view.findViewById(R.id.nav_subheader_text);
        nav_subheader_text.setText(cash_id);


    }

    private RecyclerView.LayoutManager createLayoutManager() {
        int spans = 1;/*resources.getInteger(R.integer.feed_columns);*/
        return new StaggeredGridLayoutManager(spans, RecyclerView.VERTICAL);
    }


    private void startTransactionApi() {

        if (util.checkInternetConnection()) {
            String merchantid = "", cashierid = "", locationid = "";
            try {
                JSONObject jobj = new JSONObject(PrefUtil.getLoginData(common_mypref));
                Cashiers cashier = new Cashiers(jobj.getJSONObject("cashier"));
                merchantid = cashier.merchant_id;
                cashierid = cashier.cashier_id;
                locationid = cashier.merchant_location_id;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            prodgressbar.show("");
            Log.e("startTransactionApi", Constants.transactionsUrl);
            final String finalMerchantid = merchantid;
            final String finalCashierid = cashierid;
            final String finalLocationid = locationid;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.transactionsUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            prodgressbar.hide();
                            Log.e("startTransactionApi", "" + response);
                            try {
                                JSONObject deviceData = new JSONObject(response);
                                Intent intent = new Intent(CashierDashboardActivity.this, AdvPaymentActivity.class);
                                intent.putExtra("data", "" + deviceData);
                                intent.putExtra("price", "" + edt_total_amount.getText().toString().trim().replace("$", ""));
                                intent.putExtra("merchantid", "" + finalMerchantid);
                                intent.putExtra("locationid", "" + finalLocationid);
                                startActivity(intent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            prodgressbar.hide();
                            Toast.makeText(CashierDashboardActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();

                    params.put("transaction", "start");
                    params.put("price", "" + edt_total_amount.getText().toString().replace("$", ""));
                    params.put("merchantid", finalMerchantid);
                    params.put("cashierid", finalCashierid);
                    params.put("transactiontype", "Store");
                    params.put("locationid", finalLocationid);

                    JSONArray jaar = new JSONArray();
                    for (int i = 0; i < DataKeeper.products_array.size(); i++) {
                        JSONObject jobj = new JSONObject();
                        try {
                            jobj.put("product_id", DataKeeper.products_array.get(i).id);
                            Date date = new Date();
                            long timeMilli = date.getTime();
                            jobj.put("order_id", i + "-" + timeMilli);
                            jobj.put("qty", DataKeeper.products_array.get(i).qty);
                            jobj.put("original_price", DataKeeper.products_array.get(i).price);
                            jobj.put("product_price", DataKeeper.products_array.get(i).price_applied);
                            if (DataKeeper.products_array.get(i).selected_modifier.length() != 0)
                                jobj.put("modifier_id", DataKeeper.products_array.get(i).modifier.get(Integer.parseInt(DataKeeper.products_array.get(i).selected_modifier)).id);
                            if (DataKeeper.products_array.get(i).selected_discount.length() != 0)
                                jobj.put("discount_id", DataKeeper.products_array.get(i).discount.get(Integer.parseInt(DataKeeper.products_array.get(i).selected_discount)).id);
                            if (DataKeeper.products_array.get(i).selected_discount.length() != 0)
                                jobj.put("discount_rate", DataKeeper.products_array.get(i).discount.get(Integer.parseInt(DataKeeper.products_array.get(i).selected_discount)).subtitle);
                            jobj.put("charity_id", "1" + "/" + i + "-" + timeMilli);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        jaar.put(jobj);
                    }
                    params.put("products", jaar.toString());

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

    @Override
    protected void onResume() {
        super.onResume();
        refilCart();
        countTotal();
    }

    @SuppressLint("RestrictedApi")
    private void refilCart() {
        if (DataKeeper.products_array.size() > 0) {
            ly_subtotal.setVisibility(View.VISIBLE);
            btn_proceed.setVisibility(View.VISIBLE);
            txt_subtotal.setText("Subtotal  :\n(" + DataKeeper.products_array.size() + (DataKeeper.products_array.size() == 1 ? " item)" : " items)  "));
            if (adapter == null) {
                list_products.setAdapter(adapter = new DashProductAdapter(CashierDashboardActivity.this, DataKeeper.products_array));
            } else {
                adapter.notifyDataSetChanged();
            }
            list_products.setVisibility(View.VISIBLE);
            ly_no_records.setVisibility(View.GONE);
            fab_add.setVisibility(View.VISIBLE);
        } else {
            ly_subtotal.setVisibility(View.GONE);
            btn_proceed.setVisibility(View.GONE);
            list_products.setVisibility(View.GONE);
            ly_no_records.setVisibility(View.VISIBLE);
            fab_add.setVisibility(View.GONE);
        }
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

        return super.onOptionsItemSelected(item);
    }

    public void myClickMethod(final View v) {
        // /Function purpose to Perform ClickEvent of Element Based on view
        // /PARAM 1.v=view of the clicked Element....
        switch (v.getId()) {

            case R.id.btn_ok:
                util.ButtonClickEffect(v);
                prodgressbar.hide();
                DataKeeper.products_array.remove(position);
                DataKeeper.product_id_selected.remove(position);
                DataKeeper.product_name_selected.remove(position);
                countTotal();
                refilCart();
                adapter.notifyDataSetChanged();
                if (adapter != null)
                    adapter.cd.hide();
                prodgressbar.hide();
                break;

            case R.id.btn_cancel:
                util.ButtonClickEffect(v);
                prodgressbar.hide();
                if (adapter != null)
                    adapter.cd.hide();
                break;
        }
    }


    //Total count of added porducts and refresh a view
    public void countTotal() {
        total_amount = 0.0;
        String txt = "";
        for (int i = 0; i < DataKeeper.products_array.size(); i++) {
            total_amount = total_amount + DataKeeper.products_array.get(i).price_applied;
            String sub_txt = "$" + String.format("%.2f", DataKeeper.products_array.get(i).price_applied) + " ($" + String.format("%.2f", DataKeeper.products_array.get(i).new_price) + "X" + DataKeeper.products_array.get(i).qty + ")";
            txt = txt.length() == 0 ? "  " + sub_txt : txt + "\n+" + sub_txt;
        }

        edt_total_amount.setText("$" + String.format("%.2f", total_amount));

        try {

            ((TextView) findViewById(R.id.txt_summation)).setText(txt);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_transaction) {
            Intent intent = new Intent(CashierDashboardActivity.this, AdvTransactionListActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_customers) {
            Intent intent = new Intent(CashierDashboardActivity.this, CustomerActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
