package com.yagna.petra.app.Cashier;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.yagna.petra.app.Admin.AdminListActivity;
import com.yagna.petra.app.GlobalActivity;
import com.yagna.petra.app.Model.Transaction;
import com.yagna.petra.app.R;
import com.yagna.petra.app.Util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link AdminListActivity}.
 */
public class TransactionDetailsActivity extends GlobalActivity {

    private Transaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
         }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        try {
            transaction =TransactionListActivity.click_transaction;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ((TextView)findViewById(R.id.txt_t_id)).setText(transaction.transaction_id);
            ((TextView)findViewById(R.id.txt_merchant)).setText(transaction.merchant_id);
            ((TextView)findViewById(R.id.txt_cashier)).setText(transaction.cashier_name);
            ((TextView)findViewById(R.id.txt_org_price)).setText("$"+transaction.original_price);
            ((TextView)findViewById(R.id.txt_subtotal)).setText("$"+transaction.subtotal);
            ((TextView)findViewById(R.id.txt_token_used)).setText(transaction.tokens_used);
            ((TextView)findViewById(R.id.txt_total_price)).setText("$"+(transaction.token_price.equalsIgnoreCase("null")?"0.00":transaction.token_price));
            ((TextView)findViewById(R.id.txt_pay_method)).setText(transaction.pay_method);
            ((TextView)findViewById(R.id.txt_time)).setText(Utils.getConvetredDate(transaction.transaction_complete));

        } catch (Exception e) {
            e.printStackTrace();
        }
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
            //navigateUpTo(new Intent(this, AdminListActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
