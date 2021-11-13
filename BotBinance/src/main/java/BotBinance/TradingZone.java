package BotBinance;

public class TradingZone {
protected double left;
protected double right;
protected double orderZoneQty;
protected double activeZoneQty=0;
protected double maxZoneQty;

public TradingZone(double lf,double ri,double orderZoneQt,double maxZoneQt) {

	this.left=lf;
	this.right=ri;
	this.orderZoneQty=orderZoneQt;
	this.maxZoneQty=maxZoneQt;
	
	
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
boolean isPriceInZone(double price) {
	
	if(price>=this.left&&price<=this.left) {return true;}
	return false;
	
}
}
