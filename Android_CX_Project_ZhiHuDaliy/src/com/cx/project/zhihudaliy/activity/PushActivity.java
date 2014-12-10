package com.cx.project.zhihudaliy.activity;

import com.cx.project.zhihudaliy.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class PushActivity extends Activity{
	
	private TextView txUrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_push);
		
		initView();
		
		
		
	}

	private void initView() {
		
		txUrl = (TextView) findViewById(R.id.tx_utl);
		
		txUrl.setText(getIntent().getStringExtra("url"));
		
	}

}
