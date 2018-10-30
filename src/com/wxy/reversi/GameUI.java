package com.wxy.reversi;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GameUI extends Application {
	public ChessBoard reversi;
	private Pane p;
	private StackPane pane;

	@Override
	public void start(Stage primaryStage) {
		reversi = new ChessBoard();
		BorderPane bp = new BorderPane();
		pane = new StackPane();
		pane.setPadding(new Insets(10, 10, 10, 10));
		p = new ChessPane();
		pane.getChildren().add(new LinePane());
		pane.getChildren().add(p);
		pane.setOnMousePressed(e -> {
			if (!reversi.Check()) {
				return;
			}
			int x = (int) e.getX();
			int y = (int) e.getY();
			if (x >= 0 && x <= 400 && y >= 0 && y <= 400) {
				x = x / 50;
				y = y / 50;
				if (!reversi.PlaceChess(x, y)) {
					return;
				}
				// repaint
				pane.getChildren().remove(p);
				p = new ChessPane();
				pane.getChildren().add(p);
				if (!reversi.Check()) {
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
		bp.setTop(pane);
		bp.setBottom(getHBox());
		Scene scene = new Scene(bp, 500, 500);
		primaryStage.setTitle("黑白棋");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	private HBox getHBox() {
		HBox hbox = new HBox(15);
		hbox.setPadding(new Insets(10, 10, 10, 10));
		Button b1 = new Button("开始游戏");
		Button b2 = new Button("重新开始");
		b1.setOnAction(e -> {
			reversi.Start();
		});
		b2.setOnAction(e -> {
			reversi.Reset(1);
			reversi.Start();
			pane.getChildren().remove(p);
			p = new ChessPane();
			pane.getChildren().add(p);
		});
		hbox.getChildren().addAll(b1, b2);
		return hbox;
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