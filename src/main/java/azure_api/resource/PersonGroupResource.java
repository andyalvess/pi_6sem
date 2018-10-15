package azure_api.resource;

import java.io.File;
import java.io.FileOutputStream;
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
public class PersonGroupResource {

	private static final String subscriptionKey = "cee261e071b94f568bc79cdf85f11aa7";
	private static final String uriBase = "https://brazilsouth.api.cognitive.microsoft.com/face/v1.0/persongroups";
	
	@RequestMapping(value = "/personGroups/criar/{personGroupId}", method = RequestMethod.PUT)
	public ResponseEntity <String> Create(@PathVariable("personGroupId") String id) {
		
		HttpClient httpclient = new DefaultHttpClient();

		try {
			URIBuilder builder = new URIBuilder(uriBase + "/" + id);

						// Prepare the URI for the REST API call.
			URI uri = builder.build();
			HttpPut request = new HttpPut(uri);

			// Request headers.
			request.setHeader("Content-Type", "application/json");
			request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
			
			 // Request body
            StringEntity reqEntity = new StringEntity("{\"name\": \"Projeto Integrado\", \"userData\": \"Grupo de Projeto Integrado\"}");
            request.setEntity(reqEntity);

			// Execute the REST API call and get the response entity.
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				//String jsonString = EntityUtils.toString(entity).trim();
				return new ResponseEntity<String>("OK", HttpStatus.OK);
			}else {
				return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			// Display error message.
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	@RequestMapping(value = "/personGroups/listar", method = RequestMethod.GET)
	public ResponseEntity <String> List() {
		System.out.println("alsjdbasklfbasfbaskljfasb");
		HttpClient httpclient = new DefaultHttpClient();

		try {
			URIBuilder builder = new URIBuilder(uriBase);

			// Prepare the URI for the REST API call.
			URI uri = builder.build();
			HttpGet request = new HttpGet(uri);

			// Request headers.
			request.setHeader("Content-Type", "application/json");
			request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

			// Execute the REST API call and get the response entity.
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				String jsonString = EntityUtils.toString(entity).trim();
                if (jsonString.charAt(0) == '[') {
                    JSONArray jsonArray = new JSONArray(jsonString);
                    System.out.println(jsonArray.toString(2));
                    return new ResponseEntity<String>(jsonArray.toString(2), HttpStatus.OK);
                }
                else if (jsonString.charAt(0) == '{') {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    return new ResponseEntity<String>(jsonObject.toString(2), HttpStatus.OK);
                    //System.out.println(jsonObject.toString(2));
                } else {
                	return new ResponseEntity<String>(jsonString, HttpStatus.OK);
                    //System.out.println(jsonString);
                }
			}else {
				return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			// Display error message.
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	@RequestMapping(value = "/personGroups/listar/{personGroupId}", method = RequestMethod.GET)
	public ResponseEntity <String> ListGroup(@PathVariable("personGroupId") String personGroupId) {
		System.out.println(personGroupId);
		HttpClient httpclient = new DefaultHttpClient();

		try {
			URIBuilder builder = new URIBuilder(uriBase + "/" +  personGroupId + "/persons");

			// Prepare the URI for the REST API call.
			URI uri = builder.build();
			HttpGet request = new HttpGet(uri);

			// Request headers.
			request.setHeader("Content-Type", "application/json");
			request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
			
			 // Request body
            //StringEntity reqEntity = new StringEntity("{\"name\": \"teste2\", \"userData\": \"teste userData\"}");
            //request.setEntity(reqEntity);

			// Execute the REST API call and get the response entity.
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				String jsonString = EntityUtils.toString(entity).trim();
                if (jsonString.charAt(0) == '[') {
                    JSONArray jsonArray = new JSONArray(jsonString);
                    System.out.println(jsonArray.toString(2));
                    return new ResponseEntity<String>(jsonArray.toString(2), HttpStatus.OK);
                }
                else if (jsonString.charAt(0) == '{') {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    return new ResponseEntity<String>(jsonObject.toString(2), HttpStatus.OK);
                    //System.out.println(jsonObject.toString(2));
                } else {
                	return new ResponseEntity<String>(jsonString, HttpStatus.OK);
                    //System.out.println(jsonString);
                }
			}else {
				return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			// Display error message.
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	@RequestMapping(value = "/personGroups/{personGroupId}/train", method = RequestMethod.POST)
	public ResponseEntity<String> addFace(
			@PathVariable("personGroupId") String personGroupId
			) throws Exception {

		HttpClient httpclient = new DefaultHttpClient();

		try {
			URIBuilder builder = new URIBuilder(uriBase + "/" + personGroupId + "/train");

			// Prepare the URI for the REST API call.
			URI uri = builder.build();
			HttpPost request = new HttpPost(uri);

			// Request headers.
			request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

			// Execute the REST API call and get the response entity.
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				//String jsonString = EntityUtils.toString(entity).trim();
				return new ResponseEntity<String>("OK", HttpStatus.OK);
			}else {
				return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			// Display error message.
			System.out.println(e.getMessage());
		}
		return null;
	}
	

}
