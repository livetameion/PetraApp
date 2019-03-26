package com.pos.petra.app.AdvancedCashier.Discount;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pos.petra.app.AdvancedCashier.DataKeeper;
import com.pos.petra.app.GlobalActivity;
import com.pos.petra.app.Model.Product.Discount;
import com.pos.petra.app.R;
import com.pos.petra.app.Util.Constants;
import com.pos.petra.app.Util.CustomDialog;
import com.pos.petra.app.Util.PrefUtil;
import com.pos.petra.app.Util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DiscountActivity extends GlobalActivity {
    public ArrayList<Bitmap> bitmapArray;
    public RecyclerView listview;
    public TextView tv_no_record;
    private DiscountAdapter adapter;
    private SharedPreferences common_mypref;
    private JSONObject returnedData;
    private Utils util;
    private CustomDialog cd;
    private DiscountActivity context;
    public int position;
    SimpleDateFormat sdf;

    TextView txt_producttitle, product_nam, product_sku;
    private LinearLayout ly_prod_details;
    private String admin_id;
    private String merchant_location_id;
    private RecyclerView.LayoutManager storiesLayoutManager;
    private ArrayList<Discount> disc_array;
    public int posit_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = DiscountActivity.this;
        common_mypref = getApplicationContext().getSharedPreferences(
                "user", 0);
        util = new Utils(context);
        tv_no_record = findViewById(R.id.tv_no_record_);
        util = new Utils(context);
        cd = new CustomDialog(context);
        txt_producttitle = findViewById(R.id.txt_branchtitle);
        txt_producttitle.setText("Discount");
        tv_no_record = findViewById(R.id.tv_no_record_);
        ly_prod_details = findViewById(R.id.ly_prod_details);
        ly_prod_details.setVisibility(View.VISIBLE);
        product_sku = findViewById(R.id.product_sku);
        product_nam = findViewById(R.id.product_nam);
        try {
            JSONObject jobj = new JSONObject(PrefUtil.getLoginData(common_mypref));
            admin_id = jobj.getJSONObject("admin").getString("admin_id");
            merchant_location_id = jobj.getJSONObject("admin").getString("merchant_location_id");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        bitmapArray = new ArrayList<>();
        disc_array = new ArrayList<>();
        listview = findViewById(R.id.listview);
        storiesLayoutManager = createLayoutManager(getResources());
        listview.setLayoutManager(storiesLayoutManager);
        ((ImageView) findViewById(R.id.btn_add)).setVisibility(View.GONE);

        fillData(1);
        sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
      
    }

    private RecyclerView.LayoutManager createLayoutManager(Resources resources) {
        int spans = resources.getInteger(R.integer.feed_columns);
        return new StaggeredGridLayoutManager(spans, RecyclerView.VERTICAL);
    }

    private void fillData(int i) {
        Log.e("called by", "" + i);
        posit_id = Integer.parseInt(getIntent().getStringExtra("id"));
        String title = DataKeeper.products_array.get(posit_id).title;
        product_nam.setText(title);
        product_sku.setText(DataKeeper.products_array.get(posit_id).sku);
        disc_array = DataKeeper.products_array.get(posit_id).discount;
        listview.setAdapter(adapter = new DiscountAdapter(DiscountActivity.this, disc_array));
        if (disc_array.size() != 0) {
            txt_producttitle.setText("Discounts" + " (" + disc_array.size() + ")");
            listview.setVisibility(View.VISIBLE);
            tv_no_record.setVisibility(View.GONE);
        } else {
            txt_producttitle.setText("Discounts" + "(0)");
            listview.setVisibility(View.GONE);
            tv_no_record.setVisibility(View.VISIBLE);
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //  getMenuInflater().inflate( R.menu.menu_add, menu );
        //  MenuItem addOption = menu.findItem(R.id.add);
        //    addOption.setVisible(true);
        return true;
    }

    public void setUrlPayload(int category, String extra) {
        switch (category) {

            case Constants.GETLIST:
                JSONObject j_obj = new JSONObject();
                try {
                    j_obj.put("product_id", extra);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                apiWebrequest(Constants.DsicountAPI, j_obj, Constants.GETLIST, Constants.VIEW);
                break;


        }


    }

    //Set retrived data from the API

    public void setReturnedData(int category, JSONObject j_obj) {
        switch (category) {

            case Constants.GETLIST:
                Log.e("Apitransect", "" + j_obj);
                JSONArray re_j_obj;
                try {
                    returnedData = j_obj;
                    re_j_obj = returnedData.getJSONArray("data");

                    disc_array.clear();
                    for (int i = 0; i < re_j_obj.length(); i++) {
                        try {
                            disc_array.add(new Discount(re_j_obj.getJSONObject(i)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    adapter.notifyDataSetChanged();
                    if (re_j_obj.length() != 0) {
                        txt_producttitle.setText("Discounts" + " (" + re_j_obj.length() + ")");
                        listview.setVisibility(View.VISIBLE);
                        tv_no_record.setVisibility(View.GONE);
                    } else {
                        txt_producttitle.setText("Discounts" + "(0)");
                        listview.setVisibility(View.GONE);
                        tv_no_record.setVisibility(View.VISIBLE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case Constants.ADDITEM:
                Log.e("Apitransect", "" + j_obj);
                setUrlPayload(Constants.GETLIST, DataKeeper.products_array.get(posit_id).id);
                //fillData(2);
                break;
            case Constants.UPDATEITEM:
                Log.e("Apitransect", "" + j_obj);
                setUrlPayload(Constants.GETLIST, DataKeeper.products_array.get(posit_id).id);
                //fillData(3);
                break;
            case Constants.DELETEITEM:
                Log.e("Apitransect", "" + j_obj);
                setUrlPayload(Constants.GETLIST, DataKeeper.products_array.get(posit_id).id);
                break;
        }
    }

   /// API function to fetch the data from server
    public void apiWebrequest(String url, final JSONObject j_obj, final int action, final String action_text) {
        if (util.checkInternetConnection()) {
            cd.show("");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        public JSONObject data;

                        @Override
                        public void onResponse(String response) {
                            cd.hide();
                            Log.e("loadTrans", "" + response);
                            try {
                                if ((new JSONObject(response)).getString("status").equalsIgnoreCase("1")) {

                                    JSONObject returnJSON = new JSONObject(response);

                                    if (!(new JSONObject(response)).isNull("message"))
                                        util.customToast((new JSONObject(response)).getString("message"));


                                    setReturnedData(action, returnJSON);

                                } else {
                                    cd.hide();
                                    util.customToast("Failed");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            cd.hide();
                            Toast.makeText(DiscountActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("data", j_obj.toString());
                    params.put("action", action_text);
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
            RequestQueue requestQueue = Volley.newRequestQueue(DiscountActivity.this);
            requestQueue.add(stringRequest);
        } else {
            util.customToast(getResources().getString(R.string.nointernet));

        }
    }


    //handle the clcik event of the dialogs
    public void myClickMethod(final View v) {

        switch (v.getId()) {

            case R.id.btn_ok:
                util.ButtonClickEffect(v);
                cd.hide();
                if (adapter != null)
                    adapter.cd.hide();
                cd.hide();
                break;

            case R.id.btn_cancel:
                util.ButtonClickEffect(v);
                cd.hide();
                if (adapter != null)
                    adapter.cd.hide();
                break;
        }
    }


}
