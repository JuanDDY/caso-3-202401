package aplicacion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.PublicKey;

public class Cliente extends Thread{

	String nombreArchivo;
	BigInteger P;
	
	BigInteger G;
	int g;
	
	int numeroPuerto;
	
	PublicKey llavepublicaServer;
	
	Cliente (int numeroPuertoP,String nombreArchivoP, PublicKey llavepublicaServerP){
		
		this.numeroPuerto = numeroPuertoP;
		this.nombreArchivo = nombreArchivoP;
		this.llavepublicaServer = llavepublicaServerP; 
		
	}
	
	
	@Override
	public void run() {
		
		Socket socket;
		
		try {
			socket = new Socket("localhost", this.numeroPuerto); // Conectar al servidor en el puerto 1234
			
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

	        // Enviar una consulta al servidor
	        out.println("Consulta del cliente");

	        // Leer la respuesta del servidor
	        String response = in.readLine();
	        System.out.println("Servidor dice: " + response);

	        socket.close(); // Cerrar la conexión con el servidor
	        
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}

	

}




