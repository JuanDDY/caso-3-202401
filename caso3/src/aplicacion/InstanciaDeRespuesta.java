package aplicacion;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public class InstanciaDeRespuesta extends Thread{
	
	
	String nombreArchivo;
	BigInteger P;
	
	BigInteger G;
	int g;
	

	boolean bandera;
	
	PublicKey llavePublicaServer;
	PrivateKey llavePrinvadaServer;
	
	
	Socket cliente;
	int id;
	
	InstanciaDeRespuesta (int idP, Socket clienteP){
		this.cliente = clienteP;
		this.id = idP;
	}
	
	
	@Override
	public void run() {
		System.out.println("Se inicio el thread del servidor: " + this.id + ". Para el cliente: " + this.id);
		
		try {
			DataOutputStream escribirAlCliente = new DataOutputStream(this.cliente.getOutputStream());
			DataInputStream leerDelCliente = new DataInputStream(this.cliente.getInputStream()); 
			
			ProtocoloServidor protocoloServidor = new ProtocoloServidor(leerDelCliente, escribirAlCliente, this);
			
			protocoloServidor.procesar();	        
	        
			
			escribirAlCliente.close();
			leerDelCliente.close();
			this.cliente.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
		
		
	}

	
	
	public void ejecutadorFunciones() {
		
	}
	
	
	public void detenerServidor () {
		this.bandera = false;
	}
	
	
	
	
	public void leerArchivo() {
        try {
	        FileReader fr = new FileReader(this.nombreArchivo);
	        BufferedReader br = new BufferedReader(fr);
	        
	        String lineaP = br.readLine();
	        String lineaG = br.readLine();
	        br.close();
	
	        this.P = new BigInteger(lineaP, 16);
	        this.G = new BigInteger(lineaG, 16);
	        this.g = G.intValue();
	        
	        System.out.println("Número P: " + this.P.toString());
	        System.out.println("Carácter g: " + this.G.toString());
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

}
