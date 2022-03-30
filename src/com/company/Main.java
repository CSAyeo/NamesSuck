package com.company;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        int SpoilerFlag = Decider("Standard", "Spoiler");
        int FixedFlag = Decider("Random Word", "Fixed Word");
        int GameType = Decider("CLI", "GUI");
        Model model = new Model();
        model.initalise();
        model.setflags(SpoilerFlag, FixedFlag);
        model.setAnswer();
        if (GameType == 1){
            CLIGame(model);
        }else if (GameType ==2){
            GUIGame(model);
        }else{
            System.out.println("ERROR");
        }
    }

    public static int Decider(String... params){
        Scanner Scan = new Scanner(System.in);  // Create a Scanner object
        int d = 0;
        while (!(d >= 1 && d <= params.length)) {
            System.out.printf("%s (1) OR %s (2)", params[0], params[1]);
            d = Scan.nextInt();  // Read user input
        }
        return d;
    }
    static void CLIGame(Model model) throws IOException {
        while (!model.winflag) {
            model.CLIPrint(model.calcTurn(model.TakeGuess()));
        }
        int NewGame = Decider("New Game", "End Game");
        if (NewGame==1){
            model.setAnswer();
            CLIGame(model);}
    }
    static void GUIGame(Model model){
        Controller controller = new Controller(model);
        View GUI = new View(model, controller);
    }
}