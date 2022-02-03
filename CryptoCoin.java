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
import org.json.JSONArray;
import org.json.JSONObject;

import static org.asynchttpclient.Dsl.*;
import io.netty.util.concurrent.Future;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
public class CryptoCoin {
	
	/**
	 * A CryptoCoin object  contains all data required for trading deccisions and references to processes that update the parameters
	 * There are implemented setters and getters for each atributes
	 * 
	 */
	protected String ActiveSide="OFF";


	protected String Name;
	protected int Leverage=1;
	protected double PercentProfitTarget=0.5;
	protected double ProfitPrice;
	protected double MinLiqValue=1;
	protected double LastTradedPrice=-1;
	protected double LastOrderPrice;
	protected long LastCandelCloseTime;
	protected double NumaratorVWAP;
	protected double NumitorVWAP;
	protected double VWAP;
	protected double MaxBalQty=90;
	protected double ActiveBalQty=0;
	protected boolean isActive=false;
	protected boolean isInTrade=false;
	protected boolean perMode= false;
	protected int PricePrecision=5;
	protected int QtyPrecision=5;
	protected double PairBalance=0;
	protected int PairId;
	
	public double getPairBalance() {
		return PairBalance;
	}
	public void setPairBalance(double pairBalance) {
		PairBalance = pairBalance;
	}
	public int getQtyPrecision() {
		return QtyPrecision;
	}
	public void setQtyPrecision(int qtyPrecision) {
		QtyPrecision = qtyPrecision;
	}
	public double getPercentProfitTarget() {
		return PercentProfitTarget;
	}
	public void setPercentProfitTarget(double percentProfitTarget) {
		PercentProfitTarget = percentProfitTarget;
	}
	public double getLastOrderPrice() {
		return LastOrderPrice;
	}
	public void setLastOrderPrice(double lastOrderPrice) {
		LastOrderPrice = lastOrderPrice;
	}
	public int getPricePrecision() {
		return PricePrecision;
	}
	public void setPricePrecision(int pricePrecision) {
		PricePrecision = pricePrecision;
	}
	public double getActiveBalQty() {
		return ActiveBalQty;
	}
	public void setActiveBalQty(double activeBalQty) {
		ActiveBalQty = activeBalQty;
	}
	public double getProfitPrice() {
		return ProfitPrice;
	}
	public void setProfitPrice(double profitPrice) {
		ProfitPrice = profitPrice;
	}
	public String getActiveSide() {
		return ActiveSide;
	}
	public void setActiveSide(String activeSide) {
		ActiveSide = activeSide;
	}
	public void setPerMode(boolean perMod) {
		this.perMode = perMod;
	}
	public boolean getPerMode() {
		return perMode;
	}	
	
	
	public double getMinLiqValue() {
		return MinLiqValue;
	}
	public void setMinLiqValue(double minLiqValue) {
		MinLiqValue = minLiqValue;
	}

