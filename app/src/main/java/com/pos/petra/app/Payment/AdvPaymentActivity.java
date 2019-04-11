package com.pos.petra.app.Payment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.pos.petra.app.AdvancedCashier.CashierDashboardActivity;
import com.pos.petra.app.AdvancedCashier.DataKeeper;
import com.pos.petra.app.Device.PrintActivity;
import com.pos.petra.app.GlobalActivity;
import com.pos.petra.app.Model.Roles.Cashiers;
import com.pos.petra.app.Model.Roles.Customer;
import com.pos.petra.app.Model.Roles.Merchant;
import com.pos.petra.app.R;
import com.pos.petra.app.Util.Constants;
import com.pos.petra.app.Util.CustomDialog;
import com.pos.petra.app.Util.PrefUtil;
import com.pos.petra.app.Util.Utils;
import com.pos.petra.app.Views.TypefacedButton;
import com.pos.petra.app.Views.TypefacedEditText;
import com.pos.petra.app.Views.TypefacedTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AdvPaymentActivity extends GlobalActivity {

    private static final String EXTRA_TRANSACTION_RESULT = "transactionResult";
    private static final String EXTRA_TRANSACTION_UNIQUE_ID = "transactionUniqueId";
    private static final String EXTRA_RECEIPT_ID = "receiptId";
    private static final String EXTRA_PARTIAL_AUTH = "isPartialAuth";
    private static final String EXTRA_REQUESTED_AMOUNT = "requestedAmount";
    private static final String EXTRA_AUTHORIZED_AMOUNT = "authorizedAmount";
    public static final String EXTRA_TRANSACTION_ACTION = "transactionAction";
    private SharedPreferences common_mypref;
    private Utils util;
    private TypefacedButton btn_signin;
    public TypefacedEditText edt_pin, edt_cust_id;
    private Spinner spn_user_type;
    private String[] list_user_types = new String[]{Constants.CUSTOMER, Constants.MERCHANT};
    private CustomDialog progressbar;
    private Button btn_continue;
    private TypefacedTextView txt_amount;
    private TypefacedTextView txt_org_total;
    private LinearLayout ly_sum;
    private LinearLayout ly_login_header, ly_token_5, ly_token_10;
    private TypefacedTextView txt_token_credit_store, txt_token_price, txt_subtotal, txt_cur_credit, txt_points, txt_canpurchase;
    private TypefacedTextView txt_token_rate;
    private LinearLayout ly_buy_token;
    private LinearLayout ly_sigin_view, ly_sigup_view, ly_applied_store_credit, ly_applied_credit;
    private EditText edt_signup_email, edt_signup_pin, edt_pin_confirm, edt_signup_phone;
    private TypefacedButton btn_signup_signup;
    private TypefacedButton btn_signup_signin;
    private TypefacedButton btn_signup;
    private LinearLayout ly_store_credit;
    private LinearLayout ly_prelogin_buttons;
    private Button btn_cancel_1, btn_continue_1;
    private Dialog loginDialog;
    double amount;
    private String transaction_id;
    private String cust_id = "";
    private String login_type = "";
    private String cust_name = Constants.GUEST;
    private String cust_type = Constants.GUEST;
    private double tokensInWallet = 0;
    private double remainingTokens = 0;
    private TypefacedTextView txt_applied_sotr_credit;
    private TypefacedTextView btn_delete_applied;
    private String tokenPrice;
    private TypefacedEditText edt_token;
    private double cust_points = 0;
    protected CharSequence[] arr_store_credits;
    private int applied_credit_position;
    private TypefacedTextView btn_delete_store_credit;
    private int applied_token = 0;
    private double applied_store_credit = 0;
    private Dialog payByDialog;
    private String merchantid;
    private double amount_to_pay = 0;
    private double amount_charity = 0;
    private Dialog finalCheckDialog;
    private String locationid;
    private LinearLayout ly_login_view, ly_num_tokens;
    private CardView cv_token_5, cv_token_10;
    private DecimalFormat df;
    private String vpTokens;
    private String vpFee;
    private String vpRate;
    private int vpAccepted = 0;
    private double withFee = 0.00;
    private double card_time_amount = 0.00;
    private EditText edt_name;
    private ImageView img_maintaglogo;
    private LinearLayout ly_resize;
    private LinearLayout ly_postlogin_buttons;
    private DecimalFormat fc;
    private DecimalFormat dc;
    private double tipAmount = 0.00;
    private int tipAccepted = 0;
    private AppBarLayout app_bar;
    private Toolbar toolbar;
    private ImageView petra;
    private String tip_status;
    private RecyclerView list_products;
    private RecyclerView.LayoutManager recyleLayoutManager;
    private PayMentProductAdapter adapter;
    private DlgProductAdapter dlg_adapter;
    private String charity_id;
    private String charity_name = "";
    private Map<String, String> voucher;
    public static ArrayList<String> voucharProdcutString = new ArrayList<>();
    public static ArrayList<String> voucharString = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }
        super.onCreate(savedInstanceState);
        common_mypref = getApplicationContext().getSharedPreferences("user", 0);
        setContentView(R.layout.activity_adv_payment);
        util = new Utils(this);
        progressbar = new CustomDialog(this);
        df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);
        fc = new DecimalFormat("#,###,###,###,##0.00");
        dc = new DecimalFormat("#,###,###,###,###");

        txt_amount = findViewById(R.id.txt_amount);
        ly_login_header = findViewById(R.id.ly_login_header);
        ly_store_credit = findViewById(R.id.ly_store_credit);
        ly_buy_token = findViewById(R.id.ly_buy_token);
        ly_applied_credit = findViewById(R.id.ly_applied_credit);
        ly_applied_store_credit = findViewById(R.id.ly_applied_store_credit);
        ly_num_tokens = findViewById(R.id.ly_num_tokens);
        ly_sum = findViewById(R.id.ly_sum);
        ly_prelogin_buttons = findViewById(R.id.ly_prelogin_buttons);
        ly_postlogin_buttons = findViewById(R.id.ly_postlogin_buttons);
        txt_org_total = findViewById(R.id.txt_org_total);
        txt_token_credit_store = findViewById(R.id.txt_token_credit_store);
        txt_token_price = findViewById(R.id.txt_token_price);
        txt_subtotal = findViewById(R.id.txt_subtotal);
        txt_cur_credit = findViewById(R.id.txt_cur_credit);
        txt_points = findViewById(R.id.txt_points);
        txt_canpurchase = findViewById(R.id.txt_canpurchase);
        txt_cur_credit = findViewById(R.id.txt_cur_credit);
        edt_token = findViewById(R.id.edt_token);
        ly_token_5 = findViewById(R.id.ly_token_5);
        ly_token_10 = findViewById(R.id.ly_token_10);
        btn_continue = findViewById(R.id.btn_continue);
        btn_delete_applied = findViewById(R.id.btn_delete_applied);
        txt_applied_sotr_credit = findViewById(R.id.txt_applied_sotr_credit);
        btn_delete_store_credit = findViewById(R.id.btn_delete_store_credit);
        btn_continue_1 = findViewById(R.id.btn_continue_1);
        btn_cancel_1 = findViewById(R.id.btn_cancel_1);
        txt_token_rate = findViewById(R.id.txt_token_rate);
        cv_token_5 = findViewById(R.id.cv_token_5);
        cv_token_10 = findViewById(R.id.cv_token_10);
        ly_resize = findViewById(R.id.ly_resize);
        app_bar = (AppBarLayout) findViewById(R.id.app_bar);
        toolbar = findViewById(R.id.toolbar);
        petra = findViewById(R.id.petra);
        list_products = (RecyclerView) findViewById(R.id.prod_list);
        Button btn_cancel = findViewById(R.id.btn_cancel);
        CardView btn_logout = findViewById(R.id.btn_logout);
        TypefacedTextView btn_login = findViewById(R.id.btn_login);
        TypefacedButton btn_store_credit = findViewById(R.id.btn_store_credit);


        ly_login_header.setVisibility(View.VISIBLE);
        ly_store_credit.setVisibility(View.GONE);
        ly_buy_token.setVisibility(View.GONE);
        ly_applied_credit.setVisibility(View.GONE);
        ly_applied_store_credit.setVisibility(View.GONE);
        ly_sum.setVisibility(View.GONE);
        ly_prelogin_buttons.setVisibility(View.VISIBLE);
        ly_postlogin_buttons.setVisibility(View.GONE);
        app_bar.setVisibility(View.GONE);

        btn_store_credit.setOnClickListener(mClickListner);
        ly_token_5.setOnClickListener(mClickListner);
        ly_token_10.setOnClickListener(mClickListner);
        btn_continue.setOnClickListener(mClickListner);
        btn_cancel.setOnClickListener(mClickListner);
        btn_continue_1.setOnClickListener(mClickListner);
        btn_cancel_1.setOnClickListener(mClickListner);
        btn_logout.setOnClickListener(mClickListner);
        btn_login.setOnClickListener(mClickListner);
        btn_delete_applied.setOnClickListener(mClickListner);
        btn_delete_store_credit.setOnClickListener(mClickListner);


        setSupportActionBar(toolbar);
        recyleLayoutManager = createLayoutManager(getResources());
        list_products.setLayoutManager(recyleLayoutManager);

        amount = Double.parseDouble(getIntent().getStringExtra("price"));
        merchantid = getIntent().getStringExtra("merchantid");
        locationid = getIntent().getStringExtra("locationid");
        txt_amount.setText("$" + String.format("%.2f", amount));
        try {
            transaction_id = (new JSONObject(getIntent().getStringExtra("data"))).getString("data");
            tokenPrice = (new JSONObject(getIntent().getStringExtra("data"))).getString("tokenprice");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        txt_org_total.setText("$" + String.format("%.2f", amount));
        txt_token_rate.setText("Rate is $" + tokenPrice + "/Store Credit");

        edt_token.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                    applyToken(Integer.parseInt(charSequence.toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        edt_token.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        // Identifier of the action. This will be either the identifier you supplied,
                        // or EditorInfo.IME_NULL if being called due to the enter key being pressed.
                        if (actionId == EditorInfo.IME_ACTION_SEARCH
                                || actionId == EditorInfo.IME_ACTION_DONE
                                || event.getAction() == KeyEvent.ACTION_DOWN
                                && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (cust_type.equalsIgnoreCase(Constants.MERCHANT)) {
                                dialogCashExchangeAlert();
                            }
                            return true;
                        }
                        // Return true if you have consumed the action, else false.
                        return false;
                    }
                });


        fillProductDetails();
    }


