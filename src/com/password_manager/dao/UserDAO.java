package com.password_manager.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;

import com.password_manager.Password.Password;
import com.password_manager.email.EmailServer;
import com.password_manager.main.MethodKeeper;
import com.password_manager.main.Client;
import com.password_manager.organisation.Organisation;
import com.password_manager.user.User;

public class UserDAO
{
	
	private  String query;
	private  Connection con=null;
	private  PreparedStatement ps=null;
	

	public UserDAO()		
	{
		try
		{
			con=ConnectionProvider.getInstance().getConnection();
		}
		catch(Exception ex)
		{
			System.out.println("Exception in user dao "+ex.getMessage());
		}
	}
	
	public  boolean createOrgWithUser(Organisation org,User user)
	{
		String org_name=org.getOrgName(),email=user.getUser_name(),hashed_password=user.getMaster_password();
		int role=user.getRole();
		try
		{
			
		con=DriverManager.getConnection("jdbc:mysql://localhost:3306/PASSWORD_MANAGER?useSSL=false","root","");
		con.setAutoCommit(false);
		//First creates org
		ps=con.prepareStatement("INSERT INTO Organisation(org_name) VALUES(?)");
		ps.setString(1,org_name);
		ps.executeUpdate();
		
		
		//then create the user/userloyee
		ps=con.prepareStatement("INSERT INTO User (email,master_password,org_id,user_role,private_key,public_key) VALUES(?,?,last_insert_id(),?,?,?)");
		ps.setString(1,email.toLowerCase());
		ps.setString(2,hashed_password);
		ps.setInt(3, role);
		ps.setString(4,user.getPrivate_key());
		ps.setString(5,user.getPublic_key());
		ps.executeUpdate();
		
		//getting the user id and setting it to the user
		query="SELECT last_insert_id()";
		ps=con.prepareStatement(query);
	  ResultSet rs=ps.executeQuery();
	  rs.next();
		int user_id=rs.getInt("last_insert_id()");
		user.setUser_id(user_id);
		con.commit();
		con.setAutoCommit(true);
		return true;
		}
		catch(Exception ex)
		{
			System.out.println(ex);
			return false;
		}
	}
	
