
package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

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
    private final Procesador procesador;
    private final ArrayList<String> toSend = new ArrayList();
    
    /** Constructor de la clase Hebra.
     * 
     * @param s Socket del servidor que atiende al cliente.
     */
    Hebra(Socket s) {
        socketServicio = s;
        procesador = Procesador.getInstance(socketServicio, toSend);
    }
    
    /** Método encargado de leer, atender y responder una conexión TCP de un cliente.
     * 
     */
    private void atender() {
        try {
            outPrinter = new PrintWriter(socketServicio.getOutputStream(), true);
            inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));
            
            // Leer petición
            String peticion = inReader.readLine();
            
            System.out.println(peticion);
            
            // Procesar petición con Procesador
            String respuesta = procesador.parse(peticion, socketServicio);
            
            if ("CLOSE".equals(respuesta))
                socketServicio.close();
            else if ("UPDATE".equals(peticion)) {
                System.out.println("Recibido " + peticion);
                // TO-DO: Ampliar leer varias lineas
                String message;
                if (!toSend.isEmpty()) 
                   message = "PUT " + toSend.remove(0);
                
                else
                    message = "IDDLE";
                System.out.println("Enviado " + message);
                outPrinter.println(message);
            }   
            else
                outPrinter.println(respuesta);
          
                    
        } catch(IOException io) {
            System.err.println("Error al leer/escribir en el socket");
        }
    }
    
    /** Método de la hebra. Atender la conexión TCP mientras la conexión esta abierta.
     * 
     */
    @Override
    public void run() {
        while (!socketServicio.isClosed())
            atender();
    }
}
