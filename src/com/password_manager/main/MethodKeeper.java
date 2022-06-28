package com.password_manager.main;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.net.URL;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.internet.InternetAddress;

import org.apache.commons.codec.binary.Hex;

import com.password_maanger.cryptographer.Cryptographer;
import com.password_manager.Password.Password;
import com.password_manager.Password.PasswordOperationHandler;
import com.password_manager.dao.UserDAO;
import com.password_manager.user.User;

public class MethodKeeper 
{
	private static Scanner sc=null;
	private static String site_name="",site_url="",site_password="",site_user_name="";
	static 
	{
		sc=new Scanner(System.in);
	}
	public static String hashPassword(String master_password,byte salt[]) 
	{
		try
		{	
		//generating a salt
		

	  SecretKeyFactory	secret_key_factory=SecretKeyFactory.getInstance("PBKDF2withHmacSHA1");
		PBEKeySpec spec=new PBEKeySpec(master_password.toCharArray(),salt,20000,512);
		
		byte hash[]=secret_key_factory.generateSecret(spec).getEncoded();
	    return Hex.encodeHexString(hash)+":"+Hex.encodeHexString(salt);			
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			return "";
		}
	}
	
	
	public static String printBlock(String message)
	{
		return "\""+message+"\"";
	}
	
	public static void showUserDetails(User user)
	{	
		System.out.println("User-name: "+user.getUser_name()+"\n Role: "+MethodKeeper.getRoleAsString(user.getRole()));
		if(user.getTeam_name()!=null)
		{
			System.out.println(" Team Name: "+user.getTeam_name());
		}
	}
	
	public static String getRoleAsString(int role)
	{
		String roles[]= {"","Super-Admin","Admin","Team-Admin","Employee","User"};
		return roles[role];
	}
	
	public static Password receivePasswordDetails(User user)
	{
		int user_id=user.getUser_id();
		site_name="";site_password="";site_url="";site_user_name="";
		sc=new Scanner(System.in);
		UserDAO emp_dao=new UserDAO();
		do
		{
			System.out.print("	Enter the name for the password: ");		
			site_name=sc.nextLine();			
			if(site_name.isEmpty())
			{
				Warner.requiredWarning("Name for the password");
				continue;
			}
		}while(emp_dao.isOccupiedName(site_name,user_id));
		
		do
		{
			System.out.print("	Enter the site url (Press enter if you wish not to add the site url): ");
			site_url=sc.nextLine();			
		}while(!site_url.isEmpty()&&!MethodKeeper.isValidUrl(site_url));
		
		do
		{
			System.out.print("	Enter the username for the site: ");
			site_user_name=sc.nextLine();
			if(site_user_name.length()==0)
			{
				Warner.requiredWarning("User Name");
			}
		}while(site_user_name.length()==0);
	
		do
		{
			int option=MethodKeeper.receiveIntegerInput("	1. Enter the password for the site manually \n	2. Automatically generate a strong password");
			if(option==1)
			{
				System.out.print("	Enter your password:  ");
			site_password=sc.nextLine();
			if(site_password.length()<1)
			{
				Warner.requiredWarning("Password");
			}
			}
			else if(option==2)
			{
				site_password=new PasswordOperationHandler().autoGeneratePassword();
			}
		}while(site_password.length()<1);
		
		Password password_data=new Password();
		password_data.setSite_name(site_name);
		password_data.setSite_url(site_url);
		try
		{	
			password_data.setSite_password(site_password,1);
		}
		catch(Exception ex)
		{
			System.out.println("Exception in receive password details cryptographer "+ex.getMessage());
		}
		password_data.setSite_user_name(site_user_name);
		
		return password_data;
	}
	
	
	
	
	public static String randomStringGenerate(int len)
	{
		char upper_case[]=new char[26];
        char lower_case[]=new char[26];
        char special[]={'!','@','#','$','%','^','&','*','(',')','_','+','=','{','}','|','?','/',',','<','>'};
        int num[]=new int[10];
		String res="";
		int rand;
		 for(int ind=0;ind<26;ind++)
	        {
	        	upper_case[ind]=(char)(ind+65);
	        	lower_case[ind]=(char)(ind+97);
	        }
		 for(int ind=0;ind<num.length;ind++)
		 {
			 num[ind]=ind;
		 }
		for(int ind=0;ind<len;ind++)
		{
			rand=new Random().nextInt(4);
		
            if(rand==0)
            {
                res=res+Character.toString(upper_case[new Random().nextInt(26)]);
            }
            else if(rand==1)
            {
                res=res+Character.toString(lower_case[new Random().nextInt(26)]);
            }
            else if(rand==3)
            {
                res=res+Character.toString(special[new Random().nextInt(21)]);
            }
            else if(rand==2)
            {
            	char ch=(char) (num[new Random().nextInt(10)]+48);
                res=res+Character.toString(ch);
            }
		}
		return res;
	}
	
	public static byte[] generateSalt()
	{
		byte salt[]=new byte[32];
		SecureRandom srand=new SecureRandom();
		srand.nextBytes(salt);
		return salt;
	}
	
	public static boolean isValidEmail(String user_name)
	{
		try
		{
			InternetAddress i_net_address=new InternetAddress(user_name);
			i_net_address.validate();
			return true;			
		}
		catch(Exception ex)
		{
			System.out.println("Invalid Email Format");
			return false;
		}
	}
	
	
	public static void selectOption()
	{
		System.out.println("Select an Option: ");
	}
	
	
	
	
	public static PrivateKey getPrivateKey(String base64PrivateKey){
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }
	
	//checks if the entered url is valid or not
	public static boolean isValidUrl(String url)
	{
		try
		{
			new URL(url).toURI();
			return true;
		}
		catch(Exception ex)
		{
			System.out.println("The entered url is not a valid url, Please enter a valid url");
		}
		return false;
	}


	
	 public static void generatePublicPrivateKey(int option) 
	    {
			try
			{
				KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		        keyGen.initialize(1024);
		        KeyPair pair = keyGen.generateKeyPair();
		        PrivateKey prk=pair.getPrivate();
				 byte[] key=prk.getEncoded();
				System.out.println(Base64.getEncoder().encodeToString(prk.getEncoded()));
			}
			catch(Exception ex)
			{
				System.out.println("Exception "+ex.getMessage());
			}
	    }
	
	public static int getRole(String user_role)
	{
		List<String> role_lst=new ArrayList<> (List.of("","super-admin","admin","team-admin","employee","indi-user"));
		return role_lst.indexOf(user_role);
	}
	
	public static String getLikeSymbol()
	{
		return "\uD83D\uDC4D";
	}
	
	public static int receiveIntegerInput(String message)
	{
		int val;
		while(true)
		{
		try
		{
			System.out.println(message);
			val=sc.nextInt();
			return val;
		}
		catch(Exception ex)
		{
			System.out.println("Invalid input!!");
			sc.nextLine();
		}
		}
	}
}
