# AutomationSimulator

## Install Maven

### [maven](https://maven.apache.org/install.html)

## Add .env in the root of project with env variable
### In /.env, add this example with your thingspeak api key and channel id
```
THINGSPEAK_API_KEYS=YOUR API's KEY HERE
CHANNEL_IDS=YOUR CHANNEL ID's HERE
TIME_TASK=60000
SENSOR_NAME=sensorId
SENSOR_VALUE=1-10
FIELDS=energyconsumption,humidity,temperature,tvconsumption
MIN=0.0,0.0,0.0,0.0
MAX=4650.0,100.0,70.0,250.0
```

## Compile project with dependencies
```
mvn clean compile assembly:single
```

## Execute project with dependencies .jar
```
java -jar target/HomeAutomationSimulatorRefactor-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Important!
```
If you change any environment variable, you must recompile the application!
```

## Initial thanks to...
```
Juan Boubeta at University of Cadiz
```

## Enjoy!
```
All my work is for the prosperity of the free code,
but if you want to invite me to a coffee

\   /
 \|/
  |
```
[guilogar cofee](https://ko-fi.com/guilogar)
