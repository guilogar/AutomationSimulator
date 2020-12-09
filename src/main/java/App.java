package main;

import io.github.cdimascio.dotenv.Dotenv;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Guillermo López García
 * @version 2.0 November, 8th 2020
 */
public class App
{	
	// Home Automation Simulator
	private final static Dotenv dotenv = Dotenv.load();

    private final static String[] THINGSPEAK_API_KEYS = dotenv.get("THINGSPEAK_API_KEYS").split(",");
    private final static String[] CHANNEL_IDS = dotenv.get("CHANNEL_IDS").split(",");

    private static SimulatorChannel[] thingSpeakSimulators;
	
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
		int thingSpeakSimulatorsLength = THINGSPEAK_API_KEYS.length;

		thingSpeakSimulators = new SimulatorChannel[thingSpeakSimulatorsLength];

		for(int i = 0; i < thingSpeakSimulatorsLength; i++)
		{
			thingSpeakSimulators[i] = new SimulatorChannel(
				THINGSPEAK_API_KEYS[i].trim(), Integer.parseInt(CHANNEL_IDS[i].trim())
			);
		}
		
		String[] fields = dotenv.get("FIELDS").split(",");
		String[] mins = dotenv.get("MIN").split(",");
		String[] maxs = dotenv.get("MAX").split(",");

		int numChannel = 0, range = 7;
		for(int i = 0; i < fields.length; i += range)
		{
			String[] f = new String[range + 1];
			f[0] = dotenv.get("SENSOR_NAME");
			f = ArrayUtils.addAll(f, Arrays.copyOfRange(fields, i, i + range));
			
			String[] sensors = dotenv.get("SENSOR_VALUE").split("-");

			String[] mi = new String[range + 1];
			mi[0] = (sensors[0] != null) ? sensors[0] : "1";
			mi = ArrayUtils.addAll(mi, Arrays.copyOfRange(mins, i, i + range));
			
			String[] ma = new String[range + 1];
			ma[0] = (sensors[1] != null) ? sensors[1] : mi[0];
			ma = ArrayUtils.addAll(ma, Arrays.copyOfRange(maxs, i, i + range));

			generateData(
				thingSpeakSimulators[numChannel++], 0,
				f, mi, ma
			);
		}
	}
	
	private static void generateData(
		final SimulatorChannel thingspeak, int delay,
		final String[] fields,
		final String[] mins,
		final String[] maxs
	)
	{
		TimerTask timerTask = new TimerTask()
		{ 
			@Override
			//Code will be repeated:
			public void run()
			{
				LinkedHashMap<String, Double> values = new LinkedHashMap<>();

				for(int i = 0; i < fields.length; i++)
				{
					String field = fields[i];

					if(field != null)
					{
						Double min = Double.parseDouble(mins[i].trim());
						Double max = Double.parseDouble(maxs[i].trim());
						
						double r = random(min, max);
						double result = Math.round(r * 100.0) / 100.0;
						
						if(field.equalsIgnoreCase(dotenv.get("SENSOR_NAME")))
						{
							result = Math.floor(result);
						}

						values.put(field.trim(), result);
					}
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
