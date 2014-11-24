package com.strata.deliveryapp.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mobeta.android.dslv.DragSortListView;
import com.strata.deliveryapp.DBHelper;
import com.strata.deliveryapp.R;
import com.strata.deliveryapp.SharedValue;
import com.strata.deliveryapp.arrays.Member_Order;
import com.strata.deliveryapp.tabs.TabLayoutOrderActivity;




@SuppressLint("NewApi")
public class FragmentMorning extends ListFragment{
	String data_retrieve_date = "12-12-12";
	String numb;
	String auth;
	JSONArray list;
	private DBHelper mydb;
	String[][] mera_array = new String[50][8];
	Custom_Adapter adapter;
	DragSortListView lv;
	List<Member_Order> orderlist;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the list fragment layout 
    	View rootView = inflater.inflate(R.layout.hetero_main, container, false);
    	
        return rootView;
    }
	
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		mydb = new DBHelper(getActivity());
		ArrayList<String> db_list = new ArrayList<String>();
		db_list = mydb.getAllData(getArguments().getString("name"));
		lv = (DragSortListView) getListView();
		//setting drop and remove options;
		lv.setDropListener(onDrop);
        lv.setRemoveListener(onRemove);
        
		orderlist = new ArrayList<Member_Order>();
        for (int i = 0; i < db_list.size(); i++) {
        	mera_array[i] = convertStringToArray(db_list.get(i));
	        
	        Member_Order ord = new Member_Order();
	        ord.setAddress(mera_array[i][0]);
	        ord.setBranch(mera_array[i][1]);
	        ord.setMember_name(mera_array[i][2]);
	        ord.setMembership_no(mera_array[i][3]);
	        ord.setLocality(mera_array[i][4]);
	        ord.setCity(mera_array[i][5]);
	        ord.setLphone(mera_array[i][6]);
	        ord.setMphone(mera_array[i][7]);
	        // adding HashList to ArrayList
	        orderlist.add(ord);
        }
        adapter = new Custom_Adapter(getActivity(), orderlist);
        // selecting single ListView item
        setListAdapter(adapter);
        mydb.close();
	}
	
	public void onListItemClick(ListView l, View view, int position, long id) {
		//getting values from selected ListItem
		SharedValue.data().memb_no = orderlist.get(position).getMembership_no();
		SharedValue.data().branch_id = orderlist.get(position).getBranch().split(" - ")[0];
		SharedValue.data().name = orderlist.get(position).getMember_name();
		Intent in = new Intent(getActivity(),TabLayoutOrderActivity.class);
		in.putExtra("ADDRESS",orderlist.get(position).getAddress());
		in.putExtra("BRANCH",orderlist.get(position).getBranch());
		in.putExtra("NAME",orderlist.get(position).getMember_name());
  		in.putExtra("MEMBERSHIP_NO",orderlist.get(position).getMembership_no());
  		in.putExtra("LOCALITY",orderlist.get(position).getLocality());
  		in.putExtra("CITY",orderlist.get(position).getCity());
  		in.putExtra("LPHONE",orderlist.get(position).getLphone());
  		in.putExtra("MPHONE",orderlist.get(position).getMphone());
  		startActivity(in);
	}
	
	public static String strSeparator = "__,__";
	public static String[] convertStringToArray(String str){
	    String[] arr = str.split(strSeparator);
	    return arr;
	}
	
	private DragSortListView.DropListener onDrop =
        new DragSortListView.DropListener() {
            @Override
            public void drop(int from, int to) {
            	Member_Order item = adapter.getItem(from);
            	
                adapter.remove(item);
                adapter.insert(item, to);
            }
        };
    private DragSortListView.RemoveListener onRemove = 
        new DragSortListView.RemoveListener() {
            @Override
            public void remove(int which) {
                adapter.remove(adapter.getItem(which));
            }
        };
        
    private class ViewHolder {
        public TextView me_name;
        public TextView me_addr;
        public TextView me_no;
        public TextView me_branch;
      }
//--------------------------------------        
    public class Custom_Adapter extends ArrayAdapter<Member_Order> {
    	private final List<Member_Order> memOrder;
    	Context context;
    	View rowView;
    	public Custom_Adapter(Activity context, List<Member_Order> memOrder){
    		super(context, R.layout.member_list_item,R.id.member_name, memOrder);
    		this.memOrder = memOrder;
    		this.context = context;
    	}
    	@Override
    	public View getView(int position, View view, ViewGroup parent) {
    		/*
    		 * LayoutInflater inflater = context.getLayoutInflater(); View rowView =
    		 * inflater.inflate(R.layout.list_item, null, true);
    		 */
    		View v = super.getView(position, view, parent);
    		if (v != view && v != null) {
	          ViewHolder holder = new ViewHolder();

	          holder.me_name = (TextView) v.findViewById(R.id.member_name);
	          holder.me_addr = (TextView) v.findViewById(R.id.member_address);
	          holder.me_no = (TextView) v.findViewById(R.id.membership_no);
	          holder.me_branch = (TextView) v.findViewById(R.id.member_branch);

	          v.setTag(holder);
	        }
    		
    		ViewHolder holder = (ViewHolder) v.getTag();

            holder.me_name.setText(getItem(position).member_name);
            holder.me_addr.setText(memOrder.get(position).getAddress());
            holder.me_no.setText(memOrder.get(position).getMembership_no());
            holder.me_branch.setText(memOrder.get(position).getBranch());

            return v;
    		/*if (view == null){
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

    		return rowView;*/
    	}
    	
    	public void remove(int position){
    	    
    	}
    }
    

}
