package servidor;

import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Alejandro Campoy Nieves.
 * @author David Criado Ramón.
 * 
 * La clase Procesador se encarga de a partir de una petición del cliente generar
 * una respuesta por parte del servidor.
 */


public class Procesador {
    private static final HashSet<String> USERS = new HashSet();
    private static final HashMap<Socket, String> SOCKETS = new HashMap();
    
    private static class InstanceHolder {
        private static final Procesador INSTANCE = new Procesador();
    }
    
    public static Procesador getInstance() {
        return InstanceHolder.INSTANCE;
    }
    
    // Synchronized para evitar que dos personas pongan el mismo nombre a la vez
    public synchronized String addUser(String user, Socket s) {
        if (USERS.contains(user))
            return "INVALIDUSER";
        else {
            USERS.add(user);
            IPS.put(s, user);
            return "OK";
        }
    }
    
    public String sendAll(String text, Socket s ) {
        Sender s = new Sender(text, s, SOCKETS);
    }
    
    public String parse(String toParse, Socket s) {
        int pos = toParse.indexOf(' ');
        String accion = toParse.substring(0, pos);
        
        if ("LOGIN".equals(accion))
            return addUser(toParse.substring(pos + 1), s);
        
        if ("SEND".equals(accion))
            return sendAll(toParse.substring(pos + 1), s);
        
        
        return "UNKNOWN";
    }
    
    
}
