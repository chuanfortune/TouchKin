package com.touchKin.touchkinapp.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.touchKin.touchkinapp.custom.RoundedImageView;
import com.touchKin.touchkinapp.model.ParentListModel;
import com.touchKin.touchkinapp.R;

public class SendTouchParentListAdapter extends BaseAdapter {
	Context mContext;
	LayoutInflater mLayoutInflater;

	List<ParentListModel> parentList;
	int[] mResources = { R.drawable.mom, R.drawable.activity_bg };

	public SendTouchParentListAdapter(Context context,
			List<ParentListModel> parentList) {
		mContext = context;
		this.parentList = parentList;
		mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mResources.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(
					R.layout.add_parent_to_send_touch, null);
		}
		ParentListModel item = parentList.get(position);
		RoundedImageView imageView = (RoundedImageView) convertView
				.findViewById(R.id.parentImageView);
		TextView parentName = (TextView) convertView
				.findViewById(R.id.parentNameTextView);

		imageView.setImageResource(mResources[position]);
		parentName.setText(item.getParentName());
		final CheckBox cb = (CheckBox) convertView.findViewById(R.id.isSelectedCB);
		if (item.getIsSelected()) {
			cb.setChecked(true);
		}
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				cb.setChecked(isChecked);
			}
		});
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
