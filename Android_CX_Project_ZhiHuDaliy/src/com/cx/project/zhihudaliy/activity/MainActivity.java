package com.cx.project.zhihudaliy.activity;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cx.project.zhihudaliy.R;
import com.cx.project.zhihudaliy.adapter.ThemeAdapter;
import com.cx.project.zhihudaliy.c.API;
import com.cx.project.zhihudaliy.custom.CustomTitle;
import com.cx.project.zhihudaliy.entity.Others;
import com.cx.project.zhihudaliy.entity.Theme;
import com.cx.project.zhihudaliy.fragment.ContentFragment;
import com.cx.project.zhihudaliy.fragment.MainFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * ��ҳ
 * @author CxiaoX
 *
 * 2014��12��5������3:03:07
 */
public class MainActivity extends FragmentActivity  implements OnItemClickListener {
	private CustomTitle cTitle;
	
	private Theme theme;
	
	
	// Volley���
	private RequestQueue mQueue;
	
	//�໬�˵����
	private SlidingMenu menu;
	private ListView lvThems;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initTitle();
		initSlidingMenu();
		initSlideingMenuData();
		
		showMainFragment();
		 
		
	}

	/**
	 * ��ʾ mianFragment
	 */
	private void showMainFragment() {
		FragmentTransaction ft =getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.fl_content,  MainFragment.newInstance());
		ft.commit();
	}
	
	/**
	 * ��ʼ������
	 */
	private void initTitle() {
		cTitle =(CustomTitle) findViewById(R.id.custom_title);
		cTitle.setTitle("��ҳ");
		
		cTitle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				menu.toggle();
			}
		});
	}
	
	
	/**
	 * ��ʼ���໬�˵�
	 */
	private void initSlidingMenu() {
		
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setShadowWidth(10);
		menu.setBehindOffset(50);
		menu.setBehindWidth(350);
		menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		menu.setMenu(R.layout.sliding_menu);
		
		
		lvThems = (ListView) findViewById(R.id.lv_thems);
		lvThems.setOnItemClickListener(this);
	
		
	}


	/**
	 * ��ʼ���໬�˵��е�����
	 */
	private void initSlideingMenuData() {
		mQueue =Volley.newRequestQueue(this);
		mQueue.add(new JsonObjectRequest(Method.GET, API.getThemesUrl(), null, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				 theme = Theme.parse(response);
				
				ThemeAdapter adapter = new ThemeAdapter(MainActivity.this, theme);
				lvThems.setAdapter(adapter);
			}
		}, null));
		
	}
	
	

	/*-------------���� �����б�� ����¼�-------------*/
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		if(position ==0){
			initTitle();
			showMainFragment();
		}else{
			Others others = theme.getOthers().get(position);
			cTitle.setTitle(others.getName());
			
			String image = others.getImage();
			String description = others.getDescription();
			int themeId = others.getId();
			
			FragmentTransaction ft =getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.fl_content,  ContentFragment.newInstance(image,description,themeId));
			ft.commit();
			
			
			
		}
		menu.toggle();
	}

	/*-------------���� �����б�� ����¼�-------------*/


}
