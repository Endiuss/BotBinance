package BotBinance;

import org.json.JSONObject;

public class TradingZone {
protected double left;
protected double right;
protected double orderZoneQty;
protected double activeZoneQty=0;
protected double maxZoneQty;
protected int zoneId;
public TradingZone(int zId,double lf,double ri,double orderZoneQt,double maxZoneQt) {
	this.zoneId=zId;
	this.left=lf;
	this.right=ri;
	this.orderZoneQty=orderZoneQt;
	this.maxZoneQty=maxZoneQt;
	
	
}
public TradingZone(double lf,double ri,double orderZoneQt,double maxZoneQt) {
	
	this.left=lf;
	this.right=ri;
	this.orderZoneQty=orderZoneQt;
	this.maxZoneQty=maxZoneQt;
	
	
}
public JSONObject toJson() {
	JSONObject rez=new JSONObject();
	rez.put("left", this.left);
	rez.put("right",this.right);
	rez.put("orderZoneQty",this.orderZoneQty);
	rez.put("maxZoneQty", this.maxZoneQty);
	
	return rez;
}

public void setActiveZoneQty(double activeZoneQty) {
	this.activeZoneQty = activeZoneQty;
}
public double getLeft() {
	return left;
}
public void setLeft(double left) {
	this.left = left;
}
public double getRight() {
	return right;
}
public void setRight(double right) {
	this.right = right;
}
public double getOrderZoneQty() {
	return orderZoneQty;
}
public void setOrderZoneQty(double orderZoneQty) {
	this.orderZoneQty = orderZoneQty;
}
public double getMaxZoneQty() {
	return maxZoneQty;
}
public void setMaxZoneQty(double maxZoneQty) {
	this.maxZoneQty = maxZoneQty;
}
public double getActiveZoneQty() {
	return activeZoneQty;
}
boolean isPriceInBuyZone(double price,double compPrice) {
System.out.println("LOWER SIDE left:"+(compPrice-((compPrice*this.left)/100)));
System.out.println("LOWER SIDE right:"+(compPrice-((compPrice*this.right)/100)));
	if(price<(compPrice-((compPrice*this.left)/100))&&price>=(compPrice-((compPrice*this.right)/100))) {return true;}
	return false;
	
}
boolean isPriceInSellZone(double price,double compPrice) {
	System.out.println("UPPER SIDE left:"+(compPrice+((compPrice*this.left)/100)));
	System.out.println("UPPER SIDE RIGHT:"+(compPrice+((compPrice*this.right)/100)));
	if(price>(compPrice+((compPrice*this.left)/100)) && price<=(compPrice+((compPrice*this.right)/100))) {return true;}
	return false;
	
}
}
