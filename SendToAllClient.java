

import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author Zihang
 */

public class SendToAllClient implements Runnable {
    
    private BlockingQueue<String> messageQ;
    private List<Socket> socketList;
    private String message;
    
    public SendToAllClient(BlockingQueue<String> messageQ,List<Socket> socketList){
        this.messageQ = messageQ;
        this.socketList = socketList;
    }
    /**
     * Take out a message from messageQ 
     * send it to all the client
     */
    public void run() {
        while(true){
            try {
                message = messageQ.take();
                for(Socket socket:socketList){
                    PrintWriter toClient = new PrintWriter(socket.getOutputStream());
                    toClient.println(message);
                    toClient.flush();
                } 
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
     }
}
    
    

