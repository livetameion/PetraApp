package com.pos.petra.app.Admin.ValueCalculations;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pos.petra.app.Admin.AdminListActivity;
import com.pos.petra.app.Admin.CashierUsers.CashiersFragment;
import com.pos.petra.app.R;
import com.pos.petra.app.Util.Constants;
import com.pos.petra.app.Util.CustomDialog;
import com.pos.petra.app.Util.PrefUtil;
import com.pos.petra.app.Util.Utils;
import com.pos.petra.app.Views.TypefacedTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link AdminListActivity}
 * on handsets.
 */
public class ValueCalculationsFragment extends Fragment {
    private SharedPreferences common_mypref;
    private Utils util;
    private CustomDialog cd;
    private TypefacedTextView btn_update;
    private Spinner spn_state;
    private EditText edt_sc_price,edt_avg_mon_sales,edt_advt,edt_processing,edt_paper,edt_loyalty,edt_rewards,edt_website,edt_trn_permonth;
    private EditText edt_avg_trn;
    private EditText edt_address;
    private EditText edt_city,edt_zipconde,edt_phone;
    private JSONObject deviceData;
    private String locationId;
    private String merchantId;
    private View rootView;
    private DecimalFormat fc;
    private DecimalFormat dc;
    private ArrayList<String> arr_discount=new  ArrayList<String>();
    private Spinner spn_discount;
    private EditText edt_vp;
    private EditText edt_vp_price;
    private String tip_status="0";
    private String charity_id;
    public ValueCalculationsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_value_calculation, container, false);
            common_mypref = getActivity().getSharedPreferences("user", 0);
            util = new Utils(getActivity());
            cd = new CustomDialog(getActivity());
             fc = new DecimalFormat("#,###,###,###,##0.00");
             dc = new DecimalFormat("#,###,###,###,###");
            edt_sc_price =  rootView.findViewById(R.id.edt_sc_price);
            edt_avg_mon_sales =  rootView.findViewById(R.id.edt_avg_mon_sales);
            edt_advt =  rootView.findViewById(R.id.edt_advt);
            edt_processing =  rootView.findViewById(R.id.edt_processing);
            edt_paper =  rootView.findViewById(R.id.edt_paper);
            edt_loyalty =  rootView.findViewById(R.id.edt_loyalty);
            edt_rewards =  rootView.findViewById(R.id.edt_rewards);
            edt_website =  rootView.findViewById(R.id.edt_website);
            edt_trn_permonth =  rootView.findViewById(R.id.edt_trn_permonth);
            edt_avg_trn =  rootView.findViewById(R.id.edt_avg_trn);
            edt_address =  rootView.findViewById(R.id.edt_store_address);
            edt_city =  rootView.findViewById(R.id.edt_str_city);
            edt_zipconde =  rootView.findViewById(R.id.edt_str_zipconde);
            edt_phone =  rootView.findViewById(R.id.edt_str_phone);
            edt_phone.addTextChangedListener(new TextWatcher() {
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
                        edt_phone.setText("("+charSequence.toString().substring(0,3)+") "+charSequence.toString().substring(3,6)+"-"+charSequence.toString().substring(6));
                        edt_phone.setSelection(edt_phone.getText().length());

                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
            spn_state = (Spinner) rootView.findViewById(R.id.spn_str_state);
            ArrayAdapter<String> statesArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Constants.states);
            statesArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_state.setAdapter(statesArrayAdapter);
            edt_vp = rootView.findViewById(R.id.edt_vp);
            edt_vp_price = rootView.findViewById(R.id.edt_vp_price);
            arr_discount = Constants.discount();
            spn_discount= (Spinner) rootView.findViewById(R.id.spn_discount);
            ArrayAdapter<String> spnArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,arr_discount);
            spnArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_discount.setAdapter(spnArrayAdapter);
            btn_update = (TypefacedTextView) rootView.findViewById(R.id.btn_update);
            btn_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updatePrefrences();
                }
            });
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

            String sales = deviceData.getJSONObject("data").getJSONObject("location").getJSONObject("expenses").getString("sales");
            edt_avg_mon_sales.setText(""+ fc.format(Double.parseDouble(sales)));

            String advertising = deviceData.getJSONObject("data").getJSONObject("location").getJSONObject("expenses").getString("advertising");
            edt_advt.setText(""+ fc.format(Double.parseDouble(advertising)));


            String processing = deviceData.getJSONObject("data").getJSONObject("location").getJSONObject("expenses").getString("processing");
            edt_processing.setText(""+ fc.format(Double.parseDouble(processing)));

            String paper =  deviceData.getJSONObject("data").getJSONObject("location").getJSONObject("expenses").getString("paper");
            edt_paper.setText(""+ fc.format(Double.parseDouble(paper)));
            charity_id = deviceData.getJSONObject("data").getJSONObject("location").getString("charity_id");

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
            edt_address.setText(address);

            String city = deviceData.getJSONObject("data").getJSONObject("location").getString("city");
            edt_city.setText(city);

            String state = deviceData.getJSONObject("data").getJSONObject("location").getString("state");
            spn_state.setSelection(getPosition(state));

            String zipcode = deviceData.getJSONObject("data").getJSONObject("location").getString("zipcode");
            edt_zipconde.setText(zipcode);

            String phone = deviceData.getJSONObject("data").getJSONObject("location").getString("phone");
            edt_phone.setText(phone);



            String vp_Discount = deviceData.getJSONObject("data").getJSONObject("location").getString("vp_discount");
            for(int i = 0 ;i<arr_discount.size();i++){
                if(vp_Discount.equalsIgnoreCase(arr_discount.get(i))){
                    spn_discount.setSelection(i);
                }

            }

          tip_status = deviceData.getJSONObject("data").getJSONObject("location").getString("tip_status");


            addListners();
           refreshVP();




        }catch (Exception e ){
            e.printStackTrace();
        }
    }

    private void addListners() {
        edt_website.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                refreshVP();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edt_processing.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                refreshVP();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edt_rewards.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                refreshVP();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edt_loyalty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                refreshVP();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edt_paper.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                refreshVP();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edt_advt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                refreshVP();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        spn_discount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                refreshVP();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edt_avg_mon_sales.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                     String mon_sales = edt_avg_mon_sales.getText().toString().replace(",", "").trim();
                    String trn_permont = edt_trn_permonth.getText().toString().replace(",", "").trim();

                    long trn = Math.round(Double.parseDouble(mon_sales.length()==0?"0":mon_sales) /
                            Double.parseDouble(trn_permont.length()==0?"1":trn_permont));
                    edt_avg_trn.setText("" +dc.format( trn));
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
                    String mon_sales = edt_avg_mon_sales.getText().toString().replace(",", "").trim();
                    String trn_permont = edt_trn_permonth.getText().toString().replace(",", "").trim();

                    long trn = Math.round(Double.parseDouble(mon_sales.length()==0?"0":mon_sales) /
                            Double.parseDouble(trn_permont.length()==0?"1":trn_permont));
                    edt_avg_trn.setText("" +dc.format( trn));
                    refreshVP();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    //VP- Value Praposition. It is calucalted expense/transaction permonth
    private void refreshVP() {
        String advt =edt_advt.getText().toString().replace(",","").trim();
        String procesing =edt_processing.getText().toString().replace(",","").trim();
        String paper =edt_paper.getText().toString().replace(",","").trim();
        String loyalt =edt_loyalty.getText().toString().replace(",","").trim();
        String rewards =edt_rewards.getText().toString().replace(",","").trim();
        String website =edt_website.getText().toString().replace(",","").trim();

        Double total_expense=Double.parseDouble(advt.length()==0?"0":advt)+
                    Double.parseDouble(procesing.length()==0?"0":procesing)+
                    Double.parseDouble(paper.length()==0?"0":paper)+
                    Double.parseDouble(loyalt.length()==0?"0":loyalt)+
                    Double.parseDouble(rewards.length()==0?"0":rewards)+
                    Double.parseDouble(website.length()==0?"0":website);

        double vp = Math.round(total_expense / Double.parseDouble(edt_trn_permonth.getText().toString().replace(",", "")));
        edt_vp.setText(""+Math.round(vp));
        int dicount = Integer.parseInt(arr_discount.get(spn_discount.getSelectedItemPosition()));
        double d =  (vp*dicount/100);
        double vp_price = vp-d;
        edt_vp_price.setText(""+ fc.format(vp_price));
    }

    private int getPosition(String state) {
        for(int i=0;i<Constants.states.length;i++){
            if(state.equalsIgnoreCase(Constants.states[i]))
                return i;
        }
        return 0;
    }

     ///API call to load prefrences
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
                                    fillData();

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


    @Override
    public void onResume() {
        super.onResume();


    }


    //API call to update prefrences
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
                                    CashiersFragment.isUpdated=true;
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
                    params.put("busphone",edt_phone.getText().toString().replace(" ","").replace("-","").replace("(","").replace(")","").trim());                    params.put("busaddress",edt_address.getText().toString().trim());
                    params.put("buscity",edt_city.getText().toString().trim());
                    params.put("busstate",Constants.states[spn_state.getSelectedItemPosition()]);
                    params.put("buszip", edt_zipconde.getText().toString().trim());

                    params.put("expsales",edt_avg_mon_sales.getText().toString().replace(",","").trim());
                    params.put("expadvertising",edt_advt.getText().toString().replace(",","").trim());
                    params.put("expprocessing",edt_processing.getText().toString().replace(",","").trim());

                    params.put("exppaper",edt_paper.getText().toString().replace(",","").trim());
                    params.put("exployalty",edt_loyalty.getText().toString().replace(",","").trim());
                    params.put("exprewards",edt_rewards.getText().toString().replace(",","").trim());
                    params.put("expwebsite",edt_website.getText().toString().replace(",","").trim());
                    params.put("exptransactions",edt_trn_permonth.getText().toString().replace(",","").trim());
                    params.put("expavgamount",edt_avg_trn.getText().toString().replace(",","").trim());
                    params.put("vpdiscount",arr_discount.get(spn_discount.getSelectedItemPosition()));
                    params.put("tip_status",tip_status);
                    params.put("charity_id",charity_id);

                    params.put("action","updatepreferences");

                    PrefUtil.saveVPDiscount(common_mypref,arr_discount.get(spn_discount.getSelectedItemPosition()));

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
