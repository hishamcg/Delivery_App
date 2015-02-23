package com.strata.deliveryapp.tabs;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.strata.deliveryapp.R;
import com.strata.deliveryapp.adapters.TabsPagerOrderAdapter;

@SuppressLint("NewApi")
public class TabLayoutOrderActivity extends FragmentActivity implements ActionBar.TabListener{
	private ViewPager viewPager;
	private TabsPagerOrderAdapter mAdapter;
	private ActionBar actionBar;
	String user_membership_no;
	private String[] tabs = {"Pickup Order","Delivery Order"};
	
	protected void onCreate (Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_order_view_layout);
		TextView member_name = (TextView) findViewById(R.id.member_name);
	    TextView membership_no = (TextView) findViewById(R.id.membership_no);
	    TextView branch = (TextView) findViewById(R.id.member_branch);
	    TextView address = (TextView) findViewById(R.id.member_address);
	    Button bt_lphone = (Button) findViewById(R.id.lphone);
	    Button bt_mphone = (Button) findViewById(R.id.mphone);
	    
	    final ImageView iv = (ImageView) findViewById(R.id.arrow_image);
	    final RelativeLayout Rl_bt = (RelativeLayout) findViewById(R.id.member_detail);
	    
	    final Intent in = getIntent();
		user_membership_no = in.getStringExtra("MEMBERSHIP_NO");
		address.setText(in.getStringExtra("ADDRESS"));
		branch.setText(in.getStringExtra("BRANCH"));
		member_name.setText(in.getStringExtra("NAME"));
		membership_no.setText(user_membership_no);
		setTitle(in.getStringExtra("NAME"));
		
		if(in.getStringExtra("LPHONE").isEmpty()){
			bt_lphone.setVisibility(View.GONE);
		}
		if(in.getStringExtra("MPHONE").isEmpty()){
			bt_mphone.setVisibility(View.GONE);
		}
		
	    Rl_bt.setOnClickListener(new Button.OnClickListener(){
	    	@Override
	    	public void onClick(View v){
	    		LinearLayout data_rl = (LinearLayout) findViewById(R.id.LL_member_detail);
	    		if (data_rl.getVisibility() == View.GONE){
	    			data_rl.setVisibility(View.VISIBLE);
	    			iv.setBackgroundResource(R.drawable.arrow_up);
	    		}else{
	    			data_rl.setVisibility(View.GONE);
	    			iv.setBackgroundResource(R.drawable.arrow_down);
	    		}
	    	}
	    });
	    
	    bt_lphone.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				String phoneNumber = in.getStringExtra("LPHONE");
			    Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNumber));
			    call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			    startActivity(call);
			}
	    	
	    });
	    
	    bt_mphone.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				String phoneNumber = in.getStringExtra("MPHONE");
			    Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNumber));
			    call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			    startActivity(call);
			}
	    	
	    });
	    
		viewPager = (ViewPager) findViewById(R.id.pager_order);
		actionBar = getActionBar();
		mAdapter = new TabsPagerOrderAdapter(getSupportFragmentManager(),user_membership_no);
		viewPager.setAdapter(mAdapter);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}
		
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}
		
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
		
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(tab.getPosition());
		viewPager.setOffscreenPageLimit(1);
		
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

}
