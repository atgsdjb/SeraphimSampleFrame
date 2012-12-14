package com.seraphim.td.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SeraphListAdapter<T> extends BaseAdapter {
	Context context;
	private List<T> mList;
	
	
	public  SeraphListAdapter(Context context,List<T> data){
		mList = data;
		this.context = context;
		
	}
	public void addData(T d)
	{
		mList.add(d);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView  view = new TextView(context);
		String text  = mList.get(position).toString();
		view.setText(text);
		return view;
	}

}
