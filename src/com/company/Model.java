package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.util.*;


public class Model extends Observable {
    public static final String Reset = "\u001B[0m";
    public static final String OutPlace = "\u001B[43m";
    public static final String InPlace = "\u001B[42m";
    private static final String Solution_File = "src/com/company/common.txt";
    private static final String Guess_File = "src/com/company/words.txt";
    private static List<String> Solution;
    private static List<String> Guess;
    private String Answer;
    private int gameflag;
    private String UserGuess = null;
    public boolean winflag;

    String text = "";
    public void Enter(String text){
        System.out.println("ENTER: " + text);
    }

    public String getAnswer() {
        return this.Answer;
    }

    public int getTurn(){return gameflag;}

    public void setAnswer() {
        Random rand = new Random();
        this.Answer = Solution.get(rand.nextInt(Solution.size()));
        this.gameflag = 0;
    }

    public void initalise() throws IOException {
        this.Guess = new ArrayList<>();
        BufferedReader sl = new BufferedReader(new FileReader(Solution_File));
        this.Solution = new ArrayList<>();
        String line;
        while ((line = sl.readLine()) != null) {
            this.Solution.add(line);
            this.Guess.add(line);
        }
        sl.close();
        //Guess list
        BufferedReader gr = new BufferedReader(new FileReader(Guess_File));
        while ((line = gr.readLine()) != null) {
            this.Guess.add(line);
        }
        gr.close();
    }

    private Integer letterlogic(char letter, int pos) {
        if (letter == this.Answer.charAt(pos)) {
            return 2;
        } else if (this.Answer.indexOf(letter) != -1) {
            return 1;
        } else {
            return 0;
        }
    }


    private boolean wordaccept(String GuessInput){
        if (Guess.contains(GuessInput)) {
            this.gameflag += 1;
            UserGuess = GuessInput;
            return true;
        } else {
            System.out.println("Word not recognised");
        }
        return false;
    }

    public String calcTurn(String UserGuess) {
        int cpos = 0;
        String res = "";
        if (UserGuess.equals(this.Answer)) {
            this.winflag = true;
            System.out.println(Reset + "YOU WON!");
        }

        for (char letter : UserGuess.toCharArray()){
            res = res + letterlogic(letter, cpos);
            cpos++;
        }
        return res;
    }

    // The following are only for CLI - can be safely moved to different class if needed
    private void CLIPrint(int res) {
        for (int i = 0; i < 5; i++) {
            switch (res) {
                case 2:
                    System.out.print(InPlace + UserGuess.charAt(i));
                    break;
                case 1:
                    System.out.print(OutPlace + UserGuess.charAt(i));
                    break;
                case 0:
                    System.out.print(Reset + UserGuess.charAt(i));
                    break;
                default:
                    System.out.println("BROKEN========================================: " + res);
            }
            System.out.print(Reset);
        }
    }
    public String TakeGuess() {
        boolean flag = false;
        Scanner Scan = new Scanner(System.in);  // Create a Scanner object
        String GuessInput = null;
        while (!flag) {
            System.out.println("\nEnter Guess");
            GuessInput = Scan.nextLine().toLowerCase();
            flag = wordaccept(GuessInput);
        }
        return GuessInput;
    }
}
