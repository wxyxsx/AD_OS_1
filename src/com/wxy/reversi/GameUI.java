package com.wxy.reversi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GameUI extends Application {
	public ChessBoard reversi;
	private Pane p;
	private StackPane pane;
	private TextField txf;
	private Socket skt;
	private DataInputStream fs;
	private DataOutputStream ts;
	private int xc, yc;
	private boolean myTurn = false;
	private boolean waiting = true;
	private Label whiteNo,blackNo;
	private TextField Tplayer,Taddr,Tport;
	private TextArea message;

	@Override
	public void start(Stage primaryStage) {
		reversi = new ChessBoard();

		BorderPane bp = new BorderPane();
		// pane放置棋盘
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
				int st = PressChess(x, y);
				// 如果下棋成功则改变标志
				if (st == 1) {
					waiting = false;
					xc = x;
					yc = y;
				}
			}
		});
		// bp放置游戏界面
//		bp.setTop(pane);
//		bp.setBottom(getHBox());
		bp.setCenter(pane);
		
		Tplayer = new TextField();
		Tplayer.setPrefColumnCount(4);
		Taddr = new TextField();
		Taddr.setPrefColumnCount(4);
		Tport = new TextField();
		Tport.setPrefColumnCount(4);
		GridPane gp = new GridPane();
		gp.setAlignment(Pos.CENTER);
		gp.setPadding(new Insets(10,10,10,10));
		gp.setHgap(5);
		gp.setVgap(5);
		gp.add(new Label("玩家    "),0, 0);
		gp.add(Tplayer, 1,0);
		gp.add(new Label("黑棋    "),0, 1);
		gp.add(Taddr, 1,1);
		gp.add(new Label("白棋    "),0, 2);
		gp.add(Tport, 1,2);
		gp.add(new Label("白棋    "),0, 3);
		gp.add(new TextField(), 1, 3);
		gp.add(new Label("白棋    "),0, 4);
		gp.add(new TextField(), 1, 4);
		gp.add(new Button("hello"), 0, 5);
		gp.add(new Button("hello"), 1, 5);
		
		//BorderPane bp2 = new BorderPane();
		VBox bp3 = new VBox();
		//bp2.setTop(gp);
		message = new TextArea("fdhskf");
		message.setPrefColumnCount(15);
		message.setPrefRowCount(5);
		message.setWrapText(true);
		ScrollPane sp = new ScrollPane(message);
		//bp2.setPadding(new Insets(5,5,5,5));
		//bp2.setBottom(sp);
		bp3.getChildren().addAll(gp,sp);
		bp.setRight(bp3);
		

		Scene scene = new Scene(bp, 600, 420);
		primaryStage.setTitle("黑白棋");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private int PressChess(int x, int y) {
		if (!reversi.PlaceChess(x, y)) {
			return 0;
		}
		// repaint
		Platform.runLater(()->{
			pane.getChildren().remove(p);
			p = new ChessPane();
			pane.getChildren().add(p);
		});
		
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
			return 0;
		}
		return 1;
	}

	private HBox getHBox() {
		HBox hbox = new HBox(15);
		hbox.setPadding(new Insets(10, 10, 10, 10));
		txf = new TextField("欢迎来到黑白棋游戏");
		txf.setEditable(false);
		Button b1 = new Button("开始游戏");
		b1.setOnAction(e -> {
			connectToServer();
		});
//		b2.setOnAction(e -> {
//			reversi.Reset(1);
//			reversi.Start();
//			pane.getChildren().remove(p);
//			p = new ChessPane();
//			pane.getChildren().add(p);
//		});
		hbox.getChildren().addAll(b1, txf);
		hbox.setAlignment(Pos.CENTER);
		return hbox;
	}

	private void connectToServer() {
		try {
			skt = new Socket("localhost", 8000);
			fs = new DataInputStream(skt.getInputStream());
			ts = new DataOutputStream(skt.getOutputStream());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		new Thread(() -> {
			try {
				int p = fs.readInt();
				if (p == 1) {
					txf.setText("连接成功，等待另一用户");
					fs.readInt();
					txf.setText("玩家2连接成功，游戏开始");
				} else if (p == -1) {
					txf.setText("连接成功，等待玩家1开始");
				}
				reversi.Start();
				while (reversi.Check()) {
					if (p == 1) {
						waitForPlayerAction();
						sendMove();
						receiveInfoFromServer();
					} else if (p == -1) {
						receiveInfoFromServer();
						waitForPlayerAction();
						sendMove();
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}).start();
	}

	private void waitForPlayerAction() throws InterruptedException {
		// 等待用户下棋
		while (waiting) {
			Thread.sleep(100);
		}
		// 下棋后重新等待
		waiting = true;
	}

	private void sendMove() throws IOException {
		// 把信息发送出去
		ts.writeInt(xc);
		ts.writeInt(yc);
		txf.setText("发送坐标"+xc+","+yc);
	}

	private void receiveInfoFromServer() throws IOException {
		//接受数据下棋
		int r = fs.readInt();
		int c = fs.readInt();
		txf.setText("收到坐标"+r+","+c);
		PressChess(r, c);
	}

	class LinePane extends Pane {
		// 画棋盘
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
		// 画棋子
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

	public static void main(String[] args) {
		launch(args);
	}
}