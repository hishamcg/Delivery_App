package com.strata.deliveryapp.signup;

import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.strata.deliveryapp.Config;
import com.strata.deliveryapp.JSONParser;
import com.strata.deliveryapp.R;

public class SigninActivity extends Activity {
	// JSON Node names
	private static final String SUCCESS = "success";
	String NUMBER;
	private static final String USER_INFO = "info";
	private static final String AUTH_TOKEN = "authentication_token";
	private static final String DELIVERY_PERSON_ID = "delivery_person_id";
	private static final String NAME = "name";
	private String SUCC = "false";
	String phone_no = "0";
	ProgressBar sign_in_progress;
	private JSONParse json_parse;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout);
        TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE); 
        String number = tm.getLine1Number();
        
        final Button my_Button = (Button) findViewById(R.id.button1);
        final TextView no_connection = (TextView) findViewById(R.id.no_connection);
        final TextView hint_signin = (TextView) findViewById(R.id.hint_signin);
        final EditText my_numb = (EditText) findViewById(R.id.editText1);
        sign_in_progress = (ProgressBar) findViewById(R.id.progressBar1);
        sign_in_progress.setVisibility(View.GONE);
        if (number != null){
        my_numb.setText(number.replace("+91", ""));
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
        if (isNetworkAvailable()){
	        my_Button.setOnClickListener(new Button.OnClickListener() {
	        	@SuppressLint("InlinedApi")
				public void onClick(View v){
	        		sign_in_progress.setVisibility(View.VISIBLE);
	        		phone_no = my_numb.getText().toString();
	        		Log.i("phone_no", phone_no);

	        		String url = "http://"+Config.SERVER_BASE_URL+"/api/delivery_v1/sessions.json?phone=" + phone_no;
	        		// Creating JSON Parser instance
	        		json_parse = new JSONParse();
	        		json_parse.execute(url);
	        		
	        	}
	        });
        }
        else {
        	hint_signin.setVisibility(View.GONE);
        	no_connection.setVisibility(View.VISIBLE);
        	Toast.makeText(getApplicationContext(), "NO internet Connection!",
		    Toast.LENGTH_LONG).show();
        }
    }
	
	 private class JSONParse extends AsyncTask<String,String,JSONObject>{
		  protected void onPreExecute(){
			  
		  }
		  protected JSONObject doInBackground(String... args){
			  String url = args[0];
			  JSONParser jp = new JSONParser();
			  JSONObject json = jp.getJSONFromUrl(url);
			  if (isCancelled()){
	          	  return null;}
	          else
				return json;
		  }
		  protected void onPostExecute(JSONObject json){
			  if (json != null){
	        		try { // getting JSON string from URL
	      			  SUCC = json.getString(SUCCESS);
	      			  System.out.println(SUCC);
	      			  if (SUCC.equals("true") ){
	      				  String INFO = json.getString(USER_INFO);
	      				  System.out.println(INFO);
	      				  JSONObject DATA = json.getJSONObject("data");
	      				  final String[] data_list = new String[3];
	      				  data_list[0] = DATA.getString(AUTH_TOKEN);
	      				  data_list[1] = DATA.getString(DELIVERY_PERSON_ID);
	      				  data_list[2] = DATA.getString(NAME);
		            	  Random r = new Random();
		            	  final int pas = r.nextInt(10000 - 1000) + 1000;
		            	  System.out.println(pas);
		            	  //Toast.makeText(getApplicationContext(), "password is : "+pas, Toast.LENGTH_LONG).show();
		            	  //commenting for now.
		            	  String url = "http://staging.justbooksclc.com:8787/api/v1/send_otp.json?phone=" + phone_no+"&otp="+String.valueOf(pas);
		            	  System.out.println("score here"+ phone_no);
		            	  // Creating JSON Parser instance
		            	  JSONParser jParser = new JSONParser();
		      			  jParser.getJSONFromUrl(url);
		      			              			   		      				  //------------------------------
		      				  //comment to bypass authentication via sms
		      				  //Intent checking_auth = new Intent(getApplicationContext(), PageZero.class);
			      			  Intent checking_auth = new Intent(getApplicationContext(), SignInWaitingActivity.class);
		        	          checking_auth.putExtra("pas_rand", String.valueOf(pas));
		        	          checking_auth.putExtra("AUTH_TOKEN", data_list[0]);
		        	          checking_auth.putExtra("NUMBER", phone_no);
		        	          checking_auth.putExtra("DELIVERY_PERSON_ID", data_list[1]);
		        	          checking_auth.putExtra("NAME", data_list[2]);
			        	      startActivity(checking_auth);
			        	      sign_in_progress.setVisibility(View.GONE);
	      			  }else{
	      				  Toast toast = Toast.makeText(getApplicationContext(),"      Login Failed \nPlease Try Again Later",Toast.LENGTH_LONG);
	      				  toast.setGravity(Gravity.TOP, 0, 170);
	      				  toast.show();
	      				  sign_in_progress.setVisibility(View.GONE);
	      			  }
	      				  
	      				  
		      		} catch (JSONException e) {
		      			e.printStackTrace();
		      			Toast toast = Toast.makeText(getApplicationContext(),"      Login Failed \nPlease Try Again Later",Toast.LENGTH_LONG);
	      				toast.setGravity(Gravity.TOP, 0, 170);
	      				toast.show();
	      				sign_in_progress.setVisibility(View.GONE);
		      		}
      		}else{
      			Toast toast = Toast.makeText(getApplicationContext(),"      Login Failed \nPlease Try Again Later",Toast.LENGTH_LONG);
    				toast.setGravity(Gravity.TOP, 0, 170);
    				toast.show();
    				sign_in_progress.setVisibility(View.GONE);
      		}
		  }
	  }
	
	@Override
	public void onResume(){
		super.onResume();
	}
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	@Override
	public void onDestroy() {
	    super.onDestroy();
	}
    
}

