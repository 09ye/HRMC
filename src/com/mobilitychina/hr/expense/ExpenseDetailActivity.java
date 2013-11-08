package com.mobilitychina.hr.expense;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.R.integer;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilitychina.hr.R;
import com.mobilitychina.hr.app.BaseActivity;
import com.mobilitychina.hr.app.DetailTitlebar;
import com.mobilitychina.hr.app.MCApplication;
import com.mobilitychina.hr.expense.data.ExpenseInfo;
import com.mobilitychina.hr.expense.data.ProductInfo;
import com.mobilitychina.hr.service.HttpPostService;
import com.mobilitychina.hr.util.CommonUtil;
import com.mobilitychina.hr.util.ImagesTool;
import com.mobilitychina.intf.ITaskListener;
import com.mobilitychina.intf.Task;
import com.mobilitychina.net.HttpPostTask;
import com.mobilitychina.net.NetObject;

/**
 * 费用详情
 * @author baohua
 *
 */
public class ExpenseDetailActivity extends BaseActivity implements OnClickListener,ITaskListener{

	private HttpPostTask uploadTask;
	private HttpPostTask imgTask;
	private HttpPostTask proListTask;
//	private HttpPostTask proInfoTask;
	//只读信息
	private TextView reName;
	private TextView reDepartment;
	private TextView reDeclared;
	private TextView reDate;
	private TextView reAmount;
	private Button upload;
	private List<Bitmap> cramerList= new ArrayList<Bitmap>();
	private List<String> newCramerList= new ArrayList<String>();
	private DetailTitlebar detailTitlebar;
	private LinearLayout listLayout;

	private Gallery mGallery;
	private MyAdpater mAdpater;
	private ImageAdapter imgAdapter;
	private ExpenseInfo expenseInfo = new ExpenseInfo();
	private List<ExpenseInfo> pointInfoList = new ArrayList<ExpenseInfo>();
	private List<ProductInfo> productInfoList = new ArrayList<ProductInfo>();
	private List<String> projectListId = new ArrayList<String>();
	private ListView lisView ;
	private String id;
	private String picPath;
	private Uri photoUri;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_expense_detail);
		detailTitlebar = (DetailTitlebar) findViewById(R.id.title);
		expenseInfo = (ExpenseInfo) getIntent().getSerializableExtra("expenseInfo");
		id = getIntent().getStringExtra("id");

