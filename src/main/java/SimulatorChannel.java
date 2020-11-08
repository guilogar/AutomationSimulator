package main;
import com.funapp.thingspeak.Channel;
import com.funapp.thingspeak.Entry;
import com.funapp.thingspeak.ThingSpeakException;
import java.lang.Exception;

/**
 * @author Guillermo López García
 * @version 2.0 November, 8th 2020
 */
public class SimulatorChannel 
{
	private Channel channel;
	private Entry entry;
		
	public SimulatorChannel(String apiKey, int channelId)
	{
		channel = new Channel(channelId, apiKey);
		entry = new Entry();
	}
	
	public String getChannelName()
	{
		try {
			return channel.getChannelFeed().getChannelName();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public void setDataField(int field, double value)
	{
		entry.setField(field, String.valueOf(value));
	}
	
	public String getStatus()
	{
		return entry.getStatus();
	}
	
	
	public void sendData()
	{
		try {
			channel.update(entry);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
