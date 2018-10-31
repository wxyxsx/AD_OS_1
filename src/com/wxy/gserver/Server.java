package com.wxy.gserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class Server {
	private int sno = 1; // 会话的序号
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
				// 提醒玩家1连接建立成功，等待玩家2
				Socket p2 = ss.accept();
				log.info("Player 2 joined session " + sno);
				log.info("Player 2's address " + p2.getInetAddress().getHostAddress());

				new DataOutputStream(p2.getOutputStream()).writeInt(-1);
				// 提醒玩家2连接建立成功，可以开始输入
				new DataOutputStream(p1.getOutputStream()).writeInt(1);
				// 提醒玩家1玩家2已经加入，可以发送消息
				log.info("Start two thread for session " + sno);
				sno++;
				new Thread(new HandleAS(p1, p2)).start();
				// 线程1把p1的请求全部发给p2
				new Thread(new HandleAS(p2, p1)).start();
				// 线程2把p2的请求全部发给p1
			}

		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

	class HandleAS implements Runnable {
		// 一旦从p1收到消息就立即转发给p2，不用管逻辑问题
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
					// todo 修改为发送对象
					int i = fp1.readInt();
					
					if (i == -1) {
						break;
					}
					
					tp2.writeInt(i);
					// 转发消息
					
					//停止判断（约定）
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
