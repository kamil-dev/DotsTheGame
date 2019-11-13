package main.java.model;

import main.java.view.BoardSquare;

import java.io.Serializable;

//singelton
public class Settings implements Serializable {

    public static Settings gameSettings = null;
    private Player p1;
    private Player p2;
    private int timer;
    private int boardSize;
    private Board board;
    private BoardSquare[][] boardSquares;
    private Theme globalTheme;
    private boolean isLoaded;

    private Settings(Player p1, Player p2, int timer, int boardSize) {
        this.p1 = p1;
        this.p2 = p2;
        this.timer = timer;
        this.boardSize = boardSize;
        this.board = new Board(boardSize);
        globalTheme = new Theme();
        isLoaded = false;
    }

    public static void setGameSettings(Player p1, Player p2, int timer, int boardSize) {
            gameSettings = new Settings(p1,p2,timer,boardSize);
    }

    public static void setGameSettings(Settings loadedSettings){
        gameSettings = loadedSettings;
        gameSettings.setIsLoaded(true);
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

    public Theme getGlobalTheme() {
        return globalTheme;
    }

    private void setIsLoaded(boolean isLoaded){
        this.isLoaded = isLoaded;
    }

    public boolean isLoaded() {
        return isLoaded;
    }
}
