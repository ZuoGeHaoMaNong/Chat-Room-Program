

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Zihang
 */
public class Server {
    
    public static void main(String[] args) throws IOException {
        // Default port 10000 to build the server or read the stardard input 
        int port = 10000;
        if(args.length==1) port = Integer.parseInt(args[0]);
        
        //BlockingQueue to store the message 
        BlockingQueue<String> messageQ = new LinkedBlockingQueue<>();
        //A thread-safe arraylist to store the socket 
        List<Socket> socketList = Collections.synchronizedList(new ArrayList<Socket>());
        ServerSocket serverSocket = new ServerSocket(port); 
        /* 
         * sendToAllClient thread takes the message from messageQ and
         * send message to all socket in socketList
         */
        Runnable sendToAllClient = new SendToAllClient(messageQ,socketList);
        new Thread(sendToAllClient).start();
        while(true){
            Socket socket = serverSocket.accept(); 
            socketList.add(socket);
            /*
             * Everytime the new client connect, create a new GetMessageFromClient
             * thread, which listen the socket and save the message to messageQ
             */
            Runnable getMessageFromClient = new GetMessageFromClient(messageQ,socket,socketList);
            new Thread(getMessageFromClient).start();
        }
    }
}
