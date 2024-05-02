package aplicacion;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;

import java.net.*;
import java.io.*;


public class Servidor extends Thread{
	
	String nombreArchivo;
	BigInteger P;
	
	BigInteger G;
	int g;
	
	int numeroPuerto;
	
	PublicKey llavePublicaServer;
	PrivateKey llavePrinvadaServer;
	
	
	Servidor (int numeroPuertoP, PublicKey llavePublicaServerP, PrivateKey llavePrinvadaServerP){
		
		this.numeroPuerto = numeroPuertoP;
		this.llavePublicaServer = llavePublicaServerP;
		this.llavePrinvadaServer = llavePrinvadaServerP;
	}
	
	@Override
	public void run() {
		
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(this.numeroPuerto);		// Crear un socket servidor en el puerto 1234
			System.out.println("Servidor esperando conexiones...");
	        Socket clientSocket = serverSocket.accept(); // Esperar a que un cliente se conecte
	        System.out.println("Cliente conectado.");
	        
	        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
	       
	        String inputLine;
	        while ((inputLine = in.readLine()) != null) {
	            System.out.println("Cliente dice: " + inputLine);
	            out.println("Respuesta del servidor: " + " Camarones"); // Enviar respuesta al cliente
	        }
	        /**
	        while (true) {
				try {
					wait();
					
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			*/
			clientSocket.close(); // Cerrar la conexión con el cliente
	        serverSocket.close(); // Cerrar el socket del servidor
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		
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
