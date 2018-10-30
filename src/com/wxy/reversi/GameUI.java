package com.wxy.reversi;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameUI extends Application {
	public ChessBoard reversi;
	private Pane p1, p2;

	@Override
	public void start(Stage primaryStage) {
		reversi = new ChessBoard();
		StackPane pane = new StackPane();
		p1 = new LinePane();
		p2 = new ChessPane();
		pane.getChildren().add(p1);
		pane.getChildren().add(p2);	
		pane.setOnMousePressed(e -> {
			if(!reversi.Check()) {
				return;
			}
			int x = (int) e.getX();
			int y = (int) e.getY();
			if (x >= 0 && x <= 400 && y >= 0 && y <= 400) {
				x = x / 50;
				y = y / 50;
				if(!reversi.PlaceChess(x, y)) {
					return;
				}
				//repaint
				pane.getChildren().remove(p2);
				p2 = new ChessPane();
				pane.getChildren().add(p2);
				if(!reversi.Check()) {
					int i = reversi.GetBlack();
					int j = reversi.GetWhite();
					String str = String.format("黑棋%d个，白棋%d个\n", i, j);
					String txt;
					if (i > j) {
						txt = "游戏结束，黑方胜利";
					} else if (i < j) {
						txt = "游戏结束，白方胜利";
					} else {
						txt = "游戏结束，平局胜利";
					}
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle(txt);
					alert.setHeaderText(null);
					alert.setContentText(str);
					alert.show();
				}
			}
			
		});
		Scene scene = new Scene(pane, 500, 500);
		primaryStage.setTitle("黑白棋");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	class LinePane extends Pane {
		public LinePane() {
			Rectangle r = new Rectangle(0, 0, 400, 400);
			r.setStroke(Color.BLACK);
			r.setFill(Color.GREY);
			getChildren().add(r);
			for (int i = 1; i < 8; i++) {
				Line line1 = new Line(0, i * 50, 400, i * 50);
				Line line2 = new Line(i * 50, 0, i * 50, 400);
				line1.setStroke(Color.WHITE);
				line2.setStroke(Color.WHITE);
				getChildren().addAll(line1, line2);
			}
		}
	}

	class ChessPane extends Pane {
		public ChessPane() {
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					Circle c = new Circle(25 + i * 50, 25 + j * 50, 20);
					if (reversi.GetChess(i, j) == -1) {
						c.setFill(Color.BLACK);
					} else if (reversi.GetChess(i, j) == 1) {
						c.setFill(Color.WHITE);
					} else {
						continue;
					}
					getChildren().add(c);
				}
			}
		}
	}
}