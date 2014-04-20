
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 *
 * @author Zihang
 */
public class Client extends JFrame {
    private JTextArea incoming;
    private JTextField outgoing;
    private JScrollPane qScroller;
    private JButton sendButton;
    private Socket socket;
    private PrintWriter toFriend;
    private String name;
    private Scanner scan;
    
    
    /**
     * Constructor for Client side 
     * @param host  IP address to connect
     * @param port  Port that Server is listening
     * @param name  Client side user name
     * @throws IOException 
     */
    public Client(String host , int port, String name) throws IOException {
        this.name = name;
        setUI(); // set the chatting GUI
        setTitle("Client");
        setLocation(650,100);
        socket = new Socket(host,port);// socket connected
        connect(); //contains a forever loop for receiving message
    }
    /**
     * The GUI for chatting
     */
    public void setUI(){
        JPanel mainPanel = new JPanel();
        incoming = new JTextArea(15,30);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        incoming.setForeground(Color.BLUE);
        qScroller = new JScrollPane(incoming);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        outgoing = new JTextField(20);
        sendButton = new JButton("Send"); 
        sendButton.addActionListener(new SendButtonListener()); 
        mainPanel.add(qScroller);
        mainPanel.add(outgoing);
        mainPanel.add(sendButton);
        getContentPane().add(BorderLayout.CENTER,mainPanel);
        setSize(400,330);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        /*
         * send "000CLOSED000" when press the close button
         */
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                try{
                    toFriend.println("000CLOSED000");
                    toFriend.flush();
                    socket.close();
                    System.exit(0);
                }catch(IOException ex){
                    ex.printStackTrace();
                }
            }
        });
        
    }
    
    /**
     * Initialize the Scanner and the PrintWriter to stream. 
     * Whenever received a message, show it on screen
     * @throws IOException 
     */
    public void connect() throws IOException{
        if(socket.isConnected()){
            incoming.append("=============Connected============="+'\n');
        }
        scan = new Scanner(socket.getInputStream());
        toFriend = new PrintWriter(socket.getOutputStream());
        while(scan.hasNextLine()) incoming.append(scan.nextLine()+'\n');
        }
       
    /**
     * Whenever press the button, send the message in TextField
     */
    class SendButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            toFriend.println(name+":"+"   "+outgoing.getText());
            toFriend.flush();
            outgoing.setText("");
        }
    }
    
    /**
     * Main___Start the Client, Default Name Client
     * @param args
     * @throws IOException 
     */
    
    public static void main(String[] args) throws IOException {
        if(args.length==3){
           new Client(args[0],Integer.parseInt(args[1]),args[2]);
        }else{
           new Client("127.0.0.1",10000,"Client");
        }
    }
}
