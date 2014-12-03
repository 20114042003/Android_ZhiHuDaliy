package com.cx.project.zhihudaliy.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.cx.project.zhihudaliy.R;
import com.cx.project.zhihudaliy.cache.BitmapCache;
import com.cx.project.zhihudaliy.entity.News;
import com.cx.project.zhihudaliy.entity.TopStory;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class CustomSlide extends FrameLayout implements OnPageChangeListener {
	private Context context; // 上下文，用与创建SmartImageView
	private ViewPager vpSlide; // viewpager
	private LinearLayout dotsGroup; // viewpager 中的点布局
	private TextView txTitle; // viewpager 中的标题
	private List<NetworkImageView> imageViews; // 存viewpager 中的图

	// 数据相关
	private News latest = null;
	
	//自动播放
	private Timer timer;

	public CustomSlide(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.custom_slide, this);

		initViewPager();
		initDotsGroup();
		initTitle();
	}

	/**
	 * 绑定标题
	 */
	private void initTitle() {
		txTitle = (TextView) findViewById(R.id.tx_title);
		txTitle.setTextSize(22.0f);
	}

	/**
	 * 绑定点布局
	 */
	private void initDotsGroup() {
		dotsGroup = (LinearLayout) findViewById(R.id.dots_group);
	}

	/**
	 * 初始ViewPager控件
	 */
	private void initViewPager() {
		vpSlide = (ViewPager) findViewById(R.id.vp_slide);
		vpSlide.setOnPageChangeListener(this); // 设置监听
	}

	private int item; // ViewPager的Postion
	private Handler pageChangeHandler = new Handler() {
		@SuppressLint("HandlerLeak")
		@Override
		public void handleMessage(Message msg) {
			vpSlide.setCurrentItem(item);
			if (item == imageViews.size() - 1) {
				item = 0;
			} else {
				item++;
			}
		}
	};

	/**
	 * 初始化 幻灯
	 * 
	 * @param latest
	 *            外面提供
	 */
	public void initSlide(News latest, RequestQueue mQueue) {
		this.latest = latest;

		// 初始化ImageViews
		imageViews = new ArrayList<NetworkImageView>();
		for (int i = 0; i < latest.getTopStories().size(); i++) {
			TopStory topStory = latest.getTopStories().get(i);
			NetworkImageView networkImageView = new NetworkImageView(context);
			networkImageView.setScaleType(ScaleType.CENTER_CROP);
			networkImageView.setImageUrl(topStory.getImage(), new ImageLoader(
					mQueue, new BitmapCache()));
			imageViews.add(networkImageView);
		}

		// ViewPager赋值
		MyPagerAdapter pagerAdapter = new MyPagerAdapter();
		vpSlide.setAdapter(pagerAdapter);

		// 用于自动播放
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				pageChangeHandler.sendEmptyMessage(0);
			}
		}, 3000, 3000);

		// 初始化小圆点
		initSmallDot(0);

		// 初始化标题
		txTitle.setText(latest.getTopStories().get(0).getTitle());
	}
	
	/**
	 * 关闭幻灯的自动播放
	 */
	public void cancel(){
		timer.cancel();
	}

	/**
	 * 初始化小圆点
	 * 
	 * @param index
	 */
	private void initSmallDot(int index) {
		dotsGroup.removeAllViews();

		for (int i = 0; i < imageViews.size(); i++) {
			ImageView imageView = new ImageView(context);
			imageView.setImageResource(R.drawable.dot_default);
			imageView.setPadding(5, 0, 5, 0);

			dotsGroup.addView(imageView);
		}

		// 设置选中项
		((ImageView) dotsGroup.getChildAt(index)).setImageResource(R.drawable.dot_selected);
	}

	/*---------------------监听ViewPager 的滑动---------------------*/
	@Override
	public void onPageSelected(int position) {
		initSmallDot(position);
		item = position;
		txTitle.setText(latest.getTopStories().get(position).getTitle());
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	/*---------------------监听ViewPager 的滑动---------------------*/

	/**
	 * 
	 * @description viewPager 的适配器<br />
	 * @author cx <br />
	 *         Created on 2014-12-1下午8:27:05
	 *
	 */
	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageViews.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(imageViews.get(position));
			return imageViews.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(imageViews.get(position));
		}

	}
}
