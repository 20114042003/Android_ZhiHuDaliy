package com.cx.project.zhihudaliy.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cx.project.zhihudaliy.R;

public class ActionBarCustomBack extends FrameLayout{
	

	private ImageView imgLogo;
	private LinearLayout ll_back;
	
	public ActionBarCustomBack(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		LayoutInflater.from(context).inflate(R.layout.custom_back_actionbar, this);
		imgLogo=(ImageView) findViewById(R.id.img_back_logo);
		ll_back = (LinearLayout) findViewById(R.id.custom_ll_back);
	}
	
//	public void setOnclickListener(OnClickListener l){
//		imgLogo.setOnClickListener(l);
//		
//	}
//	
//	public interface ActionBarOnClickListener{
//		void onClick();
//	}

	
	

}
