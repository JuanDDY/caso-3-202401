package aplicacion;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;



public class ProtocoloServidor {
	
	InstanciaDeRespuesta instanciaPadre;
	public static final String nombreArchivo = "caso3/documentos/numeroPrimo2.txt";
	
	DataInputStream leerDelCliente;
	DataOutputStream escribirAlCliente;
	
	boolean ejecutar;
	
	PublicKey llavePublicaServer;
	static final String llavePublicaServerSrt = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCkFB6c5xyfKjSdTXlJApRdGaKCP/QhmtZMwKGDW8vBXz+NlcFO6IFdAN7B39NVFhAV+zhiyT9jnBJy300rtN1CfwxTbSqPNFbAXbLmqdMtoN+D6Dh27UhDWIeFilq7m5XMkUlXyaWi5DMq1J+FgBFFE3WvWepEcfNc8iozP1t62QIDAQAB";
	
	PrivateKey llavePrivadaServer;
	static final String llavePrivadaServerSrt = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKQUHpznHJ8qNJ1NeUkClF0ZooI/9CGa1kzAoYNby8FfP42VwU7ogV0A3sHf01UWEBX7OGLJP2OcEnLfTSu03UJ/DFNtKo80VsBdsuap0y2g34PoOHbtSENYh4WKWrublcyRSVfJpaLkMyrUn4WAEUUTda9Z6kRx81zyKjM/W3rZAgMBAAECgYApE5xiXX2N30wSwW2GuK2Z7SwA/a/JpfDWgDcvIpENFSJc+MvPTGfud6RM1xMaLw7R4fXIzWcgMddwiLTSJ1JoxW1LZkTdxPhjSZAku4eMYPYQPphvgG563FRceRhFxgB7pWu2GKiN/oZPaiDAVtgM8RMhoY832KfCcY2Tk2uYvQJBAKYMBhB6R/AFg1FPnIvmBqSjL3hHz6HcgHJNsvkfMooWmDeEQD9R7y668ExJzkn6xm0OLgDR8bgrHWvqGhC1oTsCQQD89x1br5Ndn4PjIGT5lRb/8TqalptERmcn9U/6VsfN2nz0EDZ5uw/0AInKGWsDF0M7Qbgap12yWH9KFaPszNL7AkBcwvLQWb++cxX9YsBN1192skNeqp9wCUMomAeX7LnLMvwuZ4+M0DUSohSESecYpSQc4IMKqj6jaFKzasDM/OrpAkEA/KcTe29xECEx6LeCIoT148tbbOrfSOHIRfCNYMsr5D5Ebr+CqTJcTKf89w1MBFpHm/eXmYdv3aDmAWh7wRcR4QJAJQ4tV1AOe7jti6viKryZGG6QfQzKqqovVNa+0cPmWyQ+JrWl4MnTJVA4DLBXGpn6dnMAVrims+6KimnuDtm9pw==";
	
	BigInteger P;
	BigInteger G;
	int g;
	BigInteger x;
	BigInteger Gx;
	BigInteger Gy;
	BigInteger z;

	IvParameterSpec iv;

	SecretKey llaveSimetricaParaCifrar;
	SecretKey llaveSimetricaParaHMAC;
	
	
	ProtocoloServidor ( DataInputStream leerDelClienteP, DataOutputStream escribirAlClienteP, InstanciaDeRespuesta instanciaPadreP) {

		this.leerDelCliente = leerDelClienteP;
		this.escribirAlCliente = escribirAlClienteP;
		this.instanciaPadre = instanciaPadreP; 
		this.ejecutar = true;
	}



	/**
	public static void procesar (BufferedReader inConsola, BufferedReader inServer, PrintWriter outServer){		
	}
	* @throws NoSuchAlgorithmException 
	* @throws InvalidKeySpecException 
	 * @throws IOException 
	 * @throws SignatureException 
	 * @throws InvalidKeyException 
	*/
	public void procesar () throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, SignatureException, InvalidKeyException{	
	
		String fromUser = null;
		
		generarLlavesAsimetricas();
				
        
        //Paso 2
		fromUser = this.leerDelCliente.readUTF();	
		String[] partesFromUser = fromUser.split(",");
		if (!partesFromUser[0].equals("SECURE INIT")) {
			throw new SignatureException("La primera expresion debe ser \"SECURE INIT,...\""); 
		}

		String textoFirmado = null;
		try {
			textoFirmado = firmarConLlavePrivadaVerificacion(partesFromUser[1]);  //Se realiza la firma sobre lo que re realizo el 	
		
		} catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
			e.printStackTrace();
		}
		escribirAlCliente.writeUTF(textoFirmado);

