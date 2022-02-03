package BotBinance;

import static org.asynchttpclient.Dsl.asyncHttpClient;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Response;
import org.json.JSONArray;
import org.json.JSONObject;

public class UpdatersGenerator {

	public CompletableFuture<Void>  generateCryptoUpdater(CryptoCoin c){
		CompletableFuture<Void> updater=CompletableFuture.runAsync(()-> {
			
			/**
			 * Updates the 1 minute date on Database
			 * 
			 */
			while(true) {
				
				try {
					
					Thread.sleep(60000);
					System.out.println("wait");
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				
				
				
			Map<String, String> parameters = new HashMap<String,String>();
			parameters.put("symbol", c.getName().toUpperCase());
			parameters.put("interval", "1m");
			parameters.put("startTime", String.valueOf(c.getLastCandelCloseTime()));
			parameters.put("limit","99");
			try {
				
				
				String url="https://fapi.binance.com/fapi/v1/klines"+"?"+ParameterStringBuilder.getParamsString(parameters);
			AsyncHttpClient asyncHttpClient = asyncHttpClient();

			ListenableFuture<Response> whenResponse = asyncHttpClient.prepareGet(url).addHeader("X-MBX-APIKEY", BotBinance.ApiKey).execute();
			
			//Response response = whenResponse.get();
			System.out.println(whenResponse.get().getResponseBody());
			
		
			
			

			
					String inputLine;
					StringBuffer content = new StringBuffer();
					content.append("{ \"candels\":");
					content.append(whenResponse.get().getResponseBody());			
					
					content.append("}");
					JSONObject js=new JSONObject(content.toString());
			System.out.println(js.toString());
			Connection conn=DBConnection.CreateConnection();
			Statement stmt = conn.createStatement();
			String DbPair=c.getName();
			DbPair.replaceAll("[^A-Za-z]","");
			String InsertQuery="INSERT INTO "+DbPair+" ([open], [high], [low], [close], [volume], [hlc3], [openTime], [closeTime]) "
                    + "VALUES ";

			JSONArray jarr=js.getJSONArray("candels");
			int nrOfDatas=0;
			
			double actualNumitor = c.getNumitorVWAP();
			double actualNumarator=c.getNumaratorVWAP();
			
			if (jarr != null) {   
			   
			  
			    for (int i=0;i<jarr.length()-1;i++){   
			    	nrOfDatas++;
			          JSONArray candle=(JSONArray)jarr.get(i);
			          double open=Double.parseDouble(candle.getString(1));
			          double high=Double.parseDouble(candle.getString(2));
			          double low=Double.parseDouble(candle.getString(3));
			          double close=Double.parseDouble(candle.getString(4));
			          double volume=Double.parseDouble(candle.getString(7));
			          double hlc3=(high+low+close)/3;
			          long openTime=(Long) candle.get(0);
			          long closeTime=(Long) candle.get(6);
			          actualNumarator+=hlc3*volume;
			          actualNumitor+=volume;
			        		  
			          InsertQuery+="("+open+","+high+","+low+","+close+","+volume+","+hlc3+","+openTime+","+closeTime+"),";
			          
			          if(nrOfDatas==400) {
			        	  
			        	  InsertQuery=InsertQuery.substring(0, InsertQuery.length() - 1);

			        	  InsertQuery+=";";
			        	  //System.out.println(InsertQuery);
			        	  stmt.execute(InsertQuery);
			        	   InsertQuery="INSERT INTO "+DbPair+" ([open], [high], [low], [close], [volume], [hlc3], [openTime], [closeTime]) "
			                      + "VALUES ";
			nrOfDatas=0;
			
			          }
			
			}}
			
			c.setNumaratorVWAP(actualNumarator);
			c.setNumitorVWAP(actualNumitor);
			c.setVWAP(actualNumarator/actualNumitor);
			if(jarr != null&&jarr.length()-2>=0) {
			JSONArray candle=(JSONArray)jarr.get(jarr.length()-2);
			c.setLastCandelCloseTime((Long) candle.get(6));}
			conn.close();
			System.out.println("VWAP ="+c.getVWAP());
			
			}
			 catch (UnsupportedEncodingException | InterruptedException | ExecutionException | InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
				e.printStackTrace();
				System.out.println(e);
				
			}
		
				
			
					
				
			}
			}
		);
	return updater;	
	}
	
	
	
}
