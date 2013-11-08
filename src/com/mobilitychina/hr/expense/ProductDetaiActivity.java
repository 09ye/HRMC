package com.mobilitychina.hr.expense;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.mobilitychina.hr.R;
import com.mobilitychina.hr.app.BaseActivity;
import com.mobilitychina.hr.app.DetailTitlebar;
import com.mobilitychina.hr.expense.data.ProductInfo;

public class ProductDetaiActivity extends BaseActivity {
	private ProductInfo product = new ProductInfo();
	private DetailTitlebar detailTitlebar;
	private TextView reName;
	private TextView rePrice;
	private TextView reNum;
	private TextView reDeclar;
	private TextView reDate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_product_detail);
		product = (ProductInfo) getIntent().getSerializableExtra("product");
		detailTitlebar  = (DetailTitlebar) findViewById(R.id.title);
		detailTitlebar.setTitle("产品详情");
		detailTitlebar.setLeftButton(R.drawable.icon_back, new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		reName = (TextView) findViewById(R.id.read_name);
		reName.setText(product.getName());
		rePrice= (TextView) findViewById(R.id.read_department);
		rePrice.setText(product.getAmount());
		reNum = (TextView) findViewById(R.id.read_declared);
		reNum.setText(product.getQuantity());
		reDeclar = (TextView) findViewById(R.id.read_date);
		reDeclar.setText(product.getRemark());
		reDate = (TextView) findViewById(R.id.read_amount);
		reDate.setText(product.getDate());
	}
}
