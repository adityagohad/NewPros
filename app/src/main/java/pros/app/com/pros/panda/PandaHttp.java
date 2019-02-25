package pros.app.com.pros.panda;

import android.util.Base64;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import local.org.apache.http.HttpResponse;
import local.org.apache.http.client.methods.HttpDelete;
import local.org.apache.http.client.methods.HttpGet;
import local.org.apache.http.client.methods.HttpPost;
import local.org.apache.http.client.utils.URLEncodedUtils;
import local.org.apache.http.entity.StringEntity;
import local.org.apache.http.entity.mime.MultipartEntity;
import local.org.apache.http.entity.mime.content.FileBody;
import local.org.apache.http.impl.client.DefaultHttpClient;
import local.org.apache.http.message.BasicNameValuePair;
import local.org.apache.http.util.EntityUtils;

public class PandaHttp {
	public enum Method {
		GET, POST, UPDATE, DELETE
	}

	static String get(String url, Map<String, String> params, Properties properties) {
		Map<String, String> sParams = signedParams("GET", url, params, properties);
		String flattenParams = canonicalQueryString(sParams);
		String requestUrl = "http://" + properties.getProperty("api-host") + ":80/v2" + url + "?" + flattenParams;
		HttpGet httpGet = new HttpGet(requestUrl);
		DefaultHttpClient httpclient = new DefaultHttpClient();

		String stringResponse = null;

		try {
			HttpResponse response = httpclient.execute(httpGet);
			stringResponse = EntityUtils.toString(response.getEntity());
		} catch(IOException e){
			e.printStackTrace();
		}

		return stringResponse;
	}


	static String postFile(String url, Map<String,String> params, Properties properties, File file) {
		Map<String,String> sParams = signedParams("POST", url, params, properties);
		String flattenParams = canonicalQueryString(sParams);
		String requestUrl = "http://" + properties.getProperty("api-host") + ":80/v2" + url + "?" + flattenParams;

		HttpPost httpPost = new HttpPost(requestUrl);
    String stringResponse = null;
    FileBody bin = new FileBody(file, "application/octet-stream");
    try{
      MultipartEntity entity = new MultipartEntity();
      entity.addPart("file", bin);

      httpPost.setEntity(entity);

      DefaultHttpClient httpclient = new DefaultHttpClient();


			HttpResponse response = httpclient.execute(httpPost);
			stringResponse = EntityUtils.toString(response.getEntity());

    }catch(UnsupportedEncodingException ex){
      ex.printStackTrace();
    } catch(IOException e){
			e.printStackTrace();
		}		
    return stringResponse;
	}


	static String post(String url, Map<String,String> params, Properties properties) {
		Map<String,String> sParams = signedParams("POST", url, params, properties);
		String flattenParams = canonicalQueryString(sParams);
		String requestUrl = "http://" + properties.getProperty("api-host") + ":80/v2" + url + "?" + flattenParams;

		HttpPost httpPost = new HttpPost(requestUrl);
		DefaultHttpClient httpclient = new DefaultHttpClient();

		String stringResponse = null;

		try {
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

			httpPost.setEntity(new StringEntity(canonicalQueryString(sParams), "UTF-8"));
			HttpResponse response = httpclient.execute(httpPost);

			stringResponse = EntityUtils.toString(response.getEntity());

		} catch(IOException e){
			e.printStackTrace();
		}

		return stringResponse;
	}

	/*	
	public String put(String url, TreeMap params) {
		TreeMap sParams = signedParams("PUT", url, params);
		return "";
	}


	public String delete(String url, TreeMap params) {
		TreeMap sParams = signedParams("DELETE", url, params);
		return "";
	}
	 */	

	static Map<String, String> signedParams(String method, String url, Map<String, String> params, Properties properties) {
		params.put("cloud_id", properties.getProperty("cloud-id"));
		params.put("access_key", properties.getProperty("access-key"));
		params.put("timestamp", new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssz" ).format(new Date()));
		//		params.put("api_host", properties.getProperty("api-host"));
		params.put("signature", generateSignature(method, url, properties.getProperty("api-host"), properties.getProperty("secret-key"), params));
		return params;
	}

	private static String generateSignature(String method, String url, String host, String secretKey, Map<String,String> params) {
		String queryString = canonicalQueryString(params);
		String stringToSign = method.toUpperCase() + "\n" + host + "\n" + url + "\n" + queryString;

		String signature = null;

		try {

			SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(signingKey);

			byte[] rawHmac = mac.doFinal(stringToSign.getBytes());

			signature = Base64.encodeToString(rawHmac, Base64.URL_SAFE);
			//signature = new String(Base64.encodeBase64(rawHmac));

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}

		return signature;
	}

	private static String canonicalQueryString(Map<String,String> map) {
		List<BasicNameValuePair> qparams = new ArrayList<BasicNameValuePair>();

		for(Map.Entry<String, String> entry : map.entrySet()) {
			qparams.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));	
		}

		Comparator<BasicNameValuePair> comparator = new Comparator<BasicNameValuePair>() {
			public int compare(BasicNameValuePair o1, BasicNameValuePair o2) {
				return o1.getName().compareTo(o2.getName());
			}
		};

		Collections.sort(qparams, comparator);

	  return URLEncodedUtils.format(qparams, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E","~");
	}


	public static void delete(String url, HashMap<String, String> params, Properties properties) {
		Map<String, String> sParams = signedParams("DELETE", url, params, properties);
		String flattenParams = canonicalQueryString(sParams);
		String requestUrl = "http://" + properties.getProperty("api-host") + ":80/v2" + url + "?" + flattenParams;
		HttpDelete httpDelete = new HttpDelete(requestUrl);
		DefaultHttpClient httpclient = new DefaultHttpClient();

		try {
			httpclient.execute(httpDelete);
		} catch(IOException e){
			e.printStackTrace();
		}
	}
}
