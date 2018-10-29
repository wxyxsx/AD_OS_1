package com.wxy.reversi;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUI extends JFrame {
	private BoardUI game;
	private JTextField jtfMessage = new JTextField("欢迎来到黑白棋游戏");

	public GUI() {
		game = new BoardUI();
		game.addMouseListener(new Press());
		setLayout(new BorderLayout(0,0));
		add(game,BorderLayout.CENTER);
		add(jtfMessage,BorderLayout.SOUTH);
	}

	class Press implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (!game.reversi.Check()) {
				return;
			}
			int x = e.getX();
			int y = e.getY();
			if (x >= 0 && x <= 400 && y >= 0 && y <= 400) {
				x = x / 50;
				y = y / 50;
				if (!game.reversi.PlaceChess(x, y)) {
					jtfMessage.setText("棋子必须放在能提子的位置！");
					return;
				}
				game.repaint();
				// Check if the game is over
				// 大不了先标记一下
				if (!game.reversi.Check()) {
					int i = game.reversi.GetBlack();
					int j = game.reversi.GetWhite();
					String str = String.format("黑棋%d个，白棋%d个\n", i, j);
					if (i > j) {
						JOptionPane.showMessageDialog(null, str, "游戏结束，黑方胜利", JOptionPane.INFORMATION_MESSAGE);
					} else if (i < j) {
						JOptionPane.showMessageDialog(null, str, "游戏结束，白方胜利", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, str, "游戏结束，平局胜利", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

	public static void main(String[] args) {
		GUI frame = new GUI();
		frame.setSize(500, 500);
		frame.setTitle("黑白棋");
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
