package test.use_case;

import main.entities.Player;
import main.entities.Question;
import main.entities.QuestionList;
import main.use_case.GamePlayFunctions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static test.TestingHelperFunctions.callPrivateMethod;
import static test.TestingHelperFunctions.setPrivateVariableHelper;

// NOTE: Need Mockito to properly test this, but I can't figure it out atm. I will come back to it later.
// For now, here's a much less comprehensive set of tests.

class GamePlayFunctionsTest {
    private GamePlayFunctions gamePlayFunctions;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private InputStream originalIn;

    @BeforeEach
    void setUp() {
        gamePlayFunctions = new GamePlayFunctions();
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        originalIn = System.in;
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    void startGameTestCorrectAnswerTF(){
        QuestionList questionList = new QuestionList(1, "Any Category", "Any Difficulty", "True / False");
        ArrayList<String> wrongAnswers = new ArrayList<>(Arrays.asList("False"));
        Question question1 = new Question("?", "True", wrongAnswers, "Any Difficulty", "Any Category", "True / False");
        questionList.addQuestion(question1);

        String input = "1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        gamePlayFunctions.startGame(questionList, "Alice", "Bob");

        String output = outputStream.toString();
        assertTrue(output.contains("Correct! Your score: 1"));
    }

    @Test
    void startGameTestWrongAnswerTF(){
        QuestionList questionList = new QuestionList(1, "Any Category", "Any Difficulty", "True / False");
        ArrayList<String> wrongAnswers = new ArrayList<>(Arrays.asList("False"));
        Question question1 = new Question("?", "True", wrongAnswers, "Any Difficulty", "Any Category", "True / False");
        questionList.addQuestion(question1);

        String input = "2\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        gamePlayFunctions.startGame(questionList, "Alice", "Bob");

        String output = outputStream.toString();
        assertTrue(output.contains("Wrong! The correct answer was: "));
    }

    @Test
    void startGameTestCorrectAnswerMultiple() {
        String input = "4\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        QuestionList questionList = new QuestionList(1, "Any Category", "Any Difficulty", "Multiple Choice");
        ArrayList<String> wrongAnswers = new ArrayList<>(Arrays.asList("wrong", "wrong", "wrong"));
        Question question1 = new Question("?", "correct", wrongAnswers, "Any Difficulty", "Any Category", "Multiple Choice");
        questionList.addQuestion(question1);

        gamePlayFunctions.startGame(questionList, "Alice", "Bob", Boolean.TRUE);
        String output = outputStream.toString();

        assertTrue(output.contains("Correct! Your score: 1"));
    }

    @Test
    void startGameTestWrongAnswerMultiple() {
        String input = "1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        QuestionList questionList = new QuestionList(1, "Any Category", "Any Difficulty", "Multiple Choice");
        ArrayList<String> wrongAnswers = new ArrayList<>(Arrays.asList("wrong", "wrong", "wrong"));
        Question question1 = new Question("?", "correct", wrongAnswers, "Any Difficulty", "Any Category", "Multiple Choice");
        questionList.addQuestion(question1);

        gamePlayFunctions.startGame(questionList, "Alice", "Bob", Boolean.TRUE);
        String output = outputStream.toString();
        assertTrue(output.contains("Wrong! The correct answer was: "));
    }

    @Test
    void getResultsClearWinner() throws Exception {
        Player Alice = new Player("Alice");
        setPrivateVariableHelper(Alice, "score", 500);
        Player Bob = new Player("Bob");
        setPrivateVariableHelper(Bob, "score", 0);
        Player[] playerArray = new Player[] {Alice, Bob};

        setPrivateVariableHelper(gamePlayFunctions, "players", playerArray);
        callPrivateMethod(gamePlayFunctions, "determineWinner", new Class[]{}, new Object[]{});
        assertEquals("Alice, 500; Bob, 0; Result: Alice wins!", gamePlayFunctions.getResults());

        String output = outputStream.toString();
        assertTrue(output.contains("The winner is: Alice with a score of 500"));

    }

    @Test
    void getResultsTestTie() throws Exception {
        Player Alice = new Player("Alice");
        setPrivateVariableHelper(Alice, "score", 500);
        Player Bob = new Player("Bob");
        setPrivateVariableHelper(Bob, "score", 500);
        Player[] playerArray = new Player[] {Alice, Bob};

        setPrivateVariableHelper(gamePlayFunctions, "players", playerArray);
        callPrivateMethod(gamePlayFunctions, "determineWinner", new Class[]{}, new Object[]{});
        assertEquals("Alice, 500; Bob, 500; Result: Tie", gamePlayFunctions.getResults());

        String output = outputStream.toString();
        assertTrue(output.contains("The game is a tie!"));
    }
}
