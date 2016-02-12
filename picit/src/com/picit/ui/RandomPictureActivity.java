package com.picit.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;

import com.picit.datamodel.DownloadImageTask;

public class RandomPictureActivity extends Activity {

	private RatingBar ratingBar;
	private ImageView imageView;
	private static final int PICTURE_QUALITY = 100;
	private static final int PICTURE_OUTPUT_X = 300;
	private static final int PICTURE_OUTPUT_Y = 300;
	private String userName;

	// Generate URL for requesting a pic
	private void getImage() {
		String scriptUrl = "http://www.gameoverblog.com/requestPicture.php";
		new ImageGetter().execute(scriptUrl);
	}

	// Camera Feature
	static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final int PIC_CROP = 2;
	private Uri picUri;

	// Camera Intent
	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_IMAGE_CAPTURE) {
				// get the Uri for the captured image
				picUri = data.getData();
				// carry out the crop operation
				performCrop();
			} else if (requestCode == PIC_CROP) {
				
				//Get the file path of the cropped image
				String filePath = Environment.getExternalStorageDirectory() + "/temporary_holder.jpg";

				// get the returned data
				Bundle extras = data.getExtras();
				// get the cropped bitmap
				Bitmap thePic = extras.getParcelable("data");
				
				//Encode the data in a byteArray
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				thePic.compress(Bitmap.CompressFormat.JPEG, PICTURE_QUALITY, stream);
				byte[] byteArray = stream.toByteArray();
				
				//Start the upload to server activity and send in extras
				Intent intent = new Intent(this, UploadImageActivity.class);
				intent.putExtra("image",byteArray);
				intent.putExtra("URI", filePath);
				intent.putExtra("username", this.userName);
				startActivityForResult(intent, 0);

			}
		}
	}

	private void performCrop() {
		try {
			// call the standard crop action intent (the user device may not
			// support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			cropIntent.setDataAndType(picUri, "image/*");
			// set crop properties
			cropIntent.putExtra("crop", "true");
			// indicate aspect of desired crop
			cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			// indicate output X and Y
			cropIntent.putExtra("outputX", PICTURE_OUTPUT_X);
			cropIntent.putExtra("outputY", PICTURE_OUTPUT_Y);
			// retrieve data on return
			cropIntent.putExtra("return-data", true);

			//Store the cropped image in the phone temporally
			File f = new File(Environment.getExternalStorageDirectory(), "/temporary_holder.jpg");
			try {
				f.createNewFile();
			} catch (IOException ex) {
				Log.e("io", ex.getMessage());
			}

			picUri = Uri.fromFile(f);

			cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);

			// start the activity - we handle returning in onActivityResult
			startActivityForResult(cropIntent, PIC_CROP);
		} catch (ActivityNotFoundException anfe) {
			// display an error message
			String errorMessage = "Whoops - your device doesn't support the crop action!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_random_picture);
		
		ActionBar actionBar = getActionBar();
		actionBar.show();
		this.userName = getIntent().getStringExtra("username");
		Log.i("Username : ", this.userName);
		
		//As soon as the activity starts, get an image.
		getImage();
		
		// Getting the bar and image
		ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		// ratingBar.setOnClickListener(new RatingBarClickListener() );
		ratingBar.setOnRatingBarChangeListener(new RatingBarListener());
		imageView = (ImageView) findViewById(R.id.image);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_camera: {
			dispatchTakePictureIntent();
			return true;
		} case R.id.action_logout: {
			logout();
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	private void logout() {
		SharedPreferences prefs = getSharedPreferences(LoginActivity.USER_DETAILS_KEY, MODE_PRIVATE);
		Editor edit = prefs.edit();
		edit.remove(LoginActivity.USER_LOGGED_IN_KEY);
		edit.remove(LoginActivity.USER_USERNAME_KEY);
		edit.commit();
		Intent intent = new Intent(this, LoginActivity.class);
		this.startActivity(intent);
		finish();
	}

	protected class RatingBarListener implements OnRatingBarChangeListener {

		@Override
		public void onRatingChanged(RatingBar ratingBar, float rateNum,
				boolean fromUser) {
			ratingBar.setEnabled(false);
			ratingBar.setRating(0f);
			if (fromUser)
				getImage();
		}
	}

	protected class ImageGetter extends DownloadImageTask {
		@Override
		protected void onPostExecute(Bitmap bm) {
			if (bm != null) {
				Toast.makeText(getBaseContext(), "Lol good one",
						Toast.LENGTH_SHORT).show();
				imageView.setImageBitmap(bm);
				ratingBar.setEnabled(true);
			} else {
				Toast.makeText(getBaseContext(), "LOL good one",
						Toast.LENGTH_SHORT).show();
			}
		}
	} 

}
