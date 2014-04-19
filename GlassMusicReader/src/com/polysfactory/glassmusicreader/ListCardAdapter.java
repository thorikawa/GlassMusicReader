package com.polysfactory.glassmusicreader;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.glass.widget.CardScrollAdapter;

import java.util.List;

public class ListCardAdapter extends CardScrollAdapter {

	private Context mContext;
	private List<String> mList;
	private LayoutInflater mLayoutInflater;
	private String title;

	public ListCardAdapter(Context context, String title, List<String> list) {
		this.mContext = context;
		this.mList = list;
		this.mLayoutInflater = LayoutInflater.from(context);
		this.title = title;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = mLayoutInflater.inflate(R.layout.text_view, null);
		TextView textView = (TextView) view.findViewById(R.id.text);
		textView.setText(Html.fromHtml(mList.get(position)));
		TextView titleView = (TextView) view.findViewById(R.id.title);
		Log.d(C.TAG, title);
		titleView.setText(title);
		return view;
	}

	@Override
	public int getPosition(Object o) {
		return -1;
	}
}
