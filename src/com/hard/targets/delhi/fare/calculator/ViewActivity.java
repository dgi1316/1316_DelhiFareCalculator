package com.hard.targets.delhi.fare.calculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdView;
import com.google.ads.InterstitialAd;

public class ViewActivity extends Activity implements AdListener {

	int type, waitTime, luggage;
	float distance, FARE = 0F;
	boolean night, ac;
	String SUB_TITLE;
	AdView av;
	
	private InterstitialAd interstitial;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view);
		
		interstitial = new InterstitialAd(this, "a15295752d4668c");
		interstitial.loadAd(new AdRequest());
		interstitial.setAdListener(this);
		
		Intent t = getIntent();
		type = t.getIntExtra("type", 0);
		distance = t.getFloatExtra("distance", 0F);
		night = t.getBooleanExtra("travelTime", false);
		waitTime = t.getIntExtra("waitTime", 0);
		luggage = t.getIntExtra("luggage", 0);
		switch (type) {
		case 0:
			SUB_TITLE = getResources().getString(R.string.vehicle0);
			setupActionBar();
			TextView tv1, tv2, tv3, tv4, tvFare;
			tv1 = (TextView) findViewById(R.id.tvDistance);
			tv2 = (TextView) findViewById(R.id.tvTravelTime);
			tv3 = (TextView) findViewById(R.id.tvWaitTime);
			tv4 = (TextView) findViewById(R.id.tvLuggage);
			tvFare = (TextView) findViewById(R.id.tvFare);
			tv1.setText(distance + " " + getResources().getString(R.string.km));
			if (night)
				tv2.setText(getResources().getString(R.string.rb_yes));
			else
				tv2.setText(getResources().getString(R.string.rb_no));
			tv3.setText(waitTime + " " + getResources().getString(R.string.min));
			tv4.setText(luggage + "");
			if (distance <= 2.0)
				FARE = 25;
			else
				FARE = 25 + ((distance - 2) * 8);
			if (luggage > 0)
				FARE = FARE + (luggage * 7.5F);
			if (waitTime >= 15) {
				int hr = waitTime / 60;
				int min = waitTime % 60;
				if (min > 0)
					FARE = FARE + ((hr + 1) * 30);
				else
					FARE = FARE + (hr * 30);
			}
			if (night)
				FARE = FARE + (FARE * 0.25F);
			tvFare.setText(getResources().getString(R.string.fare) + " " + FARE);
			av = (AdView) findViewById(R.id.avv);
			av.loadAd(new AdRequest());
			break;
		case 1:
			SUB_TITLE = getResources().getString(R.string.vehicle1);
			setupActionBar();
			ac = t.getBooleanExtra("ac", false);
			setContentView(R.layout.activity_view1);
			TextView tvv1, tvv2, tvv3, tvv4, tvv5, tvvFare;
			tvv1 = (TextView) findViewById(R.id.tvvDistance);
			tvv2 = (TextView) findViewById(R.id.tvvTravelTime);
			tvv3 = (TextView) findViewById(R.id.tvvType);
			tvv4 = (TextView) findViewById(R.id.tvvWaitTime);
			tvv5 = (TextView) findViewById(R.id.tvvLuggage);
			tvvFare = (TextView) findViewById(R.id.tvvFare);
			tvv1.setText(distance + " " + getResources().getString(R.string.km));
			if (night)
				tvv2.setText(getResources().getString(R.string.rb_yes));
			else
				tvv2.setText(getResources().getString(R.string.rb_no));
			if (ac)
				tvv3.setText(getResources().getString(R.string.rb_AC));
			else
				tvv3.setText(getResources().getString(R.string.rb_NonAC));
			tvv4.setText(waitTime + " " + getResources().getString(R.string.min));
			tvv5.setText(luggage + "");
			if (distance <= 1.0)
				FARE = 25;
			else
				if (ac)
					FARE = 25 + ((distance - 1) * 16);
				else
					FARE = 25 + ((distance - 1) * 14);
			if (luggage > 0)
				FARE = FARE + (luggage * 10);
			if (waitTime >= 15) {
				int hr = waitTime / 60;
				int min = waitTime % 60;
				if (min > 0)
					FARE = FARE + ((hr + 1) * 30);
				else
					FARE = FARE + (hr * 30);
			}
			if (night)
				FARE = FARE + (FARE * 0.25F);
			tvvFare.setText(getResources().getString(R.string.fare) + " " + FARE);
			av = (AdView) findViewById(R.id.avv1);
			av.loadAd(new AdRequest());
			break;
		default:
			break;
		}
	}

	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setSubtitle(SUB_TITLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
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
