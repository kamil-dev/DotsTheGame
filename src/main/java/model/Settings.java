package main.java.model;

import main.java.view.BoardSquare;

//singelton
public class Settings {

    public static Settings GAME_SETTINGS = null;

    private Player p1;
    private Player p2;
    private int timer;
    private int boardSize;
    private Board board;
    private BoardSquare[][] boardSquares;

    private Settings(Player p1, Player p2, int timer, int boardSize) {
        this.p1 = p1;
        this.p2 = p2;
        this.timer = timer;
        this.boardSize = boardSize;
        this.board = new Board(boardSize);
    }

    public static void setGameSettings(Player p1, Player p2, int timer, int boardSize) {
            GAME_SETTINGS = new Settings(p1,p2,timer,boardSize);
    }

    public void setBoardSquares(BoardSquare[][] boardSquares){
        this.boardSquares = boardSquares;
    }

    public BoardSquare[][] getBoardSquares(){
        return boardSquares;
    }

    public Player getP1() {
        return p1;
    }

    public Player getP2() {
        return p2;
    }

    public int getTimer() {
        return timer;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public Board getBoard() {
        return board;
    }
}
