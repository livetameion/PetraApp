package com.pos.petra.app.Admin.Transaction;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pos.petra.app.Admin.AdminListActivity;
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

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link AdminListActivity}
 * on handsets.
 *
 * This Fragment is about transaction history
 */
public class TransactionFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";
    private ListView listView;
    private LinearLayout ly_btn;
    private ImageView txt_previous;
    private ImageView txt_next;
    private TextView txtview;
    private HorizontalScrollView hsv;
    private int increment;
    public int TOTAL_LIST_ITEMS;
    public int NUM_ITEMS_PAGE;
    private int noOfBtns;
    private TextView[] btns;
    private TransactionListViewAdapter listAdapter;
    private Utils util;
    private CustomDialog progressDialog;
    private JSONArray transaction;
    private View rootView;
    private TypefacedTextView txt_page_no;

    //Empty Constructor
    public TransactionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tansaction, container, false);
            util = new Utils(getActivity());
            progressDialog = new CustomDialog(getActivity());
            listView =  rootView.findViewById(R.id.listView);
            ly_btn = rootView.findViewById(R.id.ly_pagination_btn);
            txt_previous = rootView.findViewById(R.id.previous);
            txt_next = rootView.findViewById(R.id.next);
            txtview =  rootView.findViewById(R.id.txtview);
            hsv =  rootView.findViewById(R.id.horizontalScrollview);
            ly_btn = rootView.findViewById(R.id.ly_pagination_btn);
            txt_page_no = rootView.findViewById(R.id.txt_page_no);

            //API Call to loads transaction history data  
            loadTransaction();
        }

        return rootView;
    }

    //Function to set pagination buttons
    public void Btnfooter(final JSONArray array) {
        increment = 0;
        ly_btn.removeAllViews();
        int val = TOTAL_LIST_ITEMS % NUM_ITEMS_PAGE;
        val = val == 0 ? 0 : 1;
        noOfBtns = TOTAL_LIST_ITEMS / NUM_ITEMS_PAGE + val;
        btns = new TextView[noOfBtns];
        for (int i = 0; i < noOfBtns; i++) {
            btns[i] = new TextView(getActivity());
            btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
            btns[i].setText("" + (i + 1));
            btns[i].setPadding(15, 5, 15, 5);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(3, 3, 3, 3);
            ly_btn.addView(btns[i], lp);
            final int j = i;
            btns[j].setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    setListView(j, array);
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
                if (increment != (noOfBtns - 1)) {
                    increment++;
                    setListView(increment, array);
                    CheckBtnBackGroud(increment);
                    CheckEnable(increment);
                    hsv.scrollTo(btns[increment].getLeft(), 0);
                } else
                    CheckEnable(increment);

            }
        });
        txt_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (increment != 0) {
                    increment--;
                    setListView(increment, array);
                    CheckBtnBackGroud(increment);
                    CheckEnable(increment);
                    hsv.scrollTo(btns[increment].getLeft(), 0);
                } else
                    CheckEnable(increment);

            }
        });

    }


    //Set pagination button enable and disble
    private void CheckEnable(int increment) {
        if (increment == (noOfBtns - 1)) {
            txt_next.setEnabled(false);
        } else {
            txt_next.setEnabled(true);
        }

        if (increment == 0) {
            txt_previous.setEnabled(false);
        } else {
            txt_previous.setEnabled(true);
        }
    }

    public void CheckBtnBackGroud(int index) {
        txtview.setText("Showing Page " + (index + 1) + " of " + noOfBtns);
        for (int i = 0; i < noOfBtns; i++) {
            if (i == index) {
                btns[i].setBackgroundColor(getResources().getColor(R.color.dark_blue));
                btns[i].setTextColor(getResources().getColor(android.R.color.white));
                txt_page_no.setText("" + btns[i].getText().toString()+"/"+noOfBtns);
            } else {
                btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                btns[i].setTextColor(getResources().getColor(android.R.color.black));
            }
        }
    }

    //Filling data to listview
    public void setListView(int number, JSONArray array) {
        JSONArray sort = new JSONArray();
        try {
            int start = number * NUM_ITEMS_PAGE;
            for (int i = start; i < (start) + NUM_ITEMS_PAGE; i++) {
                if (i < array.length()) {
                    sort.put(array.getJSONObject(i));
                } else {
                    break;
                }
            }
            listAdapter = new TransactionListViewAdapter(getActivity(), sort);
            listView.setAdapter(listAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //API call to load Transaction data
    private void loadTransaction() {
        if (util.checkInternetConnection()) {
            JSONObject jar = new JSONObject();
            progressDialog.show("");
            Log.e("loadTransaction", Constants.transactionsUrl);
            //  Log.e("loadTransaction", ""+jar);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.transactionsUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.hide();
                            Log.e("loadTransaction", "" + response);
                            try {
                                response = response.replace("loadlocationid", "");
                                transaction = (new JSONObject(response)).getJSONObject("data").getJSONArray("transactions");
                                if (((AdminListActivity) getActivity()).mTwoPane) {
                                    NUM_ITEMS_PAGE = 8;
                                } else
                                    NUM_ITEMS_PAGE = 15;

                                TOTAL_LIST_ITEMS = transaction.length();
                                Btnfooter(transaction);
                                CheckBtnBackGroud(0);
                                setListView(0, transaction);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.hide();
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("locationid", "" + AdminListActivity.merchant_location_id);
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
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);

        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.dataconnection), Toast.LENGTH_LONG).show();
        }
    }


}
