import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

//HTTP request libraries
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

//JSON libraries
import org.json.JSONArray;
import org.json.JSONObject;

class plantIdentify {

    //Function to find scientific name of the plant species based on images submitted. Returns latin name of the plant species.
    static String findScientificName() {

        //Set variable for scientific name as N/A in case plant recognition fails.
        String scientificName = "N/A";

        //Images to be given to API. Mind the naming.
        File image1 = new File("img/1.jpg");
        File image2 = new File("img/2.jpg");

        //Create HTTPEntity for API request.
        HttpEntity entity = MultipartEntityBuilder.create()
            .addPart("images", new FileBody(image1)).addTextBody("organs", "auto")
            .addPart("images", new FileBody(image2)).addTextBody("organs", "auto")
            .build();
        
        //Prepare to POST HTTP request.
        HttpPost request = new HttpPost("https://my-api.plantnet.org/v2/identify/" + "all" + "?api-key=2b10LhD3grOwCbkvSKtnGw58s");
        request.setEntity(entity);
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response;

        //Try to get a response from API.
        //Go through JSON response to find first (best matching) result and get scientific name from it.
        try {
            response = client.execute(request);
            String jsonString = EntityUtils.toString(response.getEntity());
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray results = jsonObj.getJSONArray("results");
            JSONObject firstResult = results.getJSONObject(0);
            JSONObject species = firstResult.getJSONObject("species");
            scientificName = species.getString("scientificNameWithoutAuthor");
        } catch (IOException e) { //In case something goes wrong. TODO: better exception handling?
            e.printStackTrace();
        }

        return scientificName;
    }

    static ArrayList<String> findEnvConditions(String scientificName) {
        ArrayList<String> envConditions = new ArrayList<>();

        return envConditions;
    }
    public static void main(String[] args) {
        System.out.println(findScientificName());
        System.out.println(findEnvConditions(findScientificName()));
    }
}