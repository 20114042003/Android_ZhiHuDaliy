package com.cx.project.zhihudaliy.application;

import android.app.Application;

import com.androidpn.client.ServiceManager;
import com.cx.project.zhihudaliy.R;

public class MyApplication extends Application{
	
	@Override
	public void onCreate() {
		super.onCreate();
		initPushServer();
	}

	private void initPushServer() {
		ServiceManager serviceManager = new ServiceManager(getApplicationContext());
		serviceManager.setNotificationIcon(R.drawable.ic_launcher);
		serviceManager.startService();		
	}

}
