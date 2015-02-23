package com.strata.deliveryapp.adapters;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.strata.deliveryapp.R;
import com.strata.deliveryapp.arrays.Member_Order;

public class Custom_Adapter extends ArrayAdapter<Member_Order> {
	private final List<Member_Order> memOrder;
	Context context;
	View rowView;
	public Custom_Adapter(Activity context, List<Member_Order> memOrder){
		super(context, R.layout.member_list_item, memOrder);
		this.memOrder = memOrder;
		this.context = context;
	}
	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		/*
		 * LayoutInflater inflater = context.getLayoutInflater(); View rowView =
		 * inflater.inflate(R.layout.list_item, null, true);
		 */
		
		if (view == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.member_list_item, parent, false);
		}
		TextView title = (TextView) rowView.findViewById(R.id.member_name);
		TextView author = (TextView) rowView.findViewById(R.id.membership_no);
		TextView category = (TextView) rowView.findViewById(R.id.member_branch);
		TextView publisher = (TextView) rowView.findViewById(R.id.member_address);

		Member_Order order = memOrder.get(position);
		title.setText(order.getMember_name());
		author.setText(order.getMembership_no());
		category.setText(order.getBranch());
		publisher.setText(order.getAddress());

		return rowView;
	}
	
	public void remove(int position){
	    
	}
}
