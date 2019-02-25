package pros.app.com.pros.panda;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import pros.app.com.pros.panda.model.Encoding;
import pros.app.com.pros.panda.model.Profile;
import pros.app.com.pros.panda.model.UploadSession;
import pros.app.com.pros.panda.model.Video;


public class Panda {
	
	private Properties properties = new Properties();
	private static Gson gson = new GsonBuilder()
     .serializeNulls()
     .setDateFormat("yyyy/MM/dd HH:mm:ss Z")
     .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
     .setPrettyPrinting()
     .setVersion(1.0)
     .create();

	public Panda(String accessKey, String cloudId, String secretKey, String apiHost) {
		this.properties.put("access-key", accessKey);
		this.properties.put("cloud-id", cloudId);
		this.properties.put("secret-key", secretKey);
		this.properties.put("api-host", apiHost);
	}

	public Collection<Profile> getProfiles() {
		String json = PandaHttp.get("/profiles.json", new HashMap<String,String>(), this.properties);
		
		Type collectionType = new TypeToken<Collection<Profile>>(){/**/}.getType();
		Collection<Profile> profiles = gson.fromJson(json, collectionType);
		
		return profiles;
	}

	public Map<String,String> accessDetails(PandaHttp.Method method, String url) {
		Map<String,String> parameters = PandaHttp.signedParams(method.name(), url, new HashMap<String,String>(), this.properties);
		parameters.put("api_host", this.properties.getProperty("api-host"));
		
		return parameters;
	}

	public Video postRemoteVideo(String url) {
		
		Map<String,String> params = new HashMap<String,String>();
		
		params.put("source_url", url);
		
		String json = PandaHttp.post("/videos.json", params, this.properties);
		
		return gson.fromJson(json, Video.class);
	}

  public Video postVideo(File file) {
    return this.postVideo(file,null);
  }

  public Video postVideo(File file, String payload) {
		
		Map<String,String> params = new HashMap<String,String>();
	  if(payload!=null){
      params.put("payload", payload);
    }
		String json = PandaHttp.postFile("/videos.json", params, this.properties, file);
		
		return gson.fromJson(json, Video.class);

  }


	public Profile postProfile(Profile profile) {
		
		Map<String,String> params = new HashMap<String,String>();
		
		params.put("preset_name", profile.presetName);
		
		String json = PandaHttp.post("/profiles.json", params, this.properties);
		
		return gson.fromJson(json, Profile.class);
	}


	public Video getVideo(String id) {
		String json = PandaHttp.get("/videos/" + id + ".json", new HashMap<String,String>(), this.properties);
		
		return gson.fromJson(json, Video.class);
	}

	public Collection<Encoding> getEncodings(String id) {
		String json = PandaHttp.get("/videos/" + id + "/encodings.json", new HashMap<String,String>(), this.properties);
		
		Type collectionType = new TypeToken<Collection<Encoding>>(){/**/}.getType();
		Collection<Encoding> encodings = gson.fromJson(json, collectionType);
		
		return encodings;
	}
	
	public Collection<Encoding> getEncodings(Encoding.Status status) {
		Map<String,String> params = new HashMap<String,String>();

		params.put("status", status.name());
		
		return this.getEncodings(params);
	}
	
	private Collection<Encoding> getEncodings(Map<String,String> params) {
		String json = PandaHttp.get("/encodings.json", params, this.properties);
	
		Type collectionType = new TypeToken<Collection<Encoding>>(){/**/}.getType();
		Collection<Encoding> encodings = gson.fromJson(json, collectionType);
		
		return encodings;
	}

	public Collection<Video> getVideos(Video.Status status) {
		Map<String,String> params = new HashMap<String,String>();

		params.put("status", status.name());
		
		return this.getVideos(params);
	}
	
	/**
	 * Before uploading a file to panda, you will need to create a session for each particular files. In return you get a unique location for this file.
	 * 
	 * Required parameters
	 * file_size: Size in bytes of the video
	 * file_name: File name of the video
	 * 
     * Optional parameters
     * use_all_profiles: Default is false
     * profiles: comma-separated list of profile names or IDs to be used during encoding. Alternatively, specify "none" so no encodings are created yet
     * path_format: represents the complete video path without the extension name. It can be constructed using some provided keywords.
     * payload: arbitrary string stored along the Video object.
     * 
	 * @param params
	 * @return
	 */
	public UploadSession createVideoUploadSession(Map<String,String> params){
		if(!(params.containsKey("file_size") && params.containsKey("file_name"))){
			throw new RuntimeException("file_size and file_name parameters must be present");
		}
		
		String json = PandaHttp.post("/videos/upload.json", params, this.properties);
		
		return gson.fromJson(json, UploadSession.class);
	}
	
	public Collection<Video> getVideos() {
		return this.getVideos(new HashMap<String,String>());
	}

	
	private Collection<Video> getVideos(Map<String,String> params) {
		String json = PandaHttp.get("/videos.json", params, this.properties);
	
		Type collectionType = new TypeToken<Collection<Video>>(){/**/}.getType();
		Collection<Video> videos = gson.fromJson(json, collectionType);
		
		return videos;
	}

	public void deleteVideo(String id) {
		PandaHttp.delete("/videos/" + id + ".json", new HashMap<String,String>(), this.properties);
	}

	public Collection<Encoding> getEncodings() {
		return this.getEncodings(new HashMap<String,String>());
	}
	
	public void deleteEncoding(String id) {
		PandaHttp.delete("/videos/" + id + ".json", new HashMap<String,String>(), this.properties);
	}

	public Encoding getEncoding(String id) {
		String json = PandaHttp.get("/encodings/" + id + ".json", new HashMap<String,String>(), this.properties);

		
		return gson.fromJson(json, Encoding.class);
	}
}
