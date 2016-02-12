package com.picit.datamodel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class DownloadImageTask extends AsyncTask<String, Integer, Bitmap> {

	@Override
	protected Bitmap doInBackground(String... params) {
		String scriptUrl = params[0];
		Log.i("ScriptUrl", scriptUrl);
		try {
			URL url1 = new URL(scriptUrl);
			HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
			connection.setDoInput(true);
			connection.connect();
			//InputStream input = connection.getInputStream();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(connection.getInputStream()));
			StringBuilder imageUrl = new StringBuilder();
			String line = null;
			// Read Server Response
			while ((line = reader.readLine()) != null) {
				imageUrl.append(line);
				break;
			}
			connection.disconnect();
			Log.i("Connection1 R: " , imageUrl.toString());
		
			URL url = new URL(imageUrl.toString());
			HttpURLConnection connection2 = (HttpURLConnection) url.openConnection();
			connection2.setDoInput(true);
			connection2.connect();
			InputStream input = connection2.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
