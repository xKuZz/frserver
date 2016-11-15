
package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Alejandro Campoy Nieves.
 * @author David Criado Ramón.
 * 
 * La clase hebra se encarga de atender las comunicaciones TCP del Servidor
 */
public class Hebra extends Thread {
    private final Socket socketServicio;
    private PrintWriter outPrinter;
    private BufferedReader inReader;
    private Procesador procesador;
    
    Hebra(Socket s) {
        socketServicio = s;
        procesador = Procesador.getInstance();
    }
    
    private void atender() {
        try {
            outPrinter = new PrintWriter(socketServicio.getOutputStream(), true);
            inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));
            
            // Leer petición
            String peticion = inReader.readLine();
            
            System.out.println(peticion);
            
            // Procesar petición con Procesador
            String respuesta = procesador.parse(peticion);
            
            if (respuesta=="Código cerrar conexión")
                socketServicio.close();
            else // Enviar respuesta
                outPrinter.println(respuesta);
          
                    
        } catch(IOException io) {
            System.err.println("Error al leer/escribir en el socket");
        }
    }
    
    
    @Override
    public void run() {
        while (!socketServicio.isClosed())
            atender();
    }
}
