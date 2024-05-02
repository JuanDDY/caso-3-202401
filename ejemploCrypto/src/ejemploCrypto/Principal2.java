package ejemploCrypto;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Principal2 {
	public final static String textoSim = "Mensaje Simétrico";
	public final static String textoAsim = "Mensaje Asimétrico";
	
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		simetrico();
		System.out.println();
		Asimetrico();
	}
	
	public static void simetrico() throws NoSuchAlgorithmException {
		System.out.println("Texto a cifrar: " + textoSim);
		
		KeyGenerator keygenerator = KeyGenerator.getInstance("AES"); 
        SecretKey key = keygenerator.generateKey();
        System.out.println("-- Llave secreta: " + key.toString());
		
		byte[] cifradoSim = CifradoSimetrico.cifrar(key, textoAsim);
		System.out.println("Texto cifrado: " + cifradoSim);
		
		byte[] descifradoSim = CifradoSimetrico.descifrar(key, cifradoSim);
		String descifradoClaro = new String(descifradoSim, StandardCharsets.UTF_8);
		System.out.println("Texto descifrado: " + descifradoClaro);
	}
	
	public static void Asimetrico() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		System.out.println("Texto a cifrar: " + textoAsim);
		
		int keySize = 1024;
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(keySize); // Specify the desired key size
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();
		
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hashedData = digest.digest(textoAsim.getBytes(StandardCharsets.UTF_8));
		
		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initSign(privateKey);
		signature.update(hashedData);
		byte[] signedHash = signature.sign();
		System.out.println("Texto cifrado: " + signedHash);
		
		
		Signature signature2 = Signature.getInstance("SHA256withRSA");
		signature2.initVerify(publicKey);
		signature2.update(hashedData);
		boolean isValid = signature2.verify(signedHash);
		System.out.println("Texto descifrado: " + isValid);
		
		/**
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(256);
		KeyPair keyPair = generator.generateKeyPair();
		PublicKey publica = keyPair.getPublic();
		PrivateKey privada = keyPair.getPrivate();
		

		byte[] cifradoSim = CifradoAsimetrico.cifrar(publica, "RSA", textoAsim);
		System.out.println("Texto cifrado: " + cifradoSim);
		
		byte[] descifradoSim = CifradoAsimetrico.descifrar(privada, "RSA", cifradoSim);
		String descifradoClaro = new String(descifradoSim, StandardCharsets.UTF_8);
		System.out.println("Texto descifrado: " + descifradoClaro);
		*/
	}
}
