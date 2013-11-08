package com.mobilitychina.hr.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobilitychina.hr.R;

/**
 * 简单提示Item
 * @author chenwang
 *
 */
public class SimpleMessageItem extends RelativeLayout {
	private TextView tvMessage;

	public SimpleMessageItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SimpleMessageItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SimpleMessageItem(Context context) {
		super(context);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		this.tvMessage = (TextView) this.findViewById(R.id.tvMessage);
	}

	public void setSimpleMessage(String text) {
		this.tvMessage.setText(text);
	}
}
