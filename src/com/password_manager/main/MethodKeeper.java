package com.password_manager.main;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.mail.internet.InternetAddress;

import org.apache.commons.codec.binary.Hex;

public class MethodKeeper 
{

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
	
	public static int getRole(String user_role)
	{
		List<String> role_lst=new ArrayList<> (List.of("","super-admin","admin","team-admin","employee","user"));
		return role_lst.indexOf(user_role);
	}
}
