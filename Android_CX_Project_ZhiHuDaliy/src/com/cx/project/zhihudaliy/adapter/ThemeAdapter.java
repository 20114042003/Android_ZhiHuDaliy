package com.cx.project.zhihudaliy.adapter;

import java.util.List;

import com.cx.project.zhihudaliy.R;
import com.cx.project.zhihudaliy.entity.Others;
import com.cx.project.zhihudaliy.entity.Theme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ThemeAdapter extends BaseAdapter{
	private Context context;
	private Theme theme;
	
	private ViewHolder viewHolder;
	

	public ThemeAdapter(Context context,Theme theme) {
		this.context = context;
		this.theme = theme;
	}

	@Override
	public int getCount() {
		return theme.getOthers() ==null ?0: theme.getOthers().size();
	}

	@Override
	public Object getItem(int position) {
		return theme.getOthers().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = LayoutInflater.from(context).inflate(R.layout.list_theme, parent,false);
			
			viewHolder =new ViewHolder();
			viewHolder.txTitle = (TextView) convertView.findViewById(R.id.tx_title);
			
			convertView.setTag(viewHolder);
			
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		Others other=   theme.getOthers().get(position);
		viewHolder.txTitle.setText(other.getName());
		
		return convertView;
	}
	
	
	class ViewHolder{
		public TextView txTitle;
	}

}
