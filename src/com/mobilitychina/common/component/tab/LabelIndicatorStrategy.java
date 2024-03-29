package com.mobilitychina.common.component.tab;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.mobilitychina.hr.R;


public class LabelIndicatorStrategy {

	private final CharSequence mLabel;
	private int mIndicatorView;
	private Context mContext;

	public LabelIndicatorStrategy(Context context, CharSequence label) {
		mContext = context;
		mLabel = label;
	}

	public LabelIndicatorStrategy(Context context, CharSequence label,
			int indicatorView) {
		this(context, label);
		mIndicatorView = indicatorView;
	}

	public View createIndicatorView(TabHost mTabHost) {
		if (mIndicatorView == 0) {
			mIndicatorView = R.layout.tab_indicator_holo;
		}
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View tabIndicator = inflater.inflate(mIndicatorView,
				mTabHost.getTabWidget(), // tab widget is the parent
				false); // no inflate params

		final TextView tv = (TextView) tabIndicator
				.findViewById(android.R.id.title);
		tv.setText(mLabel);
		return tabIndicator;
	}
}