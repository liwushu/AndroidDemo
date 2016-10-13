package com.gionee.apidemos;

import java.util.List;

import com.gionee.thirdpartyeffect.EffecterFactory;
import com.gionee.thirdpartyeffect.view.EasyViewGroup;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;

public class ThirdEffectDemoActivity extends Activity {

	TextView mText;
	private List<ResolveInfo> mApps;
	private Handler mUiHandler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.thirdeffect_main);
		
		initial();		
	}
	
	private void initial() {
		
		final EasyViewGroup mView = (EasyViewGroup)findViewById(R.id.easyView);
		mView.setEffecterType(EffecterFactory.EffectStyle_Box_Outside);//设置滑动动画样式
		mView.setGestureOrientation(EasyViewGroup.HORIENZON);//设置手势识别样式
		mView.setPageLoop(true);//设置页面循环
		//设置页面监听
		mView.setPageListener(new EasyViewGroup.PageListener() {
			
			@Override
			public void onPageSrcollStateChange(int arg0) { //页面滚动状态监听
				
			}
			
			@Override
			public void onPageSelseted(int arg0) { //页面选择监听
				
			}

			@Override
			public void onPageSlideFraction(float arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		mView.setISlidePageExControl(new EasyViewGroup.ISlidePageExControl() { //页面滚动外部控制			
			@Override
			public boolean isBlockSlidePage() {
				return false;
			}
		}); 
		
		 Switch vSwitch = (Switch)findViewById(R.id.switch_loopmode);
		 vSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {		
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					mView.setPageLoop(true);
				}else{
					mView.setPageLoop(false);
				}
			}
		});
		
		Spinner spinner = (Spinner)findViewById(R.id.spinner_type); 
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.effect_type, R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				mView.setEffecterType(position+1);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				mView.setEffecterType(0);
			}
			
		});
		
		Button pre_button = (Button)findViewById(R.id.pre_button); 
		pre_button.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				mView.slideToPreviousPage();
			}
		});
		
		Button next_button = (Button)findViewById(R.id.next_button); 
		next_button.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				mView.slideToNextPage();
			}
		});
		
		mText = (TextView) findViewById(R.id.hell);
		mText.setText("1");

		mText = (TextView) findViewById(R.id.hell0);
		mText.setText("2");
		mText.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Log.d("xcl00","mText---onclick");
			}
		});

		mText = (TextView) findViewById(R.id.hell1);
		mText.setText("3");

		new Thread() {
			@Override
			public void run() {
				loadApps();
				mUiHandler.post(new Runnable() {
					public void run() {
						GridView grid = (GridView) findViewById(R.id.myGrid);
						grid.setAdapter(new AppsAdapter());
					}
				});
			}

		}.start();

		ListView listview = (ListView) findViewById(R.id.list);
		listview.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_single_choice, GENRES));
	}
	
	private void loadApps() {
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		mApps = getPackageManager().queryIntentActivities(mainIntent, 0);
	}

	public class AppsAdapter extends BaseAdapter {
		public AppsAdapter() {
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView i;

			if (convertView == null) {
				i = new ImageView(ThirdEffectDemoActivity.this);
				i.setScaleType(ImageView.ScaleType.FIT_CENTER);
				i.setLayoutParams(new GridView.LayoutParams(50, 50));
			} else {
				i = (ImageView) convertView;
			}

			ResolveInfo info = mApps.get(position);
			i.setImageDrawable(info.activityInfo.loadIcon(getPackageManager()));

			return i;
		}

		public final int getCount() {
			return mApps.size();
		}

		public final Object getItem(int position) {
			return mApps.get(position);
		}

		public final long getItemId(int position) {
			return position;
		}
	}

	private static final String[] GENRES = new String[] { "Action",
			"Adventure", "Animation", "Children", "Comedy", "Documentary",
			"Drama", "Foreign", "History", "Independent", "Romance", "Sci-Fi",
			"Television", "Thriller" };
}
