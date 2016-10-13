package com.gionee.apidemos;

import java.util.List;

import com.gionee.widget.turnpager.TurnPagerView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TurnpagerActivity extends Activity {

	// private PageWidget mPageWidget;
	TurnPagerView mPager;
	TextView mText;
	private List<ResolveInfo> mApps;
	private Handler mUiHandler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.turnpager_main);

		mText = (TextView) findViewById(R.id.hell);
		mText.setText("1");

		mText = (TextView) findViewById(R.id.hell0);
		mText.setText("2");

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

		Button prebutton = (Button) findViewById(R.id.pre_button);
		
		mPager = (TurnPagerView) findViewById(R.id.turnpager);
		
		prebutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int tar = mPager.getCurrentItem() - 1;
				mPager.setCurrentItem(tar);
			}
		});

		Button nextbutton = (Button) findViewById(R.id.next_button);

		nextbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int tar = mPager.getCurrentItem() + 1;
				mPager.setCurrentItem(tar);
			}
		});		
	
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
				i = new ImageView(TurnpagerActivity.this);
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