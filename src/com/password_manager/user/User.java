package com.password_manager.user;

import java.io.FileInputStream;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;
import java.util.Scanner;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Hex;

import com.password_manager.dao.EmployeeDAO;
import com.password_manager.email.EmailServer;
import com.password_manager.main.MethodKeeper;

public class User 
{
	 private String user_name,master_password,private_key,public_key;
	 private PBEKeySpec pbe_key_spec=null;
	 private SecretKeyFactory secret_key_factor=null;
	  private int pass_id[]=new int[1000],org_id,role;
	  private EmployeeDAO emp_dao=null;
	  
	  public User(){emp_dao=new EmployeeDAO();}
	   
	   public User(String user_name,int role,String master_password)
	    {
	        this.user_name=user_name;
	        this.role=role;
			this.master_password=MethodKeeper.hashPassword(master_password,MethodKeeper.generateSalt());
	        generatePublicPrivateKey();
	        emp_dao=new EmployeeDAO();
	    }
	   
	   public void removeUser(String user_name)
	   {
		   if(this.getUser_name()==user_name)
		   {
			   System.out.println("You cannot remove your account. Instead try delete account");
			   return;
		   }
		   if(!emp_dao.userExists(user_name,org_id))
		   {
			   System.out.println("Sorry the user does not exists in your organisation");
			   return;
		   }
		   
	   }

	    private void generatePublicPrivateKey() 
	    {
			try
			{
				KeyPairGenerator key_pair=KeyPairGenerator.getInstance("RSA");

			}
			catch(Exception ex)
			{
				System.out.println("Exception "+ex.getMessage());
			}
	    }
	    
	    protected void setOrgId(int org_id)
	    {
	    	this.org_id=org_id;
	    }
	    
	    public void addEmployee(User emp,String user_email_id) throws Exception
	    {
	        emp_dao.addInviteUser(user_email_id, emp);
	    }
	    
		public String getUser_name() {
			return user_name;
		}

		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}

		public String getMaster_password() {
			return master_password;
		}

		public void setMaster_password(String master_password) {
			this.master_password = master_password;
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

		public void setPrivate_key(String private_key) {
			this.private_key = private_key;
		}

		public String getPublic_key() {
			return public_key;
		}

		public void setPublic_key(String public_key) {
			this.public_key = public_key;
		}

		public int[] getPass_id() {
			return pass_id;
		}

		public void setPass_id(int[] pass_id) {
			this.pass_id = pass_id;
		}

		public int getOrg_id() {
			return org_id;
		}

		public void setOrg_id(int org_id) {
			this.org_id = org_id;
		}


		public String toString()
	    {
			String role[]= {"","Super-Admin","Admin","Team Admin","Employee","User"};
	        return "User Details:\n User name: "+this.user_name+"\n Role: "+role[this.role];
	    }

}