//				upload = (Button) findViewById(R.id.add_point);
		reName = (TextView) findViewById(R.id.read_name);
		reDepartment= (TextView) findViewById(R.id.read_department);
		reDeclared = (TextView) findViewById(R.id.read_declared);
		reDate = (TextView) findViewById(R.id.read_date);
		reAmount = (TextView) findViewById(R.id.read_amount);
		//		myList  = (LinearLayout) findViewById(R.id.myList);
		lisView = (ListView) findViewById(R.id.myPoinList);
		mGallery = (Gallery) findViewById(R.id.cramer);
		listLayout = (LinearLayout) findViewById(R.id.myList);
		
		detailTitlebar.setTitle("费用详情");
		detailTitlebar.setLeftButton(R.drawable.icon_back, new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		createNewCramerItem();
		mAdpater = new MyAdpater();
		imgAdapter = new ImageAdapter(this,cramerList);
		mGallery.setAdapter(imgAdapter);
		mGallery.setSelection(cramerList.size()-1);
		mGallery.setOnItemClickListener(new OnItemClickListenerImpl()) ;
		detailTitlebar.setLeftButton(null, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		//上传图片
		detailTitlebar.setRightButton("上传", new OnClickListener() {
			@Override
			public void onClick(View v) {
				showProgressDialog("正在上传信息...");
				JSONArray arry = new JSONArray();
				if(newCramerList.size()!=0)
					for(String s:newCramerList){
						arry.put(s);
						System.out.println(s);
					}
//				http://127.0.0.1:8091/mcsystem_esb/createIrAttachment
//				data: {
//						"datas":["iVBORw0KRNUgxUopUIFVIHfI9cgI5h1xGupE7yAAygvyGvEcxlIGyUT3UDLVDuag3G3"],
//						"res_model": "hr.expense.expense", 
//						"res_id": 1, 
//						"name": "google-calendars-connector-24x16-1.png", 
//						"datas_fname": "google-calendars-connector-24x16-1.png"
//							}
				uploadTask = new HttpPostTask(ExpenseDetailActivity.this);
				uploadTask.setUrl(HttpPostService.SOAP_URL+"createIrAttachment");
				uploadTask.getTaskArgs().put("res_id", Integer.valueOf(id));
				uploadTask.getTaskArgs().put("datas", arry);
				uploadTask.getTaskArgs().put("res_model", "hr.expense.expense");
				uploadTask.getTaskArgs().put("name", "google-calendars-connector-24x16-1.png");
				uploadTask.getTaskArgs().put("datas_fname", "google-calendars-connector-24x16-1.png");
				uploadTask.setListener(ExpenseDetailActivity.this);
				uploadTask.start();
			}
		});
		inintDate();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		inintDate();
	}
	private void inintDate() {
		reName.setText(expenseInfo.getEmployeeName());
		reDeclared.setText(expenseInfo.getName());
		reDate.setText(expenseInfo.getDate());
		reAmount.setText(expenseInfo.getAmount());

		this.showProgressDialog("正在获取信息..");
		proListTask = new HttpPostTask(MCApplication.getInstance().getApplicationContext());
		proListTask.setUrl(HttpPostService.SOAP_URL+"getHrExpenseMain");
		proListTask.getTaskArgs().put("id", id);
		proListTask.setListener(this);
		proListTask.start();
		imgTask = new HttpPostTask(MCApplication.getInstance().getApplicationContext());
		imgTask.setUrl(HttpPostService.SOAP_URL+"getIrAttachment");
		imgTask.getTaskArgs().put("id", id);
		imgTask.setListener(this);
		imgTask.start();
		

	}
	/**
	 * 产生新的子cramerItem
	 */


	public class ImageAdapter extends BaseAdapter{
		private Context mContext;
		private List<Bitmap> lis;
		public ImageAdapter(Context c,List<Bitmap> li){
			mContext = c;
			lis=li;
		}

		public int getCount() {
			return lis.size();
		}
		public Object getItem(int position) {
			return position;
		}
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView i = new ImageView(mContext);
			i.setImageBitmap(lis.get(position));
			i.setScaleType(ImageView.ScaleType.FIT_CENTER);
			i.setLayoutParams(new Gallery.LayoutParams(105,105));
			return i;
		}


	}
	@Override
	public void onTaskFinished(Task task) {
		dismissDialog();
			if(proListTask==task){
				NetObject result = (NetObject) task.getResult();
				if(result==null)
					//				showDialog("提示", "暂无信息", null);
					return;
				if(projectListId==null)
				projectListId = new ArrayList<String>();
				projectListId.clear();
				try {
					String 	depart = (String) result.anyListForKey("department_id").get(1);
					reDepartment.setText(depart);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					reDepartment.setText("");
				}
				List<Object> id  = result.anyListForKey("line_ids");
				if(id!=null&&id.size()>0)
				for(int i =0 ;i<id.size(); i++){
					HttpPostTask	proInfoTask = new HttpPostTask(MCApplication.getInstance().getApplicationContext());
					proInfoTask.setUrl(HttpPostService.SOAP_URL+"getHrExpenseLine");
					proInfoTask.getTaskArgs().put("id",id.get(i));
					proInfoTask.setListener(this);
					proInfoTask.start();
				}
				
			}
			
			else if(imgTask==task){
				List<NetObject> result = (List<NetObject>)task.getResult();
				if(result==null)
					return;
					//				showDialog("提示", "暂无信息", null);
				if(result.size()>0)
				for(NetObject netObject :result){
					String string = (String) netObject.stringForKey("datas");
					if(!string.equalsIgnoreCase("false")){
						cramerList.remove(cramerList.size()-1);
						Bitmap s = ImagesTool.stringtoBitmap(string);
						Bitmap b =	ImagesTool.initImage(s);
						cramerList.add(b);
						createNewCramerItem();
						imgAdapter.notifyDataSetChanged();
					}
				}
				

			}else if(uploadTask==task){
				if(((HttpPostTask) task).getResultState().getMessage().equalsIgnoreCase("OK")){
					showDialog("提示", "上传成功", null);
				}
			}
			else
			{
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
				lisView.setAdapter(mAdpater);
				mAdpater.notifyDataSetChanged();
				lisView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(ExpenseDetailActivity.this,ProductDetaiActivity.class);
						intent.putExtra("product", productInfoList.get(arg2));
						startActivity(intent);
					}
				});
			}
			}


