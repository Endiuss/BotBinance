package BotBinance;

public class VWAPStartTime {
	public long getTime() {
		long msecTime=System.currentTimeMillis()/1000;
	long dayTime=msecTime%86400;

	long startTime=0;
	if(dayTime>=57600) {startTime=(msecTime-(msecTime-5760))*1000;}
	else {startTime=(msecTime-dayTime-28800)*1000;}  
	return startTime;
}
}
