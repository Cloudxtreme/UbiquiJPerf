package com.braindeadprojects.ubiquijperf.ubiquiti;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.*;
import org.json.*;


/**
 * Class to perform actions relative to Ubiqiuti Gear 
 * (login, grab stats, etc)
 * 
 * @author gillespiem
 */
public class UbntGatherer {
	
	boolean authenticated = false;
	private HttpClient client;
	private HttpGet ubntGet;
	private HttpPost ubntPost;
	public  AirOSStats latestStats;
	
	private String apIp;
	private String username;
	private String password;
	
	private static String boundaryString = new String("--------------------abcdefghipoopingpig");
	
	/**
	 * Constructor that creates a few of the internal objects that we'll need
	 */
	public UbntGatherer()
	{
		//@todo: This proxy is only for debugging purposes, remove it.
		HttpHost proxy = new HttpHost("127.0.0.1", 8008, "http");
	 	
		client = HttpClientBuilder.create()
				  .setUserAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.7")
				  .setMaxConnPerRoute(4)
				  .setProxy(proxy)
				  .build();
		
		this.ubntGet = new HttpGet();
		this.ubntPost = new HttpPost();
	}
	
	/**
	 * Method to set basic member variables in this class
	 * 
	 * @todo Write it
	 * @param usernameString
	 * @param passwordString
	 * @param apIpString
	 */
	public UbntGatherer(String username, String password, String apIp)
	{
		this.username = username;
		this.password = password;
		this.apIp = apIp;
		//In turn, you should call the non-paramaterized constructor...
		
	}
	
	/**
	 * Method to handle phase one of login: GET the login URI, accept cookies, etc
	 */
	public void phaseOne()	{
						
		try {
			this.ubntGet.setURI(new URI("http://" + apIp + "/login.cgi"));
			client.execute(this.ubntGet);
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to handle phase two of login: POSTing to the web form
	 */	
	public void phaseTwo()	{
				
		try {
			this.ubntPost.setURI( new URI("http://" + apIp +"/login.cgi") );
			this.ubntPost.setHeader("Content-Type", 
							"multipart/form-data;"
							+ "boundary=" + boundaryString);
			
			
			//http://stackoverflow.com/questions/19292169/multipartentitybuilder-and-charset
			MultipartEntityBuilder entity = MultipartEntityBuilder.create();
			Charset chars = Charset.forName("UTF-8");
			entity.setCharset(chars);
			entity.setBoundary(boundaryString);
			entity.addTextBody("username", username, ContentType.MULTIPART_FORM_DATA);
			entity.addTextBody("password", password, ContentType.MULTIPART_FORM_DATA);
			entity.addTextBody("uri", "/status.cgi",ContentType.MULTIPART_FORM_DATA);
	
			this.ubntPost.setEntity(entity.build());
			this.ubntPost.addHeader("Referer", "http://" + apIp + "/login.cgi");
			this.ubntPost.addHeader("Expect","");

			client.execute(this.ubntPost);
		
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}		
	
		authenticated = true;
	}
	
	/**
	 * Method to handle phase three - GET the status page
	 */
	public void phaseThree() {
		HttpResponse response;
		String responseString = new String();
		String line;
		
		try {
			
			//Grab the Status CGI page
			this.ubntGet.setURI(new URI("http://" + apIp + "/status.cgi"));
			response = client.execute(ubntGet);
				
			//Build a response string:
			BufferedReader br = new BufferedReader(
							new InputStreamReader(response.getEntity().getContent()));
			
			while ((line = br.readLine()) != null) {
				  responseString = responseString.concat(line);
				} 
						
			//Create an AirOSStats object, parsing the string
			this.latestStats = new AirOSStats(responseString);
			
		} catch (ClientProtocolException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		} catch (URISyntaxException e){
			e.printStackTrace();
		} catch (JSONException e){
			e.printStackTrace();
		}
		 
		
	}
	

	/**
	 * Method to grab stats
	 * @todo We shouldn't have to login each time if we retain cookies, etc...
	 */
	public void grabStats(){
		
		//If we're not authenticated, we need to auth ourselves.
		if (!this.authenticated)
		{
			System.out.println("Phase One begin...");
			this.phaseOne();
			System.out.println("Phase One complete...");
			System.out.println("Phase Two begin...");
			this.phaseTwo();
			System.out.println("Phase Two complete...");
		}
		
		//Actually perform the grabbing of statistics
		System.out.println("Phase Three begin...");
		this.phaseThree();
		System.out.println("Phase Three end...");
	}
	
}
