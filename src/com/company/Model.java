package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.util.*;

class CLI{
    public static void main(String[] args) throws IOException {
        Model model = new Model();
        model.initalise();
        model.setAnswer();
        CLIGame(model);
    }

    static void CLIGame(Model model) throws IOException {
        while (!model.winflag) {
            model.CLIPrint(model.calcTurn(model.TakeGuess()));
        }
        System.out.println("Enter 1 for a new game:");
        Scanner scan = new Scanner(System.in);  // Create a Scanner object
        int NewGame = scan.nextInt();
        if (NewGame==1){
            model.setAnswer();
            CLIGame(model);}
    }
}

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
    private boolean spoilerflag;
    private boolean randomflag= true;
    private String UserGuess = null;
    public boolean winflag;
    private final int guesslimit = 6;
    private ArrayList<String> Inplace = new ArrayList<String>();
    private ArrayList<String> Outplace =new ArrayList<String>();
    private ArrayList<String> Noplace =new ArrayList<String>();
    private ArrayList<String> Unplace =new ArrayList<String>();




    String text = "";
    public void Enter(String text){
        System.out.println("ENTER: " + text);
    }

    public String getAnswer() {
        return this.Answer;
    }

    public int getTurn(){return gameflag;}

    public boolean getWin(){return winflag;}


    public void setAnswer() {
        Random rand = new Random();
        if (randomflag) {
            this.Answer = Solution.get(rand.nextInt(Solution.size()));
        }
        else{this.Answer=Solution.get(0);};
        this.gameflag = 0;
        this.Inplace.clear();
        this.Outplace.clear();
        this.winflag = false;
        if (spoilerflag) {
            System.out.println(this.Answer);
        }
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


    //FIX ME - Binary search and removal :)))
    private Integer letterlogic(char letter, int pos) {
        if (letter == this.Answer.charAt(pos)){
            if (!Inplace.contains(String.valueOf(letter))) {
                Inplace.add(String.valueOf(letter));
            }
            if(Outplace.contains(String.valueOf(letter))){
                Outplace.remove(new String(String.valueOf((letter))));
            }
            return 2;
        } else if (this.Answer.indexOf(letter) != -1){
            if ((!Inplace.contains(String.valueOf(letter))) && (!Outplace.contains(String.valueOf(letter)))){
            Outplace.add(String.valueOf(letter));
        }
            return 1;
        } else {
            return 0;
        }
    }


    public void wordaccept(String GuessInput){
        UserGuess = null;
        if (Guess.contains(GuessInput)) {
            this.gameflag += 1;
            UserGuess = GuessInput;
            setChanged();
            notifyObservers();
        }
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
    
    public ArrayList<String> getInplace(){
        return (Inplace);
    }


    public ArrayList<String> getOutPlace(){
        return (Outplace);
    }



    // The following are only for CLI - can be safely moved to different class if needed

    private void output(){
        System.out.println("Inplace: " + this.Inplace);
        System.out.println("Out of place: " + this.Outplace);
    }

    public void CLIPrint(ArrayList<Integer> res) {
        int cpos = 0;
        for (int i : res){
            System.out.print(printlist.get(i) + UserGuess.charAt(cpos));
            cpos++;
        }
        System.out.println(printlist.get(0));
        output();
        if (gameflag > guesslimit){
            winflag = true;
            System.out.println(("Game over! Guess limit of %d reached.\nThe word was : " + this.getAnswer()).formatted(guesslimit));
        }
    }
    public String TakeGuess() {
        Scanner Scan = new Scanner(System.in);  // Create a Scanner object
        String GuessInput = null;
        System.out.println("\nEnter Guess");
        GuessInput = Scan.nextLine().toLowerCase();
        wordaccept(GuessInput);
        while (UserGuess==null) {
            System.out.println("Word not valid! \nEnter Guess");
            GuessInput = Scan.nextLine().toLowerCase();
            wordaccept(GuessInput);
        }
        return GuessInput;
    }
}

//set changed
//notifyobservers