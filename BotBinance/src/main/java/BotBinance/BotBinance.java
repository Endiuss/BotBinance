package BotBinance;
import BotBinance.WsGenerator;



import static org.asynchttpclient.Dsl.asyncHttpClient;

import java.io.*;
import java.lang.Thread.State;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.*;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

import org.asynchttpclient.ws.*;
import org.asynchttpclient.ws.WebSocket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;
import org.json.JSONTokener;

import com.mysql.cj.xdevapi.JsonArray;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.asynchttpclient.*;


public class BotBinance {

	
	static boolean BotStatus=false;
	static String ApiKey = "ecdyk5YRfnFIfdgR9Px5wW2lLTK6ztEIJPYt1qDoREZZHLaF2UjhHgWkCshRv3uP";
	static String SecretKey="lqPOxrhGoCGdlH50u70XT5klnx4ENzpUq428DKRTItkievXsHdbBSiuIW0S3Y0Ic";
	static Map<String, CryptoCoin> allCrypto = new HashMap<String, CryptoCoin>();
	static Map<String, CryptoCoin>  pairsInTrade= new HashMap<String, CryptoCoin>();
	static UpdatersGenerator updaterGenerator=new UpdatersGenerator();
	static long startTime;
	static BinanceDataRequest dataRequest=new BinanceDataRequest();
	static WsGenerator Generator=new WsGenerator();
	static int nrActivePairs=0;
	static int MaxActivePairs=0;
	static double AccBal=0;
	static double MaxActiveBalance=0;
	static CompletableFuture<Void> checkPositions=null;
	static OrderGenerator OrderGenerator=new OrderGenerator();
	static JSONObject Settings=new JSONObject();
	static String ListenKey=null;
	static org.asynchttpclient.ws.WebSocket  AccStatusWs=null;
	
	static CryptoCoin selectedPair=null;
	@SuppressWarnings("unused")
	public static void SaveSettings() {
		/**
		 * Save settings into Settings.json
		 * 
		 */
	
		PrintWriter writer;
		try {
			writer = new PrintWriter("Settings.json", "UTF-8");
			writer.print(Settings.toString());
			
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			SaveSettings();
			e.printStackTrace();
		}
		
		
	}
	
	
	/*
	public static void SqlSettings() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException, InterruptedException, ExecutionException {
		String SelectPair="SELECT * FROM [PairsSettings] WHERE user_id = "+Main.CurrentUser;
		Statement stmt= DBConnection.CreateConnection().createStatement();
    	ResultSet dbResponse= stmt.executeQuery(SelectPair);
		 
		  if(dbResponse != null) {
			  while (dbResponse.next()) {
				  int pid = dbResponse.getInt("pair_id");
				  String PairName =dbResponse.getString("PairName");
				  double ProfitTarget= dbResponse.getDouble("ProfitTarget");
				  System.out.println("PER PROFTI TARGET :"+ProfitTarget);
				  int LiqVal =dbResponse.getInt("LiqVal");
				  int Leverage = dbResponse.getInt("Lev");
				  int per =dbResponse.getInt("PerMode");
				  boolean percentageMode;
				  if(per==1) { percentageMode=true;}
				  else {percentageMode=false;}
				double MaxBalQty=dbResponse.getDouble("MaxBalQty");
				
				ArrayList<TradingZone> BuyTradingSideZones=new ArrayList<TradingZone>();
				ArrayList<TradingZone> SellTradingSideZones=new ArrayList<TradingZone>();
				String SelectBuyZones="SELECT [zone_id] AS ZoneID , [orderZoneQty] AS OrdZQty, [left] AS Lf , [right] AS Ri , [maxZoneQty] AS mZQty FROM [BuyTradingZones] WHERE pair_id = "+pid;
				String SelectSellZones="SELECT * FROM [SellTradingZones] WHERE pair_id= "+pid;
				Statement stmt2= DBConnection.CreateConnection().createStatement();
		    	ResultSet dbResponse2= stmt2.executeQuery(SelectBuyZones);
		    	if(dbResponse2 != null) {
					  while (dbResponse2.next()) {
						  int ZoneID=dbResponse2.getInt("ZoneID");
						  double orderZoneQty=dbResponse2.getDouble("OrdZQty");
						  double left=dbResponse2.getDouble("Lf");
						  double right=dbResponse2.getDouble("Ri");
						  double MaxZoneQty=dbResponse2.getDouble("mZQty");	  
						  TradingZone t = new TradingZone(ZoneID,left,right,orderZoneQty,MaxZoneQty);
						  BuyTradingSideZones.add(t);
					  }}
		    	
		    	Statement stmt3= DBConnection.CreateConnection().createStatement();
		    	ResultSet dbResponse3= stmt3.executeQuery(SelectSellZones);
		    	if(dbResponse3 != null) {
					  while (dbResponse3.next()) {
						  int ZoneID=dbResponse3.getInt("zone_id");
						  double orderZoneQty=dbResponse3.getDouble("orderZoneQty");
						  double left=dbResponse3.getDouble("left");
						  double right=dbResponse3.getDouble("right");
						  double MaxZoneQty=dbResponse3.getDouble("maxZoneQty");	  
						  TradingZone t = new TradingZone(ZoneID,left,right,orderZoneQty,MaxZoneQty);
						  SellTradingSideZones.add(t);
					  }}
		    	allCrypto.put(PairName, new CryptoCoin(pid,PairName,MaxBalQty,ProfitTarget,Leverage,percentageMode,BuyTradingSideZones,SellTradingSideZones));
		    	
			 }}
		
		
	}*/
	
