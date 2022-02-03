package BotBinance;


import javax.swing.*;

import org.json.JSONObject;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.awt.event.ActionEvent;

public class MainWindow {

	public static void main(String[] args) {
		showWindow();
		
	}
	public static void showWindow() {
	JFrame frame = new JFrame("w1");
	frame.setBounds(200, 350,635,649);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.getContentPane().setLayout(null);
	
	JLabel lblNewLabel = new JLabel("  API Key: ");
	lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
	lblNewLabel.setBounds(0, 30, 83, 25);
	frame.getContentPane().add(lblNewLabel);
	
	JTextPane APIPane = new JTextPane();
	APIPane.setFont(new Font("Tahoma", Font.PLAIN, 14));
	APIPane.setBounds(83, 30, 528, 25);
	APIPane.setText(BotBinance.ApiKey);

	frame.getContentPane().add(APIPane);
	
	JLabel lblNewLabel_1 = new JLabel(" Secret Key:");
	lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
	lblNewLabel_1.setBounds(0, 83, 83, 25);
	frame.getContentPane().add(lblNewLabel_1);
	
	JTextPane SecretPane = new JTextPane();
	SecretPane.setFont(new Font("Tahoma", Font.PLAIN, 14));
	SecretPane.setBounds(86, 83, 525, 25);
	SecretPane.setText(BotBinance.SecretKey);
	frame.getContentPane().add(SecretPane);
	
	JTextPane MaxPairsPane = new JTextPane();
	MaxPairsPane.setFont(new Font("Tahoma", Font.PLAIN, 14));
	MaxPairsPane.setBounds(120, 154, 178, 31);
	MaxPairsPane.setText(Integer.toString(BotBinance.MaxActivePairs));
	frame.getContentPane().add(MaxPairsPane);
	
	JLabel lblNewLabel_2 = new JLabel(" Max Active Pairs:");
	lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
	lblNewLabel_2.setBounds(0, 154, 119, 25);
	frame.getContentPane().add(lblNewLabel_2);
	
	JLabel lblNewLabel_2_1 = new JLabel("Max Balance:");
	lblNewLabel_2_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
	lblNewLabel_2_1.setBounds(308, 154, 108, 25);
	frame.getContentPane().add(lblNewLabel_2_1);
	
	JTextPane MaxBalancePane = new JTextPane();
	MaxBalancePane.setFont(new Font("Tahoma", Font.PLAIN, 14));
	MaxBalancePane.setBounds(420, 154, 191, 31);
	MaxBalancePane.setText(Double.toString(BotBinance.MaxActiveBalance));
	frame.getContentPane().add(MaxBalancePane);
	
	final DefaultListModel<String> model = new DefaultListModel<>();

	for (Entry<String, CryptoCoin> entry : BotBinance.allCrypto.entrySet()) {
	    model.addElement(entry.getKey());
	}
	final JList<String> PairList = new JList<>(model);
	PairList.setBounds(10, 229, 601, 295);
	
	frame.getContentPane().add(PairList);
	
	
	
	
	JButton EditPair = new JButton("Edit Pair");
	EditPair.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			BotBinance.selectedPair=BotBinance.allCrypto.get(PairList.getSelectedValue());
			frame.setVisible(false);
			
			
			PairWindow.showWindow();
		}
	});
	EditPair.setBounds(181, 541, 136, 47);
	frame.getContentPane().add(EditPair);
	
	
	JButton Save = new JButton("Save");
	Save.setBounds(473, 541, 136, 47);
	frame.getContentPane().add(Save);
	
	JButton AddPair = new JButton("Add Pair");
	AddPair.setBounds(35, 541, 136, 47);
	frame.getContentPane().add(AddPair);

	
	JButton StartStop = new JButton("Start");
	StartStop.setBounds(327, 541, 136, 47);
	frame.getContentPane().add(StartStop);
	StartStop.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			BotBinance.StartBot();
			if(BotBinance.BotStatus==false) {
				BotBinance.BotStatus=true;
				StartStop.setText("Stop");
				StartStop.setEnabled(true);
				AddPair.setEnabled(false);
				EditPair.setEnabled(false);
				Save.setEnabled(false);
				
			}
			else {
				BotBinance.StopBot();
				BotBinance.BotStatus=false;
				StartStop.setText("Start");
				StartStop.setEnabled(true);
				AddPair.setEnabled(true);
				EditPair.setEnabled(true);
				Save.setEnabled(true);
			}
		
			
		}
	});
	
	
	if(BotBinance.BotStatus==false) {
		StartStop.setText("Start");
		StartStop.setEnabled(true);
		AddPair.setEnabled(true);
		EditPair.setEnabled(true);
		Save.setEnabled(true);
	}
	String PairA=" ";
	AddPair.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			  String PairA = JOptionPane.showInputDialog(frame,
                      "PairToAdd", null);
			  try{BotBinance.allCrypto.put(PairA.toUpperCase(),new CryptoCoin(PairA.toUpperCase()));
			  
			  }
			  catch(Exception u) {}
			 
			  BotBinance.SetPairsPricePrecision();

			
		}});
	
	
	
	MaxBalancePane.setText(String.valueOf(BotBinance.AccBal));
	frame.setLocationRelativeTo(null);

	frame.setVisible(true);
	
}
}