@Override
public void onTaskFailed(Task task) {
	this.dismissDialog();
	System.out.println("picture load"+task.getError().toString());
	this.showDialog("提示","网络异常,请重试", null);

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
	List<ProductInfo> produtcInfo = new ArrayList<ProductInfo>();
	public void setList(List<ProductInfo> LoctionInfo) {
		this.produtcInfo = LoctionInfo;
		//扩张 listview 解决 listview和ScrollView冲突问题,自定义listview的大小
		ViewGroup.LayoutParams params = listLayout.getLayoutParams(); 
		View listItem = this.getView(0, null, lisView); 
		 listItem.measure(0, 0); 
	     params.height = listItem.getMeasuredHeight()*this.produtcInfo.size()
	    		 +lisView.getDividerHeight() * (this.produtcInfo.size()/2); 
	    listLayout.setLayoutParams(params);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null){
			convertView = LayoutInflater.from(ExpenseDetailActivity.this).inflate(
					R.layout.item_expense, null);
		}
		if (produtcInfo == null||produtcInfo.size()==0) {
			return CommonUtil.buildListSimpleMsgItemView(ExpenseDetailActivity.this, convertView, "暂无数据");
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
@Override
public void onClick(View arg0) {
	// TODO Auto-generated method stub

}
/**
 * 产生新的子cramerItem
 */
private void createNewCramerItem(){
	if(cramerList==null){
		cramerList = new ArrayList<Bitmap>();
	}
	Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.emotionstore_custom_add_icon);
	cramerList.add(bitmap);

}
private class OnItemClickListenerImpl implements OnItemClickListener {

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(position==cramerList.size()-1){
			takePhoto();
		}
	}
}
/**
 * 拍照获取图片
 */
private void takePhoto() {
	//执行拍照前，应该先判断SD卡是否存在
	String SDState = Environment.getExternalStorageState();
	if(SDState.equals(Environment.MEDIA_MOUNTED))
	{
		
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"
		ContentValues values = new ContentValues();  
		photoUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);  
		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
		startActivityForResult(intent, 1);
	}else{
		Toast.makeText(this,"内存卡不存在", Toast.LENGTH_LONG).show();
	}
}
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	try {
		if (resultCode == Activity.RESULT_OK && requestCode == 1) {
			doPhoto(requestCode, data);
			if(newCramerList==null){
				newCramerList = new ArrayList<String>();
			}
			Bitmap bm = BitmapFactory.decodeFile(picPath);
			cramerList.remove(cramerList.size() - 1);
			cramerList.add(ImagesTool.initImage(bm));
			newCramerList.add(ImagesTool.bitmaptoString(ImagesTool.initImage(bm)));
			createNewCramerItem();
			imgAdapter.notifyDataSetChanged();
		}
	} catch (Exception e) {
		Toast.makeText(this, "拍照错误", 1);
		return;
	}
	super.onActivityResult(requestCode, resultCode, data);
}

/**
 * 选择图片后，获取图片的路径
 * @param requestCode
 * @param data
 */
private void doPhoto(int requestCode,Intent data){
	String[] pojo = {MediaStore.Images.Media.DATA};
	Cursor cursor = managedQuery(photoUri, pojo, null, null,null);   
	if(cursor != null ){
		int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
		cursor.moveToFirst();
		picPath = cursor.getString(columnIndex);	
		if(Integer.parseInt(Build.VERSION.SDK) < 14)  
        {  
            cursor.close();          }  
		
	}else{
		Toast.makeText(this, "选择文件不正确!", Toast.LENGTH_LONG).show();
		
	}
}

}

