package com.company;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;


public class View implements Observer {
    private final Model model;
    private static Controller controller = null;
    private JFrame frame;
    private JPanel panel;
    private static String word =  "";
    private static JFrame f=new JFrame();//creating instance of JFrame


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
        Keyboard(f);
        additionalButtons(f);
        f.setSize(400,500);//400 width and 500 height
        f.setLayout(null);//using no layout managers
        f.setVisible(true);//making the frame visible
    }

    private static void displayword(){
           int x =15;
           int y = controller.getTurn();
           for (char ch: word.toCharArray()) {
               JButton b=new JButton(String.valueOf(ch));//creating instance of JButton
               b.setBounds(x,y,50, 40);//x axis, y axis, width, height
               b.addActionListener((ActionEvent e) -> {ButtonHandler(b.getText());});
               f.add(b);//adding button in JFrame
            }
           f.repaint();
           System.out.println(word);
    }

    private static void Enter(){
        if (word.length() == 5){
            controller.Enter(word);
        }
    }

    private static void Backspace(){
        if (word.length() >=1) {
            word = word.substring(0, word.length() - 1);
            displayword();
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
        b.setBounds(135,400,110, 40);//x axis, y axis, width, height
        b.addActionListener((ActionEvent e) -> {Backspace();});
        f.add(b);//adding button in JFrame
        JButton enter=new JButton("ENTER");//creating instance of JButton
        enter.setBounds(255,400,110, 40);//x axis, y axis, width, height
        enter.addActionListener((ActionEvent e) -> {Enter();});
        f.add(enter);//adding button in JFrame
    }

    private static void Keyboard(JFrame f){
        char[] alpha = new char[26];
        int xpos = 15;
        int ypos = 200;
        for(int i = 0; i < 26; i++){
            alpha[i] = (char)(65 + i);
            JButton b=new JButton(String.valueOf(alpha[i]));//creating instance of JButton
            b.setBounds(xpos,ypos,50, 40);//x axis, y axis, width, height
            b.addActionListener((ActionEvent e) -> {ButtonHandler(b.getText());});
            f.add(b);//adding button in JFrame
            if (xpos+50 >= 350){
                ypos += 50;
                xpos =15;
            }else{xpos+=60;}
        }
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}



