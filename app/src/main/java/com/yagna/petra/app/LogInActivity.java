package com.yagna.petra.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.yagna.petra.app.Admin.AdminListActivity;
import com.yagna.petra.app.AdvncedCashier.AdvCashierActivity;
import com.yagna.petra.app.Cashier.CashierActivity;
import com.yagna.petra.app.Model.Admin;
import com.yagna.petra.app.Model.Cashiers;
import com.yagna.petra.app.Util.Constants;
import com.yagna.petra.app.Util.CustomDialog;
import com.yagna.petra.app.Util.PrefUtil;
import com.yagna.petra.app.Util.SingleTon;
import com.yagna.petra.app.Util.Utils;
import com.yagna.petra.app.views.TypefacedButton;
import com.yagna.petra.app.views.TypefacedEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LogInActivity  extends GlobalActivity {
    EditText edt_email;
    private SharedPreferences common_mypref;
    private Utils util;
    private TypefacedButton btn_signin;
    private TypefacedEditText edt_pin;
    private Spinner spn_uname, spn_uType;
    private String[] uTypes=new String[]{"Admin","Cashier"};
    private CustomDialog cd;
    private ArrayList<String> admins;
    private ArrayList<String> cashiers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
         }
        super.onCreate(savedInstanceState);
        common_mypref = getApplicationContext().getSharedPreferences("user", 0);

        if (!PrefUtil.getLoginData(common_mypref).equalsIgnoreCase("")) {
            try {
                if(!(new JSONObject(PrefUtil.getLoginData(common_mypref))).isNull("admin"))
                {
                    Toast.makeText(getApplicationContext(),"Login success",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LogInActivity.this,AdminListActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(LogInActivity.this,AdvCashierActivity.class);
                    startActivity(intent);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            setContentView(R.layout.activity_log_in);
            util = new Utils(this);
            cd = new CustomDialog(this);
            btn_signin =  findViewById(R.id.btn_signin);
            btn_signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callLoginApi();
                }
            });
            edt_pin =  findViewById(R.id.edt_pin);
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
            spn_uname =  findViewById(R.id.spn_uname);
            admins=SingleTon.admins;
            cashiers =SingleTon.cashiers;
            ArrayAdapter<String> uNameAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.item_spn_selected, admins);
            uNameAdapter.setDropDownViewResource(R.layout.item_spn);
            spn_uname.setAdapter(uNameAdapter);
            spn_uname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    validation();
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
            spn_uType =  findViewById(R.id.spn_type);
            ArrayAdapter<String> uTyperArrayAdapter = new ArrayAdapter<>(this, R.layout.item_spn_selected, uTypes);
            uTyperArrayAdapter.setDropDownViewResource(R.layout.item_spn);
            spn_uType.setAdapter(uTyperArrayAdapter);
            spn_uType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    validation();
                    if (i == 1 && cashiers != null) {
                        ArrayAdapter<String> uNameAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.item_spn_selected, cashiers);
                        uNameAdapter.setDropDownViewResource(R.layout.item_spn);
                        spn_uname.setAdapter(uNameAdapter);
                    } else if (i == 0 && admins != null) {
                        ArrayAdapter<String> uNameAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.item_spn_selected, admins);
                        uNameAdapter.setDropDownViewResource(R.layout.item_spn);
                        spn_uname.setAdapter(uNameAdapter);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


        }
    }
    private void callLoginApi() {
        if (util.checkInternetConnection()) {
            JSONObject jar = new JSONObject();
            cd.hide();
            cd.show("");
            Log.e("deviceinfo", Constants.cashierDataUrl);
            Log.e("getImageInfo", ""+jar);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.cashierDataUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            cd.hide();
                            Log.e("deviceinfo", ""+response);
                            try {
                                JSONObject loginData=new JSONObject(response);
                                if(loginData.getInt("status")==1){

                                    if(!loginData.getJSONObject("data").isNull("admin"))
                                    {
                                        PrefUtil.saveLoginData(common_mypref,loginData.getJSONObject("data").toString());
                                        Toast.makeText(getApplicationContext(),"Login success",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LogInActivity.this,AdminListActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        PrefUtil.saveLoginData(common_mypref,loginData.getJSONObject("data").toString());
                                        Toast.makeText(getApplicationContext(),"Login success",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LogInActivity.this,AdvCashierActivity.class);
                                        startActivity(intent);
                                        finish();
                                   }
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.loginfailed),Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(LogInActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                    @Override
                    protected Map<String, String> getParams() {
                        String loginid,logintype;
                        ArrayList<Admin> admins = SingleTon.cashierData.admins;
                        ArrayList<Cashiers> cashiers = SingleTon.cashierData.cashiers;

                        if(spn_uType.getSelectedItemPosition()==1)
                        {   logintype="Cashier";
                            loginid=cashiers.get(spn_uname.getSelectedItemPosition()-1).cashier_id;
                        }
                        else{
                            logintype="Admin";
                            loginid= admins.get(spn_uname.getSelectedItemPosition()-1).admin_id;
                        }

                        Map<String, String> params = new HashMap<>();
                        params.put("action","login");
                        params.put("loginid",loginid);
                        params.put("pin",edt_pin.getText().toString().trim());
                        params.put("logintype",logintype);

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
                    Toast.makeText(LogInActivity.this, error.toString(), Toast.LENGTH_LONG).show();

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }else {
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.dataconnection),Toast.LENGTH_LONG).show();
        }
    }


    //validation for credential befor calling DB connection
    private void validation() {
        if(edt_pin.getText().toString().trim().length()<4){
            btn_signin.setEnabled(false);
            btn_signin.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        }
        else if(spn_uname.getSelectedItemPosition()==0){
            btn_signin.setEnabled(false);
            btn_signin.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        }
        else
        {
            btn_signin.setEnabled(true);
            btn_signin.setBackgroundColor(getResources().getColor(R.color.dark_blue));
        }
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


}
