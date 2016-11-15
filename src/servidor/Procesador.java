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
    private static final ArrayList<ArrayList<String>> BUFFERS = new ArrayList();

    public static HashMap<Socket, String> getSOCKETS() {
        return SOCKETS;
    }
    
    private static class InstanceHolder {
        private static final Procesador INSTANCE = new Procesador();
    }
    
    public static Procesador getInstance(ArrayList<String> buffer) {
        BUFFERS.add(buffer);
        return InstanceHolder.INSTANCE;
    }
    
    // Synchronized para evitar que dos personas pongan el mismo nombre a la vez
    public synchronized String addUser(String user, Socket s) {
        if (USERS.contains(user))
            return "INVALIDUSER";
        else {
            USERS.add(user);
            SOCKETS.put(s, user);
            return "OK";
        }
    }
    
    private String sendAll(String message, Socket s) {
        for (ArrayList<String> buffer: BUFFERS) {
            String user = SOCKETS.get(s);
            buffer.add(user + ": " + message);
        }
        return "SENT";
    }
    public String parse(String toParse, Socket s) {
        if ("UPDATE".equals(toParse))
            return "UPDATE";
        int pos = toParse.indexOf(' ');
        String accion = toParse.substring(0, pos);
        
        if ("LOGIN".equals(accion))
            return addUser(toParse.substring(pos + 1), s);
        
        if ("SEND".equals(accion))
            return sendAll(toParse.substring(pos + 1), s);
        
        
        return "UNKNOWN";
    }
    
    
}
