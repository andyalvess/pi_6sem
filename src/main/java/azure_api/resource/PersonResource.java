package azure_api.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class PersonResource {

	private static final String subscriptionKey = "cee261e071b94f568bc79cdf85f11aa7";
	private static final String uriBase = "https://brazilsouth.api.cognitive.microsoft.com/face/v1.0/persongroups";

	@RequestMapping(value = "/person/criar", method = RequestMethod.POST)
	public ResponseEntity<String> Create(
			@RequestParam(value = "personGroupId", required = true) String personGroupId,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "userData", required = true) String userData) {

		HttpClient httpclient = new DefaultHttpClient();

		try {
			System.out.println(personGroupId);
			System.out.println(name);
			System.out.println(userData);

			String url = uriBase + "/" + personGroupId + "/persons";

			System.out.println(url);
			URIBuilder builder = new URIBuilder(url);

			// Prepare the URI for the REST API call.
			URI uri = builder.build();
			HttpPost request = new HttpPost(uri);

			// Request headers.
			request.setHeader("Content-Type", "application/json");
			request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

			// Request body
			String payload = "{" + "\"name\": \"" + name + "\", " + "\"userData\": \"" + userData + "\", " + "}";
			StringEntity reqEntity = new StringEntity(payload);
			request.setEntity(reqEntity);

			// Execute the REST API call and get the response entity.
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

	@RequestMapping(value = "/person/addFace", method = RequestMethod.POST)
	public ResponseEntity<String> addFace(
			@RequestParam(value="personGroupId", required=true) String personGroupId,
			@RequestParam(value="file", required=true) MultipartFile arquivo,
			@RequestParam(value="personId", required=true) String personId
			) throws Exception {

		HttpClient httpclient = new DefaultHttpClient();

		try {
			URIBuilder builder = new URIBuilder(uriBase + "/" + personGroupId + "/persons/" +  personId + "/persistedFaces");

			// Prepare the URI for the REST API call.
			URI uri = builder.build();
			HttpPost request = new HttpPost(uri);

			// Request headers.
			request.setHeader("Content-Type", "application/octet-stream");
			request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

			// Request body
//			File f = new File(arquivo.getBytes());
			File convFile = new File(arquivo.getOriginalFilename());
		    convFile.createNewFile(); 
		    FileOutputStream fos = new FileOutputStream(convFile); 
		    fos.write(arquivo.getBytes());
		    fos.close(); 
			FileEntity body = new FileEntity(convFile, ContentType.APPLICATION_OCTET_STREAM);
			request.setEntity(body);

			// Execute the REST API call and get the response entity.
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				String jsonString = EntityUtils.toString(entity).trim();
				if (jsonString.charAt(0) == '[') {
					JSONArray jsonArray = new JSONArray(jsonString);
					System.out.println(jsonArray.toString(2));
					return new ResponseEntity<String>(jsonArray.toString(2), HttpStatus.OK);
				} else if (jsonString.charAt(0) == '{') {
					JSONObject jsonObject = new JSONObject(jsonString);
					return new ResponseEntity<String>(jsonObject.toString(2), HttpStatus.OK);
					// System.out.println(jsonObject.toString(2));
				} else {
					return new ResponseEntity<String>(jsonString, HttpStatus.OK);
					// System.out.println(jsonString);
				}
			} else {
				return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			// Display error message.
			System.out.println(e.getMessage());
		}
		return null;
	}

}
