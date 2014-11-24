package com.strata.deliveryapp.adapters;

import com.strata.deliveryapp.fragment.FragmentMorning;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {
//	public TabsPagerAdapter(android.support.v4.app.Fragment fragment){
//	    super(fragment.getChildFragmentManager());
//	}
	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {
		Bundle bundle=new Bundle();
		switch (index) {
		case 0:
			FragmentMorning frag_morning = new FragmentMorning();
			bundle.putString("name", "10000"); //morning
			frag_morning.setArguments(bundle);
			return frag_morning;
		case 1:
			//return new FragmentAfternoon();
			FragmentMorning frag_afternoon = new FragmentMorning();
			bundle.putString("name", "10001"); //morning
			frag_afternoon.setArguments(bundle);
			return frag_afternoon;
		case 2:
			//return new FragmentEvening();
			FragmentMorning frag_evening = new FragmentMorning();
			bundle.putString("name", "10002"); //morning
			frag_evening.setArguments(bundle);
			return frag_evening;
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}

