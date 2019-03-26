package com.pos.petra.app.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.pos.petra.app.R;

import com.pos.petra.app.SplashActivity;
import com.pos.petra.app.Util.Constants;
import com.pos.petra.app.Util.CustomDialog;
import com.pos.petra.app.Util.PrefUtil;
import com.pos.petra.app.Util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class AdminListActivity extends GlobalActivity {
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    public boolean mTwoPane;
    private SharedPreferences common_mypref;
    private Utils util;
    private CustomDialog cd;
    ArrayList<String> arr = new ArrayList<>();
    public ArrayList<String> click_status = new ArrayList<>();

    private RecyclerView recyclerView;
    public static JSONArray transaction=new JSONArray();
    public static String merchant_location_id;
    public static String merchant_id;
    private AdminTabRecyclerAdapter tabAdpter;
    private TextView store_credit;
    private TextView store_revenu;
    private DecimalFormat fc;
    private CardView logout;
    public DrawerLayout drawer;
    private NavigationView navigationView;
    private ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //if device is with screen normal size
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mTwoPane = false;

        }
        //if Device is with big screen as tablet
        else{
            mTwoPane = true;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        common_mypref = getApplicationContext().getSharedPreferences("user", 0);
        util=new Utils(this);
        cd = new CustomDialog(this);
        fc = new DecimalFormat("#,###,###,###,##0.00");
        String cash_name="";
        try {
            JSONObject jobj = new JSONObject(PrefUtil.getLoginData(common_mypref));
            cash_name=jobj.getJSONObject("admin").getString("admin_name");
            merchant_location_id =jobj.getJSONObject("admin").getString("merchant_location_id");
            merchant_id =jobj.getJSONObject("admin").getString("merchant_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Spannable text = new SpannableStringBuilder("Hello, "+cash_name);
        util.setSpans(text, "Hello, ".length(),"Hello, ".length()+cash_name.length());
        toolbar.setTitle( text);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (mTwoPane) {

            recyclerView = (RecyclerView) findViewById(R.id.item_list);
        }
        else
        {
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            recyclerView = (RecyclerView) navigationView.findViewById(R.id.item_list);
            btn_back = (ImageView) navigationView.findViewById(R.id.btn_back);
            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawer.closeDrawer(Gravity.LEFT);
                }
            });
        }
        store_credit= (TextView)findViewById(R.id.store_credit);
        store_revenu= (TextView)findViewById(R.id.store_revenu);
        MerchantData();
        addIems();
        assert recyclerView != null;
        setupDrawerRecyclerView((RecyclerView) recyclerView);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                   recyclerView.findViewHolderForAdapterPosition(0).itemView.performClick();
            }
        },200);
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrefUtil.saveLoginData(common_mypref,"");
                Intent intent = new Intent(AdminListActivity.this,SplashActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    //Adding Left Drawer's List item
    private void addIems() {
        arr.add("Transaction");
        arr.add("Users");
        arr.add("Wallet");
        arr.add("Value Calculations");
        arr.add("Store Information");
        arr.add("Product Management");
        arr.add("Charity Management");


        if(getResources().getBoolean(R.bool.portrait_only))
            click_status.add("0");
        else
            click_status.add("1");
        click_status.add("0");
        click_status.add("0");
        click_status.add("0");
        click_status.add("0");
        click_status.add("0");
        click_status.add("0");
    }


    private void setupDrawerRecyclerView(@NonNull final RecyclerView recyclerView) {
        tabAdpter= new AdminTabRecyclerAdapter(this, arr, click_status, mTwoPane,this);
        recyclerView.setAdapter(tabAdpter);
    }

    //API call to load Merchant's Information
    private void MerchantData() {
        if (util.checkInternetConnection()) {
            JSONObject jar = new JSONObject();
            //cd.show("");
            Log.e("MerchantData", Constants.merchantDataUrl);
            //  Log.e("loadTrans", ""+jar);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.merchantDataUrl,
                    new Response.Listener<String>() {
                        public JSONObject data;

                        @Override
                        public void onResponse(String response) {
                            //cd.hide();
                            Log.e("MerchantData", ""+response);
                            try {
                                data =(new JSONObject(response)).getJSONObject("data");
                                store_credit.setText(""+fc.format(Math.round(Double.parseDouble(data.getString("outstanding_tokens")))));

                                store_revenu.setText("$"+fc.format(Double.parseDouble(data.getString("store_revenue"))));

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
                            Toast.makeText(AdminListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("locationid",""+AdminListActivity.merchant_location_id);
                    params.put("merchantid",""+AdminListActivity.merchant_id);
                    params.put("action","getstats");
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
            RequestQueue requestQueue = Volley.newRequestQueue(AdminListActivity.this);
            requestQueue.add(stringRequest);

        }else {
            Toast.makeText(AdminListActivity.this,getResources().getString(R.string.dataconnection),Toast.LENGTH_LONG).show();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }


}
