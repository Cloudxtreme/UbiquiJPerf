package com.braindeadprojects.ubiquijperf.ubiquiti;

public class UbntCredentials {
	private String httpProtocol, apIP, username, password;

	
	public UbntCredentials(String httpProtocol, String apIp, String username, char[] password)
	{
		this.httpProtocol = httpProtocol;
		this.username = username;
		this.apIP = apIp;
		//@todo: Fix this, it's a security violation (Password is now in String, readable by all)
		this.password = new String(password);
	}
	
	public String getHttpProtocol()
	{
		return this.httpProtocol;
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
	public String getApIP()
	{
		return this.apIP;
	}
	
	public String getPassword()
	{
		return this.password;
	}
	
		
}
