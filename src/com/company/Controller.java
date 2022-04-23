package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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

}
