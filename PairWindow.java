package BotBinance;

import javax.swing.*;

import com.mysql.cj.x.protobuf.MysqlxNotice.Frame;

import java.awt.*;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PairWindow{
protected static TradingZone selectedZone=null;
protected static String Side=null;
	public static void main(String[] args) {
		showWindow();
		
	}
 protected static void showZoneWindow() {
	 
	 JFrame frame = new JFrame("w3");
		frame.setBounds(200, 350,550,500);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );
		frame.getContentPane().setLayout(null);
		
		JTextPane ZoneQty = new JTextPane();
		ZoneQty.setFont(new Font("Tahoma", Font.PLAIN, 15));
		ZoneQty.setBounds(107, 23, 140, 25);
		frame.getContentPane().add(ZoneQty);
		ZoneQty.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				
				if(Character.isLetter(e.getKeyChar())) {e.consume();}
				else {
					try {
						Double.parseDouble(ZoneQty.getText()+e.getKeyChar());
						
						
					}catch(NumberFormatException event) {e.consume();}
				}
				
			}
		});
		
		
		JTextPane MaxZoneQty = new JTextPane();
		MaxZoneQty.setFont(new Font("Tahoma", Font.PLAIN, 15));
		MaxZoneQty.setBounds(369, 23, 140, 25);
		frame.getContentPane().add(MaxZoneQty);
		MaxZoneQty.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				
				if(Character.isLetter(e.getKeyChar())) {e.consume();}
				else {
					try {
						Double.parseDouble(MaxZoneQty.getText()+e.getKeyChar());
						
						
					}catch(NumberFormatException event) {e.consume();}
				}
				
			}
		});
		
		JLabel lblNewLabel = new JLabel("OrderZoneQty:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 23, 93, 25);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblMaxzoneqty = new JLabel("MaxZoneQty:");
		lblMaxzoneqty.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblMaxzoneqty.setBounds(282, 23, 93, 25);
		frame.getContentPane().add(lblMaxzoneqty);
		
		JLabel lblLeft = new JLabel("Left: ");
		lblLeft.setHorizontalAlignment(SwingConstants.CENTER);
		lblLeft.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblLeft.setBounds(10, 94, 93, 25);
		frame.getContentPane().add(lblLeft);
		
		JLabel lblRight = new JLabel("Right:");
		lblRight.setHorizontalAlignment(SwingConstants.CENTER);
		lblRight.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblRight.setBounds(282, 101, 93, 25);
		frame.getContentPane().add(lblRight);
		
		JTextPane Left = new JTextPane();
		Left.setFont(new Font("Tahoma", Font.PLAIN, 15));
		Left.setBounds(107, 101, 140, 25);
		frame.getContentPane().add(Left);
		Left.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				
				if(Character.isLetter(e.getKeyChar())) {e.consume();}
				else {
					try {
						Double.parseDouble(Left.getText()+e.getKeyChar());
						
						
					}catch(NumberFormatException event) {e.consume();}
				}
				
			}
		});
		
		
		
		JTextPane Right = new JTextPane();
		Right.setFont(new Font("Tahoma", Font.PLAIN, 15));
		Right.setBounds(369, 101, 140, 25);
		frame.getContentPane().add(Right);
		Right.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				
				if(Character.isLetter(e.getKeyChar())) {e.consume();}
				else {
					try {
						Double.parseDouble(Right.getText()+e.getKeyChar());
						
						
					}catch(NumberFormatException event) {e.consume();}
				}
				
			}
		});
		
		JButton AddSave = new JButton("Add");
		AddSave.setFont(new Font("Tahoma", Font.PLAIN, 16));
		AddSave.setBounds(184, 207, 168, 38);
		frame.getContentPane().add(AddSave);
		AddSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(selectedZone==null) {
					 if(Side.equals("BUY")) {BotBinance.selectedPair.addBuyZone(new TradingZone(Double.valueOf(Left.getText()),Double.valueOf(Right.getText()),Double.valueOf(ZoneQty.getText()),Double.valueOf(MaxZoneQty.getText())));	
				}
					 if(Side.equals("SELL")) {BotBinance.selectedPair.addSellZone(new TradingZone(Double.valueOf(Left.getText()),Double.valueOf(Right.getText()),Double.valueOf(ZoneQty.getText()),Double.valueOf(MaxZoneQty.getText())));	
						}
					
				}}}

);
		
		

		
		frame.setLocationRelativeTo(null);

		frame.setVisible(true);
	 
 }
	
	
	public static void showWindow() {
	JFrame frame = new JFrame("w2");
	frame.setBounds(200, 350,635,649);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.getContentPane().setLayout(null);
	
	JTextPane ProfitTarget = new JTextPane();
	ProfitTarget.addKeyListener(new KeyAdapter() {
		@Override
		public void keyTyped(KeyEvent e) {
			
			if(Character.isLetter(e.getKeyChar())) {e.consume();}
			else {
				try {
					Double.parseDouble(ProfitTarget.getText()+e.getKeyChar());
					
					
				}catch(NumberFormatException event) {e.consume();}
			}
			
		}
	});
	ProfitTarget.setFont(new Font("Tahoma", Font.PLAIN, 15));
	ProfitTarget.setBounds(134, 11, 155, 40);
	frame.getContentPane().add(ProfitTarget);
	
	JLabel lblNewLabel = new JLabel(" % Profit Target: ");
	lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
	lblNewLabel.setBounds(10, 11, 128, 37);
	frame.getContentPane().add(lblNewLabel);
	
	JLabel lblMinliqval = new JLabel("MinLiqValue: ");
	lblMinliqval.setFont(new Font("Tahoma", Font.PLAIN, 15));
	lblMinliqval.setBounds(331, 11, 91, 37);
	frame.getContentPane().add(lblMinliqval);
	
	
	JTextPane MinLiqValue = new JTextPane();
	MinLiqValue.setFont(new Font("Tahoma", Font.PLAIN, 15));
	MinLiqValue.setBounds(432, 11, 155, 40);
	frame.getContentPane().add(MinLiqValue);
	MinLiqValue.addKeyListener(new KeyAdapter() {
		@Override
		public void keyTyped(KeyEvent e) {
			
			if(Character.isLetter(e.getKeyChar())) {e.consume();}
			else {
				try {
					Double.parseDouble(MinLiqValue.getText()+e.getKeyChar());
					
					
				}catch(NumberFormatException event) {e.consume();}
			}
			
		}
	});
	
	
	
	JLabel lblMaxbalqty = new JLabel("MaxBalQty: ");
	lblMaxbalqty.setFont(new Font("Tahoma", Font.PLAIN, 15));
	lblMaxbalqty.setBounds(37, 92, 128, 37);
	frame.getContentPane().add(lblMaxbalqty);
	
	JTextPane MaxBalQty = new JTextPane();
	MaxBalQty.setFont(new Font("Tahoma", Font.PLAIN, 15));
	MaxBalQty.setBounds(134, 92, 155, 40);
	frame.getContentPane().add(MaxBalQty);
	MaxBalQty.addKeyListener(new KeyAdapter() {
		@Override
		public void keyTyped(KeyEvent e) {
			
			if(Character.isLetter(e.getKeyChar())) {e.consume();}
			else {
				try {
					Double.parseDouble(MaxBalQty.getText()+e.getKeyChar());
					
					
				}catch(NumberFormatException event) {e.consume();}
			}
			
		}
	});
	
	
	
	JTextPane Lev = new JTextPane();
	Lev.setFont(new Font("Tahoma", Font.PLAIN, 15));
	Lev.setBounds(432, 92, 155, 40);
	frame.getContentPane().add(Lev);
	Lev.addKeyListener(new KeyAdapter() {
		@Override
		public void keyTyped(KeyEvent e) {
			
			if(Character.isLetter(e.getKeyChar())) {e.consume();}
			else {
				try {
					Double.parseDouble(Lev.getText()+e.getKeyChar());
					
					
				}catch(NumberFormatException event) {e.consume();}
			}
			
		}
	});
	
	
	
	JLabel lblLev = new JLabel("Lev:");
	lblLev.setFont(new Font("Tahoma", Font.PLAIN, 15));
	lblLev.setBounds(358, 92, 91, 37);
	frame.getContentPane().add(lblLev);
	
	JCheckBox perMode = new JCheckBox("% Mode");
	perMode.setHorizontalAlignment(SwingConstants.CENTER);
	perMode.setFont(new Font("Tahoma", Font.PLAIN, 16));
	perMode.setBounds(6, 152, 159, 64);
	frame.getContentPane().add(perMode);
	final DefaultListModel<String> BuyModel = new DefaultListModel<>();
	
	
	
	JButton btnRemove = new JButton("Remove");
	btnRemove.setFont(new Font("Tahoma", Font.PLAIN, 16));
	btnRemove.setBounds(398, 559, 136, 40);
	frame.getContentPane().add(btnRemove);
	final DefaultListModel<String> SellModel = new DefaultListModel<>();
	
	
	
	JButton AddBuyZone = new JButton("AddBuyZone");
	AddBuyZone.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			Side="BUY";
			showZoneWindow();
}
}
	);
	
	
	
	
	
	
	
	AddBuyZone.setFont(new Font("Tahoma", Font.PLAIN, 10));
	AddBuyZone.setBounds(10, 517, 108, 29);
	frame.getContentPane().add(AddBuyZone);
	
	JButton DeleteBuyZone = new JButton("EditBuyZone");
	DeleteBuyZone.setFont(new Font("Tahoma", Font.PLAIN, 10));
	DeleteBuyZone.setBounds(135, 517, 113, 29);
	frame.getContentPane().add(DeleteBuyZone);
	
	
	
	
	
	
	JButton btnAddsellzone = new JButton("AddSellZone");
	btnAddsellzone.setFont(new Font("Tahoma", Font.PLAIN, 10));
	btnAddsellzone.setBounds(331, 519, 108, 29);
	frame.getContentPane().add(btnAddsellzone);
	btnAddsellzone.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			Side="SELL";
			showZoneWindow();
}
}
	);
	
	
	JButton btnRemovesellzone = new JButton("EditSellZone");
	btnRemovesellzone.setFont(new Font("Tahoma", Font.PLAIN, 10));
	btnRemovesellzone.setBounds(479, 517, 108, 29);
	frame.getContentPane().add(btnRemovesellzone);
	
	JTextPane PairName = new JTextPane();
	PairName.setFont(new Font("Tahoma", Font.PLAIN, 15));
	PairName.setBounds(432, 165, 155, 40);
	frame.getContentPane().add(PairName);
	
	JLabel lblPairname = new JLabel("PairName");
	lblPairname.setFont(new Font("Tahoma", Font.PLAIN, 15));
	lblPairname.setBounds(358, 165, 91, 37);
	frame.getContentPane().add(lblPairname);
	
	
	 System.out.println(BotBinance.selectedPair.PercentProfitTarget);
		PairName.setText(String.valueOf(BotBinance.selectedPair.getName()));
		ProfitTarget.setText(String.valueOf(BotBinance.selectedPair.PercentProfitTarget));
		 MinLiqValue.setText(String.valueOf(BotBinance.selectedPair.getMinLiqValue()));
		 MaxBalQty.setText(String.valueOf(BotBinance.selectedPair.getMaxBalQty()));
		 Lev.setText(String.valueOf(BotBinance.selectedPair.getLeverage()));
		 perMode.setSelected(BotBinance.selectedPair.getPerMode());
		 for(int i=0;i<BotBinance.selectedPair.SellTradingSideZones.size();i++) {String element="Lf:"+BotBinance.selectedPair.SellTradingSideZones.get(i).getLeft()+" RI:"+BotBinance.selectedPair.SellTradingSideZones.get(i).getRight()+" ZoneQty:"+BotBinance.selectedPair.SellTradingSideZones.get(i).getOrderZoneQty()+" MaxQty:"+BotBinance.selectedPair.SellTradingSideZones.get(i).getMaxZoneQty();
		 SellModel.addElement(element);
		}
	 final JList<String> SellList = new JList<>( SellModel );
		SellList.setBounds(331, 218, 256, 275);
		frame.getContentPane().add(SellList);
		
		
		
		
		for(int i=0;i<BotBinance.selectedPair.BuyTradingSideZones.size();i++) {String element="Lf:"+BotBinance.selectedPair.BuyTradingSideZones.get(i).getLeft()+" RI:"+BotBinance.selectedPair.BuyTradingSideZones.get(i).getRight()+" ZoneQty:"+BotBinance.selectedPair.BuyTradingSideZones.get(i).getOrderZoneQty()+" MaxQty:"+BotBinance.selectedPair.BuyTradingSideZones.get(i).getMaxZoneQty();
		 BuyModel.addElement(element);
		}
		final JList<String> BuyList = new JList<>( BuyModel );
		
		BuyList.setBounds(10, 218, 238, 275);
		frame.getContentPane().add(BuyList);
		 
	 
	
	
	

		JButton Save = new JButton("Save");
		Save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			if(!ProfitTarget.getText().equals(""))
			
				BotBinance.selectedPair.setPercentProfitTarget(Double.valueOf(ProfitTarget.getText()));
				BotBinance.selectedPair.setMinLiqValue(Double.valueOf(MinLiqValue.getText()));
				BotBinance.selectedPair.setLeverage(Integer.valueOf(Lev.getText()));
				BotBinance.selectedPair.setPerMode(perMode.isSelected());
				BotBinance.selectedPair.setMaxBalQty(Double.valueOf(MaxBalQty.getText()));
				MainWindow.showWindow();
				frame.setVisible(false);
								
							 }
					
						  
						  
		}
		        	
);
		
		
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		
				BotBinance.allCrypto.remove(BotBinance.selectedPair.getName());
				
				frame.setVisible(true);
				MainWindow.showWindow();
			
			}});
		
	
		Save.setFont(new Font("Tahoma", Font.PLAIN, 16));
		Save.setBounds(112, 559, 136, 40);
		frame.getContentPane().add(Save);
	
		DeleteBuyZone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BuyModel.remove(BuyList.getSelectedIndex());
				
				BotBinance.selectedPair.deleteBuyTradingZone(BuyList.getSelectedIndex());
	}
	}
		);
		btnRemovesellzone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SellModel.remove(SellList.getSelectedIndex());
				
				BotBinance.selectedPair.deleteSellTradingZone(SellList.getSelectedIndex());
	}
	}
		);
		
		
		
	frame.setLocationRelativeTo(null);

	frame.setVisible(true);
	
}
}






