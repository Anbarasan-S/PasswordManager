package com.password_manager.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.password_manager.Password.Password;
import com.password_manager.main.Client;
import com.password_manager.user.User;

public class PasswordDAO 
{
	Connection con=null;
	PreparedStatement ps=null;
	String query="";
	public PasswordDAO()
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
	
	public boolean addPassword(Password password_data,User user)
	{
		try
		{
		String site_name=password_data.getSite_name(),site_url=password_data.getSite_url(),site_password=password_data.getSite_password(0),site_username=password_data.getSite_user_name();
		site_url=site_url.length()==0?null:site_url;
		
		//Encrypt the password password mode=0
		int user_id=user.getUser_id();
		query="Insert into Password(user_id,site_name,site_user_name,site_password,site_url) VALUES(?,?,?,?,?,)";
		ps=con.prepareStatement(query);
		ps.setInt(1,user_id);
		ps.setString(2, site_name);
		ps.setString(3, site_username);
		ps.setString(4,site_password);
		ps.setString(5, site_url);
		ps.executeUpdate();
		return true;
		}
		catch(Exception ex)
		{
			System.out.println("Exception in add password method of empdao "+ex.getMessage());
			return false;
		}
	}
	
	
	public boolean changeTrashState(int pass_id[],int user_id,int status)
	{
		try
		{
			con.setAutoCommit(false);
			for(int ind=0;ind<pass_id.length;ind+=1)
			{
			int p_id=pass_id[ind];
			query="Update Password set status=? where pass_id=? AND user_id=?";
			ps=con.prepareStatement(query);
			ps.setInt(1, status);
			ps.setInt(2, p_id);
			ps.setInt(3,user_id);
			ps.executeUpdate();
			}
			con.commit();
			con.setAutoCommit(true);
			return true;
		}
		catch(Exception ex)
		{
			System.out.println("Exception in change trash stae: "+ex.getMessage());
			return false;
		}
	}
	
	
	public boolean deletePassword(int pass_id[])
	{
		try
		{
		con.setAutoCommit(false);
		for(int ind=0;ind<pass_id.length;ind+=1)
		{
		query="DELETE FROM Password where pass_id=?";
		ps=con.prepareStatement(query);
		ps.setInt(1, pass_id[ind]);
		ps.executeUpdate();
		}
		con.commit();
		con.setAutoCommit(true);
		return true;
		}
		catch(Exception ex)
		{
			System.out.println("Exception in delete password of emp dao"+ex.getMessage());
			return false;
		}
		
	}
	
	public void clearTrash(int user_id)
	{
		try
		{
		query="DELETE FROM Password where user_id=? and status=?";
		ps=con.prepareStatement(query);
		ps.setInt(1,user_id);
		ps.setInt(2, 0);
		ps.executeUpdate();
		System.out.println("All the password in the trash are permanently deleted!!");
		}
		catch(Exception ex)
		{
			System.out.println("Exception in clear trash "+ex.getMessage());
		}
	}
	
	
	public ArrayList<Password> showPassword(int status) throws Exception
	{
		int user_id=Client.getUser().getUser_id();
		query="SELECT* from Password where user_id=? and status=?";
		ps=con.prepareStatement(query);
		ps.setInt(1,user_id);
		ps.setInt(2,status);     
		ResultSet rs=ps.executeQuery();
		ArrayList<Password> lst_password=new ArrayList<>();
		while(rs.next())
		{
			Password temp_password=new Password();
			temp_password.setPass_id(rs.getInt("pass_id"));
			temp_password.setOwner_pass_id(rs.getInt("owner_pass_id"));
			temp_password.setSite_name(rs.getString("site_name"));
			temp_password.setChanged_by_id(rs.getInt("changed_by_id"));
			temp_password.setSite_url(rs.getString("site_url"));
			temp_password.setSite_password(rs.getString("site_password"),0);
			temp_password.setSite_user_name(rs.getString("site_user_name"));
			temp_password.setUser_id(rs.getInt("user_id"));
			temp_password.setCreated_at(rs.getTimestamp("created_at"));
			temp_password.setLast_changed(rs.getTimestamp("last_changed"));
			lst_password.add(temp_password);
		}
		return lst_password;
	}
	
	
	public void editPassword(Password curr_password)
	{
		try
		{
			query="UPDATE Password set site_name=?,site_url=?,site_password=?,last_changed=?,site_user_name=? where pass_id=?";			
			ps=con.prepareStatement(query);
			ps.setString(1,curr_password.getSite_name());
			ps.setString(2, curr_password.getSite_url());
			ps.setString(3, curr_password.getSite_password(0));
			ps.setTimestamp(4, curr_password.getLast_changed());
			ps.setString(5, curr_password.getSite_user_name());
			ps.setInt(6, curr_password.getPass_id());
			ps.executeUpdate();
		}
		catch(Exception ex)
		{
			System.out.println("Exception in employee dao getPasswordById method: "+ex.getMessage());
			return;
		}
	}
	
	public boolean isActivePassword(int pass_id)
	{
		try
		{
			query="SELECT* FROM Password WHERE pass_id=? and status=1";
			ps=con.prepareStatement(query);
			ps.setInt(1, pass_id);			
			ResultSet rs=ps.executeQuery();
			if(rs.next())
			{
				return true;
			}
			return false;
		}
		catch(Exception ex)
		{
			System.out.println("Exception in Active password "+ex.getMessage());
			return false;
		}
	}
		
	
	
	public boolean sharePassword(List<Password>passwords)
	{
		try
		{
			con.setAutoCommit(false);
			query="INSERT INTO Password(pass_id,user_id,site_name,site_url,site_password,created_at,last_changed,changed_by_id,owner_pass_id,site_user_name,status,permission_role) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
			for(Password pass:passwords)
			{
				ps=con.prepareStatement(query);
				ps.setInt(1, pass.getPass_id());
				ps.setInt(2, pass.getUser_id());
				ps.setString(3, pass.getSite_name());
				ps.setString(4, pass.getSite_url());
				ps.setString(5,pass.getSite_password(0));
				ps.setTimestamp(6,pass.getCreated_at());
				ps.setTimestamp(7, pass.getLast_changed());
				ps.setInt(8, pass.getChanged_by_id());
				ps.setInt(9,pass.getOwner_pass_id());
				ps.setString(10,pass.getSite_user_name());
				ps.setInt(11, 1);
				ps.setInt(12, pass.getPermissionRole());
				ps.executeUpdate();
			}			
			con.commit();
			con.setAutoCommit(true);
			return true;
		}
		catch(Exception ex)
		{
			System.out.println("Exception in the share password method of password dao "+ex.getMessage());
			return false;
		}
	}
}