////////////////////////////-------Logic & Alter Views Section----------------/////////////////////////////////////

    //Layout Manger for managig the oriantation of Layoutmanger
    private RecyclerView.LayoutManager createLayoutManager(Resources resources) {
        int spans = 1;/*resources.getInteger(R.integer.feed_columns);*/
        return new StaggeredGridLayoutManager(spans, RecyclerView.VERTICAL);
    }

    //Products detais will be fetch from static varible saved in CashierDashboardActivity.
    private void fillProductDetails() {
        if (DataKeeper.products_array.size() > 0) {

            if (adapter == null) {
                list_products.setAdapter(adapter = new PayMentProductAdapter(AdvPaymentActivity.this, DataKeeper.products_array));
            } else {
                adapter.notifyDataSetChanged();
            }
            list_products.setVisibility(View.VISIBLE);

        } else {

            list_products.setVisibility(View.GONE);
        }
    }

    // Registred views click event will be hanldled from below code
    private View.OnClickListener mClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ly_token_5:
                    edt_token.setText("" + 5);
                    if (cust_type.equalsIgnoreCase(Constants.MERCHANT)) {
                        dialogCashExchangeAlert();
                    }
                    break;
                case R.id.ly_token_10:
                    edt_token.setText("" + 10);
                    if (cust_type.equalsIgnoreCase(Constants.MERCHANT)) {
                        dialogCashExchangeAlert();
                    }
                    break;
                case R.id.btn_store_credit:
                    //
                    if (tokensInWallet <= 0) {
                        dialogFillCredit();
                    } else {
                        dialogStoreCredit();
                    }
                    break;
                case R.id.btn_continue:
                    dialogPayMethodChoice();
                    break;
                case R.id.btn_cancel:
                    dialogAskForLogout();
                    break;
                case R.id.btn_continue_1:
                    dialogPayMethodChoice();
                    break;
                case R.id.btn_cancel_1:
                    dialogAskForLogout();
                    break;
                case R.id.btn_logout:
                    viewLoginLogout(0);
                    break;
                case R.id.btn_login:
                    dialogCustomerLogin();
                    break;
                case R.id.btn_delete_applied:
                    edt_token.setText("" + 0);
                    break;
                case R.id.btn_delete_store_credit:
                    applyStroeCrdit(0);
                    break;
            }
        }
    };


    //Method for applying store credit by pressing 5,10 and custom entry
    private void applyToken(int token) {
        if (token > Double.parseDouble(txt_canpurchase.getText().toString())) {
            dialogTokenLimit();
            return;
        }
        applied_token = token;
        if (token == 0) {
            ly_applied_credit.setVisibility(View.GONE);
            edt_token.setText("");
        } else {
            ly_applied_credit.setVisibility(View.VISIBLE);
        }
        txt_token_credit_store.setText(token + " Store Credit @ $" + tokenPrice);
        txt_token_price.setText("$" + String.format("%.2f", Float.parseFloat(tokenPrice) * applied_token));
        txt_subtotal.setText("$" + String.format("%.2f", amount + (Float.parseFloat(tokenPrice) * applied_token)));
        txt_amount.setText("$" + String.format("%.2f", amount - applied_store_credit + (Float.parseFloat(tokenPrice) * applied_token)));
    }

    //Pop for offer cheap crdeits
    private void dialogCheapCredits(final double amount_to_pay, final String payMethod) {


        final Dialog cheapCreditDialog = new Dialog(AdvPaymentActivity.this, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.dialog_cheap_credit, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        cheapCreditDialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        final TypefacedTextView msg_cheap_credit = rowView.findViewById(R.id.msg_cheap_credit);
        msg_cheap_credit.setText("Get " + vpTokens + " Store Credit for only $" + vpFee + "\nThat's only $" + vpRate + " per Store Credit!");
        TypefacedTextView btn_ok = rowView.findViewById(R.id.btn_ok);
        TypefacedTextView btn_cancel = rowView.findViewById(R.id.btn_cancel);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                withFee = (amount_to_pay) + Float.parseFloat(vpFee);
                vpAccepted = 1;
                if (tip_status.equalsIgnoreCase("1"))
                    dialogAskForTip(withFee, payMethod);
                else
                    promptCharityOrNot(withFee, payMethod);
                cheapCreditDialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                withFee = amount_to_pay;
                vpAccepted = 0;
                if (tip_status.equalsIgnoreCase("1"))
                    dialogAskForTip(withFee, payMethod);
                else
                    promptCharityOrNot(withFee, payMethod);
                cheapCreditDialog.dismiss();

            }
        });
        LinearLayout ly_pay = rowView.findViewById(R.id.ly_pay);
        ly_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cheapCreditDialog.dismiss();
            }
        });
        cheapCreditDialog.setContentView(rowView);
        cheapCreditDialog.setCancelable(true);
        cheapCreditDialog.setCanceledOnTouchOutside(true);
        cheapCreditDialog.show();
    }

    //API call for loading merchant prefrences for calculate cheap credits offer
    private void callLoadPrefrences(final double amount_to_pay, final String payMethod) {
        if (util.checkInternetConnection()) {
            JSONObject jar = new JSONObject();
            progressbar.show("");
            Log.e("deviceinfo", Constants.loginUrl);
            Log.e("getImageInfo", "" + jar);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.loginUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressbar.hide();
                            Log.e("deviceinfo", "" + response);
                            try {
                                Merchant merchant = new Merchant(new JSONObject(response).getJSONObject("data"));
                                double advertising = Double.parseDouble(merchant.advertising);
                                double processing = Double.parseDouble(merchant.processing);
                                double paper = Double.parseDouble(merchant.paper);
                                double loyalty = Double.parseDouble(merchant.loyalty);
                                double rewards = Double.parseDouble(merchant.rewards);
                                double website = Double.parseDouble(merchant.website);
                                double monthly_transactions = Double.parseDouble(merchant.monthly_transactions);
                                charity_id = merchant.charity_id;
                                int discount = Integer.parseInt(merchant.vp_discount);

                                Double total = advertising +
                                        processing +
                                        paper +
                                        loyalty +
                                        rewards +
                                        website;
                                double vp = Math.round(total / monthly_transactions);
                                vpTokens = "" + Math.round(vp);
                                double d = (vp * discount / 100);
                                double vp_price = vp - d;
                                vpFee = fc.format(vp_price);
                                vpRate = String.format("%.2f", (vp_price / vp));
                                tip_status = merchant.tip_status;
                                if (cust_type.equalsIgnoreCase(Constants.GUEST)) {
                                    if (tip_status.equalsIgnoreCase("1"))
                                        dialogAskForTip(amount_to_pay, payMethod);
                                    else
                                        promptCharityOrNot(amount_to_pay, payMethod);
                                } else {
                                    if (payMethod.equalsIgnoreCase(Constants.PAYMETHOD_EXCHANGE)) {
                                        checkExchange();
                                    } else {
                                        dialogCheapCredits(amount_to_pay, payMethod);
                                    }

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressbar.hide();
                            Toast.makeText(AdvPaymentActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
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
            RequestQueue requestQueue = Volley.newRequestQueue(AdvPaymentActivity.this);
            requestQueue.add(stringRequest);

        } else {
            Toast.makeText(AdvPaymentActivity.this, getResources().getString(R.string.dataconnection), Toast.LENGTH_LONG).show();
        }

    }

    //Alert for MerchantCustomer for not buying store credit using Exchange method
    private void checkExchange() {
        if (applied_token > 0) {
            edt_token.setText("" + 0);

            final Dialog cantBuySCDialog = new Dialog(AdvPaymentActivity.this, android.R.style.Theme_Black_NoTitleBar);
            LayoutInflater inflater = getLayoutInflater();
            View rowView = (View) inflater.inflate(R.layout.dialog_alert_ok, null);
            Rect displayRectangle = new Rect();
            Window window = getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            cantBuySCDialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
            final TypefacedTextView title = rowView.findViewById(R.id.title);
            title.setText(getResources().getString(R.string.notice));
            final TypefacedTextView msg = rowView.findViewById(R.id.msg);
            msg.setText(getResources().getString(R.string.msg_not_buy_sc));
            TypefacedTextView btn_ok = rowView.findViewById(R.id.btn_ok);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cantBuySCDialog.dismiss();
                    payByDialog.dismiss();
                }
            });

            LinearLayout ly_pay = rowView.findViewById(R.id.ly_pay);
            ly_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cantBuySCDialog.dismiss();
                }
            });
            cantBuySCDialog.setContentView(rowView);
            cantBuySCDialog.setCancelable(true);
            cantBuySCDialog.setCanceledOnTouchOutside(true);
            cantBuySCDialog.show();
            //
        } else {


            final Dialog roundupTokenDialog = new Dialog(AdvPaymentActivity.this, android.R.style.Theme_Black_NoTitleBar);
            LayoutInflater inflater = getLayoutInflater();
            View rowView = (View) inflater.inflate(R.layout.dialog_alert_yes_no, null);
            Rect displayRectangle = new Rect();
            Window window = getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            roundupTokenDialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
            final TypefacedTextView title = rowView.findViewById(R.id.title);
            title.setText(getResources().getString(R.string.notice));
            final TypefacedTextView msg = rowView.findViewById(R.id.msg);
            msg.setText(getResources().getString(R.string.msg_roundup_exchange));
            TypefacedTextView btn_ok = rowView.findViewById(R.id.btn_ok);
            TypefacedTextView btn_cancel = rowView.findViewById(R.id.btn_cancel);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exchangeMethod();
                    roundupTokenDialog.dismiss();
                }
            });
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    roundupTokenDialog.dismiss();

                }
            });
            LinearLayout ly_pay = rowView.findViewById(R.id.ly_pay);
            ly_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    roundupTokenDialog.dismiss();
                }
            });
            roundupTokenDialog.setContentView(rowView);
            roundupTokenDialog.setCancelable(true);
            roundupTokenDialog.setCanceledOnTouchOutside(true);
            roundupTokenDialog.show();
        }
    }

    // When User clicks Exahange Pay mentho. Especially Merchant-Customer
    private void exchangeMethod() {
        double cePrice = Math.ceil(amount);
        applied_store_credit = (cePrice);
        amount_to_pay = applied_store_credit;
        //dialogAskForTip(amount_to_pay, Constants.PAYMETHOD_EXCHANGE);
        if (tip_status.equalsIgnoreCase("1"))
            dialogAskForTip(amount_to_pay, Constants.PAYMETHOD_EXCHANGE);
        else
            promptCharityOrNot(amount_to_pay, Constants.PAYMETHOD_EXCHANGE);
    }

    // If amount is not round figure and admin set Charity frecipenat then
    private void promptCharityOrNot(double amount_to_pay, String paymethod) {
        if (amount_to_pay != Math.floor(amount_to_pay)) {
            dialogAskCharity(amount_to_pay, paymethod);
        } else {
            dialogFinalCheck2(amount_to_pay, paymethod);
        }
    }

    //Validation before sending details of fresh customer
    private boolean validationSignup() {
        if (edt_name.getText().toString().trim().length() < 1) {
            btn_signup_signup.setEnabled(false);
            btn_signup_signup.setBackgroundColor(getResources().getColor(R.color.dark_grey));
            return false;
        }
        if (edt_signup_phone.getText().toString().trim().length() < 14) {
            btn_signup_signup.setEnabled(false);
            btn_signup_signup.setBackgroundColor(getResources().getColor(R.color.dark_grey));
            return false;
        } else if ((android.text.TextUtils.isEmpty(edt_signup_email.getText().toString()) || !android.util.Patterns.EMAIL_ADDRESS.matcher(edt_signup_email.getText().toString()).matches())) {
            btn_signup_signup.setEnabled(false);
            btn_signup_signup.setBackgroundColor(getResources().getColor(R.color.dark_grey));
            return false;
        } else if (edt_pin_confirm.getText().toString().length() < 4) {
            btn_signup_signup.setEnabled(false);
            btn_signup_signup.setBackgroundColor(getResources().getColor(R.color.dark_grey));
            return false;
        } else if (edt_signup_phone.getText().toString().trim().length() < 14) {
            btn_signup_signup.setEnabled(false);
            btn_signup_signup.setBackgroundColor(getResources().getColor(R.color.dark_grey));
            return false;
        } else if (!edt_signup_pin.getText().toString().equalsIgnoreCase(edt_pin_confirm.getText().toString())) {
            btn_signup_signup.setEnabled(false);
            btn_signup_signup.setBackgroundColor(getResources().getColor(R.color.dark_grey));
            return false;
        } else {
            btn_signup_signup.setEnabled(true);
            btn_signup_signup.setBackgroundColor(getResources().getColor(R.color.light_blue));
            return true;
        }

    }

    //validation for credential befor customer signin
    private boolean validation() {
        if (edt_cust_id.getText().toString().trim().length() < 14) {
            btn_signin.setEnabled(false);
            btn_signin.setBackgroundColor(getResources().getColor(R.color.dark_grey));
            return false;
        } else if (edt_pin.getText().length() < 4) {
            btn_signin.setEnabled(false);
            btn_signin.setBackgroundColor(getResources().getColor(R.color.dark_grey));
            return false;
        } else {
            btn_signin.setEnabled(true);
            btn_signin.setBackgroundColor(getResources().getColor(R.color.light_blue));
            return true;
        }
    }

    //Manage the view on Login or Logout
    private void viewLoginLogout(int i) {
        if (i == 1) {
            ly_login_header.setVisibility(View.GONE);
            app_bar.setVisibility(View.VISIBLE);
            ly_buy_token.setVisibility(View.VISIBLE);
            ly_sum.setVisibility(View.VISIBLE);
            if (!getResources().getBoolean(R.bool.portrait_only))
                petra.setVisibility(View.GONE);
            ly_store_credit.setVisibility(View.VISIBLE);
            ly_prelogin_buttons.setVisibility(View.GONE);
            ly_postlogin_buttons.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ly_resize.setLayoutParams(layoutparams);

        } else {
            txt_amount.setText("$" + amount);
            applied_token = 0;
            applied_store_credit = 0;
            ly_applied_store_credit.setVisibility(View.GONE);
            edt_token.setText("");
            ly_login_header.setVisibility(View.VISIBLE);
            app_bar.setVisibility(View.GONE);
            petra.setVisibility(View.VISIBLE);
            ly_buy_token.setVisibility(View.GONE);
            ly_sum.setVisibility(View.GONE);
            ly_store_credit.setVisibility(View.GONE);
            ly_prelogin_buttons.setVisibility(View.VISIBLE);
            ly_postlogin_buttons.setVisibility(View.GONE);
            LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ly_resize.setLayoutParams(layoutparams);
        }
    }

    //Set points earned by coustmer which was fetched from serve
    private void setPoints(JSONObject data) {
        try {
            if (cust_points < 100) {
                edt_token.setEnabled(false);
                edt_token.setClickable(false);
                ly_num_tokens.setVisibility(View.GONE);
                txt_token_rate.setVisibility(View.GONE);
            } else {
                edt_token.setEnabled(true);
                edt_token.setClickable(true);
                ly_num_tokens.setVisibility(View.VISIBLE);
                txt_token_rate.setVisibility(View.VISIBLE);
            }

            if (cust_points >= 500)
                cv_token_5.setVisibility(View.VISIBLE);
            else
                cv_token_5.setVisibility(View.GONE);

            if (cust_points >= 1000)
                cv_token_10.setVisibility(View.VISIBLE);
            else
                cv_token_10.setVisibility(View.GONE);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Fill the wallet details of customer(Feathers)
    private void setupCustomerFeatheres(JSONObject transRetriveed) {
        try {
            cust_name = transRetriveed.getJSONObject("data").getJSONObject("customer").getString("name") + "!";
            Spannable text = new SpannableStringBuilder("Welcome, " + cust_name);
            util.setSpans(text, "Welcome, ".length(), "Welcome, ".length() + cust_name.length());
            toolbar.setTitle(text);

            txt_org_total.setText("$" + String.format("%.2f", amount));
            ly_applied_credit.setVisibility(View.GONE);
            txt_subtotal.setText("$" + String.format("%.2f", amount));
            if (!transRetriveed.getJSONObject("data").getJSONObject("customer").isNull("feathers"))
                tokensInWallet = Float.parseFloat(transRetriveed.getJSONObject("data").getJSONObject("customer").getString("feathers"));
            remainingTokens = tokensInWallet;
            txt_cur_credit.setText("" + Math.round(tokensInWallet));
            if (cust_type.equalsIgnoreCase(Constants.CUSTOMER)) {
                callCustomerPointDetailsApi();
            } else {
                callMerchantPointDetailsApi();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Apply store credit on press of button or o type in to
    private void applyStroeCrdit(double store_credit) {
        applied_store_credit = store_credit;
        if (store_credit == 0) {
            ly_applied_store_credit.setVisibility(View.GONE);
        } else {
            ly_applied_store_credit.setVisibility(View.VISIBLE);
        }
        txt_applied_sotr_credit.setText("" + Math.round(applied_store_credit));
        txt_cur_credit.setText("" + Math.round(tokensInWallet - applied_store_credit));
        txt_amount.setText("$" + String.format("%.2f", remainingTokens = (amount - applied_store_credit + (Float.parseFloat(tokenPrice) * applied_token))));
    }

    //PayAnywhere Transaction respose  will be parsed here
    private void showTransactionResponses(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            String transactionResult = data.getStringExtra(EXTRA_TRANSACTION_RESULT);
            String transactionUniqueId = data.getStringExtra(EXTRA_TRANSACTION_UNIQUE_ID);
            long receiptId = data.getLongExtra(EXTRA_RECEIPT_ID, 0);
            String transactionAction = data.getStringExtra(EXTRA_TRANSACTION_ACTION);
            boolean isPartialAuth = data.getBooleanExtra(EXTRA_PARTIAL_AUTH, false);

            BigDecimal requestedAmount = null;
            if (data.getSerializableExtra(EXTRA_REQUESTED_AMOUNT) != null) {
                requestedAmount = (BigDecimal) data.getSerializableExtra(EXTRA_REQUESTED_AMOUNT);
            }
            BigDecimal authorizedAmount = null;
            if (data.getSerializableExtra(EXTRA_AUTHORIZED_AMOUNT) != null) {
                authorizedAmount = (BigDecimal) data.getSerializableExtra(EXTRA_AUTHORIZED_AMOUNT);
            }
            Toast.makeText(getApplicationContext(), "--Result: " + transactionResult +
                    "\nTrans_id: " + transactionUniqueId +
                    "\nreceiptId: " + receiptId +
                    "\ntransactionAction: " + transactionAction +
                    "\nisPartialAuth: " + isPartialAuth +
                    "\nrequestedAmount: " + requestedAmount +
                    "\nauthorizedAmount: " + authorizedAmount, Toast.LENGTH_LONG).show();
            if (isPartialAuth) {

                Toast.makeText(getApplicationContext(), "Opps! Partial Transaction!", Toast.LENGTH_LONG).show();
            }

            if (transactionResult.equalsIgnoreCase("transactionApproved") || transactionResult.equalsIgnoreCase("Approved")) {
                //finalCheckDialog.dismiss();
                callFinalPaymentApi(card_time_amount, Constants.PAYMETHOD_CARD, new String[]{transactionResult, transactionUniqueId, "" + receiptId, "" + isPartialAuth});
            } else if (transactionResult.equalsIgnoreCase("transactionDeclined") || transactionResult.equalsIgnoreCase("Declined")) {
                Toast.makeText(getApplicationContext(), "You declined the Payment", Toast.LENGTH_LONG).show();
            } else if (transactionResult.equalsIgnoreCase("transactionCancelled") || transactionResult.equalsIgnoreCase("Cancelled")) {
                Toast.makeText(getApplicationContext(), "Opps! Transaction has been cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

////////////////////////////-------Dialog Section----------------/////////////////////////////////////

    //Kill Payment Activity
    public void finishPaymentActivity() {
        Intent intent = new Intent(AdvPaymentActivity.this, CashierDashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        DataKeeper.products_array.clear();
        DataKeeper.product_name_selected.clear();
        DataKeeper.product_id_selected.clear();
        finish();
    }

    //Dialog for fresh customer who have zero store credits in wallet
    private void dialogFillCredit() {
        final Dialog dialog = new Dialog(AdvPaymentActivity.this, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.dialog_alert_ok, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        final TypefacedTextView title = rowView.findViewById(R.id.title);
        title.setText(getResources().getString(R.string.title_no_storecredit));
        final TypefacedTextView msg = rowView.findViewById(R.id.msg);
        msg.setText(getResources().getString(R.string.msg_no_storecredit));
        TypefacedTextView btn_ok = rowView.findViewById(R.id.btn_ok);
        TypefacedTextView btn_cancel = rowView.findViewById(R.id.btn_cancel);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        LinearLayout ly_pay = rowView.findViewById(R.id.ly_pay);
        ly_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(rowView);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    //Dialog for alerting a Merchant-customer from other store who might new to this store or with zero credit.
    private void dialogFillCreditMerchant() {
        final Dialog dialog = new Dialog(AdvPaymentActivity.this, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.dialog_alert_yes_no, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        final TypefacedTextView title = rowView.findViewById(R.id.title);
        title.setText(getResources().getString(R.string.title_no_storecredit));
        final TypefacedTextView msg = rowView.findViewById(R.id.msg);
        msg.setText(getResources().getString(R.string.title_no_storecredit_merchant));
        TypefacedTextView btn_ok = rowView.findViewById(R.id.btn_ok);
        btn_ok.setText("Yes");
        TypefacedTextView btn_cancel = rowView.findViewById(R.id.btn_cancel);
        btn_cancel.setText("No");
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callLoadPrefrences(amount_to_pay, Constants.PAYMETHOD_EXCHANGE);
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        LinearLayout ly_pay = rowView.findViewById(R.id.ly_pay);
        ly_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(rowView);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }

    //Pop up for cancel transaction confirmation
    private void dialogAskForLogout() {
        final Dialog dialog = new Dialog(AdvPaymentActivity.this, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.dialog_alert_yes_no, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        final TypefacedTextView title = rowView.findViewById(R.id.title);
        title.setText(getResources().getString(R.string.title_cancel_transaction));
        final TypefacedTextView msg = rowView.findViewById(R.id.msg);
        msg.setText("              " + getResources().getString(R.string.msg_cancel_transaction) + "              ");
        TypefacedTextView btn_ok = rowView.findViewById(R.id.btn_ok);
        btn_ok.setText("Yes");
        TypefacedTextView btn_cancel = rowView.findViewById(R.id.btn_cancel);
        btn_cancel.setText("No");
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callCancelTransactionApi();

                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });
        LinearLayout ly_pay = rowView.findViewById(R.id.ly_pay);
        ly_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(rowView);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    // Alert to Merchant customer of buying of store value
    private void dialogCashExchangeAlert() {
        final Dialog dialog = new Dialog(AdvPaymentActivity.this, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.dialog_alert_ok, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        final TypefacedTextView title = rowView.findViewById(R.id.title);
        title.setText(getResources().getString(R.string.notice));
        final TypefacedTextView msg = rowView.findViewById(R.id.msg);
        msg.setText(getResources().getString(R.string.ce_notice));
        TypefacedTextView btn_ok = rowView.findViewById(R.id.btn_ok);
        TypefacedTextView btn_cancel = rowView.findViewById(R.id.btn_cancel);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        LinearLayout ly_pay = rowView.findViewById(R.id.ly_pay);
        ly_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(rowView);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    //Alert for exceeded entry of token limit purchase
    private void dialogTokenLimit() {
        final Dialog dialog = new Dialog(AdvPaymentActivity.this, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.dialog_alert_ok, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        final TypefacedTextView title = rowView.findViewById(R.id.title);
        title.setText(getResources().getString(R.string.notice));
        final TypefacedTextView msg = rowView.findViewById(R.id.msg);
        msg.setText("You can only buy up to " + txt_canpurchase.getText().toString() + " tokens!");
        TypefacedTextView btn_ok = rowView.findViewById(R.id.btn_ok);
        TypefacedTextView btn_cancel = rowView.findViewById(R.id.btn_cancel);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        LinearLayout ly_pay = rowView.findViewById(R.id.ly_pay);
        ly_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(rowView);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    //Pop up for Choice of PayMethod Card, Cash CreditExchange
    private void dialogPayMethodChoice() {
        payByDialog = new Dialog(AdvPaymentActivity.this, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.dialog_choose_pay_new, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        TypefacedTextView txt_total_due = rowView.findViewById(R.id.txt_total_due);
        txt_total_due.setText(txt_amount.getText().toString());
        LinearLayout white_seprator = rowView.findViewById(R.id.white_seprator);
        Button btn_exchange = rowView.findViewById(R.id.btn_exchange);
        if (!cust_type.equalsIgnoreCase(Constants.MERCHANT)) {
            btn_exchange.setVisibility(View.GONE);
            white_seprator.setVisibility(View.GONE);
        }
        btn_exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tokensInWallet <= 0 || tokensInWallet <= amount_to_pay) {
                    dialogFillCreditMerchant();
                } else {
                    callLoadPrefrences(amount_to_pay, Constants.PAYMETHOD_EXCHANGE);
                }
                payByDialog.dismiss();
            }
        });
        payByDialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        LinearLayout ly_empty = rowView.findViewById(R.id.ly_empty);
        ly_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payByDialog.dismiss();
            }
        });
        Button btn_cash = rowView.findViewById(R.id.btn_cash);
        btn_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payByDialog.dismiss();
                amount_to_pay = Double.parseDouble(txt_amount.getText().toString().replace("$", "").trim());

                callLoadPrefrences(amount_to_pay, Constants.PAYMETHOD_CASH);
            }
        });
        Button btn_card = rowView.findViewById(R.id.btn_card);
        btn_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payByDialog.dismiss();
                amount_to_pay = Double.parseDouble(txt_amount.getText().toString().replace("$", "").trim());

                callLoadPrefrences(amount_to_pay, Constants.PAYMETHOD_CARD);
               /* Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse(PAYMENT_URL +  "?chargeAmount="));
                startActivityForResult(intent, PAYMENT_REQUEST_CODE);*/
            }
        });
        payByDialog.setContentView(rowView);
        payByDialog.setCancelable(true);
        payByDialog.setCanceledOnTouchOutside(true);
        payByDialog.show();
    }

    //If total amount figure is not perfect number then we can ask for round up amount as charity
    protected void dialogAskCharity(final double withfee, final String paymethod) {
        if (charity_id.equalsIgnoreCase("")) {
            amount_charity = 0.00;
            dialogFinalCheck2(withfee, paymethod);
        } else {
            if (util.checkInternetConnection()) {
                JSONObject jar = new JSONObject();
                progressbar.show("");
                Log.e("deviceinfo", Constants.charity);
                Log.e("getImageInfo", "" + jar);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.charity,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressbar.hide();
                                Log.e("deviceinfo", "" + response);
                                try {

                                    JSONArray charity_arr = (new JSONObject(response)).getJSONArray("data");


                                    for (int i = 0; i < charity_arr.length(); i++)
                                        if (charity_arr.getJSONObject(i).getString("id").equalsIgnoreCase(charity_id)) {
                                            charity_name = charity_arr.getJSONObject(i).getString("name");
                                            break;
                                        }


                                    final Dialog charityDialog = new Dialog(AdvPaymentActivity.this, android.R.style.Theme_Black_NoTitleBar);
                                    LayoutInflater inflater = getLayoutInflater();
                                    View rowView = (View) inflater.inflate(R.layout.dialog_roundup_charity, null);
                                    Rect displayRectangle = new Rect();
                                    Window window = getWindow();
                                    window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
                                    charityDialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
                                    final TypefacedTextView msg_cheap_credit = rowView.findViewById(R.id.msg_cheap_credit);
                                    msg_cheap_credit.setText("As a valued customer I am asking you to help \"" + charity_name + "\" as they are very special and very much in need. \n \nThank-you in advance.");
                                    TypefacedTextView btn_ok = rowView.findViewById(R.id.btn_ok);
                                    TypefacedTextView btn_cancel = rowView.findViewById(R.id.btn_cancel);
                                    btn_ok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            amount_charity = Math.ceil(withfee) - withfee;
                                            double amount_final = (float) (amount_charity + withfee);
                                            dialogFinalCheck2(amount_final, paymethod);
                                            charityDialog.dismiss();
                                        }
                                    });
                                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            amount_charity = 0.00;
                                            dialogFinalCheck2(withfee, paymethod);
                                            charityDialog.dismiss();

                                        }
                                    });
                                    LinearLayout ly_pay = rowView.findViewById(R.id.ly_pay);
                                    ly_pay.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            charityDialog.dismiss();
                                        }
                                    });
                                    charityDialog.setContentView(rowView);
                                    charityDialog.setCancelable(true);
                                    charityDialog.setCanceledOnTouchOutside(true);
                                    charityDialog.show();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressbar.hide();
                                Toast.makeText(AdvPaymentActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();

                        JSONObject jjobj = new JSONObject();
                        try {
                            jjobj.put("merchant_location_id", "" + locationid);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        params.put("data", jjobj.toString());
                        params.put("action", "view");

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
                RequestQueue requestQueue = Volley.newRequestQueue(AdvPaymentActivity.this);
                requestQueue.add(stringRequest);

            } else {
                Toast.makeText(AdvPaymentActivity.this, getResources().getString(R.string.dataconnection), Toast.LENGTH_LONG).show();
            }


        }
    }

    //If Store is about Restaurant type and Admin has set the tip flag 'true' then user will prompted for tip
    public void dialogAskForTip(final double amount, final String paymethod) {

        final Dialog tipDialog = new Dialog(AdvPaymentActivity.this, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.dialog_tip_box, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        tipDialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        final TypefacedEditText input = rowView.findViewById(R.id.edt_tip);
        TypefacedTextView btn_ok = rowView.findViewById(R.id.btn_ok);
        TypefacedTextView btn_cancel = rowView.findViewById(R.id.btn_cancel);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input.getText().toString().trim().length() > 0) {
                    double amount_final = amount;
                    tipAccepted = 1;
                    tipAmount = Double.parseDouble(input.getText().toString().trim());
                    amount_final = amount_final + tipAmount;
                    promptCharityOrNot(amount_final, paymethod);
                    tipDialog.dismiss();
                } else {
                    input.setError("Enter amount please!");
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipAccepted = 0;
                tipAmount = 0.0;
                tipDialog.dismiss();
                double amount_final = amount;
                promptCharityOrNot(amount_final, paymethod);

            }
        });
        LinearLayout ly_pay = rowView.findViewById(R.id.ly_pay);
        ly_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipDialog.dismiss();
            }
        });
        tipDialog.setContentView(rowView);
        tipDialog.setCancelable(true);
        tipDialog.setCanceledOnTouchOutside(true);
        tipDialog.show();

    }

    //cashier authenticaton befor proceeding final payment API
    public void dialogPinConfrim(final double amount, final String paymethod) {


        final Dialog pinDialog = new Dialog(AdvPaymentActivity.this, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.dialog_pin_confirm, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        pinDialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        final TypefacedEditText input = rowView.findViewById(R.id.edt_pin);
        TypefacedTextView btn_ok = rowView.findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinDialog.dismiss();
                callCashierLoginApi(input.getText().toString().trim(), amount, paymethod);
            }
        });
        LinearLayout ly_pay = rowView.findViewById(R.id.ly_pay);
        ly_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinDialog.dismiss();
            }
        });
        pinDialog.setContentView(rowView);
        pinDialog.setCancelable(true);
        pinDialog.setCanceledOnTouchOutside(true);
        pinDialog.show();

    }

    //Customer Login dialog
    private void dialogCustomerLogin() {
        loginDialog = new Dialog(AdvPaymentActivity.this, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.dialog_login_view, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        loginDialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        ly_login_view = rowView.findViewById(R.id.ly_login_view);
        ly_login_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginDialog != null)
                    loginDialog.dismiss();
            }
        });
        ly_sigup_view = rowView.findViewById(R.id.ly_sigup_view);
        ly_sigin_view = rowView.findViewById(R.id.ly_sigin_view);


        TypefacedTextView id_login_title = rowView.findViewById(R.id.id_login_title);
        id_login_title.setVisibility(View.VISIBLE);

        img_maintaglogo = rowView.findViewById(R.id.img_maintaglogo);
        img_maintaglogo.setImageResource(R.mipmap.fc_logo);
        btn_signin = rowView.findViewById(R.id.btn_signin);
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = "customer";
                if (spn_user_type.getSelectedItemPosition() != 0)
                    type = "merchant";
                String phone = edt_cust_id.getText().toString().replace(" ", "").replace("(", "").replace(")", "").replace("-", "");
                String pin = edt_pin.getText().toString();

                callLoginApi(type, phone, pin);
            }
        });
        edt_cust_id = rowView.findViewById(R.id.edt_cust_id);
        edt_cust_id.setVisibility(View.VISIBLE);
        edt_cust_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().startsWith("(") && charSequence.toString().replace("(", "")
                        .replace(")", "")
                        .replace("-", "")
                        .replace(" ", "").length() == 10) {
                    edt_cust_id.setText("(" + charSequence.toString().substring(0, 3) + ") " + charSequence.toString().substring(3, 6) + "-" + charSequence.toString().substring(6));
                    edt_cust_id.setSelection(edt_cust_id.getText().length());
                }
                validation();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        edt_pin = rowView.findViewById(R.id.edt_pin);
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


        spn_user_type = (Spinner) rowView.findViewById(R.id.spn_type);
        spn_user_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                validation();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<String> uTyperArrayAdapter = new ArrayAdapter<String>(this, R.layout.item_spn_selected, list_user_types);
        uTyperArrayAdapter.setDropDownViewResource(R.layout.item_spn);
        spn_user_type.setAdapter(uTyperArrayAdapter);

        btn_signup = rowView.findViewById(R.id.btn_signup);
        btn_signup.setVisibility(View.VISIBLE);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ly_sigin_view.setVisibility(View.GONE);
                ly_sigup_view.setVisibility(View.VISIBLE);

            }
        });
        edt_name = rowView.findViewById(R.id.edt_name);

        edt_signup_phone = rowView.findViewById(R.id.edt_signup_phone);
        edt_signup_pin = rowView.findViewById(R.id.edt_signup_pin);
        edt_signup_email = rowView.findViewById(R.id.edt_signup_email);

        edt_pin_confirm = rowView.findViewById(R.id.edt_pin_confirm);
        btn_signup_signup = rowView.findViewById(R.id.btn_signup_signup);

        btn_signup_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSignUpApi();
            }
        });

        btn_signup_signin = rowView.findViewById(R.id.btn_signup_signin);
        btn_signup_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ly_sigin_view.setVisibility(View.VISIBLE);
                ly_sigup_view.setVisibility(View.GONE);
            }
        });

        edt_signup_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().startsWith("(") && charSequence.toString().replace("(", "")
                        .replace(")", "")
                        .replace("-", "")
                        .replace(" ", "").length() == 10) {
                    edt_signup_phone.setText("(" + charSequence.toString().substring(0, 3) + ") " + charSequence.toString().substring(3, 6) + "-" + charSequence.toString().substring(6));
                    edt_signup_phone.setSelection(edt_signup_phone.getText().length());

                }
                validationSignup();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        edt_signup_pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validationSignup();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        edt_pin_confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validationSignup();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        edt_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validationSignup();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        loginDialog.setContentView(rowView);
        loginDialog.setCancelable(true);
        loginDialog.setCanceledOnTouchOutside(true);
        loginDialog.show();


    }

    //Dialog for use a store credit. Require a amount greater than $1.
    protected void dialogStoreCredit() {
        double usableFeathers = Math.min(Math.floor(amount), tokensInWallet);

        if (amount > 1) {
            arr_store_credits = new String[(int) usableFeathers];
            for (int i = 0; i < usableFeathers; i++) {
                arr_store_credits[i] = "" + (i + 1);
            }

            final AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                    .setSingleChoiceItems(arr_store_credits, 0, null)
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                            applied_credit_position = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                            applyStroeCrdit(Math.min(
                                    Double.parseDouble(arr_store_credits[applied_credit_position].toString()),
                                    Math.round(tokensInWallet)));
                            // Do something useful withe the position of the selected radio button
                        }
                    });
            dialog.setCancelable(true);
            dialog.show();


        } else {
            Toast.makeText(AdvPaymentActivity.this, getResources().getString(R.string.less_amount), Toast.LENGTH_LONG).show();
        }

    }

    //Dialog for shoiwng final total of transaction
    private void dialogFinalCheck2(final double amount_final, final String paymethod) {
        voucharProdcutString.clear();
        finalCheckDialog = new Dialog(AdvPaymentActivity.this, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.dialog_adv_final_totalcheck, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        finalCheckDialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        TypefacedTextView txt_pay_method = rowView.findViewById(R.id.txt_pay_method);
        txt_pay_method.setText(paymethod);
        voucharString.add("Transaction No.: "+transaction_id);

        voucharString.add("Paymethod : "+paymethod);

        TypefacedTextView txt_pay_org_price = rowView.findViewById(R.id.txt_ref_org_price);
        txt_pay_org_price.setText("$" + String.format("%.2f", amount));
        voucharString.add("Origninal Price:  $" + String.format("%.2f", amount));


        TypefacedTextView txt_token_detail = rowView.findViewById(R.id.txt_ref_token_detail);
        TypefacedTextView txt_token = rowView.findViewById(R.id.txt_token);
        LinearLayout ly_normal_token = rowView.findViewById(R.id.ly_normal_token);
        LinearLayout ly_pay = rowView.findViewById(R.id.ly_pay);
        ly_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalCheckDialog.dismiss();
            }
        });

        LinearLayout ly_charity = rowView.findViewById(R.id.ly_charity);
        TypefacedTextView txt_charity_place = rowView.findViewById(R.id.txt_charity_place);







        LinearLayout ly_insert_credit = rowView.findViewById(R.id.ly_insert_credit);
        TypefacedTextView txt_store_credit = rowView.findViewById(R.id.txt_inserted_token);
        txt_store_credit.setText("-$" + String.format("%.2f", applied_store_credit));


        TypefacedTextView txt_you_pay = rowView.findViewById(R.id.txt_you_pay);

        if (amount_charity > 0)
        {
            ly_charity.setVisibility(View.VISIBLE);
            txt_charity_place.setText("Charity for " + charity_name);
            TypefacedTextView txt_charity_amount = rowView.findViewById(R.id.txt_charity_amount);
            txt_charity_amount.setText("$" + String.format("%.2f", amount_charity));
            voucharString.add("Charity for " + charity_name+":   $" + String.format("%.2f", amount_charity));

        }
        else
            ly_charity.setVisibility(View.GONE);

        LinearLayout ly_tip = rowView.findViewById(R.id.ly_tip);
        TypefacedTextView txt_tip_amout = rowView.findViewById(R.id.txt_tip_amount);
        if (tipAccepted == 1) {
            ly_tip.setVisibility(View.VISIBLE);
            txt_tip_amout.setText("$" + String.format("%.2f", tipAmount));
            voucharString.add("Tip " +":  " + "$" +String.format("%.2f", tipAmount));


        } else
            ly_tip.setVisibility(View.GONE);

        LinearLayout ly_vp_token = rowView.findViewById(R.id.ly_vp_token);
        TypefacedTextView txt_vptoken_detail = rowView.findViewById(R.id.txt_vptoken_detail);
        TypefacedTextView txt_vptoken = rowView.findViewById(R.id.txt_vptoken);
        if (vpAccepted == 1) {
            txt_vptoken_detail.setText(vpTokens + " Store Credit @ $" + vpRate);
            txt_vptoken.setText("$" + vpFee);
            ly_vp_token.setVisibility(View.VISIBLE);
            voucharString.add(vpTokens + " Store Credit @ $" + vpRate +":  " + "$" +vpFee);

        } else {
            ly_vp_token.setVisibility(View.GONE);
        }

        if (cust_type.equalsIgnoreCase(Constants.MERCHANT) && paymethod.equalsIgnoreCase(Constants.PAYMETHOD_EXCHANGE)) {
            txt_token_detail.setText("Exchange Price");
            txt_token.setText(Math.round(amount_final) + " Tokens");
            txt_you_pay.setText(Math.round(amount_final) + " Tokens");
            voucharString.add("Exchange Price " +"   " + String.format("%.2f", Math.round(amount_final)));
            voucharString.add("Price You Pay " +"   " + String.format("%.2f", Math.round(amount_final)));

            ly_insert_credit.setVisibility(View.GONE);
        } else {
            if (applied_token > 0) {
                ly_normal_token.setVisibility(View.VISIBLE);
                txt_token_detail.setText(txt_token_credit_store.getText().toString());
                txt_token.setText("$" + String.format("%.2f", Float.parseFloat(tokenPrice) * applied_token));
                voucharString.add(txt_token_credit_store.getText().toString()+" : " +"$" + String.format("%.2f", Float.parseFloat(tokenPrice) * applied_token));
            } else {
                ly_normal_token.setVisibility(View.GONE);
            }
            if (applied_store_credit > 0) {
                ly_insert_credit.setVisibility(View.VISIBLE);
                voucharString.add("Inserted Store Credit:  $" + String.format("%.2f", applied_store_credit));

            } else
                ly_insert_credit.setVisibility(View.GONE);
            txt_you_pay.setText("$" + String.format("%.2f", amount_final));
            voucharString.add("Price You Pay " +":  " + "$" + String.format("%.2f", amount_final));

        }

        TypefacedButton btn_pay = rowView.findViewById(R.id.btn_pay);
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (paymethod.equalsIgnoreCase(Constants.PAYMETHOD_CARD)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.PAYMENT_URL + "?chargeAmount=" + String.format("%.2f", amount_final) +
                            "&primaryColor=234263&contrastColor=000000&receiptScreen=true"));
                    try {
                        startActivityForResult(intent, Constants.PAYMENT_REQUEST_CODE);
                        card_time_amount = amount_final;
                    } catch (Exception e) {
                        e.printStackTrace();
                        dialogInstallSdk();
                        //Toast.makeText(getApplicationContext(), "Could not found PayAnywhere SDK App installed in your device.", Toast.LENGTH_LONG).show();
                    }

                } else {
                    dialogPinConfrim(amount_final, paymethod);

                }
                //  callFinalPayment(amount_final,paymethod);
            }
        });
        RecyclerView dlg_rec_prodlist = (RecyclerView) rowView.findViewById(R.id.dlg_rec_prodlist);
        dlg_rec_prodlist.setLayoutManager(createLayoutManager(getResources()));
        if (DataKeeper.products_array.size() > 0) {
            if (dlg_adapter == null) {
                dlg_rec_prodlist.setAdapter(dlg_adapter = new DlgProductAdapter(AdvPaymentActivity.this, DataKeeper.products_array));
            } else {
                adapter.notifyDataSetChanged();
            }
            dlg_rec_prodlist.setVisibility(View.VISIBLE);

        } else {

            dlg_rec_prodlist.setVisibility(View.GONE);

        }

        for(int i=0; i<DataKeeper.products_array.size(); i++){

            voucharProdcutString.add((DataKeeper.products_array.get(i).title+"                                ").substring(0,24)+"  (x"+DataKeeper.products_array.get(i).qty+")"+"     "+"$"+DataKeeper.products_array.get(i).new_price+"    $"+DataKeeper.products_array.get(i).price_applied);

        }




        finalCheckDialog.setContentView(rowView);
        finalCheckDialog.setCancelable(true);
        finalCheckDialog.setCanceledOnTouchOutside(true);
        try {
            finalCheckDialog.show();
        } catch (Exception e) {

        }

    }

    //Instructional Dialog to Install Payanywhere SDK
    private void dialogInstallSdk() {

        final Dialog dialog = new Dialog(AdvPaymentActivity.this, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.dialog_install_sdk, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        TypefacedButton btn_cancel = rowView.findViewById(R.id.btn_cancel);
        TypefacedButton btn_ok = rowView.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + Constants.Payanywhere_package)));
                    dialog.dismiss();
                } catch (android.content.ActivityNotFoundException anfe) {

                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + Constants.Payanywhere_package)));

                        dialog.dismiss();
                    } catch (Exception e) {
                        try {
                            Intent viewIntent =
                                    new Intent("android.intent.action.VIEW",
                                            Uri.parse(Constants.Payanywhere_Install_Url));
                            startActivity(viewIntent);
                            dialog.dismiss();
                        } catch (Exception ee) {
                            Toast.makeText(getApplicationContext(), "Unable to Connect Try Again...",
                                    Toast.LENGTH_LONG).show();
                            ee.printStackTrace();
                        }
                    }

                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        LinearLayout ly_pay = rowView.findViewById(R.id.ly_pay);
        ly_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(rowView);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

    }

    //PressMe screen comes after trasaction completition
    private void dialogSuccessExit(String message) {


        final Dialog dialog = new Dialog(AdvPaymentActivity.this, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.dialog_thanx_transaction, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        final TypefacedTextView msg_cheap_credit = rowView.findViewById(R.id.msg_cheap_credit);
        msg_cheap_credit.setText(message);
        TypefacedButton btn_ok = rowView.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        LinearLayout ly_pay = rowView.findViewById(R.id.ly_pay);
        ly_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(rowView);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                PrintActivity prnt =new PrintActivity(AdvPaymentActivity.this);
                prnt.printer(voucharString,voucharProdcutString);
            }
        });
        dialog.show();


    }

