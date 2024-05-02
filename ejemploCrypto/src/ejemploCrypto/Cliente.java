package ejemploCrypto;

import java.net.*;
import java.io.*;

public class Cliente {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 1234); // Conectar al servidor en el puerto 1234
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Enviar una consulta al servidor
        out.println("Consulta del cliente");

        // Leer la respuesta del servidor
        String response = in.readLine();
        System.out.println("Servidor dice: " + response);

        socket.close(); // Cerrar la conexión con el servidor
    }
}

