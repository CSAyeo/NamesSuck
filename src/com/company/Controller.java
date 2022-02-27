package com.company;

import java.util.ArrayList;

public class Controller {
    
    private Model model;
    private View view;
    
    public Controller(Model model) {this.model = model;}

    public void setView(View  view) {this.view = view;}

    public boolean Enter(String text) {System.out.println("SENDING : " + text);return model.wordaccept(text);}

    public ArrayList<Integer> Calcturn(String text) {return model.calcTurn(text);}

    public int getTurn(){
       return model.getTurn();
    }

}
