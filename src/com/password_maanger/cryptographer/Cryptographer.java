package com.password_maanger.cryptographer;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.password_manager.main.MethodKeeper;
import com.password_manager.dao.UserDAO;
import com.password_manager.main.Client;
import com.password_manager.user.User;

public class Cryptographer 
{
	//AES-256 ENCRYPTION
	public String encrypt(String plain_text,String secret_key)
	{
		String salt=MethodKeeper.randomStringGenerate(16);

	// This method use to encrypt to string
		try 
		{

			byte[] iv =new byte[16];
			IvParameterSpec ivspec= new IvParameterSpec(iv);

			// Create SecretKeyFactory
			SecretKeyFactory factory= SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(secret_key.toCharArray(), salt.getBytes(),65536, 256);
			SecretKey tmp=factory.generateSecret(spec);
			SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(),"AES");
			Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
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
	
	
	public String decrypt(String encrypted_text,String secret_key)
	{
		try 
		{
			  
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
	
	
	public static PrivateKey loadPrivateKey(String key64) 
	{
		try
		{		
//		Security.addProvider(new BouncyCastleProvider());
	    byte[] byte_data = Base64.getDecoder().decode(key64.getBytes());
	    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(byte_data);
	    KeyFactory fact = KeyFactory.getInstance("RSA");
	    PrivateKey priv = fact.generatePrivate(keySpec);
	    return priv;
		}
		catch(Exception ex)
		{
			System.out.println("Exception in loadPrivateKey method "+ex.getMessage());
			return null;
		}
	}
	
	public static PublicKey loadPublicKey(String key64)
	{
		try
		{
			byte[] byte_data=Base64.getDecoder().decode(key64);
		X509EncodedKeySpec spec = new X509EncodedKeySpec(byte_data);
		return KeyFactory.getInstance("RSA").generatePublic(spec);
		}
		catch(Exception ex)
		{
			System.out.println("Exception in load public key method"+ex.getMessage());
			return null;
		}
	}
	

	public KeyPair RsaPublicPrivateGenerator() 
	{
		try
		{
		KeyPairGenerator keyPairGen=KeyPairGenerator.getInstance("RSA");
		keyPairGen.initialize(2048);
		KeyPair kpair=keyPairGen.generateKeyPair();
		return kpair;
		}
		catch(Exception ex)
		{
			System.out.println("Exception "+ex.getMessage());
			return null;
		}
	}
	
	
	public String ecEncryptWithPublicKey(String key,String data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidParameterSpecException
	{
		PublicKey pub_key=loadPublicKey(key);	
		Cipher cipher=Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pub_key);
		byte encr_byte[]=cipher.doFinal(data.getBytes());
		return Base64.getEncoder().encodeToString(encr_byte);
    }
	
	public String decryptWithPrivateKey(String data)
	{
		try
		{
		String key=new UserDAO().getUserPrivateKey();
		key=decrypt(key,Client.getUser().getMaster_password());
		PrivateKey priv_key=loadPrivateKey(key);
		Cipher cipher=Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, priv_key);
		byte decr_byte[]=cipher.doFinal(Base64.getDecoder().decode(data));
		return new String(decr_byte);
		}
		catch(Exception ex)
		{
			System.out.println("Exception in decrypt with private key"+ex.getMessage());
			return null;
	}
	}
}
