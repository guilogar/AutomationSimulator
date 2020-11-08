package main;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Guillermo López García
 * @version 1.0 November, 14th 2015
 */
public class App
{	
    // Home Automation Simulator
    private final static String THINGSPEAK_API_KEY = "A0CIYT0EQ8D273RQ"; // ATTENTION : PUT your API_KEY
    private final static int CHANNEL_ID = 915944;

    private static SimulatorChannel thingSpeak;
	
    // Frequency updating time
    private static int time = 60000;
    
    private static double energyconsumption = 0.0; // Field 1 in thingspeak
    private static double humidity = 0.0;          // Field 2 in thingspeak
    private static double temperature = 0.0;       // Field 3 in thingspeak
    private static double tvconsumption = 0.0;     // Field 4 in thingspeak
    
    // Formater to values
    static DecimalFormat df2 = new DecimalFormat("#,##");
    
    private static double maxHumidity = 100.0;
    private static double minHumidity = 0.0;
    private static double maxTemperature = 70.0;
    private static double minTemperature = 0.0;
    private static double maxTvConsumption = 250.0;
    private static double minTvConsumption = 0.0;
    private static double maxEnergyConsumption = 4650.0;
    private static double minEnergyConsumption = 0.0;
    
    private static double random(double min, double max)
    {
       double range = Math.abs(max - min);     
       return (Math.random() * range) + (min <= max ? min : max);
    }
    
	public static void main (String [] args)
	{
    	thingSpeak = new SimulatorChannel(THINGSPEAK_API_KEY, CHANNEL_ID);
    	generateData(thingSpeak,0);
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
				//generation of energyconsumption value and format the value with 2 decimals
				energyconsumption = random(minEnergyConsumption, maxEnergyConsumption);
				energyconsumption = Math.round(energyconsumption * 100.0) / 100.0;
				//generation of temperature value and format the value with 2 decimals
				temperature = random(minTemperature, maxTemperature);
				temperature = Math.round(temperature * 100.0) / 100.0;
				//generation of humidity value and format the value with 2 decimals
				humidity = random(minHumidity, maxHumidity);
				humidity = Math.round(humidity * 100.0) / 100.0;
				//generation of tvconsumption value and format the value with 2 decimals
				tvconsumption = random(minTvConsumption, maxTvConsumption);
				tvconsumption = Math.round(tvconsumption * 100.0) / 100.0;
			
				System.out.println("\n*Generating random data from channel "  + thingspeak.getChannelName() +" \n");
				//We will establish a value for each ThingSpeak channel field

				thingspeak.setDataField(1,energyconsumption);
				System.out.println("Random value of Field 1, Energy Consumption : " + energyconsumption);
				
				thingspeak.setDataField(2, temperature);
				System.out.println("Random value of Field 2, Temperature : " + temperature);
									
				thingspeak.setDataField(3, humidity);
				System.out.println("Random value of Field 3, Humidity : " + humidity);
				
				thingspeak.setDataField(4, tvconsumption);
				System.out.println("Random value of Field 4, Tv Consumption : " + tvconsumption);
									
				
				
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
