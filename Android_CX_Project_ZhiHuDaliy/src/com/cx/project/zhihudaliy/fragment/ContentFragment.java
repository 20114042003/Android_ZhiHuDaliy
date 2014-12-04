package com.cx.project.zhihudaliy.fragment;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.cx.project.zhihudaliy.R;
import com.cx.project.zhihudaliy.c.API;
import com.cx.project.zhihudaliy.cache.BitmapCache;
import com.cx.project.zhihudaliy.custom.CustomListViewForScrollView;
import com.cx.project.zhihudaliy.entity.Story;
import com.cx.project.zhihudaliy.entity.ThemeNew;

public class ContentFragment extends Fragment {

	// 控件相关
	private TextView txDesc;
	private NetworkImageView imgContent;
	private CustomListViewForScrollView lvTheme;
	private ScrollView svContent;

	// 参数相关
	private String image;
	private String description;
	private int themeId;

	// Volley
	private RequestQueue mQueue;

	// 数据相关
	private ThemeNew themeNew;

	public static ContentFragment newInstance(String image, String description,
			int themeId) {
		ContentFragment cf = new ContentFragment();
		Bundle args = new Bundle();
		args.putString("image", image);
		args.putString("description", description);
		args.putInt("themeId", themeId);
		cf.setArguments(args);
		return cf;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View layout = inflater.inflate(R.layout.fragment_content, container,
				false);

		initPara();
		initView(layout);
		initNewsData();

		return layout;
	}

	/**
	 * 初始化参数
	 */
	private void initPara() {
		image = getArguments().getString("image");
		description = getArguments().getString("description");
		themeId = getArguments().getInt("themeId");

	}

	/**
	 * 初始化控件
	 */
	private void initView(View layout) {
		mQueue = Volley.newRequestQueue(getActivity());

		initImage(layout);
		initTitle(layout);
		initlvTheme(layout);
	}

	/**
	 * 初始化 图片
	 * 
	 * @param layout
	 */
	private void initImage(View layout) {
		imgContent = (NetworkImageView) layout.findViewById(R.id.img_content);

		imgContent.setImageUrl(image,
				new ImageLoader(mQueue, new BitmapCache()));
	}

	/**
	 * 初始化描述
	 * 
	 * @param layout
	 */
	private void initTitle(View layout) {
		txDesc = (TextView) layout.findViewById(R.id.tx_desc);
		txDesc.setText(description);

	}

	/**
	 * 初始化主题新闻列表
	 * 
	 * @param layout
	 */
	private void initlvTheme(View layout) {
		lvTheme = (CustomListViewForScrollView) layout
				.findViewById(R.id.lv_fragment_theme);
	}

	/**
	 * 初始化主题新闻数据。
	 */
	private void initNewsData() {

		mQueue.add(new JsonObjectRequest(Method.GET, String.format(
				API.getTheme(), themeId), null, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				themeNew = ThemeNew.parse(response);
				ThemeNewsAdapter adapter = new ThemeNewsAdapter();
				lvTheme.setAdapter(adapter);

			}
		}, null));

	}

	class ThemeNewsAdapter extends BaseAdapter {

		private ViewHolder viewHolder;

		@Override
		public int getCount() {
			return themeNew.getStories().size();
		}

		@Override
		public Object getItem(int position) {
			return themeNew.getStories().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.list_news_item, parent, false);

				viewHolder = new ViewHolder();
				viewHolder.txTitle = (TextView) convertView.findViewById(R.id.tx_title);
				viewHolder.imgThumb = (NetworkImageView) convertView.findViewById(R.id.img_thumb);
				viewHolder.imgThumb.setVisibility(View.GONE);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();

			}
			
			Story story = themeNew.getStories().get(position);
			viewHolder.txTitle.setText(story.getTitle());
			if(story.getImages()!=null &&  story.getImages().size()>0){
				viewHolder.imgThumb.setVisibility(View.VISIBLE);
				viewHolder.imgThumb.setImageUrl(story.getImages().get(0), new ImageLoader(mQueue, new BitmapCache()));
				
			}
			return convertView;
		}

		private class ViewHolder {
			public TextView txTitle;
			public NetworkImageView imgThumb;
		}

	}

}
