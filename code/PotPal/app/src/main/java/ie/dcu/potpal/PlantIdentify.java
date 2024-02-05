package ie.dcu.potpal;

import android.util.Log;

import okhttp3.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;

class PlantIdentify {

	static JSONObject findPlantNames(){ //(File imageFile) {
		JSONObject jsonObj = new JSONObject();
		String scientificName = "N/A";

		//File for testing purposes.
		File imageFile = new File("img/1.jpg");

		// Log the image file path for debugging
		Log.d("PlantIdentify", "Image File Path: " + imageFile.getAbsolutePath());

		// Create a MultipartBody for API request.
		//RequestBody requestBody = new MultipartBody.Builder()
		//		.setType(MultipartBody.FORM)
		//		.addFormDataPart("images", imageFile.getName(), RequestBody.create(MediaType.parse("image/jpeg"), imageFile))
		//		.addFormDataPart("organs", "auto")
		//		.build();

		//Create HTTPEntity for API request.
		RequestBody requestBody = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("images", imageFile.getName(), RequestBody.create(MediaType.parse("image/jpeg"), imageFile))
				.addFormDataPart("organs", "auto")
				.build();

		// Prepare to POST HTTP request.
		Request request = new Request.Builder()
				.url("https://my-api.plantnet.org/v2/identify/all?api-key=2b10LhD3grOwCbkvSKtnGw58s")
				.post(requestBody)
				.build();

		OkHttpClient client = new OkHttpClient();

		// Try to get a response from API.
		try (Response response = client.newCall(request).execute()) {
			if (response.isSuccessful()) {
				String jsonString = response.body().string();
				jsonObj = new JSONObject(jsonString);
				Log.d("PlantIdentify", "JSON: " + jsonObj);
				JSONArray results = jsonObj.getJSONArray("results");
				JSONObject firstResult = results.getJSONObject(0);
				JSONObject species = firstResult.getJSONObject("species");
				scientificName = species.getString("scientificNameWithoutAuthor");
				Log.d("PlantIdentify", "Scientific Name: " + scientificName);
			} else {
				Log.e("PlantIdentify", "Failed to get response from the API. Response Code: " + response.code());
			}
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}

		return jsonObj;
	}

}