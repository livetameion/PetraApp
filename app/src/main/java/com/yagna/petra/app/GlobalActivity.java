package com.yagna.petra.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.splunk.mint.Mint;
import com.yagna.petra.app.Util.LocaleManager;


public class GlobalActivity extends AppCompatActivity {
	public String dynamicrssi;
	public GlobalActivity activity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		//code for Appcrash Notification to Developer
		super.onCreate(savedInstanceState);
	//	Mint.initAndStartSession(this.getApplication(), "4a7d8481");
		Mint.initAndStartSession(this.getApplication(), "6197e52e");//yagna.calicainfosoft@gmail.com
		LocaleManager.setLocale(this);

		//Mint.initAndStartSession(this.getApplication(), "1fadc36d");
		// last update-----Mint.initAndStartSession(this.getApplication(), "c76d131d");
		activity=this;
	}


/*	Timer timer;
	MyTimerTask myTimerTask;
	@Override
	protected void onResume() {
		super.onResume();

		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}
	@Override
	protected void onPause()
	{
		if (timer == null) {
			myTimerTask = new MyTimerTask();
			timer = new Timer();
			timer.schedule(myTimerTask, 10, 10);
		}
		super.onPause();
	}
	private void bringApplicationToFront()
	{
		KeyguardManager myKeyManager = (KeyguardManager)getSystemService(this.KEYGUARD_SERVICE);
		if( myKeyManager.inKeyguardRestrictedInputMode())
			return;

		Log.d("TAG", "====Bringging Application to Front====");

		Intent notificationIntent = new Intent(this, LogInActivity.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		try
		{
			pendingIntent.send();
		}
		catch (PendingIntent.CanceledException e)
		{
			e.printStackTrace();
		}
	}
	public void onBackPressed() {
		// do not call super onBackPressed.
	}
	class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			bringApplicationToFront();
		}
	}*/

}
