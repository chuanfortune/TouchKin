package com.touchKin.touchkinapp.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.touchKin.touchkinapp.R;
import com.touchKin.touchkinapp.custom.RoundedImageView;
import com.touchKin.touchkinapp.model.ParentListModel;


public class ImageAdapter extends BaseAdapter {
	Context mContext;
	LayoutInflater mLayoutInflater;

	List<ParentListModel> parentList;

	// int[] mResources = { R.drawable.mom, R.drawable.activity_bg,
	// R.drawable.mom, R.drawable.mom, R.drawable.mom };

	public ImageAdapter(Context context, List<ParentListModel> parentList) {
		mContext = context;
		this.parentList = parentList;
		mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return parentList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return parentList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.image_item, null);
		}
		RoundedImageView imageView = (RoundedImageView) convertView
				.findViewById(R.id.parentImage);
		if (parentList.get(position).getIsSelected()) {
			convertView.setBackgroundDrawable(mContext.getResources()
					.getDrawable(R.drawable.circular_image_selected));
		} else {
			convertView.setBackgroundDrawable(mContext.getResources()
					.getDrawable(R.drawable.circular_image));
		}
		imageView.setImageResource(R.drawable.mom);
		if (position + 1 == parentList.size()) {
			convertView = mLayoutInflater
					.inflate(R.layout.image_add_item, null);

		}
		return convertView;
	}
	// @Override
	// public boolean isViewFromObject(View view, Object object) {
	// return view == ((LinearLayout) object);
	// }
	//
	// @Override
	// public Object instantiateItem(ViewGroup container, int position) {
	// View itemView = mLayoutInflater.inflate(R.layout.image_item, container,
	// false);
	//
	// RoundedImageView imageView = (RoundedImageView) itemView
	// .findViewById(R.id.parentImage);
	// imageView.setImageResource(mResources[position]);
	//
	// container.addView(itemView);
	//
	// return itemView;
	// }
	//
	// @Override
	// public void destroyItem(ViewGroup container, int position, Object object)
	// {
	// container.removeView((LinearLayout) object);
	// }

}
