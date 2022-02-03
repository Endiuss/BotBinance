package BotBinance;

import static org.asynchttpclient.Dsl.asyncHttpClient;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Response;

public class OrderGenerator {
	
	/**
	 * 
	 * Contains methods that generate Buy, Sell and TakeProfit request orders.
	 * @param qty
	 * @param pair
	 * @throws BinanceException
	 */


	public void BuyMarketOrderGenerator(double qty, String pair) throws BinanceException {

	
		byte[] encodeKey = BotBinance.SecretKey.getBytes();
	
		/*parameters.put("recvWindow","5000");*/
String time=String.valueOf(System.currentTimeMillis());	
String param="symbol="+pair+"&side=BUY"+"&type=MARKET"+"&quantity="+qty+"&newOrderRespType=RESULT"+"&timestamp="+time;

		
		 
		 
		try {
		
			
			String querry_string = URLEncoder.encode(param,"UTF-8");
			
			 System.out.println(querry_string.toString());
			
			 byte[] encodeParam=param.getBytes("UTF-8");
			 System.out.println(encodeParam.length);
			 String sig=Hex.encodeHexString(HMAC.calcHmacSha256(encodeKey,encodeParam));

			 System.out.println(param);
			 System.out.println(sig);
	







			 URL url=new URL("https://fapi.binance.com/fapi/v1/order?"+param+"&signature="+sig);
				
				
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("POST");
				System.out.println(con.getRequestMethod());
			con.setRequestProperty("X-MBX-APIKEY", BotBinance.ApiKey);
			
			if (con.getResponseCode() == 200) {
			BufferedReader in = new BufferedReader(
					  new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer content = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {
					    content.append(inputLine);
					}
					in.close();
					System.out.println(content.toString());
			}
			else {String msg="";
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			    String strCurrentLine;
			        while ((strCurrentLine = br.readLine()) != null) {
			              msg+=strCurrentLine;
			        }
			        throw new BinanceException(msg);}
		
/*req.addQueryParam("symbol", pair);
req.addQueryParam("side", "buy");
req.addQueryParam("timestamp",time);
req.addQueryParam("type", "market");
req.addQueryParam("quantity", String.valueOf(qty));
req.addQueryParam("recvWindow", "5000");
req.addQueryParam("signature", sig);*/

//Response response = whenResponse.get();






				
		
		
		
		
		} catch (IOException e) {
			
			throw new BinanceException(e);
		
		
		
		}

		

		
		
		
		
		
		
	}
	public void SellMarketOrderGenerator(double qty, String pair) throws BinanceException {
		byte[] encodeKey = BotBinance.SecretKey.getBytes();

		/*parameters.put("recvWindow","5000");*/
String time=String.valueOf(System.currentTimeMillis());	
String param="symbol="+pair+"&side=SELL"+"&type=MARKET"+"&quantity="+qty+"&newOrderRespType=RESULT"+"&timestamp="+time;

		
		 
		 
		try {
		
			
			String querry_string = URLEncoder.encode(param,"UTF-8");
			
			 System.out.println(querry_string.toString());
			
			 byte[] encodeParam=param.getBytes("UTF-8");
			 System.out.println(encodeParam.length);
			 String sig=Hex.encodeHexString(HMAC.calcHmacSha256(encodeKey,encodeParam));

			 System.out.println(param);
			 System.out.println(sig);
	







			 URL url=new URL("https://fapi.binance.com/fapi/v1/order?"+param+"&signature="+sig);
				
				
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("POST");
				System.out.println(con.getRequestMethod());
			con.setRequestProperty("X-MBX-APIKEY", BotBinance.ApiKey);
			
			if (con.getResponseCode() == 200) {
			BufferedReader in = new BufferedReader(
					  new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer content = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {
					    content.append(inputLine);
					}
					in.close();
					System.out.println(content.toString());
			}
			else {String msg=new String("");
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			    String strCurrentLine;
			        while ((strCurrentLine = br.readLine()) != null) {
			              msg+=strCurrentLine;
			        }
			        throw new BinanceException(msg);}
		
/*req.addQueryParam("symbol", pair);
req.addQueryParam("side", "buy");
req.addQueryParam("timestamp",time);
req.addQueryParam("type", "market");
req.addQueryParam("quantity", String.valueOf(qty));
req.addQueryParam("recvWindow", "5000");
req.addQueryParam("signature", sig);*/

//Response response = whenResponse.get();
	
		} catch (IOException e) {
			
			throw new BinanceException(e);
		
		
		
		}

	}
	public static void ChangeLeverage(String pair,int lev) throws BinanceException {
		String time=String.valueOf(System.currentTimeMillis());	
		String param="symbol="+pair+"&leverage="+lev+"&timestamp="+time;
		byte[] encodeKey = BotBinance.SecretKey.getBytes();
		try {
			String querry_string = URLEncoder.encode(param,"UTF-8");
			
			 System.out.println(querry_string.toString());
			
			 byte[] encodeParam=param.getBytes("UTF-8");
			 System.out.println(encodeParam.length);
			 String sig=Hex.encodeHexString(HMAC.calcHmacSha256(encodeKey,encodeParam));

			URL url=new URL("https://fapi.binance.com/fapi/v1/leverage?"+param+"&signature="+sig);
			
			
			
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			System.out.println(con.getRequestMethod());
		con.setRequestProperty("X-MBX-APIKEY", BotBinance.ApiKey);
		
		if (con.getResponseCode() == 200) {
		BufferedReader in = new BufferedReader(
				  new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer content = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
				    content.append(inputLine);
				}
				in.close();
				System.out.println(content.toString());
		}
		else {String msg=new String("");
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
		    String strCurrentLine;
		        while ((strCurrentLine = br.readLine()) != null) {
		              msg+=strCurrentLine;
		        }
		        throw new BinanceException(msg);}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void CancelAllOrdersPair(String pair) throws BinanceException {

		
		byte[] encodeKey = BotBinance.SecretKey.getBytes();

		/*parameters.put("recvWindow","5000");*/
String time=String.valueOf(System.currentTimeMillis());	
String param="symbol="+pair+"&timestamp="+time;

		
		 
		 
		try {
		
			
			String querry_string = URLEncoder.encode(param,"UTF-8");
			
			 System.out.println(querry_string.toString());
			
			 byte[] encodeParam=param.getBytes("UTF-8");
			 System.out.println(encodeParam.length);
			 String sig=Hex.encodeHexString(HMAC.calcHmacSha256(encodeKey,encodeParam));

			 System.out.println(param);
			 System.out.println(sig);
	







			 URL url=new URL("https://fapi.binance.com/fapi/v1/allOpenOrders?"+param+"&signature="+sig);
				
				
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("DELETE");
				System.out.println(con.getRequestMethod());
			con.setRequestProperty("X-MBX-APIKEY", BotBinance.ApiKey);
			
			if (con.getResponseCode() == 200) {
			BufferedReader in = new BufferedReader(
					  new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer content = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {
					    content.append(inputLine);
					}
					in.close();
					System.out.println(content.toString());
			}
			else {String msg="";
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			    String strCurrentLine;
			        while ((strCurrentLine = br.readLine()) != null) {
			              msg+=strCurrentLine;
			        }
			        throw new BinanceException(msg);}

		} catch (IOException e) {
			
			throw new BinanceException(e);
		
		
		
		}
}
	
	
	public void TakeLongProfits(double qty, String pair,double price) throws BinanceException {
		byte[] encodeKey = BotBinance.SecretKey.getBytes();

		/*parameters.put("recvWindow","5000");*/
String time=String.valueOf(System.currentTimeMillis());	
String param="symbol="+pair+"&side=SELL"+"&type=LIMIT"+"&timeInForce=GTC"+"&quantity="+qty+"&reduceOnly=true"+"&price="+price+"&newOrderRespType=RESULT"+"&timestamp="+time;

		
		 
		 
		try {
		
			
			String querry_string = URLEncoder.encode(param,"UTF-8");
			
			 System.out.println(querry_string.toString());
			
			 byte[] encodeParam=param.getBytes("UTF-8");
			 System.out.println(encodeParam.length);
			 String sig=Hex.encodeHexString(HMAC.calcHmacSha256(encodeKey,encodeParam));

			 System.out.println(param);
			 System.out.println(sig);
	

			 URL url=new URL("https://fapi.binance.com/fapi/v1/order?"+param+"&signature="+sig);
				
				
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("POST");
				System.out.println(con.getRequestMethod());
			con.setRequestProperty("X-MBX-APIKEY", BotBinance.ApiKey);
			
			if (con.getResponseCode() == 200) {
			BufferedReader in = new BufferedReader(
					  new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer content = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {
					    content.append(inputLine);
					}
					in.close();
					System.out.println(content.toString());
			}
			else {String msg=new String("");
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			    String strCurrentLine;
			        while ((strCurrentLine = br.readLine()) != null) {
			              msg+=strCurrentLine;
			        }
			        throw new BinanceException(msg);}

		} catch (IOException e) {
			
			throw new BinanceException(e);
}}
	
	
	
	public void TakeShortProfits(double qty, String pair,double price) throws BinanceException {
		byte[] encodeKey = BotBinance.SecretKey.getBytes();

		/*parameters.put("recvWindow","5000");*/
String time=String.valueOf(System.currentTimeMillis());	
String param="symbol="+pair+"&side=BUY"+"&type=LIMIT"+"&timeInForce=GTC"+"&quantity="+qty+"&reduceOnly=true"+"&price="+price+"&newOrderRespType=RESULT"+"&timestamp="+time;

		
		 
		 
		try {
		
			
			String querry_string = URLEncoder.encode(param,"UTF-8");
			
			 System.out.println(querry_string.toString());
			
			 byte[] encodeParam=param.getBytes("UTF-8");
			 System.out.println(encodeParam.length);
			 String sig=Hex.encodeHexString(HMAC.calcHmacSha256(encodeKey,encodeParam));

			 System.out.println(param);
			 System.out.println(sig);
	

			 URL url=new URL("https://fapi.binance.com/fapi/v1/order?"+param+"&signature="+sig);
				
				
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("POST");
				System.out.println(con.getRequestMethod());
			con.setRequestProperty("X-MBX-APIKEY", BotBinance.ApiKey);
			
			if (con.getResponseCode() == 200) {
			BufferedReader in = new BufferedReader(
					  new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer content = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {
					    content.append(inputLine);
					}
					in.close();
					System.out.println(content.toString());
			}
			else {String msg=new String("");
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			    String strCurrentLine;
			        while ((strCurrentLine = br.readLine()) != null) {
			              msg+=strCurrentLine;
			        }
			        throw new BinanceException(msg);}

		} catch (IOException e) {
			
			throw new BinanceException(e);
}}
	
	


}
