package com.strata.deliveryapp.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.strata.deliveryapp.Config;
import com.strata.deliveryapp.DBHelper;
import com.strata.deliveryapp.JSONParser;
import com.strata.deliveryapp.R;
import com.strata.deliveryapp.SharedValue;
import com.strata.deliveryapp.arrays.LockedArray;
import com.strata.deliveryapp.arrays.Member_Sub_Order;





@SuppressLint("NewApi")
public class FragmentPickup extends ListFragment{
	private DBHelper mydb;
	List<Member_Sub_Order> member_order_list;
	String auth;
	String numb;
	String memb;
	ArrayList<LockedArray> selected_array = new ArrayList<LockedArray>();
	
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		SharedPreferences pref  = getActivity().getSharedPreferences("DELIVERY",Context.MODE_PRIVATE);
		numb = pref.getString("NUMBER", "");
		auth = pref.getString("AUTH_TOKEN", "");
		
		memb = SharedValue.data().memb_no;
		mydb = new DBHelper(this.getActivity());
		ArrayList<String> mem_data_pickup = new ArrayList<String>();
		mem_data_pickup = mydb.getAllOrder(memb,"P");
		selected_array = mydb.getAllSelected(memb,"P");
		member_order_list = new ArrayList<Member_Sub_Order>();
		for (int i = 0; i < mem_data_pickup.size(); i++) {
			String[] ind_mem_data = convertStringToArray(mem_data_pickup.get(i));
			
			// Storing each json item in variable
			String order_book_no = ind_mem_data[0];
			String order_date = ind_mem_data[1];
			String order_no = ind_mem_data[2];
			String order_title = ind_mem_data[3];
			String order_type = ind_mem_data[4];
	
			Member_Sub_Order memb = new Member_Sub_Order();
			memb.setOrder_book_no(order_book_no);
			memb.setOrder_date(order_date);
			memb.setOrder_no(order_no);
			memb.setOrder_title(order_title);
			memb.setOrder_type(order_type);
			memb.setId(false);
			// adding HashList to ArrayList
			member_order_list.add(memb);
		}
		Member_Sub_Order[] memb_ord = new Member_Sub_Order[member_order_list.size()];
        Member_Custom_Adapter adapter = new Member_Custom_Adapter(getActivity(), member_order_list.toArray(memb_ord));
        // selecting single ListView item
        setListAdapter(adapter);
        mydb.close();
	}
	
	@SuppressWarnings("deprecation")
	public void onListItemClick(ListView l, final View view, final int position, long id) {
		if(!selected_array.get(position).getSelect().equals("true")){
			//getting values from selected ListItem
			final String book_no = member_order_list.get(position).getOrder_book_no();
			final String order_no = member_order_list.get(position).getOrder_no();
			
			AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
	        alert.setTitle("PICKUP");
	        alert.setMessage("Please confirm the book no: "+book_no+" before PICKUP");
	        alert.setButton("Yes", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int which) {
	        	   //String url = "http://"+Config.SERVER_BASE_URL+"/api/v1/orders/pickup.json?api_key="+auth+"&phone="+numb+"&book_no="+book_no+"&membership_no="+memb;
	        	   //httpToPostRequest(url);
	        	   String url = "http://"+Config.SERVER_BASE_URL+"/api/delivery_v1/delivery_order/pickup.json?" +
	        	   		"api_key="+auth+"&phone="+numb+"&book_number="+book_no+"&membership_no="+memb+
	        	   		"&pickup_order_id="+order_no+"&branch_id="+SharedValue.data().branch_id+
	        	   		"&username=delivery_app_"+numb;
	        	   JSONParser jp = new JSONParser();
	     		   JSONObject json = jp.getJSONFromUrl(url);
	     		   
	     		   	boolean Success;
					try {
						Success = json.getBoolean("success");
						JSONObject jerror = json.getJSONObject("errors");
			            if(Success){
			            	selected_array.get(position).setSelect("true");
			        	   ((ImageView) view.findViewById(R.id.tick_image)).setVisibility(View.VISIBLE);
			        	   ((LinearLayout) view.findViewById(R.id.main_LL)).setBackgroundColor(0xffEEEEEE);
			        	   
			        	   DBHelper mydb = new DBHelper(getActivity());
			        	   mydb.setNewSelected(selected_array.get(position).getId());
			        	   mydb.close();
			            }else if(!jerror.isNull("book_number")){
			            	Toast.makeText(getActivity(), jerror.getString("book_number"),Toast.LENGTH_LONG).show();
			            }else{
			            	Toast.makeText(getActivity(),"Sorry something went wrong",Toast.LENGTH_LONG).show();
			            }
					} catch (JSONException e) {
						Toast.makeText(getActivity(),"Sorry something went wrong",Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
	           }
	        });
	        alert.setButton2("No",new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	                dialog.cancel();
	            }
	        });
	        // Set the Icon for the Dialog
	        alert.setIcon(R.drawable.ic_launcher);
	        alert.show();
		}
	}
	
	public static String strSeparator = "__,__";
	public static String[] convertStringToArray(String str){
	    String[] arr = str.split(strSeparator);
	    return arr;
	}
	
	public class Member_Custom_Adapter extends ArrayAdapter<Member_Sub_Order>{
		private final Member_Sub_Order[] memSubOrder;
		Context context;
		public Member_Custom_Adapter(Activity context, Member_Sub_Order[] memSubOrder){
			super(context, R.layout.member_order_list_item, memSubOrder);
			this.memSubOrder = memSubOrder;
			this.context = context;
		}
		
		@SuppressLint({ "ViewHolder", "ResourceAsColor" })
		@Override
		public View getView(int position, View view, ViewGroup parent) {
			/*
			 * LayoutInflater inflater = context.getLayoutInflater(); View rowView =
			 * inflater.inflate(R.layout.list_item, null, true);
			 */
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.member_order_list_item, parent, false);
			TextView book_no = (TextView) rowView.findViewById(R.id.order_book_no);
			TextView date = (TextView) rowView.findViewById(R.id.order_date);
			TextView order_no = (TextView) rowView.findViewById(R.id.order_no);
			TextView title = (TextView) rowView.findViewById(R.id.order_title);
			TextView type = (TextView) rowView.findViewById(R.id.order_type);
			ImageView tick_image = (ImageView) rowView.findViewById(R.id.tick_image);
			LinearLayout MainLL = (LinearLayout) rowView.findViewById(R.id.main_LL);
			
			Member_Sub_Order order = memSubOrder[position];
			book_no.setText(order.getOrder_book_no());
			date.setText(order.getOrder_date());
			order_no.setText(order.getOrder_no());
			title.setText(order.getOrder_title());
			type.setText(order.getOrder_type());
			
			if (order.getOrder_type().equals("PickupOrder")){
				//MainLL.setBackgroundColor(R.color.light_violet);0xff000000
				MainLL.setBackgroundColor(0xffF3CDCD);
			}
			
			if (selected_array.get(position).getSelect().equals("true")){
				tick_image.setVisibility(View.VISIBLE);
				MainLL.setBackgroundColor(0xffEEEEEE);
			}else{
				tick_image.setVisibility(View.GONE);
			}

			return rowView;
		}
	}
}
