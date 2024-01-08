import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

class callAPI {
private static final String IMAGE1 = "img/1.jpg";
private static final String IMAGE2 = "img/2.jpg";

private static final String PROJECT = "all";
private static final String URL = "https://my-api.plantnet.org/v2/identify/" + PROJECT + "?api-key=2b10LhD3grOwCbkvSKtnGw58s";

    public static void main(String[] args) {
        File file1 = new File(IMAGE1);
        File file2 = new File(IMAGE2);
        
        HttpEntity entity = MultipartEntityBuilder.create()
            .addPart("images", new FileBody(file1)).addTextBody("organs", "auto")
            .addPart("images", new FileBody(file2)).addTextBody("organs", "auto")
            .build();
        
        HttpPost request = new HttpPost(URL);
        request.setEntity(entity);
        
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response;
        try {
            response = client.execute(request);
            String jsonString = EntityUtils.toString(response.getEntity());
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray results = jsonObj.getJSONArray("results");
            JSONObject firstResult = results.getJSONObject(0);
            JSONObject species = firstResult.getJSONObject("species");
            String scientificName = species.getString("scientificNameWithoutAuthor");
            System.out.println(scientificName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}