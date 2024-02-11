package ie.dcu.potpal;

import android.util.Log;

import okhttp3.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;

class PlantIdentify {

	static List<List<String>> findPlantNames(){ //(File imageFile) {

		//Prepare to store results.
		List<List<String>> results =  new ArrayList<>();
		List<String> firstResult = new ArrayList<>();
		List<String> secondResult = new ArrayList<>();
		List<String> thirdResult = new ArrayList<>();

		JSONObject jsonObj = new JSONObject();

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
				.url("https://my-api.plantnet.org/v2/identify/all?api-key=2b10AOILklY0vvHmKDYT1MqLye")
				.post(requestBody)
				.build();

		OkHttpClient client = new OkHttpClient();

		// Try to get a response from API.
		try (Response response = client.newCall(request).execute()) {
			if (response.isSuccessful()) {
				String jsonString = response.body().string();
				jsonObj = new JSONObject(jsonString);
				Log.d("PlantIdentify", "JSON: " + jsonObj);
				JSONArray jsonResults = jsonObj.getJSONArray("results");
				JSONObject firstJsonResult = jsonResults.getJSONObject(0);
				JSONObject secondJsonResult = jsonResults.getJSONObject(1);
				JSONObject thirdJsonResult = jsonResults.getJSONObject(2);
				firstResult.add(firstJsonResult.getJSONObject("species").getString("scientificNameWithoutAuthor"));
				secondResult.add(secondJsonResult.getJSONObject("species").getString("scientificNameWithoutAuthor"));
				thirdResult.add(thirdJsonResult.getJSONObject("species").getString("scientificNameWithoutAuthor"));
				JSONArray firstCommonNames = firstJsonResult.getJSONObject("species").getJSONArray("commonNames");
				JSONArray secondCommonNames = secondJsonResult.getJSONObject("species").getJSONArray("commonNames");
				JSONArray thirdCommonNames = thirdJsonResult.getJSONObject("species").getJSONArray("commonNames");
				for(int i=0; i<firstCommonNames.length(); i++) {
					firstResult.add(firstCommonNames.getString(i));
				}
				for(int i=0; i<secondCommonNames.length(); i++) {
					secondResult.add(secondCommonNames.getString(i));
				}
				for(int i=0; i<thirdCommonNames.length(); i++) {
					thirdResult.add(thirdCommonNames.getString(i));
				}
				firstResult.add(firstJsonResult.getJSONArray("images").getJSONObject(0).getJSONObject("url").getString("o"));
				firstResult.add(firstJsonResult.getJSONArray("images").getJSONObject(0).getJSONObject("url").getString("m"));
				firstResult.add(firstJsonResult.getJSONArray("images").getJSONObject(0).getJSONObject("url").getString("s"));
				results.add(firstResult);
				secondResult.add(secondJsonResult.getJSONArray("images").getJSONObject(0).getJSONObject("url").getString("o"));
				secondResult.add(secondJsonResult.getJSONArray("images").getJSONObject(0).getJSONObject("url").getString("m"));
				secondResult.add(secondJsonResult.getJSONArray("images").getJSONObject(0).getJSONObject("url").getString("s"));
				results.add(secondResult);
				thirdResult.add(thirdJsonResult.getJSONArray("images").getJSONObject(0).getJSONObject("url").getString("o"));
				thirdResult.add(thirdJsonResult.getJSONArray("images").getJSONObject(0).getJSONObject("url").getString("m"));
				thirdResult.add(thirdJsonResult.getJSONArray("images").getJSONObject(0).getJSONObject("url").getString("s"));
				results.add(thirdResult);
			} else {
				Log.e("PlantIdentify", "Failed to get response from the API. Response Code: " + response.code());
			}
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}

		return results;
	}

}