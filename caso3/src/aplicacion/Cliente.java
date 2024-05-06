package aplicacion;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;


public class Cliente {

	
	public static final int numeroPuerto = 1234;
	public static final String servidor = "localhost";
	
	
	
	public static void main (String args[]) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
		
		Socket socket = null;
		DataOutputStream outServer = null;
		DataInputStream inServer = null;
		
		System.out.println("Cliente iniciado ...");
			
		try {
			socket = new Socket(Cliente.servidor, Cliente.numeroPuerto); // Conectar al servidor en el puerto 1234
			
			outServer = new DataOutputStream(socket.getOutputStream());
	        inServer = new DataInputStream(socket.getInputStream());       
	       	        	        
		} catch (IOException e) {
			e.printStackTrace();
		} 
						
		DataInputStream inConsola = new DataInputStream(System.in);       

        ProtocoloCliente protocoloCliente = new ProtocoloCliente(inConsola, inServer, outServer);
        protocoloCliente.procesar();
        
        // Cerrar todos los BufferedReaders y PrintReader
        inConsola.close();
        inServer.close();
        outServer.close();
        socket.close(); 		// Cerrar la conexión con el servidor
    }	

	

}