	public void setIsInTrade(boolean isInTrade) {
		this.isInTrade = isInTrade;
	}
	public boolean getIsInTrade() {
		return isInTrade;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public double getLastTradedPrice() {
		return LastTradedPrice;
	}
	public void setLastTradedPrice(double lastTradedPrice) {
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
	
	public int getLeverage() {
		return Leverage;
	}
	public void setLeverage(int leverage) {
		Leverage = leverage;
	}

	protected ArrayList<TradingZone> BuyTradingSideZones=new ArrayList<TradingZone>();
	protected ArrayList<TradingZone> SellTradingSideZones=new ArrayList<TradingZone>();
	protected org.asynchttpclient.ws.WebSocket WsLiqProcess=null;
	protected org.asynchttpclient.ws.WebSocket WsLastPrice=null;
	protected CompletableFuture<Void> Updater=null;
	protected CompletableFuture<Void> WsCheck=null;
	
	
			


	CompletableFuture<Void> updater=null;
	@SuppressWarnings("unchecked")
	public void splitPozSize() {
		double s=this.getActiveBalQty();
		int i=0;
if(this.ActiveSide.equals("LONG")) {
while(s>0 &&  i<this.BuyTradingSideZones.size()) {

	if(BuyTradingSideZones.get(i).getMaxZoneQty()<s) {
	this.BuyTradingSideZones.get(i).setActiveZoneQty(BuyTradingSideZones.get(i).getMaxZoneQty());}
	else {this.BuyTradingSideZones.get(i).setActiveZoneQty(s);}
	s-=BuyTradingSideZones.get(i).getMaxZoneQty();
	i++;
}
	}
else {
	while(s>0 &&  i<this.SellTradingSideZones.size()) {

		if(SellTradingSideZones.get(i).getMaxZoneQty()<s) {
		this.SellTradingSideZones.get(i).setActiveZoneQty(SellTradingSideZones.get(i).getMaxZoneQty());}
		else {this.SellTradingSideZones.get(i).setActiveZoneQty(s);}
		s-=SellTradingSideZones.get(i).getMaxZoneQty();
		i++;
	}
	
}
	
}		
		
	public void Reset() {
		for(int i=0;i < this.BuyTradingSideZones.size();i++) {
			this.BuyTradingSideZones.get(i).setActiveZoneQty(0);
			
		}
		for(int i=0;i < this.SellTradingSideZones.size();i++) {
			this.SellTradingSideZones.get(i).setActiveZoneQty(0);
			
		}
		
	this.setIsInTrade(false);
	this.setActiveSide("OFF");
	}
	

	
	
	public  CryptoCoin(String nume,double MaxBQty,double perProfitTarget, int lev, boolean perMod,ArrayList<TradingZone> BuyZones, ArrayList<TradingZone> SellZones) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, SQLException, InterruptedException, ExecutionException {
		this.Name=nume.toUpperCase();
		this.MaxBalQty=MaxBQty;
		this.PercentProfitTarget=perProfitTarget;
		this.Leverage=lev;
		this.perMode= perMod;
		this.BuyTradingSideZones=BuyZones;
		this.SellTradingSideZones=SellZones;
BotBinance.dataRequest.GetCoinData(this);
		try{
		this.WsLastPrice=BotBinance.Generator.WsCurrentPrice(this);}
		catch(Exception e) {
			this.WsLastPrice=BotBinance.Generator.WsCurrentPrice(this);}
try{Updater=BotBinance.updaterGenerator.generateCryptoUpdater(this);}
		catch(Exception e) {Updater=BotBinance.updaterGenerator.generateCryptoUpdater(this);}
	System.out.println("A MERS ASDASDASDASD");
		//BotBinance.OrderGenerator.BuyMarketOrderGenerator(5, this.Name);
	}
	
	
	
	
	
	public  CryptoCoin(int pId,String nume,double MaxBQty,double perProfitTarget, int lev, boolean perMod,ArrayList<TradingZone> BuyZones, ArrayList<TradingZone> SellZones) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, SQLException, InterruptedException, ExecutionException {
		this.PairId=pId;
		this.Name=nume.toUpperCase();
		
		this.MaxBalQty=MaxBQty;
		this.PercentProfitTarget=perProfitTarget;
		this.Leverage=lev;
		this.perMode= perMod;
		this.BuyTradingSideZones=BuyZones;
		this.SellTradingSideZones=SellZones;
BotBinance.dataRequest.GetCoinData(this);
		try{
		this.WsLastPrice=BotBinance.Generator.WsCurrentPrice(this);}
		catch(Exception e) {
			this.WsLastPrice=BotBinance.Generator.WsCurrentPrice(this);}
try{Updater=BotBinance.updaterGenerator.generateCryptoUpdater(this);}
		catch(Exception e) {Updater=BotBinance.updaterGenerator.generateCryptoUpdater(this);}
	System.out.println("A MERS ASDASDASDASD");
		//BotBinance.OrderGenerator.BuyMarketOrderGenerator(5, this.Name);
	}
	public  CryptoCoin(int pId,String nume,ArrayList<TradingZone> BuyZones, ArrayList<TradingZone> SellZones) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, SQLException, InterruptedException, ExecutionException {
		this.PairId=pId;
		this.Name=nume.toUpperCase();
		
		
		this.BuyTradingSideZones=BuyZones;
		this.SellTradingSideZones=SellZones;
BotBinance.dataRequest.GetCoinData(this);
		try{
		this.WsLastPrice=BotBinance.Generator.WsCurrentPrice(this);}
		catch(Exception e) {
			this.WsLastPrice=BotBinance.Generator.WsCurrentPrice(this);}
try{Updater=BotBinance.updaterGenerator.generateCryptoUpdater(this);}
		catch(Exception e) {Updater=BotBinance.updaterGenerator.generateCryptoUpdater(this);}
	System.out.println("A MERS ASDASDASDASD");
		//BotBinance.OrderGenerator.BuyMarketOrderGenerator(5, this.Name);
	}
	
	public CryptoCoin(String nume) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, SQLException, InterruptedException, ExecutionException {this.Name=nume;
	BotBinance.dataRequest.GetCoinData(this);
	try{
	this.WsLastPrice=BotBinance.Generator.WsCurrentPrice(this);}
	catch(Exception e) {
		this.WsLastPrice=BotBinance.Generator.WsCurrentPrice(this);}
try{Updater=BotBinance.updaterGenerator.generateCryptoUpdater(this);}
	catch(Exception e) {Updater=BotBinance.updaterGenerator.generateCryptoUpdater(this);}
	}
	
	
	
	TradingZone SearchTradeableZone(String side,double liqPrice){
	
		if(isInTrade == false) {
			if(side.equals("SELL") ) {
				
				for(TradingZone zone : BuyTradingSideZones) {
					if(zone.isPriceInBuyZone(liqPrice,VWAP)) {return zone;}}}
				
			if(side.equals("BUY")) {
					
					for(TradingZone zone : SellTradingSideZones) {
						if(zone.isPriceInSellZone(liqPrice,VWAP)) {return zone;}}}}
				
			
		else {
			
			if(side.equals("SELL") && ActiveSide.equals("LONG") && liqPrice< LastOrderPrice ){

				for(TradingZone zone : BuyTradingSideZones) {
					if(zone.isPriceInBuyZone(liqPrice,ProfitPrice)) {return zone;}}}
			
			if(side.equals("BUY") && ActiveSide.equals("SHORT") && liqPrice > LastOrderPrice){
				
				for(TradingZone zone : SellTradingSideZones) {
					if(zone.isPriceInSellZone(liqPrice,ProfitPrice)) {return zone;}}}}
		
	return null;
	}
	public void StartWsLiquidation(){
		try {
			this.WsLiqProcess=BotBinance.Generator.WsMarketLiq(this);
		} catch (InterruptedException | ExecutionException e) {
			
			e.printStackTrace();
		}
		this.WsCheck=CompletableFuture.runAsync(()-> {
			while(true) {
				
				try {
					
					Thread.sleep(5000);
				
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
			
			if(!this.WsLastPrice.isOpen()) {
				try{System.out.println("Restart WsPice");
					this.WsLastPrice=BotBinance.Generator.WsCurrentPrice(this);}
					catch(Exception e) {
						}
				
			}
			if(!this.WsLiqProcess.isOpen()) {
				System.out.println("Restart WsLiq");
				try{this.StartWsLiquidation();}
				catch(Exception e) {}
				
			}
			}});
		
	}		
	public void StopWsLiquidation() {
		this.WsLiqProcess.removeWebSocketListener(WsGenerator.wsListner);
		this.WsLiqProcess=null;
		this.WsCheck=null;
	}
	
	
	public void addBuyZone(TradingZone t) {
		this.BuyTradingSideZones.add(t);
	}
	public void addSellZone(TradingZone t) {
		this.SellTradingSideZones.add(t);
	}
	public void deleteBuyTradingZone(int i) {
		this.BuyTradingSideZones.remove(i);
	}
	public void deleteSellTradingZone(int i) {
		this.SellTradingSideZones.remove(i);
	}
	public JSONObject toJson() {
		JSONObject rez=new JSONObject();
		rez.put("pair", this.Name);
		rez.put("perMode", this.perMode);
		rez.put("lev",this.Leverage);
		rez.put("perProfitTarget",this.PercentProfitTarget);
		rez.put("maxBalQty",this.MaxBalQty);
		rez.put("minLiqValue",this.MinLiqValue);
		JSONArray BuyZones=new JSONArray();
		for(TradingZone t : BuyTradingSideZones){BuyZones.put(t.toJson());}
		rez.put("BuyTradingZones",BuyZones);
		JSONArray SellZones=new JSONArray();
		for(TradingZone t : SellTradingSideZones){SellZones.put(t.toJson());}
		rez.put("SellTradingZones",SellZones);
		
		return rez;
	}		
		
		
		
	}

