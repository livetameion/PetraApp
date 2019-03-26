package com.pos.petra.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pos.petra.app.Model.Roles.Admin;
import com.pos.petra.app.Model.Roles.Cashiers;
import com.pos.petra.app.Model.Roles.LoadCashiers;
import com.pos.petra.app.Model.Roles.Merchant;
import com.pos.petra.app.Util.Constants;
import com.pos.petra.app.Util.CustomDialog;
import com.pos.petra.app.Util.SingleTon;
import com.pos.petra.app.Util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.pos.petra.app.Views.TypefacedButton;
import com.pos.petra.app.Views.TypefacedTextView;

public class SplashActivity extends GlobalActivity {
    private Utils util;
    private CustomDialog cd;
    private Merchant deviceData;
    private TypefacedButton login_now;
    private RoundCornerProgressBar progrss_bar;
    private CountDownTimer timer;
    private TypefacedTextView text_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        util = new Utils(this);
        cd = new CustomDialog(this);
        progrss_bar =  findViewById(R.id.progrss_bar);
        text_loading = findViewById(R.id.text_loading);
        timer = new CountDownTimer(10000, 10) {
            public void onTick(long millisUntilFinished) {
                progrss_bar.setProgress(100 - millisUntilFinished / 10);
            }
            public void onFinish() {
                timer.start();
            }
        }.start();
        login_now = findViewById(R.id.login_now);
        login_now.setVisibility(View.GONE);
        login_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //   progressBar =  findViewById(R.id.progressBar);
        callDeviceDataApi();
    }

    //API call for getting device information.
    private void callDeviceDataApi() {
        if (util.checkInternetConnection()) {
            //  progressBar.setVisibility(View.VISIBLE);
            JSONObject jar = new JSONObject();
            Log.e("deviceinfo", Constants.loginUrl);
            Log.e("getImageInfo", "" + jar);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.loginUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.e("deviceinfo", "" + response);
                            try {
                                deviceData = new Merchant(new JSONObject(response).getJSONObject("data"));
                                String merchantId = deviceData.merchant_id;
                                String locationId = deviceData.merchant_location_id;
                                callUserApi(locationId, merchantId);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            cd.hide();
                            Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();

                    /*For now this is static device id but in future it will be the dynamic id.
                     Every merchant id have its owns device id*/

                    params.put("deviceid", "4C4C4544-0052-4D10-8037-B9C04F344C32");
                    params.put("action", "checkdevice");
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


    //Device id gives location id and location id gives merchants details. Merchant details contains list of admins and cashiers
    private void callUserApi(final String locationId, final String merchantId) {
        if (util.checkInternetConnection()) {
            Log.e("loadcashiers", Constants.cashierDataUrl);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.cashierDataUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            timer.cancel();
                            timer = new CountDownTimer(3000, 30) {

                                public void onTick(long millisUntilFinished) {
                                    progrss_bar.setProgress(100 - millisUntilFinished / 30);
                                }

                                public void onFinish() {
                                    progrss_bar.setVisibility(View.GONE);
                                    text_loading.setVisibility(View.GONE);
                                    login_now.setVisibility(View.VISIBLE);
                                }

                            }.start();

                            Log.e("loadcashiers", "" + response);
                            try {
                                SingleTon.cashierData = new LoadCashiers(new JSONObject(response).getJSONObject("data"));
                                ArrayList<Admin> admins = SingleTon.cashierData.admins;
                                ArrayList<Cashiers> cashiers = SingleTon.cashierData.cashiers;

                                //SigleTon class Saves data of admin and cashier list for current session in static variable
                                SingleTon.admins = new ArrayList<>();
                                SingleTon.admins.add("Select Admin");
                                SingleTon.cashiers = new ArrayList<>();
                                SingleTon.cashiers.add("Select Cashier");
                                for (int i = 0; i < cashiers.size(); i++)
                                    SingleTon.cashiers.add(cashiers.get(i).name);
                                for (int i = 0; i < admins.size(); i++)
                                    SingleTon.admins.add(admins.get(i).admin_name);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            cd.hide();
                            Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("merchantlocationid", "" + locationId);
                    params.put("merchantid", "" + merchantId);
                    params.put("action", "loadcashiers");

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
