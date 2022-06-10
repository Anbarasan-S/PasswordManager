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
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.internet.InternetAddress;

import org.apache.commons.codec.binary.Hex;

import com.password_manager.Password.Password;
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
	
	public static void showUserDetails(User user)
	{	
		System.out.println("User-name: "+user.getUser_name()+"\n Role: "+MethodKeeper.getRoleAsString(user.getRole())+"\n Team: "+user.getTeam_name());
	}
	
	public static String getRoleAsString(int role_enum)
	{
		String roles[]= {"","Super-Admin","Admin","Team-Admin","Employee","User"};
		return roles[role_enum];
	}
	
	public static Password receivePasswordDetails()
	{
		site_name="";site_password="";site_url="";site_user_name="";
		Scanner sc=new Scanner(System.in);
		Map<String,String>details;
		System.out.println("Enter The site name: ");
		site_name=sc.nextLine();
		System.out.println("Enter the site url, Press enter if you wish not to add the site url: ");
		site_url=sc.nextLine();
		do
		{
			System.out.println("Enter the username for the site: ");
			site_user_name=sc.nextLine();
			if(site_user_name.length()==0)
			{
				Warner.requiredWarning("User Name");
			}
		}while(site_user_name.length()==0);
	
		do
		{
			System.out.println("1. Enter the password for the site manually \n2. Automatically generate a strong password");
			int option=sc.nextInt();
			if(option==1)
			{
				sc.nextLine();
			site_password=sc.nextLine();
			if(site_password.length()<1)
			{
				Warner.requiredWarning("Password");
			}
			}
			else if(option==2)
			{
				autoGeneratePassword();
			}
		}while(site_password.length()<1);
		
		Password password_data=new Password();
		password_data.setSite_name(site_name);
		password_data.setSite_url(site_url);
		password_data.setSite_password(site_password);
		password_data.setIs_own(1);
		password_data.setSite_user_name(site_user_name);
		
		return password_data;
	}
	
	public static String autoGeneratePassword()
	{
		

        char upper_case[]=new char[26];
        char lower_case[]=new char[26];
        for(int i=0;i<26;i++)
        {
        	upper_case[i]=(char)(i+65);
        	lower_case[i]=(char)(i+97);
        }
        char special[]={'!','@','#','$','%','^','&','*','(',')','_','+','=','{','}','|','?','/',',','<','>'};
        int num[]=new int[10];
	        System.out.println("Enter the length of the string: ");
	        int len=sc.nextInt();
	        len=len<4?4:len;
	            int rand;
	            String pass="";
	            for(int i=0;i<len;i++)
	            {
	                rand=new Random().nextInt(4);
	                if(rand==1)
	                {
	                    pass+=(Character.toString(upper_case[new Random().nextInt(26)]));
	                }
	                else if(rand==2)
	                {
	                    pass+=(Character.toString(lower_case[new Random().nextInt(26)]));
	                }
	                else if(rand==3)
	                {
	                    pass+=(Character.toString(special[new Random().nextInt(21)]));
	                }
	                else
	                {
	                	char ch=(char) (num[new Random().nextInt(10)]+48);
	                    pass+=(Character.toString(ch));
	                }
	            }
	        return pass;
	}
	
	
	public static String randomStringGenerate(int len)
	{
		char upper_case[]=new char[26];
        char lower_case[]=new char[26];
        char special[]={'!','@','#','$','%','^','&','*','(',')','_','+','=','{','}','|','?','/',',','<','>'};
        int num[]=new int[10];
		String res="";
		int rand;
		 for(int i=0;i<26;i++)
	        {
	        	upper_case[i]=(char)(i+65);
	        	lower_case[i]=(char)(i+97);
	        }
		for(int i=0;i<len;i++)
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
	
	//AES-256 ENCRYPTION
	public static String encrypt(String plain_text,String secret_key)
	{
		String salt=randomStringGenerate(16);

	// This method use to encrypt to string
		try {

			byte[] iv =new byte[16];
			IvParameterSpec ivspec= new IvParameterSpec(iv);

			// Create SecretKeyFactory
			SecretKeyFactory factory= SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(secret_key.toCharArray(), salt.getBytes(),65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(),"AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey,ivspec);
			// Return encrypted string
			return salt+" "+Base64.getEncoder().encodeToString(cipher.doFinal(plain_text.getBytes("UTF-8")));
		}
		catch (Exception e) 
		{
			System.out.println("Error while encrypting: "
							+ e.toString());
		}
		return null;
	}
	
	
	public static String decrypt(String encrypted_text,String secret_key)
	{
		try {
			  
            // Default byte array
			String salt=encrypted_text.split(" ")[0];
			encrypted_text=encrypted_text.split(" ")[1];
            byte[] iv =new byte[16];
            IvParameterSpec ivspec= new IvParameterSpec(iv);
            SecretKeyFactory factory= SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secret_key.toCharArray(), salt.getBytes(),65536,256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey,ivspec);
            // Return decrypted string
            return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted_text)));
        }
        catch (Exception e) {
            System.out.println("Error while decrypting: "
                               + e.toString());
        }
        return null;
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
		List<String> role_lst=new ArrayList<> (List.of("","super-admin","admin","team-admin","employee","user"));
		return role_lst.indexOf(user_role);
	}
}
