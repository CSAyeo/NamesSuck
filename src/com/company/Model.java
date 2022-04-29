package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.util.*;

class CLI{
    public static void main(String[] args) throws IOException {//Main for a new CLI game
        Model model = new Model(); //initates a Model class, no controller or view required
        model.initalise(); //exectues model first run initalisation
        model.setAnswer(); //Calls set answer to select a new answer
        CLIGame(model); //calls the gameloop method
    }

    static void CLIGame(Model model) throws IOException {
        while (model.getGame()) { //while the game isnt over
            model.TakeGuess();
            model.CLIPrint(model.calcTurn());
        }
        if (model.winflag){System.out.println("YOU WON!");}
        System.out.println("Enter 1 for a new game:"); //offer the user a new game
        Scanner scan = new Scanner(System.in);  // Create a Scanner object
        int NewGame = scan.nextInt();
        if (NewGame==1){
            model.setAnswer(); //Calls set answer to select a new answer
            CLIGame(model);} //recursively create a new game
    }
}

public class Model extends Observable {

    //variables used for running the game
    private static final String Solution_File = "src/com/company/common.txt";
    private static final String Guess_File = "src/com/company/words.txt";
    private static List<String> Solution; //list of valid answers to select from
    private static List<String> Guess; //list of valid guesses for entry
    private boolean gameflag; //is game in play?
    public boolean winflag;
    private String Answer;
    private int turncount;
    private String UserGuess = ""; //stores the user guess for processing

    //adjustable flags
    private final boolean spoilerflag = true; //print the spoiler? true - print, false -don't print
    private final boolean randomflag = true; //select a random word or fixed. true - random, false 'cigar'
    private final boolean validflag = false; // Allow entry of 5 letters not in wordlist. true - allow all, false - allow only valid
    private final int guesslimit = 5; //sets the number of guesses allowed per game, GUI is dynamically resized

    //CLI Variables
    private List<String> printlist = Arrays.asList("\u001B[0m", "\u001B[43m", "\u001B[42m"); //reset, out, in, accessed by index from letterlogic
    private ArrayList<String> Inplace = new ArrayList<String>();
    private ArrayList<String> Outplace = new ArrayList<String>();
    private ArrayList<String> Noplace = new ArrayList<String>();
    private ArrayList<String> Unplace = new ArrayList<String>();

    /*  Precondition:   none
        Postcondition:  turncount < guesslimit
                        Answer != null and Answer != ""
                        Inplace = ∅
                        Outplace = ∅
                        Noplace = ∅
                        Unplace = {a, b,c,d,e,f,....,z}
     */
    public void setAnswer() {
        Random rand = new Random();
        if (randomflag) { //if the flag is set to random (true)
            this.Answer = Solution.get(rand.nextInt(Solution.size())); //get a random answer from the list
        } else {
            this.Answer = Solution.get(0); //otherwise select position 0 - 'cigar'
        }
        this.turncount = 0; //reset the turns
        this.Inplace.clear(); //reset all lists
        this.Outplace.clear();
        this.Noplace.clear();
        this.Unplace.clear();
        for (int i = 97; i < 123; i++) { //set Unplaced to contain all valid letters
            Unplace.add(String.valueOf((char) (i)));
        }
        this.gameflag = true; //set the game to running
        this.winflag = false;
        if (spoilerflag) {
            System.out.println(this.Answer);
        }
    }

    /*  Precondition:   SolutionFile != null
                        GuessFile != null
        Postcondition:
                        this.Solution !=null
                        this.Guess != null
     */
    public void initalise() throws IOException {
        this.Guess = new ArrayList<>();
        BufferedReader sl = new BufferedReader(new FileReader(Solution_File));
        this.Solution = new ArrayList<>();
        String line;
        while ((line = sl.readLine()) != null) {
            this.Solution.add(line);
            this.Guess.add(line); //for maximum efficiency you could not include this step if the validflag is false,
            // but it would require extra checks and seperation for little gain
        }
        sl.close();
        //Guess list, again not needed if validflag is false
        BufferedReader gr = new BufferedReader(new FileReader(Guess_File));
        while ((line = gr.readLine()) != null) {
            this.Guess.add(line);
        }
        gr.close();
    }

