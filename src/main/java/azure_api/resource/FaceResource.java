package azure_api.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FaceResource {

	private static final String subscriptionKey = "cee261e071b94f568bc79cdf85f11aa7";
	private static final String uriBase = "https://brazilsouth.api.cognitive.microsoft.com/face/v1.0/";
	
	@RequestMapping(value = "/face/identify", method = RequestMethod.POST)
	public ResponseEntity <String> identify(
			@RequestParam(value="file", required=true) MultipartFile arquivo,
			@RequestParam(value = "personGroupId", required = true) String personGroupId
			) {
		try {
			
			JSONArray faceDetect = detect(arquivo);
			
			JSONObject jsonObject = faceDetect.getJSONObject(0);
			 
			String faceId = jsonObject.get("faceId").toString();
			ArrayList faceArray = new ArrayList();
			faceArray.add(faceId);
			
			
			String url = uriBase + "/" + personGroupId + "/identify";
			
			
			URIBuilder builder = new URIBuilder(url);

			// Prepare the URI for the REST API call.
			URI uri = builder.build();
			HttpPost request = new HttpPost(uri);

			// Request headers.
			request.setHeader("Content-Type", "application/json");
			request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

			// Request body
			String payload = "{" + "\"faceIds\": \"" + faceArray + "\"}";
			StringEntity reqEntity = new StringEntity(payload);
			request.setEntity(reqEntity);

			// Execute the REST API call and get the response entity.
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				String jsonString = EntityUtils.toString(entity).trim();
				return new ResponseEntity<String>(jsonString, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			}

			
		} catch (Exception e) {
			// Display error message.
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	
	public JSONArray detect(MultipartFile file) throws ParseException, IOException, URISyntaxException {

		URIBuilder builder = new URIBuilder(uriBase + "/detect");

		// Prepare the URI for the REST API call.
		URI uri = builder.build();
		HttpPost request = new HttpPost(uri);

		// Request headers.
		request.setHeader("Content-Type", "application/octet-stream");
		request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

		// Request body
		File convFile = new File(file.getOriginalFilename());
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		FileEntity body = new FileEntity(convFile, ContentType.APPLICATION_OCTET_STREAM);
		request.setEntity(body);

		// Execute the REST API call and get the response entity.
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response = httpclient.execute(request);
		HttpEntity entity = response.getEntity();
		
		JSONArray jsonArray;

		
		if (entity != null) {
			String jsonString = EntityUtils.toString(entity).trim();
			jsonArray = new JSONArray(jsonString);
			
		} else {
			jsonArray = new JSONArray("[]");
			
		}
		return jsonArray;
	}

}
