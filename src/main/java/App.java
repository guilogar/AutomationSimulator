package main;

import io.github.cdimascio.dotenv.Dotenv;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Guillermo López García
 * @version 2.0 November, 8th 2020
 */
public class App
{	
	// Home Automation Simulator
	private final static Dotenv dotenv = Dotenv.load();

    private final static String THINGSPEAK_API_KEY = dotenv.get("THINGSPEAK_API_KEY"); // ATTENTION : PUT your API_KEY
    private final static int CHANNEL_ID = Integer.parseInt(dotenv.get("CHANNEL_ID"));

    private static SimulatorChannel thingSpeak;
	
    // Frequency updating time
    private static int time = Integer.parseInt(dotenv.get("TIME_TASK"));
    
    // Formater to values
    static DecimalFormat df2 = new DecimalFormat("#,##");
    
    private static double random(double min, double max)
    {
       double range = Math.abs(max - min);     
       return (Math.random() * range) + (min <= max ? min : max);
    }
    
	public static void main (String [] args)
	{
    	thingSpeak = new SimulatorChannel(THINGSPEAK_API_KEY, CHANNEL_ID);
    	generateData(thingSpeak, 0);
    }
    
    /*
     * thingspeak : channel
     * delay : time for waiting in milliseconds next start of execution
     */
	private static void generateData(final SimulatorChannel thingspeak, int delay)
	{
		TimerTask timerTask = new TimerTask()
		{ 
			@Override
			//Code will be repeated:
			public void run()
			{
				String[] fields = dotenv.get("FIELDS").split(",");
				String[] mins = dotenv.get("MIN").split(",");
				String[] maxs = dotenv.get("MAX").split(",");

				LinkedHashMap<String, Double> values = new LinkedHashMap<>();


				for(int i = 0; i < fields.length; i++)
				{
					String field = fields[i];
					Double min = Double.parseDouble(mins[i]);
					Double max = Double.parseDouble(maxs[i]);
					
					double r = random(min, max);
					double result = Math.round(r * 100.0) / 100.0;

					values.put(field.trim(), result);
				}
			
				System.out.println("\n*Generating random data from channel "  + thingspeak.getChannelName() +" \n");
				//We will establish a value for each ThingSpeak channel field

				int numField = 1;
				for (Map.Entry<String, Double> entry : values.entrySet())
				{
					String field = entry.getKey();
					Double value = entry.getValue();
					
					System.out.println("Random value of Field 1, " + field + " : " + value);

					thingspeak.setDataField(numField++, value);
				}
				
				System.out.println("\n*Remember, system will generate data every "  + time/1000 + " seconds\n");	            	
				thingspeak.sendData();	                

				System.out.println("\n");
			}
		}; 

		Timer timer = new Timer(); 
		
		// Every time variable the value generation function is run and the generated values are sent to the channel
		timer.scheduleAtFixedRate(timerTask, delay, time);
	}
}
