import javax.xml.namespace.QName;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerIper2go {
    ServerSocket socket;
    Socket client_socket;
    private int port;
    private int client_id=0;
    Prodotti prodotti = new Prodotti();
    // DICHIARAZIONE DELLA LISTA CONDIVISA




    public static void main(String args[]){
        if (args.length!=1){
            System.out.println("Utilizza la porta <port> del Server");
            return;
        }
        ServerIper2go server = new ServerIper2go(Integer.parseInt(args[0]));
        server.start();
    }

    public ServerIper2go(int port){
        System.out.println("Initializing server with port "+port);
        this.port = port;
    }

    public void start() {
        try {
            System.out.println("Avvio Server sulla porta "+port);
            socket = new ServerSocket(port);
            System.out.println("Server avviato sulla porta "+port);
            while (true) {
                System.out.println("In attesa sulla porta " + port);
                client_socket = socket.accept();
                //assegno un thread per ogni client
                ClientManager cm = new ClientManager(client_socket,prodotti,client_id);
                Thread t = new Thread(cm,"client_"+client_id);
                t.start();
                client_id++;
            }

        } catch (IOException e) {
            System.out.println("Non è stato possibile avviare il server sulla porta "+port);
            e.printStackTrace();
        }

    }
}
