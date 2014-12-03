package com.example.costercab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.graphics.YuvImage;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	TextView username;
	TextView password;
	TextView result;
	String password1;
	String username1;
	EditText etusername;
	EditText etpassword;
	Button login;
	ProgressDialog prgDialog;
	InputStream content = null;
	protected String tag = "PostActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		result = (TextView) findViewById(R.id.tv_success);
		username = (TextView) findViewById(R.id.tv_username);
		password = (TextView) findViewById(R.id.tv_password);
		etusername = (EditText) findViewById(R.id.et_username);
		etpassword = (EditText) findViewById(R.id.et_password);
		login = (Button) findViewById(R.id.bt_login);
		prgDialog = new ProgressDialog(this);

		prgDialog.setMessage("Please wait...");

		prgDialog.setCancelable(false);
		login.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				String username1 = etusername.getText().toString();
				String password1 = etpassword.getText().toString();
				Toast.makeText(getBaseContext(), username1 + password1, 4000);
				String myUrl = "http://192.168.1.101:8080/CosterApi/rest/login/validateLoginGet?userName="
						+ username1 + "&password=" + password1;
				try {
					MyAsyncTask asyncTask = new MyAsyncTask();
					asyncTask.doInBackground(myUrl);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public void login(String u) throws JSONException {

		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(u);
		// Toast.makeText(getBaseContext(), u, 3000).show();
		try {

			HttpResponse response = client.execute(get);
			
			content = response.getEntity().getContent();
	
			
			
			
			JSONObject json = new JSONObject(convertStreamToString(content));

			/****
			 * 
			 * {"timeBand":120,"sysParam":{"bookingWinStartTimeslot":2,"cancellationDur":20,"createdOn":{"date":14,"day":1,"hours":0,"minutes":0,"month":9,"nanos":0,"seconds":0,"time":1381689000000,"timezoneOffset":-330,"year":113},"defaultAdminEmail":"admin@yahoo.com","distanceForMatchingBooking":0.25,"lastModifiedOn":{"date":13,"day":3,"hours":11,"minutes":16,"month":10,"nanos":0,"seconds":36,"time":1384321596000,"timezoneOffset":-330,"year":113},"maxTaxiSharing":6,"minTaxiSharing":1,"settingsId":1},"result":"8"}
			 * 
			 * 
			 */
			String str_value = json .getString("result");
			
			Log.i("***********************",str_value);
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
			Log.e(tag, "exception is here+", ex);
		} catch (ClientProtocolException ex) {
			ex.printStackTrace();
			Log.e(tag, "exception is here+", ex);
		} catch (IOException ex) {
			ex.printStackTrace();
			Log.e(tag, "exception is here+", ex);

		}

	}

	private String convertStreamToString(InputStream is) {
		// TODO Auto-generated method stub
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	private class MyAsyncTask extends AsyncTask<String, Integer, Double> {

		@Override
		protected Double doInBackground(String... params) {

			try {
				login(params[0]);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

	}
}
