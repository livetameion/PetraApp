package com.yagna.petra.app.Admin.StoreInformation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.yagna.petra.app.Admin.Users.UsersFragment;
import com.yagna.petra.app.Admin.Wallet.WalletListViewAdapter;
import com.yagna.petra.app.R;
import com.yagna.petra.app.Util.Constants;
import com.yagna.petra.app.Util.CustomDialog;
import com.yagna.petra.app.Util.PrefUtil;
import com.yagna.petra.app.Util.Utils;
import com.yagna.petra.app.views.TypefacedTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link AdminListActivity}
 * in two-pane mode (on tablets) or a {@link TransactionSingleViewActivity}
 * on handsets.
 */
public class StoreInformationFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";
    private LinearLayout ly_btn;

    private int increment;
    public int TOTAL_LIST_ITEMS;
    public int NUM_ITEMS_PAGE;
    private int noOfBtns;
    private TextView[] btns;
    private WalletListViewAdapter listAdapter;
    private SharedPreferences common_mypref;
    private Utils util;
    private CustomDialog cd;
    private JSONArray wallet;
    public static boolean isUpdated =false;
    private TypefacedTextView btn_update;
    private Spinner spn_str_state;
    private EditText edt_sc_price,edt_avg_mon_sales,edt_advt,edt_processing,edt_paper,edt_loyalty,edt_rewards,edt_website,edt_trn_permonth;
    private EditText edt_avg_trn;
    private EditText edt_store_address;
    private EditText edt_str_city, edt_str_zipcode, edt_str_phone;
    private JSONObject deviceData;
    private String locationId;
    private String merchantId;
    private View rootView;
    private DecimalFormat fc;
    private DecimalFormat dc;
    private EditText edt_biz_name,edt_ownr_name,edt_ownr_address,edt_ownr_city,edt_ownr_fax,edt_ownr_website,edt_ownr_zipconde,edt_ownr_phone;
    private Spinner spn_ownr_state;
    private EditText edt_ownr_email;
    private Spinner spn_ownr_charity;
    private String vp_Discount="0";
    private String tip_status="1";
    private LinearLayout ly_isTipactive;
    private Spinner spn_tip_active;
    private ArrayList<String> charity_name_array=new ArrayList<>();
    private ArrayList<String> charity_id_array=new ArrayList<>();


    public StoreInformationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
           /* mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle("");
            }*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_store_information, container, false);
            common_mypref = getActivity().getSharedPreferences("user", 0);
            util = new Utils(getActivity());
            cd = new CustomDialog(getActivity());
             fc = new DecimalFormat("#,###,###,###,##0.00");
            dc = new DecimalFormat("#,###,###,###,###");

            edt_sc_price = (EditText) rootView.findViewById(R.id.edt_sc_price);
            edt_avg_mon_sales = (EditText) rootView.findViewById(R.id.edt_avg_mon_sales);
            edt_advt = (EditText) rootView.findViewById(R.id.edt_advt);
            edt_processing = (EditText) rootView.findViewById(R.id.edt_processing);
            edt_paper = (EditText) rootView.findViewById(R.id.edt_paper);
            edt_loyalty = (EditText) rootView.findViewById(R.id.edt_loyalty);
            edt_rewards = (EditText) rootView.findViewById(R.id.edt_rewards);
            edt_website = (EditText) rootView.findViewById(R.id.edt_website);
            edt_trn_permonth = (EditText) rootView.findViewById(R.id.edt_trn_permonth);
            edt_avg_trn = (EditText) rootView.findViewById(R.id.edt_avg_trn);
            edt_store_address = (EditText) rootView.findViewById(R.id.edt_store_address);
            edt_str_city = (EditText) rootView.findViewById(R.id.edt_str_city);
            edt_str_zipcode = (EditText) rootView.findViewById(R.id.edt_str_zipconde);
            edt_str_phone = (EditText) rootView.findViewById(R.id.edt_str_phone);
            edt_str_phone.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(!charSequence.toString().startsWith("(")&&charSequence.toString().replace("(","")
                            .replace(")","")
                            .replace("-","")
                            .replace(" ","").length()==10)
                    {
                        edt_str_phone.setText("("+charSequence.toString().substring(0,3)+") "+charSequence.toString().substring(3,6)+"-"+charSequence.toString().substring(6));
                        edt_str_phone.setSelection(edt_str_phone.getText().length());

                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
            spn_str_state = (Spinner) rootView.findViewById(R.id.spn_str_state);
            ArrayAdapter<String> statesArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_spn_selected_state, Constants.states);
            statesArrayAdapter.setDropDownViewResource(R.layout.item_spn);
            spn_str_state.setAdapter(statesArrayAdapter);

            btn_update = (TypefacedTextView) rootView.findViewById(R.id.btn_update);
            btn_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updatePrefrences();
                }
            });

            edt_biz_name = (EditText) rootView.findViewById(R.id.edt_biz_name);
            edt_ownr_name = (EditText) rootView.findViewById(R.id.edt_ownr_name);

            edt_ownr_website = (EditText) rootView.findViewById(R.id.edt_ownr_website);

            edt_ownr_fax = (EditText) rootView.findViewById(R.id.edt_ownr_fax);
            edt_ownr_fax.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(!charSequence.toString().startsWith("(")&&charSequence.toString().replace("(","")
                            .replace(")","")
                            .replace("-","")
                            .replace(" ","").length()==10)
                    {
                        edt_ownr_fax.setText("("+charSequence.toString().substring(0,3)+") "+charSequence.toString().substring(3,6)+"-"+charSequence.toString().substring(6));
                        edt_ownr_fax.setSelection(edt_ownr_fax.getText().length());

                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
            edt_ownr_email = (EditText) rootView.findViewById(R.id.edt_ownr_email);

            spn_ownr_charity = (Spinner) rootView.findViewById(R.id.edt_ownr_charity);

            ly_isTipactive = (LinearLayout) rootView.findViewById(R.id.ly_isTipactive);

            spn_tip_active = (Spinner) rootView.findViewById(R.id.spn_tip_active);

            loadPrefrences();
        }
        return rootView;
    }

    private void fillData() {
        try {
            merchantId = deviceData.getJSONObject("data").getJSONObject("merchant").getString("merchant_id");
            locationId = deviceData.getJSONObject("data").getJSONObject("device").getString("merchant_location_id");

            String token_price = deviceData.getJSONObject("data").getJSONObject("location").getString("token_price");
            edt_sc_price.setText(""+ fc.format(Double.parseDouble(token_price)));
             vp_Discount = deviceData.getJSONObject("data").getJSONObject("location").getString("vp_discount");

            String sales = deviceData.getJSONObject("data").getJSONObject("location").getJSONObject("expenses").getString("sales");
            edt_avg_mon_sales.setText(""+ fc.format(Double.parseDouble(sales)));
            edt_avg_mon_sales.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    try {
                        edt_avg_trn.setText("" +dc.format( Math.round(Double.parseDouble(edt_avg_mon_sales.getText().toString().replace(",","").trim()) /
                                Double.parseDouble(edt_trn_permonth.getText().toString().replace(",","").trim()))));
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            edt_trn_permonth.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    try {
                        edt_avg_trn.setText("" +dc.format( Math.round(Double.parseDouble(edt_avg_mon_sales.getText().toString().replace(",","").trim()) /
                                Double.parseDouble(edt_trn_permonth.getText().toString().replace(",","").trim()))));
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            String advertising = deviceData.getJSONObject("data").getJSONObject("location").getJSONObject("expenses").getString("advertising");
            edt_advt.setText(""+ fc.format(Double.parseDouble(advertising)));

            String processing = deviceData.getJSONObject("data").getJSONObject("location").getJSONObject("expenses").getString("processing");
            edt_processing.setText(""+ fc.format(Double.parseDouble(processing)));

            String paper =  deviceData.getJSONObject("data").getJSONObject("location").getJSONObject("expenses").getString("paper");
            edt_paper.setText(""+ fc.format(Double.parseDouble(paper)));

            String loyalty =  deviceData.getJSONObject("data").getJSONObject("location").getJSONObject("expenses").getString("loyalty");
            edt_loyalty.setText(""+ fc.format(Double.parseDouble(loyalty)));

            String rewards = deviceData.getJSONObject("data").getJSONObject("location").getJSONObject("expenses").getString("rewards");
            edt_rewards.setText(""+dc.format(Math.round(Double.parseDouble(rewards))));

            String website = deviceData.getJSONObject("data").getJSONObject("location").getJSONObject("expenses").getString("website");
            edt_website.setText(""+ fc.format(Double.parseDouble(website)));

            String monthly_transactions = deviceData.getJSONObject("data").getJSONObject("location").getJSONObject("expenses").getString("monthly_transactions");
            edt_trn_permonth.setText(dc.format(Math.round(Double.parseDouble(monthly_transactions))));

            String avg_transaction = deviceData.getJSONObject("data").getJSONObject("location").getJSONObject("expenses").getString("avg_transaction");
            edt_avg_trn.setText(""+dc.format(Math.round(Double.parseDouble(avg_transaction))));
            edt_avg_trn.setEnabled(false);
            edt_avg_trn.setClickable(false);

            String address = deviceData.getJSONObject("data").getJSONObject("location").getString("address");
            edt_store_address.setText(address);

            String city = deviceData.getJSONObject("data").getJSONObject("location").getString("city");
            edt_str_city.setText(city);

            String state = deviceData.getJSONObject("data").getJSONObject("location").getString("state");
            spn_str_state.setSelection(getPosition(state));

            String zipcode = deviceData.getJSONObject("data").getJSONObject("location").getString("zipcode");
            edt_str_zipcode.setText(zipcode);

            String phone = deviceData.getJSONObject("data").getJSONObject("location").getString("phone");
            edt_str_phone.setText(phone);

            String ownr_name = deviceData.getJSONObject("data").getJSONObject("merchant").getString("dba");
            edt_ownr_name.setText(ownr_name);

            String bizname = deviceData.getJSONObject("data").getJSONObject("merchant").getString("name");
            edt_biz_name.setText(bizname);

            String ownr_fax = deviceData.getJSONObject("data").getJSONObject("merchant").getString("fax");
            edt_ownr_fax.setText(ownr_fax);

            String ownr_web = deviceData.getJSONObject("data").getJSONObject("merchant").getString("website");
            edt_ownr_website.setText(ownr_web);

            String ownr_email = deviceData.getJSONObject("data").getJSONObject("merchant").getString("email");
            edt_ownr_email.setText(ownr_email);
             for(int i=0;i<charity_id_array.size();i++)
              if(charity_id_array.get(i).equalsIgnoreCase(deviceData.getJSONObject("data").getJSONObject("location").getString("charity_id")))
                  spn_ownr_charity.setSelection(i);

            tip_status = deviceData.getJSONObject("data").getJSONObject("location").getString("tip_status");
            ArrayAdapter<CharSequence> uTyperArrayAdapter = new ArrayAdapter<CharSequence>(getActivity(), R.layout.item_spn_selected, (CharSequence[]) getActivity().getResources().getTextArray(R.array.array_isactive));
            uTyperArrayAdapter.setDropDownViewResource(R.layout.item_spn);
            spn_tip_active.setAdapter(uTyperArrayAdapter);


            String industry = deviceData.getJSONObject("data").getJSONObject("merchant").getString("website");
            if(!industry.equalsIgnoreCase("restaurant"))
                ly_isTipactive.setVisibility(View.GONE);

            if(tip_status.equalsIgnoreCase("1"))
            {
                spn_tip_active.setSelection(0);
            }
            else
            {
                spn_tip_active.setSelection(1);
            }



        }catch (Exception e ){
            e.printStackTrace();
        }
    }

    private int getPosition(String state) {
        for(int i=0;i<Constants.states.length;i++){
            if(state.equalsIgnoreCase(Constants.states[i]))
                return i;
        }
        return 0;
    }


    private void loadPrefrences() {
            if (util.checkInternetConnection()) {
                JSONObject jar = new JSONObject();
                cd.show("");
                Log.e("deviceinfo", Constants.loginUrl);
                Log.e("getImageInfo", ""+jar);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.loginUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                cd.hide();
                                Log.e("deviceinfo", ""+response);
                                try {
                                    deviceData=new JSONObject(response);
                                    loadCharity();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                cd.hide();
                                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("deviceid","4C4C4544-0052-4D10-8037-B9C04F344C32");
                        params.put("action","checkdevice");
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
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(stringRequest);

            }else {
                Toast.makeText(getActivity(),getResources().getString(R.string.dataconnection),Toast.LENGTH_LONG).show();
            }

        }
    public void loadCharity() {
        if (util.checkInternetConnection()) {
            JSONObject jar = new JSONObject();
            cd.show("");
            Log.e("loadTrans", Constants.charity);
            //  Log.e("loadTrans", ""+jar);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.charity,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            cd.hide();
                            Log.e("loadTrans", ""+response);
                            try {
                                JSONArray charity= (new JSONObject(response)).getJSONArray("data");
                                charity_name_array =new ArrayList<>();
                                for(int i=0;i<charity.length();i++){
                                    charity_name_array.add(charity.getJSONObject(i).getString("name"));
                                    charity_id_array.add(charity.getJSONObject(i).getString("id"));
                                }

                                ArrayAdapter<String> uTyperArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_spn_selected, charity_name_array);
                                uTyperArrayAdapter.setDropDownViewResource(R.layout.item_spn);
                                spn_ownr_charity.setAdapter(uTyperArrayAdapter);

                                fillData();



                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                                fillData();

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            cd.hide();
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                            fillData();

                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    JSONObject jjobj=new JSONObject();
                    try {
                        jjobj.put("merchant_location_id",""+AdminListActivity.merchant_location_id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    params.put("data",jjobj.toString());
                    params.put("action","view");
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
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);

        }else {
            Toast.makeText(getActivity(),getResources().getString(R.string.dataconnection),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private void updatePrefrences() {
        if (util.checkInternetConnection()) {
            JSONObject jar = new JSONObject();
            cd.show("");
            Log.e("deviceinfo", Constants.merchantDataUrl);
            Log.e("getImageInfo", ""+jar);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.merchantDataUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            cd.hide();
                            Log.e("deviceinfo", ""+response);
                            try {
                                if((new JSONObject(response)).getString("status").equalsIgnoreCase("1")){
                                    UsersFragment.isUpdated=true;
                                    loadPrefrences();
                                }
                                else {
                                    Toast.makeText(getActivity(),"Unable to update preferences. Try again or contact Freedom Choice.",Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            cd.hide();
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }){

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("locationid",locationId);
                    params.put("tokenprice",edt_sc_price.getText().toString().replace(",","").trim());
                    params.put("busphone", edt_str_phone.getText().toString().replace(" ","").replace("-","").replace("(","").replace(")","").trim());
                    params.put("busaddress", edt_store_address.getText().toString().trim());
                    params.put("buscity", edt_str_city.getText().toString().trim());
                    params.put("busstate",Constants.states[spn_str_state.getSelectedItemPosition()]);
                    params.put("buszip", edt_str_zipcode.getText().toString().trim());
                    params.put("busfax", edt_ownr_fax.getText().toString().trim());
                    params.put("buswebsite", edt_ownr_website.getText().toString().trim());
                    params.put("expsales",edt_avg_mon_sales.getText().toString().replace(",","").trim());
                    params.put("expadvertising",edt_advt.getText().toString().replace(",","").trim());
                    params.put("expprocessing",edt_processing.getText().toString().replace(",","").trim());
                    params.put("exppaper",edt_paper.getText().toString().replace(",","").trim());
                    params.put("exployalty",edt_loyalty.getText().toString().replace(",","").trim());
                    params.put("exprewards",edt_rewards.getText().toString().replace(",","").trim());
                    params.put("expwebsite",edt_website.getText().toString().replace(",","").trim());
                    params.put("exptransactions",edt_trn_permonth.getText().toString().replace(",","").trim());
                    params.put("expavgamount",edt_avg_trn.getText().toString().replace(",","").trim());
                    params.put("vpdiscount",vp_Discount);
                    params.put("tip_status",spn_tip_active.getSelectedItemPosition()==0?"1":"0");
                    params.put("charity_id",""+charity_id_array.get(spn_ownr_charity.getSelectedItemPosition()));

                    params.put("action","updatepreferences");

                    PrefUtil.saveCharityPlace(common_mypref, charity_id_array.get(spn_ownr_charity.getSelectedItemPosition()));

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
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);

        }else {
            Toast.makeText(getActivity(),getResources().getString(R.string.dataconnection),Toast.LENGTH_LONG).show();
        }

    }

}
