package com.company;

import java.util.ArrayList;

public class Controller {

    private Model model;
    private View view;

    public Controller(Model model) {this.model = model;}

    public void setView(View  view) {this.view = view;}

    public void Enter(String text) {model.wordaccept(text.toLowerCase());}

    public ArrayList<Integer> Calcturn(String text) {return model.calcTurn(text.toLowerCase());}

    public int getTurn(){return model.getTurn();    }

    public boolean getWin(){return model.getWin();}

    public void newGame(){model.setAnswer();}

}
