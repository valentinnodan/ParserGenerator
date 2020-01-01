import generator.Grammar;
import generator.gen.LexicalAnalizerGenerator;
import generator.gen.SyntaxAnalizerGenerator;
import generator.gen.TokensGenerator;
import generator.inputGrammar.grammarInputLexer;
import generator.inputGrammar.grammarInputParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import result.arithmetic.arithmeticLexicalAnalizer;
import result.arithmetic.arithmeticSyntaxAnalizer;
import result.varDef.varDefLexicalAnalizer;
import result.varDef.varDefSyntaxAnalizer;

import java.io.IOException;
import java.util.ArrayList;

public class MakeGen {
    public static void main(String[] args) {
        try {
//            arithmeticLexicalAnalizer analizer = new arithmeticLexicalAnalizer("3/2*2-8*2-1");
//            arithmeticSyntaxAnalizer sA = new arithmeticSyntaxAnalizer(analizer);
//            arithmeticSyntaxAnalizer.Node curr = sA.e();
//            System.out.println(((arithmeticSyntaxAnalizer.E)curr).v);
//            curr.visualize(0, new ArrayList<>());
            varDefLexicalAnalizer analizer2 = new varDefLexicalAnalizer("int a;");
            varDefSyntaxAnalizer sA2 = new varDefSyntaxAnalizer(analizer2);
            sA2.e().visualize(0, new ArrayList<Boolean>());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
