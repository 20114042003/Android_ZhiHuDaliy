package com.cx.project.zhihudaliy.adapter;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.cx.project.zhihudaliy.R;
import com.cx.project.zhihudaliy.cache.BitmapCache;
import com.cx.project.zhihudaliy.entity.News;
import com.cx.project.zhihudaliy.entity.Story;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomListViewAdapter extends BaseAdapter{
	private Context context;
	private News latest;
	private RequestQueue mQueue;
	
	private ViewHolder viewHolder;
	
	public CustomListViewAdapter(Context context,News latest,RequestQueue mQueue) {
		this.context = context;
		this.latest = latest;
		this.mQueue= mQueue;
	}

	@Override
	public int getCount() {
		return latest==null?0: latest.getStories().size();
	}

	@Override
	public Object getItem(int position) {
		return latest.getStories().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Story story = latest.getStories().get(position);
		
		//判断是否为标题
		if(story.getId()==0){ //ListView 的一个Head模块
			convertView = LayoutInflater.from(context).inflate( R.layout.list_news_titile, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.txTitle = (TextView) convertView.findViewById(R.id.tx_title);
			viewHolder.txTitle.setText(story.getTitle());
		}else{
			if (convertView == null || story.getId()!=0) {
				convertView = LayoutInflater.from(context).inflate( R.layout.list_news_item, parent, false);
				
				viewHolder = new ViewHolder();
				viewHolder.txTitle = (TextView) convertView.findViewById(R.id.tx_title);
				viewHolder.imgThumb = (NetworkImageView) convertView.findViewById(R.id.img_thumb);
				
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			viewHolder.txTitle.setText(story.getTitle());
			if(story.getImages() != null&&story.getImages().size()>0){
				viewHolder.imgThumb.setImageUrl(story.getImages().get(0), new ImageLoader(mQueue, new BitmapCache()));
			}
		}
		
		
		
		return convertView;
	}
	
	class ViewHolder {
		public TextView txTitle;
		public NetworkImageView imgThumb;
	}
	
}
