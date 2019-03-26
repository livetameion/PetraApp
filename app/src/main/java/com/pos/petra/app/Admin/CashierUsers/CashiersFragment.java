package com.pos.petra.app.Admin.CashierUsers;

import android.app.Dialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 */
public class CashiersFragment extends Fragment {
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
    private CashierListViewAdapter listAdapter;
    private Utils util;
    private CustomDialog cd;
    private JSONArray users;
    public static boolean isUpdated = false;
    private Button btn_add_cashier;
    private View rootView;
    private TypefacedTextView txt_page_no;


    JSONObject user;
    EditText edt_name;
    EditText edt_e_id;
    EditText edt_phone;
    EditText edt_email;
    EditText edt_pin;
    Spinner spn_isactive;
    Button btn_update;

    LinearLayout ly_isactive;

    private Dialog dialogCreateCashier;

    public CashiersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_cashiers, container, false);
            util = new Utils(getActivity());
            cd = new CustomDialog(getActivity());
            listView = rootView.findViewById(R.id.listView);
            ly_btn = rootView.findViewById(R.id.ly_pagination_btn);
            txt_previous = rootView.findViewById(R.id.previous);
            txt_next = rootView.findViewById(R.id.next);
            txtview = rootView.findViewById(R.id.txtview);
            hsv = rootView.findViewById(R.id.horizontalScrollview);
            ly_btn = rootView.findViewById(R.id.ly_pagination_btn);
            btn_add_cashier = rootView.findViewById(R.id.btn_add_cashier);
            btn_add_cashier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogAddCashier();
                }
            });
            txt_page_no =  rootView.findViewById(R.id.txt_page_no);
            loadUsers();

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
                txt_page_no.setText("" + btns[i].getText().toString());

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
            listAdapter = new CashierListViewAdapter(getActivity(), sort, this);
            listView.setAdapter(listAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void loadUsers() {
        if (util.checkInternetConnection()) {

            Log.e("loadTrans", Constants.merchantDataUrl);
            //  Log.e("loadTrans", ""+jar);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.merchantDataUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            cd.hide();
                            Log.e("loadTrans", "" + response);
                            try {
                                users = (new JSONObject(response)).getJSONObject("data").getJSONArray("cashiers");
                                NUM_ITEMS_PAGE = 8;
                                TOTAL_LIST_ITEMS = users.length();
                                Btnfooter(users);
                                CheckBtnBackGroud(0);
                                setListView(0, users);

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
                    params.put("locationid", "" + AdminListActivity.merchant_location_id);
                    params.put("merchantid", "" + AdminListActivity.merchant_id);

                    params.put("action", "loadcashiers");
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
                public void retry(VolleyError error) {

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);

        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.dataconnection), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    //Dialog for shoiwng final total of transaction
    public void dialogAddCashier() {
        dialogCreateCashier = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rowView =  inflater.inflate(R.layout.activity_user_detail, null);
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialogCreateCashier.getWindow().setBackgroundDrawableResource(R.color.translucent_black);

        edt_name = (EditText) rowView.findViewById(R.id.edt_name);
        edt_e_id = (EditText) rowView.findViewById(R.id.edt_e_id);
        edt_phone = (EditText) rowView.findViewById(R.id.edt_str_phone);
        edt_email = (EditText) rowView.findViewById(R.id.edt_email);
        edt_pin = (EditText) rowView.findViewById(R.id.edt_pin);
        edt_pin.setText("");
        ly_isactive = rowView.findViewById(R.id.ly_isactive);
        ly_isactive.setVisibility(View.GONE);
        spn_isactive = (Spinner) rowView.findViewById(R.id.spn_active);
        btn_update = rowView.findViewById(R.id.btn_update);
        btn_update.setBackgroundColor(getResources().getColor(R.color.dark_grey));
        btn_update.setTextColor(getResources().getColor(R.color.white));
        btn_update.setText("Create");
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadCashier();
            }
        });
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

    public boolean validation() {
        if (edt_name.getText().toString().trim().length() == 0) {
            btn_update.setEnabled(false);
            btn_update.setBackgroundColor(getResources().getColor(R.color.dark_grey));
            btn_update.setTextColor(getResources().getColor(R.color.white));
            return false;
        } else if (edt_phone.getText().length() < 10) {
            btn_update.setEnabled(false);
            btn_update.setBackgroundColor(getResources().getColor(R.color.dark_grey));
            btn_update.setTextColor(getResources().getColor(R.color.white));
            return false;
        } else if (edt_pin.getText().length() < 4) {
            btn_update.setEnabled(false);
            btn_update.setBackgroundColor(getResources().getColor(R.color.dark_grey));
            btn_update.setTextColor(getResources().getColor(R.color.white));
            return false;
        } else {
            btn_update.setEnabled(true);
            btn_update.setBackgroundColor(getResources().getColor(R.color.dark_green));
            btn_update.setTextColor(getResources().getColor(R.color.white));
            return true;
        }
    }

    private void uploadCashier() {
        if (util.checkInternetConnection()) {
            JSONObject jar = new JSONObject();
            cd.show("");
            Log.e("uploadCashier", Constants.cashierDataUrl);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.cashierDataUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            cd.hide();
                            Log.e("uploadCashier", "" + response);
                            try {
                                if ((new JSONObject(response)).getString("status").equalsIgnoreCase("1")) {
                                    dialogCreateCashier.dismiss();
                                    loadUsers();
                                } else {
                                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("cashiername", edt_name.getText().toString().trim());
                    params.put("empid", edt_e_id.getText().toString().trim());
                    params.put("cashierphone", edt_phone.getText().toString().trim());
                    params.put("email", edt_email.getText().toString().trim());
                    params.put("cashierpin", edt_pin.getText().toString().trim());
                    params.put("locationid", AdminListActivity.merchant_location_id);
                    params.put("merchantid", AdminListActivity.merchant_id);
                    params.put("action", "addcashier");
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
