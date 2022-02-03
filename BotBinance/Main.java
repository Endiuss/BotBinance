package BotBinance;

import java.sql.SQLException;
import java.sql.Statement;


public class Main {
	public static int CurrentUser=-1;
	public static String Api=null;
	public static String Secret=null;
public static void main(String[] args) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
	
	   Statement stmt= DBConnection.CreateConnection().createStatement();
		
	   
		  
	   String CheckDatabase="IF (EXISTS (SELECT *"
	   		+ "   FROM INFORMATION_SCHEMA.TABLES"
	   		+ "   WHERE TABLE_SCHEMA = 'dbo'"
	   		+ "   AND TABLE_NAME = '"+"BotUser"+"'))"
	   		+ "   BEGIN "
	   		+"WAITFOR DELAY '00:00:00'"
	   		+ "   END;"
	   		+ "ELSE"
	   		+ "   BEGIN"
	   		+ " CREATE TABLE ["+"BotUser"+"]("
	   		+ " [user_id] int IDENTITY(1,1) PRIMARY KEY,"
	   		+ " [username] varchar(25) NOT NULL,"
	   		+ " [password] varchar(30) NOT NULL,"
	   		+ " [ApiKey] varchar(65) NOT NULL,"
	   		+ " [SecretKey] varchar(65) NOT NULL,"
	   		
	   		+ ");"
	   		+ "   END;";
System.out.println(CheckDatabase);
stmt.execute(CheckDatabase);
 
   String CheckDatabase2="IF (EXISTS (SELECT *"
   		+ "   FROM INFORMATION_SCHEMA.TABLES"
   		+ "   WHERE TABLE_SCHEMA = 'dbo'"
   		+ "   AND TABLE_NAME = '"+"PairsSettings"+"'))"
   		+ "   BEGIN "
   		+"WAITFOR DELAY '00:00:00'"
   		+ "   END;"
   		+ "ELSE"
   		+ "   BEGIN"
   		+ " CREATE TABLE ["+"PairsSettings"+"]("
   		+ " [pair_id] int IDENTITY(1,1) PRIMARY KEY,"
   		+ " user_id int FOREIGN KEY REFERENCES BotUser(user_id),"

   		+" [PairName] varchar(25) NOT NULL,"
   		+ " [ProfitTarget] numeric(6,3),"
   		+ " [LiqVal] int NOT NULL,"
   		+ " [Lev] int NOT NULL,"
   		+ " [PerMode] int NOT NULL,"
   		+ " [MaxBalQty] numeric(6,3), "
   		+ " "
   		+ ");"
   		+ "   END;";
   
System.out.println(CheckDatabase2);
stmt.execute(CheckDatabase2);
String CheckDatabase3="IF (EXISTS (SELECT *"
   		+ "   FROM INFORMATION_SCHEMA.TABLES"
   		+ "   WHERE TABLE_SCHEMA = 'dbo'"
   		+ "   AND TABLE_NAME = '"+"BuyTradingZones"+"'))"
   		+ "   BEGIN "
   		+"WAITFOR DELAY '00:00:00'"
   		+ "   END;"
   		+ "ELSE"
   		+ "   BEGIN"
   		+ " CREATE TABLE ["+"BuyTradingZones"+"]("
   		+ " [zone_id] int IDENTITY(1,1) PRIMARY KEY,"
   		+ " [pair_id] int FOREIGN KEY REFERENCES PairsSettings(pair_id),"
   		+ " [orderZoneQty] numeric(6,3),"
   		+ " [left] numeric(6,3),"
   		+ " [right] numeric(6,3),"
   		+ " [maxZoneQty] numeric(6,3),"
   		+ ");"
   		+ "   END;";
System.out.println(CheckDatabase3);
stmt.execute(CheckDatabase3);

String CheckDatabase4="IF (EXISTS (SELECT *"
   		+ "   FROM INFORMATION_SCHEMA.TABLES"
   		+ "   WHERE TABLE_SCHEMA = 'dbo'"
   		+ "   AND TABLE_NAME = '"+"SellTradingZones"+"'))"
   		+ "   BEGIN "
   		+"WAITFOR DELAY '00:00:00'"
   		+ "   END;"
   		+ "ELSE"
   		+ "   BEGIN"
   		+ " CREATE TABLE ["+"SellTradingZones"+"]("
   		+ " [zone_id] int IDENTITY(1,1) PRIMARY KEY,"
   		+ " [pair_id] int FOREIGN KEY REFERENCES PairsSettings(pair_id),"
   		+ " [orderZoneQty] numeric(6,3),"
   		+ " [left] numeric(6,3),"
   		+ " [right] numeric(6,3),"
   		+ " [maxZoneQty] numeric(6,3),"
   		+ ");"
   		+ "   END;";
System.out.println(CheckDatabase4);
stmt.execute(CheckDatabase4);

String CheckDatabase5="ALTER TABLE [dbo].[PairsSettings]  WITH CHECK ADD  CONSTRAINT [user_id] FOREIGN KEY([user_id])"
		+ "REFERENCES [dbo].[BotUser] ([user_id])"
		+ "ON DELETE CASCADE ";
		
System.out.println(CheckDatabase5);
stmt.execute(CheckDatabase5);


String CheckDatabase6="ALTER TABLE [dbo].[BuyTradingZones]  WITH CHECK ADD  CONSTRAINT [pair_id] FOREIGN KEY([pair_id])"
		+ "REFERENCES [dbo].[PairsSettings] ([pair_id])"
		+ " ON DELETE CASCADE ";
		
System.out.println(CheckDatabase6);
stmt.execute(CheckDatabase6);

String CheckDatabase7="ALTER TABLE [dbo].[SellTradingZones]  WITH CHECK ADD  CONSTRAINT [pair_id] FOREIGN KEY([pair_id])"
		+ "REFERENCES [dbo].[PairsSettings] ([pair_id])"
		+ " ON DELETE CASCADE ";
		
System.out.println(CheckDatabase7);
stmt.execute(CheckDatabase7);


LogIn.showWindow();



	
}
}
