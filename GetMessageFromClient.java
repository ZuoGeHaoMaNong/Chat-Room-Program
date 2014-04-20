

import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author Zihang
 */

/*
 * Everytime the new client connect, create a new GetMessageFromClient
 * thread, which listen the socket and save the message to messageQ
 */
public class GetMessageFromClient implements Runnable {
    
    private BlockingQueue<String> messageQ;
    private Socket socket;
    private List<Socket> socketList;

    public GetMessageFromClient(BlockingQueue<String> messageQ,Socket socket,List<Socket> socketList){
        this.messageQ = messageQ;
        this.socket = socket;
        this.socketList = socketList;
    }
    
    /**
     * Save the received message to messageQ
     * Whenever received the "000CLOSE000", close the socket, 
     * remove the socket from the socketList and shutdown this thread.
     */
    public void run() {
        try {
            String message;
            Scanner scan = new Scanner(socket.getInputStream());
            while(!socket.isClosed() && scan.hasNextLine()) {
                message = scan.nextLine();
                if (message.equals("000CLOSED000")){
                    socketList.remove(socket);
                    socket.close();
                }else{
                    messageQ.put(message);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
