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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void addActivityToDB(String activity, String userId, String userName) {
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
			boolean addToDb=true;
			
			Statement stmt1=connection.createStatement();
			ResultSet rs1 = stmt1.executeQuery("SELECT "+columnName+" FROM "+activity);
			
			if(rs1!=null)
			while (rs1.next()) {
				if(rs1.getString(columnName)!=null && rs1.getString(columnName).equals(screenName+"::"+userName))
				{
					addToDb=false;
					break;
				}
			}
			
			if(addToDb)
			{
				PreparedStatement stmt=connection.prepareStatement("INSERT INTO "+activity+"("+columnName+") VALUES(?)");
				stmt.setString(1, screenName+"::"+userName);
				stmt.executeUpdate();
			}
			
			
			
			
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
			
			
			System.out.println("Current Time:::::"+currHour);
			
			Statement stmt=connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT "+columnName+" FROM "+activity);
			
			if(rs!=null)
			while (rs.next()) {
				currUsersList.add(rs.getString(columnName));
			}
			
			return currUsersList;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
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
