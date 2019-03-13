package com.yagna.petra.app.Admin.Transaction;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.yagna.petra.app.Admin.AdminListActivity;
import com.yagna.petra.app.Admin.Charity.CharityFragment;
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
public class TransactionSingleViewActivity extends GlobalActivity {
    private  TransactionFragment tr_fragment;
    private  UsersFragment usr_fragment;
    private  WalletFragment wallet_fragment;
    private  ValueCalculationsFragment preferenceFragment;
    private  StoreInformationFragment storeInformationFragment;
    private CharityFragment charityFragment;
    private  TransactionSingleViewActivity mParentActivit=this;
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

        tr_fragment = new TransactionFragment();
        usr_fragment = new UsersFragment();
        wallet_fragment = new WalletFragment();
        preferenceFragment = new ValueCalculationsFragment();
        storeInformationFragment = new StoreInformationFragment();
        charityFragment = new CharityFragment();

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
            arguments.putString(TransactionFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(TransactionFragment.ARG_ITEM_ID));
    //        TransactionFragment fragment = new TransactionFragment();
  //          fragment.setArguments(arguments);
            /*getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
*/
            int position= Integer.parseInt(getIntent().getStringExtra(TransactionFragment.ARG_ITEM_ID));
            switch (position){
                case 0:

                    tr_fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, tr_fragment)
                            .commit();
                    break;
                case 1:
                    usr_fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, usr_fragment)
                            .commit();
                    break;
                case 2:

                    wallet_fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, wallet_fragment)
                            .commit();
                    break;

                case 3:

                    preferenceFragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, preferenceFragment)
                            .commit();
                    break;
                case 4:

                    storeInformationFragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, storeInformationFragment)
                            .commit();
                    break;
                case 6:

                    charityFragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, charityFragment)
                            .commit();
                    break;

            }


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
            navigateUpTo(new Intent(this, AdminListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
