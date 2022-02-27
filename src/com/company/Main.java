package com.company;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        int GameType = Decider();
        Model model = new Model();
        model.initalise();
        model.setAnswer();
        if (GameType == 1){
            CLIGame(model);
        }else if (GameType ==2){
            GUIGame(model);
        }else{
            System.out.println("ERROR");
        }
    }

    public static int Decider(){
        Scanner Scan = new Scanner(System.in);  // Create a Scanner object
        int d = 0;
        while (d != 1 && d !=2) {
            System.out.println(d);
            System.out.println("CLI (1) OR GUI (2)");
            d = Scan.nextInt();  // Read user input
        }
        return d;
    }
    static void CLIGame(Model model) {
        while (!model.winflag) {
            model.calcTurn(model.TakeGuess());
        }
    }
    static void GUIGame(Model model){
        Controller controller = new Controller(model);
        View GUI = new View(model, controller);
        GUI.initGUI();

    }
}