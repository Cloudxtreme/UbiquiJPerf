package com.braindeadprojects.ubiquijperf.ubiquiti;
import org.json.*;

/**
 * Class to extract Air OS Statistics from Ubiquiti gear
 * 
 * @author gillespiem
 */
public class AirOSStats extends JSONObject {
	
	public AirOSStats(String str) throws JSONException
	{
		super(str);
	}

	public String getHost(String str)
	{
		String result = new String();
		try
		{
			result = this.getJSONObject("host").get(str).toString();
		} catch (JSONException e)
		{
			e.printStackTrace();
		}

		return result;
	}
	
	public String getWireless(String str)
	{
		String result = new String();
		try
		{
			result = this.getJSONObject("wireless").get(str).toString();
		} catch (JSONException e)
		{
			e.printStackTrace();
		}

		return result;
	}

	public String getPolling(String str)
	{
		String result = new String();
		try
		{
			result = this.getJSONObject("wireless").getJSONObject("polling").get(str).toString();
		} catch (JSONException e)
		{
			e.printStackTrace();
		}

		return result;
	}

	public String getStats(String str)
	{
		String result = new String();
		try
		{
			result = this.getJSONObject("wireless").getJSONObject("stats").get(str).toString();
		} catch (JSONException e)
		{
			e.printStackTrace();
		}

		return result;
	}
	
	
	
	/**
	 * Method to return host.uptime as a string
	 * @return string The uptime
	 */
	public String getUptime()
	{
		return this.getHost("uptime");
	}
	
	public String getTime()
	{
		return this.getHost("time");		
	}

	public String getFWVersion()
	{
		return this.getHost("fwversion");
	}
	
	public String getHostname()
	{
		return this.getHost("hostname");
	}
	
	public String getNetRole()
	{
		return this.getHost("netrole");
	}	
	
	
	
	public String getMode()
	{
		return this.getWireless("mode");
	}

	public String getEssid()
	{
		return this.getWireless("essid");
	}
	
	public String getHideEssid()
	{
		return this.getWireless("hide_essid");
	}
	
	public String getApMac()
	{
		return this.getWireless("apmac");
	}
	
	public String getCountryCode()
	{
		return this.getWireless("countrycode");
	}
	
	public String getChannel()
	{
		return this.getWireless("channel");
	}
	
	public String getFrequency()
	{
		return this.getWireless("frequency");
	}
	
	public String getDFS()
	{
		return this.getWireless("dfs");
	}
	
	public String getOpMode()
	{
		return this.getWireless("opmode");
	}
	
	public String getAntenna()
	{
		return this.getWireless("antenna");
	}
	
	public String getChains()
	{
		return this.getWireless("chains");
	}
	
	public String getSignal()
	{
		return this.getWireless("signal");
	}
	
	public String getRSSI()
	{
		return this.getWireless("rssi");
	}
	
	public String getNoiseFloor()
	{
		return this.getWireless("noisef");
	}
		
	public String getAck()
	{
		return this.getWireless("ack");
	}
	
	public String getDistance()
	{
		return this.getWireless("distance");
	}
	
	public String getCCQ()
	{
		return this.getWireless("ccq");
	}
		
	public String getTxRate()
	{
		return this.getWireless("txrate");
	}
	
	public String getRxRate()
	{
		return this.getWireless("rxrate");
	}
	
	public String getQOS()
	{
		return this.getWireless("qos");
	}
	
	public String getTXChainmask()
	{
		return this.getWireless("tx_chainmask");
	}
	
	public String getChainRSSI()
	{
		return this.getWireless("chainrssi");
	}
	
	public String getChainRSSIMgmt()
	{
		return this.getWireless("chainrssimgmt");
	}
	
	public String getChainRSSIExt()
	{
		return this.getWireless("chainrssiext");
	}

	public String getSecurity()
	{
		return this.getWireless("security");
	}
	
	public String getRStatus()
	{
		return this.getWireless("rstatus");
	}
	
	public String getCount()
	{
		return this.getWireless("count");
	}
	
	public String getWDS()
	{
		return this.getWireless("wds");
	}
	
	public String getAPRepeater()
	{
		return this.getWireless("aprepeater");
	}
	
	public String getCHWidth()
	{
		return this.getWireless("chwidth");
	}
	
	public String getChanBW()
	{
		return this.getWireless("chanbw");
	}
	
	public String getCWMmode()
	{
		return this.getWireless("cwmmode");
	}
	
	public String getRXChainMask()
	{
		return this.getWireless("rx_chainmask");
	}
	
	public String getTXChainMask()
	{
		return this.getWireless("tx_chainmask");
	}
	
	/*
	"polling": {
		"enabled": 1, "quality": 77, "capacity": 56, "priority": 3, "noack": 0,
		"airsync_mode": 0, "airsync_connections": 0,
		"airsync_down_util" : 0, "airsync_up_util" : 0,
		"airselect" : 0, "airselect_interval" : 1000
	},
	*/
	
	public String getPollingEnabled()
	{
		return this.getPolling("enabled"); 
	}
	
	public String getPollingQuality()
	{
		return this.getPolling("quality"); 
	}
	
	public String getPollingCapacity()
	{
		return this.getPolling("capacity"); 
	}
	
	public String getPollingPriority()
	{
		return this.getPolling("priority"); 
	}
	
	public String getPollingNoACK()
	{
		return this.getPolling("noack"); 
	}
	
	public String getPollingAirSyncMode()
	{
		return this.getPolling("airsync_mode"); 
	}
	
	public String getPollingAirSyncConnections()
	{
		return this.getPolling("airsync_connections"); 
	}
	
	public String getPollingAirSyncDownUtil()
	{
		return this.getPolling("airsync_down_util"); 
	}
	
	public String getPollingAirSyncUpUtil()
	{
		return this.getPolling("airsync_up_util"); 
	}
	
	public String getPollingAirSelect()
	{
		return this.getPolling("airselect"); 
	}
	
	public String getPollingAirSyncInterval()
	{
		return this.getPolling("airselect_interval"); 
	}
	
	/*
	"stats": {		"rx_nwids": 91,
		"rx_crypts": 0,
		"rx_frags": 0,
		"tx_retries": 0,
		"missed_beacons": 0,
		"err_other": 0
	},
	*/
	
	public String getStatsRXNwids()
	{
		return this.getStats("rx_nwids"); 
	}
	
	public String getStatsRXCrypts()
	{
		return this.getStats("rx_crypts"); 
	}
	
	public String getStatsRXFrags()
	{
		return this.getStats("rx_frags"); 
	}
	
	public String getStatsTXRetries()
	{
		return this.getStats("tx_retries"); 
	}
	
	public String getStatsMissedBeacons()
	{
		return this.getStats("missed_beacons"); 
	}
	
	public String getStatsErrOther()
	{
		return this.getStats("err_other"); 
	}
	
	
}
