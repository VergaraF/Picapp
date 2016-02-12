package com.picit.ui;

import com.picit.datamodel.SigninTask;
import com.picit.utils.RegexUtil;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {

	public static final String USER_DETAILS_KEY = "com.picit.datamodek.USER_DETAILS_KEY";
	public static final String USER_LOGGED_IN_KEY = "com.picit.datamodek.USER_LOGGED_IN_KEY";
	public static final String USER_USERNAME_KEY = "com.picit.datamodek.USER_USERNAME_KEY";

	private EditText usernameField, passwordField;
	private TextView errorText;
	private String username, password;
	private Button loginButton;
	private Button signupButton;

	/**
	 * to read and write to preferences(login details)
	 */
	private SharedPreferences userDetails;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		
		// Auto login user
		userDetails = getSharedPreferences(USER_DETAILS_KEY, MODE_PRIVATE);
		username = userDetails.getString(USER_USERNAME_KEY, null);
		if (userDetails.getBoolean(USER_LOGGED_IN_KEY, false)
				&& username != null) {
			Intent intent = new Intent(this, RandomPictureActivity.class);
			intent.putExtra("username", this.username);
			this.startActivity(intent);
			// Stop the previous activity
			finish();
		} else {
			usernameField = (EditText) findViewById(R.id.txtUsername);
			passwordField = (EditText) findViewById(R.id.txtPassword);
			errorText = (TextView) findViewById(R.id.txtLoginError);
			loginButton = (Button) findViewById(R.id.btnLogin);
			signupButton = (Button) findViewById(R.id.btnSignUp);
			loginButton.setOnClickListener(new ButtonListener());
			signupButton.setOnClickListener(new ButtonListener());
		}
	}

	public void login() {
		username = usernameField.getText().toString().trim();
		password = passwordField.getText().toString().trim();
		if (username.isEmpty() || password.isEmpty()) {
			errorText.setText(R.string.error_login1);
		} else if (!RegexUtil.isValidUsername(username)
				|| !RegexUtil.isValidPassword(password)) {
			errorText.setText(R.string.error_login2);
		} else {
			// Clear field
			errorText.setText("");
			SigninTask signinHandler = new SigninTask(this, SigninTask.SIGN_IN);
			signinHandler.execute(username, password);
		}

	}

	public void signUp() {
		Intent intent = new Intent(this, SignUpActivity.class);
		startActivity(intent);
	}

	private class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnLogin: {
				login();
				break;
			}
			case R.id.btnSignUp: {
				signUp();
				break;
			}

			default:
				break;
			}

		}

	}
}