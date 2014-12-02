package com.cx.project.zhihudaliy.adapter;

import com.cx.project.zhihudaliy.R;
import com.cx.project.zhihudaliy.entity.Latest;
import com.cx.project.zhihudaliy.entity.Story;
import com.loopj.android.image.SmartImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomListViewAdapter extends BaseAdapter{
	private Context context;
	private Latest latest;
	private ViewHolder viewHolder;
	
	
	public CustomListViewAdapter(Context context,Latest latest) {
		this.context = context;
		this.latest = latest;
	}

	@Override
	public int getCount() {
		return latest.getStories().size();
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
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate( R.layout.list_news_item, parent, false);
			
			viewHolder = new ViewHolder();
			viewHolder.txTitle = (TextView) convertView.findViewById(R.id.tx_title);
			viewHolder.imgThumb = (SmartImageView) convertView.findViewById(R.id.img_thumb);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		Story story = latest.getStories().get(position);
		viewHolder.txTitle.setText(story.getTitle());
		viewHolder.imgThumb.setImageUrl(story.getImages().get(0));
		
		return convertView;
	}
	
	
	
	class ViewHolder {
		public TextView txTitle;
		public SmartImageView imgThumb;
	}
	
}
