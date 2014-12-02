package com.cx.project.zhihudaliy.task;

import com.alibaba.fastjson.JSON;
import com.cx.project.zhihudaliy.activity.MainActivity;
import com.cx.project.zhihudaliy.c.API;
import com.cx.project.zhihudaliy.entity.StartImage;
import com.cx.project.zhihudaliy.util.NetUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

public class StartImageTask extends AsyncTask<Void, Void, Bitmap> implements AnimationListener {
	private Context context;
	private ImageView imgStart;
	

	public StartImageTask(Context context,ImageView imgStart) {
		this.context = context;
		this.imgStart = imgStart;
	}

	@Override
	protected Bitmap doInBackground(Void... params) {
		String json = NetUtil.getJson(API.getStartImageUrl());
		StartImage startImage =JSON.parseObject(json, StartImage.class);
		
		return NetUtil.getBitmap(startImage.getImg());
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		if(result!=null){
			imgStart.setImageBitmap(result);
		}
		
		//ʹ�ö�������ͼƬ
		
		Animation animation = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f); // ��ͼƬ�Ŵ�1.2���������Ŀ�ʼ����
		animation.setDuration(2000); // ��������ʱ��
		animation.setFillAfter(true); // ����������ͣ���ڽ�����λ��
		animation.setAnimationListener(this);
		imgStart.startAnimation(animation);
		
		
	}

	
	/*---------------��������---------------*/
	@Override
	public void onAnimationStart(Animation animation) {
		
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		context.startActivity(new Intent(context, MainActivity.class));
		
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		
	}
	/*---------------��������---------------*/

}
