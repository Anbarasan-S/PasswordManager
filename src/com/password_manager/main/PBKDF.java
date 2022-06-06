package com.password_manager.main;
import java.security.SecureRandom;
import java.util.*;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Hex;

public class PBKDF
{
    private static String str;
    public static byte[] generateSalt()
    {
        byte salt[]=new byte[32];
        SecureRandom srand=new SecureRandom();
        srand.nextBytes(salt);
        return salt;
    }

    public static byte[] generateHash(String password,byte salt[]) throws Exception
    {
        char password_arr[]=password.toCharArray();
        SecretKeyFactory sKeyFactory=SecretKeyFactory.getInstance("PBKDF2withHmacSHA1");
        PBEKeySpec pbe_key_spec=new PBEKeySpec(password_arr,salt,10000,512);
        byte hashed_password[]=sKeyFactory.generateSecret(pbe_key_spec).getEncoded();
        String hashed_string=Arrays.toString(hashed_password);
        hashed_string+="|"+Arrays.toString(salt);
       str= (Hex.encodeHexString(hashed_password))+"|"+(Hex.encodeHexString(salt));
        // StringTokenizer strtok=new StringTokenizer(hashed_string,",][|");
        // byte stored_password[]=new byte[64];
        // int ptr=0;
        // str=strtok.nextToken(); 
        // while(strtok.hasMoreTokens())
        // {
        //    str=str.trim();
        //    stored_password[ptr++]=Byte.parseByte(str);
        //    str=strtok.nextToken();
        // }
        return hashed_password;
    }
    private static boolean comparePasswords(byte[] password,byte[] salt) 
    {
        String recent_hashed=Hex.encodeHexString(password);
        recent_hashed+="|"+Hex.encodeHexString(salt);
        
        return recent_hashed.equals(str);
    }
    public static void main(String[] args) throws Exception
    {
        Scanner sc=new Scanner(System.in);
        String password=sc.next();    
        //Compare function 
        byte salt[] = generateSalt();
        byte salt2[]=generateSalt();
       byte ans[]=generateHash(password,salt);
       byte ans2[]=generateHash(password,salt);
       System.out.println(comparePasswords(ans, salt));
       System.out.println(Arrays.toString(ans2));
    }    
}
