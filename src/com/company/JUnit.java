package com.company;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;


//I'm assuming the board class allowed in the spec is referencing the view,
// but I kept all my tests to models to avoid confusion


class JUnit {
    static Model model;


    @BeforeEach //Before each was used to prevent reuisng the same model for each test,
                // using up guesses and changing the four arrays
    void initAll() throws IOException {//create and initialise the model for the tests
        this.model = new Model();
        model.initalise();
        model.setAnswer();
        assertTrue(model.InvarientTurn());
    }

    @RepeatedTest(10)//repeat the test 10 times for good coverage
    @DisplayName("Valid Answers Should be set")
    void ValidAnswerTest() {
        assertTrue(model.getAnswer().matches("[a-z]{5}"));//checks via regex that the answer is 5 members of the lowercase latin alphabet
    }

    //Test is unaffected if daisy is the correct answer,
    @RepeatedTest(5) //run the test 5 times for good coverage
    @DisplayName("No Place size reduction")
    void NoPlaceEmptiesTest() {
        String data = model.randomGuess(); //select a random valid guess
        System.setIn(new ByteArrayInputStream(data.getBytes())); //imitate user entry using SetIn
        model.TakeGuess(); //take user guess
        model.CLIPrint(model.calcTurn()); //calculate the turn as usual
        assertEquals(26-data.chars().distinct().count(),model.getUnplace().size());//Unplace should have the size of 26- the number of unique letters in the word
    }

    @Test
    @DisplayName("Final Turn Win")
    void GameWinConfirmedTest() {
        for (int i =0; i < model.getLimit()-1; i++){ //use all but one attempts
            String data = model.randomGuess(); //select a random valid guess
            //if the guess is the answer, select a new one
            while (data == model.getAnswer()) {
                data = model.randomGuess();
            }
                //set the random guess as the input
                System.setIn(new ByteArrayInputStream(data.getBytes())); //imitate user entry using SetIn
                model.TakeGuess(); //take user guess
                model.CLIPrint(model.calcTurn()); //calculate the turn as usual
        }
        //get the answer, and submit it
        String ans = model.getAnswer();
        System.setIn(new ByteArrayInputStream(ans.getBytes()));
        model.TakeGuess(); //take user guess of the answer
        model.CLIPrint(model.calcTurn()); //calculate the turn as usual
        assertTrue(model.winflag);//Check that the winflag is set to true, detecting a win.
    }

}