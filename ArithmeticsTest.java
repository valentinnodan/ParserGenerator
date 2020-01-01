import org.junit.jupiter.api.Test;
import result.arithmetic.arithmeticLexicalAnalizer;
import result.arithmetic.arithmeticSyntaxAnalizer;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.testng.Assert.assertThrows;

public class ArithmeticsTest {
    @Test
    public void testTwoPlusTwo() {
        arithmeticLexicalAnalizer analizer = new arithmeticLexicalAnalizer("2+2");
        arithmeticSyntaxAnalizer sA = new arithmeticSyntaxAnalizer(analizer);

        try {
            assertEquals(4, sA.e().v, "2+2==4");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testMinusGood() {
        arithmeticLexicalAnalizer analizer = new arithmeticLexicalAnalizer("3  - 2  -1");
        arithmeticSyntaxAnalizer sA = new arithmeticSyntaxAnalizer(analizer);

        try {
            assertEquals(0, sA.e().v, "3-2-1 == 0");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testCombGood() {
        arithmeticLexicalAnalizer analizer = new arithmeticLexicalAnalizer("(5*4 + 4)/2+5");
        arithmeticSyntaxAnalizer sA = new arithmeticSyntaxAnalizer(analizer);

        try {
            assertEquals(17, sA.e().v, "(5*4 + 4)/2+5 == 17");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testWrongInput() {
        arithmeticLexicalAnalizer analizer = new arithmeticLexicalAnalizer("!1");
        arithmeticSyntaxAnalizer sA = new arithmeticSyntaxAnalizer(analizer);

        assertThrows(() -> sA.e());
    }

    @Test
    public void testSuperPuper() {
        arithmeticLexicalAnalizer analizer = new arithmeticLexicalAnalizer("(2+5*9 - 165)*(3 - 5)/4");
        arithmeticSyntaxAnalizer sA = new arithmeticSyntaxAnalizer(analizer);

        try {
            assertEquals(59, sA.e().v, "(2+5*9 - 165)*(3 - 5)/4 == 59");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMinusOneTwoThree() {
        arithmeticLexicalAnalizer analizer = new arithmeticLexicalAnalizer("1-2-3");
        arithmeticSyntaxAnalizer sA = new arithmeticSyntaxAnalizer(analizer);

        try {
            assertEquals(-4, sA.e().v, "1-2-3 == -4");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testFactFour() {
        arithmeticLexicalAnalizer analizer = new arithmeticLexicalAnalizer("4!");
        arithmeticSyntaxAnalizer sA = new arithmeticSyntaxAnalizer(analizer);

        try {
            assertEquals(24, sA.e().v, "4! = 24");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testExpFact() {
        arithmeticLexicalAnalizer analizer = new arithmeticLexicalAnalizer("(150/5-20-7)!");
        arithmeticSyntaxAnalizer sA = new arithmeticSyntaxAnalizer(analizer);

        try {
            assertEquals(6, sA.e().v, "(150/5-20-7)! == 6");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testFactInExp() {
        arithmeticLexicalAnalizer analizer = new arithmeticLexicalAnalizer("3!!");
        arithmeticSyntaxAnalizer sA = new arithmeticSyntaxAnalizer(analizer);

        try {
            assertEquals(720, sA.e().v, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
