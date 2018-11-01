package com.wxy.gserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class Server {
    private int sno = 1; // session No.
    private Logger log = Logger.getLogger(Server.class.getName());
    private int port;

    public Server(int port) {
        this.port = port;
    }

    private void start() {
        try {
            ServerSocket ss = new ServerSocket(port);
            log.info("Server started at socket " + port);

            while (true) {
                log.info("Wait for players to join session " + sno);

                Socket p1 = ss.accept();
                log.info("Player 1 joined session " + sno);
                log.info("Player 1's address " + p1.getInetAddress().getHostAddress());

                new DataOutputStream(p1.getOutputStream()).writeInt(1);
                // Remind players that 1 connection is established successfully, waiting for player 2
                Socket p2 = ss.accept();
                log.info("Player 2 joined session " + sno);
                log.info("Player 2's address " + p2.getInetAddress().getHostAddress());

                new DataOutputStream(p2.getOutputStream()).writeInt(-1);
                // Remind the player 2 that the connection is successfully established and can start typing
                new DataOutputStream(p1.getOutputStream()).writeInt(1);
                // Remind players 1 that player 2 has joined and can send a message
                log.info("Start two thread for session " + sno);
                sno++;
                new Thread(new HandleAS(p1, p2)).start();
                // Thread 1 sends all requests from p1 to p2
                new Thread(new HandleAS(p2, p1)).start();
                // Thread 2 sends all requests from p2 to p1
            }

        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    class HandleAS implements Runnable {
        // Once received from p1, it is immediately forwarded to p2
        // without thinking of game logic
        private Socket p1, p2;
        private DataInputStream fp1;
        private DataOutputStream tp2;

        public HandleAS(Socket p1, Socket p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        @Override
        public void run() {
            try {
                fp1 = new DataInputStream(p1.getInputStream());
                tp2 = new DataOutputStream(p2.getOutputStream());

                while (true) {
                    // todo Modified to send object
                    int i = fp1.readInt();

                    if (i == -1) {
                        break;
                    }
                    tp2.writeInt(i);
                }
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }

    }

    public static void main(String args[]) {
        Server s = new Server(8000);
        s.start();
    }
}
