package com.mobilitychina.hr.app;



import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class MCApplication extends Application {
	private static MCApplication mInstance = null;
    public boolean m_bKeyRight = true;

	
	@Override
    public void onCreate() {
	    super.onCreate();
		mInstance = this;
	}
	
	
	public static MCApplication getInstance() {
		return mInstance;
	}
	
	

}
