package com.mobilitychina.hr.home;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mobilitychina.hr.R;
import com.mobilitychina.hr.app.BaseDetailListActivity;
import com.mobilitychina.hr.app.DetailTitlebar;
import com.mobilitychina.hr.expense.ExpenseDetailActivity;
import com.mobilitychina.hr.expense.data.ExpenseInfo;
import com.mobilitychina.hr.service.HttpPostService;
import com.mobilitychina.intf.ITaskListener;
import com.mobilitychina.intf.Task;
import com.mobilitychina.net.HttpPostTask;
import com.mobilitychina.net.NetObject;
import com.mobilitychina.util.Environment;

/**
 * @author baohua
 *拉取费用列表
 */
public class MainActivity  extends BaseDetailListActivity implements ITaskListener{
	private MyAdpater mAdpater;
	DetailTitlebar detailTitlebar;
	HttpPostTask getListTask;
	List<ExpenseInfo> expenseInfoList = new ArrayList<ExpenseInfo>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		detailTitlebar = (DetailTitlebar) findViewById(R.id.detailTitlebar);
		detailTitlebar.setTitle("报销申请");
		mAdpater = new MyAdpater();
		this.showProgressDialog("正在获取信息..");
		getListTask = new HttpPostTask(this);
		getListTask.setUrl(HttpPostService.SOAP_URL+"getAllHrExpense");
//		loginTask.setUrl(Geocoding.getInstance().getDefinitUrl()+"login");
//		loginTask.getTaskArgs().put("imei", CommonUtil.getIMEI(this));
		getListTask.setListener(this);
		getListTask.start();
		 
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		if(expenseInfoList.size()>position){
			Intent intent = new Intent(this,ExpenseDetailActivity.class);
			intent.putExtra("id", expenseInfoList.get(position).getId());
			intent.putExtra("expenseInfo", expenseInfoList.get(position));
			startActivity(intent);
		}
	}

	@Override
	public void onTaskFinished(Task task) {
		// TODO Auto-generated method stub
		dismissDialog();
		List<NetObject> result = (List<NetObject>)task.getResult();
//		Log.i("HttpPostTask","Point message:" +result.get(0).toString());
//		NetResultState state = ((HttpPostTask)task).getResultState();
//		int code = state.getResultCode();
//		String message = state.getMessage();	
			if (result == null) {
				return ;
			}
			// {"code":0,"message":"OK","data":[{"partner_id":[5,"中国外交部"],"line_status":"E","product_id":[2,"数字报栏屏幕"],"line_id":1,"longitue":116,"latitude":40}]
			if(expenseInfoList==null)
				expenseInfoList = new ArrayList<ExpenseInfo>();
				expenseInfoList.clear();
			for (NetObject netObject : result) {
//				if(netObject.stringForKey("line_status").equalsIgnoreCase("I")){
					
					ExpenseInfo product = new ExpenseInfo();
//					product.setId(netObject.stringForKey("line_id"));
					product.setId(netObject.stringForKey("id"));
					product.setAmount(netObject.stringForKey("amount"));
					product.setName(netObject.stringForKey("name"));
					product.setDate(netObject.stringForKey("date"));
					product.setEmployeeName((String) netObject.anyListForKey("employee_id").get(1));
					
//				product.setPower(netObject.boolForKey("custName"));
//					product.setLatitude(netObject.doubleForKey("latitude"));
//					product.setLongitue(netObject.doubleForKey("longitue"));
//					product.setStatus(netObject.stringForKey("line_status"));
					expenseInfoList.add(product);
//				}
			}
			if(mAdpater==null)
			mAdpater = new MyAdpater();
			mAdpater.setList(expenseInfoList);
			this.setListAdapter(mAdpater);
			mAdpater.notifyDataSetChanged();
		
	}

	@Override
	public void onTaskFailed(Task task) {
		// TODO Auto-generated method stub
		dismissDialog();
		showDialog("提示","网络异常,请重试", null);
		ExpenseInfo product = new ExpenseInfo();
//		product.setId(netObject.stringForKey("line_id"));
		product.setId("1");
		product.setAmount("2");
		product.setName("mrs");
		product.setDate("2012");
		product.setEmployeeName("xx");
		
//	product.setPower(netObject.boolForKey("custName"));
//		product.setLatitude(netObject.doubleForKey("latitude"));
//		product.setLongitue(netObject.doubleForKey("longitue"));
//		product.setStatus(netObject.stringForKey("line_status"));
		expenseInfoList.add(product);
		expenseInfoList.add(product);
		expenseInfoList.add(product);
		expenseInfoList.add(product);
		if(mAdpater==null)
			mAdpater = new MyAdpater();
			mAdpater.setList(expenseInfoList);
			mAdpater.notifyDataSetChanged();
			this.setListAdapter(mAdpater);
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
		
		List<ExpenseInfo> expenseList =  new ArrayList<ExpenseInfo>();
		public void setList(List<ExpenseInfo> expenseList) {
			this.expenseList = expenseList;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return expenseList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return expenseList.get(arg0);
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
				convertView = LayoutInflater.from(MainActivity.this).inflate(
						R.layout.item_expense, null);
			}
			ChildViewHolder holder = new ChildViewHolder();
			holder.mName = (TextView) convertView.findViewById(R.id.name);
			holder.mTime = (TextView) convertView.findViewById(R.id.time);
			holder.mName.setText(expenseList.get(position).getEmployeeName());
			holder.mTime.setText(expenseList.get(position).getDate());
			
			return convertView;
		}
		private class ChildViewHolder {

			TextView mName;
			TextView mTime;
		}
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			dialog();
			return true;
		}
		return false;
	}
	protected void dialog() {

		AlertDialog.Builder builder = new Builder(this);

		builder.setMessage("确定要退出吗?");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Environment.getInstance().setSessionId("");
				finish();
			}

		});

		builder.setNegativeButton("取消",

		new android.content.DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
				
			}

		});

		builder.create().show();

	}


}
