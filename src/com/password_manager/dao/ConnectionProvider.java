package com.password_manager.dao;
import java.sql.*;

public class ConnectionProvider 
{
	private static ConnectionProvider connectionprovider=null;
	private ConnectionProvider()
	{
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch(Exception ex)
		{
			System.out.println("Exception "+ex.getMessage());
		}
	}
	
	public  Connection getConnection()
	{
		try
		{
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/PASSWORD_MANAGER","root","");
			return con;
		}
		catch(Exception ex)
		{
			System.out.println("Exception in getconnection "+ex.getMessage());
			return null;
		}
	}
	
	public static ConnectionProvider getInstance()
	{
		if(connectionprovider==null)
		{
			connectionprovider=new ConnectionProvider();
		}
		return connectionprovider;
	}
}
