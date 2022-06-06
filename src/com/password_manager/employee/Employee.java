package com.password_manager.employee;

import java.io.FileInputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Hex;

import com.password_manager.dao.EmployeeDAO;

public class Employee 
{
	 private String user_name,master_password,role,private_key,public_key,added_by;
	 private PBEKeySpec spec=null;
	 private SecretKeyFactory secret_key_factory=null;
	 private int pass_id[]=new int[1000],org_id;
	 private EmployeeDAO emp_dao=null;


	   Employee(){}
	   
	   public Employee(String user_name,String role,String master_password)
	    {
	        this.user_name=user_name;
	        this.role=role;
		    this.master_password=hashPassword(master_password);
	        generatePublicPrivateKey();
	    }
	   
	   
	   public Employee(String user_name,String role,String master_password,String public_key,String private_key,String added_by,int org_id)
	    {
	        this.user_name=user_name;
	        this.role=role;
		    this.master_password=hashPassword(master_password);
		    this.public_key=public_key;
		    this.private_key=private_key;
		    this.added_by=added_by;
		    this.org_id=org_id;
	        generatePublicPrivateKey();
	    }


		public  static String hashPassword(String master_password) 
		{
			try
			{	
			//generating a salt
			byte salt[]=new byte[32];
			SecureRandom srand=new SecureRandom();
			srand.nextBytes(salt);

		SecretKeyFactory	secret_key_factory=SecretKeyFactory.getInstance("PBKDF2withHmacSHA1");
		PBEKeySpec	spec=new PBEKeySpec(master_password.toCharArray(),salt,10000,512);
	        byte hashed_password[]=secret_key_factory.generateSecret(spec).getEncoded();
	        return (Hex.encodeHexString(hashed_password))+" "+(Hex.encodeHexString(salt));
			}
			catch(Exception ex)
			{
				System.out.println(ex.getMessage());
				return "";
			}
		}
		
		
		public  static String hashPassword(String master_password,byte salt[]) 
		{
			try
			{	
			//generating a salt
		SecretKeyFactory	secret_key_factory=SecretKeyFactory.getInstance("PBKDF2withHmacSHA1");
		PBEKeySpec	spec=new PBEKeySpec(master_password.toCharArray(),salt,10000,512);
	        byte hashed_password[]=secret_key_factory.generateSecret(spec).getEncoded();
	        return (Hex.encodeHexString(hashed_password))+" "+(Hex.encodeHexString(salt));
			}
			catch(Exception ex)
			{
				System.out.println(ex.getMessage());
				return "";
			}
		}


	    private void generatePublicPrivateKey() 
	    {
			try
			{
				KeyPairGenerator key_gen=KeyPairGenerator.getInstance("RSA");
			    KeyPair keyPair=key_gen.generateKeyPair();
			    SecureRandom random =new SecureRandom();
			    key_gen.initialize(2048, random);
			    PrivateKey privateKey=keyPair.getPrivate();
			    PublicKey publicKey=keyPair.getPublic();    
			    this.private_key=Hex.encodeHexString(privateKey.getEncoded());
			    this.public_key=Hex.encodeHexString(publicKey.getEncoded());
			   
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
	    
	    public void addEmployee(Employee emp)
	    {
	        System.out.println("Enter the user's user name");
	        Scanner sc=new Scanner(System.in);
	        String user_email=sc.next();
	        System.out.println("Enter the secret password to send to the employee");
	        String secret_token=sc.next();
	        System.out.println("Invite pending");
	        emp_dao=new EmployeeDAO();
	        emp_dao.addInviteUser(user_email,emp,secret_token);
	    }
	   
	    
	    private boolean sendEmail()
	    {
	        try
	        {
	            Properties properties=new Properties();
	            FileInputStream fis=new FileInputStream("/home/anbu/password_manager/process.properties");
	            properties.load(fis);
	            String pass=properties.getProperty("password");
	            System.out.println(pass);
	        
	            return true;
	        }
	        catch(Exception ex)
	        {
	            return false;
	        }
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

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
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
	        return "User name "+this.user_name+" Role "+this.role;
	    }

}
