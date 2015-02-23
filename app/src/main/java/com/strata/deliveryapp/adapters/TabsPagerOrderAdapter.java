package com.strata.deliveryapp.adapters;

import com.strata.deliveryapp.fragment.FragmentDelivery;
import com.strata.deliveryapp.fragment.FragmentPickup;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerOrderAdapter extends FragmentPagerAdapter {
	String user_membership_no;
//	public TabsPagerAdapter(android.support.v4.app.Fragment fragment){
//	    super(fragment.getChildFragmentManager());
//	}
	public TabsPagerOrderAdapter(FragmentManager fm,String user_membership_no) {
		super(fm);
		this.user_membership_no = user_membership_no;
	}

	@Override
	public Fragment getItem(int index) {
		//FragmentMorning frag_morning = new FragmentMorning();
		//Bundle bundle=new Bundle();
		//bundle.putString("user_membership_no", user_membership_no);
		switch (index) {
		case 0:
//			FragmentPickup frag_pickup = new FragmentPickup(); //morning
//			frag_pickup.setArguments(bundle);
//			return frag_pickup;
			return new FragmentPickup(); 
		case 1:
//			FragmentDelivery frag_delivery = new FragmentDelivery(); //morning
//			frag_delivery.setArguments(bundle);
//			return frag_delivery;
			return new FragmentDelivery();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 2;
	}

}

