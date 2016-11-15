package servidor;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Alejandro Campoy Nieves.
 * @author David Criado Ramón.
 * 
 * La clase Procesador se encarga de a partir de una petición del cliente generar
 * una respuesta por parte del servidor. No envía nada solo genera respuestas y
 * las coloca en la estructura apropiada si procede.
 */


public class Procesador {
    private static final HashSet<String> USERS = new HashSet();
    private static final HashMap<Socket, String> SOCKETS = new HashMap();
    private static final HashMap<Socket, ArrayList<String>> BUFFERS = new HashMap();

    /** Accede a los nombres de usuario a partir de su socket.
     * 
     * @return Tabla hash que relaciona Sockets y nombres de usuario
     */
    public static HashMap<Socket, String> getSOCKETS() {
        return SOCKETS;
    }
    
    /** Singleton concurrente.
     * 
     */
    private static class InstanceHolder {
        private static final Procesador INSTANCE = new Procesador();
    }
    
    /** Acceder a la única instancia y añade un buffer para almacenar los mensajes
     *  de chat no enviados.
     * @param socket Socket que relaciona un cliente con el servidor.
     * @param buffer Buffer del chat en el que se almacenan los mensajes pendientes de enviar.
     * @return 
     */
    public static Procesador getInstance(Socket socket, ArrayList<String> buffer) {
        BUFFERS.put(socket, buffer);
        return InstanceHolder.INSTANCE;
    }
    
    // Synchronized para evitar que dos personas pongan el mismo nombre a la vez
    /** Configura el nombre de usuario de un cliente si no ha sido usado todavía.
     * @param user Nombre de usuario que se desea utilizar.
     * @param s Socket que relaciona un cliente con el servidor.
     * @return Respuesta del Servidor.
     */
    public synchronized String addUser(String user, Socket s) {
        if (USERS.contains(user))
            return "INVALIDUSER";
        else {
            USERS.add(user);
            SOCKETS.put(s, user);
            return "OK";
        }
    }
    
    /** 
     * Añade el mensaje a los mensajes pendientes de todos los clientes menos
     * el remitente.
     * @param message Mensaje a envíar.
     * @param socketOrigen Socket del remitente del mensaje.
     * @return Confirmación de que el servidor ha enviado el mensaje.
     */
    private String sendAll(String message, Socket socketOrigen) {
        for (Socket socket: SOCKETS.keySet()) {
            if (socket != socketOrigen) {
                String user = SOCKETS.get(socketOrigen);
                ArrayList<String> buffer = BUFFERS.get(socket);
                buffer.add(user + ": " + message);
            }
        }
        return "SENT";
    }
    
    /** 
     * Cierra la conexión TCP del servidor con el cliente.
     * @param s Socket que conecta dicho cliente con el servidor.
     * @return "CLOSE"
     */
    public String close(Socket s) {
        String user = SOCKETS.remove(s);
        BUFFERS.remove(s);
        USERS.remove(user);
        return "CLOSE";
    }
    
    /** 
     * Se encarga de determinar que acción ha de realizarse según el mensaje recibido.
     * @param toParse Mensaje recibido del cliente.
     * @param s Socket que conecta dicho cliente con el servidor.
     * @return Respuesta del servidor.
     */
    public String parse(String toParse, Socket s) {
        if ("UPDATE".equals(toParse))
            return "UPDATE";
        
        if ("CLOSE".equals(toParse))
            return close(s);
        int pos = toParse.indexOf(' ');
        String accion = toParse.substring(0, pos);
        
        if ("LOGIN".equals(accion))
            return addUser(toParse.substring(pos + 1), s);
        
        if ("SEND".equals(accion))
            return sendAll(toParse.substring(pos + 1), s);
        
        
        return "UNKNOWN";
    }
    
    
}