		System.out.println("Se le devuelve al cliente el RETO, con firma");

      
		
		// Paso 5
		fromUser = null;
		fromUser = this.leerDelCliente.readUTF();	

		if (fromUser.equals("ERROR")) {
			throw new SignatureException("La firma digital enviada no es válida. En el Thread ");
		}
        // Si sigue es porque En el paso 5 se recibio un "OK"
		System.out.println("El mensaje firmado coincidio con el mensaje enviado");
		

		//Paso 6
		calcularGyP();
		
		
		//Paso 7
		String GPGx = this.G.toString() + "," + this.P.toString() + "," + this.Gx.toString();  // GPGx = G,P,G^x
		this.iv = generarIv();

		String ivTxt = Base64.getEncoder().encodeToString(this.iv.getIV());
		
		escribirAlCliente.writeUTF(this.G.toString());
		escribirAlCliente.writeUTF(this.P.toString());
		escribirAlCliente.writeUTF(this.Gx.toString());
		escribirAlCliente.writeUTF(ivTxt);
		String conjuntoNumerosFirmados = firmarConLlavePrivadaDiffie(GPGx);
		escribirAlCliente.writeUTF(conjuntoNumerosFirmados);
		System.out.println("Se extrajeron P y G. Fueron enviados al Cliente junto con G^x e iv");
		

		//Paso 9
		fromUser = null;
		fromUser = this.leerDelCliente.readUTF();	

		if (fromUser.equals("ERROR")) {
			System.out.println("ERROR");
			throw new SignatureException("La firma digital enviada no es válida. En el Thread ");
		}
        // Si sigue es porque En el paso 9 se recibio un "OK"
		System.out.println("OK");
		System.out.println("El cliente verifico correctamente los numeros G, P y G^x mod P ");


		//Paso 11.b
		fromUser = null;
		fromUser = this.leerDelCliente.readUTF();	
		this.Gy = new BigInteger(fromUser);

		this.z = this.Gy.modPow(this.x, this.P);

		byte[] bytesDeZ = z.toByteArray();
		MessageDigest digest = MessageDigest.getInstance("SHA-512");
		byte[] hash = digest.digest(bytesDeZ);							// Es de tamanio 512 bits, ppor tanto 64 Bytes
		byte[] primeraMitasHash = new byte[hash.length / 2];			// De tamanio 32 Bytes, porque es la mitad del tamanio del Hash
		byte[] SegundaMitasHash = new byte[hash.length / 2];	

		for (int i=0; i<hash.length; i++ ) {
			if( i < (hash.length / 2) ) {
				primeraMitasHash[i] = hash[i];
			} else {
				SegundaMitasHash[i-(hash.length / 2)] = hash[i];
			}
		}

		this.llaveSimetricaParaCifrar = generarLlaveSecreta(primeraMitasHash);
		this.llaveSimetricaParaHMAC = generarLlaveSecreta(SegundaMitasHash);

		System.out.println("Se generaron las llaves secretas para cifrar y para el HMAC");



		//Paso 12
		escribirAlCliente.writeUTF("CONTINUAR");


		// Paso 13 -14
		String login = leerDelCliente.readUTF();
		String password = leerDelCliente.readUTF();


		//Paso 15
		if(verifyLoginAndPassword(login,password)){ 
			escribirAlCliente.writeUTF("OK"); 
			System.out.println("el cliente se verifico correctamente");
		}else {
			escribirAlCliente.writeUTF("ERROR");
		}
		// Paso 17 -18
		String consulta = leerDelCliente.readUTF(); 
		byte[] hmacConsulta = generarHMAC(consulta.getBytes());
		escribirAlCliente.writeUTF(Base64.getEncoder().encodeToString(hmacConsulta));

		// Paso 19
		String rta = consulta; 
		byte[] ivBytes = Base64.getDecoder().decode(ivTxt); 
		IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

