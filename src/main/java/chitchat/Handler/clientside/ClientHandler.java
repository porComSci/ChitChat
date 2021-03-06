package chitchat.Handler.clientside;

import chitchat.message.ChitChatMessage;
import chitchat.message.MessageType;
import chitchat.view.client.ClientPanel;
import chitchat.view.client.PrivateChatWindow;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by PorPaul on 7/12/2558.
 */
public class ClientHandler {

    private static ClientHandler ourInstance = new ClientHandler();

    public static ClientHandler getInstance() {
        return ourInstance;
    }

    private ClientPanel clientPanel = null;
    public static String[] membersArray;
    private ObjectOutputStream outToServer = null;
    private ObjectInputStream inFromServer = null;
    private Map<String, PrivateChatWindow> privateChatWindowMap = new HashMap<String, PrivateChatWindow>();

    private ClientHandler() {
    }

    public Map<String, PrivateChatWindow> getPrivateChatWindowMap() {
        return privateChatWindowMap;
    }

    public void setPrivateChatWindowMap(Map<String, PrivateChatWindow> privateChatWindowMap) {
        this.privateChatWindowMap = privateChatWindowMap;
    }

    public void addPrivateChatWindow(String chattingClientName, PrivateChatWindow privateChatWindow) {
        privateChatWindowMap.put(chattingClientName, privateChatWindow);
    }

    public void removePrivateChatWindow(String chattingClientName) {
        privateChatWindowMap.remove(chattingClientName);
    }

    public void setOutToServer(ObjectOutputStream outToServer) {
        this.outToServer = outToServer;
    }

    public void setInFromServer(ObjectInputStream inFromServer) {
        this.inFromServer = inFromServer;
    }

    public void setClientPanel(ClientPanel clientPanel) {
        this.clientPanel = clientPanel;
    }

    public void register() throws IOException {
        ChitChatMessage register = new ChitChatMessage(MessageType.REGISTER);
        register.setName(clientPanel.getUserName());
        synchronized (outToServer) {
            outToServer.writeObject(register);
            outToServer.flush();
        }
        System.out.println("sent name to server ...");
    }

    public void announce(String message) throws IOException {

        ChitChatMessage chitChatMessage = new ChitChatMessage(MessageType.ANNOUNCE);
        String userName = clientPanel.getUserName();
        chitChatMessage.setName(userName);
        chitChatMessage.setMessage(message);

        synchronized (outToServer) {
            outToServer.writeObject(chitChatMessage);
            outToServer.flush();
        }
    }
    
    /**
     * send private chat message to another client via server
     * @param receiverClientName
     * @param message
     * @throws IOException 
     */
    public void sendPrivate(String receiverClientName, String message) throws IOException {

        ChitChatMessage chitChatMessage = new ChitChatMessage(MessageType.PRIVATE);
        chitChatMessage.setName(receiverClientName);
        chitChatMessage.setMessage(message);

        synchronized (outToServer) {
            outToServer.writeObject(chitChatMessage);
            outToServer.flush();
        }
    }

    public void sendImOk() throws IOException {

        ChitChatMessage returnMessage = new ChitChatMessage(MessageType.IMOK);
        synchronized (outToServer) {
            outToServer.writeObject(returnMessage);
            outToServer.flush();
        }
        System.out.println("reply I'M OK");
    }

    public void displayPrivateChat(String senderClientName, String message) throws IOException {

        if (privateChatWindowMap.containsKey(senderClientName)) {
            //send message to the window
            PrivateChatWindow window = privateChatWindowMap.get(senderClientName);
            window.displayNewChatMessage(senderClientName+" : "+message+"\n");
        } else {
            // no private window for this client. open the window.
            PrivateChatWindow window = new PrivateChatWindow(clientPanel.getUserName(), senderClientName);
            privateChatWindowMap.put(senderClientName, window);
            window.setVisible(true);
            //send message to the window
            window.displayNewChatMessage(senderClientName+" : "+message+"\n");
        }
    }

    /*
     */    /**
     * requesting new list
     *
     * @throws IOException
     *//*
     public void sendNotify() throws IOException {

     ChitChatMessage chitChatMessage = new ChitChatMessage(MessageType.NOTIFY);
     chitChatMessage.setName(clientPanel.getUserName());

     synchronized (outToServer) {
     outToServer.writeObject(chitChatMessage);
     outToServer.flush();
     }

     }*/

}
