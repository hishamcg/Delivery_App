package com.strata.deliveryapp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.strata.deliveryapp.signup.SigninActivity;
import com.strata.deliveryapp.sort.ArbItemSizeDSLV;
import com.strata.deliveryapp.tabs.TabLayoutActivity;


public class MainActivity extends FragmentActivity {
	
	int current_date;
    private Button fetchData;
    Context context;
    //private int year;
    //private int month;
    //private int day;
    String data_retrieve_date = "11-11-11";
    static final int DATE_PICKER_ID = 1111;
    JSONArray list = null;
    private JSONParse json_parse_mor;
    private JSONParse json_parse_aft;
    private JSONParse json_parse_eve;
	ListView lview;
	String auth;
	String numb;
	private DBHelper mydb = new DBHelper(this);
	int jumpTime = 10;
	ProgressBar mProgress;
	Button button;
	//String slot = "10000";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		SharedPreferences pref  = getSharedPreferences("DELIVERY",Context.MODE_PRIVATE);
		numb = pref.getString("NUMBER", "");
		auth = pref.getString("AUTH_TOKEN", "");
		int last_data_fetch = pref.getInt("DATA_FETCH_DATE", 0);
		if (numb != null && numb != ""){
	        fetchData = (Button) findViewById(R.id.fetch_data);
	        mProgress = (ProgressBar) findViewById(R.id.progress_bar);
	        button = (Button) findViewById(R.id.button1);
	        // Get current date by calender
	         
	        final Calendar c = Calendar.getInstance();
			current_date = c.get(Calendar.WEEK_OF_YEAR)*7+c.get(Calendar.DATE);
			if (last_data_fetch == current_date){
				Intent in = new Intent(getApplicationContext(),TabLayoutActivity.class);
			    startActivity(in);
			    finish();
			}else{
		        fetchData.setOnClickListener(new Button.OnClickListener(){
					@Override
					public void onClick(View arg0) {
					    mProgress.setVisibility(View.VISIBLE);
					    mProgress.setProgress(jumpTime);
					    
				        try {
				        	int year  = c.get(Calendar.YEAR);
					        int month = c.get(Calendar.MONTH)+1;
					        int day   = c.get(Calendar.DAY_OF_MONTH);
		
					        data_retrieve_date = day+"-"+month+"-"+year;
				  			data_retrieve_date = URLEncoder.encode(data_retrieve_date, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
				  		
						mydb.deleteAllData(); // delete all data from database
						
						json_parse_mor = new JSONParse();
						json_parse_mor.execute("10000");
						
						json_parse_aft = new JSONParse();
						json_parse_aft.execute("10001");
						
						json_parse_eve = new JSONParse();
						json_parse_eve.execute("10002");
					}
		        });
		        
		        button.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent in = new Intent(getApplicationContext(),ArbItemSizeDSLV.class);
						startActivity(in);
					}
				});
			}
		}else{
			Intent in = new Intent(getApplicationContext(),SigninActivity.class);
		    startActivity(in);
		    finish();
		}
	}
    
    private class JSONParse extends AsyncTask<String,String,JSONObject>{
    	String slot = "10000";
    	protected void onPreExecute(){
  		  
  	  }
  	  protected JSONObject doInBackground(String... args){
  		  slot = args[0];
  		  //String url = "http://"+Config.SERVER_BASE_URL+"/api/delivery_v1/delivery_order.json?date="+data_retrieve_date+"&api_key="+auth+"&phone="+numb+"&slot_id="+args[0];
  		  String url = "http://"+Config.SERVER_BASE_URL+"/api/delivery_v1/delivery_order.json?date=14-12-2011&api_key="+auth+"&phone="+numb+"&slot_id="+args[0];
  		  JSONParser jp = new JSONParser();
  		  JSONObject json = jp.getJSONFromUrl(url);
  		  jumpTime += 20;
		  mProgress.setProgress(jumpTime);
  		  if (isCancelled()){
            	  return null;}
          else
  		  return json;
  	  }
  	  protected void onPostExecute(JSONObject json){
  		if (json != null && !isCancelled()){
  			
  			try {
  				// Getting Array of data
  				list = json.getJSONArray("data");
  				String[] member_data_array = new String[8];
  				String[] data_concat = new String[list.length()];
  				if(list.length() != 0){
  					// looping through All data
  					JSONObject c;
  					for (int i = 0; i < list.length(); i++) {
  						c = list.getJSONObject(i);
  						
  						// Storing each json item in variable
  						member_data_array[0] = c.getString("address");
  						member_data_array[1] = c.getString("branch");
  						member_data_array[2] = c.getString("member_name");
  						member_data_array[3] = c.getString("membership_no");
  						member_data_array[4] = c.getString("locality");
  						member_data_array[5] = c.getString("city");
  						member_data_array[6] = c.getString("lphone");
  						member_data_array[7] = c.getString("mphone");
  						data_concat[i] = convertArrayToString(member_data_array);	
  						
  						
  						JSONArray orders_pickup = c.getJSONArray("orders_pickup");
  						String[] member_pickup_order = new String[5];
  		  				String[] data_pickup_concat = new String[orders_pickup.length()];
  		  				
  		  				JSONArray orders_delivery = c.getJSONArray("orders_delivery");
  		  				String[] member_delivery_order = new String[5];
		  				String[] data_delivery_concat = new String[orders_delivery.length()];
		  				JSONObject cp;
		  				for (int k = 0; k < orders_pickup.length(); k++) {
							cp = orders_pickup.getJSONObject(k).getJSONObject("orders_pickup");
							
							// Storing each json item in variable
							member_pickup_order[0] = cp.getString("order_book_no");
							member_pickup_order[1] = cp.getString("order_date");
							member_pickup_order[2] = cp.getString("order_no");
							member_pickup_order[3] = cp.getString("order_title");
							member_pickup_order[4] = cp.getString("order_type");
							data_pickup_concat[k] = convertArrayToString(member_pickup_order);
						}
		  				mydb.insertAllOrder(data_pickup_concat, member_data_array[3], "P");
		  				JSONObject cd;
						for (int j = 0; j < orders_delivery.length(); j++) {
							cd = orders_delivery.getJSONObject(j).getJSONObject("orders_delivery");
							
							// Storing each json item in variable
							member_delivery_order[0] = cd.getString("order_book_no");
							member_delivery_order[1] = cd.getString("order_date");
							member_delivery_order[2] = cd.getString("order_no");
							member_delivery_order[3] = cd.getString("order_title");
							member_delivery_order[4] = cd.getString("order_type");
							data_delivery_concat[j] = convertArrayToString(member_delivery_order);
						}
						mydb.insertAllOrder(data_delivery_concat, member_data_array[3], "D");	
  					 }
  					mydb.insertAllData(data_concat,slot);
  				}else{
  				}
  			} catch (JSONException e) {
  				e.printStackTrace();
  			}
  			jumpTime += 10;
  			mProgress.setProgress(jumpTime);
  			if(slot.equals("10002")){
  				mydb.close();
  				SharedPreferences pref  = getSharedPreferences("DELIVERY",Context.MODE_PRIVATE);
				SharedPreferences.Editor   editor = pref.edit();
				editor.putInt("DATA_FETCH_DATE", current_date);
				editor.commit();
	  			Intent in = new Intent(getApplicationContext(),TabLayoutActivity.class);
			    startActivity(in);
			    finish();
			}
  		    
  		}
  	  }
    }
    
    public static String strSeparator = "__,__";
  	public static String convertArrayToString(String[] array){
  	    String str = "";
  	    for (int i = 0;i<array.length; i++) {
  	        str = str+array[i];
  	        // Do not append comma at the end of last element
  	        if(i<array.length-1){
  	            str = str+strSeparator;
  	        }
  	    }
  	    return str;
  	}
  	public static String[] convertStringToArray(String str){
  	    String[] arr = str.split(strSeparator);
  	    return arr;
  	}
    
    public void onResume(){
  		super.onResume();
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