	public static void SetPairsPricePrecision() {
		/**
		 * Request Price Precision and Quantity precission for all pairs
		 * 
		 */
		try {
			JSONArray symbolList=BinanceDataRequest.GetPrecision();
			for(int i=0;i<symbolList.length();i++) {
				JSONObject symbol=symbolList.getJSONObject(i);
				if(allCrypto.containsKey(symbol.getString("symbol"))) {
					allCrypto.get(symbol.getString("symbol")).setQtyPrecision(symbol.getInt("quantityPrecision"));
					allCrypto.get(symbol.getString("symbol")).setPricePrecision(symbol.getInt("pricePrecision"));
				}
			}
			
		} catch (JSONException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void ReadSettings() throws FileNotFoundException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		/**
		 * Read settings from Settings.json
		 * 
		 */
		String resourceName = "Settings.json";
		 File initialFile = new File("Settings.json");
        InputStream is =new FileInputStream(initialFile); ;
        JSONTokener tokener = new JSONTokener(is);
        JSONObject settings = new JSONObject(tokener);
   int PairId=1;
        MaxActivePairs = settings.getInt("MaxActivePairs");
        JSONArray arrpairs=settings.getJSONArray("pairs");
        for(int i=0;i<arrpairs.length();i++) {
        	
        	JSONObject pair=arrpairs.getJSONObject(i);
        	 /* String scope="SELECT TOP 1 pair_id FROM PairsSettings ORDER BY pair_id DESC";
        	Statement stmt= DBConnection.CreateConnection().createStatement();
        	ResultSet dbResponse= stmt.executeQuery(scope);
			 
			  if(dbResponse != null) {
				  while (dbResponse.next()) {
					  PairId = dbResponse.getInt("pair_id")+1;
					
					
				
					
					
				 }}
				  else {PairId=1;}
        	*/
        	
        	
        	
        	
        	JSONArray BuyZones=pair.getJSONArray("BuyTradingZones");
        	ArrayList<TradingZone> BuyTradingSideZones=new ArrayList<TradingZone>();
        	for(int j=0;j<BuyZones.length();j++) {
        		JSONObject Zone = BuyZones.getJSONObject(j);
        		TradingZone t = new TradingZone(Zone.getDouble("left"),
        										Zone.getDouble("right"),
        										Zone.getDouble("orderZoneQty"),
        										Zone.getDouble("maxZoneQty"));
        		
        		
        		/*String insertq="Insert INTO BuyTradingZones ([pair_id],[orderZoneQty],[left],[right],[maxZoneQty])"
				  		+ "Values('"+PairId+"','"+Zone.getDouble("orderZoneQty")+"','"+Zone.getDouble("left")+"','"+Zone.getDouble("right")+"','"+Zone.getDouble("maxZoneQty")+"')";
        		Statement stmt2= DBConnection.CreateConnection().createStatement();
          	stmt2.execute(insertq);*/
        		
        		BuyTradingSideZones.add(t);
        				//double lf,double ri,double orderZoneQt,double maxZoneQt
        				/*"orderZoneQty": 5.1,
                        "left": 30,
                        "right": 35,
                        "maxZoneQty": 5*/
}		
        JSONArray SellZones=pair.getJSONArray("SellTradingZones");
        ArrayList<TradingZone> SellTradingSideZones=new ArrayList<TradingZone>();
        for(int j=0;j<SellZones.length();j++) {
        JSONObject Zone = SellZones.getJSONObject(j);
        TradingZone t = new TradingZone(Zone.getDouble("left"),
        										Zone.getDouble("right"),
        										Zone.getDouble("orderZoneQty"),
        										Zone.getDouble("maxZoneQty"));
        /*String insertq="Insert INTO SellTradingZones ([pair_id],[orderZoneQty],[left],[right],[maxZoneQty])"
		  		+ "Values('"+PairId+"','"+Zone.getDouble("orderZoneQty")+"','"+Zone.getDouble("left")+"','"+Zone.getDouble("right")+"','"+Zone.getDouble("maxZoneQty")+"')";
    	Statement stmt2= DBConnection.CreateConnection().createStatement();
      	stmt2.execute(insertq);*/
        SellTradingSideZones.add(t);
        		
}
         
        
       String PairName =  pair.getString("pair").toUpperCase();
       double perProfitTarget = pair.getDouble("perProfitTarget");
       System.out.println("PER PROFTI TARGET :"+perProfitTarget);
       double MaxBalQty = pair.getDouble("maxBalQty");
       int leverage = pair.getInt("lev");
       int MinLiqValue=pair.getInt("minLiqValue");
       boolean perMode = pair.getBoolean("perMode");
     /*  int perrmode=0;
       if(perMode==true) {perrmode=1;}
       
       String insertq="Insert INTO PairsSettings ([user_id],[PairName],[ProfitTarget],[LiqVal],[Lev],[PerMode],[MaxBalQty])"
		  		+ "Values('"+Main.CurrentUser+"','"+PairName+"','"+perProfitTarget+"','"+MinLiqValue+"','"+leverage+"','"+perrmode+"','"+MaxBalQty+"')";
       Statement stmt2= DBConnection.CreateConnection().createStatement();
       stmt2.execute(insertq);*/
       try {CryptoCoin  c =new CryptoCoin(PairName,MaxBalQty,perProfitTarget,leverage,perMode,BuyTradingSideZones,SellTradingSideZones);
       allCrypto.put(PairName, c);
       }
       catch(Exception e) {
    	   e.printStackTrace();
       }
       
        	
        	
        	
        }
       
	}
public static void Init() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException, InterruptedException, ExecutionException {
	//SqlSettings();
	/**Init Read Settings, AccInfo and PricePrecision
	 * 
	 */
					
ReadSettings();
					
				BinanceDataRequest.AccInfoInit();
				SetPairsPricePrecision();
		
			
}

public static void getAccListenKey() throws IOException {
	/**
	 * Request an Account Listen Key used by AccountChangeWebsocket
	 * 
	 * 
	 */
	
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

  

ListenKey= resp.toMap().get("listenKey").toString();
}

static void KeepAlive() throws IOException {
	
	
	
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

	con.setRequestMethod("PUT");
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
}



public static void StopBot() {
	/**
	 * Start Websockets processes for all pairs
	 */
	for (Entry<String, CryptoCoin> entry : BotBinance.allCrypto.entrySet()) {
	   entry.getValue().StopWsLiquidation();
	}
}
public static void StartBot() {
	/**
	 * Stop Websockets processes for all pairs
	 */
	for (Entry<String, CryptoCoin> entry : BotBinance.allCrypto.entrySet()) {
	   entry.getValue().StartWsLiquidation();
	}
}
public static void StartAccWs() {
	/**
	 * Start AccountWebsocket using Account Listen key
	 */
	try {
		getAccListenKey();
	} catch (IOException e1) {
		StartAccWs();
		e1.printStackTrace();
	}
	try {
		AccStatusWs = Generator.WsAccStatus(ListenKey);
	} catch (InterruptedException | ExecutionException | IOException e) {
		StartAccWs();
		e.printStackTrace();
	}
		
		
		
	CompletableFuture<Void> WsCheck=CompletableFuture.runAsync(()-> {
		while(true) {
			
			try {
				
				Thread.sleep(3000);
			
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		
		if(!AccStatusWs.isOpen()) {
			try{System.out.println("Restart WsPice");
				AccStatusWs=Generator.WsAccStatus(ListenKey);}
				catch(Exception e) {
					}
			
		}}});
	
	
		CompletableFuture<Void> KeepAlive=CompletableFuture.runAsync(()-> {
			while(true) {
				
				try {
					
					Thread.sleep(3000000);
					KeepAlive();
				
				} catch (InterruptedException | IOException e) {
					
					e.printStackTrace();
				}
			
			
				
			}
		
		
		
		});
			
		
		
		
		
		
		
		
		
					
	
}

/////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("deprecation")
public static void main(String[] args) throws InterruptedException, ExecutionException, IOException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException  {
	/**
	 * Start and control the bot
	 */
	
	
		
		
		
Connection conn=DBConnection.CreateConnection();
System.out.println(System.currentTimeMillis());

startTime=new VWAPStartTime().getTime();
Init();
StartAccWs();
MainWindow.showWindow();
for (String key: allCrypto.keySet()) {
	CryptoCoin c =allCrypto.get(key);
	if(c.isInTrade) {System.out.println("Active pair: "+key+" ProfitPrice:"+c.getProfitPrice() );
	try {
		BinanceDataRequest.setLastTradedPrice(c);
	} catch (BinanceException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
} 
AccBal=AccBal+200;

checkPositions=CompletableFuture.runAsync(()-> {
	while(true) {
		
		try {
			
			Thread.sleep(5000);
			System.out.println("wait");
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
		
	Set<String> activeSet=pairsInTrade.keySet();
	
	try {
		
		byte[] encodeKey = BotBinance.SecretKey.getBytes();
		String parameters="timestamp="+System.currentTimeMillis();
		String querry_string = URLEncoder.encode(parameters,"UTF-8");
		
		 System.out.println(querry_string.toString());
		
		 byte[] encodeParam=parameters.getBytes("UTF-8");
		 System.out.println(encodeParam.length);
		 String sig=Hex.encodeHexString(HMAC.calcHmacSha256(encodeKey,encodeParam));
		String url="https://fapi.binance.com/fapi/v2/positionRisk"+"?"+parameters+"&signature="+sig;
	AsyncHttpClient asyncHttpClient = asyncHttpClient();

	ListenableFuture<Response> whenResponse = asyncHttpClient.prepareGet(url).addHeader("X-MBX-APIKEY", BotBinance.ApiKey).execute();
	
	//Response response = whenResponse.get();

	if(whenResponse.get().getStatusCode()==200) {
		JSONArray PositionsList=new JSONArray(whenResponse.get().getResponseBody());
		for(int i=0;i<PositionsList.length();i++) {
			JSONObject position=PositionsList.getJSONObject(i);
	if(allCrypto.containsKey(position.getString("symbol"))) {CryptoCoin c=allCrypto.get(position.getString("symbol"));
		if(pairsInTrade.containsKey(c.getName())){System.out.println("Pana la set TP");
												if(position.getDouble("entryPrice")!=c.getProfitPrice()) {
													OrderGenerator.CancelAllOrdersPair(c.getName());
													if(c.getActiveSide().equals("LONG")) {
														double price=BigDecimal.valueOf(position.getDouble("entryPrice")+(position.getDouble("entryPrice")*c.getPercentProfitTarget()/100)).setScale(c.getPricePrecision(),RoundingMode.HALF_UP).doubleValue();
														OrderGenerator.TakeLongProfits(Math.abs(position.getDouble("positionAmt")),c.getName(),price);}
													if(c.getActiveSide().equals("SHORT")) {
														double price=BigDecimal.valueOf(position.getDouble("entryPrice")-(position.getDouble("entryPrice")*c.getPercentProfitTarget()/100)).setScale(c.getPricePrecision(),RoundingMode.HALF_UP).doubleValue();
														OrderGenerator.TakeShortProfits(Math.abs(position.getDouble("positionAmt")),c.getName(),price);}
													
													
												}
	}}
	
	}}
	

	}catch(Exception e) {e.printStackTrace();}}});
//OrderGenerator.BuyMarketOrderGenerator(35, "DOGEUSDT");




//WsProcesses.add(Generator.WsAccStatus("ILdsHr4D53UqgMLBVkZO4d19FSl6s4VjATDZqrbz2px5S1WzZMF30zSyqT23rm1Y"));
/*WsProcesses.add(Generator.WsMarketLiq("btcusdt"));*/
/*System.out.println("dfdsfds: "+(new VWAPStartTime().getTime()));*/
/*CryptoCoin a= new CryptoCoin("btcusdt",2);
allCrypto.put("BTCUSDT",a);
CryptoCoin b= new CryptoCoin("ethusdt",2);
allCrypto.put("ETHUSDT",b);
CryptoCoin c= new CryptoCoin("bnbusdt",2);
allCrypto.put("BNBUSDT",c);
CryptoCoin d= new CryptoCoin("dogeusdt",2);
allCrypto.put("DOGEUSDT",d);*/





//OrderGenerator.BuyMarketOrderGenerator(5, "LITUSDT");
	
/*ArrayList<Candlestick> arr1=BinanceDataRequest.CryptoDataRequest("ETHUSDT",new VWAPStartTime().getTime(),"1m");
ArrayList<Candlestick> arr2=BinanceDataRequest.CryptoDataRequest("BNBUSDT",new VWAPStartTime().getTime(),"1m");
ArrayList<Candlestick> arr3=BinanceDataRequest.CryptoDataRequest("BCHUSDT",new VWAPStartTime().getTime(),"1m");*/

//while(true) {}
		
		/*System.out.println("Ok");
		WsProcesses.add(Generator.WsMarketLiq("btcusdt"));
		System.out.println("ASDASDASDASDSADASDASDASD");*/

}}
//wss://fstream.binance.com/ws/BTCUSDT