////////////////////////////-------API Section----------------/////////////////////////////////////

    //API to for Cashier Authentication
    private void callCashierLoginApi(final String pin, final double amount_final, final String paymethod) {
        if (util.checkInternetConnection()) {
            JSONObject jar = new JSONObject();
            progressbar.hide();
            progressbar.show("");
            Log.e("deviceinfo", Constants.cashierDataUrl);
            Log.e("getImageInfo", "" + jar);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.cashierDataUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressbar.hide();
                            Log.e("deviceinfo", "" + response);
                            try {
                                JSONObject loginData = new JSONObject(response);
                                if (loginData.getInt("status") == 1) {

                                    callFinalPaymentApi(amount_final, paymethod, null);
                                } else {

                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.loginfailed), Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressbar.hide();
                            Toast.makeText(AdvPaymentActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    String loginid, logintype;
                    Cashiers cashiers = null;
                    try {
                        cashiers = new Cashiers((new JSONObject(PrefUtil.getLoginData(common_mypref))).getJSONObject("cashier"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    logintype = "Cashier";
                    loginid = cashiers.cashier_id;


                    Map<String, String> params = new HashMap<>();
                    params.put("action", "login");
                    params.put("loginid", loginid);
                    params.put("pin", pin);
                    params.put("logintype", logintype);

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
                    Toast.makeText(AdvPaymentActivity.this, error.toString(), Toast.LENGTH_LONG).show();

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.dataconnection), Toast.LENGTH_LONG).show();
        }
    }

    //Api call to save transaction data to server
    private void callFinalPaymentApi(final double amount_final, final String paymethod, final String[] transValues) {
        if (util.checkInternetConnection()) {
            progressbar.show("");
            Log.e("complete", Constants.transactionsUrl);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.transactionsUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressbar.hide();
                            Log.e("complete", "" + response);
                            try {
                                // finalCheckDialog.dismiss();
                                JSONObject deviceData = new JSONObject(response);
                                if (deviceData.getInt("status") == 1) {
                                    // Toast.makeText(getApplicationContext(), deviceData.getString("message"), Toast.LENGTH_SHORT).show();

                                    if (finalCheckDialog != null)
                                        finalCheckDialog.dismiss();
                                    if (payByDialog != null)
                                        payByDialog.dismiss();



                                    dialogSuccessExit(deviceData.getString("message"));
                                } else {
                                    Toast.makeText(getApplicationContext(), deviceData.getString("message"), Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressbar.hide();
                            Toast.makeText(AdvPaymentActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {

                    String customerid_text = (cust_type.equalsIgnoreCase(Constants.CUSTOMER) || cust_type.equalsIgnoreCase(Constants.GUEST)) ? "customerid" : "customermerchantid";
                    String totalprice = String.format("%.2f", amount_final);
                    String transactionMethod = (paymethod.equalsIgnoreCase(Constants.PAYMETHOD_EXCHANGE)) ? "cashlessexchange" : "customerpay";
                    String tokensUsed = (paymethod.equalsIgnoreCase(Constants.PAYMETHOD_EXCHANGE)) ? "" + Math.round(amount_final) : "" + applied_store_credit;
                    String tokensRate = (cust_id.length() > 0) ? tokenPrice : "0.00";
                    String points = (cust_id.length() > 0) ? "" + cust_points : "";

                    Map<String, String> params = new HashMap<>();

                    params.put("transaction", transactionMethod);
                    params.put("transaction_id", transaction_id);
                    params.put(customerid_text, (cust_id.length() > 0) ? cust_id : "" + 0);
                    params.put("buytokens", "" + Math.round(applied_token));
                    params.put("buytokensrate", tokensRate);
                    params.put("buytokensprice", "" + Float.parseFloat(tokenPrice) * applied_token);
                    if (vpAccepted == 1) {
                        params.put("vpaccepted", "1");
                        params.put("vpfee", vpFee);
                        params.put("vprate", vpRate);
                        params.put("vptokens", vpTokens);
                    } else {
                        params.put("vpaccepted", "0");
                        params.put("vpaccepted", "0");
                        params.put("vpfee", "0");
                        params.put("vprate", "0");
                        params.put("vptokens", "0");

                    }

                    params.put("charityprice", "" + String.format("%.2f", amount_charity));
                    params.put("pointstotal", points);
                    params.put("originalprice", "" + String.format("%.2f", amount));
                    params.put("subtotal", "" + String.format("%.2f", amount + (Float.parseFloat(tokenPrice) * applied_token)));
                    params.put("totalprice", "" + totalprice);
                    params.put("tokensused", tokensUsed);
                    params.put("tokensinwallet", "" + Math.round(tokensInWallet));
                    params.put("remainingtokens", "" + Math.round(remainingTokens));
                    params.put("transactiontype", "Store");
                    params.put("paymethod", paymethod);
                    params.put("merchantid", merchantid);
                    params.put("locationid", locationid);
                    params.put("tip", String.format("%.2f", tipAmount));
                    params.put("transaction_status", "1");

                    if (paymethod.equalsIgnoreCase(Constants.PAYMETHOD_CARD)) {
                        params.put("patransactionid", transValues[1]);
                        params.put("pareceiptid", transValues[2]);
                        params.put("papaymentsuccess", (transValues[3].equalsIgnoreCase("false") ? transValues[0] : "PartialAuth"));
                    } else {
                        Calendar cal = Calendar.getInstance();
                        String invoicenumber = "" + cal.getTimeInMillis();
                        params.put("patransactionid", "" + 0);
                        params.put("pareceiptid", "" + 0);
                        params.put("papaymentsuccess", "0");
                    }


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

    //API call for Login credential check of customer or merchant
    private void callLoginApi(final String type, final String phone, final String pin) {
        if (util.checkInternetConnection()) {
            progressbar.show("");
            Log.e("callLoginApi", Constants.loginUrl);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.loginUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressbar.hide();
                            Log.e("callLoginApi", "" + response);
                            try {
                                JSONObject data = new JSONObject(response);
                                if (data.getInt("status") == 1) {
                                    loginDialog.dismiss();
                                    Customer customer = new Customer(data.getJSONObject("data"));

                                    Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_SHORT).show();
                                    if (!data.getJSONObject("data").isNull("customer_id")) {
                                        cust_id = customer.customer_id;
                                        login_type = Constants.CUSTOMER;
                                        cust_type = Constants.CUSTOMER;

                                    } else {
                                        cust_id = data.getJSONObject("data").getString("merchant_id");
                                        login_type = Constants.MERCHANT;
                                        cust_type = Constants.MERCHANT;
                                    }
                                    callTransactionRetriveApi(data);
                                } else {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.loginfailed), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressbar.hide();
                            Toast.makeText(AdvPaymentActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<>();
                    params.put("action", "customerlogin");
                    params.put("type", type);
                    params.put("phone", phone);
                    params.put("password", pin);


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

    //Retrive the tranasction details after login by Customer
    private void callTransactionRetriveApi(final JSONObject data) {
        if (util.checkInternetConnection()) {
            progressbar.show("");
            Log.e("TransRetrive", Constants.transactionsUrl);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.transactionsUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressbar.hide();
                            Log.e("TransRetrive", "" + response);
                            try {
                                JSONObject data = new JSONObject(response);
                                if (data.getInt("status") == 1) {
                                    if (data.getJSONObject("data").getJSONObject("merchant").getString("merchant_id").equalsIgnoreCase(cust_id) && cust_type.equalsIgnoreCase(Constants.MERCHANT)) {
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.cannot_purachse_own), Toast.LENGTH_SHORT).show();
                                        loginDialog.dismiss();
                                    } else {
                                        setupCustomerFeatheres(data);
                                        viewLoginLogout(1);
                                        loginDialog.dismiss();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.loginfailed), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressbar.hide();
                            Toast.makeText(AdvPaymentActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("transaction", "retrieve");
                    params.put("transaction_id", transaction_id);
                    String cust_type = "";
                    try {
                        if (!data.getJSONObject("data").isNull("customer_id")) {
                            cust_type = "customerid";
                        } else {
                            cust_type = "customermerchantid";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    params.put(cust_type, cust_id);
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

    //Load Customer points if login type is customer
    private void callCustomerPointDetailsApi() {
        if (util.checkInternetConnection()) {
            JSONObject jar = new JSONObject();
            progressbar.show("");
            String url = Constants.customerUrl + cust_id;
            Log.e("callLoadPointApi", url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressbar.hide();
                            Log.e("callLoadPointApi", "" + response);
                            try {
                                JSONObject data = new JSONObject(response);
                                cust_points = data.getJSONObject("data").getJSONObject("customer").getDouble("points");
                                txt_points.setText(data.getJSONObject("data").getJSONObject("customer").getString("points"));
                                txt_canpurchase.setText("" + (Math.round(cust_points / 100)));
                                setPoints(data);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressbar.hide();
                            Toast.makeText(AdvPaymentActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<>();


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

    //Load merchant's points if login type is Merchant
    private void callMerchantPointDetailsApi() {
        if (util.checkInternetConnection()) {
            progressbar.show("");
            String url = Constants.merchantDataUrl;
            Log.e("callLoadPointApi", url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressbar.hide();
                            Log.e("loadTrans", "" + response);
                            try {

                                JSONObject data = new JSONObject(response);
                                cust_points = data.getJSONObject("data").getJSONObject("merchant").getDouble("points");
                                txt_points.setText(data.getJSONObject("data").getJSONObject("merchant").getString("points"));
                                txt_canpurchase.setText("" + (Math.round(cust_points) / 100));
                                setPoints(data);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressbar.hide();
                            Toast.makeText(AdvPaymentActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "buyingpotential");
                    params.put("customermerchantid", cust_id);

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

    //Signup details of customer - sent to server
    private void callSignUpApi() {
        if (util.checkInternetConnection()) {
            progressbar.show("");
            final String phone = edt_signup_phone.getText().toString().replace(" ", "").replace("(", "").replace(")", "").replace("-", "");
            final String pin = edt_signup_pin.getText().toString();

            String url = Constants.signupUrl;
            Log.e("callSignUpApi", url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressbar.hide();
                            Log.e("callSignUpApi", "" + response);
                            try {

                                JSONObject data = new JSONObject(response);
                                if (data.getInt("status") == 1) {
                                    Toast.makeText(getApplicationContext(), data.getString("message"), Toast.LENGTH_SHORT).show();
                                    ly_sigin_view.setVisibility(View.VISIBLE);
                                    ly_sigup_view.setVisibility(View.GONE);
                                    String type = "customer";
                                    callLoginApi(type, phone, pin);
                                } else {
                                    Toast.makeText(getApplicationContext(), data.getString("message"), Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressbar.hide();
                            Toast.makeText(AdvPaymentActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("type", "signup");
                    params.put("phone", phone);
                    params.put("name", edt_name.getText().toString().trim());
                    params.put("password", pin);
                    params.put("email", edt_signup_email.getText().toString().trim());
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

    //API for cancellling transaction
    private void callCancelTransactionApi() {
        if (util.checkInternetConnection()) {
            JSONObject jar = new JSONObject();
            progressbar.show("");
            Log.e("cancel", Constants.transactionsUrl);
            Log.e("cancel", "" + jar);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.transactionsUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressbar.hide();
                            Log.e("cancel", "" + response);
                            try {
                                JSONObject data = new JSONObject(response);
                                finishPaymentActivity();


                            } catch (JSONException e) {
                                e.printStackTrace();
                                finishPaymentActivity();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressbar.hide();
                            Toast.makeText(AdvPaymentActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("transaction", "customerpay");
                    params.put("transactionid", transaction_id);
                    params.put("customermerchantid", cust_id);
                    params.put("paytype", "Canceled");

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

    //PayAnywhere response comes here
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("PetraPay", "onActivityResult requestCode " + requestCode + " resultCode " + resultCode);
        if (requestCode == Constants.PAYMENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.e("PetraPay", "result ok");
                showTransactionResponses(requestCode, resultCode, data);
            } else if (resultCode == RESULT_CANCELED) {
                Log.e("Petra", "result cancelled");
                showTransactionResponses(requestCode, resultCode, data);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        dialogAskForLogout();
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
