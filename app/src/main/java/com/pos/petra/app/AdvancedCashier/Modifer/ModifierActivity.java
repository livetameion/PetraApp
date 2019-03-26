package com.pos.petra.app.AdvancedCashier.Modifer;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.pos.petra.app.AdvancedCashier.DataKeeper;
import com.pos.petra.app.ImageCropper.FileUtils;
import com.pos.petra.app.ImageCropper.ImageCropActivity;
import com.pos.petra.app.GlobalActivity;
import com.pos.petra.app.Model.Product.Categories;
import com.pos.petra.app.Model.Product.Modifier;
import com.pos.petra.app.R;
import com.pos.petra.app.Util.Constants;
import com.pos.petra.app.Util.CustomDialog;
import com.pos.petra.app.Util.PrefUtil;
import com.pos.petra.app.Util.TouchImageView;
import com.pos.petra.app.Util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;
import id.zelory.compressor.FileUtil;

public class ModifierActivity extends GlobalActivity {
    public ArrayList<Bitmap> bitmapArray;
    public RecyclerView listview;
    public TextView tv_no_record;
    private ModifierAdapter adapter;
    private JSONObject returnedData;
    private Utils util;
    private CustomDialog cd;
    private ModifierActivity context;
    public int position;
    String productid=null;
    SimpleDateFormat sdf;
    TextView txt_producttitle,product_nam,product_sku;
    private LinearLayout ly_prod_details;
    private RecyclerView.LayoutManager storiesLayoutManager;
    private ArrayList<Modifier> modif_array;
    public int posit_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context= ModifierActivity.this;
        util=new Utils(context);
        tv_no_record =  findViewById(R.id.tv_no_record_);
        util=new Utils(context);
        cd=new CustomDialog(context);
        txt_producttitle=findViewById(R.id.txt_branchtitle);
        txt_producttitle.setText("Modifier");
        tv_no_record =  findViewById(R.id.tv_no_record_);
        ly_prod_details = (LinearLayout) findViewById(R.id.ly_prod_details);
        ly_prod_details.setVisibility(View.VISIBLE);
        product_sku =  findViewById(R.id.product_sku);
        product_nam =  findViewById(R.id.product_nam);
        bitmapArray=new ArrayList<>();
        modif_array = new ArrayList<>();
        listview=(RecyclerView) findViewById(R.id.listview);
        storiesLayoutManager = createLayoutManager(getResources());
        listview.setLayoutManager(storiesLayoutManager);
        ((ImageView)findViewById(R.id.btn_add)).setVisibility(View.GONE);
        fillData(1);
        sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

    }


    private RecyclerView.LayoutManager createLayoutManager(Resources resources) {
        int spans = resources.getInteger(R.integer.feed_columns);
        return new StaggeredGridLayoutManager(spans, RecyclerView.VERTICAL);
    }

    private void fillData(int i){
        Log.e("called by",""+i);
         posit_id = Integer.parseInt(getIntent().getStringExtra("id"));
         String title =  DataKeeper.products_array.get(posit_id).title;
        product_nam.setText(title);
        product_sku.setText(DataKeeper.products_array.get(posit_id).sku);
         modif_array = DataKeeper.products_array.get(posit_id).modifier;
        listview.setAdapter(adapter = new ModifierAdapter(ModifierActivity.this, modif_array));
        if(modif_array.size()!=0) {
            txt_producttitle.setText("Modifier" + " (" + modif_array.size() + ")");
            listview.setVisibility(View.VISIBLE);
            tv_no_record.setVisibility(View.GONE);
        }
        else
        {
            txt_producttitle.setText("Modifier" + "(0)");
            listview.setVisibility(View.GONE);
            tv_no_record.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }
    public void setUrlPayload(int category,String extra) {
        switch (category){

            case Constants.GETLIST:
                JSONObject j_obj = new JSONObject();
                try {
                    j_obj.put("product_id", extra);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                apiWebrequest(Constants.ModifierAPI,j_obj,Constants.GETLIST,Constants.VIEW);
                break;

        }



    }

    //Handle the retrived data from url
    public void setReturnedData(int category, JSONObject j_obj) {
        switch (category){

            case Constants.GETLIST:
                Log.e("Apitransect",""+j_obj);
                JSONArray re_j_obj;
                try {
                    returnedData =j_obj;
                    re_j_obj = returnedData.getJSONArray("data");

                    modif_array.clear();
                    for(int i =0 ;i<re_j_obj.length();i++){
                        try {
                            modif_array.add(new Modifier(re_j_obj.getJSONObject(i)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                    if(re_j_obj.length()!=0) {
                        txt_producttitle.setText("Modifier" + " (" + re_j_obj.length() + ")");
                        listview.setVisibility(View.VISIBLE);
                        tv_no_record.setVisibility(View.GONE);
                    }
                    else
                    {
                        txt_producttitle.setText("Modifier" + "(0)");
                        listview.setVisibility(View.GONE);
                        tv_no_record.setVisibility(View.VISIBLE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;


        }
    }


  //API request to fetch the data from server
    public void apiWebrequest(String url , final JSONObject j_obj, final int action, final String action_text) {
        if (util.checkInternetConnection()) {
            cd.show("");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        public JSONObject data;

                        @Override
                        public void onResponse(String response) {
                            cd.hide();
                            Log.e("loadTrans", ""+response);
                            try {
                                if((new JSONObject(response)).getString("status").equalsIgnoreCase("1")) {

                                    JSONObject returnJSON =new JSONObject(response);

                                    if(!(new JSONObject(response)).isNull("message"))
                                        util.customToast((new JSONObject(response)).getString("message"));
                                    setReturnedData(action, returnJSON);
                                }
                                else{
                                    cd.hide();
                                    util.customToast("Failed");
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
                            Toast.makeText(ModifierActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("data",j_obj.toString());
                    params.put("action",action_text);
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
            RequestQueue requestQueue = Volley.newRequestQueue(ModifierActivity.this);
            requestQueue.add(stringRequest);
        }
        else
        {
            util.customToast(getResources().getString(R.string.nointernet));

        }
    }


    // Show Image in full screenview
    public void showimage(int p){
        final Dialog Dialog = new Dialog(ModifierActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        LayoutInflater inflater = getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.banner_full_image, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        rowView.setMinimumWidth((int)(displayRectangle.width() * 1f));
        rowView.setMinimumHeight((int)(displayRectangle.height() * 0.5f));
        TouchImageView image_full_pathview = (TouchImageView) rowView.findViewById(R.id.image_detail);
        if(modif_array.get(p).image_full_path!=null&&modif_array.get(p).image_full_path.trim().length()!=0)
        {
            Picasso.get()
                    .load(modif_array.get(p).image_full_path.replace(" ","%20"))
                    .placeholder(R.mipmap.petra)
                    .error(R.mipmap.petra)
                    .into(image_full_pathview);
        }
        else{
            Picasso.get()
                    .load(R.mipmap.petra)
                    .placeholder(R.mipmap.petra)
                    .error(R.mipmap.petra)
                    .into(image_full_pathview);
        }
        image_full_pathview.setMaxZoom(4f);
        Dialog.setContentView(rowView);
        Dialog.setCancelable(true);
        Dialog.setCanceledOnTouchOutside(true);
        Dialog.show();
    }

  //Handle the click event of api
    public void myClickMethod(final View v) {

        switch (v.getId()) {

            case R.id.btn_ok:
                util.ButtonClickEffect(v);
                cd.hide();
                break;

            case R.id.btn_cancel:
                util.ButtonClickEffect(v);
                cd.hide();

                break;
        }
    }


}
