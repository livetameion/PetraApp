package com.pos.petra.app.Customer;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pos.petra.app.Admin.AdminListActivity;
import com.pos.petra.app.GlobalActivity;
import com.pos.petra.app.R;
import com.pos.petra.app.Util.Constants;
import com.pos.petra.app.Util.CustomDialog;
import com.pos.petra.app.Util.Utils;
import com.pos.petra.app.Views.TypefacedTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//This Activity is for managing the customer
public class CustomerActivity extends GlobalActivity {
    private ListView listView;
    private ImageView txt_previous;
    private ImageView txt_next;
    public int TOTAL_LIST_ITEMS;
    public int NUM_ITEMS_PAGE;
    private CustomerListAdapter listAdapter;
    private Utils util;
    private CustomDialog cd;
    private JSONArray customers;
    private TypefacedTextView txt_page_no;
    private int page_no = 1;
    private int TOTAL_PAGE=0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_coustomer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Customers");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        util = new Utils(this);
        cd = new CustomDialog(this);
        listView = (ListView) findViewById(R.id.listView);

        txt_previous = findViewById(R.id.previous);
        txt_next =  findViewById(R.id.next);
        txt_page_no =(TypefacedTextView) findViewById(R.id.txt_page_no);
        loadCustomers();

    }

    //Function to set pagination buttons
    public void Btnfooter(final JSONArray array) {
        if(page_no==TOTAL_PAGE)
            txt_next.setEnabled(false);
        else
            txt_next.setEnabled(true);

        if(page_no==1)
            txt_previous.setEnabled(false);
        else
            txt_previous.setEnabled(true);

        txt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(page_no<TOTAL_PAGE)
                {
                    page_no++;
                    loadCustomers();
                }
                if(page_no==TOTAL_PAGE)
                    txt_next.setEnabled(false);
                else
                    txt_next.setEnabled(true);

                if(page_no==1)
                    txt_previous.setEnabled(false);
                else
                    txt_previous.setEnabled(true);

            }
        });
        txt_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page_no>1)
                {
                    page_no--;
                    loadCustomers();

                }
                if(page_no==TOTAL_PAGE)
                    txt_next.setEnabled(false);
                else
                    txt_next.setEnabled(true);

                if(page_no==1)
                    txt_previous.setEnabled(false);
                else
                    txt_previous.setEnabled(true);
            }
        });

    }
    //Filling data to listview
    public void setListView(int number,JSONArray array) {

        JSONArray sort = new JSONArray();
        try {
            int start = number * NUM_ITEMS_PAGE;
            for(int i=start;i<(start)+NUM_ITEMS_PAGE;i++)
            {
                if(i<array.length())
                {
                    sort.put(array.getJSONObject(i));
                }
                else
                {
                    break;
                }
            }
            listAdapter = new CustomerListAdapter(this, sort,this);
            listView.setAdapter(listAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //API call to load customer details
    public void loadCustomers() {
        if (util.checkInternetConnection()) {
            JSONObject jar = new JSONObject();
            cd.show("");
            Log.e("loadCust", Constants.signupUrl);
            //  Log.e("loadTrans", ""+jar);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.signupUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            cd.hide();
                            Log.e("loadCust", ""+response);

                            try {
                                JSONObject jsonObject =new JSONObject(response);

                                customers =(new JSONObject(response)).getJSONArray("customers");
                                NUM_ITEMS_PAGE = 10;
                                TOTAL_LIST_ITEMS= customers.length();
                                TOTAL_PAGE=  jsonObject.getInt("pages") ;
                                txt_page_no.setText("" + page_no);

                                Btnfooter(customers);
                              //  CheckBtnBackGroud(0);
                                setListView(0, customers);

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
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    JSONObject jjobj=new JSONObject();
                    params.put("type","customers");
                    params.put("action","list");
                    params.put("page", ""+page_no);

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
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }else {
            Toast.makeText(this,getResources().getString(R.string.dataconnection),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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


}
