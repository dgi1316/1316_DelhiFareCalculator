package com.hard.targets.delhi.fare.calculator;

import java.util.Locale;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdView;
import com.google.ads.InterstitialAd;

public class DisclaimerActivity extends Activity implements AdListener {

	WebView wv;
	AdView av;
	String url;
	private InterstitialAd interstitial;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disclaimer);
		setupActionBar();
		interstitial = new InterstitialAd(this, "a15295752d4668c");
		interstitial.loadAd(new AdRequest());
		interstitial.setAdListener(this);
		Locale l = Locale.getDefault();
		if(l.toString().equals("hi_IN"))
			url = "file:///android_asset/disclaimer_hi.html";
		else
			url = "file:///android_asset/disclaimer.html";
		wv = (WebView) findViewById(R.id.wvDisclaimer);
		av = (AdView) findViewById(R.id.avDisclaimer);
		wv.loadUrl(url);
		av.loadAd(new AdRequest());
	}

	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setSubtitle(getResources().getString(R.string.action_disclaimer));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.disclaimer, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDismissScreen(Ad arg0) {
	}

	@Override
	public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
	}

	@Override
	public void onLeaveApplication(Ad arg0) {
	}

	@Override
	public void onPresentScreen(Ad arg0) {
	}

	@Override
	public void onReceiveAd(Ad arg0) {
		if (arg0 == interstitial)
			interstitial.show();
	}

	@Override
	protected void onDestroy() {
		if (av != null)
			av.destroy();
		interstitial.stopLoading();
		super.onDestroy();
	}

}
