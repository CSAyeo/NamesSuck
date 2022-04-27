package com.company;

import java.util.ArrayList;

public class Controller {

    private Model model;
    private View view;
    private String word ="";

    public Controller(Model model) {this.model = model;}

    public void setView(View  view) {this.view = view;}

    public int getLimit() {return model.getLimit();}

    public void Enter() {model.wordaccept(word.toLowerCase());}

    public ArrayList<Integer> Calcturn(String text) {return model.calcTurn(text.toLowerCase());}

    public int getTurn(){return model.getTurn();}

    public boolean getWin(){return model.getWin();}

    public void newGame(){
        view.clearWords();
        if (!view.Keyboard[0].isEnabled()){
            view.ToggleButtons();
        }else{view.ClearButtons();}
        model.setAnswer();
    }


    void EndHandler(){
        if (getTurn()==1) {
            view.addNewGame();
        }
        if (getTurn()>=6 || getWin()){
            view.ToggleButtons();
        }
    }
     void addletter(String letter){
        if (word.length() < 5){
            word = word + letter;

        }
    }

    void dropword(){
        word = "";
    }

    void removeletter(){
        assert word.length() >= 1;
        word = word.substring(0, word.length() - 1);
    }

    String getword(){
        return word;
    }
}
