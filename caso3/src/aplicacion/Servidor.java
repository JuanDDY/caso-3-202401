package aplicacion;

import java.security.PrivateKey;
import java.security.PublicKey;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;





public class Servidor extends Thread{
	
	
	PublicKey llavePublicaServer;
	PrivateKey llavePrinvadaServer;
	
	
	public static final int numeroPuerto = 1234;
	public static final String servidor = "localhost";
	
	public static boolean bandera;	
	public static ServerSocket serverSocket = null;
	
	public static void main (String [] args) throws IOException {

		System.out.println("Servidor iniciado, esperando clientes...");
		Servidor.bandera = true;
		
		try {

			serverSocket = new ServerSocket(Servidor.numeroPuerto);    // Crear un socket servidor en el puerto 1234		
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}  
		
		int i = 1;
		
		while (bandera) {
            Socket nuevoCliente = serverSocket.accept();     
            InstanciaDeRespuesta tNuevoCliente = new InstanciaDeRespuesta(i, nuevoCliente);
            i = i+1;
            tNuevoCliente.start();
            Servidor.bandera = false;
        }
		
		serverSocket.close();
	}
	
}
