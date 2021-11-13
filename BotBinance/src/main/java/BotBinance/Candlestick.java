package BotBinance;

  public class Candlestick {
private double Open;
@Override
public String toString() {
	return "Candlestick [Open=" + Open + ", High=" + High + ", Low=" + Low + ", Close=" + Close + ", Volume=" + Volume
			+ ", OpenTime=" + OpenTime + ", CloseTime=" + CloseTime + ", HLC3=" + HLC3 + "]";
}

private double High;
private double Low;
private double Close;
private double Volume;
private long OpenTime;
private long CloseTime;
private double HLC3;
public double getOpen() {
	return Open;
}

public double getLow() {
	return Low;
}
public double getHigh() {
	return High;
}
public double getClose() {
	return Close;
}

public double getVolume() {
	return Volume;
}

public long getOpenTime() {
	return OpenTime;
}
public long getCloseTime() {
	return CloseTime;
}

public double getHLC3() {
	return HLC3;
}

public Candlestick(double open, double high, double low, double close, double volume,double hLC3 ,long openTime,long closeTime) {
	
	Open = open;
	High = high;
	Low = low;
	Close = close;
	Volume = volume;
	HLC3=hLC3;
	OpenTime = openTime;
	CloseTime=closeTime;
	
}

}
