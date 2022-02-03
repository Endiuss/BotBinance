package BotBinance;
import java.io.BufferedReader; 


import org.json.*;

import com.mysql.cj.xdevapi.JsonArray;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.*;
import java.util.concurrent.ExecutionException;
import java.util.HashMap;
import java.util.Map;

import org.asynchttpclient.Dsl;
import org.asynchttpclient.ws.*;
import org.asynchttpclient.ws.WebSocket;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
public class WsGenerator {
	/**************************************************************************************************************************************************/
	
		public static WebSocketUpgradeHandler.Builder upgradeHandlerBuilderMarketLiq = new WebSocketUpgradeHandler.Builder();
		public static WebSocketListener wsListner=new WebSocketListener() {
			public void onBinaryFrame(byte[] payload, boolean finalFragment, int rsv){
				System.out.println("payload"+payload);
			}
	      public void onOpen(WebSocket websocket) {
	    	System.out.println("WsLiqStart");
	    	  }

	   
	      public void onClose(WebSocket websocket, int code, String reason) {
	    	
	    	  System.out.println("Close:"+reason);
	      }
	      
	      public void  onTextFrame(String payload, boolean finalFragment, int rsv) {
	    	  JSONObject js=new JSONObject(payload);
	    
	    	  JSONObject data=js.getJSONObject("o");
	    	  String pair=data.getString("s");
	    	  String side=data.getString("S");
	    	  CryptoCoin c=BotBinance.allCrypto.get(pair);
	    	  double ap=data.getDouble("ap");
	    	  double q=data.getDouble("q");
	    	  double LiqValue=ap*q;
	    	  if(LiqValue>=c.getMinLiqValue()) {
	    		  if((BotBinance.nrActivePairs <= BotBinance.MaxActivePairs) || c.getIsInTrade()==true) {
	    			  TradingZone t = null;
	    			  t= c.SearchTradeableZone(side,ap);
	    	System.out.println("ok1");
	    		if(t != null) {
	    			System.out.println("ok2");
	    			double pairqty=0;
	    			double usdqty=0;
	    			  if(c.getPerMode()) {pairqty=BigDecimal.valueOf(((BotBinance.AccBal*t.getOrderZoneQty())/100)/c.getLastTradedPrice()).setScale(c.getQtyPrecision(),RoundingMode.HALF_UP).doubleValue();
	    			  					usdqty=(BotBinance.AccBal*t.getOrderZoneQty())/100; }
	    			  else {pairqty=BigDecimal.valueOf((t.getOrderZoneQty())/c.getLastTradedPrice()).setScale(c.getQtyPrecision(),RoundingMode.HALF_UP).doubleValue();
	    			  		usdqty=t.getOrderZoneQty();}
	    			  System.out.println(pairqty);
	    		
	    			 if(side.equals("SELL")) { if( !c.getActiveSide().equals("SHORT")){try{BotBinance.OrderGenerator.BuyMarketOrderGenerator(pairqty, pair);
	    			 						System.out.println("A ajuns pana aici LONG");
	    			 						if(!c.isInTrade) {
	    			 						BotBinance.pairsInTrade.put(c.getName(), c);
	    			 						c.setIsInTrade(true);
	    			 						c.setActiveSide("LONG");}
	    			 						c.setActiveBalQty(c.getActiveBalQty()+usdqty );
	    			 						t.setActiveZoneQty(t.getActiveZoneQty()+usdqty);}
	    			 					
	    			 						catch(Exception e) {
	    			 							System.out.println(e);
	    			 							}
	    			 }}
	    			 
	    			 
	    			 
	    			 else {try {
						if(!c.getActiveSide().equals("LONG")) {BotBinance.OrderGenerator.SellMarketOrderGenerator(pairqty, pair);
																
						System.out.println("A ajuns pana aici Short");
 						if(!c.isInTrade) {
 						BotBinance.pairsInTrade.put(c.getName(), c);
 						c.setIsInTrade(true);
 						c.setActiveSide("SHORT");}
 						c.setActiveBalQty(c.getActiveBalQty()+usdqty );
 						t.setActiveZoneQty(t.getActiveZoneQty()+usdqty);}}
					 catch (Exception e) {
						 System.out.println(e);		}}
	    		  }
	    		
	    		  }
	      
	    	  
	     
	      
	      }
	    	  System.out.println("Liq "+pair+" "+LiqValue+" "+side+" VWAP: "+c.getVWAP());}
	    
	      public void onError(Throwable t) {
	    	  System.out.println("Error wsHandlerMarketLiq");
	    	  System.out.println(t);
	    	  
	      }
	      public void onPingFrame(byte[] payload){}
			public void onPongFrame(byte[] payload) {}
	  };
		 public static WebSocketUpgradeHandler wsHandlerMarketLiq  = upgradeHandlerBuilderMarketLiq.addWebSocketListener(wsListner
			
				 ).build();
	/**************************************************************************************************************************************************/
		 
