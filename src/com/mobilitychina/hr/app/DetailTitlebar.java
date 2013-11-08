package com.mobilitychina.hr.app;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobilitychina.hr.R;

public class DetailTitlebar extends RelativeLayout {
	private TextView titleText;
	private Button backButton;
	private Button rightButton;
//	private LinearLayout rightCustomView;
	private ImageView ivSeperator;
	
	public DetailTitlebar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public DetailTitlebar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DetailTitlebar(Context context) {
		super(context);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		backButton = (Button) findViewById(R.id.back_button);
		titleText = (TextView) findViewById(android.R.id.title);
		rightButton = (Button) findViewById(R.id.right_button);
//		this.rightCustomView = (LinearLayout)findViewById(R.id.rightCustomView);
		this.ivSeperator = (ImageView)findViewById(R.id.ivSeperator);
		
//		this.rightCustomView.setVisibility(View.GONE);
//		rightButton.setVisibility(View.INVISIBLE);
	}

	public void setTitle(CharSequence title) {
		if (titleText != null)
			titleText.setText(title);
	}

	public void setRightButton(String text, OnClickListener listener) {
		rightButton.setText(text);
		rightButton.setOnClickListener(listener);
		rightButton.setVisibility(View.VISIBLE);
	}
	public void setRightButton(int res, OnClickListener listener) {
		rightButton.setOnClickListener(listener);
		rightButton.setVisibility(View.VISIBLE);
		rightButton.setBackgroundResource(res);
	}
	public void setLeftButton(String text, OnClickListener listener) {
		backButton.setText(text);
		backButton.setOnClickListener(listener);
		backButton.setVisibility(View.VISIBLE);
	}
	public void setLeftButton(int res, OnClickListener listener) {
		backButton.setOnClickListener(listener);
		backButton.setBackgroundResource(res);
		backButton.setVisibility(View.VISIBLE);
	}

	public Button getBackButton() {
		return backButton;
	}

	public Button getRightButton() {
		return rightButton;
	}
	
//	public void setRightCustomeView(View view) {
//		if (view == null) {
//			return;
//		}
//		rightCustomView.removeAllViews();
//		this.rightCustomView.addView(view);
//		this.rightCustomView.setVisibility(View.VISIBLE);
//	}
	
	public void setShowSeperator(boolean show) {
		if (show) {
			this.ivSeperator.setVisibility(View.VISIBLE);
		} else {
			this.ivSeperator.setVisibility(View.GONE);
		}
	}
}
