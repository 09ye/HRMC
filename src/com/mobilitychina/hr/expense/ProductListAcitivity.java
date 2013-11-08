package com.mobilitychina.hr.expense;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mobilitychina.hr.R;
import com.mobilitychina.hr.app.BaseDetailListActivity;
import com.mobilitychina.hr.app.DetailTitlebar;
import com.mobilitychina.hr.app.MCApplication;
import com.mobilitychina.hr.expense.ExpenseDetailActivity.MyAdpater;
import com.mobilitychina.hr.expense.data.ExpenseInfo;
import com.mobilitychina.hr.expense.data.ProductInfo;
import com.mobilitychina.hr.service.HttpPostService;
import com.mobilitychina.hr.util.CommonUtil;
import com.mobilitychina.intf.ITaskListener;
import com.mobilitychina.intf.Task;
import com.mobilitychina.net.HttpPostTask;
import com.mobilitychina.net.NetObject;

/**
 * 产品列表》》》》已废弃
 * @author baohua
 *
 */
public class ProductListAcitivity extends BaseDetailListActivity implements ITaskListener {
	private MyAdpater mAdpater;
	DetailTitlebar detailTitlebar;
	HttpPostTask proInfoTask;
	private List<ProductInfo> productInfoList = new ArrayList<ProductInfo>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		detailTitlebar = (DetailTitlebar) findViewById(R.id.detailTitlebar);
		detailTitlebar.setTitle("产品列表");
		mAdpater = new MyAdpater();
		this.showProgressDialog("正在获取信息..");
		proInfoTask = new HttpPostTask(MCApplication.getInstance().getApplicationContext());
		proInfoTask.setUrl(HttpPostService.SOAP_URL+"getHrExpenseLine");
//		proInfoTask.getTaskArgs().put("id",id.get(i));
		proInfoTask.setListener(this);
		proInfoTask.start();
		 
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		if(productInfoList.size()>position){
			Intent intent = new Intent(this,ExpenseDetailActivity.class);
			intent.putExtra("id", productInfoList.get(position).getId());
			intent.putExtra("expenseInfo", productInfoList.get(position));
			startActivity(intent);
		}
	}

	@Override
	public void onTaskFinished(Task task) {
		// TODO Auto-generated method stub
		dismissDialog();
		NetObject result = (NetObject) task.getResult();
		if(result==null)
			//				showDialog("提示", "暂无信息", null);
			return;
		if(productInfoList==null)
			productInfoList = new ArrayList<ProductInfo>();
		ProductInfo product = new ProductInfo();
		product.setId(result.stringForKey("id"));
		product.setAmount(result.stringForKey("unit_amount"));
		product.setRemark(result.stringForKey("name"));
		product.setRef(result.stringForKey("ref"));
		product.setDate(result.stringForKey("date_value"));
		product.setQuantity(result.stringForKey("unit_quantity"));
		product.setName((String) result.anyListForKey("product_id").get(1));
		productInfoList.add(product);
		if(mAdpater==null)
			mAdpater = new MyAdpater();
		System.out.println(productInfoList.size()+result.toString());
		mAdpater.setList(productInfoList);
		this.setListAdapter(mAdpater);
		mAdpater.notifyDataSetChanged();
	}

	@Override
	public void onTaskFailed(Task task) {
		// TODO Auto-generated method stub
		dismissDialog();
		showDialog("提示","网络异常,请重试", null);
	}

	@Override
	public void onTaskUpdateProgress(Task task, int count, int total) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTaskTry(Task task) {
		// TODO Auto-generated method stub
		
	}
	public class MyAdpater extends BaseAdapter{	
		
		List<ProductInfo> produtcInfo =  new ArrayList<ProductInfo>();
		public void setList(List<ProductInfo> expenseList) {
			this.produtcInfo = expenseList;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return produtcInfo.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return produtcInfo.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if(convertView==null){
				convertView = LayoutInflater.from(ProductListAcitivity.this).inflate(
						R.layout.item_expense, null);
			}
			if (produtcInfo == null||produtcInfo.size()==0) {
				return CommonUtil.buildListSimpleMsgItemView(ProductListAcitivity.this, convertView, "暂无数据");
			} 
			ChildViewHolder holder = new ChildViewHolder();
			holder.mName = (TextView) convertView.findViewById(R.id.name);
			holder.mTime = (TextView) convertView.findViewById(R.id.time);
			holder.mName.setText(produtcInfo.get(position).getName());
			holder.mTime.setText(produtcInfo.get(position).getDate());
			//			holder.mAddress.setText(mIstallInfos.get(position).getUnitName());
			return convertView;
		}
		private class ChildViewHolder {

			TextView mName;
			TextView mTime;
		}
		
	}
}
