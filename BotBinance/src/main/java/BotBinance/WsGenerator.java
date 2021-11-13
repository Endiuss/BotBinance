package BotBinance;
import java.io.BufferedReader;

import org.json.*;

import com.fasterxml.jackson.core.JsonParser;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Thread.State;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.*;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.asynchttpclient.Dsl;
import org.asynchttpclient.ws.*;
import org.asynchttpclient.ws.WebSocket;
import org.asynchttpclient.AsyncHttpClient;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.asynchttpclient.*;
import java.lang.Object;
import java.net.URLConnection;
import java.net.HttpURLConnection;
public class WsGenerator {
	
	
		WebSocketUpgradeHandler.Builder upgradeHandlerBuilderMarketLiq = new WebSocketUpgradeHandler.Builder();
		 WebSocketUpgradeHandler wsHandlerMarketLiq  = upgradeHandlerBuilderMarketLiq.addWebSocketListener(new WebSocketListener() {
	
		      public void onOpen(WebSocket websocket) {
		    	
		    	  }

		   
		      public void onClose(WebSocket websocket, int code, String reason) {
		          
		    	  System.out.print("nu ok");
		      }
		      
		      public void  onTextFrame(String payload, boolean finalFragment, int rsv) {
		      JSONObject js=new JSONObject(payload);
		    
		      JSONObject data=js.getJSONObject("o");
		      String pair=data.getString("s");
		      String side=data.getString("S");
		      CryptoCoin c=BotBinance.allCrypto.get(pair);
		      
		      
		      System.out.println(js.toString());
		     
		      
		      }
		    
		      public void onError(Throwable t) {
		    	  System.out.print("nu ok:  "+t);
		      }
		  }).build();
	
		 
		WebSocketUpgradeHandler.Builder upgradeHandlerBuilderAccStatus = new WebSocketUpgradeHandler.Builder();
		WebSocketUpgradeHandler wsHandlerAccStatus  = upgradeHandlerBuilderAccStatus
		  .addWebSocketListener(new WebSocketListener() {
	
		      public void onOpen(WebSocket websocket) {
		    	
		    	  }

		   
		      public void onClose(WebSocket websocket, int code, String reason) {
		        
		    	  System.out.print("nu ok");
		      }
		      
		      public void  onTextFrame(java.lang.String payload, boolean finalFragment, int rsv) {System.out.println(payload);
		    
		      
		      }
		    
		      public void onError(Throwable t) {
		          
		      }
		  }).build();
		
		
		
		
		
		public org.asynchttpclient.ws.WebSocket WsMarketLiq(CryptoCoin pair) throws InterruptedException, ExecutionException{
			 org.asynchttpclient.ws.WebSocket webSocketClient = Dsl.asyncHttpClient()
					  .prepareGet("wss://fstream3.binance.com/ws/"+pair.getName()+"@forceOrder")

						
					  .execute(wsHandlerMarketLiq)
					  .get();
			 return webSocketClient;
			
		}
public  org.asynchttpclient.ws.WebSocket WsAccStatus(String string) throws InterruptedException, ExecutionException, IOException {
	String rawString =BotBinance.SecretKey;
	byte[] encodeKey = StringUtils.getBytesUtf8(rawString);
	 String param="?";
	 String querry_string= URLEncoder.encode(param,"UTF-8");

	 byte[] encodeParam=StringUtils.getBytesUtf8(querry_string);

	
	String sig=Hex.encodeHexString(HMAC.calcHmacSha256(encodeKey, encodeParam));
	Map<String, String> parameters = new HashMap<String,String>();
	parameters.put("signature", sig);
	HttpClient client = HttpClient.newHttpClient();
	
	

		URL url=new URL("https://fapi.binance.com/fapi/v1/listenKey"+"?"+ParameterStringBuilder.getParamsString(parameters));
	System.out.println(url);
	
	HttpURLConnection con = (HttpURLConnection) url.openConnection();
	
	con.setRequestMethod("POST");
con.setRequestProperty("X-MBX-APIKEY", BotBinance.ApiKey);
	int status = con.getResponseCode();
	BufferedReader in = new BufferedReader(
			  new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
			    content.append(inputLine);
			}
			in.close();


	 //System.out.println(content.toString());
	
	     JSONObject resp = new JSONObject(content.toString());

	   
	
	 String ListenKey= resp.toMap().get("listenKey").toString();
	String x = "wss://fstream.binance.com/ws/"+ListenKey;
	 
	 System.out.println(x);
	
			 org.asynchttpclient.ws.WebSocket webSocketClient = Dsl.asyncHttpClient()
					  .prepareGet(x)
					  .execute(wsHandlerMarketLiq)
					  .get();
			 return webSocketClient;
			
		}
		
	}

