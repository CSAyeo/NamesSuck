package com.company;

import java.io.IOException;

public class Controller {
    
    private Model model;
    private View view;
    
    public Controller(Model model) {
        this.model = model;
    }
    
    public void setView(View  view) {
        this.view = view;
    }
    
    public void Enter(String text) {
        model.Enter(text);
    }
    public int getTurn(){
       return model.getTurn();
    }

}
