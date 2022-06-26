package com.password_manager.user;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.password_maanger.cryptographer.Cryptographer;
import com.password_manager.Password.Password;
import com.password_manager.dao.UserDAO;
import com.password_manager.dao.PasswordDAO;
import com.password_manager.main.MethodKeeper;

public class User 
{
	 private String user_name,master_password,private_key,public_key,team_name;
     private int org_id,role,user_id,team_id,is_team_admin;

     
	public String getTeam_name() 
	{
		return team_name;
	}

	public void setTeam_name(String team_name) {
		this.team_name = team_name;
	}

	public void setIs_team_admin(int is_team_admin) {
		this.is_team_admin = is_team_admin;
	}

	private UserDAO user_dao=null;
	  
	  public User(){user_dao=new UserDAO();}
	  
	  
	   
	   public int getIs_team_admin() {
		return is_team_admin;
	}

	public User(String user_name,int role,String master_password)
	    {
			   privatePublicKeySetter(master_password);
			   this.user_name=user_name;
			   this.role=role;
			   this.master_password=master_password;
			   user_dao=new UserDAO();
	    }
	   
	   
	   
	   	private void privatePublicKeySetter(String master_password)
	   	{
	        KeyPair kpair=null;
	   		Cryptographer new_cryptographer=new Cryptographer();
	   		kpair=new_cryptographer.RsaPublicPrivateGenerator();
	   		try
	   		{
	   			this.public_key=Base64.getEncoder().encodeToString(kpair.getPublic().getEncoded());	   			
	   			this.private_key=new_cryptographer.encrypt(Base64.getEncoder().encodeToString(kpair.getPrivate().getEncoded()), master_password);
	   		}
	   		catch(Exception ex)
	   		{
	   			System.out.println(ex.getMessage());
	   		}
	   	}
	   	
	  
	  
	    protected void setOrgId(int org_id)
	    {
	    	this.org_id=org_id;
	    }
	    
	    
	    
		public String getUser_name() {
			return user_name;
		}

		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}
		
		public String getPlainMasterPassword()
		{
			return this.master_password;
		}
		public String getMaster_password() 
		{
			return 	new Cryptographer().decryptMasterPassword(this.master_password);
		}

		public void setMaster_password(String master_password) {
			this.master_password =master_password;
		}

		public int getRole() {
			return role;
		}

		public void setRole(int role) {
			this.role = role;
		}

		public String getPrivate_key() {
			return private_key;
		}

		public int getUser_id() {
			return user_id;
		}

		public void setUser_id(int user_id) {
			this.user_id = user_id;
		}

		public void setPrivate_key(String private_key) {
			this.private_key = private_key;
		}

		public String getPublic_key() {
			return public_key;
		}

		public void setPublic_key(String public_key) {
			this.public_key = public_key;
		}

	

		public int getOrg_id() {
			return org_id;
		}

		public void setOrg_id(int org_id) {
			this.org_id = org_id;
		}
		
		public List<User> viewUsers() 
		{
			try
			{
			if(this.getRole()==5)
			{
				System.out.println("You are not allowed to access this menu as you are not a part of any organisation");
				return null;
			}
			ResultSet rs=user_dao.viewUsers(this);
			List<User> usr_lst=new ArrayList<>();
			int ind=1;
			
			while(rs.next())
			{
				String user_name=rs.getString("email");
				int user_id=rs.getInt("user_id");
				int org_id=rs.getInt("org_id");
				String user_role=rs.getString("user_role");
				int team_id=rs.getInt("team_id");
				Date date=rs.getDate("created_at");
				User temp_user=new User();
				temp_user.setUser_name(user_name);
				temp_user.setOrg_id(org_id);
				temp_user.setUser_id(user_id);
				temp_user.setRole(MethodKeeper.getRole(user_role));
				temp_user.setTeam_id(team_id);
				System.out.print(ind+".) ");
				MethodKeeper.showUserDetails(temp_user);
				usr_lst.add(temp_user);
			}

			return usr_lst;
			}
			catch(Exception ex)
			{
				System.out.println("Exception in view users method of User class "+ex.toString());
				return null;
			}
		}
	


		public int getTeam_id() 
		{
			return team_id;
		}

		public void setTeam_id(int team_id) {
			this.team_id = team_id;
		}

		public String toString()
	    {
			String role[]= {"","Super-Admin","Admin","Team Admin","Employee","User"};
	        return "User Details:\n\n User name: "+this.user_name+"\n Role: "+role[this.role];
	    }
}
