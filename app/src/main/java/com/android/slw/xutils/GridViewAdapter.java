package com.android.slw.xutils;


import com.android.slw.R;
import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GridViewAdapter extends BaseAdapter{

	public LayoutInflater inflater;
	public String[] imageUrls;
	public BitmapUtils bitmapUtils;

	public GridViewAdapter(Context context,String[] imageUrls,BitmapUtils bitmapUtils) {
		inflater = LayoutInflater.from(context);
		this.imageUrls = imageUrls;
		this.bitmapUtils = bitmapUtils;
	}
	@Override
	public int getCount() {
		return imageUrls.length;
	}

	@Override
	public String getItem(int position) {
		return imageUrls[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String currStr = getItem(position);
		View view;
		ViewHolder holder;
		if(convertView == null)
		{
			view = inflater.inflate(R.layout.gridview, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) view.findViewById(R.id.photo);
			view.setTag(holder);
		}else
		{
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		//为防止图片出现乱序，对每个ImageView进行绑定设置
		holder.imageView.setTag(currStr);
		//设置默认的显示图片
		holder.imageView.setImageResource(R.drawable.empty_photo);
		showBitmapToImageView(currStr, holder.imageView);
		return view;
	}

	/**
	 * 设置Bitmap图片到ImageView上面
	 * @param url
	 * @param imageView
	 */
	public void showBitmapToImageView(String url,ImageView imageView)
	{
		bitmapUtils.display(imageView,url);
	}
	static class ViewHolder
	{
		ImageView imageView;
	}
}












