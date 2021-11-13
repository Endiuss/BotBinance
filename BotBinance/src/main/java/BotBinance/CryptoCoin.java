package BotBinance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.asynchttpclient.*;
import org.json.JSONObject;

import static org.asynchttpclient.Dsl.*;
import io.netty.util.concurrent.Future;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
public class CryptoCoin {
	protected String Name;
	protected long LastTradedPrice=-1;
	protected long LastCandelCloseTime;
	protected double NumaratorVWAP;
	protected double NumitorVWAP;
	protected double VWAP;
	protected double MaxBalQty;
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public long getLastTradedPrice() {
		return LastTradedPrice;
	}
	public void setLastTradedPrice(long lastTradedPrice) {
		LastTradedPrice = lastTradedPrice;
	}
	public long getLastCandelCloseTime() {
		return LastCandelCloseTime;
	}
	public void setLastCandelCloseTime(long lastCandelCloseTime) {
		LastCandelCloseTime = lastCandelCloseTime;
	}
	public double getNumaratorVWAP() {
		return NumaratorVWAP;
	}
	public void setNumaratorVWAP(double numaratorVWAP) {
		NumaratorVWAP = numaratorVWAP;
	}
	public double getNumitorVWAP() {
		return NumitorVWAP;
	}
	public void setNumitorVWAP(double numitorVWAP) {
		NumitorVWAP = numitorVWAP;
	}
	public double getVWAP() {
		return VWAP;
	}
	public void setVWAP(double vWAP) {
		VWAP = vWAP;
	}
	public double getMaxBalQty() {
		return MaxBalQty;
	}
	public void setMaxBalQty(double maxBalQty) {
		MaxBalQty = maxBalQty;
	}
	
	protected ArrayList<TradingZone> BuyTradingSideZones=new ArrayList<TradingZone>();
	protected ArrayList<TradingZone> SellTradingSideZones=new ArrayList<TradingZone>();
	protected org.asynchttpclient.ws.WebSocket WsLiqProcess=null;
	protected CompletableFuture<Void> Updater=null;

	
	
			


	CompletableFuture<Void> updater=null;
	@SuppressWarnings("unchecked")
	
	
	public  CryptoCoin(String nume,double MaxBQty) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, SQLException, InterruptedException, ExecutionException {
		this.Name=nume;
		
		
		this.MaxBalQty=MaxBQty;
		this.WsLiqProcess=BotBinance.Generator.WsMarketLiq(this);
		//this.VWAP=NumaratorVWAP/NumaratorVWAP;
		BotBinance.dataRequest.GetCoinData(this);
		Updater=BotBinance.updaterGenerator.generateCryptoUpdater(this);
		
	
		System.out.println("A MERS ASDASDASDASD");
	}
	TradingZone getBuyZoneByPrice(double price) {
		
		for(TradingZone zone : BuyTradingSideZones) {
			if(zone.isPriceInZone(price)) {return zone;}
			
		}
		return null;
	}
	/*boolean isPriceTradeable(double price,String side) {
		if(side==)
		if(pr)
			}
}
*/
}
