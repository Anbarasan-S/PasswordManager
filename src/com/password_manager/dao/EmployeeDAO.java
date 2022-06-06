package com.password_manager.dao;
import com.password_manager.employee.*;
import java.sql.*;

import org.apache.commons.codec.binary.Hex;

public class EmployeeDAO
{
	private String query;
	private Connection con=null;
	private PreparedStatement ps=null;
	
	public EmployeeDAO()		
	{
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/password_manager?useSSL=false","root","");
		}
		catch(Exception ex)
		{
			System.out.println("Class Not Found "+ex.getMessage());
		}
	}
	
	public boolean userExists(String user_name)
	{
		try
		{
		query="SELECT * FROM employee where employee.user_name=?";
		ps=con.prepareStatement(query);
		ps.setString(1,user_name);
		ResultSet rs=ps.executeQuery();
	   boolean is_found=rs.next(); 
		if(is_found)
		{
			return true;			
		}
			return false;
		}
		catch(Exception ex)
		{
			System.out.println("Exception in user exists method "+ex.getMessage());
			return true;
		}
	}
	
	private void incrementOrgId(Employee emp) throws Exception
	{
		query="SELECT MAX(org_id) AS MAX FROM employee";
		ps=con.prepareStatement(query);
		ResultSet rs=ps.executeQuery();
		int org_id;
		rs.next();
		org_id=rs.getInt("MAX");	
		if(rs.wasNull())
		{
			org_id=0;
		}
		else
		{
			org_id=rs.getInt("MAX");						
		}
		emp.setOrg_id(org_id+1);
	}
	
	
	public void addInviteUser(String user_email,Employee emp,String secret_token)
	{
		try
		{
			query="SELECT* FROM invited_employees where org_id=?";
			ps=con.prepareStatement(query);
			ps.setInt(1, emp.getOrg_id());
			ResultSet rs=ps.executeQuery();
			boolean has_next=rs.next();
			if(has_next)
			{
				System.out.println("The user was already invited from your organisation ");
				return;
			}
			else if(userExists(user_email))
			{
				System.out.println("The user was already a part of an organisation ");
			}
			query="INSERT INTO invited_employees(org_id,secret_token,org_user_name,user_name) VALUES(?,?,?,?)";
			ps=con.prepareStatement(query);
			ps.setInt(1,emp.getOrg_id());
			ps.setString(2,secret_token);
			ps.setString(3,emp.getUser_name());
			ps.setString(4,user_email);
			ps.executeUpdate();
			System.out.println("Success");
		}
		catch(Exception ex)
		{
			System.out.println("Exception "+ex.getMessage());
		}
	}


	public boolean verifySecretToken(String user_name,String secret_token,String catch_data[],int org_id) 
	{
		try
		{
		query="SELECT* FROM invited_employees WHERE user_name=? and org_id=?";
		ps=con.prepareStatement(query);
		ps.setString(1, user_name);
		ps.setInt(2,org_id);
		ps.setString(1,user_name);
		ResultSet rs=ps.executeQuery();
		boolean has_next=rs.next();
		if(!has_next)
		{
			 return false;
		}
	//verifying is the password in the db matches the secret_token provided by the user
		if(rs.getString("secret_token").equals(secret_token))
		{
		    catch_data[0]=String.valueOf(rs.getInt("org_id"));
		    catch_data[1]=rs.getString("org_user_name");
			return true;	
		}
		return false;
		}
		catch(Exception ex)
		{
			System.out.println("Exception "+ex.getMessage());
			return false;
		}
	}
	
	public Employee verifyHashedPassword(String original_password,String user_name)
	{
		try
		{
		query="SELECT * FROM employee WHERE user_name=?";
		ps=con.prepareStatement(query);
		ps.setString(1, user_name);
		ResultSet rs=ps.executeQuery();
		rs.next();
		String hashed_password=rs.getString("master_password");
		byte[] salt=Hex.decodeHex(hashed_password.split(" ")[1]);
		String original_hashed_password=Employee.hashPassword(original_password,salt);
		
		if(original_hashed_password.equals(hashed_password))
		{
			String user_role=rs.getString("user_role");
			int org_id=rs.getInt("org_id");
			String public_key=rs.getString("public_key");
			String private_key=rs.getString("private_key");
			String added_by=rs.getString("added_by");
			
			System.out.println("Login Successful");
			
			return new Employee(user_name,user_role,original_password,public_key,private_key,added_by,org_id);
		}
		return null;
		
		}
		catch(Exception ex)
		{
			System.out.println("Exception in verifyHash method: "+ex.getMessage());
			return null;
		}
	}

	public boolean createEmployee(Employee emp,String added_by)
	{
		try
		{
			if(userExists(emp.getUser_name()))
			{
				System.out.println("The user email already exists");
				return false;
			}
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/password_manager","root","");
			if(emp.getRole()=="Super-admin")
			{
				incrementOrgId(emp);
				added_by="owner";
			}
			query="INSERT into employee (user_name,master_password,user_role,org_id,public_key,private_key,added_by) VALUES(?,?,?,?,?,?,?)";
			ps=con.prepareStatement(query);
			ps.setString(1, emp.getUser_name());
			ps.setString(2, emp.getMaster_password());
			ps.setString(3, emp.getRole());
			ps.setInt(4, emp.getOrg_id());
			ps.setString(5,emp.getPublic_key());
			ps.setString(6,emp.getPrivate_key());
			ps.setString(7, added_by);
			ps.executeUpdate();
			if(emp.getRole()=="Employee")
			{
				query="SELECT id FROM password_manager.invited_employees where user_name=?";				
				ps=con.prepareStatement(query);
				ps.setString(1,emp.getUser_name());
				ResultSet rs=ps.executeQuery();
				while(rs.next())
				{
					query="DELETE FROM invited_employees WHERE id=?";
					ps=con.prepareStatement(query);
					ps.setInt(1, rs.getInt("id"));
					ps.executeUpdate();
				}
			}
			
			con.close();
			return true;
		}
		catch(Exception ex)
		{
			System.out.println("Exception "+ex.getMessage());
			return false;
		}
	}
	
}
