package com.heroku.dhruv.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DBWork {
	public static Connection connection;
	public static Connection connection2;
	
	static {
		
		//URI dbUri = new URI(System.getenv("DATABASE_URL"));
		URI dbUri=null;
		try {
			dbUri = new URI("postgres://twgaonnegohdug:1cd6ec817a63856e78ab15d62b46dc87c59647ef4d8764482fa6250e1e500315@ec2-54-243-54-6.compute-1.amazonaws.com:5432/de6sgvkntk4hcr");
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
                try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		String dbUrl = "jdbc:postgresql://"
				+ dbUri.getHost()
				+ ':'
				+ dbUri.getPort()
				+ dbUri.getPath()
				+ "?sslmode=require&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
		try {
			connection= DriverManager.getConnection(dbUrl, username, password);
			connection2= DriverManager.getConnection(dbUrl, username, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void addActivityToDB(String activity, String userId, String userName, String location) {
		try {
			String columnName="";
			
			int index = userId.indexOf('@');
			String screenName = userId.substring(0,index);
			
			
			DateFormat dateFormat = new SimpleDateFormat("HH");
			Date date = new Date();
			String currHourString=dateFormat.format(date);
			int currHour=Integer.parseInt(currHourString);
			
			if(currHour>=7 && currHour<=10)
				columnName="seventoten";
			else if(currHour>=11 && currHour<=14)
				columnName="eleventofourteen";
			else if(currHour>=15 && currHour<=18)
				columnName="fifteentoeighteen";
			else if(currHour>=19 && currHour<=22)
				columnName="nineteentotwentytwo";
			else if(currHour>=23 && currHour<=24)
				columnName="twentyhreetosix";
			else if(currHour>=0 && currHour<=6)
				columnName="twentyhreetosix";
			
			
			System.out.println("Current Time:::::"+currHour);
			
			try {
				Statement stmt1 = connection.createStatement();
				ResultSet rs = stmt1.executeQuery("SELECT * from "+activity);
				}catch(org.postgresql.util.PSQLException exc)
				{
					System.out.println("Creating new table:::"+activity);
					PreparedStatement stmt1=connection.prepareStatement("CREATE TABLE "+activity+"(seventoten text,eleventofourteen text,fifteentoeighteen text,nineteentotwentytwo text,twentyhreetosix text);");
					stmt1.executeUpdate();
				}
			
			boolean userExists=false;
			Statement stmt1=connection.createStatement();
			ResultSet rs1 = stmt1.executeQuery("SELECT "+columnName+" FROM "+activity);
			
			if(rs1!=null)
			while (rs1.next()) {
				if(rs1.getString(columnName)!=null)
				{
					String screeNameindb=rs1.getString(columnName).split("::")[0];
					String userNameindb=rs1.getString(columnName).split("::")[1];
					
					if(screeNameindb.equals(screeNameindb) && userNameindb.equals(userName))
					{
						//user exists..delete it
						userExists=true;
						break;
					}
					
				}
			}
			
			if(userExists)
			{
				Statement stmt2=connection.createStatement();
				stmt2.executeUpdate("DELETE FROM "+activity+" WHERE "+columnName+" like '"+screenName+"::"+userName+"%'");
			}
			
			
			if(location==null)
				location="";
			
			
			boolean userdetailsexists=false;
			Statement stmt7=connection.createStatement();
			ResultSet rs7 = stmt7.executeQuery("SELECT details from userstrack");
			
			if(rs7!=null)
			while (rs7.next()) {
				if(rs7.getString("details")!=null && rs7.getString("details").equals(screenName+":"+activity))
				{
					userdetailsexists=true;
					break;
				}
			}
			
			
			if(userdetailsexists==false)
			{
				
				PreparedStatement stmt3=connection.prepareStatement("INSERT INTO userstrack(details) VALUES(?)");
				stmt3.setString(1, screenName+":"+activity);
				stmt3.executeUpdate();
			}
			
			
				PreparedStatement stmt=connection.prepareStatement("INSERT INTO "+activity+"("+columnName+") VALUES(?)");
				stmt.setString(1, screenName+"::"+userName+"::"+location);
				stmt.executeUpdate();
			
			
			
			
//			ResultSet rs = stmt.executeQuery("SELECT Thing FROM Goods");
//			while (rs.next()) {
//				System.out.println("Thing: " + rs.getString("thing"));
//			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<String> fetchActivityFromDB(String activity) {
		
		ArrayList<String> currUsersList=new ArrayList<String>();
		
		try {
			String columnName="";
			
			DateFormat dateFormat = new SimpleDateFormat("HH");
			Date date = new Date();
			String currHourString=dateFormat.format(date);
			int currHour=Integer.parseInt(currHourString);
			
			if(currHour>=7 && currHour<=10)
				columnName="seventoten";
			else if(currHour>=11 && currHour<=14)
				columnName="eleventofourteen";
			else if(currHour>=15 && currHour<=18)
				columnName="fifteentoeighteen";
			else if(currHour>=19 && currHour<=22)
				columnName="nineteentotwentytwo";
			else if(currHour>=23 && currHour<=24)
				columnName="twentyhreetosix";
			else if(currHour>=0 && currHour<=6)
				columnName="twentyhreetosix";
			
			
			Statement stmt=connection2.createStatement();
			Statement stmt7=connection2.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT "+columnName+" FROM "+activity);
			
			if(rs!=null)
			while (rs.next()) {
				
				//get chatFeature status from db..
				System.out.println(":::::::::"+rs.getString(columnName)+":::"+columnName+"::"+activity);
				String userName=rs.getString(columnName).split("::")[0];
				String mobileNumber="";
				String chat="";
				
				ResultSet rs1 = stmt7.executeQuery("SELECT * FROM usermobnumber where email='"+userName+"'");
				
				while(rs1.next())
				{
					mobileNumber=rs.getString("mobnumber");
					chat=rs.getString("chat");
					break;
				}
				
				
				if(chat.equalsIgnoreCase("Y"))
				{
					currUsersList.add(rs.getString(columnName)+"::"+mobileNumber);
				}
				else
				{
					currUsersList.add(rs.getString(columnName)+"::");
				}
				
				
			}
			
			return currUsersList;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		
		
	}
	
	
	
	
	public static void chatFeature(String userEmail, String mobnumber, String chat) {
		try {
			int index = userEmail.indexOf('@');
			String screenName = userEmail.substring(0,index);
			
			Statement stmt1=connection.createStatement();
			
			ResultSet rs1 = stmt1.executeQuery("SELECT * FROM usermobnumber where email='"+screenName+"'");
			
			if(rs1!=null && rs1.next()==false)
			{
				
				//user doesnt exists. create new.
				PreparedStatement stmt3=connection.prepareStatement("INSERT INTO usermobnumber(email, mobnumber, chat) VALUES(?,?,?)");
				stmt3.setString(1, screenName);
				stmt3.setString(2, mobnumber);
				stmt3.setString(3, chat);
				stmt3.executeUpdate();
				
			}
			else
			{
				
				Statement st7=connection2.createStatement();
				st7.executeUpdate("UPDATE usermobnumber SET chat='"+chat+"',mobnumber='"+mobnumber+"' where email='"+screenName+"'");
				
			}
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void startCron()
	{
		ResultSet rs=null;
		
		String tablesToTruncate="";
		
		try {
			Statement stmt1 = connection.createStatement();
			rs = stmt1.executeQuery("SELECT tablename, tableowner FROM pg_catalog.pg_tables");
			
		
			
		while (rs.next()) {
			
			if(rs.getString("tableowner").equals("twgaonnegohdug"))
			{
				tablesToTruncate+=rs.getString("tablename")+",";
			}
			
		}
		
		tablesToTruncate = tablesToTruncate.substring(0, tablesToTruncate.length() - 1);
		
		System.out.println("Tables deleted::"+tablesToTruncate);
		
		stmt1.executeUpdate("TRUNCATE " + tablesToTruncate);
		
		
		}catch(Exception exc)
		{
			exc.printStackTrace();
		}
		
		
		
	}
	
	
}
