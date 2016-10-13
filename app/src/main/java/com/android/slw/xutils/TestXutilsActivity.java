package com.android.slw.xutils;

import java.io.File;

import com.android.slw.R;
import com.android.slw.SlwApplication;
import com.android.slw.activity.BaseActivity;
import com.lidroid.xutils.BitmapUtils;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.widget.GridView;

public class TestXutilsActivity extends BaseActivity {

	public GridView gridView;
	public Bitmap bitmap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_test_xutils);
	}

	@Override
	protected void initViews() {
		gridView = (GridView) findViewById(R.id.photo_wall);
		String filePath = Utils.getDiskCacheDir(this, "bitmap");
		File directory  = new File(filePath);
		if(!directory.exists())
		{
			directory.mkdirs();
		}
		BitmapUtils bitmapUtils = new BitmapUtils(this, filePath);
		GridViewAdapter adapter = new GridViewAdapter(this, Utils.imageUrls, bitmapUtils);
		gridView.setAdapter(adapter);
	}

	@Override
	protected void bindListener() {

	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}
}