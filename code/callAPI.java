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

class callAPI {
    //Images to be given to API. Mind the naming.
    private static final String IMAGE1 = "img/1.jpg";
    private static final String IMAGE2 = "img/2.jpg";

    //API call parameters.
    private static final String PROJECT = "all";
    private static final String URL = "https://my-api.plantnet.org/v2/identify/" + PROJECT + "?api-key=2b10LhD3grOwCbkvSKtnGw58s";

    //Function to find scientific name of the plant species based on images submitted. Returns latin name of the plant species.
    static String findScientificName() {

        //Set variable for scientific name as N/A in case plant recognition fails.
        String scientificName = "N/A";

        File image1 = new File(IMAGE1);
        File image2 = new File(IMAGE2);

        //Create HTTPEntity for API request.
        HttpEntity entity = MultipartEntityBuilder.create()
            .addPart("images", new FileBody(image1)).addTextBody("organs", "auto")
            .addPart("images", new FileBody(image2)).addTextBody("organs", "auto")
            .build();
        
        //Prepare to POST HTTP request.
        HttpPost request = new HttpPost(URL);
        request.setEntity(entity);
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response;

        //Try to get a response from API.
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
    public static void main(String[] args) {
        System.out.println(findScientificName());
    }
}