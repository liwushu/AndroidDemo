package com.gionee.apidemos;

import com.gionee.widget.turnpager.CycleTurnpagerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class CycleTurnPagerActivity  extends Activity{
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		CycleTurnpagerView turnview = new CycleTurnpagerView(this);
		
		turnview.setViewFactory(new CycleTurnpagerView.ViewFactory() {			
			
			@Override
			public void updateViewInfo(View view, int pagenum) {
				
				if (view instanceof TextView) {
					TextView tx = (TextView) view;
					tx.setText(pagenum+"");
				}
			}
			
			@Override
			public View makeView() {
				TextView tx = new TextView(CycleTurnPagerActivity.this);
				tx.setBackgroundColor(0x9922aa22);
				tx.setGravity(Gravity.CENTER);
				tx.setTextSize(TypedValue.COMPLEX_UNIT_SP,200);
				tx.setText(0+"");
				return tx;
			}
			
			@Override
			public int getCount() {
				return Integer.MAX_VALUE;
			}
		});
		
		setContentView(turnview);
	}
}
