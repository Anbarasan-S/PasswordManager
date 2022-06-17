package com.password_manager.dao;

import java.sql.*;

public class OrganisationDAO 
{
	private Connection con=null;
	private PreparedStatement ps=null;
	private String query="";
	public OrganisationDAO()
	{
		
	try
	{
		con=ConnectionProvider.getInstance().getConnection();
	}
	catch(Exception ex)
	{
		System.out.println("Exception "+ex.getMessage());
	}
	
	}
	
	public  String getOrgName(int org_id) throws Exception
	{
		query="SELECT org_name FROM Organisation where org_id=?";
		ps=con.prepareStatement(query);
		ps.setInt(1, org_id);
		ResultSet rs=ps.executeQuery();
		rs.next();
		String org_name=rs.getString("org_name");
		return org_name;
	}
}
