package com.wxy.reversi;

public class ChessBoard {
    private static final int SCALE = 8; // must be even number
    private int board[][] = new int[SCALE][SCALE]; // 0 space 1 white -1 black
    private int cur; // current number of space
    private int xf, yf; // searching result
    private int player; // current player
    private int white, black; // count the number of piece
    private boolean iflock; // check for deadlock | Mark whether the game starts

    // white first default
    public ChessBoard() {
        Reset(1);
    }

    // choose the order
    public ChessBoard(int o) {
        Reset(o);
    }

    public void SetPlayer(int o) {
        player = o;
    }

    public void Start() {
        iflock = false;
    }

    // reset the game state
    public void Reset(int s) {
        cur = SCALE * SCALE - 4;
        player = s;
        white = 0;
        black = 0;
        iflock = true;
        for (int i = 0; i < SCALE; i++) {
            for (int j = 0; j < SCALE; j++) {
                board[i][j] = 0;
            }
        }
        board[SCALE / 2 - 1][SCALE / 2 - 1] = 1;
        board[SCALE / 2][SCALE / 2] = 1;
        board[SCALE / 2 - 1][SCALE / 2] = -1;
        board[SCALE / 2][SCALE / 2 - 1] = -1;
    }

    // get the color of the piece
    public int GetChess(int x, int y) {
        return board[x][y];
    }

    // the number of white
    public int GetWhite() {
        SumChess();
        return white;
    }

    // the number of black
    public int GetBlack() {
        SumChess();
        return black;
    }

    // get current user
    public int GetPlayer() {
        return player;
    }

    private void SumChess() {
        white = 0;
        black = 0;
        for (int i = 0; i < SCALE; i++) {
            for (int j = 0; j < SCALE; j++) {
                if (board[i][j] == 1) {
                    white++;
                } else if (board[i][j] == -1) {
                    black++;
                }
            }
        }
    }

    // Change the pieces between (x1, y1) (x2, y2) to the same color
    private void change(int x1, int y1, int x2, int y2) {
        // the direction between pieces
        int dx = x2 - x1;
        int dy = y2 - y1;
        if (dx != 0) {
            dx = dx > 0 ? 1 : -1;
        }
        if (dy != 0) {
            dy = dy > 0 ? 1 : -1;
        }
        // change color one by one
        int i = x1 + dx;
        int j = y1 + dy;
        while (i != x2 || j != y2) {
            board[i][j] = player;
            i += dx;
            j += dy;
        }
    }

    // Determine if the piece is within the bounds
    private boolean judge(int x, int y) {
        if (x < 0 || x > 7 || y < 0 || y > 7) {
            return false;
        } else {
            return true;
        }
    }

    // Check if the current piece can be placed at a certain position
    private boolean ifallow(int x, int y) {
        if (board[x][y] != 0 || !search8(x, y)) {
            return false;
        } else {
            return true;
        }
    }

    // Search 8 directions
    private boolean search8(int x, int y) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (search(x, y, i, j)) {
                    // if find one result
                    return true;
                }
            }
        }
        return false;
    }

    // Detect if the current player can act
    private boolean ifaction() {
        for (int i = 0; i < SCALE; i++) {
            for (int j = 0; j < SCALE; j++) {
                if (ifallow(i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Search the current user color along the direction of dx, dy
    private boolean search(int x, int y, int dx, int dy) {
        int i = x + dx;
        int j = y + dy;
        while (judge(i, j)) {
            if (board[i][j] == player) {
                xf = i;
                yf = j;
                // If it is adjacent
                if (i == x + dx && j == y + dy) {
                    return false;
                } else {
                    return true;
                }
            } else if (board[i][j] == 0) {
                // If there is a blank
                return false;
            }
            i += dx;
            j += dy;
        }
        return false;
    }

    // Check if the game is over
    public boolean Check() {
        if (cur == 0 || GetWhite() == 0 || GetBlack() == 0 || iflock == true) {
            return false;
        } else {
            return true;
        }
    }

    // Determine if you can put a piece
    public boolean PlaceChess(int x, int y) {
        if (!ifallow(x, y)) {
            return false;
        }
        cur -= 1;
        board[x][y] = player;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (search(x, y, i, j)) {
                    change(x, y, xf, yf);
                }
            }
        }
        player *= -1;
        // If you can’t act, it’s still moved by the original user.
        if (!ifaction()) {
            player *= -1;
            if (!ifaction()) {
                iflock = true;
            }
        }
        return true;
    }
}
