package server.udp;


import java.net.DatagramSocket;

/**
 * Created by PorPaul on 17/11/2558.
 */
public class ChitChatUDP implements Runnable {
    private int port;
    private DatagramSocket chitchatServer;

    public ChitChatUDP(int port) {
        this.port = port;
    }

    @Override
    public void run() {

    }

}