    /*  Precondition:   pos >= 0
                        letter != "" & letter != null
        Postcondition:
                        letter ∉ Unplace
                        letter ∈ Inplace xor letter ∈ Outplace xor letter ∈ Noplace
     */
    //Checks the letter against the answer
    private Integer letterlogic(char letter, int pos) {
        assert this.Answer != null;
        if (letter == this.Answer.charAt(pos)) { //perfect match, green
            if (!Inplace.contains(String.valueOf(letter))) {
                Inplace.add(String.valueOf(letter));
            }
            if (Outplace.contains(String.valueOf(letter))) {
                Outplace.remove(new String(String.valueOf((letter))));
            }
            if (Unplace.contains(String.valueOf(letter))) {
                Unplace.remove(new String(String.valueOf((letter))));
            }
            return 2;
        } else if (this.Answer.indexOf(letter) != -1) { //letter features in word, but not location
            if ((!Inplace.contains(String.valueOf(letter))) && (!Outplace.contains(String.valueOf(letter)))) {
                Outplace.add(String.valueOf(letter));
            }
            if (Unplace.contains(String.valueOf(letter))) {
                Unplace.remove(new String(String.valueOf((letter))));
            }
            return 1;
        } else {//letter does not feature in word
            if (Unplace.contains(String.valueOf(letter))) {
                Unplace.remove(new String(String.valueOf((letter))));
            }
            if (!Noplace.contains(String.valueOf(letter))) {
                Noplace.add(String.valueOf(letter));
            }
            return 0;
        }
    }

    //check that the word is an acceptable length, is a valid word and/or loose entry is allowed
        /*  Precondition: GuessInput !=null
        Postcondition: if GuessInput.length =5 & GuessInput ∈ Guess, view.update()
     */
    public void wordaccept(String GuessInput) {
        if (GuessInput.length() == 5) {
            if (Guess.contains(GuessInput) || validflag) { //if the guess is valid or flag is set to allow all 'words'
                this.turncount += 1;
                UserGuess = GuessInput;
                setChanged();  //trigger an update to the view to set colours
                notifyObservers();
            }else{UserGuess = "";}
        }else{UserGuess = "";}
    }

    /*  Precondition:   UserGuess != null
                        this.Answer != null
        Postcondition:  res.length() = 5

     */
    public ArrayList<Integer> calcTurn() {
        int cpos = 0;
        ArrayList<Integer> res = new ArrayList<Integer>();
        if (UserGuess.equals(this.Answer)) {
            this.gameflag = false;
            this.winflag = true;
        }
        for (char letter : UserGuess.toCharArray()) {
            res.add(letterlogic(letter, cpos));
            //sort all the lists - a BST implementation would be preferable but this works for now
            Collections.sort(Noplace);
            Collections.sort(Unplace);
            Collections.sort(Inplace);
            Collections.sort(Outplace);
            cpos++;
        }
        return res;
    }

    public String getAnswer() { //only public for JUnit testing
        return this.Answer;
    }

    //getters for controller

    public int getTurn() {
        return turncount;
    }

    public boolean getGame(){
        return gameflag;
    }

    public boolean getWin() {
        return winflag;
    }

    public int getLimit() {
        return guesslimit;
    }

    // The following are only for CLI - can be safely moved to different class if needed

    private void output() {
        System.out.println("Inplace: " + this.Inplace);
        System.out.println("Out of place: " + this.Outplace);
        System.out.println("No place (Wrong): " + this.Noplace);
        System.out.println("Unplaced: " + this.Unplace);
    }

    /*  Precondition:   printlist != null
        Postcondition:
     */
    public void CLIPrint(ArrayList<Integer> res) {
        int cpos = 0;
        for (int i : res) {
            System.out.print(printlist.get(i) + UserGuess.charAt(cpos));
            cpos++;
        }
        System.out.println(printlist.get(0));
        output();
        if (turncount >= getLimit()) {
            gameflag = false;
            System.out.println(("Game over! Guess limit of %d reached.\nThe word was : " + this.getAnswer()).formatted(guesslimit));
        }
    }
    /*  Precondition: none
        Postcondition: UserGuess != "" and UserGuess != null
     */

    public String TakeGuess() {
        Scanner Scan = new Scanner(System.in);  // Create a Scanner object
        String GuessInput = null;
        System.out.println("\nEnter Guess");
        GuessInput = Scan.nextLine().toLowerCase();
        wordaccept(GuessInput);
        while (UserGuess == "") {
            System.out.println("Word not valid! \nEnter Guess");
            GuessInput = Scan.nextLine().toLowerCase();
            wordaccept(GuessInput);
        }
        return GuessInput;
    }

    //Getters & Setters for testing

    public ArrayList<String> getUnplace() {return Unplace; }

    public String randomGuess() {
        Random rand = new Random();
        return Solution.get(rand.nextInt(Solution.size()));
    }


    public boolean InvarientTurn(){
        return (this.getTurn() < guesslimit);
    }
}



//set changed
//notifyobservers