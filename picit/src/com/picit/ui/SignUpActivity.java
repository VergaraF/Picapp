package com.picit.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.picit.datamodel.SigninTask;
import com.picit.utils.RegexUtil;

public class SignUpActivity extends Activity {

	private EditText usernameField, passwordField, emailField;
	private TextView errorText;
	private String username, password, email;
	private Button signUpButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		usernameField = (EditText) findViewById(R.id.txtUsername);
		passwordField = (EditText) findViewById(R.id.txtPassword);
		emailField = (EditText) findViewById(R.id.txtEmail);
		errorText = (TextView) findViewById(R.id.txtSignUpError);
		signUpButton = (Button) findViewById(R.id.btnSignUp);
		signUpButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				signUp();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}

	public void signUp() {
		username = usernameField.getText().toString();
		password = passwordField.getText().toString();
		email = emailField.getText().toString();
		if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
			errorText.setText(R.string.error_signup1);
		} else if (!RegexUtil.isValidUsername(username) || !RegexUtil.isValidPassword(password) || !RegexUtil.isValidEmail(email)) {
			errorText.setText(R.string.error_signup2);
		} else {
			// Clear field
			errorText.setText("");
			new SigninTask(this, SigninTask.SIGN_UP).execute(username, password, email);	
		}
	}
}
