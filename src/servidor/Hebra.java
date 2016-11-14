
package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Alejandro Campoy Nieves.
 * @author David Criado Ramón.
 * 
 * La clase hebra se encarga de atender las comunicaciones TCP del Servidor
 */
public class Hebra extends Thread {
    private final ServerSocket socketServidor;
    private Socket socketServicio;
    private PrintWriter outPrinter;
    private BufferedReader inReader;
    private Procesador procesador;
    
    Hebra(ServerSocket s) {
        socketServidor = s;
        procesador = Procesador.getInstance();
    }
    
    private void atender() {
        try {
            socketServicio = socketServidor.accept();
            
            outPrinter = new PrintWriter(socketServicio.getOutputStream(), true);
            inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));
            
            // Leer petición
            String peticion = inReader.readLine();
            
            // Procesar petición con Procesador
            String respuesta = "ejemplo";
            
            // Enviar respuesta
            outPrinter.println(respuesta);
            socketServicio.close();
                    
                    
        } catch(IOException io) {
            System.err.println("Error al leer/escribir en el socket");
        }
    }
    
    
    @Override
    public void run() {
        while (true)
            atender();
        
    }
}
