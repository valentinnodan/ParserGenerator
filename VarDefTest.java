
import org.junit.jupiter.api.Test;
import result.varDef.varDefLexicalAnalizer;
import result.varDef.varDefSyntaxAnalizer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testng.Assert.assertThrows;

public class VarDefTest {

    @Test
    public void testIntAGood() {
        varDefLexicalAnalizer analizer = new varDefLexicalAnalizer("int a;");
        varDefSyntaxAnalizer sA = new varDefSyntaxAnalizer(analizer);

        assertDoesNotThrow(()->sA.e());
    }
    @Test
    public void testIntABad() {
        varDefLexicalAnalizer analizer = new varDefLexicalAnalizer("inta;");
        varDefSyntaxAnalizer sA = new varDefSyntaxAnalizer(analizer);

        assertThrows(()->sA.e());
    }
    @Test
    public void testPointersGood() {
        varDefLexicalAnalizer analizer = new varDefLexicalAnalizer("int ****a;");
        varDefSyntaxAnalizer sA = new varDefSyntaxAnalizer(analizer);

        assertDoesNotThrow(()->sA.e());
    }
    @Test
    public void testPointersBad() {
        varDefLexicalAnalizer analizer = new varDefLexicalAnalizer("int **a*;");
        varDefSyntaxAnalizer sA = new varDefSyntaxAnalizer(analizer);

        assertThrows(()->sA.e());
    }
    @Test
    public void testManyGood() {
        varDefLexicalAnalizer analizer = new varDefLexicalAnalizer("char *a, b, *****c;");
        varDefSyntaxAnalizer sA = new varDefSyntaxAnalizer(analizer);

        assertDoesNotThrow(()->sA.e());
    }
    @Test
    public void testManyBad() {
        varDefLexicalAnalizer analizer = new varDefLexicalAnalizer("bool a, b; baaaad");
        varDefSyntaxAnalizer sA = new varDefSyntaxAnalizer(analizer);

        assertThrows(()->sA.e());
    }
    @Test
    public void testManyEntersAndWhitespaces() {
        varDefLexicalAnalizer analizer = new varDefLexicalAnalizer("bool\t \na, \rb; \n");
        varDefSyntaxAnalizer sA = new varDefSyntaxAnalizer(analizer);

        assertDoesNotThrow(()->sA.e());
    }
    @Test
    public void testTypoInType() {
        varDefLexicalAnalizer analizer = new varDefLexicalAnalizer("bolo a, ***b;");
        varDefSyntaxAnalizer sA = new varDefSyntaxAnalizer(analizer);

        assertThrows(()->sA.e());
    }
}