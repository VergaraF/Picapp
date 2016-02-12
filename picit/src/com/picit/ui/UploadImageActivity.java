package com.picit.ui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.picit.datamodel.HttpFileUpload;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UploadImageActivity extends Activity {

	TextView messageText;
	Thread t1;
	private String captionText;
	EditText caption;
	Button uploadButton;
	Bitmap image;
	ImageView imageView;
	String uri;
	int serverResponseCode = 0;
	ProgressDialog dialog = null;

	String upLoadServerUri = null;

	/********** File Path *************/
	private String uploadFilePath;
	private String userName;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_to_server);

		uploadFilePath = getIntent().getExtras().getString("URI");
		userName = getIntent().getStringExtra("username");

		uploadButton = (Button) findViewById(R.id.uploadButton);
		//messageText = (TextView) findViewById(R.id.messageText);
		imageView = (ImageView) findViewById(R.id.uploadImage);
		caption = (EditText) findViewById(R.id.captionText);

		byte[] byteArray = getIntent().getByteArrayExtra("image");
		image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
		imageView.setImageBitmap(image);

		//messageText.setText("Uploading file path :- '" + uploadFilePath + "'");

		/************* Php script path ***************************/
		upLoadServerUri = "http://www.gameoverblog.com/upload.php";

		uploadButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				captionText = caption.getText().toString();
				dialog = ProgressDialog.show(UploadImageActivity.this, "",
						"Uploading file...", true);

				t1 = new Thread(new Runnable() {
					public void run() {
						runOnUiThread(new Runnable() {
							public void run() {
								
							}
						});
						UploadFile();
					}
				});
				t1.start();

			}
		});
	}

	public void UploadFile() {
		try {
			// Set your file path here
			FileInputStream fstrm = new FileInputStream(uploadFilePath);

			// Set your server page url (and the file user/caption)
			HttpFileUpload hfu = new HttpFileUpload(upLoadServerUri, this.userName, captionText);

			hfu.Send_Now(fstrm);

			dialog.cancel();

			runOnUiThread(new Runnable() {
				public void run() {
					t1 = null;
					Toast.makeText(UploadImageActivity.this,
							"File Upload Complete.", Toast.LENGTH_SHORT).show();

				}
			});
			Thread.sleep(1000);

		} catch (FileNotFoundException e) {
			// Error: File not found
		} catch (InterruptedException e) {

		}
		Intent intent = new Intent(this, RandomPictureActivity.class);
		intent.putExtra("username", userName);
		startActivity(intent);
	}
}