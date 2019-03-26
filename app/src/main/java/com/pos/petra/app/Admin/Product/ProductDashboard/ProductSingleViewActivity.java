package com.pos.petra.app.Admin.Product.ProductDashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.pos.petra.app.Admin.AdminListActivity;
import com.pos.petra.app.Admin.StoreInformation.StoreInformationFragment;
import com.pos.petra.app.Admin.CashierUsers.CashiersFragment;
import com.pos.petra.app.Admin.ValueCalculations.ValueCalculationsFragment;
import com.pos.petra.app.Admin.Wallet.WalletFragment;
import com.pos.petra.app.GlobalActivity;
import com.pos.petra.app.R;

import java.io.File;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link AdminListActivity}.
 */
public class ProductSingleViewActivity extends GlobalActivity {
    private ProductFragment tr_fragment;
    private CashiersFragment usr_fragment;
    private  WalletFragment wallet_fragment;
    private  ValueCalculationsFragment preferenceFragment;
    private  StoreInformationFragment storeInformationFragment;
    private ProductSingleViewActivity mParentActivit=this;

    Uri myuri;
    private Uri imageToUploadUri;
    private static int REQUEST_PICTURE = 112;
    private static int REQUEST_CROP_PICTURE = 122;
    int SELECT_FILE=2,REQUEST_CAMERA=4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_frag_single_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        tr_fragment = new ProductFragment();


       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        // Show the Up button in the action bar.


        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ProductFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(ProductFragment.ARG_ITEM_ID));



            tr_fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, tr_fragment)
                    .commit();


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(getIntent().getStringExtra(ProductFragment.ARG_TYPE).equalsIgnoreCase("true")) {
                        tr_fragment.fillData("",true);
                    }
                    else if(getIntent().getStringExtra(ProductFragment.ARG_ITEM_ID)!=null) {
                        String id = getIntent().getStringExtra(com.pos.petra.app.Admin.Transaction.TransactionFragment.ARG_ITEM_ID);
                        tr_fragment.fillData(id,false);
                    }
                    else if(getIntent().getStringExtra(ProductFragment.ARG_SEARCH)!=null) {
                        String search_query = getIntent().getStringExtra(ProductFragment.ARG_SEARCH);
                        String locationid = getIntent().getStringExtra(ProductFragment.LOC_ID);
                        tr_fragment.callFreeSearch(search_query,locationid);
                    }

                }
            },200);





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
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void galleryIntent() {
        //startActivityForResult(MediaStoreUtils.getPickImageIntent(b_OffersActivity.this), REQUEST_PICTURE);
        myuri=null;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), REQUEST_PICTURE);
    }

    public void captureCameraImage() {
        myuri=null;
        Intent chooserIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(Environment.getExternalStorageDirectory(), "POST_IMAGE.jpg");
        chooserIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        imageToUploadUri = Uri.fromFile(f);
        startActivityForResult(chooserIntent, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



    }
}
