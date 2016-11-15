package servidor;

import java.net.Socket;
import java.util.HashMap;

/**
 *
 * @author Alejandro Campoy Nieves.
 * @author David Criado Ramón
 */
public class Sender extends Thread {
    private final String toSend;
    private final Socket socket;
    private final HashMap<Socket, String> SOCKETS;
    Sender(String text, Socket s, HashMap<Socket, String> h) {
        toSend = text;
        socket = s;
        SOCKETS = h;
    }
    
    @Override
    public void run() {
        
    }
}
