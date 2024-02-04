package ie.dcu.potpal;

import android.util.Log;

import okhttp3.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

class PlantIdentify {

	static JSONObject findPlant(File imageFile) {
		JSONObject jsonObj = new JSONObject();

		// Log the image file path for debugging
		Log.d("PlantIdentify", "Image File Path: " + imageFile.getAbsolutePath());

		// Create a MultipartBody for API request.
		RequestBody requestBody = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("images", imageFile.getName(), RequestBody.create(MediaType.parse("image/jpeg"), imageFile))
				.addFormDataPart("organs", "auto")
				.build();

		// Prepare to POST HTTP request.
		Request request = new Request.Builder()
				.url("https://my-api.plantnet.org/v2/identify/all?api-key=2b10iHt45K2DhpclhX1oGOy8u")
				.post(requestBody)
				.build();

		OkHttpClient client = new OkHttpClient();

		// Try to get a response from API.
		try (Response response = client.newCall(request).execute()) {
			if (response.isSuccessful()) {
				String jsonString = response.body().string();
				jsonObj = new JSONObject(jsonString);
			} else {
				Log.e("PlantIdentify", "Failed to get response from the API. Response Code: " + response.code());
			}
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}

		return jsonObj;
	}

}