package com.yagna.petra.app.AdvncedCashier.Search;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.yagna.petra.app.Admin.AdminListActivity;
import com.yagna.petra.app.Admin.StoreInformation.StoreInformationFragment;
import com.yagna.petra.app.Admin.Users.UsersFragment;
import com.yagna.petra.app.Admin.ValueCalculations.ValueCalculationsFragment;
import com.yagna.petra.app.Admin.Wallet.WalletFragment;
import com.yagna.petra.app.GlobalActivity;
import com.yagna.petra.app.R;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link AdminListActivity}.
 */
public class ProductSingleViewActivity extends GlobalActivity {
    private  ProductFragment tr_fragment;
    private  UsersFragment usr_fragment;
    private  WalletFragment wallet_fragment;
    private  ValueCalculationsFragment preferenceFragment;
    private  StoreInformationFragment storeInformationFragment;
    private  ProductSingleViewActivity mParentActivit=this;
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
                       String id = getIntent().getStringExtra(com.yagna.petra.app.Admin.Transaction.TransactionFragment.ARG_ITEM_ID);
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
}
