package com.pos.petra.app.Admin.Charity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.pos.petra.app.Util.PrefUtil;
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
 * <p>
 * A Fragment for Charity screen inside Admin Login
 */


public class CharityFragment extends Fragment {
    private ListView charity_lv;
    private LinearLayout ly_pagination_btn;
    private ImageView txt_previous;
    private ImageView txt_next;
    private TextView txtview;
    private HorizontalScrollView hsv;
    private int increment;
    public int TOTAL_LIST_ITEMS;
    public int NUM_ITEMS_PAGE;
    private int noOfBtns;
    private TextView[] btns;
    private SharedPreferences common_mypref;
    private Utils util;
    private CustomDialog progressDialog;
    private JSONArray users;
    private View rootView;
    private TypefacedTextView txt_page_no;
    private EditText edt_name;
    private EditText edt_e_id, edt_add, edt_ac_number;
    private EditText edt_phone;
    private EditText edt_email;
    private TextView edt_donated_amountt;
    private Button btn_create;
    private Dialog dialogCreateCharity;

    //Empty Cunstructor
    public CharityFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //If View is loading First time then it will initialize
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_charity, container, false);
            common_mypref = getActivity().getSharedPreferences("user", 0);
            util = new Utils(getActivity());
            progressDialog = new CustomDialog(getActivity());
            charity_lv =  rootView.findViewById(R.id.lv_charity);
            ly_pagination_btn =  rootView.findViewById(R.id.ly_pagination_btn);
            txt_previous = rootView.findViewById(R.id.previous);
            txt_next = rootView.findViewById(R.id.next);
            txtview =  rootView.findViewById(R.id.txtview);
            hsv =  rootView.findViewById(R.id.horizontalScrollview);
            ly_pagination_btn =  rootView.findViewById(R.id.ly_pagination_btn);
            Button btn_add_charity = rootView.findViewById(R.id.btn_add_charity);
            btn_add_charity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogAddCharity();
                }
            });
            txt_page_no = rootView.findViewById(R.id.txt_page_no);

            //Request API for getting Charity details from Server
            loadCharityData();
        }

        return rootView;
    }

    //Function to set pagination buttons
    public void Btnfooter(final JSONArray array) {
        increment = 0;
        ly_pagination_btn.removeAllViews();
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
            ly_pagination_btn.addView(btns[i], lp);
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
            CharityListViewAdapter listAdapter = new CharityListViewAdapter(getActivity(), sort, this);
            charity_lv.setAdapter(listAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // API Call to server
    public void loadCharityData() {
        if (util.checkInternetConnection()) {
            progressDialog.show("");
            Log.e("loadTrans", Constants.charity);
            //  Log.e("loadTrans", ""+jar);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.charity,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Response from Server
                            progressDialog.hide();
                            Log.e("loadTrans", "" + response);
                            try {
                                users = (new JSONObject(response)).getJSONArray("data");
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
                            progressDialog.hide();
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    // Prepare the payload
                    Map<String, String> params = new HashMap<>();
                    JSONObject jjobj = new JSONObject();
                    try {
                        jjobj.put("merchant_location_id", "" + AdminListActivity.merchant_location_id);
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
    public void dialogAddCharity() {
        dialogCreateCharity = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.dialog_charity_detail, null);
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialogCreateCharity.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        edt_name =  rowView.findViewById(R.id.edt_name);
        edt_ac_number =  rowView.findViewById(R.id.edt_ac_number);
        edt_add =  rowView.findViewById(R.id.edt_add);
        edt_e_id =  rowView.findViewById(R.id.edt_e_id);
        edt_phone =  rowView.findViewById(R.id.edt_str_phone);
        edt_email =  rowView.findViewById(R.id.edt_email);
        edt_donated_amountt =  rowView.findViewById(R.id.edt_donated_amountt);
        edt_donated_amountt.setText("");
        btn_create =  rowView.findViewById(R.id.btn_create);
        btn_create.setBackgroundColor(getResources().getColor(R.color.dark_grey));
        btn_create.setTextColor(getResources().getColor(R.color.white));
        btn_create.setText("Create");
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadCharityDetails();
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

        edt_donated_amountt.addTextChangedListener(new TextWatcher() {
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
        edt_add.addTextChangedListener(new TextWatcher() {
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
        edt_ac_number.addTextChangedListener(new TextWatcher() {
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
                dialogCreateCharity.dismiss();
            }
        });

        dialogCreateCharity.setContentView(rowView);
        dialogCreateCharity.setCancelable(true);
        dialogCreateCharity.setCanceledOnTouchOutside(true);
        dialogCreateCharity.show();

    }

    public boolean validation() {
        if (edt_name.getText().toString().trim().length() == 0) {
            btn_create.setEnabled(false);
            btn_create.setBackgroundColor(getResources().getColor(R.color.dark_grey));
            btn_create.setTextColor(getResources().getColor(R.color.white));
            return false;
        } else if (edt_phone.getText().length() < 10) {
            btn_create.setEnabled(false);
            btn_create.setBackgroundColor(getResources().getColor(R.color.dark_grey));
            btn_create.setTextColor(getResources().getColor(R.color.white));
            return false;
        } else if (edt_add.getText().length() < 4) {
            btn_create.setEnabled(false);
            btn_create.setBackgroundColor(getResources().getColor(R.color.dark_grey));
            btn_create.setTextColor(getResources().getColor(R.color.white));
            return false;
        } else if (edt_ac_number.getText().length() < 4) {
            btn_create.setEnabled(false);
            btn_create.setBackgroundColor(getResources().getColor(R.color.dark_grey));
            btn_create.setTextColor(getResources().getColor(R.color.white));
            return false;
        } else {
            btn_create.setEnabled(true);
            btn_create.setBackgroundColor(getResources().getColor(R.color.dark_green));
            btn_create.setTextColor(getResources().getColor(R.color.white));

            return true;
        }
    }

    //API call to send created charity data to server
    private void uploadCharityDetails() {
        if (util.checkInternetConnection()) {
            progressDialog.show("");
            Log.e("uploadCharity", Constants.charity);
            //  Log.e("loadTrans", ""+jar);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.charity,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.hide();
                            Log.e("uploadCharity", "" + response);
                            try {
                                if ((new JSONObject(response)).getString("status").equalsIgnoreCase("1")) {
                                    dialogCreateCharity.dismiss();
                                    loadCharityData();
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
                            progressDialog.hide();
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    //Prepare payload
                    Map<String, String> paramss_pair = new HashMap<String, String>();
                    JSONArray params_array = new JSONArray();
                    JSONObject params = new JSONObject();
                    try {
                        params.put("name", edt_name.getText().toString().trim());
                        params.put("address", edt_add.getText().toString().trim());
                        params.put("mentor", edt_e_id.getText().toString().trim());
                        params.put("account_no", edt_ac_number.getText().toString().trim());
                        params.put("phone", edt_phone.getText().toString().trim());
                        params.put("email", edt_email.getText().toString().trim());
                        params.put("amount", edt_donated_amountt.getText().toString().trim());
                        params_array.put(params);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JSONObject jsonObject_parent = new JSONObject();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        String admin_id = "";
                        String merchant_location_id = "";
                        try {
                            JSONObject jobj = new JSONObject(PrefUtil.getLoginData(common_mypref));
                            admin_id = jobj.getJSONObject("admin").getString("admin_id");
                            merchant_location_id = jobj.getJSONObject("admin").getString("merchant_location_id");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        jsonObject.put("merchant_location_id", merchant_location_id);
                        jsonObject.put("admin_id", admin_id);
                        jsonObject.put("charity", params_array);
                        jsonObject_parent.put("data", jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    paramss_pair.put("data", jsonObject_parent.toString());
                    paramss_pair.put("action", "create");
                    Log.e("getParams", String.valueOf(paramss_pair));
                    return paramss_pair;
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