			String rtaCifrada = Base64.getEncoder().encodeToString(CifradoSimetrico.cifrar(llaveSimetricaParaCifrar, rta, ivSpec));
			
			// Paso 20
			byte[] hmacRta = generarHMAC(rta.getBytes());
			String hmacRtaBase64 = Base64.getEncoder().encodeToString(hmacRta);
			
			// Enviar rta cifrada y HMAC al cliente
			escribirAlCliente.writeUTF(rtaCifrada);
			escribirAlCliente.writeUTF(hmacRtaBase64);
	}
		
		
	
	
	
	public void calcularGyP() {
		leerArchivoNumeroPrimo();		//Se generan P y G
		this.x = new BigInteger(256, new SecureRandom());
		
		this.Gx = this.G.modPow(this.x, this.P); 	//Calcular G^x mod P
	}
	

	public byte[] generarHMAC (byte[] textoBytes) throws InvalidKeyException, NoSuchAlgorithmException {
		
		Mac hMac = Mac.getInstance("HmacSHA256");
		hMac.init(this.llaveSimetricaParaHMAC);
		byte[] hmacBytes = hMac.doFinal(textoBytes);
		return hmacBytes;
	}


	public SecretKey generarLlaveSecreta(byte[] listaBytes) {

		SecretKey llaveSecreta = new SecretKeySpec(listaBytes, 0, listaBytes.length, "AES");
		return llaveSecreta;
	}


	
	public String firmarConLlavePrivadaVerificacion(String textoAsim) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {			
		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initSign(this.llavePrivadaServer);
		signature.update(new BigInteger(textoAsim).toByteArray());
		byte[] signedHash = signature.sign();
		return Base64.getEncoder().encodeToString(signedHash);
	}
	

	public String firmarConLlavePrivadaDiffie(String textoAsim) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {			
		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initSign(this.llavePrivadaServer);
		signature.update(textoAsim.getBytes());
		byte[] signedHash = signature.sign();
		return Base64.getEncoder().encodeToString(signedHash);
	}


	private IvParameterSpec generarIv() {
		
		byte[] iv = new byte[16];
		new SecureRandom().nextBytes(iv);
		
		return new IvParameterSpec(iv);
	}
	
	
	public void generarLlavesAsimetricas() throws NoSuchAlgorithmException, InvalidKeySpecException {
		
		//Creacion de llave privada
		byte[] bytesLavePrivada = Base64.getDecoder().decode(llavePrivadaServerSrt);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		this.llavePrivadaServer =  keyFactory.generatePrivate(new PKCS8EncodedKeySpec(bytesLavePrivada));

		//Creacion de llave publica
		byte[] bytesLlavePublica = Base64.getDecoder().decode(llavePublicaServerSrt);
		KeyFactory keyFactory2 = KeyFactory.getInstance("RSA");
		this.llavePublicaServer = keyFactory2.generatePublic(new X509EncodedKeySpec(bytesLlavePublica));	
        
	}
	private boolean verifyLoginAndPassword(String login, String password) {

		byte[] loginDescifrado = CifradoSimetrico.descifrar(this.llaveSimetricaParaCifrar, Base64.getDecoder().decode(login), this.iv);
		byte[] passwordDescifrado = CifradoSimetrico.descifrar(this.llaveSimetricaParaCifrar, Base64.getDecoder().decode(password), this.iv);

		String loginStr = new String(loginDescifrado);
		String passwordStr =new String(passwordDescifrado);

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("caso3/documentos/clientes.txt"));
			String linea;
	
			while ((linea = reader.readLine()) != null) {
				String[] partes = linea.split(" "); // Dividir la línea en login y password
	
				if (partes.length > 1 && partes[0].equals(loginStr) && partes[1].equals(passwordStr)) {
					return true; // Devolver true si se encontró el login y la contraseña
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	
		return false; // Devolver false si no se encontró el login o la contraseña
	}
	

	
	public void leerArchivoNumeroPrimo() {
        try {
	        FileReader fr = new FileReader(ProtocoloServidor.nombreArchivo);
	        BufferedReader br = new BufferedReader(fr);
	        
	        String lineaP = br.readLine();
	        String lineaG = br.readLine();
	        br.close();
	
	        this.P = new BigInteger(lineaP, 16);
	        this.G = new BigInteger(lineaG, 16);
	        this.g = G.intValue();

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