	public boolean userExists(String user_name)
	{
		try
		{
		query="SELECT * FROM User where email=?";
		ps=con.prepareStatement(query);
		ps.setString(1,user_name.toLowerCase());
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
	
	public boolean removeUser(int user_id)
	{
		try
		{
			con.setAutoCommit(false);
		
			//Deleting the password not owned by the user
			query="DELETE FROM Password where user_id=? AND is_own=?";
			ps=con.prepareStatement(query);
			ps.setInt(1,user_id);
			ps.setInt(2, 0);
			ps.executeUpdate();
			//Removing the user from the team member table
			query="DELETE FROM TeamMember WHERE team_member_id=?";
			ps=con.prepareStatement(query);
			ps.setInt(1,user_id);
			ps.executeUpdate();
			//Setting status of the user to inactive and removing the user from the org
			query="UPDATE User set status=?,org_id=?,team_id=? where user_id=?";
			ps=con.prepareStatement(query);
			ps.setInt(1,2);
			ps.setString(2,null);
			ps.setString(3,null);
			ps.setInt(4, user_id);
			ps.executeUpdate();
			con.commit();
				
			con.setAutoCommit(true);
			return true;
		}
		catch(Exception ex)
		{
			System.out.println("Problem in removing the user "+ex.getMessage());
			return false;
		}
	}
	
	public boolean userExists(String user_name,int org_id)
	{
		try
		{
		query="SELECT * FROM User where email=? and org_id=?";
		ps=con.prepareStatement(query);
		ps.setString(1,user_name.toLowerCase());
		ps.setInt(2, org_id);
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
	
	
	
	public boolean isOccupiedName(String name,int user_id)
	{
		try
		{
		name=name.trim();
		query="SELECT * FROM Password WHERE site_name=? and user_id=?";
		ps=con.prepareStatement(query);
		ps.setString(1, name);
		ps.setInt(2, user_id);
		ResultSet rs=ps.executeQuery();
		boolean has_next=rs.next();
		if(has_next)
		{
			System.out.println("Another password with the same name already exists. Please try adding the password with any other name");
			return true;
		}
		return false;
		}
		catch(Exception ex)
		{
			System.out.println("Oops! Internal Server Error");
		}
		return false;
	}
	
	
	public ResultSet viewUsers(User user)
	{
		try
		{
		query="SELECT*FROM User where org_id=? and NOT(user_id=?) ORDER BY user_id";
		ps=con.prepareStatement(query);
		ps.setInt(1, user.getOrg_id());
		ps.setInt(2, user.getUser_id());
		ResultSet rs=ps.executeQuery();
		return rs;
		}
		catch(Exception ex)
		{
			System.out.println("Exception in viewUsers of dao object "+ex.toString());
			return null;
		}
	}
	
	
	
	
	
	public void addInviteUser(String user_email,User user)
	{
		try
		{
			if(!MethodKeeper.isValidEmail(user_email))
			{
				return;
			}
			query="SELECT* FROM Invitees where inviter_org_id=? AND invitee_email=?";
			ps=con.prepareStatement(query);
			ps.setInt(1, user.getOrg_id());
			ps.setString(2, user_email.toLowerCase());
			ResultSet rs=ps.executeQuery();
			boolean has_next=rs.next();
			if(has_next)
			{
				System.out.println("The user was already invited from your organisation ");
				return;
			}
			else if(userExists(user_email))
			{
				System.out.println("The user was already a part of an organisation. Try adding other users");
				return;
			}
			
			con.setAutoCommit(false);
			int otp=1000+(int)(Math.random()*((9999-1000)+1));
			String invite_token=otp+":"+user.getOrg_id();
			invite_token=new String(Base64.getEncoder().encode(invite_token.getBytes()));
			String message="Invite from "+user.getUser_name()+" to join the organisation "+ new OrganisationDAO().getOrgName(user.getOrg_id())+ " \nSECRET TOKEN: "+invite_token;
            EmailServer.sendMail(user_email,"Password Manager Verification",message);
			query="INSERT INTO Invitees(inviter_org_id,invitee_token,invitee_email) VALUES(?,?,?)";
			ps=con.prepareStatement(query);
			ps.setInt(1,user.getOrg_id());
			ps.setString(2,invite_token);
			ps.setString(3,user_email);
			ps.executeUpdate();
			con.commit();
			con.setAutoCommit(true);
		}
		catch(Exception ex)
		{
			System.out.println("Exception "+ex.getMessage());
		}
	}


	public boolean verifySecretToken(String user_name,String secret_token,int org_id) 
	{
		try
		{
		query="SELECT* FROM Invitees WHERE invitee_email=? and inviter_org_id=?";
		ps=con.prepareStatement(query);
		ps.setString(1, user_name);
		ps.setString(1,user_name);
		ps.setInt(2,org_id);
		ResultSet rs=ps.executeQuery();
		boolean has_next=rs.next();
		if(!has_next)
		{
			 return false;
		}
		
	//verifying if the secret_token in the db matches the secret_token provided by the user
		if(rs.getString("invitee_token").equals(secret_token))
		{
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
	
	public User verifyHashedPassword(String original_password,String user_name)
	{
		try
		{
			
		query="SELECT * FROM User WHERE email=?";
		ps=con.prepareStatement(query);
		ps.setString(1, user_name);
		ResultSet rs=ps.executeQuery();
		rs.next();
		String hashed_password=rs.getString("master_password");
		byte[] salt=Hex.decodeHex(hashed_password.split(":")[1]);
		String original_hashed_password=MethodKeeper.hashPassword(original_password,salt);
		int user_id;
		
		if(original_hashed_password.equals(hashed_password))
		{
			String user_role=rs.getString("user_role");
			int org_id=rs.getInt("org_id");
			user_name=rs.getString("email");
			user_id=rs.getInt("user_id");
			String status=rs.getString("status");
			String public_key=rs.getString("public_key");
			String private_key=rs.getString("private_key");
			
			
			if(status=="inactive")
			{
				System.out.println("Your account is inactive and you can't login.");
				return null;
			}
			
			User retrived_user=new User();
			retrived_user.setOrg_id(org_id);
			retrived_user.setUser_name(user_name);
			retrived_user.setRole(MethodKeeper.getRole(user_role));
			retrived_user.setPublic_key(public_key);
			retrived_user.setUser_id(user_id);
			retrived_user.setMaster_password(original_password);
			return retrived_user;
		}
		return null;
		
		}
		catch(Exception ex)
		{
			System.out.println("Exception in verifyHash method: "+ex.getMessage());
			return null;
		}
	}

	public boolean createEmployee(User user)
	{
		try
		{
			
			//Checks if the user exists before creating 
			if(userExists(user.getUser_name()))
			{
				System.out.println("The user email already exists");
				return false;
			}
			
			con.setAutoCommit(false);
			query="INSERT into User (email,master_password,user_role,org_id,public_key,private_key) VALUES(?,?,?,?,?,?)";
			ps=con.prepareStatement(query);
			ps.setString(1, user.getUser_name().toLowerCase());
			ps.setString(2, user.getMaster_password());
			ps.setInt(3, user.getRole());
			ps.setInt(4, user.getOrg_id());
			ps.setString(5,user.getPublic_key());
			ps.setString(6,user.getPrivate_key());
			ps.executeUpdate();
			query="SELECT last_insert_id()";
			ps=con.prepareStatement(query);
		  ResultSet rs=ps.executeQuery();
		  rs.next();
			int user_id=rs.getInt("last_insert_id()");
			user.setUser_id(user_id);
			query="DELETE FROM PASSWORD_MANAGER.Invitees where invitee_email=?";				
			ps=con.prepareStatement(query);
			ps.setString(1,user.getUser_name());
			ps.executeUpdate();
			con.commit();
			con.setAutoCommit(true);
			con.close();
			return true;
		}
		catch(Exception ex)
		{
			user=null;
			System.out.println("Exception "+ex.getMessage());
			return false;
		}
	}
	
	public boolean createUser(User user)
	{
		try
		{
			
			//Checks if the user exists before creating 
			if(userExists(user.getUser_name()))
			{
				System.out.println("The user email already exists");
				return false;
			}
		
			con.setAutoCommit(false);
			query="INSERT into  User(email,master_password,user_role,public_key,private_key) VALUES(?,?,?,?,?)";
			ps=con.prepareStatement(query);
			ps.setString(1, user.getUser_name().toLowerCase());
			ps.setString(2, user.getMaster_password());
			ps.setInt(3,user.getRole());
			ps.setString(4,user.getPublic_key());
			ps.setString(5,user.getPrivate_key());
			ps.executeUpdate();
			query="SELECT last_insert_id()";
			ps=con.prepareStatement(query);
		  ResultSet rs=ps.executeQuery();
			int user_id=rs.getInt("user_id");
			user.setUser_id(user_id);
			con.commit();
			con.setAutoCommit(true);
			return true;
		}
		catch(Exception ex)
		{
			System.out.println("Exception "+ex.getMessage());
			return false;
		}
	}
	
	public String getUserPrivateKey()
	{
		try
		{
			query="SELECT private_key from User where user_id=?";
			ps=con.prepareStatement(query);			
			ps.setInt(1, Client.getUser().getUser_id());
			ResultSet rs=ps.executeQuery();
			rs.next();
			return rs.getString("private_key");
		}
		catch(Exception ex)
		{
			System.out.println("Exception in get user private key "+ex.getMessage());
			return null;
		}	
	}
}
