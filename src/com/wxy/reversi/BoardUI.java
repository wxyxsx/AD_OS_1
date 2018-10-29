package com.wxy.reversi;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class BoardUI extends JPanel {
	public ChessBoard reversi;

	public BoardUI() {
		reversi = new ChessBoard();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.gray);
		g.fillRect(0, 0, 400, 400);
		g.setColor(Color.WHITE);
		for (int i = 1; i < 8; i++) {
			g.setColor(Color.WHITE);
			g.drawLine(0, i * 50, 400, i * 50);// drawing Horizontal line
			g.drawLine(i * 50, 0, i * 50, 400); // drawing Vertical line
		}
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (reversi.GetChess(i, j) == -1) {
					g.setColor(Color.BLACK);
					g.fillOval(5 + i * 50, 5 + j * 50, 40, 40);
				} else if (reversi.GetChess(i, j) == 1) {
					g.setColor(Color.WHITE);
					g.fillOval(5 + i * 50, 5 + j * 50, 40, 40);
				}
			}
		}
	}
}
