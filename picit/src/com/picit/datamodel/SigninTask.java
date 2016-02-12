package com.picit.datamodel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.picit.ui.LoginActivity;
import com.picit.ui.RandomPictureActivity;

public class SigninTask extends AsyncTask<String, Void, String> {

	private Context context;
	// ACTION = 1 for sign Up or 0 for sign in
	private int action;
	private String serverResponse;
	public final static int SIGN_IN = 0;
	public final static int SIGN_UP = 1;
	private String username;
	private SharedPreferences userDetails;

	public SigninTask(Context context, int flag) {
		this.context = context;
		action = flag;
	}

	protected void onPreExecute() {
		userDetails = context.getSharedPreferences(LoginActivity.USER_DETAILS_KEY, Context.MODE_PRIVATE);
	}

	// Should change this method to not allow PHP injection and stuff
	@Override
	protected String doInBackground(String... arg0) {
		this.username = (String) arg0[0];
		if (action == SIGN_IN) {

			try {
				String username = (String) arg0[0];
				String password = (String) arg0[1];
				String link = "http://gameoverblog.com/login.php";
				String data = URLEncoder.encode("username", "UTF-8") + "="
						+ URLEncoder.encode(username, "UTF-8");
				data += "&" + URLEncoder.encode("password", "UTF-8") + "="
						+ URLEncoder.encode(password, "UTF-8");
				URL url = new URL(link);
				URLConnection conn = url.openConnection();
				conn.setDoOutput(true);
				OutputStreamWriter wr = new OutputStreamWriter(
						conn.getOutputStream());
				wr.write(data);
				wr.flush();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line = null;
				// Read Server Response
				while ((line = reader.readLine()) != null) {
					sb.append(line);
					break;
				}

				Log.i("SIGN_IN.Response: ", sb.toString());
				return sb.toString();
			} catch (Exception e) {
				return new String("Exception: " + e.getMessage());
			}
		} else if (action == SIGN_UP) {
			try {
				String username = (String) arg0[0];
				String password = (String) arg0[1];
				String email = (String) arg0[2];
				String link = "http://gameoverblog.com/register.php";

				String data = URLEncoder.encode("username", "UTF-8") + "="
						+ URLEncoder.encode(username, "UTF-8");
				data += "&" + URLEncoder.encode("password", "UTF-8") + "="
						+ URLEncoder.encode(password, "UTF-8");
				data += "&" + URLEncoder.encode("email", "UTF-8") + "="
						+ URLEncoder.encode(email, "UTF-8");

				URL url = new URL(link);
				URLConnection conn = url.openConnection();
				conn.setDoOutput(true);
				OutputStreamWriter wr = new OutputStreamWriter(
						conn.getOutputStream());
				wr.write(data);
				wr.flush();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line = null;
				// Read Server Response
				while ((line = reader.readLine()) != null) {
					sb.append(line);
					break;
				}

				Log.i("SIGN_UP.Response: ", sb.toString());
				return sb.toString();
			} catch (Exception e) {
				return new String("Exception: " + e.getMessage());
			}
		} else {
			Log.i("LOL: ",
					"Something went wrong here. This guy ain't signin' up either signin' in");
			return null;
		}

	}

	public String serverResponse() {
		return serverResponse;
	}

	@Override
	protected void onPostExecute(String result) {
		Editor edit = userDetails.edit();
		// Put the username and boolean keys
		edit.putString(LoginActivity.USER_USERNAME_KEY, username);
		edit.putBoolean(LoginActivity.USER_LOGGED_IN_KEY, true);
		if (this.username.equalsIgnoreCase(result)) {
			Intent intent = new Intent(context, RandomPictureActivity.class);
			intent.putExtra("username", this.username);
			context.startActivity(intent);
			Toast.makeText(context, "Welcome " + this.username,
					Toast.LENGTH_SHORT).show();
		} else if (result.equalsIgnoreCase("Welcome " + this.username)) {
			Intent intent = new Intent(context, LoginActivity.class);
			context.startActivity(intent);
			Toast.makeText(context, "Your account has been created",
					Toast.LENGTH_SHORT).show();
		}
		// Commit preferences changes
		edit.commit();
		// Stop the previous activity
		

	}
}
