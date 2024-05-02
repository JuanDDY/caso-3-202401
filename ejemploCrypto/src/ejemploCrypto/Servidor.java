package ejemploCrypto;

import java.net.*;
import java.io.*;

public class Servidor {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234); // Crear un socket servidor en el puerto 1234
        System.out.println("Servidor esperando conexiones...");
        Socket clientSocket = serverSocket.accept(); // Esperar a que un cliente se conecte
        System.out.println("Cliente conectado.");

        // Aquí puedes implementar la lógica para recibir y responder consultas del cliente
        // Por ejemplo:
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Cliente dice: " + inputLine);
            out.println("Respuesta del servidor: " + " Camarones"); // Enviar respuesta al cliente
        }

        clientSocket.close(); // Cerrar la conexión con el cliente
        serverSocket.close(); // Cerrar el socket del servidor
    }
}