		protected WebSocketUpgradeHandler.Builder upgradeHandlerBuilderAccStatus = new WebSocketUpgradeHandler.Builder();
		protected WebSocketUpgradeHandler wsHandlerAccStatus  = upgradeHandlerBuilderAccStatus
		  .addWebSocketListener(new WebSocketListener() {
			  
			  public void onPingFrame(byte[] payload){}
				public void onPongFrame(byte[] payload) {}
			  public void onBinaryFrame(byte[] payload, boolean finalFragment, int rsv){
					System.out.println("payload"+payload);
				}
		      public void onOpen(WebSocket websocket) {
		    	
		    	  }

		   
		      public void onClose(WebSocket websocket, int code, String reason) {
		        
		    	  System.out.println("Close: "+reason);
		      }
		      
		      public void  onTextFrame(java.lang.String payload, boolean finalFragment, int rsv) {
		    
		      JSONObject js=new JSONObject(payload);
		      
		      
		      if(js.getString("e").equals("ORDER_TRADE_UPDATE")) {
		    	  System.out.println(payload);
		    	JSONObject o =js.getJSONObject("o");
		    	if(BotBinance.pairsInTrade.containsKey(o.getString("s"))) {
		    	
		    		if(o.getString("X").equals("FILLED")) {
		    			CryptoCoin c= BotBinance.pairsInTrade.get(o.getString("s"));
		    			if((o.getString("S").equals("SELL")&&c.getActiveSide().equals("SHORT"))||(o.getString("S").equals("BUY")&&c.getActiveSide().equals("LONG"))) {
		    				try {
								BotBinance.OrderGenerator.CancelAllOrdersPair(c.getName());
							} catch (BinanceException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    				c.setLastOrderPrice(o.getDouble("ap"));
		    				try {
		    				if(c.getActiveSide().equals("LONG")) {
								double price=BigDecimal.valueOf((c.getProfitPrice()+(c.getProfitPrice()*c.getPercentProfitTarget()/100))).setScale(c.getPricePrecision(),RoundingMode.HALF_UP).doubleValue();
								BotBinance.OrderGenerator.TakeLongProfits(Math.abs(c.getPairBalance()),c.getName(),price);}
							if(c.getActiveSide().equals("SHORT")) {
								double price=BigDecimal.valueOf(c.getProfitPrice()-(c.getProfitPrice()*c.getPercentProfitTarget()/100)).setScale(c.getPricePrecision(),RoundingMode.HALF_UP).doubleValue();
								BotBinance.OrderGenerator.TakeShortProfits(Math.abs(c.getPairBalance()),c.getName(),price);}
		    				}
		    				catch(Exception e) {
		    					e.printStackTrace();
		    				}
		    				
		    				
		    			}
		    		
		    	}
		    	
		    	  
		    	  
		      }}
		      
		      
		      
		      
		      if(js.getString("e").equals("ACCOUNT_UPDATE")) {
		    	  System.out.println(payload);
		    	 JSONObject a=js.getJSONObject("a");
		    	  JSONArray B=a.getJSONArray("B");
		    	  for(int i=0;i<B.length();i++) {
		    		  if(B.getJSONObject(i).getString("a").equals("USDT")) {
		    			  BotBinance.AccBal=B.getJSONObject(i).getDouble("wb");
		    		 break; }
		    	  }
		    	  
		    	  JSONArray P=a.getJSONArray("P");
		    	  for(int i=0;i<P.length();i++) {
		    		  
		    		  if(BotBinance.allCrypto.containsKey(P.getJSONObject(i).getString("s"))) {
		    			  CryptoCoin c=BotBinance.allCrypto.get(P.getJSONObject(i).getString("s"));
		    			  c.setProfitPrice(P.getJSONObject(i).getDouble("ep"));
		    			  if(P.getJSONObject(i).getDouble("pa")==0) {
		    				  if(BotBinance.pairsInTrade.containsKey(c.getName())) {BotBinance.pairsInTrade.remove(c.getName());
		    				  														 c.Reset();
		    				  														try {
																						BotBinance.OrderGenerator.CancelAllOrdersPair(c.getName());
																					} catch (BinanceException e) {
																						// TODO Auto-generated catch block
																						e.printStackTrace();
																					}}
		    				  
		    			  }
		    			  if(P.getJSONObject(i).getDouble("pa")>0) {if(!BotBinance.pairsInTrade.containsKey(c.getName())) {BotBinance.pairsInTrade.put(c.getName(), c);}
		    				  										c.setActiveSide("LONG");
		    				  										c.setPairBalance(P.getJSONObject(i).getDouble("pa"));
		    				  										c.setActiveBalQty(P.getJSONObject(i).getDouble("pa")*P.getJSONObject(i).getDouble("ep"));}
		    			  else {if(P.getJSONObject(i).getDouble("pa")<0) {if(!BotBinance.pairsInTrade.containsKey(c.getName())) {BotBinance.pairsInTrade.put(c.getName(), c);}
		    				  c.setActiveSide("SHORT");
		    				  c.setPairBalance(P.getJSONObject(i).getDouble("pa")*(-1));
		    				  c.setActiveBalQty(P.getJSONObject(i).getDouble("pa")*(-1)*P.getJSONObject(i).getDouble("ep"));}}
		    			  
		    		  }
		    	  }
		    	  
		    	  
		    	  
		    	  
		    	
		      }
		      }
		    
		      public void onError(Throwable t) {
		    	  System.out.println("Error wsHandlerAccStatus");
		    	  System.out.println(t);
		      }
		      }).build();
		/**************************************************************************************************************************************************/	
		protected WebSocketUpgradeHandler.Builder upgradeHandlerBuilderPrice = new WebSocketUpgradeHandler.Builder();
		 protected WebSocketUpgradeHandler wsHandlerPrice = upgradeHandlerBuilderPrice.addWebSocketListener(new WebSocketListener() {
			  public void onPingFrame(byte[] payload){}
				public void onPongFrame(byte[] payload) {}
			
		      public void onOpen(WebSocket websocket) {
		    	System.out.println("Start Price Ws");
		    	  }

		   
		      public void onClose(WebSocket websocket, int code, String reason) {
		          
		    	  System.out.println("Close: "+reason);
		      }
		      
		      public void  onTextFrame(String payload, boolean finalFragment, int rsv) {
		    	  
		    	  JSONObject js=new JSONObject(payload);
		    	  String pair = js.getString("s");
		   
		    	  CryptoCoin c=BotBinance.allCrypto.get(pair);
		    	  JSONObject data=js.getJSONObject("k");
		    	  double price = data.getDouble("c");
		    	  c.setLastTradedPrice(price);
		    	  
		    	  
		    	  
		    	  
		    	  
		    	 }
		    
		      public void onError(Throwable t) {
		    	  System.out.println("Error wsHandlerPrice");
		    	  System.out.println(t);
		      }
		  }).build();
		
		/**************************************************************************************************************************************************/
		
		public org.asynchttpclient.ws.WebSocket WsMarketLiq(CryptoCoin pair) throws InterruptedException, ExecutionException{
		/**
		 * Initiate the MarketLiquidation websocket.It receives liquidation feed signals and compare the liquidation values with pair parameters and it make the buy/sell/stay decision!
		 * 
		 * 	
		 */
			 org.asynchttpclient.ws.WebSocket webSocketClient = Dsl.asyncHttpClient()
					  .prepareGet("wss://fstream3.binance.com/ws/"+pair.getName().toLowerCase()+"@forceOrder").
					  

						
					  execute(wsHandlerMarketLiq)
					  .get();
			 return webSocketClient;
			
		}
	/**************************************************************************************************************************************************/
		
		public org.asynchttpclient.ws.WebSocket WsCurrentPrice(CryptoCoin pair) throws InterruptedException, ExecutionException{
			/**
			 * Initiate the WEbsocket that receives current price of the pair and update it in the CryptoCoin object.
			 */
			 org.asynchttpclient.ws.WebSocket webSocketClient = Dsl.asyncHttpClient().
					  prepareGet("wss://fstream3.binance.com/ws/"+pair.getName().toLowerCase()+"@kline_1m")

						
					  .execute(wsHandlerPrice)
					  .get();
			 return webSocketClient;
			 
		}
		
	/**************************************************************************************************************************************************/
public  org.asynchttpclient.ws.WebSocket WsAccStatus(String ListenKey) throws InterruptedException, ExecutionException, IOException {
	/**
	 * Initiate the Websocket that listen to AccountListeKey and receives Account changes.
	 * 
	 */
	String x = "wss://fstream.binance.com/ws/"+ListenKey;
	 
	 System.out.println(x);

			 org.asynchttpclient.ws.WebSocket webSocketClient = Dsl.asyncHttpClient()
					  .prepareGet(x).
					  execute(wsHandlerAccStatus)
					  .get();
			 return webSocketClient;
			
		}
		
	}

