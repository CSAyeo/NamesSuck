package com.company;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.Key;
import java.util.*;
import java.util.List;

public class View implements Observer {
    private final Model model;
    private static Controller controller = null;
    private static String word =  "";
    private static List<Color> printlist = Arrays.asList(Color.gray, Color.orange, Color.green); //reset, out, in
    private static JFrame f=new JFrame();//creating instance of JFrame
    private static JButton[] Keyboard = new JButton[26];
    private static JLabel[][] DissGuess = new JLabel[6][7];
    private static Color DefColour;


    public View(Model model, Controller controller)  {
        this.model = model;
        model.addObserver(this);
        this.controller = controller;
        initGUI();
        controller.setView(this);
        update(model, null);
    }

    public static void initGUI() {
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        InitKeyboard(f);
        additionalButtons(f);
        f.setSize(380,600);//400 width and 500 height
        f.setLayout(null);//using no layout managers
        f.setVisible(true);//making the frame visible
    }



    private static void displayword(){
        int x =15 + (50 * word.length()-1);
        int y = controller.getTurn();
        int temp = word.length() -1;
        DissGuess[word.length()][y] = new JLabel();
        DissGuess[word.length()][y].setOpaque(true);
        DissGuess[word.length()][y].setHorizontalAlignment(SwingConstants.CENTER);
        DissGuess[word.length()][y].setVerticalAlignment(SwingConstants.CENTER);
        DissGuess[word.length()][y].setText(String.valueOf(word.charAt(temp)));
        DissGuess[word.length()][y].setBounds(x,y*50,50, 40);
        f.add(DissGuess[word.length()][y]);
        f.repaint();
    }



    private static void Enter(JButton enter) {
        if (word.length() == 5) {
            controller.Enter(word);
        }
    }

    private static void Backspace(){
        if (word.length() >=1) {
            int temp = word.length();
            f.remove(DissGuess[temp][controller.getTurn()]);
            word = word.substring(0, word.length() - 1);
            f.repaint();
        }

    }

    private static void ButtonHandler(String letter){
        if (word.length() < 5){
            word = word + letter;
            displayword();
        }
    }

    private static void additionalButtons(JFrame f){
        JButton b=new JButton(String.valueOf((char)(171)));//creating instance of JButton
        b.setBounds(265,500,40, 40);//x axis, y axis, width, height
        b.setMargin(new Insets(0, 0, 0, 0));
        b.addActionListener((ActionEvent e) -> {Backspace();});
        DefColour = b.getBackground();
        f.add(b);//adding button in JFrame
        JButton enter=new JButton(String.valueOf((char) 187));//creating instance of JButton
        enter.setMargin(new Insets(0, 0, 0, 0));
        enter.setBounds(315,500,40, 40);//x axis, y axis, width, height
        enter.addActionListener((ActionEvent e) -> {Enter(enter);});
        f.add(enter);//adding button in JFrame
    }

    private static void InitKeyboard(JFrame f){
        char[] alpha = new char[26];
        int xpos = 15;
        int ypos = 350;
        int c = 0;
        for(int i = 0; i < 26; i++){
            alpha[i] = (char)(65 + i);
            JButton b=new JButton(String.valueOf(alpha[i]));//creating instance of JButton
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

    private static void ToggleButtons(){
        for(JButton k : Keyboard){
            k.setBackground(DefColour);
            k.setEnabled(!k.isEnabled());
        }
    }
    private static void ClearButtons(){
        for(JButton k : Keyboard){
            k.setBackground(DefColour);
        }
    }
    private static void clearWords() {
        word = "";
        for (JLabel[] j : DissGuess) {
            for (JLabel k : j) {
                if (!(k ==null)) {
                    f.remove(k);
                }
            }
        }
    }

    private static void NewGame(JButton b) {
        controller.newGame();
        clearWords();
        if (!Keyboard[0].isEnabled()){
            ToggleButtons();
        }else{ClearButtons();}
        f.remove(b);
        f.repaint();
    }

    private static void EndHandler(){
        if (controller.getTurn()==1) {
            JButton b = new JButton("New Game");//creating instance of JButton
            b.setMargin(new Insets(0, 0, 0, 0));
            b.setBounds(15, 300, 340, 30);//x axis, y axis, width, height
            b.addActionListener((ActionEvent e) -> {
                NewGame(b);
            });
            f.add(b);
            f.repaint();
        }
        if (controller.getTurn()>=6 || controller.getWin()){
            ToggleButtons();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println(word=="");
        if (word !="") {
            int y = controller.getTurn() - 1;
            for (int i = 1; i < 6; i++) {
                try {
                    int score = controller.Calcturn(word).get(i - 1);
                    Color scorecolour = printlist.get(score);
                    DissGuess[i][y].setBackground(scorecolour);
                    int charpos = (int) word.charAt(i - 1) - 65; //65
                    if (!(Keyboard[charpos].getBackground() == Color.green)) {
                        Keyboard[charpos].setBackground(scorecolour);
                    }
                } catch (Exception e) {
                    System.out.println("COLOUR BAD" + e);
                }
            }
            word = "";
            EndHandler();
        }
    }
}



