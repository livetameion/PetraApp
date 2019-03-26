package com.pos.petra.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.splunk.mint.Mint;
import com.pos.petra.app.Util.LocaleManager;


public class GlobalActivity extends AppCompatActivity {
	public GlobalActivity activity;


    @Override
	protected void onCreate(Bundle savedInstanceState) {

	//code for App crash notification to Developer
		super.onCreate(savedInstanceState);

		Mint.initAndStartSession(this.getApplication(), "6197e52e");
		LocaleManager.setLocale(this);
		activity=this;
	}


}
