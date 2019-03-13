package com.yagna.petra.app.Customer;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yagna.petra.app.Admin.AdminListActivity;
import com.yagna.petra.app.Admin.Transaction.TransactionSingleViewActivity;
import com.yagna.petra.app.GlobalActivity;
import com.yagna.petra.app.R;
import com.yagna.petra.app.Util.Constants;
import com.yagna.petra.app.Util.CustomDialog;
import com.yagna.petra.app.Util.Utils;
import com.yagna.petra.app.views.TypefacedTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link AdminListActivity}
 * in two-pane mode (on tablets) or a {@link TransactionSingleViewActivity}
 * on handsets.
 */
public class CustomerActivity extends GlobalActivity {
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
    private JSONArray lastVoucherArrary;
    private CustomerListViewAdapter listAdapter;
    private SharedPreferences common_mypref;
    private Utils util;
    private CustomDialog cd;
    private JSONArray customers;
    public static boolean isUpdated =false;
    private Button btn_add_cashier;
    private View rootView;
    private TypefacedTextView txt_page_no;
    private int page_no = 1;


    JSONObject user;
    EditText edt_name;
    EditText edt_e_id;
    EditText edt_phone;
    EditText edt_email;
    EditText edt_pin;
    Spinner spn_isactive;
    Button btn_update;

    LinearLayout ly_isactive;

    private String isactive;
    private Dialog dialogCreateCashier;
    private int TOTAL_PAGE=0;

    public CustomerActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_coustomer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        toolbar.setTitle("Customers");
        setSupportActionBar(toolbar);
        common_mypref = this.getSharedPreferences("user", 0);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        util = new Utils(this);
        cd = new CustomDialog(this);
        listView = (ListView) findViewById(R.id.listView);
        ly_btn = (LinearLayout) findViewById(R.id.ly_btn);
        txt_previous = findViewById(R.id.previous);
        txt_next =  findViewById(R.id.next);
        txtview = (TextView) findViewById(R.id.txtview);
        hsv = (HorizontalScrollView) findViewById(R.id.horizontalScrollview);
        ly_btn = (LinearLayout) findViewById(R.id.ly_btn);


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
        /*increment = 0;
        ly_btn.removeAllViews();
        int val = TOTAL_LIST_ITEMS % NUM_ITEMS_PAGE;
        val = val == 0 ? 0 : 1;
        noOfBtns = TOTAL_LIST_ITEMS / NUM_ITEMS_PAGE + val;

        btns = new TypefacedTextView[noOfBtns];
        for (int i = 0; i < noOfBtns; i++) {
            btns[i] = new TypefacedTextView(AdvTransactionListActivity.this);
            btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
            btns[i].setText("" + (i + 1));
            btns[i].setPadding(15, 5, 15, 5);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(android.widget.Toolbar.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
        CheckEnable(increment);*/

        txt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (increment != (noOfBtns - 1)) {
                    increment++;
                    setListView(increment, array);
                    CheckBtnBackGroud(increment);
                    CheckEnable(increment);
                    hsv.scrollTo(btns[increment].getLeft(), 0);
                } else
                    CheckEnable(increment);*/
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
               /* if (increment != 0) {
                    increment--;
                    setListView(increment, array);
                    CheckBtnBackGroud(increment);
                    CheckEnable(increment);
                    hsv.scrollTo(btns[increment].getLeft(), 0);
                } else
                    CheckEnable(increment);*/
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
    public void setListView(int number,JSONArray array) {
        lastVoucherArrary=array;
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
            listAdapter = new CustomerListViewAdapter(this, sort,this);
            listView.setAdapter(listAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void loadCustomers() {
        if (util.checkInternetConnection()) {
            JSONObject jar = new JSONObject();
            cd.show("");
            Log.e("loadTrans", Constants.signupUrl);
            //  Log.e("loadTrans", ""+jar);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.signupUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            cd.hide();
                            Log.e("loadTrans", ""+response);

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

    //Dialog for shoiwng final total of transaction
    public void dialogAddCashier() {
       dialogCreateCashier = new Dialog(this, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = this.getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.activity_user_detail, null);
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialogCreateCashier.getWindow().setBackgroundDrawableResource(R.color.translucent_black);



        edt_name =(EditText)rowView.findViewById(R.id.edt_name);

        edt_e_id=(EditText)rowView.findViewById(R.id.edt_e_id);

        edt_phone=(EditText)rowView.findViewById(R.id.edt_str_phone);

        edt_email=(EditText)rowView.findViewById(R.id.edt_email);

        edt_pin=(EditText)rowView.findViewById(R.id.edt_pin);
        edt_pin.setText("");


        ly_isactive  =(LinearLayout) rowView.findViewById(R.id.ly_isactive);
        ly_isactive.setVisibility(View.GONE);

        spn_isactive =(Spinner) rowView.findViewById(R.id.spn_active);





        btn_update = (Button) rowView.findViewById(R.id.btn_update);
        btn_update.setBackgroundColor(getResources().getColor(R.color.dark_grey));
        btn_update.setTextColor(getResources().getColor(R.color.white));
        btn_update.setText("Create");


        edt_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validation();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        edt_pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validation();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        edt_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validation();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        LinearLayout ly_pay = rowView.findViewById(R.id.ly_pay);
        ly_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCreateCashier.dismiss();
            }
        });

        dialogCreateCashier.setContentView(rowView);
        dialogCreateCashier.setCancelable(true);
        dialogCreateCashier.setCanceledOnTouchOutside(true);
        dialogCreateCashier.show();

    }

    public boolean validation(){
        if(edt_name.getText().toString().trim().length()==0){
            btn_update.setEnabled(false);
            btn_update.setBackgroundColor(getResources().getColor(R.color.dark_grey));
            btn_update.setTextColor(getResources().getColor(R.color.white));
            return false;
        }
        else if(edt_phone.getText().length()<10){
            btn_update.setEnabled(false);
            btn_update.setBackgroundColor(getResources().getColor(R.color.dark_grey));
            btn_update.setTextColor(getResources().getColor(R.color.white));
            return false;
        }
        else if(edt_pin.getText().length()<4){
            btn_update.setEnabled(false);
            btn_update.setBackgroundColor(getResources().getColor(R.color.dark_grey));
            btn_update.setTextColor(getResources().getColor(R.color.white));
            return false;
        }
        else
        {
            btn_update.setEnabled(true);
            btn_update.setBackgroundColor(getResources().getColor(R.color.dark_green));
            btn_update.setTextColor(getResources().getColor(R.color.white));

            return true;
        }
    }

    private void uploadUser() {
        if (util.checkInternetConnection()) {
            JSONObject jar = new JSONObject();
            cd.show("");
            Log.e("uploadUser", Constants.cashierDataUrl);
            //  Log.e("loadTrans", ""+jar);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.cashierDataUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            cd.hide();
                            Log.e("uploadUser", ""+response);
                            try {
                                if((new JSONObject(response)).getString("status").equalsIgnoreCase("1")){
                                    dialogCreateCashier.dismiss();
                                    loadCustomers();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                                }

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
                    params.put("cashiername",edt_name.getText().toString().trim());
                    params.put("empid",edt_e_id.getText().toString().trim());
                    params.put("cashierphone",edt_phone.getText().toString().trim());
                    params.put("email",edt_email.getText().toString().trim());
                    params.put("cashierpin",edt_pin.getText().toString().trim());
                    params.put("locationid",AdminListActivity.merchant_location_id);
                    params.put("merchantid",AdminListActivity.merchant_id);
                    params.put("action","addcashier");
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


}
