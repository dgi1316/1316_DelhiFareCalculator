package com.hard.targets.delhi.fare.calculator;

import java.util.Locale;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdView;
import com.google.ads.InterstitialAd;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener, AdListener {

	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;
	
	private static InterstitialAd interstitial;
	static AdView av;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		interstitial = new InterstitialAd(this, "a15295752d4668c");
		interstitial.loadAd(new AdRequest());
		interstitial.setAdListener(this);

		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		if (av != null)
			av.destroy();
		interstitial.stopLoading();
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this)
			.setIcon(R.drawable.ic_action_alert)
			.setTitle(R.string.dialog_title)
			.setMessage(R.string.dialog_message)
			.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					MainActivity.this.finish();
				}
			})
			.setNegativeButton(R.string.no, null)
			.setCancelable(false)
			.show();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.rate:
			//String url = "http://www.samsungapps.com/appquery/appDetail.as?appId=com.nits.thedecoder";
			//Intent rate = new Intent(Intent.ACTION_VIEW);
			//rate.setData(Uri.parse(url));
			//startActivity(rate);
			return true;
		case R.id.share:
			Intent localIntent = new Intent("android.intent.action.SEND");
		    localIntent.setType("text/plain");
		    localIntent.putExtra("android.intent.extra.SUBJECT", "Delhi Fare Calculator");
		    localIntent.putExtra("android.intent.extra.TEXT", "Travel anywhere in Delhi with 'Delhi Fare Calculator' - Instantly calculate fare of auto rickshaw and taxi (AC & Non-AC).");
		    startActivity(Intent.createChooser(localIntent, "Share Via"));
			return true;
		case R.id.disclaimer :
			Intent disclaimer = new Intent("com.hard.targets.delhi.fare.calculator.DISCLAIMERACTIVITY");
			startActivity(disclaimer);
			return true;
		case R.id.aboutUs :
			Intent aboutUs = new Intent("com.hard.targets.delhi.fare.calculator.ABOUTACTIVITY");
			startActivity(aboutUs);
			return true;
		case R.id.exit :
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			}
			return null;
		}
	}

	public static class DummySectionFragment extends Fragment {
		
		public static final String ARG_SECTION_NUMBER = "section_number";
		
		View rootView = null;
		EditText etDistance, etWaitTime, etLuggage;
		RadioGroup rgTime, rgAC;
		Button bCalculate;
		float distance;
		int waitTime, luggage, position;
		boolean night = false, ac = false;

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			
			position = getArguments().getInt(ARG_SECTION_NUMBER);
			switch (position) {
			case 0:
				rootView = inflater.inflate(R.layout.fragment_main_dummy0, container, false);
				etDistance = (EditText) rootView.findViewById(R.id.etDistance);
				etWaitTime = (EditText) rootView.findViewById(R.id.etWaitTime);
				etLuggage = (EditText) rootView.findViewById(R.id.etLuggage);
				rgTime = (RadioGroup) rootView.findViewById(R.id.rgTravelTime);
				bCalculate = (Button) rootView.findViewById(R.id.btnCalculate);
				av = (AdView) rootView.findViewById(R.id.av);
				rgTime.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.rbDay:
							night = false;
							break;
						case R.id.rbNight:
							night = true;
							break;
						default:
							break;
						}
					}
				});
				bCalculate.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if (etDistance.getText().toString().equals("") || etDistance.getText().toString().equals("0"))
							Toast.makeText(getActivity(), "Enter Distance Travelled", Toast.LENGTH_SHORT).show();
						else {
							distance = Float.parseFloat(etDistance.getText().toString());
							if (etWaitTime.getText().toString().equals(""))
								waitTime = 0;
							else
								waitTime = Integer.parseInt(etWaitTime.getText().toString());
							if (etLuggage.getText().toString().equals(""))
								luggage = 0;
							else
								luggage = Integer.parseInt(etLuggage.getText().toString());
							Intent i = new Intent("com.hard.targets.delhi.fare.calculator.VIEWACTIVITY");
							i.putExtra("distance", distance);
							i.putExtra("travelTime", night);
							i.putExtra("waitTime", waitTime);
							i.putExtra("luggage", luggage);
							i.putExtra("type", position);
							startActivity(i);
						}
					}
				});
				av.loadAd(new AdRequest());
				interstitial.loadAd(new AdRequest());
				break;
			case 1:
				rootView = inflater.inflate(R.layout.fragment_main_dummy1, container, false);
				etDistance = (EditText) rootView.findViewById(R.id.etDistance);
				etWaitTime = (EditText) rootView.findViewById(R.id.etWaitTime);
				etLuggage = (EditText) rootView.findViewById(R.id.etLuggage);
				rgTime = (RadioGroup) rootView.findViewById(R.id.rgTravelTime);
				rgAC = (RadioGroup) rootView.findViewById(R.id.rgAC);
				bCalculate = (Button) rootView.findViewById(R.id.btnCalculate);
				av = (AdView) rootView.findViewById(R.id.av);
				rgTime.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.rbDay:
							night = false;
							break;
						case R.id.rbNight:
							night = true;
							break;
						default:
							break;
						}
					}
				});
				rgAC.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.rbNonAC:
							ac = false;
							break;
						case R.id.rbAC:
							ac = true;
							break;
						default:
							break;
						}
					}
				});
				bCalculate.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if (etDistance.getText().toString().equals("") || etDistance.getText().toString().equals("0"))
							Toast.makeText(getActivity(), "Enter Distance Travelled", Toast.LENGTH_SHORT).show();
						else {
							distance = Float.parseFloat(etDistance.getText().toString());
							if (etWaitTime.getText().toString().equals(""))
								waitTime = 0;
							else
								waitTime = Integer.parseInt(etWaitTime.getText().toString());
							if (etLuggage.getText().toString().equals(""))
								luggage = 0;
							else
								luggage = Integer.parseInt(etLuggage.getText().toString());
							Intent i = new Intent("com.hard.targets.delhi.fare.calculator.VIEWACTIVITY");
							i.putExtra("distance", distance);
							i.putExtra("travelTime", night);
							i.putExtra("ac", ac);
							i.putExtra("waitTime", waitTime);
							i.putExtra("luggage", luggage);
							i.putExtra("type", position);
							startActivity(i);
						}
					}
				});
				av.loadAd(new AdRequest());
				interstitial.loadAd(new AdRequest());
				break;
			default:
				break;
			}
			
			return rootView;
		}
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

}
