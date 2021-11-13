package BotBinance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class BinanceDataRequest {
	BinanceDataRequest(){};
public static void GetCoinData(CryptoCoin c) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
	Connection conn=DBConnection.CreateConnection();
   Statement stmt= conn.createStatement();
   String Pair=c.Name;
   String LastCandel="IF (EXISTS (SELECT *"
   		+ "   FROM INFORMATION_SCHEMA.TABLES"
   		+ "   WHERE TABLE_SCHEMA = 'dbo'"
   		+ "   AND TABLE_NAME = '"+Pair+"'))"
   		+ "   BEGIN"
   		+ "      SELECT MAX(closeTime) AS 'closeTime' FROM "+Pair+""
   		+ "   END;"
   		+ "ELSE"
   		+ "   BEGIN"
   		+ " CREATE TABLE ["+Pair+"]("
   		+ " [Open] numeric(30,20),"
   		+ " [High] numeric(30,20),"
   		+ " [Low] numeric(30,20),"
   		+ " [Close] numeric(30,20),"
   		+ " [Volume] numeric(30,20),"
   		+ " [HLC3] numeric(30,20),"
   		+ " [openTime] BIGINT,"
   		+ " [closeTime] BIGINT"
   		+ ");"
   		+ "   END;";
   long lstime=-1;
  System.out.println(LastCandel);
 try {
  ResultSet dbResponse= stmt.executeQuery(LastCandel);
  ResultSetMetaData resultSetMetaData = dbResponse.getMetaData();
 if(dbResponse != null) {
	  while (dbResponse.next()) {
		  lstime = dbResponse.getLong("closeTime");
		  if(lstime>0) {System.out.println(lstime);
		  }
	 }}}
 catch(Exception e){System.out.println(e);}
 long startTimeRequest=BotBinance.startTime;
 
 if(lstime<BotBinance.startTime) {String delsql="DELETE FROM "+Pair+";";
 stmt.execute(delsql);
 }
 else {startTimeRequest=lstime;}
 System.out.println("LAST TIME: "+startTimeRequest);
 long maxLimit= ((System.currentTimeMillis()-startTimeRequest)/60000)+50;
	  System.out.println((System.currentTimeMillis()-startTimeRequest)/60000);
 // System.out.println( stmt.executeQuery(LastCandel));
  // double open, double high, double low, double close, double volume,double hLC3 ,long openTime,long closeTime
	
	
	Map<String, String> parameters = new HashMap<String,String>();
	String stringStartTime=String.valueOf(startTimeRequest);
	
	System.out.println(stringStartTime);
	parameters.put("symbol", Pair.toUpperCase());
	parameters.put("interval", "1m");
	parameters.put("startTime", stringStartTime);
	parameters.put("limit","1440");//String.valueOf(maxLimit)
	/*String rawString =BotBinance.SecretKey;
	byte[] encodeKey = StringUtils.getBytesUtf8(rawString);
	 String param="?";
	 param+=ParameterStringBuilder.getParamsString(parameters);
	 String querry_string= URLEncoder.encode(param,"UTF-8");

	 byte[] encodeParam=StringUtils.getBytesUtf8(querry_string);
	 String sig=Hex.encodeHexString(HMAC.calcHmacSha256(encodeKey, encodeParam));*/
	// parameters.put("signature",sig);
	URL url=new URL("https://fapi.binance.com/fapi/v1/klines"+"?"+ParameterStringBuilder.getParamsString(parameters));
HttpURLConnection con = (HttpURLConnection) url.openConnection();
	
	con.setRequestMethod("GET");
	
con.setRequestProperty("X-MBX-APIKEY", BotBinance.ApiKey);
	int status = con.getResponseCode();
	BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	

	
			String inputLine;
			StringBuffer content = new StringBuffer();
			content.append("{ \"candels\":");
			content.append(in.readLine());			
			in.close();
			content.append("}");
			JSONObject js=new JSONObject(content.toString());
			  ArrayList<Object> response = new ArrayList<Object>();  
			  String InsertQuery="INSERT INTO "+Pair+" ([open], [high], [low], [close], [volume], [hlc3], [openTime], [closeTime]) "
                        + "VALUES ";

JSONArray jarr=js.getJSONArray("candels");
int nrOfDatas=0;
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
          InsertQuery+="("+open+","+high+","+low+","+close+","+volume+","+hlc3+","+openTime+","+closeTime+"),";
          
          if(nrOfDatas==400) {
        	  
        	  InsertQuery=InsertQuery.substring(0, InsertQuery.length() - 1);

        	  InsertQuery+=";";
        	  //System.out.println(InsertQuery);
        	  stmt.execute(InsertQuery);
        	   InsertQuery="INSERT INTO "+Pair+" ([open], [high], [low], [close], [volume], [hlc3], [openTime], [closeTime]) "
                      + "VALUES ";
nrOfDatas=0;
          }
    
    
          //Candlestick c=new Candlestick(candel[1].,);
        	
          //double open, double high, double low, double close, double volume,double hLC3 ,long openTime,long closeTime
        	
    } }
if(nrOfDatas>0) {
InsertQuery=InsertQuery.substring(0, InsertQuery.length() - 1);

InsertQuery+=";";
//System.out.println(InsertQuery);
stmt.execute(InsertQuery);}
String getVWAP="SELECT TOP 1  SUM(HLC3*Volume) OVER(ORDER BY closeTime) AS CummulativeNumarator,SUM(Volume) OVER(ORDER BY closeTime) AS CummulativeNumitor"
		+ " FROM "+Pair
		+ " Order BY CummulativeNumarator DESC";
System.out.println(getVWAP);
ResultSet vwapResponse=stmt.executeQuery(getVWAP);
double vwapNumarator=0;
double vwapNumitor=0;		
if(vwapResponse != null) {
	  while (vwapResponse.next()) {
		  vwapNumarator = vwapResponse.getDouble("CummulativeNumarator");
		  vwapNumitor = vwapResponse.getDouble("CummulativeNumitor");
		  if(lstime>0) {System.out.println(lstime);
		  }
	 }}
JSONArray candle=(JSONArray)jarr.get(jarr.length()-2);
c.setLastCandelCloseTime((Long) candle.get(6));
c.setNumaratorVWAP(vwapNumarator);
c.setNumitorVWAP(vwapNumitor);
c.setVWAP(vwapNumarator/vwapNumitor);
System.out.println(vwapNumarator/vwapNumitor);

conn.close();

	     
//System.out.println(result.toString());
	
	
}
}
