package com.pos.petra.app.Admin.CashierUsers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
 * Created by Sejal on 14-06-2017.
 */
public class CashierListViewAdapter extends BaseAdapter {
    private final CashiersFragment frag;
    LayoutInflater inflater;
    private Activity context;
    private JSONArray jsonarray;   
    private EditText edt_name;
    private EditText edt_e_id;
    private EditText edt_phone;
    private EditText edt_email;
    private EditText edt_pin;
    private Spinner spn_isactive;
    private Button btn_update;
    private Utils util;
    private CustomDialog cd;
    private Dialog dialogUpdateCashier;

    public CashierListViewAdapter(Activity mainActivity, JSONArray jarry, CashiersFragment usersFragment) {
        context = mainActivity;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        jsonarray = jarry;
        frag = usersFragment;
    }

    @Override
    public int getCount() {
        return jsonarray.length();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }



    public class Holder
    {
        TypefacedTextView list_name, list_e_id, list_email,list_isactive;
        TypefacedTextView list_phone;
        LinearLayout ly_item;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {


        Holder holder=new Holder();
        View rowView;
        rowView=view;
        if(rowView==null)
        {
            rowView = inflater.inflate(R.layout.item_user_list, null);

        }
      
        holder.list_name = rowView.findViewById(R.id.list_name);
        try {
            holder.list_name.setText(jsonarray.getJSONObject(position).getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.list_e_id = rowView.findViewById(R.id.list_e_id);
        try {
            holder.list_e_id.setText(jsonarray.getJSONObject(position).getString("employee_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.list_phone =  rowView.findViewById(R.id.list_phone);
        try {
            holder.list_phone.setText(jsonarray.getJSONObject(position).getString("phone"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        holder.list_email =  rowView.findViewById(R.id.list_email);
        try {
            holder.list_email.setText(jsonarray.getJSONObject(position).getString("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.list_isactive =  rowView.findViewById(R.id.list_isactive);
        try {
            holder.list_isactive.setText((jsonarray.getJSONObject(position).getString("is_active").equalsIgnoreCase("1") ? "Yes" : "No"));
           // LinearLayout la =  rowView.findViewById(R.id.linearLayoutProdottoGenerale);
            //la.setBackgroundResource( ((position % 2) == 0) ? R.color.grey : R.color.white);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.ly_item =  rowView.findViewById(R.id.ly_item);
        holder.ly_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dialogUpdateCashier(jsonarray.getJSONObject(position));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        return rowView;
    }


    //Dialog for shoiwng final total of transaction
    public void dialogUpdateCashier(final JSONObject user) {
        dialogUpdateCashier = new Dialog(context, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_user_detail, null);
        Rect displayRectangle = new Rect();
        Window window = context.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialogUpdateCashier.getWindow().setBackgroundDrawableResource(R.color.translucent_black);

        LinearLayout ly_pay = rowView.findViewById(R.id.ly_pay);
        ly_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogUpdateCashier.dismiss();
            }
        });

        try {
            edt_name =rowView.findViewById(R.id.edt_name);
            edt_name.setText(user.getString("name"));

            edt_e_id=rowView.findViewById(R.id.edt_e_id);
            edt_e_id.setText(user.getString("employee_id"));

            edt_phone=rowView.findViewById(R.id.edt_str_phone);
            edt_phone.setText(user.getString("phone"));

            edt_email=rowView.findViewById(R.id.edt_email);
            edt_email.setText(user.getString("email"));

            edt_pin=rowView.findViewById(R.id.edt_pin);
            edt_pin.setText("hola");


            spn_isactive =(Spinner) rowView.findViewById(R.id.spn_active);
            ArrayAdapter<CharSequence> uTyperArrayAdapter = new ArrayAdapter<>(context, R.layout.item_spn_selected,  context.getResources().getTextArray(R.array.array_isactive));
            uTyperArrayAdapter.setDropDownViewResource(R.layout.item_spn);
            spn_isactive.setAdapter(uTyperArrayAdapter);
            if(user.getString("is_active").equalsIgnoreCase("1"))
            {
                spn_isactive.setSelection(0);
            }
            else
            {

                spn_isactive.setSelection(1);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        btn_update =  rowView.findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateUser(user);
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



        dialogUpdateCashier.setContentView(rowView);
        dialogUpdateCashier.setCancelable(true);
        dialogUpdateCashier.setCanceledOnTouchOutside(true);
        dialogUpdateCashier.show();

    }


      //Api call to update the user details
    private void updateUser(final JSONObject user) {
        util = new Utils(context);
        cd = new CustomDialog(context);

        if (util.checkInternetConnection()) {
            cd.show("");
            Log.e("uploadUser", Constants.cashierDataUrl);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.cashierDataUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            cd.hide();
                            Log.e("uploadUser", ""+response);
                            try {
                                if((new JSONObject(response)).getString("status").equalsIgnoreCase("1")){
                                    dialogUpdateCashier.dismiss();
                                    frag.loadUsers();
                                }
                                else{
                                    Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                   //Preapare te payload
                    Map<String, String> params = new HashMap<>();
                    params.put("employeeid",edt_e_id.getText().toString().trim());
                    try {
                        params.put("cashierid", user.getString("cashier_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    params.put("name",edt_name.getText().toString().trim());
                    params.put("phone",edt_phone.getText().toString().trim());
                    params.put("email",edt_email.getText().toString().trim());
                    if(!edt_pin.getText().toString().trim().equalsIgnoreCase("hola"))
                        params.put("password",edt_pin.getText().toString().trim());
                    params.put("isactive",spn_isactive.getSelectedItemPosition()==0?"1":"0");

                    params.put("action","updatecashier");
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

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);

        }else {
            Toast.makeText(context,context.getResources().getString(R.string.dataconnection),Toast.LENGTH_LONG).show();
        }
    }

    public boolean validation(){
        if(edt_name.getText().toString().trim().length()==0){
            btn_update.setEnabled(false);
            btn_update.setBackgroundColor(context.getResources().getColor(R.color.dark_grey));
            btn_update.setTextColor(context.getResources().getColor(R.color.white));
            return false;
        }
        else if(edt_phone.getText().length()<10){
            btn_update.setEnabled(false);
            btn_update.setBackgroundColor(context.getResources().getColor(R.color.dark_grey));
            btn_update.setTextColor(context.getResources().getColor(R.color.white));
            return false;
        }
        else if(edt_pin.getText().length()<4){
            btn_update.setEnabled(false);
            btn_update.setBackgroundColor(context.getResources().getColor(R.color.dark_grey));
            btn_update.setTextColor(context.getResources().getColor(R.color.white));
            return false;
        }
        else
        {
            btn_update.setEnabled(true);
            btn_update.setBackgroundColor(context.getResources().getColor(R.color.dark_green));
            btn_update.setTextColor(context.getResources().getColor(R.color.white));

            return true;
        }
    }

}
