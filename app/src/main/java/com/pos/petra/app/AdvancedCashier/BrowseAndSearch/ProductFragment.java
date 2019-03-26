package com.pos.petra.app.AdvancedCashier.BrowseAndSearch;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.pos.petra.app.AdvancedCashier.CashierDashboardActivity;
import com.pos.petra.app.AdvancedCashier.DataKeeper;
import com.pos.petra.app.Model.Product.Products;
import com.pos.petra.app.R;
import com.pos.petra.app.Util.Constants;
import com.pos.petra.app.Util.CustomDialog;
import com.pos.petra.app.Util.TouchImageView;
import com.pos.petra.app.Util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductFragment extends Fragment {
    public static CustomDialog cd;
    public ArrayList<Bitmap> bitmapArray;
    public RecyclerView listview;
    public TextView tv_no_record;
    private SearchProductAdapter adapter;
    private SharedPreferences common_mypref;
    private JSONObject returnedData;
    private Utils util;
    private Context context;
    public static int position;
    public ArrayList<String[]> imageData;
    static String productid = null;
    SimpleDateFormat sdf;
    private RecyclerView.LayoutManager storiesLayoutManager;
    private FloatingActionButton fab_cart;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.c_activity_product, container, false);
            context = getActivity();
            common_mypref = getActivity().getSharedPreferences(
                    "user", 0);
            util = new Utils(context);
            tv_no_record = rootView.findViewById(R.id.tv_no_record_);
            cd = new CustomDialog(context);
            tv_no_record = rootView.findViewById(R.id.tv_no_record_);
            bitmapArray = new ArrayList<>();
            imageData = new ArrayList<>();
            listview = rootView.findViewById(R.id.listview);
            storiesLayoutManager = createLayoutManager(getResources());
            listview.setLayoutManager(storiesLayoutManager);
            listview.setAdapter(adapter = new SearchProductAdapter(this, DataKeeper.search_product_array));

            fab_cart = rootView.findViewById(R.id.fab_cart);
            fab_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), CashierDashboardActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(i);
                }
            });
            sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        }
        return rootView;

    }

    private RecyclerView.LayoutManager createLayoutManager(Resources resources) {
        int spans = resources.getInteger(R.integer.product_columns);
        return new StaggeredGridLayoutManager(spans, RecyclerView.VERTICAL);
    }

    public void fillData(String i, boolean type_saved) {
        Log.e("called by", "" + i);
        if (type_saved) {
            adapter.notifyDataSetChanged();
            if (DataKeeper.search_product_array.size() != 0) {

                listview.setVisibility(View.VISIBLE);
                tv_no_record.setVisibility(View.GONE);
            } else {
                listview.setVisibility(View.GONE);
                tv_no_record.setVisibility(View.VISIBLE);
            }

        } else if (i != null) {
            setUrlPayload(Constants.GETLIST, i);
            listview.setVisibility(View.VISIBLE);
            tv_no_record.setVisibility(View.GONE);
        } else {

            tv_no_record.setVisibility(View.VISIBLE);
            listview.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setUrlPayload(int category, String extra) {
        switch (category) {

            case Constants.GETLIST:
                JSONObject j_obj = new JSONObject();
                try {
                    j_obj.put("category_id", extra);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                apiWebrequest(Constants.ProductAPI, j_obj, Constants.GETLIST, Constants.VIEW);
                break;


        }
    }

    public void setReturnedData(int category, JSONObject j_obj) {
        switch (category) {
            case Constants.GETLIST:
                try {
                    returnedData = j_obj;
                    JSONArray re_j_obj = returnedData.getJSONArray("products");
                    DataKeeper.search_product_array.clear();
                    ArrayList<Products.Product> json = (new Products(re_j_obj)).products;
                    for (int i = 0; i < json.size(); i++) {
                        DataKeeper.search_product_array.add(json.get(i));
                    }
                    adapter.notifyDataSetChanged();
                    if (DataKeeper.search_product_array.size() != 0) {

                        listview.setVisibility(View.VISIBLE);
                        tv_no_record.setVisibility(View.GONE);
                    } else {

                        listview.setVisibility(View.GONE);
                        tv_no_record.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
    }


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
                                    if (!(new JSONObject(response)).isNull("data"))
                                        returnJSON = (new JSONObject(response)).getJSONObject("data");

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
                            Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
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
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        } else {
            util.customToast(getResources().getString(R.string.nointernet));

        }
    }


    public void callFreeSearchApi(final String q, final String locationid) {


        if (util.checkInternetConnection()) {

            cd.show("");
            Log.e("search", Constants.searchUrl);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.searchUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            cd.hide();
                            Log.e("search", "" + response);
                            try {

                                JSONObject deviceData = new JSONObject(response);
                                JSONArray products = deviceData.getJSONArray("products");
                                setReturnedData(Constants.GETLIST, deviceData);

                                DataKeeper.search_product_array.clear();
                                ArrayList<Products.Product> json = (new Products(products)).products;
                                for (int i = 0; i < json.size(); i++) {
                                    DataKeeper.search_product_array.add(json.get(i));
                                }
                                adapter.notifyDataSetChanged();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            cd.hide();
                            Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();

                    params.put("q", "" + q.trim());
                    params.put("merchant_location_id", locationid);
                    params.put("action", "search");

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
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);

        } else {
            Toast.makeText(context, getResources().getString(R.string.dataconnection), Toast.LENGTH_LONG).show();
        }


    }


    public void myClickMethod(final View v) {

        switch (v.getId()) {
            case R.id.btn_ok:
                util.ButtonClickEffect(v);
                cd.hide();
                setUrlPayload(Constants.DELETEITEM, "" + productid);
                cd.hide();
                break;

            case R.id.btn_cancel:
                util.ButtonClickEffect(v);
                cd.hide();

                break;
        }
    }


}
