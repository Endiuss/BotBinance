package BotBinance;



import javax.swing.JFrame;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutionException;

public class LogIn {


	/**
	 * @wbp.parser.entryPoint
	 */
	public static void showWindow() {
	final JFrame frame = new JFrame("w2");
	frame.setBounds(200, 350,435,449);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.getContentPane().setLayout(null);
	
	JLabel lblNewLabel = new JLabel("Username");
	lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
	lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
	lblNewLabel.setBounds(10, 48, 104, 52);
	frame.getContentPane().add(lblNewLabel);
	
	JLabel lblPassword = new JLabel("Password");
	lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
	lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));
	lblPassword.setBounds(10, 128, 104, 52);
	frame.getContentPane().add(lblPassword);
	
	final JTextPane userPane = new JTextPane();
	userPane.setFont(new Font("Tahoma", Font.PLAIN, 14));
	userPane.setBounds(113, 60, 236, 40);
	frame.getContentPane().add(userPane);
	
	final JTextPane passwordPane = new JTextPane();
	
	passwordPane.setFont(new Font("Tahoma", Font.PLAIN, 14));
	passwordPane.setBounds(113, 128, 236, 40);
	frame.getContentPane().add(passwordPane);
	
	JButton loginBtn = new JButton("Login");
	loginBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
	loginBtn.setBounds(144, 197, 120, 40);
	frame.getContentPane().add(loginBtn);
	
	JLabel lblNewLabel_1 = new JLabel("If you already have an account, please log in!");
	lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
	lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
	lblNewLabel_1.setBounds(28, 24, 342, 25);
	frame.getContentPane().add(lblNewLabel_1);
	
	JLabel lblNewLabel_2 = new JLabel("If you don't have an account, sign in!");
	lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
	lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
	lblNewLabel_2.setBounds(28, 260, 348, 25);
	frame.getContentPane().add(lblNewLabel_2);
	
	JButton signBtn = new JButton("Sign in");
	signBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
	signBtn.setBounds(144, 306, 120, 40);
	frame.getContentPane().add(signBtn);
	
	final JLabel error = new JLabel("");
	error.setHorizontalAlignment(SwingConstants.CENTER);
	error.setFont(new Font("Tahoma", Font.PLAIN, 14));
	error.setBounds(47, 357, 348, 25);
	frame.getContentPane().add(error);
	//SnakeGame.conn
	signBtn.addActionListener(new ActionListener() {

		public void actionPerformed(java.awt.event.ActionEvent e) {
			frame.setVisible(false);
			SignUp.showWindow();
			
		}});
	loginBtn.addActionListener(new ActionListener() {

		public void actionPerformed(java.awt.event.ActionEvent e) {
		String usr= userPane.getText();
		String pass=passwordPane.getText();
				
		 try {
			Statement stmt= DBConnection.CreateConnection().createStatement();
			
			String q=" SELECT user_id , ApiKey , SecretKey  FROM [BotUser] Where [username] = \'"+usr.toLowerCase()+"\' AND [password] = \'"+pass+"\'";
			
			System.out.println(q);
				  ResultSet dbResponse= stmt.executeQuery(q);
				  ResultSetMetaData resultSetMetaData = dbResponse.getMetaData();
				  if(dbResponse != null) {int ok=0;
					  while (dbResponse.next()) {ok=1;
						Main.CurrentUser = dbResponse.getInt("user_id");
						Main.Api = dbResponse.getString("ApiKey");
						Main.Secret = dbResponse.getString("SecretKey");
						frame.setVisible(false);
						BotBinance.Bot();
						
						
					 }
					  if(ok==0) {error.setText("Invalid username or password!");}}
				  else {error.setText("Invalid username or password!");}
				
		 } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e1) {
			
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		}});
	
	
	
	frame.setLocationRelativeTo(null);

	frame.setVisible(true);
	
}
}