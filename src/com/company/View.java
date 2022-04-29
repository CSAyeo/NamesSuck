package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.List;


//contains the main method for running from the view
class GUI {
    public static void main(String[] args) throws IOException {//Main for a new CLI game
        Model model = new Model(); //initates a Model class, no controller or view required
        model.initalise(); //exectues model first run initalisation
        model.setAnswer();
        GUIGame(model); //calls the gameloop method
    }

    static void GUIGame(Model model) throws IOException {
        Controller controller = new Controller(model);
        View GUI = new View(model, controller);
    }
}

public class View implements Observer {
    private final Model model;
    private Controller controller;
    private static List<Color> printlist = Arrays.asList(Color.gray, Color.orange, Color.green); //reset, out, in
    private JFrame f=new JFrame();//creating instance of JFrame
    public JButton[] Keyboard = new JButton[26];
    private JLabel[][] DissGuess = new JLabel[6][7];
    public Color DefColour;


    public View(Model model, Controller controller)  {
        assert (model != null && controller !=null);
        this.model = model;
        model.addObserver(this);
        this.controller = controller;
        initGUI();
        controller.setView(this);
        update(model, null);
    }

    public void initGUI() {
        assert f!=null;
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        InitKeyboard(f);
        additionalButtons(f);
        f.setSize(380,(controller.getLimit()*100)+250);//380 width and (number of guesses*100) height
        f.setLayout(null);//using no layout managers
        f.setVisible(true);//making the frame visible
    }



    private void Enter() { //triggered when enter is pressed
        if (controller.getword().length() == 5) { //check the current word is 5 letters long
            controller.Enter();
        }
    }

    private void Backspace(){
        int len = controller.getword().length();
        if (len>=1) {
            f.remove(DissGuess[len][controller.getTurn()]);
            controller.removeletter();
            f.repaint();
        }

    }


    private void additionalButtons(JFrame f){
        JButton b=new JButton(String.valueOf((char)(171)));//creating instance of JButton
        b.setBounds(265,(controller.getLimit()*100)+150,40, 40);//x axis, y axis, width, height
        b.setMargin(new Insets(0, 0, 0, 0));
        b.addActionListener((ActionEvent e) -> {Backspace();});
        DefColour = b.getBackground();
        f.add(b);//adding button in JFrame
        JButton enter=new JButton(String.valueOf((char) 187));//creating instance of JButton
        enter.setMargin(new Insets(0, 0, 0, 0));
        enter.setBounds(315,(controller.getLimit()*100)+150,40, 40);//x axis, y axis, width, height
        enter.addActionListener((ActionEvent e) -> {Enter();});
        f.add(enter);//adding button in JFrame
    }

    private void InitKeyboard(JFrame f){
        char alpha;
        int xpos = 15;
        //f.getHeight isnt usable here as dynamic values can only be read after drawing is finished.
        // Rather than stop draw and resume or overwrite the method I chose to use guesslimit again
        int ypos = (controller.getLimit()*100);
        int c = 0;
        for(int i = 65; i < 91; i++){
            alpha = (char)(i);
            JButton b=new JButton(String.valueOf(alpha));//creating instance of JButton
            b.setBounds(xpos,ypos,40, 40);//x axis, y axis, width, height
            b.addActionListener((ActionEvent e) -> {ButtonHandler(b.getText());});
            b.setRolloverEnabled(false);
            b.setFont(new Font("Arial", Font.BOLD, 10));
            b.setBorderPainted( false );
            b.setMargin(new Insets(0, 0, 0, 0));
            Keyboard[c] = b;
            f.add(Keyboard[c]);//adding button in JFrame
            if (xpos+50 >= 350){
                ypos += 50;
                xpos =15;
            }else{xpos+=50;}
            c++;
        }
    }


    public void clearWords() { //remove all the labels displaying current words
        controller.dropword();
        for (JLabel[] j : DissGuess) {
            for (JLabel k : j) {
                if (!(k ==null)) {
                    f.remove(k);
                }
            }
        }
    }

    void addNewGame(){
        JButton b = new JButton("New Game");//creating instance of JButton
        b.setMargin(new Insets(0, 0, 0, 0));
        b.setBounds(15, (controller.getLimit()*100)-50, 340, 30);//x axis, y axis, width, height
        b.addActionListener((ActionEvent e) -> {NewGame(b);
        });
        f.add(b);
        f.repaint();
    }


    void ToggleButtons(){ //set the background to default (remove colour), and toggle button access
        for(JButton k : Keyboard){
            k.setBackground(DefColour);
            k.setEnabled(!k.isEnabled());
        }
    }
    void ClearButtons(){
        for(JButton k : Keyboard){
            k.setBackground(DefColour);
        }
    }

    //listeners

    public void NewGame(JButton b) {
        controller.newGame();
        f.remove(b);
        f.repaint();
    }

    private void ButtonHandler(String letter){
        if (controller.getword().length() < 5) {
            controller.addletter(letter);
            displayword();
        }
    }

    //functions

    private void displayword(){ //display the letter entered, triggered after button press
        int wl = controller.getword().length();
        int x =15 + (50 * wl-1);
        int y = controller.getTurn();
        DissGuess[wl][y] = new JLabel();
        DissGuess[wl][y].setOpaque(true);
        DissGuess[wl][y].setHorizontalAlignment(SwingConstants.CENTER);
        DissGuess[wl][y].setVerticalAlignment(SwingConstants.CENTER);
        DissGuess[wl][y].setText(String.valueOf(controller.getword().charAt(wl-1)));
        DissGuess[wl][y].setBounds(x,y*50,50, 40);
        f.add(DissGuess[wl][y]);
        f.repaint();
    }

    //Update is called when observer is triggered - submission of a complete matching word, colouring letter and keyboard
    @Override
    public void update(Observable o, Object arg) {
        if (controller.getword() !="") {
            int y = controller.getTurn() - 1;
            for (int i = 1; i < 6; i++) {
                try {
                    int score = controller.Calcturn().get(i - 1);
                    Color scorecolour = printlist.get(score);
                    DissGuess[i][y].setBackground(scorecolour);
                    int charpos = (int) controller.getword().charAt(i - 1) - 65; //65
                    if (!(Keyboard[charpos].getBackground() == Color.green)) {
                        Keyboard[charpos].setBackground(scorecolour);
                    }
                } catch (Exception e) {
                    System.out.println("COLOUR BAD" + e);
                }
            }
            controller.dropword();
            controller.EndHandler();
        }
    }
}



