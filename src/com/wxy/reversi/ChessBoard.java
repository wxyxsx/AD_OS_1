package com.wxy.reversi;

public class ChessBoard {
    private static final int SCALE = 8;
    // 棋盘大小，必须为偶数
    private int board[][] = new int[SCALE][SCALE];
    // 0 空格 1 白棋 2 黑棋
    private int cur;
    // 当前空格数量
    private int xf, yf;
    // 需要发送的坐标
    private int player;
    // 当前玩家 1 白棋 -1 黑棋 直接通过*-1转换
    private int white, black;
    // 当前白棋和黑棋的数量
    private boolean iflock;
    // 标记游戏是否开始 或 标记是否两个玩家都没法下子 （逻辑不冲突）

    // 默认白棋先走
    public ChessBoard() {
        Reset(1);
    }

    // unused 可以选择先手
    public ChessBoard(int o) {
        Reset(o);
    }

    // unused 设置先手玩家
    public void SetPlayer(int o) {
        player = o;
    }

    // 游戏开始
    public void Start() {
        iflock = false;
    }

    // 初始化/重置游戏状态
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

    // 获得某一坐标棋子的颜色
    public int GetChess(int x, int y) {
        return board[x][y];
    }

    // 计算白棋的数量
    public int GetWhite() {
        SumChess();
        return white;
    }

    // 计算黑棋的数量
    public int GetBlack() {
        SumChess();
        return black;
    }

    // 获取当前要下子的用户
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

    // 把坐标为(x1, y1)、(x2, y2)的两点之间设置为同一颜色
    private void change(int x1, int y1, int x2, int y2) {
        // 计算两点的方向
        int dx = x2 - x1;
        int dy = y2 - y1;
        if (dx != 0) {
            dx = dx > 0 ? 1 : -1;
        }
        if (dy != 0) {
            dy = dy > 0 ? 1 : -1;
        }
        // 依次改变颜色
        int i = x1 + dx;
        int j = y1 + dy;
        while (i != x2 || j != y2) {
            board[i][j] = player;
            i += dx;
            j += dy;
        }
    }

    // 判断坐标是否在界限内
    private boolean judge(int x, int y) {
        if (x < 0 || x > SCALE - 1 || y < 0 || y > SCALE - 1) {
            return false;
        } else {
            return true;
        }
    }

    // 判断当前位置是否能下子
    private boolean ifallow(int x, int y) {
        if (board[x][y] != 0 || !search8(x, y)) {
            return false;
        } else {
            return true;
        }
    }

    // 沿八个方向搜索棋子
    private boolean search8(int x, int y) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (search(x, y, i, j)) {
                    // 只要找到一个结果就返回
                    return true;
                }
            }
        }
        return false;
    }

    // 遍历棋盘判断当前用户是否能行动
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

    // 沿着dx，dy的方向找出是否有当前颜色的棋子
    private boolean search(int x, int y, int dx, int dy) {
        int i = x + dx;
        int j = y + dy;
        while (judge(i, j)) {
            if (board[i][j] == player) {
                xf = i;
                yf = j;
                // 相邻的棋子不算
                if (i == x + dx && j == y + dy) {
                    return false;
                } else {
                    return true;
                }
            } else if (board[i][j] == 0) {
                // 如果是空格则肯定没有
                return false;
            }
            i += dx;
            j += dy;
        }
        return false;
    }

    // 判断游戏是否结束
    public boolean Check() {
        if (cur == 0 || GetWhite() == 0 || GetBlack() == 0 || iflock == true) {
            return false;
        } else {
            return true;
        }
    }

    // 判断是否能下子
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
        // 如果对方不能行动则依然是当前用户下子
        if (!ifaction()) {
            player *= -1;
            if (!ifaction()) {
                iflock = true;
            }
        }
        return true;
    }
}
