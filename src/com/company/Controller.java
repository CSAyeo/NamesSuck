package com.company;

import java.util.ArrayList;

public class Controller {

    private Model model;
    private View view;
    private String word ="";

    public Controller(Model model) {this.model = model;} //sets the model

    public void setView(View  view) {this.view = view;} //sets the view

    public int getLimit() {return model.getLimit();} //gets the turn limit, for sizing of the GUI

    public void Enter() {
        if (word.length() == 5){
            model.wordaccept(word.toLowerCase());
        }
    } //triggers when enter is pressed on the GUI

    public ArrayList<Integer> Calcturn() {return model.calcTurn();}//gets the integer return of the turn

    public int getTurn(){return model.getTurn();} //gets the current turn count, for the y axis of current word

    public boolean getWin(){return model.getWin();} //checks for a win

    public void newGame(){ //clears the GUI for a new game
        view.clearWords();
        if (!view.Keyboard[0].isEnabled()){
            view.ToggleButtons();
        }else{view.ClearButtons();}
        model.setAnswer();
    }


    void EndHandler(){ //adds a new game button after the first turn and disables the buttons when the game is over
        if (getTurn()==1) {
            view.addNewGame();
        }
        if (getTurn()>= model.getLimit() || getWin()){
            view.ToggleButtons();
        }
    }

     void addletter(String letter){//builds the word variable to submit
         if (word.length() < 5) {
             word = word + letter;
             view.displayword();
         }
    }

    void dropword(){
        word = "";
    } //clears the current word

    void removeletter(){ //removes the letter from the word
        assert word.length() >= 1;
        //System.out.println("pre: %s \n post: %s".formatted(word, word.substring(0, word.length() - 1)));
        word = word.substring(0, word.length() - 1);
    }

    int getwl(){
        return word.length();
    }

    char getlet(int lpos){
        return word.charAt(lpos);
    }
    String getword(){
        return word;
    }

}
