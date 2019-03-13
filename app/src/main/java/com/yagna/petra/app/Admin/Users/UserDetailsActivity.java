package com.yagna.petra.app.Admin.Users;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import com.yagna.petra.app.GlobalActivity;
import com.yagna.petra.app.R;
import com.yagna.petra.app.Util.Constants;
import com.yagna.petra.app.Util.CustomDialog;
import com.yagna.petra.app.Util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link AdminListActivity}.
 */
public class UserDetailsActivity extends GlobalActivity {

    private JSONObject user;
    private EditText edt_name;
    private EditText edt_e_id;
    private EditText edt_phone;
    private EditText edt_email;
    private EditText edt_pin;
    private Spinner spn_isactive;
    private Button btn_update;
    private Utils util;
    private CustomDialog cd;
    private String isactive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        toolbar.setTitle("View/Edit Details");
        setSupportActionBar(toolbar);
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        try {
            user =(new JSONObject(getIntent().getStringExtra("data")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        util=new Utils(this);
        cd = new CustomDialog(this);
        try {
            edt_name =(EditText)findViewById(R.id.edt_name);

            edt_name.setText(user.getString("name"));
            edt_e_id=(EditText)findViewById(R.id.edt_e_id);
            edt_e_id.setText(user.getString("employee_id"));

            edt_phone=(EditText)findViewById(R.id.edt_str_phone);

            edt_phone.setText(user.getString("phone"));
            edt_email=(EditText)findViewById(R.id.edt_email);
            edt_email.setText(user.getString("email"));

            edt_pin=(EditText)findViewById(R.id.edt_pin);
            edt_pin.setText("hola");


            spn_isactive =(Spinner) findViewById(R.id.spn_active);

            if(user.getString("is_active").equalsIgnoreCase("1"))
            {    isactive ="1";
                spn_isactive.setSelection(0);
            }
            else
            {
                isactive ="0";
                spn_isactive.setSelection(1);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        btn_update = (Button) findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadUser();
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


        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //

    }

  public boolean validation(){
      if(edt_name.getText().toString().trim().length()==0){
          btn_update.setEnabled(false);
          btn_update.setBackgroundColor(getResources().getColor(android.R.color.transparent));
          btn_update.setTextColor(getResources().getColor(R.color.black));
          return false;
      }
      else if(edt_phone.getText().length()<10){
          btn_update.setEnabled(false);
          btn_update.setBackgroundColor(getResources().getColor(android.R.color.transparent));
          btn_update.setTextColor(getResources().getColor(R.color.black));
          return false;
      }
      else if(edt_pin.getText().length()<4){
          btn_update.setEnabled(false);
          btn_update.setBackgroundColor(getResources().getColor(android.R.color.transparent));
          btn_update.setTextColor(getResources().getColor(R.color.black));
          return false;
      }
      else
      {
          btn_update.setEnabled(true);
          btn_update.setBackgroundColor(getResources().getColor(R.color.green));
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
                                    UsersFragment.isUpdated=true;
                                    finish();
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
           // navigateUpTo(new Intent(this, AdminListActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
