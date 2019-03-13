package com.yagna.petra.app.Product.Product;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.yagna.petra.app.R;
import com.yagna.petra.app.Util.Constants;
import com.yagna.petra.app.Util.CustomDialog;
import com.yagna.petra.app.Util.PrefUtil;
import com.yagna.petra.app.Util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class P_Product_Seemore extends AppCompatActivity {

    private GridView gridView;
    private JSONArray imagearray = new JSONArray();
    private ProductGridViewAdapter gridviewAdapter;
    private JSONArray imagearray1;
    private FullImageProductAdapter mAdapter;
    private Utils util;
    private CustomDialog cd;
    private P_Product_Seemore context;
    private SharedPreferences common_mypref;
    private TextView seeMore;
    int id = 0;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p__product__seemore);
        context=P_Product_Seemore.this;
        util=new Utils(context);
        cd=new CustomDialog(context);
        common_mypref = getApplicationContext().getSharedPreferences(
                "user", 0);
        gridView = (GridView) findViewById(R.id.grid_product);
        seeMore = (TextView)findViewById(R.id.product_seemore_grid);
        setSeeMore();
        seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=i+1;
                setUrlPayload(Constants.GETLIST,i);
            }
        });

    setgridview(id);
    }
    public void setSeeMore(){
        if(imagearray.length()>=9) {
            seeMore.setVisibility(View.VISIBLE);
        }
        else {
            seeMore.setVisibility(View.GONE);
        }

    }
    private void setgridview(int id){
        setUrlPayload(Constants.GETLIST,i);
    }
    public void jsonwebrequest(final String url, final JSONObject j_obj) {
        if (util.checkInternetConnection()) {
            cd.show("");
            Log.e("Api",""+j_obj+"\n"+ url);
            /*int position = 0;
            try {
                if(url.equalsIgnoreCase(Constants.Get_BranchList))
                    position=Integer.parseInt(j_obj.getString("categoryId"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

            RequestQueue requestQueue = Volley.newRequestQueue(P_Product_Seemore.this);
            //  final int finalPosition = position;
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, j_obj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                cd.hide();
                                JSONObject returnJSON = response;
                              /*  JSONArray j_imagearray=returnJSON.getJSONArray("ResponseData");*/
                                Log.e("API RESPONSEeeee",""+response);
                                if(url.equalsIgnoreCase(Constants.b_Get_ProductList))
                                {

                                    setReturnedData(Constants.GETLIST,returnJSON);
                                   // setSeeMore();
                                }

                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
            jsObjRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 5000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 5000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            requestQueue.add(jsObjRequest);
        }
        else
        {
            util.customToast(getResources().getString(R.string.nointernet));
        }
    }
    public void setReturnedData(int category, JSONObject j_obj) {
        switch (category) {

            case Constants.GETLIST:
                Log.e("Apitransect", "" + j_obj);
                JSONObject returnedData = j_obj;
                try {
                    JSONArray tempjasonarray=new JSONArray();
                    tempjasonarray = returnedData.getJSONArray("ResponseData");
                    if(tempjasonarray.length()<9)
                    {
                        seeMore.setVisibility(View.GONE);
                    }else
                    {
                        seeMore.setVisibility(View.VISIBLE);
                    }
                    if(imagearray.length()>0)
                    {
                        imagearray=concatArray(imagearray,tempjasonarray);
                        if(gridviewAdapter!=null)
                        {
                            gridviewAdapter.setupdateadapter(imagearray);
                            gridviewAdapter.notifyDataSetChanged();
                        }
                    }else
                    {
                       imagearray= tempjasonarray;
                        gridviewAdapter = new ProductGridViewAdapter(this,imagearray);
                        gridView.setAdapter(gridviewAdapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    private JSONArray concatArray(JSONArray arr1, JSONArray arr2)
            throws JSONException {
        JSONArray result = new JSONArray();
        for (int i = 0; i < arr1.length(); i++) {
            result.put(arr1.get(i));
        }
        for (int i = 0; i < arr2.length(); i++) {
            result.put(arr2.get(i));
        }
        return result;
    }
    public void setUrlPayload(int category,int extra) {
        switch (category) {
            case Constants.GETLIST:
                JSONObject j_obj = new JSONObject();
                try {
                    j_obj.put("UserId", PrefUtil.getuser_id(common_mypref));
                    j_obj.put("Page", extra);
                    j_obj.put("PageSize",9);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonwebrequest(Constants.b_Get_ProductList, j_obj);
                break;
        }
    }



    public void showimage(){
        final Dialog Dialog = new Dialog(P_Product_Seemore.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        LayoutInflater inflater = getLayoutInflater();
        View rowView = (View) inflater.inflate(R.layout.p_viewpager_full, null);
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        rowView.setMinimumWidth((int)(displayRectangle.width() * 1f));
        rowView.setMinimumHeight((int)(displayRectangle.height() * 0.5f));
        ViewPager viewPager2 = (ViewPager)rowView.findViewById(R.id.vp_catlog);
        Button backfullimg = (Button) rowView.findViewById(R.id.btn_close);
        backfullimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 /*   _activity.finish();*/
                Dialog.dismiss();
            }
        });
        imagearray1 = imagearray;
        mAdapter = new FullImageProductAdapter(P_Product_Seemore.this,imagearray1);
        viewPager2.setAdapter(mAdapter);
        Dialog.setContentView(rowView);
        Dialog.setCancelable(true);
        Dialog.setCanceledOnTouchOutside(true);
        Dialog.show();
    }







}
