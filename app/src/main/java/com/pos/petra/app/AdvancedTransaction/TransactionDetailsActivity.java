package com.pos.petra.app.AdvancedTransaction;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.pos.petra.app.Admin.AdminListActivity;
import com.pos.petra.app.GlobalActivity;
import com.pos.petra.app.Model.Transaction.AdvTransactions;
import com.pos.petra.app.R;
import com.pos.petra.app.Util.Utils;

public class TransactionDetailsActivity extends GlobalActivity {
    private AdvTransactions.AdvTransaction transaction;
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

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        try {
            transaction =AdvTransactionListActivity.click_transaction;
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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
