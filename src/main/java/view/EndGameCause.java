package main.java.view;

public enum EndGameCause {
    RESIGNATION("Your opponent has resigned!"),
    TIMER("Your opponent has ran out of time!"),
    NO_AVAILABLE_MOVE("The whole board is filled with dots!");

    private String text;
    EndGameCause(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
