package com.wxy.reversi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class Server {
    private int sno = 1; // 会话的标识
    private Logger log = Logger.getLogger(Server.class.getName());
    private int port;

    public Server(int port) {
        this.port = port;
    }

    private void start() {
        try {
            ServerSocket ss = new ServerSocket(port);
            log.info("服务端监听端口 " + port);

            while (true) {
                log.info("等待用户加入会话 " + sno);

                Socket p1 = ss.accept();
                log.info("玩家1加入会话 " + sno);
                log.info("玩家1的IP地址为 " + p1.getInetAddress().getHostAddress());

                new DataOutputStream(p1.getOutputStream()).writeInt(1);
                // 提醒玩家1连接已经建立，让其等待玩家2
                Socket p2 = ss.accept();
                log.info("玩家2加入会话 " + sno);
                log.info("玩家3的IP地址为 " + p2.getInetAddress().getHostAddress());

                new DataOutputStream(p2.getOutputStream()).writeInt(-1);
                // 提醒玩家2连接已经建立，让其等待玩家1操作
                new DataOutputStream(p1.getOutputStream()).writeInt(1);
                // 提醒玩家1游戏已经开始，可以下子
                log.info("Start two thread for session " + sno);
                sno++;
                new Thread(new HandleAS(p1, p2)).start();
                // 线程1把接收到p1的数据转发给p2
                new Thread(new HandleAS(p2, p1)).start();
                // 线程2把接收到p2的数据转发给p1
            }

        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    class HandleAS implements Runnable {
        // 一旦收到p1的数据立即转发给p2
        // 不管游戏的逻辑（由客户端控制）
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
