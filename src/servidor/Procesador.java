package servidor;

import java.net.InetAddress;
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
    
    private static class InstanceHolder {
        private static final Procesador INSTANCE = new Procesador();
    }
    
    public static Procesador getInstance() {
        return InstanceHolder.INSTANCE;
    }
    
    // Synchronized para evitar que dos personas pongan el mismo nombre a la vez
    public synchronized String addUser(String user) {
        if (USERS.contains(user))
            return "Código de error: usuario en uso";
        else {
            USERS.add(user);
            return "Código de inserción correcta";
        }
    }
    
    
}
