package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.util.*;


public class Model extends Observable {
    /*
    public static final String Reset = "\u001B[0m";
    public static final String OutPlace = "\u001B[0m";
    public static final String InPlace = "\u001B[42m";
     */
    private List<String> printlist = Arrays.asList("\u001B[0m", "\u001B[43m","\u001B[42m" ); //reset, out, in
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


    public boolean wordaccept(String GuessInput){
        System.out.println("WORD DETECTED : " + GuessInput);
        System.out.println("LOGIC" + (Guess.contains(GuessInput)));
        System.out.println(Guess);
        if (Guess.contains(GuessInput)) {
            this.gameflag += 1;
            UserGuess = GuessInput;
            return true;
        }
            return false;
    }

    public ArrayList<Integer> calcTurn(String UserGuess) {
        int cpos = 0;
        ArrayList<Integer> res = new ArrayList<Integer>();
        if (UserGuess.equals(this.Answer)) {
            this.winflag = true;
            System.out.println(printlist.get(0) + "YOU WON!");
        }

        for (char letter : UserGuess.toCharArray()){
            res.add(letterlogic(letter, cpos));
            cpos++;
        }
        return res;
    }

    // The following are only for CLI - can be safely moved to different class if needed
    public void CLIPrint(ArrayList<Integer> res) {
        int cpos = 0;
       for (int i : res){
           System.out.print(printlist.get(i) + UserGuess.charAt(cpos));
           cpos++;
       }
        System.out.println(printlist.get(0));
       if (gameflag > 6){
           winflag = true;
           System.out.println("Game over! Guess limit of 6 reached.\nThe word was : " + this.getAnswer());
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
