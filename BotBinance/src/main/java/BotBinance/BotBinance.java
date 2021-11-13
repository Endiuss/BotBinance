package BotBinance;
import BotBinance.WsGenerator;

import java.io.IOException;
import java.lang.Thread.State;
import java.lang.reflect.Array;
import java.net.http.*;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.asynchttpclient.Dsl;
import org.asynchttpclient.ws.*;
import org.asynchttpclient.ws.WebSocket;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.*;


public class BotBinance {
	static String ApiKey = "CRVTpwZX7ra1b2I24UC6YJwctIGdGvLDa6Bs9fTHcaCRcVpaJFa7SLVXNcd478ZC";
	static String SecretKey="5QwQvPm5lpIrncTSK1fvnXnah6in8gyTrZbnGTFDQsBTSmIOPKttuTpSbHyjlZBp";
	static Map<String, CryptoCoin> allCrypto = new HashMap<String, CryptoCoin>();
	static UpdatersGenerator updaterGenerator=new UpdatersGenerator();
	static long startTime;
	static BinanceDataRequest dataRequest=new BinanceDataRequest();
	static WsGenerator Generator=new WsGenerator();
	@SuppressWarnings("unused")
	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException  {
	
	
	
		List<org.asynchttpclient.ws.WebSocket> WsProcesses =new ArrayList<org.asynchttpclient.ws.WebSocket>();
		WsGenerator Generator=new WsGenerator();
		
Connection conn=DBConnection.CreateConnection();
System.out.println(System.currentTimeMillis());

startTime=new VWAPStartTime().getTime();

WsProcesses.add(Generator.WsAccStatus("ILdsHr4D53UqgMLBVkZO4d19FSl6s4VjATDZqrbz2px5S1WzZMF30zSyqT23rm1Y"));
/*WsProcesses.add(Generator.WsMarketLiq("btcusdt"));*/
/*System.out.println("dfdsfds: "+(new VWAPStartTime().getTime()));*/
CryptoCoin a= new CryptoCoin("btcusdt",2);
CryptoCoin b= new CryptoCoin("ethusdt",2);
CryptoCoin c= new CryptoCoin("bnbusdt",2);
CryptoCoin d= new CryptoCoin("dogeusdt",2);
/*ArrayList<Candlestick> arr1=BinanceDataRequest.CryptoDataRequest("ETHUSDT",new VWAPStartTime().getTime(),"1m");
ArrayList<Candlestick> arr2=BinanceDataRequest.CryptoDataRequest("BNBUSDT",new VWAPStartTime().getTime(),"1m");
ArrayList<Candlestick> arr3=BinanceDataRequest.CryptoDataRequest("BCHUSDT",new VWAPStartTime().getTime(),"1m");*/

//while(true) {}
		
		/*System.out.println("Ok");
		WsProcesses.add(Generator.WsMarketLiq("btcusdt"));
		System.out.println("ASDASDASDASDSADASDASDASD");*/

}}
//wss://fstream.binance.com/ws/BTCUSDT