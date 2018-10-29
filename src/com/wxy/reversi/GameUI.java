package com.wxy.reversi;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GameUI extends JFrame implements MouseListener, MouseMotionListener {
	// 动态调整UI大小
	// 先调整棋盘逻辑
	private static final long serialVersionUID = 1L;
	private ChessBoard test;
	int x, y;// Mouse coordinates

	public GameUI() {
		test = new ChessBoard(1);
		this.setTitle("Reversi");
		this.setSize(500, 500);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.repaint();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	public void paint(Graphics g) {
		BufferedImage buf = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
		Graphics g1 = buf.createGraphics();
		g1.setColor(new Color(153, 153, 255));
		g1.fill3DRect(20, 40, 400, 400, true);
		// x y width height
		for (int i = 1; i < 8; i++) {
			g1.setColor(Color.WHITE);
			g1.drawLine(20, 40 + i * 50, 20 + 400, 40 + i * 50);// drawing Horizontal line
			// x1 y1 x2 y2
			g1.drawLine(20 + i * 50, 40, 20 + i * 50, 40 + 400); // drawing Vertical line
		}
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				// drawing black
				if (test.GetChess(i, j) == -1) {
					int tempX = i * 50 + 20;
					int tempY = j * 50 + 40;
					g1.setColor(Color.BLACK);
					g1.fillOval(tempX, tempY, 50, 50);
					g1.setColor(Color.BLACK);
					g1.drawOval(tempX, tempY, 50, 50);
				}

				// drawing white
				if (test.GetChess(i, j) == 1) {
					int tempX = i * 50 + 20;
					int tempY = j * 50 + 40;
					g1.setColor(Color.WHITE);
					g1.fillOval(tempX, tempY, 50, 50);
					g1.setColor(Color.BLACK);
					g1.drawOval(tempX, tempY, 50, 50);
				}
			}
		}
		g.drawImage(buf, 0, 0, this);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		x = e.getX();
		y = e.getY();
		if (x >= 20 && x <= 420 && y >= 40 && y <= 440) {
			x = (x - 20) / 50;
			y = (y - 40) / 50;
			if (!test.PlaceChess(x, y)) {
				JOptionPane.showMessageDialog(this, "棋子必须放在能提子的位置！");
				return;
			}
			if (test.GetPlayer() == 1) {
				this.setTitle("白棋行动");
			} else {
				this.setTitle("黑棋行动");
			}
			this.repaint();
			// Check if the game is over
			// 大不了先标记一下
			if (!test.Check()) {
				int i = test.GetBlack();
				int j = test.GetWhite();
				String str = String.format("黑棋%d个，白棋%d个\n", i, j);
				if (i > j) {
					JOptionPane.showMessageDialog(this, str + "游戏结束，黑方胜利");
				} else if (i < j) {
					JOptionPane.showMessageDialog(this, str + "游戏结束，白方胜利");
				} else {
					JOptionPane.showMessageDialog(this, str + "游戏结束，平局");
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
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
	public void mouseMoved(MouseEvent e) {
//		int i = e.getXOnScreen();
//		int j = e.getYOnScreen();
//		String str = String.format("(%d,%d)", i, j);
//		this.setTitle(str);
	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

//	public static void main(String[] args) {
//		new GameUI();
//	}

}
