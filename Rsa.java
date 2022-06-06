import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;

public class Rsa 
{
    public static void main(String[] args) throws Exception 
    {
        // KeyPairGenerator key_gen=KeyPairGenerator.getInstance("RSA");
        // KeyPair keyPair=key_gen.generateKeyPair();
        // SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        // key_gen.initialize(1024, random);
        // PrivateKey privateKey=keyPair.getPrivate();
        // PublicKey publicKey=keyPair.getPublic();    
        // System.out.println(Hex.encodeHex(privateKey.getEncoded())+" "+Hex.encodeHex(publicKey.getEncoded()));

        System.out.printf("%8s%8s","hello\n","What's up"," ");
		System.out.printf("%15s","What Do You want to do\n");
		System.out.printf("%8s","1.Sign Up 2.Login ");
    }    
}
