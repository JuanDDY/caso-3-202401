package aplicacion;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;



public class Principal {
	
	static PublicKey llavePublicaServer;
	static PrivateKey llavePrivadaServer;
	
	static int numeroPuerto = 1234;

	public static void main(String[] args) throws NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		generadorLlavesAsimetricas();
				
		Cliente cliente = new Cliente(Principal.numeroPuerto, 
										"documentos/numeroPrimo2.txt", 
										Principal.llavePublicaServer);
		
		Servidor servidor = new Servidor(Principal.numeroPuerto, 
											llavePublicaServer, 
											llavePrivadaServer);
		
		
		servidor.start();
		cliente.start();
	
	}
	
	
	public static void generadorLlavesAsimetricas () throws NoSuchAlgorithmException {
		
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(1024);
		KeyPair keyPair = generator.generateKeyPair();
		Principal.llavePublicaServer = keyPair.getPublic();
		Principal.llavePrivadaServer = keyPair.getPrivate();
	}

}
